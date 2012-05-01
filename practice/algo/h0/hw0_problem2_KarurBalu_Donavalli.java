import java.util.Scanner;

public class hw0_problem2_KarurBalu_Donavalli {
	public static void main(String argsp[]) {
		Scanner sc = new Scanner(System.in);
		int input = Integer.parseInt(sc.next());
		for(int i = 0;i < input;i+=2) 
			System.out.print(i+ " ");
		System.out.println();
	}
}
