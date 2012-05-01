/**
 * hw1q1smp.java
 *
 *
 * @version   $Id: hw1q1smp.java,v 1.6 
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

import edu.rit.util.Random;
import edu.rit.pj.Comm;
import edu.rit.pj.IntegerForLoop;
import edu.rit.pj.ParallelRegion;
import edu.rit.pj.ParallelTeam;

/**
 * 
 * To calculate the INS and FNS value in parallel
 * 
 * @author Karthikeyan Karur Balu
 *
 */
public class hw1q1smp {
	static int m = 10000, n = 10000;
	static float[][] matrix = new float[m][n];
	static float[] tempins = new float[m];
	static HashMap<Integer, Float> INS = new HashMap<Integer, Float>();
	static HashMap<Integer, Float> FNS = new HashMap<Integer, Float>();
	static int[] range = new int[4];

	public static void main(String[] args) throws Exception {
		long t1, t2;
		Comm.init(args);

		Random number = Random.getInstance(10L);
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++)
				matrix[i][j] = number.nextFloat() * 100;
		}

		t1 = System.currentTimeMillis();
		
		//parallel part 
		new ParallelTeam().execute(new ParallelRegion() {
			public void run() throws Exception {
				execute(0, m - 1, new IntegerForLoop() {
					public void run(int first, int last) throws Exception {
						for (int i = first; i <= last; i++) {
							float ins = 0;
							for (int j = 0; j < n; j++) {
								float temp = matrix[i][j];
								temp = temp * temp;
								ins += temp;
							}
							tempins[i] = ins / n;
						}
					}
				});
			}
		});

		for (int i = 0; i < m; i++)
			INS.put(i, tempins[i]);

		//sorting the hashMap based on value instead of key 
		ArrayList<Map.Entry<Integer, Float>> al = new ArrayList<Map.Entry<Integer, Float>>(
				INS.entrySet());
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

		int j = 0;
		
		//FNS calculation
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

