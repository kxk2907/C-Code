/**
 * hw1q2smp.java
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
import edu.rit.pj.Comm;
import edu.rit.pj.IntegerForLoop;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;
import edu.rit.pj.reduction.SharedLong;
import edu.rit.util.Random;

/**
 * 
 * @author Karthikeyan Karur Balu 
 * 
 * Parallel program
 * To calculate the collapse of a set of N integers and find the ultimate sum
 *
 */
public class hw2q2smp {
	public static long array[]; //array of random numbers
	public static SharedLong sum = new SharedLong(); //shared integers
	public static int n;
	public static int ultimate;

	public static void main(String[] args) throws Exception {
		n = Integer.parseInt(args[0]);
		int seed = Integer.parseInt(args[1]);
		array = new long[n];
		Random number = Random.getInstance(seed);
		for (int i = 0; i < n; i++) 
			array[i] = number.nextInt(Integer.MAX_VALUE);  //generated upto MAXVALUE


		Comm.init(args);   //involing parallel
		long t1 = System.currentTimeMillis();     			//start time
		new ParallelTeam().execute(new ParallelRegion() {
			public void run() throws Exception {
				execute(0, n - 1, new IntegerForLoop() {
					public void run(int first, int last) throws Exception {
						long localsum = 0;
						for (int i = first; i <= last; i++) {
							localsum = localsum + array[i];	//svaing in localsum
						}
						sum.getAndAdd(localsum);			//adding to the shared variables
					}
				});
				}
			});
		
		Long temp = sum.get();
		int ultimate = 0;
		
		//count each number and when the number > 9, subtract it by 9 
		// for eg : 15678 = 1+5 < 9 then 6 + 6 > 9 => by subtracting 9 => 3 and continue to get further
		while(temp > 0){
			  ultimate = ultimate + (int) (temp % 10);
			  ultimate = (ultimate > 9) ? (ultimate - 9) : ultimate;
		      temp /= 10;
		}
		
		long t2 = System.currentTimeMillis();
		System.out.println(sum);
		System.out.println(ultimate);
		System.out.println((t2 - t1) + " msec");
	}
}
