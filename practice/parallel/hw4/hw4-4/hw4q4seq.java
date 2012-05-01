/**
 * hw4q4seq.java
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
 * Sequential program for doing matrix and vector multiplication
 * 
 * 
 * @author kbkarthi
 * 
 */
public class hw4q4seq {
	public static int m = 0;			//m rows
	public static int n = 0;			// n columns
	public static int seed = 0;			//seed for random number 
	public static int[][] matrixA; 		//matrix A (m x n)
	public static int[] vectorX, vectorY;	//vectorX (n x 1), vectorY (m x 1)
	public static String filename;		//output file name

	/**
	 * main method
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 4) { 			// usage check
			usage();
		}
		seed = Integer.parseInt(args[0]); 	// see for rand
		m = Integer.parseInt(args[1]);
		n = Integer.parseInt(args[2]);		// number of rows/columns
		filename = args[3]; 				// output file name
		
		// matrices and vectors 
		matrixA = new int[m][n];
		vectorX = new int[n];
		vectorY = new int[m];

		long t1 = System.currentTimeMillis();
		Comm.init(args);

		// random number generation
		Random number = Random.getInstance(seed);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				matrixA[i][j] = number.nextInt(10);
			}
		}
		for (int i = 0; i < n; i++) {
			vectorX[i] = number.nextInt(10);
		}
		
		// matrix-vectol multiplication calculation
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				vectorY[i] = vectorY[i] + matrixA[i][j] * vectorX[j];
			}
		}

		long t2 = System.currentTimeMillis();
		System.out.println((t2 - t1) + " msec");
		// output file to be written
		FileWriter fstream = new FileWriter(filename);
		BufferedWriter out = new BufferedWriter(fstream);
		out.write("Matrix C : \n");
		for (int i = 0; i < m; i++) {
			//System.out.println(vectorY[i] + " ");
			out.write(vectorY[i] + " ");
		}
		out.close();
		//System.out.println();
	}

	/**
	 * Usage check 
	 */
	private static void usage() {
		System.err.println("java hw4q4seq 9 1 output_parallel.bin");
		System.err.println("output file :: Name of the output file");
		System.exit(1);
	}
}
