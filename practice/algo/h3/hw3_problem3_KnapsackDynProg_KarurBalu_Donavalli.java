/**
 * hw2_problem2_KarurBalu_Donavalli_.java
 *
 *
 * @version   $Id: hw3_problem3_KnapsackDynProg_KarurBalu_Donavalli.java ,v 1.1 
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

public class hw3_problem3_KnapsackDynProg_KarurBalu_Donavalli {
	/**
	 * 
	 * @param args	command line arguments
	 */
	public static void main(String args[]) {
		double start = System.currentTimeMillis();
		Scanner sc = new Scanner(System.in);
		Integer n, W;
		String[] temp = new String[2];
		try {
			if (sc.hasNextLine())
				temp = sc.nextLine().split(" ");
			n = Integer.parseInt(temp[0]);
			W = n/2;
			Integer cw = 1;
			Sack1[][] S = new Sack1[n + 1][W + 1];
			knapsackIndivisible(n, cw, W, S);
			TraceBack(S, n, W);
		} catch (NumberFormatException nfe) { // Exception handling
			System.out.println("Illegal number Format !!");
			System.exit(1);
		} catch (ArrayIndexOutOfBoundsException aobe) {
			System.out
					.println("Array out of Bound Exception, check the # of numbers !!");
			System.exit(1);
		} catch (Exception e) {
			System.out.println("Other Exception !!");
			System.exit(1);
		}
		double end = System.currentTimeMillis();
		 System.out.println("Time : " + (end-start));

	}

	public static void TraceBack(Sack1 S[][], Integer n, Integer W) {
		int j = W;
		int[] output = new int[n+1];
		System.out.println(S[n][W].getSackValue());	
		for (int i = n; i >= 0; i--) {
			if (S[i][j].getSackMinus() == -1)
				continue;
			else {
				j = S[i][j].getSackMinus();
				output[i] = 1;
			}
		}
		for(int i = 0;i<=n;i++) {
			if(output[i] == 1)
				System.out.print(i + " ");
		}
		System.out.println();
	}
/**
 * 
 * @param n   -  number of different items available
 * @param cw  - class that stores the cost and weight of each item
 * @param W   - Max weight of the knapsack
 * @param S   - Sack Matrix that gets the MAX(Sack[j].weight, Sack[j-1].weight + cost)
 */
	public static void knapsackIndivisible(Integer n, Integer cw,
			Integer W, Sack1[][] S) {
		for (int i = 0; i <= n; i++)
			for (int j = 0; j <= W; j++)
				S[i][j] = new Sack1(-1, -1);
		for (int i = 0; i <= n; i++)
			S[i][0].setSackValue(0);
		for (int i = 0; i <= W; i++)
			S[0][i].setSackValue(0);
		for (int i = 1; i <= W; i++) {
			for (int j = 1; j <= n; j++) {
				S[j][i].setSackValue(S[j - 1][i].getSackValue());
				Integer weight = 1;
				Integer cost = 1;
				if ((weight <= i)
						&& ((S[j - 1][i - weight].getSackValue() + cost) > S[j][i]
								.getSackValue())) {
					S[j][i].setSackMinus(i - weight);
					S[j][i].setSackValue(S[j - 1][i - weight].getSackValue()
							+ cost);
				}
			}
		}
	}
}

/**
 * 
 * @author kbkarthi
 *
 */
class Sack1 {
	private Integer sackValue;
	private Integer minus;
/**
 * 
 * @param sackValue  - value in the sack at that particular position
 * @param minus		 - (i-weight) value at that position
 */
	Sack1(int sackValue, int minus) {
		this.sackValue = sackValue;
		this.minus = minus;
	}

	Integer getSackValue() {
		return this.sackValue;
	}

	Integer getSackMinus() {
		return this.minus;
	}

	void setSackValue(int sackValue) {
		this.sackValue = sackValue;
	}

	void setSackMinus(int minus) {
		this.minus = minus;
	}
}
