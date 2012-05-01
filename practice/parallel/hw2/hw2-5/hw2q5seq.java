/**
 * hw2q5seq.java
 *
 *
 * @version   $Id: hw2q5seq.java,v 1.6 
 * 2011/2/4 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * 
 * Revision 1.0     3/29/2011			10:05:00
 * Initial revision
 *
 */
import java.util.ArrayList;

/**
 * SEQ program
 * 
 * Program to find prime numbers in a range using the algo given 
 * 
 * @author Karthikeyan Karur Balu
 *
 */
public class hw2q5seq {
	static int n = 0;
	static boolean[] array;				// input array containing all n numbers 
	static boolean[] sqrtarray;			// sqrtarray containing numbers between 0 to sqrt(n)
	static int sqrt;
	static ArrayList<Integer> al = new ArrayList<Integer>();	//list of integers

	public static void main(String[] args) throws Exception {
		n = Integer.parseInt(args[0]);
		sqrt = (int) Math.sqrt(n);
		array = new boolean[n + 1];
		sqrtarray = new boolean[sqrt + 1];

		int k = 2;						//starting point 
		int numbers = 0;				//starting numbers calculation
		
		long t1 = System.currentTimeMillis();
		while (k <= sqrt) {				//finding the sqrts
			for (int i = k + k; i <= sqrt; i = i + k) {
				sqrtarray[i] = true;
			}
			k++;
			while (k <= sqrt && array[k])
				k++;
		}

		//adding to sqrt array
		for (int i = 2; i <= sqrt; i++) {
			if (!sqrtarray[i]) {
				numbers++;
				al.add(i);
			}
		}

		// counting the numbers 
		for(int i = 0;i<numbers;i++) {
			int l = al.get(i);
			for (int j = l + l ; j <= n; j += l)
				array[j] = true;
		}
		
		long t2 = System.currentTimeMillis();
		int sum = 0;

		//printing result
		for (int i = 2; i <= n; i++) {
			if (!array[i]) {
				sum++;
				System.out.print(i + " ");
			}
		}
		System.out.println();
		System.out.println(sum + " primes found between 2 and " + n);
		System.out.println("Total Time :: " + (t2 - t1) + " msec");
	}
}
