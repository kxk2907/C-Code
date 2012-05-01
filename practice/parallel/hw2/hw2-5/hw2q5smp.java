/**
 * hw2q5smp.java
 *
 *
 * @version   $Id: hw2q5smp.java,v 1.6 
 * 2011/2/4 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * 
 * Revision 1.0     3/29/2011			10:05:00
 * Initial revision
 *
 */
import java.util.ArrayList;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;
import edu.rit.pj.Comm;

/**
 * PARALLEL program
 * 
 * Program to find prime numbers in a range using the algo given 
 * 
 * @author Karthikeyan Karur Balu
 *
 */
public class hw2q5smp {
	static int n = 0;
	static boolean[] array;	// input array containing all n numbers 
	static boolean[] sqrtarray;	// sqrtarray containing numbers between 0 to sqrt(n)
	static int sqrt;
	static ArrayList<Integer> al = new ArrayList<Integer>();	//list of integers

	public static void main(String[] args) throws Exception {
		n = Integer.parseInt(args[0]);
		sqrt = (int) Math.sqrt(n);
		array = new boolean[n + 1];
		sqrtarray = new boolean[sqrt + 1];

		int k = 2;	//starting point 
		int numbers = 0;	//starting numbers calculation
		
		Comm.init(args);
		long t1 = System.currentTimeMillis();
		//adding to sqrt array
		while (k <= sqrt) {
			for (int i = k + k; i <= sqrt; i = i + k) {
				sqrtarray[i] = true;
			}
			k++;
			while (k <= sqrt && array[k])
				k++;
		}

		for (int i = 2; i <= sqrt; i++) {
			if (!sqrtarray[i]) {
				numbers++;
				al.add(i);
			}
		}
		//parallel section to count the number of numbers for the counting 
		//massively parallel program
		new ParallelTeam(numbers).execute(new ParallelRegion() {
			public void run() {
				int i = al.get(getThreadIndex());
				for (int j = i + i; j <= n; j += i)
					array[j] = true;
			}
		});

		long t2 = System.currentTimeMillis();
		int sum = 0;

		//displaying result
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
