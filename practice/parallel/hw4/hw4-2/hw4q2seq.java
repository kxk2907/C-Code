/**
 * hw4q2seq.java
 *
 *
 * @version   $Id: hw4q2seq.java,v 1.6 
 * 2011/2/4 10:05:00 $
 *
 * @author	  Karthikeyan Karur Balu
 * 
 * Revision 1.0     3/29/2011			10:05:00
 * Initial revision
 *
 */
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;
import edu.rit.pj.Comm;

/**
 * Sequenctial shortes path algorithm 
 * 
 * @author kbkarthi
 *
 */
public class hw4q2seq {
	public static int[][] matrix;		//adjacecy matrix
	public static int n;				//number of vertex
	//queue for maintaining the vertices
	public static Queue<Integer> queue = new LinkedList<Integer>();
	public static int[] dist;			//result distance from a single source

	/**
	 * Main program 
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) throws IOException {
		Comm.init(args);
		
		//user arguments
		if (args.length != 1)
			Usage();
		File file = new File(args[0]);
		Scanner sc = new Scanner(file);
		if (sc.hasNextLine())
			n = Integer.parseInt(sc.nextLine());
		matrix = new int[n][n];
		dist = new int[n];
		int k = 0;
		while (sc.hasNextLine()) {
			String[] temp = sc.nextLine().split(" ");
			for (int j = 0; j < temp.length; j++) {
				matrix[k][j] = Integer.parseInt(temp[j]);
			}
			k++;
		}		
		queue.add(0); //source 
		
		//algorithm to fin d the shortest path
		while (queue.peek() != null) {
			int i = queue.remove();
			for (int j = 0; j < n; j++) {
				if ((matrix[i][j] != -1) && i != j) {
					int newdist = dist[i] + matrix[i][j];
					if (newdist < dist[j] || dist[j] == 0) {
						dist[j] = newdist;
						queue.add(j);
					}
				}
			}
		}
		
		//display the result
		System.out.print("[");
		for (int i = 0; i < n - 1; i++) {
			System.out.print(dist[i] + ", ");
		}
		System.out.print(dist[n - 1] + "]\n");
	}

	/**
	 * Usage
	 */
	private static void Usage() {
		System.err.println("java hw4q2seq <input-text-file-name>");
		System.exit(1);
	}
}
