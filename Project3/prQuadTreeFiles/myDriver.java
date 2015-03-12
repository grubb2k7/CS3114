import Minor.P3.DS.*;
import Minor.P3.Client.*;

public class myDriver {

	public myDriver() {
		// TODO Auto-generated constructor stub
	}

	static prQuadTree<Point> tree;
	static long xMin = -1000;
	static long xMax = 1000;
	static long yMin = -1000;
	static long yMax = 1000;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		tree = new prQuadTree<Point>(xMin, xMax, yMin, yMax);
		Point p = new Point(30, 40);
		tree.insert(p);
		tree.insert(p);
		p = new Point(100, 40);
		tree.insert(p);
		tree.printTree();
	}
}
