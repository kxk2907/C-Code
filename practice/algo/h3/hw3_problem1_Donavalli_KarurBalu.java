
/**
 * hw2_problem2_KarurBalu_Donavalli_.java
 *
 *
 * @version   $Id: hw2_problem2_KarurBalu_Donavalli_.java,v 1.6 
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
 * Program to calculate the sum of distances from the most profitable coordinate that
 * minimizes the sum of the distances to every house.
 * 
 * @author    Venkat Sasank Donavalli
 * @author	  Karthikeyan Karur Balu
 *
 */ 
public class hw3_problem1_Donavalli_KarurBalu {

	/**
	 * main method
	 * 
	 *@param		args	command line arguments
	 *
	 */ 
	public static void main( String args[] ){
		hw3_problem1_Donavalli_KarurBalu o = new hw3_problem1_Donavalli_KarurBalu();
        Scanner sc = new Scanner(System.in );
        int n = 0;
		String str[] = null;
		float a[] = null;
		try {
			if(sc.hasNextLine())
			        n = Integer.parseInt(sc.nextLine());
			a = new float[n];
			if(sc.hasNextLine())
			        str = sc.nextLine().split(" ");
			for( int i = 0; i < n; i++ ){
				a[i] = Float.parseFloat(str[i]);
			}
		}
		catch(Exception e){
			System.out.println("Input Error !!");
			System.exit(1);
		}
		float temp = o.select( a, n / 2, n );
		System.out.println( (int)o.calculate(temp, a, n) );
		
	}
	
	
	/**
	 * Method to find the median value at a given position in an input array
	 * 
	 * 
	 * @param 		a[]			input array
	 * @param 		k			median position in the array to find
	 * @param		n			size of input array
	 * 	
	 * @return		returns the median value at position k of the input array
	 */
	private float select(float[] a, int k, int n) {
		float b[] = new float[ (n / 5) + 1 ], pivot;
		float large[] = new float[n], small[] = new float[n];
		int temp = 0, j1 = 0, j2 = 0;
		if( n <= 5){
			median( a, 0, n);
			return a[k];
		}
		else{
			int i = 0;
			while( i < n ){
				if( i+5 > n )
					b[temp++] = median( a, i, n );
				else
					b[temp++] = median( a, i, i + 5);
				i += 5;
			}
		}
		pivot = select( b, (( temp ) / ( 10 ) ) + 1, (temp / 5) + 1 );
		for( int i = 0; i < n; i++ ){
			if(a[i] > pivot)
				large[j2++] = a[i];
			if(a[i] < pivot)
				small[j1++] = a[i];
		}
		if( k < j1 )
			return select( small, k, j1 );
		if( k > n - j2 )
			return select( large, k - ( n - j2 ), j2 );
		return pivot;
	}

	
	/**
	 * Method to sort a specified part of the input array with the given 
	 * starting and ending position using bubble sort. It return the median 
	 * value of the sorted part.
	 * 
	 * 
	 * @param 		a[]			input array 
	 * @param 		start		starting position of the array
	 * @param 		end			ending position of the array
	 */
	private static float median(float[] a, int start, int end) {
		for( int i = start; i < end; i++ ){
			//System.err.println(i + " " );
			for( int j = i+1; j < end; j++ ){
			//	System.err.println("   " + j );
				if(a[i] < a[j]){
					a[i] = a[i] + a[j];
					a[j] = a[i] - a[j];
					a[i] = a[i] - a[j];
				}
			}
		}
		return a[ (start + end) / 2];
	}
	
	
	/**
	 * Method to calculate the sum of distances from each house from xbest 
	 * 
	 * 
	 * @param 		temp		xbest
	 * @param 		a			input array having the coordinates of 
	 * 							the houses
	 * @param		n			size of input array i.e. the total number
	 * 							of houses
	 * 	
	 * @return		returns the sum of distances from each house from xbest
	 */
	private float calculate( float temp, float[] a, int n ) {
		float sum = 0;
		for( int i = 0; i < n; i++ ){
			sum = sum + ( Math.abs( a[i] - temp ) );
		}
		return sum;
	}
}


	
