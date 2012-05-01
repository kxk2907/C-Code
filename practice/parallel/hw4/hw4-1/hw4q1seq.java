/**
 * hw4q1seq.java
 *
 *
 * @version   $Id: hw4q1seq.java,v 1.6 
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
 * Sequential program for odd and even sort 
 * 
 * @author Karthikeyan Karur Balu
 *
 */
public class hw4q1seq {
	static int seed;			//seed for random numbers
	static int n;				//# of numbers to be sorted
	static int[] array;			//array of numbers
	static String filename;		//output filename
	
	/**
	 * Main program 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 3)	//usage check
			Usage();
		//arguments read from user
		seed = Integer.parseInt(args[0]);	
		n = Integer.parseInt(args[1]);
		filename = args[2];
		array = new int[n + 1];
		
		//cluster sections
		Comm.init(args);
		FileWriter fstream = new FileWriter(filename);
		BufferedWriter out = new BufferedWriter(fstream);

		long t1 = System.currentTimeMillis();
		Random random = Random.getInstance(seed);
		for (int i = 1; i <= n; i++) {
			array[i] = random.nextInt(Integer.MAX_VALUE);
		}
		
		//algorithm for odd and even sort as given in the question
		for (int i = 1; i <= n; i++) {
			if (i % 2 != 0) {
				for (int j = 0; j < n / 2; j++) {
					int lower = 2 * j + 1;
					int higher = 2 * j + 2;
					if (array[lower] > array[higher]) {
						int temp = array[lower];
						array[lower] = array[higher];
						array[higher] = temp;
					}
				}
			} else {
				for (int j = 0; j < n / 2; j++) {
					int lower = 2 * j;
					int higher = 2 * j + 1;
					if (array[lower] > array[higher]) {
						int temp = array[lower];
						array[lower] = array[higher];
						array[higher] = temp;
					}
				}
			}
		}
		
		//display the result
		for (int i = 1; i <= n; i++) {
			out.write(array[i] + " ");
		}
		out.write("\n");
		out.close();
		long t2 = System.currentTimeMillis();
		System.out.println((t2 - t1) + " msec");
	}

	private static void Usage() {
		System.err.println("Usgae : java hw4q1seq <seed> <number> ");
		System.exit(1);
	}
}
