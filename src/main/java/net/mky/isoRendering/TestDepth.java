package net.mky.isoRendering;

/*
 * Test program
 */

class TestDepth {

	final static int SIZE = 10;
	static int[][] arr;

	// Method
	public static int depth(int x, int y) {
		return x + y;
	}

	public static void print() {
		// prints the Array
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				// i and j reversed = print the rows first
				int val;
				String str;
				val = arr[j][i];
				if (val < 10) {
					str = "0" + val;
				} else {
					str = "" + val;
				}
				System.out.print("[" + str + "]");
			}
			System.out.println(""); // start new row
		}
	}

	// Main
	/*
	 * public static void main(String[] args) { arr = new int[SIZE][SIZE];
	 * 
	 * for(int i=0; i<SIZE; i++) { for(int j=0; j<SIZE; j++) { arr[i][j] =
	 * depth(i,j); } }
	 * 
	 * print(); }
	 */
}