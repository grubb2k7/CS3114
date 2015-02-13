// On my honor:
//
// - I have not discussed the Java language code in my program with
// anyone other than my instructor or the teaching assistants
// assigned to this course.
//
// - I have not used Java language code obtained from another student,
// or any other unauthorized source, either modified or unmodified.
//
// - If any Java language code or documentation used in my program
// was obtained from another source, such as a text book or course
// notes, that has been clearly noted with a proper citation in
// the comments of my program.
//
// - I have not designed this program in such a way as to defeat or
// interfere with the normal operation of the Curator System.
//
// Jonathan D. Grubb

/**
 * @author grubb2k7 (Jonathan D. Grubb)
 * Project2
 * Last Modified: 02/12/15
 */

// The test harness will belong to the following package; the BST
// implementation will belong to it as well.  In addition, the BST
// implementation will specify package access for the inner node class
// and all data members in order that the test harness may have access
// to them.
//
package Minor.P2.DS;

// BST<> provides a generic implementation of a binary search tree
//
// BST<> implementation constraints:
//   - The tree uses package access for root, and for the node type.
//   - The node type uses package access for its data members.
//   - The tree never stores two objects for which compareTo() returns 0.
//   - All tree traversals are performed recursively.
//   - Optionally, the BST<> employs a pool of deleted nodes.
//     If so, when an insertion is performed, a node from the pool is used 
//     unless the pool is empty, and when a deletion is performed, the
//     (cleaned) deleted node is added to the pool, unless the pool is
//     full.  The maximum size of the pool is set via the constructor.
//
// User data type (T) constraints:
//   - T implements compareTo() and equals() appropriately
//   - compareTo() and equals() are consistent; that is, compareTo()
//     returns 0 in exactly the same situations equals() returns true
//
public class BST<T extends Comparable<? super T>> {
	
    class BinaryNode {
       // Initialize a childless binary node.
       // Pre:   elem is not null
       // Post:  (in the new node)
       //        element == elem
       //        left == right == null 
       public BinaryNode( T elem ) {
    	   //Return if the element given is null
    	   if(elem == null) return;
    	   element = elem;
    	   left = null;
    	   right = null;
       }
       // Initialize a binary node with children.
       // Pre:   elem is not null
       // Post:  (in the new node)
       //        element == elem
       //        left == lt, right == rt 
       public BinaryNode( T elem, BinaryNode lt, BinaryNode rt ) {
    	   if(elem == null) return;
    	   element = elem;
    	   left = lt;
    	   right = rt;
       }

       T          element;  // the data in the node
       BinaryNode left;     // pointer to the left child
       BinaryNode right;    // pointer to the right child
    }

    BinaryNode 	root;       // pointer to root node, if present
    BinaryNode 	pool;       // pointer to first node in the pool
    int        	pSize;      // size limit for node pool
    private int	pCurSize;	// current pool size;

    // Initialize empty BST with no node pool.
    // Pre:   none
    // Post:  (in the new tree)
    //        root == null, pool == null, pSize = 0
    public BST( ) {
    	root	 = null;
    	pool	 = null;
    	pSize	 = 0;
    	pCurSize = 0;
    }

    // Initialize empty BST with a node pool of up to pSize nodes.
    // Pre:   none
    // Post:  (in the new tree)
    //        root == null, pool = null, pSize == Sz 
    public BST( int Sz ) {
    	root	 = null;
    	pool	 = null;
    	pSize	 = Sz;
    	pCurSize = 0;
    }

    // Return true iff BST contains no nodes.
    // Pre:   none
    // Post:  the binary tree is unchanged
    public boolean isEmpty( ) {
    	return root == null;
    }
    
    private BinaryNode findNode(T x, BinaryNode sRoot) {
    	if(sRoot == null || x == null) return null;
    	
    	int compareResult = x.compareTo(sRoot.element);
    	
    	if(compareResult < 0)	   return findNode(x, sRoot.left);
    	else if(compareResult > 0) return findNode(x, sRoot.right);
    	
    	return sRoot;
    }
    
    /**
     * Recursively inserts node into the BST's pool if there is room
     * for insertion.  Everything is inserted in order, the lowest node
     * according to compareTo() is at the beginning and the highest node
     * is at the end. The finished result is a complete link of nodes in the
     * pool structure if there is available space.
     * @param node	the node that needs to be inserted.
     * @param sRoot	the node of the pool that node compares to. 
     */
    private BinaryNode insertPoolNode(BinaryNode node, BinaryNode sRoot) {
    	if(node == null || pCurSize == pSize) return sRoot;
    	//Case 1: if the pool is empty
    	if(sRoot == null) {
    		pCurSize++;
    		node.left = null;
    		node.right = null;
    		return node;
    	}
    	//Case 2:	if the node can fit in between the elements
    	//			(assuming pool already has a node)
    	else if(sRoot.right != null) {
    		int compareResult1 = node.element.compareTo(sRoot.element);
        	int compareResult2 = node.element.compareTo(sRoot.right.element);
        	//if in between insert into pool
        	if(compareResult1 > 0 && compareResult2 < 0) {
        		node.right = sRoot.right;
        		node.left = null;
        		sRoot.right = node;
        		pCurSize++;
        	}
        	else sRoot.right = insertPoolNode(node, sRoot.right);
    	}
    	//Case 3:	if we are at the end of the pool
    	else {
    		sRoot.right = node;
    		node.right = null;
    		node.left = null;
    		pCurSize++;
    	}
    	return sRoot;
    }
    
    /**
     * Recursively goes through the pool structure using sRoot as the
     * current node that compares the value of x with. Returns the node
     * that was in the pool, or null if that node was there. 
     */
    private BinaryNode retrievePoolNode(T x, BinaryNode sRoot) {
    	if(x == null || sRoot == null) return null;
    	
    	int compareResult1 = x.compareTo(sRoot.element);
    	int compareResult2 = (sRoot.right != null) ?
    			x.compareTo(sRoot.right.element) : -1;
    	
    	//Case1: if sRoot is the first node in the pool
    	if(sRoot == pool && compareResult1 == 0) {
    		pool = sRoot.right;
    		sRoot.right = null;
    		pCurSize--;
    		return sRoot;
    	}
    	//Case2: if sRoot.right is equivalent to x re-link pool
    	//		 and return sRoot.right. compareResult2 == 0
    	//		 means that a right node exists.
    	else if(compareResult2 == 0) {
    		BinaryNode tempNode = sRoot.right;
    		sRoot.right = tempNode.right;
    		tempNode.right = null;
    		pCurSize--;
    		return tempNode;
    	}
    	else {
    		return retrievePoolNode(x, sRoot.right);
    	}
    }

    // Return pointer to matching data element, or null if no matching
    // element exists in the BST.  "Matching" should be tested using the
    // data object's compareTo() method.
    // Pre:  x is null or points to a valid object of type T
    // Post: the binary tree is unchanged
    public T find( T x ) {
    	if(x == null) return null;
    	
    	BinaryNode temp = findNode(x, root);
    	if(temp != null) return temp.element;
    	
    	return null;
    }
    
    // Insert element x into BST, unless it is already stored.  Return true
    // if insertion is performed and false otherwise.
    // Pre:   x is null or points to a valid object of type T
    // Post:  the binary tree contains x
    public boolean insert( T x ) {
    	if(x == null || find(x)!=null) return false;
    	
    	BinaryNode poolNode = retrievePoolNode(x, pool);
    	
    	if(poolNode != null) root = insert(poolNode, root);
    	else 				 root = insert(x, root);
    	
    	return true;
    }
    
    /**
     * Recursive function to help insert a new node with the value of x
     * @param x		new node to be inserted
     * @param sRoot	a node that is already a part of the tree
     * @return		BinaryNode that was is in the tree
     */
    private BinaryNode insert(T x, BinaryNode sRoot) {
    	if(sRoot == null) {
    		return new BinaryNode(x);
    	}
    	
    	int compareResult = x.compareTo(sRoot.element);
    	
    	if(compareResult < 0)	sRoot.left = insert(x, sRoot.left);
    	if(compareResult > 0)	sRoot.right = insert(x, sRoot.right);
    	
    	return sRoot;
    }
    
    /**
     * Recursive function to help insert an existing node into the BST
     * @param x		existing node
     * @param sRoot	a node that is already a part of the tree
     * @return		Binary node that was is in the tree.
     */
    private BinaryNode insert(BinaryNode x, BinaryNode sRoot) {
    	if(sRoot == null) {
    		return x;
    	}
    	
    	int compareResult = x.element.compareTo(sRoot.element);
    	
    	if(compareResult < 0)	sRoot.left = insert(x, sRoot.left);
    	if(compareResult > 0)	sRoot.right = insert(x, sRoot.right);
    	
    	return sRoot;
    }
    
    // Delete element matching x from the BST, if present.  Return true if
    // matching element is removed from the tree and false otherwise.
    // Pre:   x is null or points to a valid object of type T
    // Post:  the binary tree does not contain x
    public boolean remove( T x ) {
    	if(x == null) return false;
    	root = remove(x, root);
    	
    	if(root == null) return false;
    	else 			 return true;
    }
    
    /**
     * Recursive function that searches for a node with value x and removes
     * it appropriately keeping the BST in proper structure.
     * @param x			The value within the node that needs to be removed
     * @param sRoot		a node that is already a part of the tree
     * @return			A BinaryNode within the tree causing with the node of value x
     * 					removed from the tree.
     */
    private BinaryNode remove(T x, BinaryNode sRoot) {
    	if(sRoot == null) return sRoot;
    	
    	BinaryNode returnNode = sRoot;
    	int compareResult = x.compareTo(sRoot.element);
    	
    	if(compareResult < 0)	   sRoot.left = remove(x, sRoot.left);
    	else if(compareResult > 0) sRoot.right = remove(x, sRoot.right);
    	
    	//if sRoot is the node we are looking for
    	else {
	    	//Case 1: no leaves attached
	    	if(sRoot.right == null && sRoot.left == null) returnNode = null;
			//Case 2: 1 leaf
			else if(sRoot.right == null ^ sRoot.left == null) {
				if(sRoot.right == null)	returnNode = sRoot.left;
				else					returnNode = sRoot.right;
			}
			//Case 3: 2 leaves
			else {
				BinaryNode rMinParent = findRMinParent(sRoot.right);
				
				if(rMinParent.left == null) {
					returnNode = rMinParent;
					returnNode.left = sRoot.left;
				}
				else {
					returnNode = rMinParent.left;
					
					//Replacing the left most node with it's right most node
					//so the tree does not lose any information.
					rMinParent.left  = returnNode.right;
					returnNode.left	 = sRoot.left;
					returnNode.right = sRoot.right;
				}
			}
	    	
	    	pool = insertPoolNode(sRoot, pool);
    	}
    	
		return returnNode;
    }
    
    /**
     * Helper function to find the smallest valued parent node within a tree
     * @param sRoot	The node that needs further inspection
     * @return The parent node that contains the smallest valued node in the tree
     */
    private BinaryNode findRMinParent(BinaryNode sRoot) {    	
    	if(sRoot.left == null) return sRoot;
    	
    	BinaryNode sRootL = sRoot.left;
    	
    	if(sRootL.left == null) {
    		return sRoot;
    	}
    	else return findRMinParent(sRootL);
    }

    // Remove from the tree all values y such that y > x, according to
    // compareTo().
    // Pre:   x is null or points to a valid object of type T
    // Post:  if the tree contains no value y such that compareTo()
    //           indicates y > x
    public void cap( T x ) {
    	if(x == null) return;
    	
    	BinaryNode targetNode = findNode(x, root);
    	if(targetNode == null) return;
    	targetNode.right	= null;
    }    

    // Return the tree to an empty state.
    // Pre:   none
    // Post:  the binary tree contains no elements
    public void clear( ) {
    	root = null;
    }

    // Return true iff other is a BST that has the same physical structure
    // and stores equal data values in corresponding nodes.  "Equal" should
    // be tested using the data object's equals() method.
    // Pre:   other is null or points to a valid BST<> object, instantiated
    //           on the same data type as the tree on which equals() is invoked
    // Post:  both binary trees are unchanged
    @SuppressWarnings("unchecked")
	public boolean equals(Object other) {
    	if(other == null || !this.getClass().equals(other.getClass())) return false;
    	
    	return equals(root, ((BST<T>)other).root);
    }

    /**
     * Recursive function that equally traverses two different trees at the same
     * time in the same direction determining whether or not each node is equivalent
     * according to the equals() method.
     * @param sRoot1	node from Tree1
     * @param sRoot2	node from Tree2
     * @return			true if the trees are equivalent in structure and values, false
     * 					if it is not equivalent
     */
    private boolean equals(BinaryNode sRoot1, BinaryNode sRoot2) {
    	if(sRoot1 == null && sRoot2 == null)	 return true;
    	else if(sRoot1 == null ^ sRoot2 == null) return false;
    	
    	if(!sRoot1.element.equals(sRoot2.element)) return false; 
    	if(!equals(sRoot1.left, sRoot2.left)) return false;
    	if(!equals(sRoot1.right, sRoot2.right)) return false;
    	
    	return true;
    }
    
    // Return number of levels in the tree.  (An empty tree has 0 levels.)
    // Pre:   tree is a valid BST<> object
    // Post:  the binary tree is unchanged
    public int levels() {
    	return findMaxlevel(0, 0, root);
    }
    
    /**
     * Recursively goes through the tree keeping track of the max level found.
     * @param cur	The current level the function call is at within the BST
     * @param max	The current max level found as recursively calling the function
     * @param sRoot The root to the binary tree
     * @return		The max amount of levels within the tree
     */
    private int findMaxlevel(int cur, int max, BinaryNode sRoot) {
    	if(sRoot == null) return max;
    	
    	cur++;
    	max = (cur > max) ? cur : max;
    	max = findMaxlevel(cur, max, sRoot.left);
    	return findMaxlevel(cur, max, sRoot.right);    	
    }
}
