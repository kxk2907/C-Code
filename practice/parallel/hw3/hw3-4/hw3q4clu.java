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
import edu.rit.util.Random;
import edu.rit.util.Range;

/**
 * Cluster program for doing matrix multiplication
 * 
 * 
 * @author kbkarthi
 *
 */
public class hw3q4clu {
	public static int n = 0;				//# of rows/columns in the matrix
	public static int seed = 0;				// random number generation
	public static int[][] matrixA, matrixB, matrixC;	//three matrix to be multiplied to each other
	public static IntegerBuf[] slicesA;		//Intger buffers for slicing the matrices
	public static IntegerBuf mysliceA;
	public static IntegerBuf[] slicesB;
	public static IntegerBuf mysliceB;
	public static IntegerBuf[] slicesC;
	public static IntegerBuf mysliceC;
	public static String filename;

	public static Random number;

	/**
	 * 
	 * @param args - check the usage for arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 3) { // usage check
			usage();
		}
		n = Integer.parseInt(args[0]);	//number of rows/columns 
		seed = Integer.parseInt(args[1]);	//see for rand 
		filename = args[2];				//output file name
		matrixA = new int[n][n];
		matrixB = new int[n][n];
		matrixC = new int[n][n];
		
		long t1 = System.currentTimeMillis();
		
		//cluster parallel program part 
		Comm.init(args);	
		Comm world = Comm.world();
		int size = world.size();
		int rootp = (int) Math.sqrt(size);
		int rank = world.rank();

		//row and col range for slicing the matrices
		Range row = new Range(0, n - 1);
		Range[] rowrange = row.subranges(rootp);
		Range col = new Range(0, n - 1);
		Range[] colrange = col.subranges(rootp);

		//finding the lower and upper bound of the slice
		int rowLB = rowrange[rank / rootp].lb();
		int rowUB = rowrange[rank / rootp].ub();
		int colLB = colrange[rank % rootp].lb();
		int colUB = colrange[rank % rootp].ub();

		//slicing the matrix based on the above calculated ranges
		slicesA = IntegerBuf.patchBuffers(matrixA, rowrange, colrange);
		mysliceA = slicesA[rank];
		slicesB = IntegerBuf.patchBuffers(matrixB, rowrange, colrange);
		mysliceB = slicesB[rank];
		slicesC = IntegerBuf.patchBuffers(matrixC, rowrange, colrange);
		mysliceC = slicesC[rank];

		//random generation for matrixA and matrixB
		//using skip inorder for seq and clu result to be same
		for (int i = rowLB; i <= rowUB; i++) {
			number = Random.getInstance(seed);
			number.skip(i * n + (rank % rootp) * (n / rootp));
			for (int j = colLB; j <= colUB; j++) {
				matrixA[i][j] = number.nextInt(100);
			}
		}

		for (int i = rowLB; i <= rowUB; i++) {
			number = Random.getInstance(seed);
			number.skip(n * n + i * n + (rank % rootp) * (n / rootp));
			for (int j = colLB; j <= colUB; j++) {
				matrixB[i][j] = number.nextInt(100);
			}
		}

		//Methods to distribute the rows and columns
		//General IDEA 
		//consider matrixA - distribute among the rows 
		// eg : [0,1,2] [3,4,5] [6,7,8] .. within the particular row it is distributed
		//consider matrixB - distribute among the columns
		// eg : [0,3,6] [1,4,7] [2,5,8] .. within the particular col it is distributed
		
		//everything from 1,2 is collected to proc 0
		if (rank % rootp == 0) {
			for (int j = 1; j < rootp; j++) {
				int dest = rank + j;
				world.receive(dest, slicesA[dest]);
			}
		} else { // from proc 1,2 is recoeved to proc 0
			int offset = rank % rootp;
			int dest = rank - offset;
			world.send(dest, mysliceA);
		}

		//everything from proc 0 is sent to proc 1,2 and recieved by proc 1,2
		if (rank % rootp == 0) {
			for (int j = 1; j < rootp; j++) {
				int dest = rank + j;
				for (int i = rank; i < rank + rootp; i++) {
					world.send(dest, slicesA[i]);
				}
			}
		} else {
			int offset = rank % rootp;
			int dest = rank - offset;
			for (int i = dest; i < dest + rootp; i++) {
				world.receive(dest, slicesA[i]);
			}
		}

		//for matrixB part
		//specific to proc 3,6 is sent to proc 0
		
		if (rank / rootp == 0) {
			for (int j = 1; j < rootp; j++) {
				int dest = rank + j * rootp;
				world.receive(dest, slicesB[dest]);
			}
		} else {
			int dest = rank % rootp;
			world.send(dest, mysliceB);
		}

		//everything from proc 0 si shared to proc 3,6
		if (rank / rootp == 0) {
			for (int j = 1; j < rootp; j++) {
				int dest = rank + j * rootp;
				for (int i = rank; i < size; i = i + rootp) {
					world.send(dest, slicesB[i]);
				}
			}
		} else {
			int dest = rank % rootp;
			for (int i = dest; i < size; i = i + rootp) {
				world.receive(dest, slicesB[i]);
			}
		}

		//perform matrix calculation for each block seperate processes
		// it is defined by LB and UB values
		for (int i = rowLB; i <= rowUB; i++) {
			for (int j = colLB; j <= colUB; j++) {
				for (int k = 0; k < n; k++) {
					matrixC[i][j] = matrixC[i][j] + matrixA[i][k]
							* matrixB[k][j];
				}
			}
		}

		//gather all the elements to proc 0 
		world.gather(0, mysliceC, slicesC);

		long t2 = System.currentTimeMillis();

		//display the result
		if (rank == 0) {
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

	//usage
	private static void usage() {
		System.err.println("java hw3q4clu 9 1 output_parallel.bin");
		System.err.println("output file :: Name of the output file");
		System.exit(1);
	}
}
