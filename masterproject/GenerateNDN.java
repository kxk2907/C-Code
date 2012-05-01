import java.util.ArrayList;
import java.util.Random;

public class GenerateNDN {
	private final int NUMSTRING; // 1000
	private final int NUMCHARACTERS; // 26
	private String[] NDNNames;
	private ArrayList<String> AllNDNPrefixes = new ArrayList<String>();
	private ArrayList<Integer> AllNDNPrefixLengths = new ArrayList<Integer>();
	
	public GenerateNDN(int NUMSTRING, int NUMCHARACTERS) {
		this.NUMSTRING = NUMSTRING;
		this.NUMCHARACTERS = NUMCHARACTERS;
		this.NDNNames = new String[this.NUMSTRING];
	}

	public String[] generate() {
		Random stringLength = new Random();
		Random wordLength = new Random();
		Random randChar = new Random();

		for (int i = 0; i < NUMSTRING; i++) {
			int slen = stringLength.nextInt(10) + 1;
			String temp = new String();
			for (int j = 0; j < slen; j++) {
				int wlen = wordLength.nextInt(10) + 1;
				for (int k = 0; k < wlen; k++) {
					int c = randChar.nextInt(NUMCHARACTERS);
					temp += Character.toString((char) ((c + 97) % 256));
				}
				temp += "/";
			}
			NDNNames[i] = temp;
		}

		return NDNNames;
	}

	public ArrayList<String> generateAllPrefixes() {
		for (int j = 0; j < NUMSTRING; j++) {		
			for (int i = 0,k=1; i < NDNNames[j].length(); i++) {
				if (NDNNames[j].charAt(i) == '/') {
					AllNDNPrefixLengths.add(k);
					AllNDNPrefixes.add(NDNNames[j].substring(0, i + 1));
					k++;
				// System.out.println(temp.substring(0,i + 1));
				}
			}
		}
		return AllNDNPrefixes;
	}
	
	public ArrayList<Integer> getAllPrefixLengths() {
		return AllNDNPrefixLengths;
	}
	
	public void Nameprinter() {
		for (int i = 0; i < NUMSTRING; i++) {
			System.out.println(i + " :: " + NDNNames[i]);
		}
	}
	
	public void AllPrefixPrinter() {
		for (int i = 0; i < AllNDNPrefixes.size(); i++)
			System.out.println(AllNDNPrefixes.get(i));
	}
	
	public void AllPrefixPrinterLength() {
		for (int i = 0; i < AllNDNPrefixes.size(); i++)
			System.out.println(AllNDNPrefixes.get(i) + " :: " + AllNDNPrefixLengths.get(i));
	}
}
