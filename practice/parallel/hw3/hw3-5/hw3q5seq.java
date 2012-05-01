/**
 * hw3q4seq.java
 *
 *
 * @version   $Id: hw3q4seq.java,v 1.6 
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
import edu.rit.pj.Comm;
import edu.rit.util.Random;

/**
 * Sequential program for doing matrix multiplication
 * 
 * 
 * @author kbkarthi
 * 
 */
public class hw3q5seq {
	public static int n = 0;
	public static int seed = 0;
	public static int[][] matrixA, matrixB, matrixC;
	public static String filename;

	/**
	 * main method
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 3) { // usage check
			usage();
		}
		n = Integer.parseInt(args[0]); // number of rows/columns
		seed = Integer.parseInt(args[1]); // see for rand
		filename = args[2]; // output file name
		// matrices
		matrixA = new int[n][n];
		matrixB = new int[n][n];
		matrixC = new int[n][n];

		long t1 = System.currentTimeMillis();
		Comm.init(args);

		// random number generation
		Random number = Random.getInstance(seed);
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matrixA[i][j] = number.nextInt(100);
			}
		}
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				matrixB[i][j] = number.nextInt(100);
			}
		}

		// matrix calculation
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				for (int k = 0; k < n; k++) {
					matrixC[i][j] = matrixC[i][j] + matrixA[i][k]
							* matrixB[k][j];
				}
			}
		}

		long t2 = System.currentTimeMillis();
		System.out.println((t2 - t1) + " msec");
		// output file to be written
		FileWriter fstream = new FileWriter(filename);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write("Matrix C : \n");
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				out.write(matrixC[i][j] + " ");
			}
			out.write("\n");
		}
		out.close();

	}

	private static void usage() {
		System.err.println("java hw3q4clu 9 1 output_parallel.bin");
		System.err.println("output file :: Name of the output file");
		System.exit(1);
	}
}
