/**
 * Fibonacci series program. 
 *
 *
 * @version   $Id: Fibo.java,v 1.1 2010/09/12 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu)
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/12 12:58:00  kxk2907
 *      Initial revision
 *
 */

// No import statements Required
/*
 * This Program computes 10 fibonacci 3-series numbers,
 * It used recursion to compute the series. 
 * Here,
 * n0 = 0; n1 =1; n2 = 2 are initialized. 
 * Formula : n(k+3) = n(k+2) + n(k+1) + n(k)
 * It uses recursion 
 */

// Fibo class start here 
class Fibo {   
        public static void main(String args[]) { //main program
                int n0 = 0;
                int n1 = 1;           // Initializing the first 3 values in the series
                int n2 = 2;
                System.out.println(n0 + "\n" + n1 + "\n" + n2 );  
                if(args.length == 0) {
                	// Calculating first 10 Fibo series if no arguments passed 
                	calculateFibo(n0,n1,n2,10);     
                } 
                else {
                	// If # of series is specified in the input
                	calculateFibo(n0,n1,n2,Integer.parseInt(args[0])); 
                }
        }
        
        //Method for computing series. Uses Recursive method
        public static void calculateFibo(int n0,int n1,int n2,int x) {  
                if(x <= 0) return;   // If x = 0 return as to nothing to be computed anymore.
                int sum = n0 + n1 + n2;	// Formula
                System.out.println(sum); 
                calculateFibo(n1,n2,sum,--x);  // Recursion
        }
}
//Fibo class ends here 
