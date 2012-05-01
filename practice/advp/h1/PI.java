/**
 * Calculate the value of PI 
 *
 *
 * @version   $Id: PI.java,v 1.1 2010/09/12 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu)
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/12 12:58:00  kxk2907
 *      Initial revision
 *
 */

//No imports required

/*
 * Formula used here : PI = sqrt(6*(1 + 1/2^2 + 1/3^2 + 1/4^2 + .... + 1/n^2))
 * As n increases we get better approximation here
 * 
 * Logic Used : 
 * 		Get input (upto # decimal of approximation required)
 * 		Iterate based on the input valu 
 * 		calculate pi's at regular intervals and compare with given input to narrow down your approximation
 * `	Once you get the approximation print the result
 */

//Class PI starts here 
class PI {
        public static void main(String args[]) { // main program
        		double checker = 0.0;
        		try {
        			checker = Double.valueOf(args[0]); //converts string to Double 
        		}
        		catch (Exception e){  // Array out of bound exception occurs 
        			System.out.println("Args[0] not present, please provide input by default calculating for 0.01"); 
        			checker = 0.01; // If the exception occurs 0.01 is assigned
        		}
                calculatePI(checker);  // Method to compute PI
        }
        
        // Method to compute PI. Input is a double value
        public static void calculatePI(double checker) {
                long i = 2; // For iteration
                double newpi= 0.0;
                double oldpi = 0.0;   // Initialize
                double sum = 0.0;
                newpi = (double)Math.sqrt(6*(1+sum));
                //System.out.println(newpi + "  ::  " +newpi*7);
                checker = checker/1000000;   // For more approximation # of 00's has to increased here 
                while ((newpi - oldpi) > checker) {   	//Check the difference with the checker to decimal places given as an input
                	oldpi = newpi;	//oldpi will be current newpi
                	sum = sum + (1/(double)(i*i));
                	newpi = (double)Math.sqrt(6*(1+sum));	//Compute newpi 
                	//System.out.println(newpi + "  ::  " +newpi*7);
                	i++;	//Iterator
                }
                System.out.println("\nPI ::: " + newpi); //Print the output 
        	System.out.println(); 
	 }
}
//Class PI ends here 
