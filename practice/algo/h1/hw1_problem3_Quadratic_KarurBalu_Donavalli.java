/**
 * hw1_problem2_Donavalli_KarurBalu.java
 *
 *
 * @version   $Id: hw1_problem3_Quadratic_Donavalli_KarurBalu.java,v 1.6 
 * 2010/12/7 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * @author    Venkat Sasank Donavalli
 * 
 * Revisions:
 *
 *      Revision 1.0     10/16/2010			10:05:00
 *      *      Initial revision
 *
 */
import java.util.Scanner;

/**
 * This program solves the hw1 problem3 question in O(n^2) 
 * time complexity using Bubble sort algorithm
 * 
 * @author	  Karthikeyan Karur Balu
 * @author    Venkat Sasank Donavalli
 */
public class hw1_problem3_Quadratic_KarurBalu_Donavalli {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int number = 0;											//input 'n' array size 
		Integer array[] = null;
		String str[] = null;
		try {
			if(sc.hasNextLine()) 
				number = Integer.parseInt(sc.nextLine());		//1st line size of array
			if(number != 0) {
				array = new Integer[number];
				str = new String[number];
			}
			if(sc.hasNextLine())  								//2nd line contents of array
				str = sc.nextLine().split(" ");
			for(int i = 0;i<number;i++) 						//Read input and pushing to integer array
				array[i] = Integer.parseInt(str[i]);
			quad(array,number);									//Quadratic sort call
			for(int i = 0;i<number;i++) 
				System.out.print(array[i] + " ");
			System.out.println();
		}
		catch(NumberFormatException nfe) {
			System.out.println("Illegal number Format !!");
			System.exit(1);
		}
		catch(ArrayIndexOutOfBoundsException aobe) {
			System.out.println("Array out of Bound Exception, check the # of numbers !!");
			System.exit(1);
		}
		catch(Exception e) {
			System.out.println("Other Exception !!");
			System.exit(1);
		}
	}
	/**
     * method to perform the bubble sort on the input array
     * compare each and every element in the array to each other.
     *  @param	
     *  		array - input array containing the unsorted list
     *  		number - input array size
     */
	public static void quad(Integer[] array,int number) {
		if(number == 0)
			return;
		for(int i = 0;i<number;i++) {
			for(int j = i+1;j<number;j++) {
				if(array[i] > array[j]) {
					int temp = array[i];
					array[i] = array[j];
					array[j] = temp;
				}
			}
		}
	}
}
