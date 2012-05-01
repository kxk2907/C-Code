public class Trie {
	
	private Trie[] t;
	private char ch;
	private int accessCount;
	private int costFactor;
	private boolean isWord;
	private static final int ALPH = 27;
	private static final int SLASH = 26;
	private static final int BUFSIZE = 1024;
	private static int index = -1;
	private int[] cost;
	private int[] weight;
	private char[] traverse;
	private int nodes;
	private int[][] knapsack;
	
	
	public Trie() {
		t = new Trie[ALPH];
		isWord = false;
	}

	public void addString(String s, int accessCount) {
		Trie local = this;
		for (int k = 0; k < s.length(); k++) {
			int index = s.charAt(k) - 'a';
			if(s.charAt(k) == '/')
				index = SLASH;
			if (local.t[index] == null) {
				local.t[index] = new Trie();
				local.t[index].ch = s.charAt(k);
			}
			local.t[index].costFactor += accessCount;
			local = local.t[index];
			if(index == SLASH)
				local.isWord = true;
		}
		local.accessCount = accessCount;
		local.isWord = true;
	}

	public void print() {
		char[] buffer = new char[BUFSIZE];
		doPrint(new Printer(), 0, buffer, this);
	}

	private void doPrint(Printer pr, int index, char buffer[], Trie local) {
		if (local != null) {
			if (local.isWord) {
				pr.record(new String(buffer, 0, index)
						+ new String("," + local.accessCount));
			}
			for (int k = 0; k < ALPH; k++) {
				if (local.t[k] != null) {
					buffer[index] = (char) (k + 'a');
					if(k == SLASH)
						buffer[index] = '/';
					doPrint(pr, index + 1, buffer, local.t[k]);
				}
			}
		}
	}

	public void isPresent(String s) {
		Trie local = this;
		for (int k = 0; k < s.length(); k++) {
			int index = s.charAt(k) - 'a';
			if(s.charAt(k) == '/')
				index = SLASH;
			if (local.t[index] == null) {
				System.out.println("Not Present.");
				return;
			}
			local = local.t[index];
		}
		if (local.isWord)
			System.out.println("Present.");
		else
			System.out.println("Not Present.");
	}
	
	public int numberofNodes() {
		nodes = 0;
		numberofNodes(this);
		--nodes; //excluding the root
		return nodes;
	}
	
	private void numberofNodes(Trie local) {
		if (local != null) {
			++nodes;
			for(int k = 0; k < ALPH; k++) {
				if(local.t[k] != null)
						numberofNodes(local.t[k]);
			}
		}
	}
	
	public void recordCostWeight() {
		if(nodes == 0)
			numberofNodes();
		index = -1; //init
		weight = new int[nodes];
		cost = new int[nodes];
		traverse = new char[nodes];
		recordCostWeight(this, 0);
		for(int i = 0;i<traverse.length;i++) 
			System.out.print(traverse[i]+"\t");
		System.out.println();
		for(int i = 0;i<weight.length;i++) 
			System.out.print(weight[i]+"\t");
		System.out.println();
		for(int i = 0;i<cost.length;i++) 
			System.out.print(cost[i]+"\t");
		System.out.println();
		index = -1; //post
	}
	
	private void recordCostWeight(Trie local, int level) {
		if(local != null) {
			if(index != -1)  {
				weight[index] = level;
				cost[index] = local.costFactor;
				traverse[index] = local.ch;
			}
			for(int k = 0; k < ALPH; k++) {
				if(local.t[k] != null) {
					++index;
					recordCostWeight(local.t[k], level + 1);
				}
			}
		}
	}
	
	public void buildKnapsack(int capacity) {
		knapsack = new int[traverse.length+1][capacity + 1];
		for(int i = 0;i<traverse.length+1;i++) {
			for(int j = 0;j<capacity+1;j++)
				knapsack[i][j] = 0;
		}
		
		for (int j = 1; j < capacity + 1; j++) {
			for (int i = 1; i < traverse.length + 1; i++) {
				knapsack[i][j] = knapsack[i - 1][j];
				if(weight[i-1] <= j && (knapsack[i - 1][j - weight[i - 1]] + cost[i - 1]) > knapsack[i][j]) {
					knapsack[i][j] = knapsack[i - 1][j - weight[i - 1]]
							+ cost[i - 1];
				}
			}
		}
		
		for (int i = 0; i < traverse.length + 1; i++) {
			for (int j = 0; j < capacity + 1; j++) {
				System.out.print(knapsack[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();
		backTrack(capacity);
	}
	
	public void backTrack(int capacity) {
		int row = traverse.length;
		int column = capacity;
		while(row > 0 && column > 0) {
			if(knapsack[row][column]  == knapsack[row-1][column]) ;
			else {
				column = column - weight[row - 1];
				System.out.println("Item : " + traverse[row - 1] + " :: " + cost[row - 1]);
			}	
			--row;	
		}
	}
}

class Printer {
	public void record(Object o) {
		System.out.println(o);
	}
}
