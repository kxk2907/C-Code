/**
 * hw4q1clu.java
 *
 *
 * @version   $Id: hw4q1clu.java,v 1.6 
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
import edu.rit.mp.buf.IntegerItemBuf;
import edu.rit.pj.Comm;
import edu.rit.util.Random;
import edu.rit.util.Range;

/**
 * Cluster parallel program for odd and even sort
 * 
 * @author Karthikeyan Karur Balu
 *
 */
public class hw4q1clu {
	static int seed;				//seed for random numbers
	static int n;					//# of numbers to be sorted
	static int[] array;				//array of numbers
	static String filename;			//output filename
	public static Range[] ranges; 	//slicing the matrix based on rows
	public static Range myrange;	//rank specific range
	public static IntegerBuf[] slices; // integer buffers from matrix
	public static IntegerBuf myslice;
	public static int lb;			//lower and uppper bounds
	public static int ub;

	public static void main(String[] args) throws IOException {
		if (args.length != 3)		//usage check
			Usage();
		//arguments read from user
		seed = Integer.parseInt(args[0]);
		n = Integer.parseInt(args[1]);
		filename = args[2];
		array = new int[n];
		
		//cluster sections
		Comm.init(args);
		Comm world = Comm.world();
		int size = world.size();
		int rank = world.rank();

		//splitting the matrix 
		ranges = new Range(0, n - 1).subranges(size); // ranges
		myrange = ranges[rank];
		slices = IntegerBuf.sliceBuffers(array, ranges);
		myslice = slices[rank];
		lb = myrange.lb();
		ub = myrange.ub();

		long t1 = System.currentTimeMillis();
		Random random = Random.getInstance(seed);
		
		//skipping to maintian consitency between the ouputs. 
		random.skip(rank * n / size);
		for (int i = lb; i <= ub; i++) {
			array[i] = random.nextInt(Integer.MAX_VALUE);
		}
		
		//odd and even sort 
		//during the odd phase there should be an exchange between the processors
		//this exchange is taken care by the send and recieve methods as represented below
		for (int i = 0; i < n; i++) {
			if (i % 2 == 0) {	//even position numbers swapped here
				for (int j = lb; j <= ub; j += 2) {
					if (((j + 1) <= ub) && (array[j] > array[j + 1])) {
						int temp = array[j];
						array[j] = array[j + 1];
						array[j + 1] = temp;
					}
				}
			} else {			//odd position numbers swapped here 
				for (int j = (lb + 1); j < ub; j += 2) {
					if (array[j] > array[j + 1]) {
						int temp = array[j];
						array[j] = array[j + 1];
						array[j + 1] = temp;
					}
				}
				//Logic used :: 
				//lets say 8 processors 
				//0,1,2,3,4,5,6,7
				//0,1,2,3,4,5,6 should send its upper bound to 1,2,3,4,5,6,7 respectively
				//1,2,3,4,5,6,7 should send its lower bound to 0,1,2,3,4,5,6 respectively
				if (rank % 2 == 0) { //send and receive between the processes
					IntegerItemBuf sendbuf = IntegerBuf.buffer();
					sendbuf.item = array[ub];
					if ((rank + 1) != size)
						world.send(rank + 1, sendbuf);
					IntegerItemBuf recvbuf = IntegerBuf.buffer();
					if ((rank + 1) != size)
						world.receive(rank + 1, recvbuf);
					int local = array[ub];
					int remote = recvbuf.item;
					if (local > remote)
						array[ub] = remote;
					if (rank != 0) {
						IntegerItemBuf recvbuf1 = IntegerBuf.buffer();
						world.receive(rank - 1, recvbuf1);
						IntegerItemBuf sendbuf1 = IntegerBuf.buffer();
						sendbuf1.item = array[lb];
						world.send(rank - 1, sendbuf1);
						int remote1 = recvbuf1.item;
						int local1 = array[lb];
						if (local1 < remote1)
							array[lb] = remote1;
					}
				} else {
					int local = array[lb];
					IntegerItemBuf recvbuf = IntegerBuf.buffer();
					world.receive(rank - 1, recvbuf);
					int remote = recvbuf.item;
					IntegerItemBuf sendbuf = IntegerBuf.buffer();
					sendbuf.item = local;
					world.send(rank - 1, sendbuf);
					if (local < remote)
						array[lb] = remote;
					if (rank != (size - 1)) {
						IntegerItemBuf sendbuf1 = IntegerBuf.buffer();
						sendbuf1.item = array[ub];
						world.send(rank + 1, sendbuf1);
						int local1 = array[ub];
						IntegerItemBuf recvbuf1 = IntegerBuf.buffer();
						world.receive(rank + 1, recvbuf1);
						int remote1 = recvbuf1.item;
						if (local1 > remote1)
							array[ub] = remote1;
					}
				}
			}
		}

		//Gather all the individual result from different processor to processor 0
		world.gather(0, myslice, slices);
		if (rank == 0) {
			FileWriter fstream = new FileWriter(filename);
			BufferedWriter out = new BufferedWriter(fstream);
			for (int i = 0; i < n; i++) {
				out.write(array[i] + " ");
			}
			out.write("\n");
			out.close();
		}
		long t2 = System.currentTimeMillis();
		System.out.println((t2 - t1) + " msec");
	}

	private static void Usage() {
		System.err.println("Usgae : java hw4q1clu <seed> <number> ");
		System.exit(1);
	}
}
