import java.util.*;
import static java.lang.Math.*;
public class Circle {
	public Point c;
	public double r;

	public Circle (Point center, double radius) {
		c = center;
		r = radius;
	}

	public boolean contains (Point p) {
		return c.distance(p) < r;
	}

	public boolean contains (Line l) {	// segment
		return contains(l.s) && contains(l.s.add(l.d));
	}

	// return the angle of a point relative to the circle center
	public double angle (Point p) {
		p = p.sub(c);
		return atan2(p.x,p.y);
	}

	// test me!
	public Point[] intersection (Circle k) {
		double d = c.distance(k.c);
		double x = (d*d - k.r*k.r + r*r)/(2*d);	// length along center-center segment
		if (r*r - x*x < 0) return null;
		double y = sqrt(r*r - x*x);	// perp. distance away from center-center segment
		Point[] res = new Point[2];
		Point dir = k.c.sub(c).normalize();
		Point m = c.add(dir.mul(x));
		Point s = dir.cw90().mul(y);
		res[0] = m.add (s);
		res[1] = m.sub (s);
		return res;
	}
}
