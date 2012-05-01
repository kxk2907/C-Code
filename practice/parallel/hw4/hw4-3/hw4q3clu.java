/**
 * hw4q3clu.java
 *
 *
 * @version   $Id: hw4q3clu.java,v 1.6 
 * 2011/2/4 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * 
 * Revision 1.0     3/29/2011			10:05:00
 * Initial revision
 *
 */

import java.io.IOException;
import edu.rit.mp.IntegerBuf;
import edu.rit.pj.Comm;
import edu.rit.pj.reduction.IntegerOp;
import edu.rit.util.Random;

/**
 * Cluster program for moore's algorithm for travelling salesperson
 * 
 * @author Karthikeyan Karur Balu
 *
 */
public class hw4q3clu {
	public static int seed;
	public static int n;
	public static int block;
	public static boolean uneven = false;
	public static int[] result = new int[2];

	/**
	 * Main program 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		seed = Integer.parseInt(args[0]);
		n = Integer.parseInt(args[1]);

		//cluster inits
		Comm.init(args);
		Comm world = Comm.world();
		int size = world.size();
		int rank = world.rank();
		block = n / size;
		int diff = n - block * size;

		long t1 = System.currentTimeMillis();

		if (rank == 0) {
			block += diff;
		}

		//random integer generation
		Random random = Random.getInstance(seed);
		int lb = 0;
		int ub = 0;

		if (rank == 0) {
			lb = rank * block;
			ub = lb + block;
		} else {
			lb = rank * block + diff;
			ub = lb + block;
		}
		//uppper and lower bound calculation
		random.skip(lb);
		IntegerBuf buf = IntegerBuf.buffer(result);
		
		//mooore's algorithm for east, west, north and south
		for (int i = lb; i < ub; i++) {
			int next = random.nextInt(8);
			if (next <= 3)
				result[0]++;
			else if (next <= 5)
				result[0]--;
			else if (next == 6)
				result[1]++;
			else if (next == 7)
				result[1]--;
		}
		
		//reduce from all the processors
		world.reduce(0, buf, IntegerOp.SUM);
		long t2 = System.currentTimeMillis();
		if (rank == 0) {
			System.out.println(result[0] + "," + result[1]);
			System.out.println((t2 - t1) + " msec");
		}
	}
}
