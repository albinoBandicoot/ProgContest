public class Euclid {

	public static int gcd (int a, int b) {
		if (a == 0) return b;
		return gcd (b%a, a);
	}

	public static int lcm (int a, int b) {
		return a*b / gcd(a,b);
	}

	// compute x^-1 (mod m). Done as computing integers a,b st. ax + bm = 1. Then a is x's inverse
	public static int modularInverse (int x, int m) {
		return extendedEuclid (x, m, 1)[0];
	}

	public static int mod (int x, int m) {
		if (x > 0) return x%m;
		return (m - ((-x) % m)) % m;
	}

	// compute integers a and b such that ax + by = Q
	// additional solutions can be obtained as (a+ ky)x + (b- kx)y = ax + by + kxy - kxy = 1
	public static int[] extendedEuclid (int x, int y, int Q) {
		int len = (int) (Math.log(Math.max(x,y))/Math.log((1+Math.sqrt(5))/2));
		int[] r = new int[len+3];
		int[] q = new int[len+3];
		System.out.println("length: " + r.length);
		r[0] = x;
		r[1] = y;
		int i = 1;
		do {
			i++;
			r[i] = r[i-2] % r[i-1];
			q[i] = r[i-2] / r[i-1];
			System.out.println("r[" + i + "] = " + r[i] + "; q[i] = " + q[i]);
		} while (r[i-1] % r[i] != 0);
		if (Q % r[i] != 0) {
			// no solutions
			return null;
		}
		int mult = Q / r[i];
		// 1 = r[i-1] - r[i]*q[i]
		i--;
		int a = -q[i+1];	// coefficient of r[i]
		int b = 1;	// coefficient of r[i-1]
		System.out.println("1 = " + r[i] + "*" + a + " + " + r[i-1] + "*" + b);
		while (i >= 2) {
			// r[i] = r[i-2] - r[i-1]q[i-1]
			int na = b - a*q[i];	// coeff of i-1
			int nb = a; // coeff of i-2
			a = na;
			b = nb;
			i--;
			System.out.println("1 = " + r[i] + "*" + a + " + " + r[i-1] + "*" + b);
		}
		System.out.println("Solutions: " + mult*b + ", " + mult*a);
		return new int[]{mult*b,mult*a};
	}

	// find the smallest positive integer such that z = x[i] (mod m[i]) for all i.
	//
	// 2 congruences: z = x[0] + km[0]   and z = x[1] + jm[1]
	// 	want to solve for a and b so that   am[0] - bm[1] = x[1] - x[0]
	// 	this just uses extended Euclid. This gives (k
	public static int solveSimultaneousCongruences (int[] x, int[] m) {
		return 1;
	}

	public static void main (String[] args) {
		extendedEuclid (10, 14, 2);
	}


}
