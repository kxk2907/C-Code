/**
 * hw1q1seq.java
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import edu.rit.util.*;

/**
 * 
 * @author Karthikeyan Karur Balu
 * 
 *         To calculate INS and FNS value in case of 10000 students
 * 
 * 
 */
public class hw1q1seq {
	/**
	 * @param args 
	 * @throws Exception - exception if occurs
	 */
	public static void main(String[] args) throws Exception {
		int m = 10000, n = 10000;
		long t1 = 0, t2 = 0;
		float[][] matrix = new float[m][n];								//input matrix
		int[] range = new int[4];
		float[] tempins = new float[m];
		HashMap<Integer, Float> INS = new HashMap<Integer, Float>();
		HashMap<Integer, Float> FNS = new HashMap<Integer, Float>();
		Random number = Random.getInstance(10L);
		for (int i = 0; i < m; i++) {									//value genration
			for (int j = 0; j < n; j++)
				matrix[i][j] = number.nextFloat() * 100;
		}
		t1 = System.currentTimeMillis();
		for (int i = 0; i < m; i++) {
			float ins = 0;
			for (int j = 0; j < n; j++) {
				float temp = matrix[i][j];
				temp = temp * temp;
				ins += temp;
			}
			tempins[i] = ins / n;
		}

		//INS result
		for (int i = 0; i < m; i++)
			INS.put(i, tempins[i]);

		ArrayList<Map.Entry<Integer, Float>> al = new ArrayList<Map.Entry<Integer, Float>>(
				INS.entrySet());

		for (int i = 0; i < m; i++)
			INS.put(i, tempins[i]);

		Collections.sort(al, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				Map.Entry e1 = (Map.Entry) o1;
				Map.Entry e2 = (Map.Entry) o2;
				Float first = (Float) e1.getValue();
				Float second = (Float) e2.getValue();
				return second.compareTo(first);
			}
		});

		Iterator<Entry<Integer, Float>> i = al.iterator();

		range[0] = m * 10 / 100;
		range[1] = m * 30 / 100;
		range[2] = m * 60 / 100;
		range[3] = m * 80 / 100;

		//FNS calculation 
		int j = 0;
		while (i.hasNext()) {
			int key = i.next().getKey();
			int fns = (j < range[0]) ? 4 : (j < range[1] ? 3
					: (j < range[2] ? 2 : (j < range[3] ? 1 : 0)));
			FNS.put(key, (float) fns);
			j++;
		}

		t2 = System.currentTimeMillis();

		for (int k = 0; k < m; k++) {
			System.out.println(k + " :: " + INS.get(k));
		}
		for (int k = 0; k < m; k++) {
			System.out.println(k + " :: " + FNS.get(k));
		}

		System.out.println("Total Time :: " + (t2 - t1) + " msec");
	}
}

