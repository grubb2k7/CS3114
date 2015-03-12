package Minor.P3.Client;
import Minor.P3.DS.Compare2D;
import Minor.P3.DS.Direction;

public class Point implements Compare2D<Point> {

   private long xcoord;
   private long ycoord;
   
   public Point() {
      xcoord = 0;
      ycoord = 0;
   }

   public Point(long x, long y) {
      xcoord = x;
      ycoord = y;
   }

   public long getX() {
      return xcoord;
   }

   public long getY() {
      return ycoord;
   }
   
   public Direction directionFrom(long X, long Y) {
	   long x = this.getX() - X;
	   long y = this.getY() - Y;
	   
	   if(x > 0) {
		   if(y >= 0)	  return Direction.NE;
		   else if(y < 0) return Direction.SE;
	   }
	   else if(x < 0){
		   if(y > 0)	  return Direction.NW;
		   else if(y <= 0) return Direction.SW;
	   }
	   else if(x == 0 && y != 0) {
		   if(y > 0)	  return Direction.NW;
		   else if(y < 0) return Direction.SE;
	   }
	   return Direction.NOQUADRANT;
   }
   
   public Direction inQuadrant(double xLo, double xHi, 
                               double yLo, double yHi) { 
      return Direction.NOQUADRANT;
   }

   public boolean inBox(double xLo, double xHi, 
                          double yLo, double yHi) { 
      return false;
   }

   public String toString() {
      return getX() + ", " + getY();
   }
   
   public boolean equals(Object o) { 
      if(o.getClass().equals(Point.class)) {
    	  Point p = (Point)o;
    	  if(p.getX() == this.getX() && p.getY() == this.getY()) {
    		  return true;
    	  }
      }
      
      return false;
   }
}
