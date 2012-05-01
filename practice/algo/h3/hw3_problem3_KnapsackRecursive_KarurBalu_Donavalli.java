/**
 * hw2_problem2_KarurBalu_Donavalli_.java
 *
 *
 * @version   $Id: hw3_problem3_KnapsackRecursive_KarurBalu_Donavalli.java,v 1.1 
 * 2010/12/17 10:05:00 $
 *
 * @author    Venkat Sasank Donavalli
 * @author	  Karthikeyan Karur Balu
 * 
 * 
 * Revisions:
 *
 *      Revision 1.0     10/16/2010			10:05:00
 *      *      Initial revision
 *
 */

import java.util.Scanner;

/**
 * Program to find the Max size in a Knapsack using Rcursive method 
 * 
 *  * @author    Venkat Sasank Donavalli
 * @author	  Karthikeyan Karur Balu
 */
public class hw3_problem3_KnapsackRecursive_KarurBalu_Donavalli {
	
	/**
	 * main method
	 * 
	 *@param		args	command line arguments
	 *
	 */ 
	public static void main(String args[]) {
		double start = System.currentTimeMillis();	
		Scanner sc = new Scanner(System.in);
		Integer n, W;
		Integer max;
		String[] temp = new String[2];
		try {
			if (sc.hasNextLine())
				temp = sc.nextLine().split(" ");
			n = Integer.parseInt(temp[0]);
			W = n/2;
			Integer cw = 1;
			max = knapsackIndivisibleRecursive(n, cw, W);
			System.out.println(max);
		} catch (NumberFormatException nfe) { // Exception handling
			System.out.println("Illegal number Format !!");
			System.exit(1);
		} catch (ArrayIndexOutOfBoundsException aobe) {
			System.out
					.println("Array out of Bound Exception, check the # of numbers !!");
			System.exit(1);
		} catch (Exception e) {
			System.out.println("Other Exception !!");
			System.exit(1);
		}
		double end = System.currentTimeMillis();
				System.out.println("Time : " + (end-start));
	}
	
	/**
	 * 
	 * @param n  - number of different items available
	 * @param cw - class that stores the cost and weight of each item
	 * @param W	 - Max weight of the knapsack
	 * @return	 - Integer that max weight that the knapsack can hold
	 */
	public static Integer knapsackIndivisibleRecursive(Integer n,
			Integer cw, Integer W) {
		if (n <= 0)
			return 0;
		Integer withLastItem;
		if (W < cw)
			withLastItem = -1;
		else
			withLastItem = cw
					+ knapsackIndivisibleRecursive(n - 1, cw,
							W - cw);
		Integer withoutLastItem = knapsackIndivisibleRecursive(n - 1, cw, W);
		if (withoutLastItem > withLastItem)
			return withoutLastItem;
		else
			return withLastItem;
	}
}

