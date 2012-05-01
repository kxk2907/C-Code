/**
 * Evaluate the matrix multiplication
 *
 *
 * @version   $Id: Evaluator.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) && Nipun Sud 
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/27 12:58:00  kxk2907
 *      Initial revision
 *
 */


import java.util.Scanner;

public class Evaluator extends Thread{
	static int result[][],A[][],B[][];
	static int rowA,colA,rowB,colB;
	int ii,jj;
	public Evaluator(int ii,int jj) {
		this.ii = ii;
		this.jj = jj;
	}
	public synchronized void run() {	
		synchronized (result) {			// synchronized based on the result
			int sum =  0;
			for(int i = 0;i<colA;i++) 
				sum = sum + A[ii][i]*B[i][jj];
			Evaluator.result[ii][jj] = sum;
		}
	}
	public static void display() {		// display all three matrices
		System.out.println("Matrix A :");
		for(int i =0 ;i<rowA;i++) {
			for(int j =0;j<colA;j++) {
				System.out.print(A[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("Matrix B :");
		for(int i =0 ;i<rowB;i++) {
			for(int j =0;j<colB;j++) {
				System.out.print(B[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		System.out.println("Matrix result : A X B");
		for(int i =0 ;i<rowA;i++) {
			for(int j =0;j<colB;j++) {
				System.out.print(result[i][j] + " ");
			}
			System.out.println();
		}
	}
	public static void main(String args[]) {		// entering details of the matrix
		Scanner cin=new Scanner(System.in);
		System.out.println("Enter the number of rows in matrix A : ");
		rowA=cin.nextInt();
		System.out.println("Enter the number of columns in matrix A : ");
		colA=cin.nextInt();
		System.out.println("Enter the number of rows in matrix B : ");
		rowB= cin.nextInt();
		System.out.println("Enter the number of columns in matrix B : ");
		colB=cin.nextInt();
		if(colA != rowB) {
			System.out.println("Good Bye !! Matrix A and B cannot be multiplied");
			System.exit(0);
		}
		else {
			System.out.println("Enter Matrix A, in the order of rows : ");	// contents of matrix A
			A = new int[rowA][colA];
			B = new int[rowB][colB];
			result = new int[rowA][colB];
			for(int i = 0;i<rowA;i++) {
				for(int j = 0 ; j<colA;j++) {
					A[i][j] = cin.nextInt();
				}
			}
			System.out.println("Enter Matrix B, in the order of rows : ");	// contents of matrix B
			for(int i = 0;i<rowB;i++) {
				for(int j = 0 ; j<colB;j++) {
					B[i][j] = cin.nextInt();
				}
			}	
			for(int i = 0;i<rowA;i++) {
				for(int j = 0;j<colB;j++) {
					Evaluator ev1 = new Evaluator(i,j); // create threads for calculating the matrix A X B
					ev1.start();	// start
					try {
						ev1.join();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			Evaluator.display();	// display the matrix ...
		}
	}
}


