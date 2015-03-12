package Minor.P3.DS;
import java.io.FileWriter;
import java.util.Vector;
   
// The test harness will belong to the following package; the quadtree
// implementation must belong to it as well.  In addition, the quadtree
// implementation must specify package access for the node types and tree
// members so that the test harness may have access to it.
//

public class prQuadTree< T extends Compare2D<? super T> > {
   
   // You must use a hierarchy of node types with an abstract base
   // class.  You may use different names for the node types if
   // you like (change displayHelper() accordingly).
   abstract class prQuadNode { }

   class prQuadLeaf extends prQuadNode {
      Vector<T> Elements;
      
      public prQuadLeaf() { 
    	  Elements = new Vector<T>();
      }
   }

   class prQuadInternal extends prQuadNode {
      prQuadNode NW, NE, SE, SW;
      
      //Not calling it's super because internal nodes don't hold
      //data just other internals or the leafs
      public prQuadInternal() {
    	  NW = null;
    	  NE = null;
    	  SW = null;
    	  SE = null;
      }
   }
   
   //QuadNode because can be a leaf or a internal node
   prQuadNode root;
   long xMin, xMax, yMin, yMax;
   
   // Initialize quadtree to empty state, representing the specified region.
   public prQuadTree(long xMin, long xMax, long yMin, long yMax) {
	   this.xMin = xMin;
	   this.xMax = xMax;
	   this.yMin = yMin;
	   this.yMax = yMax;
	   
	   root = null;
   }
    
   // Pre:   elem != null
   // Post:  If elem lies within the tree's region, and elem is not already 
   //        present in the tree, elem has been inserted into the tree.
   // Return true iff elem is inserted into the tree. 
   public boolean insert(T elem) {
	  //Checking if elem is null and making sure elem is within bounds
	  if(elem == null || elem.getX() > xMax || elem.getX() < xMin ||
			  elem.getY() > yMax || elem.getY() < yMin) {
		  
		  return false;
	  }
	  
	  root = insert(elem, root, xMin, xMax, yMin, yMax);
	  
      return true;
   }
   
   @SuppressWarnings("unchecked")
private prQuadNode insert(T elem, prQuadNode sRoot, long xMin, long xMax,
							long yMin, long yMax) {
	   //If there is no node here I can suspect that it is suitable to
	   //insert the element here
	   if(sRoot == null) {
		   sRoot = new prQuadLeaf();
		   ((prQuadLeaf)sRoot).Elements.add(elem);
	   }
	   
	   //Checking to see if this node is a prQuadInternal node
	   //If it is we are going to be traversing further into the tree
	   //to find the ending node.
	   else if(sRoot.getClass().equals(prQuadInternal.class)) {
		   sRoot = insertCorrectPath(elem, null, sRoot, xMin, xMax,
				   					yMin, yMax);
	   }
	   else if(sRoot.getClass().equals(prQuadLeaf.class)) {
		   //Acquiring the original Leaf
		   prQuadLeaf sRootLeaf = (prQuadLeaf)sRoot;
		   T leafElem = sRootLeaf.Elements.elementAt(0);
		   
		   if(leafElem.equals(elem)) return sRoot;
		   
		   sRoot = new prQuadInternal();
		   prQuadNode curInternalNode = sRoot;
		   
		   //Preparing for the continuous splitting if necessary
		   long xOrgn = (xMax + xMin) / 2;
		   long yOrgn = (yMax + yMin) / 2;
		   
		   //Continuously splitting until each element can be in their own quadrant
		   while(leafElem.directionFrom(xOrgn, yOrgn) == elem.directionFrom(xOrgn, yOrgn)) {
			   switch(leafElem.directionFrom(xOrgn, yOrgn)) {
			   case NE:
			   case NOQUADRANT: {
				   xMin  = xOrgn;
				   yMin  = yOrgn;
				   xOrgn = (xMax + xOrgn) / 2;
				   yOrgn = (yMax + yOrgn) / 2;
				   ((prQuadInternal)curInternalNode).NE = new prQuadInternal();
				   curInternalNode = ((prQuadInternal)curInternalNode).NE;
				   break;
			   }
			   case NW: {
				   xMax  = xOrgn;
				   yMin  = yOrgn;
				   xOrgn = (xMin + xOrgn) / 2;
				   yOrgn = (yMax + yOrgn) / 2;
				   ((prQuadInternal)curInternalNode).NW = new prQuadInternal();
				   curInternalNode = ((prQuadInternal)curInternalNode).NW;
				   break;
			   }
			   case SW: {
				   xMax  = xOrgn;
				   yMax  = yOrgn;
				   xOrgn = (xMin + xOrgn) / 2;
				   yOrgn = (yMin + yOrgn) / 2;
				   ((prQuadInternal)curInternalNode).SW = new prQuadInternal();
				   curInternalNode = ((prQuadInternal)curInternalNode).SW;
				   break;
			   }
			   case SE: {
				   xMin  = xOrgn;
				   yMax  = yOrgn;
				   xOrgn = (xMax + xOrgn) / 2;
				   yOrgn = (yMin + yOrgn) / 2;
				   ((prQuadInternal)curInternalNode).SE = new prQuadInternal();
				   curInternalNode = ((prQuadInternal)curInternalNode).SE;
				   break;
			   }
			   }
		   }
		   curInternalNode = insertCorrectPath(null, sRootLeaf, curInternalNode,
				   								xMin, xMax, yMin, yMax);
		   curInternalNode = insertCorrectPath(elem, null, curInternalNode,
				   								xMin, xMax, yMin, yMax);
	   }
	   
	   return sRoot;	   
   }
   
   	@SuppressWarnings("unchecked")
   private prQuadNode insertCorrectPath(T elem, prQuadLeaf leaf, prQuadNode sRoot, long xMin,
		   								long xMax, long yMin, long yMax) {
	   //Calculating the x and y origin
	   long xOrgn = (xMax + xMin) / 2;
	   long yOrgn = (yMax + yMin) / 2;
	   
	   prQuadInternal sRootNode = (prQuadInternal)sRoot;
	   //If we are inserting an elem
	   if(elem != null) {
		   
		   //Seeing where elem should go and traverse in the proper quadrant
		   switch(elem.directionFrom(xOrgn, yOrgn)) {
		   case NE:
		   case NOQUADRANT: {
			   sRootNode.NE = insert(elem, sRootNode.NE, xOrgn, xMax, yOrgn, yMax);
			   break;
		   }
		   case NW: {
			   sRootNode.NW = insert(elem, sRootNode.NW, xMin, xOrgn, yOrgn, yMax);
			   break;
		   }
		   case SW: {
			   sRootNode.SW = insert(elem, sRootNode.SW, xMin, xOrgn, yMin, yOrgn);
			   break;
		   }
		   case SE: {
			   sRootNode.SE = insert(elem, sRootNode.SE, xOrgn, xMax, yMin, yOrgn);
			   break;
		   }
		   }
	   }
	   else if(leaf != null){
		   switch(leaf.Elements.get(0).directionFrom(xOrgn, yOrgn)) {
		   case NE:
		   case NOQUADRANT: {
			   sRootNode.NE = leaf;
			   break;
		   }
		   case NW: {
			   sRootNode.NW = leaf;
			   break;
		   }
		   case SW: {
			   sRootNode.SW = leaf;
			   break;
		   }
		   case SE: {
			   sRootNode.SE = leaf;
			   break;
		   }
		   }
	   }
	   return sRoot;
   }

    
   // Pre:  elem != null
   // Post: If elem lies in the tree's region, and a matching element occurs
   //       in the tree, then that element has been removed.
   // Returns true iff a matching element has been removed from the tree.
   public boolean delete(T Elem) {
      return false;
   }

   // Pre:  elem != null
   // Returns reference to an element x within the tree such that 
   // elem.equals(x)is true, provided such a matching element occurs within
   // the tree; returns null otherwise.
   public T find(T Elem) {
      return null;
   }
 
   // Pre:  xLo, xHi, yLo and yHi define a rectangular region
   // Returns a collection of (references to) all elements x such that x is 
   //in the tree and x lies at coordinates within the defined rectangular 
   // region, including the boundary of the region.
   public Vector<T> find(long xLo, long xHi, long yLo, long yHi) {
      return null;      
   }
   
   public void printTree() {
	   printTreeHelper(root, "");
   }
   
   	@SuppressWarnings("unchecked")
   private void printTreeHelper(prQuadNode sRoot, String Padding) {
   		if(sRoot == null) {
   			System.out.println(Padding + "*\n");
   			return;
		}
		if ( sRoot.getClass().equals(prQuadInternal.class) ) {
		   prQuadInternal p = (prQuadInternal) sRoot;
		   printTreeHelper(p.SW, Padding + "    ");
		   printTreeHelper(p.SE, Padding + "    ");
	   }
	   // Display indentation padding for current node
	   System.out.println(Padding);
	   // Determine if at leaf or internal and display accordingly
	   if ( sRoot.getClass().equals(prQuadLeaf.class) ) {
		   prQuadLeaf p = (prQuadLeaf) sRoot;
		   System.out.println(Padding + p.Elements.get(0).toString());
	   }
	   else
		   System.out.println(Padding + "@\n");
	   if ( sRoot.getClass().equals(prQuadInternal.class) ) {
		   prQuadInternal p = (prQuadInternal) sRoot;
		   printTreeHelper(p.NE, Padding + "    ");
		   printTreeHelper(p.NW, Padding + "    ");
	   }
   }
}
