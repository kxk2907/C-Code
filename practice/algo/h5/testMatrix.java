public class testMatrix {
	public static void main(String[] args) {
		int m = 10000, n = 10000;
		int[][] array = new int[m][n];
		for(int i = 0;i<m;i++) {
			for(int j = 0;j<n;j++) {
				array[i][j] = 0;
			}
		}
		array[0][0] = 2;
		array[m-1][n-1] = 3;
		System.out.println(m+" "+n);
		for(int i = 0;i<m;i++) {
			for(int j = 0;j<n;j++) {
				System.out.print(array[i][j]+" ");
			}
			System.out.println();
		}
	}
}
