public class LSolve {

	public static final double E = 1e-8;

	public static boolean zero (double x) {
		return Math.abs (x) < E;
	}

	// each row is one equation; just the coefficients
	//   a1x1 + a2x2 + ... + anxn = b
	// last column is the constant on the RHS.
	public static double[] solveSystem (double[][] matrix) {
		/* Solve by gaussian eliminiation. 
		 * Forwards phase:  */
		int nvars = matrix[0].length - 2;
		int n = matrix.length;
		for (int r=0; r < n; r++) {
			boolean swap = r >= nvars || zero (matrix[r][r]);
			for (int r2= r+1; r2 < n; r2++) {
				if (swap) {
					if (!zero (matrix[r2][r])) {
						for (int c = 0; c <= nvars; c++) {
							double tmp = matrix[r][c];
							matrix[r][c] = matrix[r2][c];
							matrix[r2][c] = tmp;
						}
						r2 = r;	// reset - this will cause the loop to go around again
						swap = false;
					}
				} else {
					double mul = -matrix[r2][r] / matrix[r][r];
					if (!zero (mul)) {	// should speed things up a bit
						for (int c=0; c <= nvars; c++) {
							matrix[r2][c] += mul * matrix[r][c];
						}
					}
				}
			}
		}
		/* Do the backwards phase */
		for (int r = n-1; r >= 0; r--) {
			int c = 0;
			for (c = 0; c < nvars; c++) {
				if (!zero (matrix[r][c])) break;
			}
			if (!zero (matrix[r][c])) {	// we found a pivot at (r,c)
				double mul = 1.0 / matrix[r][c];
				for (int c2=c; c2 <= nvars; c2++) {
					matrix[r][c2] *= mul;
				}
				for (int r2=r-1; r2 >= 0; r2--) {
					double m = -matrix[r2][c];	
					for (int c2=0; c2 <= nvars; c2++) {
						matrix[r2][c2] += m * matrix[r][c2];
					}
				}
			}
		}
		// now determine the solution. For now we will just set any free variables to 0. 
		double[] soln = new double[nvars];
		int c = 0;
		for (int r=0; r < n; r++) {
			while (zero (matrix[r][c]) && c < nvars) c++;
			if (c == nvars) {	// we had a row of all 0s.
				if (zero(matrix[r][nvars])) {	// consistent
					System.out.println ("Row of zeros detected");
				} else {
					System.out.println ("Inconsistent linear system!");
					return null;
				}
			} else {
				soln[c] = matrix[r][nvars];
			}
		}
		return soln;
	}
}
