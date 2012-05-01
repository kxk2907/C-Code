/**
 * Examples for pass by value and pass by reference in java
 *
 *
 * @version   $Id: Example.java,v 1.1 2010/09/19 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu)
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/12 12:58:00  kxk2907
 *      Initial revision
 *
 */

/*
 *  Using swap function to give example for pass by value
 *  Using array ref to illustrate pass by reference
 */
class Example {	
	int a,b;

	public Example(int a,int b) {
		this.a = a;
		this.b = b;
	}
	
	public static void swap(Example ex1,Example ex2) { // for pass by value
		Example exTemp = ex1;
		ex1 = ex2;
		ex2 = exTemp;
	}
	
	public void display(int i,String s) {
		if(i == 0)
			System.out.println("Before : " + s +" " + this.a + "," + this.b);
		else 
			System.out.println("After : " + s +" " +this.a + "," + this.b);
	}
	public static void passbyRef (int arrayMain[]) { // for pass by ref
		arrayMain[0] = 100;
	}
	public static void main(String args[]) {
		int[] arrayMain = new int[4];
		Example ex1 = new Example(5,10);			// creating objects ex1 and ex2
		Example ex2 = new Example(15,20);
		System.out.println("Pass by value in java : ");		
		ex1.display(0,"ex1");
		ex2.display(0,"ex2");
		System.out.println("Calling swap function ....."); // even after swapping value in ex1 and ex2 is not changed
		swap(ex1,ex2);										// because value is passed
		ex1.display(1,"ex1");
		ex2.display(1,"ex2");
		System.out.println("Pass by reference in java : ");			
		System.out.println("before : array[0] " + arrayMain[0]);	
		passbyRef(arrayMain);							// Here array ref is passed so arrayMain[0] is assigned in function
		System.out.println("after : array[0] " + arrayMain[0]);
	}
}

	                             

