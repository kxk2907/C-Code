/**
 * hw3q4clu.java
 *
 *
 * @version   $Id: hw3q4clu.java,v 1.6 
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
import edu.rit.mp.IntegerBuf;
import edu.rit.pj.Comm;
import edu.rit.pj.reduction.IntegerOp;
import edu.rit.util.Random;

/**
 * Cluster program for doing DNS matrix multiplication
 * 
 * 
 * @author Karthikeyan Karur Balu
 *
 */
public class hw3q5clu {
	public static int n = 0;				//# of rows/columns in the matrix
	public static int seed = 0;				// random number generation
	public static int[][][] matrixA, matrixB;
	public static int[][] matrixC;			//three matrix to be multiplied to each other
	public static IntegerBuf mysliceC;
	public static String filename;
	public static Random number;

	public static void main(String[] args) throws IOException {
		if (args.length != 3) { // usage check
			usage();
		}
		n = Integer.parseInt(args[0]);	//number of rows/columns 
		seed = Integer.parseInt(args[1]);	//see for rand 
		filename = args[2];				//output file name
		matrixA = new int[n][n][n];
		matrixB = new int[n][n][n];
		matrixC = new int[n][n];	
		long t1 = System.currentTimeMillis();
		
		Comm.init(args);	
		Comm world = Comm.world();
		int rank = world.rank();

		// Assigning the 3D matrix based on the rank. Each dimension 
		//contains appropriate values like specified in the document
		for (int i = 0; i < n; i++) {
			number = Random.getInstance(seed);
			number.skip(i * n + rank);
			int num = number.nextInt(100);
			for (int j = 0; j < n; j++) {
				matrixA[rank][i][j] = num;
			}
		}

		//MatrixB generation
		for (int i = 0; i < n; i++) {
			number = Random.getInstance(seed);
			number.skip(n * n + rank * n);
			for (int j = 0; j < n; j++) {
				matrixB[rank][i][j] = number.nextInt(100);
			}
		}

		//Doing multiplication within the matrix
		for(int i = 0;i<n;i++) {
			for(int j = 0;j<n;j++) {
				matrixC[i][j] = matrixA[rank][i][j] * matrixB[rank][i][j];
			}
		}
		
		//create a slice for matrixC 
		//using world.reduce to calculate the final sum
		mysliceC = IntegerBuf.buffer(matrixC);	
		world.reduce(0, mysliceC, IntegerOp.SUM);
		
		long t2 = System.currentTimeMillis();
		
		//printing the result into the file
		if(rank == 0) {
			System.out.println((t2-t1) + " msec");
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
	}
	/**
	 * Usage block
	 */
	private static void usage() {
		System.err.println("java hw3q4clu 9 1 output_parallel.bin");
		System.err.println("output file :: Name of the output file");
		System.exit(1);
	}
}
