package Minor.P2.DS;
import java.util.Random;
public class Project2 {
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		BST<Integer> tree = new BST<Integer>(10);
		
		//Testing generic inserting features
		tree.insert(null);
		Random rand = new Random();
		for(int i = 0; i < 10; i++) {
			 tree.insert(rand.nextInt());
		}
		printTree(tree.root, 0);
		
		//Testing to see if insert will not insert more of the same
		//valued object
		tree.insert(0);
		if(!tree.insert(0)) System.out.println("test2 passed");
		if(tree.insert(3)) System.out.println("test2 passed");
		if(tree.insert(1)) System.out.println("test2 passed");
		if(tree.insert(2)) System.out.println("test2 passed");
		
		if(tree.insert(-1)) System.out.println("test2 passed");
		printTree(tree.root, 0);
		
		//Testing to see if the remove function works
		//Should put the removed node into the pool
		if(tree.remove(0)) System.out.println("test3 passed");
		System.out.println("root:");
		printTree(tree.root, 0);
		System.out.println("pool:");
		printTree(tree.pool, 0);
		
		if(tree.remove(3)) System.out.println("test3 passed");
		System.out.println("root:");
		printTree(tree.root, 0);
		System.out.println("pool:");
		printTree(tree.pool, 0);
		
		if(tree.remove(1)) System.out.println("test3 passed");
		System.out.println("root:");
		printTree(tree.root, 0);
		System.out.println("pool:");
		printTree(tree.pool, 0);
		
		if(tree.insert(0)) System.out.println("test3 passed");
		System.out.println("root:");
		printTree(tree.root, 0);
		System.out.println("pool:");
		printTree(tree.pool, 0);
		
		if(tree.insert(3)) System.out.println("test3 passed");
		System.out.println("root:");
		printTree(tree.root, 0);
		System.out.println("pool:");
		printTree(tree.pool, 0);
		
		if(tree.insert(1)) System.out.println("test3 passed");
		System.out.println("root:");
		printTree(tree.root, 0);
		System.out.println("pool:");
		printTree(tree.pool, 0);
		
		tree.cap(2);
		System.out.println("root:");
		printTree(tree.root, 0);
		System.out.println("pool:");
		printTree(tree.pool, 0);
		
		System.out.println("The number of levels: " + tree.levels());
		
		BST<Integer> tree1 = new BST<Integer>();
		BST<Integer> tree2 = new BST<Integer>();
		BST<Double> treeDouble = new BST<Double>();
		
		for(int i = 0; i < 10; i++) {
			int random = rand.nextInt();
			tree1.insert(random);
			tree2.insert(random);
			treeDouble.insert((double)random);
		}
		
		if(tree1.equals(tree2)) System.out.println("Two trees equal eachother");
		printTree(tree1.root, 0);
		printTree(tree2.root, 0);
		
		if(!tree1.equals(tree)) System.out.println("Two trees dont equal");
		
		
		
		if(!tree1.equals(treeDouble)) System.out.println("These two trees are not the same type");
	}
	
	static public void printTree(BST<Integer>.BinaryNode sRoot, int numTab) {
		for(int i = 0; i < numTab; i++) {
			System.out.print("\t");
		}
		if(sRoot == null) {
			System.out.println("NULL");
			return;
		}
		System.out.println(sRoot.element.toString());
		printTree(sRoot.right, numTab + 1);
		printTree(sRoot.left, numTab + 1);
	}

}
