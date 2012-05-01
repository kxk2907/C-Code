
/**
 * hw1q4seq.java
 *
 *
 * @version   $Id: hw1q1seq.java,v 1.6 
 * 2011/2/4 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * 
 * Revision 1.0     3/29/2011			10:05:00
 * Initial revision
 *
 */
import edu.rit.pj.IntegerForLoop;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;
import edu.rit.pj.Comm;

/**
 * Calculating cos x series in parallel
 * @author kbkarthi
 *
 */
public class hw1q4smp {
	static final int limit = 150000;
	static final double twopi = Math.PI * 2;
	static double[] result = new double[limit];

	public static void main(String[] args) {
		long t1 = System.currentTimeMillis();
		System.out.println();
		try {
			Comm.init(args);
			new ParallelTeam().execute(new ParallelRegion() {
				public void run() throws Exception {
					execute(0, limit - 1, new IntegerForLoop() {
						public void run(int first, int last) throws Exception {
							for (int x = first; x <= last; x++) {
								double x1 = ((double) x / 10) % twopi;
								double term = -(x1 * x1 / 2);
								double sum = 1;
								double tbs = term / sum;
								double tbs1 = tbs;
								sum = sum + term;
								int j = 4;
								if(tbs1 < 0)
									tbs1 = (-tbs);
								while (tbs1 > 0.0001) {
									double temp = ((double) j) * ((double) j - 1);
									double localterm = -(x1 * x1 / temp);
									term = term * localterm;
									tbs = term / sum;
									if(tbs1 < 0)
										tbs1 = (-tbs);
									else 
										tbs1 = tbs;
									sum = sum + term;
									j += 2;
								}
								result[x] = sum;
							}
						}
					});
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

		long t2 = System.currentTimeMillis();
		for (int x = 0; x < limit; x++) {
			System.out.println("For x :: " + ((double) x / 10) + " :: "
					+ result[x]);
		}
		System.out.println("Time :: " + (t2 - t1) + " msec");
	}
}
