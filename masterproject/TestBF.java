import java.util.ArrayList;

public class TestBF {
	
	static final int NUMSTRING = 50000;

	static final int EXPECTEDNDNNAMES = 1000;
	static final int NUMHASHES = 10;
	static final double BYTEFACTOR = 15;

	static final int NUMCHARACTERS = 26;
	static final int MAXNDNPREFIXLENGTHS = 10;

	static String[] NDNNames;
	static ArrayList<String> AllNDNPrefixes = new ArrayList<String>();
	static ArrayList<Integer> AllNDNPrefixLengths = new ArrayList<Integer>();
	static ArrayList<String> Destination;
	
	static GenerateNDN NDN;

	@SuppressWarnings("unchecked")
	static BF<String>[] bf = new BF[MAXNDNPREFIXLENGTHS];

	static {
		for (int i = 0; i < MAXNDNPREFIXLENGTHS; i++) {
			bf[i] = new BF<String>(BYTEFACTOR, EXPECTEDNDNNAMES, NUMHASHES);
		}
	}

	public static void main(String[] args) {
		NDN = new GenerateNDN(NUMSTRING, NUMCHARACTERS);
		NDNNames = NDN.generate();
		AllNDNPrefixes = NDN.generateAllPrefixes();
		AllNDNPrefixLengths = NDN.getAllPrefixLengths();

		Destination = new ArrayList<String>(AllNDNPrefixes);
		
		for(int i = 0;i<Destination.size();i++) {
			String temp = Destination.get(i);
			Destination.set(i, temp + "karur/balu/karthikeyan/");
		}
		
		for (int i = 0; i < AllNDNPrefixes.size(); i++) {
			bf[AllNDNPrefixLengths.get(i) - 1].add(AllNDNPrefixes.get(i));
		}
		
		printing();
		
		System.out.println("===================================================================");
		int time1 = (int) System.currentTimeMillis();
		System.out.println(time1);
		for(int i = 0;i<Destination.size();i++) {
			String temp = Destination.get(i);
			int templen = temp.length();
			int length = AllNDNPrefixLengths.get(i) + 3; //for now 3
			int k = 0;
			boolean flag = true;
			for(int j = templen-1; j >= 0 ; j --) {
				if(temp.charAt(j) == '/' && (length - k ) <= MAXNDNPREFIXLENGTHS) {
					int l = length - k - 1;
					String substr = temp.substring(0,j + 1);
					if(bf[l].lookup(substr)) {
						//System.out.println("Present !!");
						//System.out.println(substr);
						flag = false;
						break;
					}
				}
				if(temp.charAt(j) == '/')
					++k;
			}
			if(flag) {
				System.err.println("FATAL ERROR !!" + " :: " + temp );
			}
		}
		int time2 = (int) System.currentTimeMillis();
		System.out.println(time2);
		System.out.println("DIFF :: " + (time2 - time1) + " ms");
	}

	public static void printing() {
		if (NDN != null) {
			NDN.Nameprinter();
			NDN.AllPrefixPrinter();
			NDN.AllPrefixPrinterLength();
		}
	}
}