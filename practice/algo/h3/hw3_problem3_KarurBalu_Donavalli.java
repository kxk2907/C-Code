
/**
 * hw3_problem3_KarurBalu_Donavalli.java
 *
 *
 * @version   $Id: hw2_problem2_KarurBalu_Donavalli.java,v 1.6 
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
 * 
 * @author Karthikeyan Karur Balu
 * 
 * Program to find the max amount in a knapsack when cost and weight is given
 * Two dimensional array is used to maintain the items in the Stack
 *
 */
public class hw3_problem3_KarurBalu_Donavalli {
	/**
	 * 
	 * @param args 	Command line argument
	 */
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		Integer n, W;
		String[] temp = new String[2];
		try {
			if (sc.hasNextLine())
				temp = sc.nextLine().split(" ");
			n = Integer.parseInt(temp[0]);
			W = Integer.parseInt(temp[1]);
			if (sc.hasNextLine())
				sc.nextLine();
			CostWeight[] cw = new CostWeight[n];
			for (int i = 0; i < n; i++) {
				if (sc.hasNextLine()) {
					String[] temp2 = new String[2];
					temp2 = sc.nextLine().split(" ");
					cw[i] = new CostWeight(Integer.parseInt(temp2[1]),
							Integer.parseInt(temp2[0]));
				}
			}
			Sack[][] S = new Sack[n + 1][W + 1];
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
	}

	/**
	 * 
	 * @param S	- Sack Matrix that gets the MAX(Sack[j].weight, Sack[j-1].weight + cost)
	 * @param n - number of different items available
	 * @param W - Max weight of the knapsack
	 */
	public static void TraceBack(Sack S[][], Integer n, Integer W) {
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
	 * @param n	- number of different items available
	 * @param cw - class that stores the cost and weight of each item
	 * @param W - Max weight of the knapsack
	 * @param S - Sack Matrix that gets the MAX(Sack[j].weight, Sack[j-1].weight + cost)
	 */
	public static void knapsackIndivisible(Integer n, CostWeight[] cw,
			Integer W, Sack[][] S) {
		for (int i = 0; i <= n; i++)
			for (int j = 0; j <= W; j++)
				S[i][j] = new Sack(-1, -1);
		for (int i = 0; i <= n; i++)
			S[i][0].setSackValue(0);
		for (int i = 0; i <= W; i++)
			S[0][i].setSackValue(0);
		for (int i = 1; i <= W; i++) {
			for (int j = 1; j <= n; j++) {
				S[j][i].setSackValue(S[j - 1][i].getSackValue());
				Integer weight = cw[j - 1].getWeight();
				Integer cost = cw[j - 1].getCost();
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

	public static Integer knapsackIndivisibleRecursive(Integer n,
			CostWeight[] cw, Integer W) {
		if (n <= 0)
			return 0;
		Integer withLastItem;
		if (W < cw[n - 1].getWeight())
			withLastItem = -1;
		else
			withLastItem = cw[n - 1].getCost()
					+ knapsackIndivisibleRecursive(n - 1, cw,
							W - cw[n - 1].getWeight());
		Integer withoutLastItem = knapsackIndivisibleRecursive(n - 1, cw, W);
		if (withoutLastItem > withLastItem)
			return withoutLastItem;
		else
			return withLastItem;
	}
}

class Sack {
	private Integer sackValue;
	private Integer minus;

	Sack(int sackValue, int minus) {
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

/**
 * 
 * @author Karthikeyan Karur Balu
 * class to maintain the cost and weight of each item
 */
class CostWeight {
	private Integer cost;
	private Integer weight;

	CostWeight(int cost, int weight) {
		this.cost = cost;
		this.weight = weight;
	}

	Integer getCost() {
		return this.cost;
	}

	Integer getWeight() {
		return this.weight;
	}
}

