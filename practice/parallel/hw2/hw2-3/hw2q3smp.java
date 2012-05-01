/**
 * hw2q3smp.java
 *
 *
 * @version   $Id: hw2q3smp.java,v 1.6 
 * 2011/2/4 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * 
 * Revision 1.0     3/29/2011			10:05:00
 * Initial revision
 *
 */
import edu.rit.pj.Comm;
import edu.rit.pj.IntegerForLoop;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;
import edu.rit.pj.reduction.SharedIntegerArray;
import edu.rit.util.Random;

/**
 * 
 * @author Karthikeyan Karur Balu
 * 
 *         PARALLLE PROGRAM Code to move the co-ordinates of a point 0,0 to some
 *         random locations based on the random value genarted
 * 
 */
public class hw2q3smp {
	public static int n = 0;
	public static int seed = 0;// seed for random number generation
	public static int[] move;// move array
	public static SharedIntegerArray location = new SharedIntegerArray(
			new int[3]);// shared array containing the addition of all final
						// location
	public static double distance = 0;

	public static void main(String[] args) throws Exception {
		seed = Integer.parseInt(args[0]);
		n = Integer.parseInt(args[1]);
		move = new int[n];
		Random number = Random.getInstance(seed);
		for (int i = 0; i < n; i++) {
			move[i] = number.nextInt(Integer.MAX_VALUE) % 6;// run upto MAXVALUE
		}

		Comm.init(args);
		long t1 = System.currentTimeMillis();
		new ParallelTeam().execute(new ParallelRegion() {
			public void run() throws Exception {
				execute(0, n - 1, new IntegerForLoop() {
					public void run(int first, int last) throws Exception {
						int[] locallocation = new int[3];
						//Doing a binary search for deciding which side to move based on all possible cases from 1 - 6 
						// binary search as starting 2, > 2 or < 2;
						//using macro to run in less time
						for (int i = first; i <= last; i++) {
							int nouse = (move[i] > 2) ? ((move[i] > 4) ? locallocation[2]++
									: ((move[i] > 3) ? locallocation[2]--
											: locallocation[1]++))
									: ((move[i] > 1) ? locallocation[1]--
											: ((move[i] > 0) ? locallocation[0]++
													: locallocation[0]--));
						}
						//adding to the final location from different processors
						for (int j = 0; j < 3; j++) {
							location.getAndAdd(j, locallocation[j]);
						}
					}
				});
			}
		});

		//getting x,y,z location
		int x = location.get(0);
		int y = location.get(1);
		int z = location.get(2);

		//calculating the distance from 0,0,0
		distance = Math.sqrt(x * x + y * y + z * z);
		long t2 = System.currentTimeMillis();
		System.out.println(x + ", " + y + ", " + z);
		System.out.println(distance);
		System.out.println((t2 - t1) + " msec");
	}
}
