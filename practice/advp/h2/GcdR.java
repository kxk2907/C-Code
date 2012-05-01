/**
 * Find GCD of a series (both recursive and iterative algo)
 *
 *
 * @version   $Id: GcdR.java,v 1.1 2010/09/12 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu)
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/19 12:58:00  kxk2907
 *      Initial revision
 *
 */

import java.util.LinkedList;
import java.util.Queue;

/*
 * To find the gcd of a given set of numbers (used recursive and iterative method)
 * 
 * Recursive method : 
 * 			1. Queue containing the set of numbers is sent to the method.
 * 			2. pop out first two numbers in the queue, find gcd.
 * 			3. Recursively find the gcd with above gcd by popping out 1 number each time 
 * 			4. Return the gcd when the queue is empty
 * 
 * Iterative Method :
 * 			Almost the same method except recurion to find out the gcd.
 * 
 * Algo to find GCD : find modulo for num2,num1 (lowest of num1 and num2), highest number 
 * 			in range 0 to num1 where num1 and num2 divisble by the number
 * 
 * Sample : ]java GcdR 4 8 10 12
 * Iterative method : 2
 * Recursive method : 2
 */

class GcdR {
	public static void main(String args[]) {
		Queue<String> q1 = new LinkedList<String>();
		Queue<String> q2 = new LinkedList<String>();
		for(String arg : args) {
			q1.add(arg);						// Queue for iterative method
			q2.add(arg);						// Queue for recursive method
		}
		System.out.println("Iterative method : " + iterativeGcd(q1));
		System.out.println("Recursive method : " + recursiveGcd(Integer.parseInt(q2.remove()),q2,0));
	}
	public static int recursiveGcd (int num1,Queue<String> q1, int gcd){
		if(q1.isEmpty()) 
			return gcd;						// If empty queue return current value stored in gcd variable
		else {
			int num2;
			if(Integer.parseInt((String) q1.peek()) < num1) { // num1 shud be lowest if num2 > num2
				num2 = num1;									// Exchange num1 and num2
				num1 = Integer.parseInt((String) q1.remove());
			}
			else {
				num2 = Integer.parseInt((String) q1.remove());
			}
			for(int i = 1;i<=num1;i++) {					// find largest number that divides num1 and num2
				if((num1 % i == 0) && (num2 % i == 0))		
				gcd = i;
			}
			if (num1 == 0 || num2 == 0) gcd = 0;
		}
		return recursiveGcd(gcd,q1,gcd);					// recursively find gcd with a earlier gcd in previous recursion
	}
	public static int iterativeGcd (Queue<String> q1) {		// Iterative method
		int num11; 
		int num12;
		int gcd = 0;
		if(q1.isEmpty()) return 0;
		num11 = Integer.parseInt(q1.remove());
		while (!q1.isEmpty()) {								// Find it until the list become empty 
			num12 = Integer.parseInt(q1.remove());
			if(num11 > num12) {								// Interchange num1 and num2 if num2 > num1
				int temp = num11;
				num11 = num12;
				num12 = temp;
			}
			for(int i = 1;i<=num11;i++) {
				if((num11 % i == 0) && (num12 % i == 0))
				gcd = i;
			}
			if (num11 == 0 || num12 == 0) gcd = 0;
			num11 = gcd;
		}
		return gcd;
	}
}
