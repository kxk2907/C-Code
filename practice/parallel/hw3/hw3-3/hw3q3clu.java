/**
 * hw3q3clu.java
 *
 *
 * @version   $Id: hw3q3clu.java,v 1.6 
 * 2011/2/4 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * 
 * Revision 1.0     3/29/2011			10:05:00
 * Initial revision
 *
 */

import java.io.IOException;
import edu.rit.mp.LongBuf;
import edu.rit.pj.Comm;
import edu.rit.util.Range;

/**
 * 
 * @author Karthikeyan Karur Balu
 * 
 *         To prove collatz conjecture
 * 
 */
public class hw3q3clu {
	public static long number = 0; // number of iterations
	public static long[][] matrix; // matrix for storing the result calculated
									// from different proc
	public static Range[] ranges; // ranges
	public static Range myrange;
	public static LongBuf[] slices; // integer buffers from matrix
	public static LongBuf myslice;

	/**
	 * 
	 * @param args
	 *            input arguments
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		number = Long.parseLong(args[0]); // number of iterations
		long maxiter = 0;
		long maxnumber = 1;
		long t1 = System.currentTimeMillis();
		Comm.init(args);
		Comm world = Comm.world();
		int size = world.size();
		int rank = world.rank();
		matrix = new long[size][2]; // matrix for storing result from differnt
									// matrices
		ranges = new Range(0, size - 1).subranges(size); // ranges
		slices = LongBuf.rowSliceBuffers(matrix, ranges); // slices
		myslice = slices[rank];

		// loop for the collatz conjecture
		for (int i = rank + 1; i <= number; i = i + size) {
			long x = i;
			long localmaxiter = 0;
			while (x > 1) {
				if ((x & 1) == 0)
					x = x / 2;
				else
					x = 3 * x + 1;
				localmaxiter++;
			}
			if (localmaxiter > maxiter) {
				maxiter = localmaxiter;
				maxnumber = i;
			}
		}
		matrix[rank][0] = maxiter; // maxiter for a specic local number
		matrix[rank][1] = maxnumber; // number for that maxiter
		world.gather(0, myslice, slices); // gather everything to the process 0
		if (rank == 0) {
			long finalmaxiter = matrix[0][0];
			long finalmaxnumber = matrix[0][1];
			for (int i = 1; i < size - 1; i++) {
				if (finalmaxiter < matrix[i][0]) {
					finalmaxiter = matrix[i][0];
					finalmaxnumber = matrix[i][1];
				}
			}
			long t2 = System.currentTimeMillis();
			System.out.println(finalmaxnumber);
			System.out.println((t2 - t1) + " msec");
		}
	}
}
