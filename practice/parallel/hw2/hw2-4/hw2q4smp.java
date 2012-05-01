/**
 * hw2q4smp.java
 *
 *
 * @version   $Id: hw1q1smp.java,v 1.6 
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

import edu.rit.pj.Comm;
import edu.rit.pj.IntegerForLoop;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;
import edu.rit.util.Random;


/**
 * 
 * Parallel program 
 * 
 * 			To find the summation of all the numbers using partial summation method
 * 
 * @author Karthikeyan Karur Balu
 *
 */
public class hw2q4smp {
	public static int n;
	public static int row;
	public static int seed;			//seed for random number generation
	public static int j;			// 'j' is outer array used inorder to be shared among all processors
	public static int[] input;		//input set of n numbers
	public static int[][] matrix;	//calculated matrix
	public static int power;		//power for each 2 ^ j

	/**
	 * Main program
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		n = Integer.parseInt(args[0]);
		seed = Integer.parseInt(args[1]);
		FileWriter fstream = new FileWriter(args[2]);			//File input / output 
		BufferedWriter out = new BufferedWriter(fstream);
		input = new int[n];
		Random number = Random.getInstance(seed);
		for (int i = 0; i < n; i++) {
			input[i] = number.nextInt(10);						//Random number
		}
		
		Comm.init(args);										//for parallel program
		long t1 = System.currentTimeMillis();		
		row = (int) Math.ceil((Math.log(n) / Math.log(2)));		//finding log value using ceil 
		matrix = new int[row][n];
		//storing the inital set in the matrix
		for (int i = 0; i < n; i++) {
			matrix[0][i] = (i - 1) >= 0 ? (input[i - 1] + input[i])
					: (input[i]);
		}
		
		//parallel section
		for (j = 1; j < row; j++) {
			power = (int) Math.pow(2, j);
			new ParallelTeam().execute(new ParallelRegion() {
				public void run() throws Exception {
					execute(0, n - 1, new IntegerForLoop() {
						public void run(int first, int last) throws Exception {
							// Check if i - temp >=0 to avoid Arrayout of bound exception
							for (int i = first; i <= last; i++) {
								matrix[j][i] = (i - power) >= 0 ? (matrix[j - 1][i
										- power] + matrix[j - 1][i])
										: (matrix[j - 1][i]);
							}
						}
					});
				}
			});
		}
		long t2 = System.currentTimeMillis();

		//timing calculation
		System.out.println("Run time :: " + (t2 - t1) + " msec");
		out.write("\nRun time :: " + (t2 - t1) + " msec\n\n");
		//file operation as specified in the question
		out.write("Original data\n");
		for (int i = 0; i < input.length; i++)
			out.write(input[i] + " ");
		out.write("\n\n");
		for (int k = 0; k < row; k++) {
			out.write("[");
			for (int i = 0; i < n; i++) {
				String str = (i == (n - 1)) ? (matrix[k][i] + "]\n")
						: (matrix[k][i] + ", ");
				out.write(str);
			}
		}
		out.write("\n");
		out.write("Final data\n");
		for (int i = 0; i < n; i++) {
			out.write(matrix[row - 1][i] + " ");
		}
		out.write("\n");
		out.close();
	}
}
