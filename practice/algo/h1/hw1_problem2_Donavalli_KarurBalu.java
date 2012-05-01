/**
 * hw1_problem2_Donavalli_KarurBalu.java
 *
 *
 * @version   $Id: hw1_problem2_Donavalli_KarurBalu.java,v 1.6 
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
 * This program solves the hw1 problem2 question in O(n) 
 * time complexity
 * 
 * @author	  Karthikeyan Karur Balu
 * @author    Venkat Sasank Donavalli
 */

public class hw1_problem2_Donavalli_KarurBalu {
		
	
		/**
		 * main method
		 * 
		 *  @param	args : System.in inputs
		 * 
		 */
        public static void main( String args[] ){
                Scanner sc = new Scanner(System.in );
                int n = 0,k;								//n is the size of input array
                Integer b[][] = null;
                String str[] = null;
                try {										//input reading
                	if(sc.hasNextLine())
                		n = Integer.parseInt(sc.nextLine());
                	b = new Integer[n][3];
                	if(sc.hasNextLine())
                        str = sc.nextLine().split(" ");
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
                for( int i = 0; i < n; i++ ){				//input read is stored in 2-D array format
                        k = Integer.parseInt(str[i]);		//[0] position - number from input range 0 - n^2 -1
                        b[i][0] = k;						//[1] position - based
                        b[i][1] = (k % (n));				//[2] position - decimal values
                        k = k/n;
                        b[i][2] = (k % (n));
                        if( k / n != 0 ){
                        	System.out.println("Input Error: Input exceeds n2 - 1 !!!");
                            System.exit(1);
                        }
                }
                radixSort(b,n);								//implementing radix sort on the 2nd D               
        }
        
        /**
         * method to perform the radix sort for base n numbers
         * 
         *  @param	b		stores the digits and decimal value 
         *  				of the numbers
         *  		n		base of the numbers
         * 
         */
        public static void radixSort( Integer[][] b, int n ){
        	LLT c[] = new LLT[n];			
            LLT d[] = new LLT[n];
            int k = 0;
            for( int i = 0; i < n; i++){							//Linked list
                    c[i] = new LLT();
                    d[i] = new LLT();
            }
            for( int i = 0; i < n; i++){
                    c[b[i][1]].add(i);
            }
            for( int i = 0; i < n && k < n; i++){					//Read each element from LL array
                    LLT temp = c[i];
                    while( temp != null && temp.value != null ){	//store in the other one (sorting here)
                        	d[b[temp.value][2]].add(temp.value);
                            k++;
                            temp = temp.next;
                    }
            }
            k = 0;
            for( int i = 0; i < n && k < n; i++){					//display here 
                    LLT temp = d[i];
                    while( temp!= null && temp.value != null){
                            System.out.print( b[temp.value][0] + " ");
                            k++;
                            temp = temp.next;
                    }
            }
            System.out.println();
        }
	
}


/**
 * This program implements an integer linked list class
 * 
 * @author	  Karthikeyan Karur Balu
 * @author    Venkat Sasank Donavalli
 */
class LLT {
        Integer value = null;
        LLT next = null;
        void add( int value ){
                if( this.value == null )
                        this.value = value;
                else{
                        if(next == null)
                                next = new LLT();
                        next.add( value );
                }
        }
}


