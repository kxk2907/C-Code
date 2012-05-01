/**
 * hw1q2seq.java
 *
 *
 * @version   $Id: hw1q1seq.java,v 1.6 
 * 2011/2/4 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * 
 * Revision 1.0     3/29/2011			10:05:00
 * Initial revision
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Map.Entry;

/**
 * 
 * @author kbkarthi
 * 
 *         Program to find the word permutations
 */
public class hw1q2seq {
	public static ArrayList<String> list = new ArrayList<String>();
	public static ArrayList<HashMap> fin = new ArrayList<HashMap>();
	public static String[] common;
	public static int size;

	public static void main(String[] args) {
		File file = new File(args[0]);
		try {
			Scanner sc = new Scanner(file);				//file name command line argument
			while (sc.hasNextLine())
				list.add(sc.nextLine());
			size = list.size();
			common = new String[size];
			for (int i = 0; i < size; i++)
				common[i] = new String(list.get(i));    //all the words
			int t1 = (int) System.currentTimeMillis();
			for (int q = 0; q < size - 1; q++) {
				String local = common[q];
				HashMap<String, String> result = new HashMap<String, String>();
				result.put("", local);
				while (!result.containsValue("")) {					//permuting here 
					Iterator<Entry<String, String>> itr = result.entrySet()
							.iterator();
					HashMap<String, String> nresult = new HashMap<String, String>();
					while (itr.hasNext()) {
						Entry<String, String> lo = itr.next();
						String key = lo.getKey();
						String value = lo.getValue();
						for (int i = 0; i < value.length(); i++) {
							String ch = Character.toString(value.charAt(i));
							String nkey = key.concat(ch);
							String nvalue = value.replaceFirst(ch, "");
							nresult.put(nkey, nvalue);
						}
					}
					result = new HashMap<String, String>(nresult);
				}
				fin.add(result);
			}
			int t2 = (int) System.currentTimeMillis();

			for (int i = 0; i < fin.size(); i++) {				//printing result
				Iterator<Entry<String, String>> itr = fin.get(i).entrySet().iterator();
				while (itr.hasNext())
					System.out.print(itr.next().getKey() + " ");
				System.out.println();
			}
			System.out.println("TOTAL TIME :: " + (t2 - t1) + " msec");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
