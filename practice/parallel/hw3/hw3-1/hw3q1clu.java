/**
 * hw1q4seq.java
 * 
 * 
 * @version $Id: hw1q1seq.java,v 1.6 2011/2/4 10:05:00 $
 * 
 * @author Karthikeyan Karur Balu
 * 
 *         Revision 1.0 3/29/2011 10:05:00 Initial revision
 * 
 */
import java.io.BufferedWriter;
import java.io.FileWriter;
import edu.rit.pj.Comm;

public class hw3q1clu {
	static final long limit = 150000; // limit till which you need to execute
	static Comm world;
	static int size;
	static int rank;
	static final double twopi = Math.PI * 2;
	static String filename;

	/**
	 * Program to find the cos x series
	 * 
	 * @param args
	 * @throws Exception
	 */
	public static void main(String[] args) throws Exception {
		if (args.length != 1) { // usage check
			usage();
		}
		filename = args[0]; // file for writing output values
		Comm.init(args);
		world = Comm.world();
		size = world.size();
		rank = world.rank();
		FileWriter fstream = new FileWriter(filename + "_" + rank);
		BufferedWriter out = new BufferedWriter(fstream);
		long t1 = System.currentTimeMillis(); // start time
		int range = (int) (limit / size);
		int first = rank * range;
		int last = (rank + 1) * range;
		for (long counter = first; counter < last; ++counter) {
			double x1 = ((double) counter / 10) % twopi;
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
			out.write("For x :: " + ((double) counter / 10) + " :: " + sum
					+ "\n");
		}
		out.close();

		long t2 = System.currentTimeMillis();
		System.out.println((t2 - t1) + " msec");
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
