/**
 * hw2q3seq.java
 *
 *
 * @version   $Id: hw2q1seq.java,v 1.6 
 * 2011/2/4 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * 
 * Revision 1.0     3/29/2011			10:05:00
 * Initial revision
 *
 */
import edu.rit.pj.reduction.SharedIntegerArray;
import edu.rit.util.Random;

/**
 * 
 * @author Karthikeyan Karur Balu
 * 
 * SEQUENTIAL 
 *         Code to move the co-ordinates of a point 0,0 to some random locations
 *         based on the random value genarted
 * 
 */
public class hw2q3seq {
	public static int n = 0;
	public static int seed = 0;			//seed for random number generation
	public static int[] move;			//move array
	public static SharedIntegerArray finallocation = new SharedIntegerArray(
			new int[3]); // shared array containing the addition of all final
							// location
	public static int[] location = new int[3];
	public static double distance = 0;

	public static void main(String[] args) {
		seed = Integer.parseInt(args[0]);
		n = Integer.parseInt(args[1]);
		move = new int[n];
		Random number = Random.getInstance(seed);
		for (int i = 0; i < n; i++) {
			move[i] = number.nextInt(Integer.MAX_VALUE) % 6;		// run upto MAXVALUE
		}
		long t1 = System.currentTimeMillis();
		//Doing a binary search for deciding which side to move based on all possible cases from 1 - 6 
		// binary search as starting 2, > 2 or < 2;
		//using macro to run in less time
		for (int i = 0; i < n; i++) {
			int nouse = (int) ((move[i] > 2) ? ((move[i] > 4) ? location[2]++
					: ((move[i] > 3) ? location[2]-- : location[1]++))
					: ((move[i] > 1) ? location[1]--
							: ((move[i] > 0) ? location[0]++ : location[0]--)));
		}
		//adding to the final location
		for (int j = 0; j < 3; j++) {
			finallocation.getAndAdd(j, location[j]);
		}

		int x = finallocation.get(0);	//getting x,y,z location
		int y = finallocation.get(1);
		int z = finallocation.get(2);

		//calculating the distance from 0,0,0
		distance = Math.sqrt(x * x + y * y + z * z);
		long t2 = System.currentTimeMillis();
		System.out.println(x + ", " + y + ", " + z);
		System.out.println(distance);
		System.out.println((t2 - t1) + " msec");
	}
}
