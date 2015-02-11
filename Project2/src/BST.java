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
    	return root == null || (root.left == null && root.right == null);
    }
    
    private BinaryNode findNode(T x, BinaryNode sRoot) {
    	if(sRoot == null || x == null) return null;
    	
    	int compareResult = x.compareTo(sRoot.element);
    	
    	if(compareResult < 0)	   return findNode(x, sRoot.left);
    	else if(compareResult > 0) return findNode(x, sRoot.right);
    	else					   return sRoot;
    }
    
    private void insertNodePool(BinaryNode node, BinaryNode sRoot) {
    	if(node == null || pCurSize == pSize) return;
    	//Case 1: if the pool is empty
    	if(sRoot == null) {
    		sRoot = node;
    		pCurSize++;
    		node.left = null;
    		node.right = null;
    	}
    	//Case 2:	if the node can fit in between the elements
    	//			(assuming pool already has a node)
    	else if(sRoot.right != null) {
    		int compareResult1 = node.element.compareTo(sRoot.element);
        	int compareResult2 = node.element.compareTo(sRoot.right.element);
        	if(compareResult1 > 0 && compareResult2 < 0) {
        		node.right = sRoot.right;
        		node.left = null;
        		sRoot.right = node;
        	}
        	else insertNodePool(node, sRoot.right);
        	pCurSize++;
    	}
    	//Case 3:	if we are at the end of the pool
    	else {
    		sRoot.right = node;
    		node.right = null;
    		node.left = null;
    		pCurSize++;
    	}
    	return;
    }
    
    private BinaryNode retrieveNodePool(T x, BinaryNode sRoot) {
    	if(x == null || sRoot == null) return null;
    	
    	int compareResult1 = x.compareTo(sRoot.element);
    	int compareResult2 = (sRoot.right != null) ?
    			x.compareTo(sRoot.right.element) : -1;
    	
    	//Case1: if sRoot is the first node in the pool
    	if(sRoot == pool && compareResult1 == 0) {
    		pool = sRoot.right;
    		sRoot.right = null;
    		return sRoot;
    	}
    	//Case2: if sRoot.right is equivalent to x re-link pool
    	//		 and return sRoot.right. compareResult2 == 0
    	//		 means that a right node exists.
    	else if(compareResult2 == 0) {
    		BinaryNode tempNode = sRoot.right;
    		sRoot.right = tempNode.right;
    		tempNode.right = null;
    		return tempNode;
    	}
    	else {
    		return retrieveNodePool(x, sRoot.right);
    	}
    }

    // Return pointer to matching data element, or null if no matching
    // element exists in the BST.  "Matching" should be tested using the
    // data object's compareTo() method.
    // Pre:  x is null or points to a valid object of type T
    // Post: the binary tree is unchanged
    public T find( T x ) {
    	if(x == null) return null;
    	return findNode(x, root).element;
    }
    
    // Insert element x into BST, unless it is already stored.  Return true
    // if insertion is performed and false otherwise.
    // Pre:   x is null or points to a valid object of type T
    // Post:  the binary tree contains x
    public boolean insert( T x ) {
    	if(x == null) return false;
    	return insert(x, root);
    }
    
    private boolean insert(T x, BinaryNode sRoot) {
    	if(sRoot == null) {
    		sRoot.element = x;
    		return true;
    	}
    	
    	int compareResult = x.compareTo(sRoot.element);
    	
    	if(compareResult < 0)	insert(x, sRoot.left);
    	if(compareResult > 0)	insert(x, sRoot.right);
    	else					return false;
    }
    
    // Delete element matching x from the BST, if present.  Return true if
    // matching element is removed from the tree and false otherwise.
    // Pre:   x is null or points to a valid object of type T
    // Post:  the binary tree does not contain x
    public boolean remove( T x ) {
    	if(x == null) return false;
    	
    	BinaryNode targetNode = findNode(x, root);
    	
    	if(targetNode == null) return false;
    	
    	if(targetNode.right == null && targetNode.left == null) {
    		targetNode = null;
		}
		//Case 2: 1 leaf
		else if(targetNode.right == null ^ targetNode.left == null) {
			if(targetNode.right == null)	targetNode = targetNode.left;
			else							targetNode = targetNode.right;
		}
		//Case 3: 2 leaves
		else {
			BinaryNode rMinParent	= findRMinParent(targetNode);
			BinaryNode rMin			= rMinParent.left;
			
			rMin.left		= targetNode.left;
			rMin.right		= targetNode.right;
			targetNode		= rMin;
			rMinParent.left	= null;
		}
		return true;
    	
    	
    }
    
    private BinaryNode findRMinParent(BinaryNode sRoot) {
    	BinaryNode sRootLeft = sRoot.left;
    	
    	if(sRootLeft.left == null && sRootLeft.right == null)
    		return sRoot;
    	else return findRMinParent(sRootLeft);
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
    	targetNode.left		= null;
    	targetNode.right	= null;
    }    

    // Return the tree to an empty state.
    // Pre:   none
    // Post:  the binary tree contains no elements
    public void clear( ) {
    	
    }

    // Return true iff other is a BST that has the same physical structure
    // and stores equal data values in corresponding nodes.  "Equal" should
    // be tested using the data object's equals() method.
    // Pre:   other is null or points to a valid BST<> object, instantiated
    //           on the same data type as the tree on which equals() is invoked
    // Post:  both binary trees are unchanged
    public boolean equals(Object other) {
    	if(other == null) return false;
    	
    }

    // Return number of levels in the tree.  (An empty tree has 0 levels.)
    // Pre:   tree is a valid BST<> object
    // Post:  the binary tree is unchanged
    public int levels() { . . . }
}
