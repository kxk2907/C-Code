/**
 * hw1q4seq.java
 *
 *
 * @version   $Id: hw3q1seq.java,v 1.6 
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

public class hw3q1seq {
	static final int limit = 150000; // limit till which you need to execute

	/**
	 * Program to find the cos x series
	 * 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		if (args.length != 1) { // usage check
			usage();
		}
		FileWriter fstream = new FileWriter(args[0]); // file for writing output
														// values
		BufferedWriter out = new BufferedWriter(fstream);

		double twopi = Math.PI * 2;
		Comm.init(args); // backend process
		long t1 = System.currentTimeMillis(); // start time
		for (int x = 0; x < limit; x++) {
			double x1 = ((double) x / 10) % twopi;
			double term = -(x1 * x1 / 2);
			double sum = 1;
			double tbs = term / sum;
			double tbs1 = tbs;
			sum = sum + term;
			int j = 4;
			if (tbs1 < 0) // get the absolute value
				tbs1 = (-tbs);
			while (tbs1 > 0.0001) { // calculation block
				double temp = ((double) j) * ((double) j - 1);
				double localterm = -(x1 * x1 / temp);
				term = term * localterm;
				tbs = term / sum;
				if (tbs1 < 0)
					tbs1 = (-tbs);
				else
					tbs1 = tbs;
				tbs1 = Math.abs(tbs);
				sum = sum + term;
				j += 2;
			}
			out.write("For x :: " + ((double) x / 10) + " :: " + sum + "\n");
		}
		out.close();
		long t2 = System.currentTimeMillis();
		System.out.println("Time :: " + (t2 - t1) + " msec");
	}

	/**
	 * Usage block
	 */
	private static void usage() {
		System.err.println("java hw3q1seq <output file>");
		System.err.println("output file :: Name of the output file");
		System.exit(1);
	}
}
