/**
 * hw4q4clu.java
 *
 *
 * @version   $Id: hw4q4clu.java,v 1.6 
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
 * Cluster program for doing matrix-vector multiplication
 * 
 * 
 * @author kbkarthi
 * 
 */
public class hw4q4clu {
	public static int m = 0;			//m rows
	public static int n = 0;			//n columns
	public static int seed = 0;			//seed for rand
	public static int blockrow = 0;		//rowblock
	public static int blockcol = 0;		//colblock
	public static int[][] matrixA;		//input matrix
	public static int[] vectorX, vectorY;	//input and output vectors
	public static String filename;		//.output filename
	public static Range[] rangerow, rangecol;	//row and col ranges
	public static Range myrangerow, myrangecol;	//current row and col range
	public static IntegerBuf[] slicesA; // integer buffers from matrix
	public static IntegerBuf mysliceA;
	public static IntegerBuf[] slicesX; // integer buffers from matrix
	public static IntegerBuf mysliceX;
	public static IntegerBuf[] slicesY; // integer buffers from matrix
	public static IntegerBuf mysliceY;
	public static int rowlb;			//lower and upper bounds
	public static int rowub;
	public static int collb;
	public static int colub;

	/**
	 * main method
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 4) { // usage check
			usage();
		}
		seed = Integer.parseInt(args[0]); // see for rand
		m = Integer.parseInt(args[1]);
		n = Integer.parseInt(args[2]); // number of rows/columns
		filename = args[3]; // output file name
		// matrices and vectors
		matrixA = new int[m][n];
		vectorX = new int[n];
		vectorY = new int[m];

		long t1 = System.currentTimeMillis();
		Comm.init(args);
		Comm world = Comm.world();
		int size = world.size();
		int rank = world.rank();
		blockrow = m / size;
		blockcol = n / size;

		//split the matrixA into rows equally for processors
		rangerow = new Range(0, m - 1).subranges(size);
		myrangerow = rangerow[rank];
		rowlb = myrangerow.lb();
		rowub = myrangerow.ub();

		//split the vectorX into cols equally for processors
		rangecol = new Range(0, n - 1).subranges(size);
		myrangecol = rangecol[rank];
		collb = myrangecol.lb();
		colub = myrangecol.ub();

		//row slicing in matrixA and slicing in vectorX
		slicesA = IntegerBuf.rowSliceBuffers(matrixA, rangerow);
		mysliceA = slicesA[rank];
		slicesX = IntegerBuf.sliceBuffers(vectorX, rangecol);
		mysliceX = slicesX[rank];
		slicesY = IntegerBuf.sliceBuffers(vectorY, rangerow);
		mysliceY = slicesY[rank];

		Random number = Random.getInstance(seed);

		number.skip(rowlb * n);
		// matrixA generated by idividual processors
		for (int i = rowlb; i <= rowub; i++) {
			for (int j = 0; j < n; j++) {
				matrixA[i][j] = number.nextInt(10);
			}
		}
		
		number = Random.getInstance(seed);
		number.skip(m * n + collb);
		//vectorX generated by individual processors
		for (int i = collb; i <= colub; i++) {
			vectorX[i] = number.nextInt(10);
		}

		//allGather in sliceX
		world.allGather(0, mysliceX, slicesX);
		
		//matrix-vector calculation performed in individual processor
		for (int i = rowlb; i <= rowub; i++) {
			for (int j = 0; j < n; j++) {
				vectorY[i] = vectorY[i] + matrixA[i][j] * vectorX[j];
			}
		}
		
		//gather everything to processor 0
		world.gather(0, mysliceY, slicesY);
		long t2 = System.currentTimeMillis();
		System.out.println((t2 - t1) + " msec");

		//display the result
		if (rank == 0) {
			FileWriter fstream = new FileWriter(filename);
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("Matrix C : \n");
			for (int i = 0; i < m; i++) {
				out.write(vectorY[i] + " ");
			}
			out.close();
		}
	}

	//usage check 
	private static void usage() {
		System.err.println("java hw4q4seq 9 1 output_parallel.bin");
		System.err.println("output file :: Name of the output file");
		System.exit(1);
	}
}