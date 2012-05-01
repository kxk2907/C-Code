import java.util.Scanner;

public class hw0_problem1_KarurBalu_Donavalli {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int input = Integer.parseInt(sc.next());
		int output = factorial(input);
		if(output == -1)
			System.out.println("Invalid Input");
		else
			System.out.println(factorial(input));
	}
	public static int factorial(int n ) {
		if(n == 1 || n == 0)
			return 1;
		else if(n < 0)
			return -1;
		else 
			return n*factorial(n-1);
	}
}
