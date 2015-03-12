package Minor.P3.DS;
import java.util.Vector;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;
import Minor.P3.Client.Point;
import Minor.P3.DS.prQuadTree.prQuadInternal;
import Minor.P3.DS.prQuadTree.prQuadLeaf;
import Minor.P3.DS.prQuadTree.prQuadNode;

public class Lewis {

	int xMin = 0, 
	    xMax = 0, 
	    yMin = 0, 
	    yMax = 0;
	Vector<Point> data = null;
	Random        source;
	boolean       profMode = true;
	int           totalPtsAssigned;
	String        pad;
	
	prQuadTree<Point> localPR;
	prQuadTree<Point>.prQuadInternal Internal;
	prQuadTree<Point>.prQuadLeaf     Leaf;

	
	public Lewis(int xMin, int xMax, int yMin, int yMax, Vector<Point> data, Random source, boolean profMode) {
		
		this.xMin = xMin;
		this.xMax = xMax;
		this.yMin = yMin;
		this.yMax = yMax;
		this.data = data;
		this.source = source;
		this.totalPtsAssigned = 0;
		this.profMode = profMode;
	    pad = new String("---");
	    
		localPR = new prQuadTree<Point>(0, 0, 0, 0);
		Internal = localPR.new prQuadInternal();
		Leaf     = localPR.new prQuadLeaf();
	}
	
	public void checkTreeInitialization() {
		   
	   String Notes = new String();
	   Notes = Notes + writePoints(0) + " checkTreeInitialization results:\n";
	   Notes = Notes + writePoints(0) + "   Creating a new prQuadTree with world boundaries:\n";
	   Notes = Notes + writePoints(0) + "     xMin:  " + xMin + "\n";
	   Notes = Notes + writePoints(0) + "     xMax:  " + xMax + "\n";
	   Notes = Notes + writePoints(0) + "     yMin:  " + yMin + "\n";
	   Notes = Notes + writePoints(0) + "     yMax:  " + yMax + "\n";
	   
	   prQuadTree<Point> Tree = new prQuadTree<Point>(xMin, xMax, yMin, yMax);
	   Notes = Notes + writePoints(10);
	   if ( Tree.root != null ) {
		   Notes = Notes + "   Bummer:  prQuadTree root was NOT null.\n";
       }
       else {
	       Notes = Notes + "   prQuadTree root was OK.\n";
       }
	   Notes = Notes + writePoints(10);
	   if ( Tree.xMin != xMin ) {
		   Notes = Notes + "   Bummer:  tree has xMin as " + Tree.xMin + ".\n";
       }
       else {
	       Notes = Notes + "   prQuadTree xMin was OK.\n";
       }
	   Notes = Notes + writePoints(10);
	   if ( Tree.xMax != xMax ) {
		   Notes = Notes + "   Bummer:  tree has xMax as " + Tree.xMax + ".\n";
       }
       else {
	       Notes = Notes + "   prQuadTree xMax was OK.\n";
       }
	   Notes = Notes + writePoints(10);
	   if ( Tree.yMin != yMin ) {
		   Notes = Notes + "   Bummer:  tree has yMin as " + Tree.yMin + ".\n";
       }
       else {
	       Notes = Notes + "   prQuadTree yMin was OK.\n";
       }
	   Notes = Notes + writePoints(10);
	   if ( Tree.yMax != yMax ) {
		   Notes = Notes + "   Bummer:  tree has yMax as " + Tree.yMax + ".\n";
       }
       else {
	       Notes = Notes + "   prQuadTree yMax was OK.\n";
       }

      
       try {
           String logName = "TestTreeInitialization.txt";
           if ( profMode )
    		  logName = "prof" + logName;
           FileWriter Log = new FileWriter(logName);
	       Log.write(Notes);
	       Log.close();
       }
       catch ( IOException e ) {
		   System.out.println("Error writing notes to log file in Lewis.checkTreeInitialization().");
	   }
	}
	
    public void checkInsertion() throws IOException {
    	
    	FileWriter Log = null;
        try {
            String logName = "TestInsertion.txt";
            if ( profMode )
     		  logName = "prof" + logName;
            Log = new FileWriter(logName);
        }
        catch ( IOException e ) {
 		    System.out.println("Error writing notes to log file in Lewis.checkInsertion().");
 	    }
    	String Notes = new String();
		prQuadTree<Point> Tree = new prQuadTree<Point>(xMin, xMax, yMin, yMax);
    	
    	Notes += writePoints(0) + " checkInsertion() results:\n";
    	
    	for (int dataIdx = 0; dataIdx < data.size(); dataIdx++) {
    		Notes += writePoints(0) + "   Inserting value: " + data.elementAt(dataIdx) + "\n";
    		boolean success = false;
    		try {
    		   success = Tree.insert(data.elementAt(dataIdx));
    		}
    		catch ( Exception e ) {
        		Notes += writePoints(0) + "   Caught an exception while inserting value:\n";
        		Notes += writePoints(0) + "     " + e.getMessage() + "\n";
        		Notes += writePoints(0) + "   Aborting insertion test.\n";
        		Log.write(Notes);
        		Log.close();
        		return;
    		}

    		try {
    			if ( !success ) {
    				Notes += writePoints(0) + "   Error: insert() returned false.\n";
    			}
    			else {
    				Notes += writePoints(0) + "   insert() returned true.\n";
    			}
    			Notes += writePoints(0) + "   Resulting tree:\n";
    			Log.write(Notes);
    			Notes = "";
    			printTree(Log, Tree, 1);
    		}
    		catch ( IOException e ) {
    			System.out.println("Error writing notes to log file in Lewis.checkInsertion().");
    		}
    	}
    	
    	try {
    	    int dataIdx = Math.abs(source.nextInt()) % data.size();
    	    Notes += writePoints(0) + "   Now trying to insert a duplicate entry: " + data.elementAt(dataIdx) + "\n";
    	    boolean success;
    	    try {
    	       success = Tree.insert( data.elementAt(dataIdx) );
    	    }
    		catch ( Exception e ) {
        		Notes += writePoints(0) + "   Caught an exception while inserting value:\n";
        		Notes += writePoints(0) + "     " + e.getMessage() + "\n";
        		Notes += writePoints(0) + "   Aborting insertion test.\n";
        		Log.write(Notes);
        		Log.close();
        		return;
    		}
			if ( success ) {
				Notes += writePoints(0) + "   Error: insert() returned true.\n";
			}
			else {
				Notes += writePoints(0) + "   insert() returned false.\n";
			}
			Notes += writePoints(0) + "   Resulting tree:\n";
			Log.write(Notes);
			Notes = "";
			printTree(Log, Tree, 1);
    	}
    	catch ( Exception e ) {
    		Notes += writePoints(0) + "   Caught an exception during insertion.\n";
    		Notes += writePoints(0) + "     " + e.getMessage() + "\n";
    	}
    	
        try {
 	        Log.write(Notes);
 	        Log.close();
        }
        catch ( IOException e ) {
 		    System.out.println("Error writing notes to log file in Lewis.checkInsertion().");
 	    }
    }
    
    public void checkDeletion() throws IOException {
    	
    	FileWriter Log = null;
        try {
            String logName = "TestDeletion.txt";
            if ( profMode )
     		  logName = "prof" + logName;
            Log = new FileWriter(logName);
        }
        catch ( IOException e ) {
 		    System.out.println("Error writing notes to log file in Lewis.checkDeletion().");
 	    }
    	String Notes = new String();
		prQuadTree<Point> Tree = new prQuadTree<Point>(xMin, xMax, yMin, yMax);
    	
    	Notes += writePoints(0) + " checkDeletion() results:\n";
    	Notes += writePoints(0) + "     Building a tree for testing...(if checkInsertion() failed this should fail also)\n";

    	try {
    		for (int dataIdx = 0; dataIdx < data.size(); dataIdx++) {
    			Notes += writePoints(0) + "       Inserting value: " + data.elementAt(dataIdx) + "\n";
    			boolean success = Tree.insert(data.elementAt(dataIdx));
    		}
    		//System.out.println("Finished building tree for deletion test.");
    	} catch ( Exception e ) {
    		Notes += writePoints(0) + "   Caught an exception while building tree:\n";
    		Notes += writePoints(0) + "     " + e.getMessage() + "\n";
    		Notes += writePoints(0) + "   Aborting deletion test.\n";
    		Log.write(Notes);
    		Log.close();
    		return;
    	}
    	
    	try {
    		Notes += writePoints(0) + "     Resulting tree:\n";
    		Log.write(Notes);
    		Notes = "";
    		printTree(Log, Tree, 0);
    	}
    	catch ( IOException e ) {
    		System.out.println("Error writing notes to log file in Lewis.checkDeletion().");
    	}
    	catch ( Exception e ) {
    		Notes += writePoints(0) + "   Caught an exception while displaying tree.\n";
    		Notes += writePoints(0) + "     " + e.getMessage() + "\n";
    		Notes += writePoints(0) + "   Aborting deletion test.\n";
    		Log.write(Notes);
    		Log.close();
    		return;
    	}
   	
    	try {
    		for (int dataIdx = 0; dataIdx < data.size(); dataIdx++) {
    			Notes += writePoints(0) + "    Deleting value: " + data.elementAt(dataIdx) + "\n";
    			Point p = data.elementAt(dataIdx);
    			boolean success = Tree.delete( new Point(p.getX(), p.getY()));

    			try {
    				if ( !success ) {
    					Notes += writePoints(0) + "   Bummer: delete() returned false.\n";
    				}
    				else {
    					Notes += writePoints(0) + "   delete() returned true.\n";
    				}
    				Notes += writePoints(0) + "   Resulting tree:\n";
    				Log.write(Notes);
    				Notes = "";
    	    		printTree(Log, Tree, 2);    			}
    			catch ( IOException e ) {
    				System.out.println("Error writing notes to log file in Lewis.checkDeletion().");
    			}
    		}
    	}
    	catch ( Exception e ) {
    		Notes += writePoints(0) + "   Caught an exception while performing deletion:\n";
    		Notes += writePoints(0) + "     " + e.getMessage() + "\n";
    		Notes += writePoints(0) + "   Aborting deletion test.\n";
    		Log.write(Notes);
    		Log.close();
    		return;
    	}
        try {
 	        Log.write(Notes);
 	        Log.close();
        }
        catch ( IOException e ) {
 		    System.out.println("Error writing notes to log file in Lewis.checkDeletion().");
 	    }
    	
    }

    public void checkRegionSearch() throws IOException {
    	
    	FileWriter Log = null;
        try {
            String logName = "TestRegionSearch.txt";
            if ( profMode )
     		  logName = "prof" + logName;
            Log = new FileWriter(logName);
        }
        catch ( IOException e ) {
 		    System.out.println("Error writing notes to log file in Lewis.checkRegionSearch().");
 	    }
    	String Notes = new String();
		prQuadTree<Point> Tree = new prQuadTree<Point>(xMin, xMax, yMin, yMax);
    	
    	Notes += writePoints(0) + " checkRegionSearch() results:\n";
    	Notes += writePoints(0) + "     Building a tree for testing...(if checkInsertion() failed this should fail also)\n";

    	try {
    		for (int dataIdx = 0; dataIdx < data.size(); dataIdx++) {
    			Notes += writePoints(0) + "       Inserting value: " + data.elementAt(dataIdx) + "\n";
    			boolean success = Tree.insert(data.elementAt(dataIdx));
    		}
    	} catch ( Exception e ) {
    		Notes += writePoints(0) + "   Caught an exception while building tree:\n";
    		Notes += writePoints(0) + "     " + e.getMessage() + "\n";
    		Notes += writePoints(0) + "   Aborting region search test.\n";
    		Log.write(Notes);
    		Log.close();
    		return;
    	}
    	
    	try {
    		Notes += writePoints(0) + "     Resulting tree:\n";
    		Log.write(Notes);
    		Notes = "";
    		printTree(Log, Tree, 0);
    	}
    	catch ( IOException e ) {
    		System.out.println("Error writing notes to log file in Lewis.checkDeletion().");
    	}
    	catch ( Exception e ) {
    		Notes += writePoints(0) + "   Caught an exception while displaying tree.\n";
    		Notes += writePoints(0) + "     " + e.getMessage() + "\n";
    		Notes += writePoints(0) + "   Aborting region search test.\n";
    		Log.write(Notes);
    		Log.close();
    		return;
    	}
    	// can use world boundaries to use in setting up the test regions
    	try {
        	// get center of world:
    		long xCenter = (xMin + xMax)/2;
    		long yCenter = (yMin + yMax)/2;
        	// first, try the NE quadrant
    		//System.out.println("First:");
    		runRegionSearch(Tree, xCenter, xMax, yCenter, yMax, Log);
    		
        	// second, try the SW quadrant
    		//System.out.println("Second:");
    		runRegionSearch(Tree, xMin, xCenter, yMin, yCenter, Log);
    		
    		// third, try a rectangle around the center of the world
    		//System.out.println("Third:");
    		long xDelta = 1000 + Math.abs(source.nextLong()) % 10000;
    		long yDelta = 1000 + Math.abs(source.nextLong()) % 10000;
    		runRegionSearch(Tree, xCenter - xDelta, xCenter + xDelta, yCenter - yDelta, yCenter + yDelta, Log);
    		
    		// fourth, try a rectangle around a random center within the world
    		int dataIdx = Math.abs(source.nextInt()) % data.size();
    		long xC = data.get(dataIdx).getX();
    		long yC = data.get(dataIdx).getY();
    		xDelta = 1000 + Math.abs(source.nextLong()) % xC;
    		if ( xC - xDelta < xMin )
    			xDelta = xC - xMin;
    		else if ( xC + xDelta > xMax )
    			xDelta = xMax - xC;
    		yDelta = 1000 + Math.abs(source.nextLong()) % yC;
    		if ( yC - yDelta < yMin )
    			yDelta = yC - yMin;
    		else if ( yC + yDelta > yMax )
    			yDelta = yMax - yC;
    		runRegionSearch(Tree, xC - xDelta, xC + xDelta, yC - yDelta, yC + yDelta, Log);
    		
    		// fifth, try a very small one, likely empty
    		xDelta = 10 + Math.abs(source.nextLong()) % 21;
    		yDelta = 10 + Math.abs(source.nextLong()) % 21;
    		runRegionSearch(Tree, xC - xDelta, xC + xDelta, yC - yDelta, yC + yDelta, Log);
    	}
    	catch ( Exception e ) {
    		System.out.println("Exception: " + e.getMessage());
    		Notes += writePoints(0) + "   Caught an exception while testing region search.\n";
    		Notes += writePoints(0) + "     " + e.getMessage() + "\n";
    		Notes += writePoints(0) + "   Aborting region search test.\n";
    		Log.write(Notes);
    		Log.close();
    		return;
    	}
        try {
 	        Log.write(Notes);
 	        Log.close();
        }
        catch ( IOException e ) {
 		    System.out.println("Error writing notes to log file in Lewis.checkRegionSearch().");
 	    }
    }
    
    private void runRegionSearch(prQuadTree<Point> Tree, long xLo, long xHi, long yLo, long yHi, FileWriter Log) throws IOException {
    	
    	//System.out.println("Entering runRegionSearch");
		String Notes = new String();
    	try {
    		Notes += writePoints(0) + "Searching region: " 
    		+ "(" + xLo + ", " + xHi + ", " + yLo + ", " + yHi + ")" + "\n";
    		
    		Vector<Point> results = Tree.find(xLo, xHi, yLo, yHi);
    		Notes += writePoints(0) + "Results:\n";
    		if ( results.isEmpty() ) {
    			Notes += writePoints(20) + "  no data points found within that region.\n";
    		}
    		else {
    			//sortPoints(results);
    			for (int pos = 0; pos < results.size(); pos++) {
    				Notes += writePoints(10) + "   " + results.get(pos) + "\n";
    			}
    		}
    		//System.out.println("Finished search.");
    	}
    	catch ( Exception e ) {
    		Notes += writePoints(0) + "   Caught an exception while testing region search.\n";
    		Notes += writePoints(0) + "     " + e.getMessage() + "\n";
    		System.out.println("exception: " + e.getMessage());
    	}
	    Log.write(Notes);
	    //System.out.println("Leaving runRegionSearch");
    }
    
    private void sortPoints(Vector<Point> points) {
    	
    	int   N = points.size();
    	for (int Stop = N-1; Stop > 0; Stop--) {
    		for (int i = 0; i < N-1; i++) {
    			Point ith = points.get(i);
    			Point ipth = points.get(i+1);
    			if ( lessThan(ipth, ith) ) {
    				points.set(i, ipth);
    				points.set(i+1, ith);
    			}
    		}
    	}
    }
    
    private boolean lessThan(Point A, Point B) {
    	
    	long Ax = A.getX();
    	long Bx = B.getX();
    	long Ay = A.getY();
    	long By = B.getY();
    	
    	if ( A.getX() != B.getX() )
    		return Ax < Bx;
    	return Ay < By;
    }
    
    public String writePoints(int Pts) {

        String S;
        if ( profMode ) {
     	   totalPtsAssigned += Pts;
     	   if ( Pts < 10 )
     	      S = new String("[ " + Pts + "]");
     	   else
     		  S = new String("[" + Pts + "]");
        }
        else
     	  S = new String("");
        
        return S;
     }

    public void printTree(FileWriter Out, prQuadTree<Point> Tree, int ptsPerDataItem) {
    	try {
           if ( Tree.root == null )
              Out.write( writePoints(ptsPerDataItem) + "  Empty tree.\n" );
           else
              printTreeHelper(Out,  Tree.root, "", ptsPerDataItem);
    	}
    	catch ( IOException e ) {
    		return;
    	}
     }

	@SuppressWarnings("rawtypes")
	public void printTreeHelper(FileWriter Out, prQuadNode sRoot, String Padding, int ptsPerDataItem) {

		try {
			// Check for empty leaf
			if ( sRoot == null ) {
				Out.write( writePoints(0) + " " + Padding + "*\n");
				return;
			}
			// Check for and process SW and SE subtrees
			if ( sRoot.getClass().equals(Internal.getClass()) ) {
				prQuadInternal p = (prQuadInternal) sRoot;
				printTreeHelper(Out, p.SW, Padding + pad, ptsPerDataItem);
				printTreeHelper(Out, p.SE, Padding + pad, ptsPerDataItem);
			}
			// Display indentation padding for current node
			//Out.write(Padding);

			// Determine if at leaf or internal and display accordingly
			if ( sRoot.getClass().equals(Leaf.getClass()) ) {
				prQuadLeaf p = (prQuadLeaf) sRoot;
				for (int pos = 0; pos < p.Elements.size(); pos++) {
					Out.write( writePoints(ptsPerDataItem) + " " + Padding + p.Elements.get(pos) + "\n" );
				}
			}
			else if ( sRoot.getClass().equals(Internal.getClass()) )
				Out.write( writePoints(1) + " " + Padding + "@\n" );
			else
				Out.write( writePoints(0) + " " + sRoot.getClass().getName() + "#\n");

			// Check for and process NE and NW subtrees
			if ( sRoot.getClass().equals(Internal.getClass()) ) {
				prQuadInternal p = (prQuadInternal) sRoot;
				printTreeHelper(Out, p.NE, Padding + pad, ptsPerDataItem);
				printTreeHelper(Out, p.NW, Padding + pad, ptsPerDataItem);
			}
		}
		catch ( IOException e ) {
			return;
		}
	}

    public int ptsAssigned() {
    	return totalPtsAssigned;
    }
}
