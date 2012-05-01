import java.util.ArrayList;
import java.util.Random;

public class TestTrie {
	
	static final int NUMSTRING = 1000;
	static final int NUMCHARACTERS = 26;
	static final int KNAPSACKSIZE = 100;
	
	static String[] NDNNames;
	static ArrayList<String> AllNDNPrefixes = new ArrayList<String>();
	static ArrayList<Integer> AllNDNPrefixLengths = new ArrayList<Integer>();
	static ArrayList<String> Destination;

	static GenerateNDN NDN;

	public static void main(String[] args) {
		NDN = new GenerateNDN(NUMSTRING, NUMCHARACTERS);
		NDNNames = NDN.generate();
		AllNDNPrefixes = NDN.generateAllPrefixes();
		AllNDNPrefixLengths = NDN.getAllPrefixLengths();
		
		Random accessCount = new Random();
		
		 Trie t = new Trie(); 
		 for(int i = 0 ;i< NUMSTRING;i++)
			 t.addString(NDNNames[i], Math.abs(accessCount.nextInt(10000)));
		 NDN.Nameprinter();
		 t.print(); 
		 System.out.println("Nodes :: " + t.numberofNodes());
		 t.recordCostWeight();
		 t.buildKnapsack(KNAPSACKSIZE);
		 
		 for(int i = 0;i<AllNDNPrefixes.size();i++) 
			 t.isPresent(AllNDNPrefixes.get(i));
		 
		
		/*BloomFilters[] bf = new BloomFilters[10];
		bf[0] = new BloomFilters();
		bf[0].addString("karthikeyan");
		bf[0].addString("karthikeyankarur");
		bf[0].addString("karthi");
		bf[1] = new BloomFilters();
		bf[1].addString("sid");
		
		bf[0].print(); 
		bf[1].print();*/
/*		 
		int[] weight = { 2, 8, 6 };
		int[] cost = { 100, 200, 250 };
		int[][] knapsack = new int[4][11];
		int i = 0, j = 0;

		for (i = 0; i < 4; i++) {
			for (j = 0; j < 11; j++) {
				knapsack[i][j] = 0;
			}
		}
		for (i = 1; i < 4; i++) {
			for (j = 1; j < 11; j++) {
				knapsack[i][j] = knapsack[i - 1][j];
				if (weight[i - 1] <= j
						&& (knapsack[i - 1][j - weight[i - 1]] + cost[i - 1]) > knapsack[i][j]) {
					knapsack[i][j] = knapsack[i - 1][j - weight[i - 1]]
							+ cost[i - 1];
				}
			}
		}
		
		for (i = 0; i < 4; i++) {
			for (j = 0; j < 11; j++) {
				System.out.print(knapsack[i][j] + "\t");
			}
			System.out.println();
		}
		System.out.println();

*/		
	}
}