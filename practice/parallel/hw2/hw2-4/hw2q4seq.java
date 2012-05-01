/**
 * hw2q4seq.java
 *
 *
 * @version   $Id: hw1q1seq.java,v 1.6 
 * 2011/2/4 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * 
 * Revision 1.0     3/29/2011			10:05:00
 * Initial revision
 *
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import edu.rit.util.Random;

/**
 * 
 * Sequential program 
 * 
 * 			To find the summation of all the numbers using partial summation method
 * 
 * @author Karthikeyan Karur Balu
 *
 */
public class hw2q4seq {
	public static int n;
	public static int row;
	public static int seed;
	public static int[] input;			//input list of numbers
	public static int[][] matrix;		//matrix for storing the numbers
	public static void main(String[] args) throws IOException {
		n = Integer.parseInt(args[0]);
		seed = Integer.parseInt(args[1]);	//seed for random number
		FileWriter fstream = new FileWriter(args[2]);
        BufferedWriter out = new BufferedWriter(fstream);
		input = new int[n];
		Random number = Random.getInstance(seed);
		for (int i = 0; i < n; i++) {
			input[i] = number.nextInt(10);	//number < 10 always 
		}
		
		long t1 = System.currentTimeMillis();
		row = (int) Math.ceil((Math.log(n)/Math.log(2)));
		matrix = new int[row][n];
		for(int i = 0;i<n;i++) {
			matrix[0][i] = (i - 1) >= 0 ? (input[i-1] + input[i]) : (input[i]);
		}
		for(int j = 1;j<row;j++) {
			int temp = (int) Math.pow(2, j);
			for(int i = 0;i<n;i++) {
				matrix[j][i] = (i - temp) >= 0 ? (matrix[j-1][i-temp] + matrix[j-1][i]) : (matrix[j-1][i]);
			}
		}
		
		// Check if i - temp >=0 to avoid Arrayout of bound exception
		for(int j = 1;j<row;j++) {
			int temp = (int) Math.pow(2, j);
			for(int i = 0;i<n;i++) {
				matrix[j][i] = (i - temp) >= 0 ? (matrix[j-1][i-temp] + matrix[j-1][i]) : (matrix[j-1][i]);
			}
		}
		long t2 = System.currentTimeMillis();
		
		System.out.println("Run time :: " + (t2-t1) + " msec"); //runtime calculation
		
		//storing in a file in the format specified
		out.write("\nRun time :: " + (t2-t1) + " msec\n");
		out.write("Original data\n\n");
		for(int i = 0;i<input.length;i++)
			out.write(input[i] + " ");
		out.write("\n\n");
		for(int j = 0;j<row;j++) {
			out.write("[");
			for(int i = 0;i<n;i++) {
				String str = (i == (n-1)) ? (matrix[j][i] + "]\n") : (matrix[j][i] + ", "); 
				out.write(str);
			}
		}
		out.write("\n");
		out.write("Final data\n");
		for(int i = 0;i<n;i++) {
			out.write(matrix[row-1][i] + " ");
		}
		out.write("\n");
		out.close();
	}
}
