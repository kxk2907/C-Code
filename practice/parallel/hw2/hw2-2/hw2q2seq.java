/**
 * hw1q2seq.java
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

import edu.rit.pj.reduction.SharedLong;
import edu.rit.util.Random;

/**
 * 
 * @author Karthikeyan Karur Balu 
 * 
 * Sequential program
 * To calculate the collapse of a set of N integers and find the ultimate sum
 *
 */
public class hw2q2seq {
	static long array[];		//array of numbers
	//sharedLong among the processors
	public static SharedLong sum = new SharedLong();	

	public static void main(String[] args) {
		int n = Integer.parseInt(args[0]);
		int seed = Integer.parseInt(args[1]);
		array = new long[n];
		Random number = Random.getInstance(seed);		//generating random based on seed value
		for (int i = 0; i < n; i++) 
			array[i] = number.nextInt(Integer.MAX_VALUE);

		long t1 = System.currentTimeMillis();
		
		long locallong = 0;
		for (int i = 0; i < n; i++) {
			locallong = locallong + array[i];
		}
		sum.getAndAdd(locallong);
		
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
