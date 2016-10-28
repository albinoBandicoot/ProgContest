public class Grid {

	/* Everthing here has been tested */

	public static long[][] rotateCCW (long[][] x) {
		long[][] res = new long[x.length][x[0].length];
		for (int i=0; i < x.length; i++) {
			for (int j=0; j < x[0].length; j++) {
				res[x[0].length - j - 1][i] = x[i][j];
			}
		}
		return res;
	}

	public static long[][] rotateCW (long[][] x) {
		long[][] res = new long[x.length][x[0].length];
		for (int i=0; i < x.length; i++) {
			for (int j=0; j < x[0].length; j++) {
				res[j][x.length-i-1] = x[i][j];
			}
		}
		return res;
	}

	public static void print (long[][] x) {
		for (int i=0; i < x.length; i++) {
			for (int j=0; j < x[0].length; j++) {
				System.out.print (x[i][j]);
			}
			System.out.println();
		}
	}

	public static void main (String[] args) {
		long[][] x = {{1,0,0},{2,3,4},{5,6,7}};
		print(x);
		System.out.println();
		print(rotateCW(x));
	}
}
