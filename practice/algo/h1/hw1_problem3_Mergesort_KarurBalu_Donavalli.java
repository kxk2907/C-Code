/**
 * hw1_problem2_Donavalli_KarurBalu.java
 *
 *
 * @version   $Id: hw1_problem3_Mergesort_Donavalli_KarurBalu.java,v 1.6 
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
 * This program solves the hw1 problem3 question in O(nlogn) 
 * time complexity using Mergesort algorithm
 * 
 * @author	  Karthikeyan Karur Balu
 * @author    Venkat Sasank Donavalli
 */
public class hw1_problem3_Mergesort_KarurBalu_Donavalli {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int number = 0;										//input 'n' array size 
		Integer array[] = null;								
		String str[] = null;
		try {
			if(sc.hasNextLine()) 
				number = Integer.parseInt(sc.nextLine());	//1st line size of array
			if(number != 0) {
				array = new Integer[number];
				str = new String[number];
			}
			if(sc.hasNextLine())  							//2nd line contents of array
				str = sc.nextLine().split(" ");
			for(int i = 0;i<number;i++) 
				array[i] = Integer.parseInt(str[i]);		//Read input and pushing to integer array
			mergeSort(array,0,number-1);					//mergeSort call
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
     * method to perform the merge sort on the input array
     * 
     *  @param	
     *  		array - input array containing the unsorted list
     *  		p - lowerbound (starts with 0)
     * 			r - uppperbound (starts with n-1)
     */
	public static void mergeSort(Integer[] array,int p,int r) {
		int q = 0;
		if(p < r) {
			q = (p+r)/2;										//finding mid = (0 + arraysize - 1)/2
			mergeSort(array,p,q);								//mergesort on 1st half
			mergeSort(array,q+1,r);								//2nd half
			merge(array,p,q,r);									//merge the sorted 2 halves
		}
	}
	
	/**
     * method to perform the merge operations
     * 
     *  @param	
     *  		array - input array containing the unsorted list
     *  		p-q - 1st range to be merged with q-r (2nd range)
     * 			
     */
	public static void merge(Integer[] array,int p,int q,int r) {
		int n1 = q-p+1;											
		int n2 = r-q;
		Integer[] A = new Integer[n1];							//two seperate arrays are created
		Integer[] B = new Integer[n2];
		for(int i = 0;i<n1;i++) 								//p to q copied to A
			A[i] = array[i+p];
		for(int i = 0;i<n2;i++)									//q to r copied to B
			B[i] = array[i+q+1];
		for(int i=p,j=0,k=0;i<=r;i++) {							//merging A and B and storing in array
			if(j < n1 && k < n2 ) {
				if(A[j] < B[k]) {
					array[i] = A[j];
					j++;
				}
				else {
					array[i] = B[k];
					k++;
				}
			}
			else if(j >= n1 && k >= n2) 
				return;
			else {
				if(j >= n1) {
					array[i] = B[k];
					k++;
				}
				else if(k >= n2) {
					array[i] = A[j];
					j++;
				}
			}
		}
		return;
	}
}

