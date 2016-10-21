import java.util.*;
public class Arc {
	public Point c;
	public double r;
	public Point n;	// normal vector at midpoint of arc
	public double w;	// width of arc (radians, on each side of midpoint).
	private double dpmin;

	/* Needs testing (and completion!) */

	public Arc (Point c, double r, Point n, double w) {
		this.c = c;
		this.r = r;
		this.n = n.normalize();
		this.w = w;
		dpmin = Math.cos(w);
	}

	// start and end points, and a thrid arbitrary point on the arc
	public Arc (Point s, Point p, Point e) {

	}

	// center, radius, start and end points
	public Arc (Point c, double r, Point s, Point e, boolean cw) {

	}

	// starting point, starting tangent, radius, sweep in radians
	public Arc (Point p, Point t, double r, double theta, boolean cw) {
		Point ns = (cw ? t.ccw90() : t.cw90()).normalize();
		c = p.sub(ns.mul(r));
		n = ns.rotate ((cw?1:-1)*theta/2);
		w = theta/2;
	}

	// test whether p is in the sector described by the arc. If p is on the circle,
	// this tells whether p is on the arc.
	public boolean inSector (Point p) {
		return p.sub(c).normalize().dot(n) >= dpmin;
	}
}
