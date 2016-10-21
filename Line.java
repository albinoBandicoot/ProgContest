import java.util.*;
import static java.lang.Math.*;
public class Line {
	public Point s, d;
	public double tmin, tmax;

	public Line (Point start, Point dir, double min, double max) {
		s = start;
		d = dir.normalize();
		tmin = min;
		tmax = max;
	}

	public static Line line (Point s, Point d) {
		return new Line (s,d,Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);
	}

	public static Line ray (Point s, Point d) {
		return new Line (s,d,0,Double.POSITIVE_INFINITY);
	}

	public static Line segment (Point s, Point d, double len) {
		return new Line (s,d,0,len);
	}

	public Point eval (double t) {
		return s.add (d.mul(t));
	}

	// using Cramer's rule; returns parameter value
	// tested
	public double[] intersection (Line l) {
		double a = (d.x * -l.d.y) - (-l.d.x * d.y);
		if (abs(a) < 1e-10) return null;	// parallel lines
		double d1 = ((l.s.x - s.x) * -l.d.y) - ((l.s.y - s.y) * -l.d.x);
		double d2 = (d.x * (l.s.y - s.y)) - (d.y * (l.s.x - s.x));
		double[] t = new double[]{d1/a, d2/a};
		if (t[0] >= tmin && t[0] <= tmax && t[1] >= l.tmin && t[1] <= l.tmax) return t;
		return null;
	}

	// if l intersects this, return a new ray starting at the intersection
	// point in the direction determined by a reflection about the normal
	// test me!
	public Line reflect (Line l) {
		double[] t = intersection (l);
		if (t == null) return null;
		Point p = eval(t[0]);
		Point v = l.d;
		Point n = d.cw90();
		if (n.dot(v) > 0) n = n.mul(-1);
		return ray (p, v.sub(n.mul(2*n.dot(v))));
	}

	public ArrayList<Point> intersection (Circle c) {
		ArrayList<Point> ipts = new ArrayList<Point>();
		Point o = s.add(c.c.mul(-1));
		double b = d.dot(o);
		double a = o.dot(o) - c.r*c.r;
		double e = b*b - a;
		if (e < 0) return ipts;
		double f = sqrt (e);
		if (-b+f >= tmin && -b+f <= tmax) ipts.add(eval(-b+f));
		if (-b-f >= tmin && -b-f <= tmax) ipts.add(eval(-b-f));
		return ipts;
	}
}
