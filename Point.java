import java.util.*;
import static java.lang.Math.*;
public class Point {
	public double x,y;

	public Point (double x, double y) {
		this.x = x;
		this.y = y;
	}

	public Point add (Point p) {
		return new Point (x+p.x, y+p.y);
	}

	public Point sub (Point p) {
		return new Point (x-p.x, y-p.y);
	}

	public Point mul (double d) {
		return new Point (x*d, y*d);
	}

	public double dot (Point p) {
		return x*p.x + y*p.y;
	}

	public double length () {
		return sqrt (x*x + y*y);
	}

	public double distance (Point p) {
		return sub(p).length();
	}

	public Point normalize () {
		return mul(1/length());
	}

	public Point rotate (double t) {	// radians
		return new Point (cos(t)*x - sin(t)*y, sin(t)*x + cos(t)*y);
	}

	public Point cw90 () {
		return new Point (y,-x);
	}

	public Point ccw90 () {
		return new Point (-y,x);
	}

	public String toString () {
		return "(" + x + ", " + y + ")";
	}

}
