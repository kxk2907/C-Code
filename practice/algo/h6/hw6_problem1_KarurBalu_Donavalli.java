/**
 * hw6_problem1_KarurBalu_Donavalli.java
 *
 *
 * @version   $Id: hw6_problem1_KarurBalu_Donavalli.java ,v 1.6 
 * 2011/1/28 10:05:00 $
 *
 * @author    Venkat Sasank Donavalli
 * @author	  Karthikeyan Karur Balu
 * 
 * 
 * Revisions:
 *
 *      Revision 1.0     1/113/2011			10:05:00
 *      *      Initial revision
 *
 */

import java.util.Scanner;

/**
 * Program to find the number of connected components of a given graph
 * 
 * @author Karthikeyan Karur Balu
 * @author Venkat Sasank Donavalli
 * 
 */
public class hw6_problem1_KarurBalu_Donavalli {

	public static Integer time = 0;
	public static Integer[] fin;
	/**
	 * Main method - Input using scanner
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Integer v = 0, e = 0;
		String[] temp = new String[2];
		try {
			if (sc.hasNextLine()) {
				temp = sc.nextLine().split(" ");
				v = Integer.parseInt(temp[0]);
				fin = new Integer[v];
				e = Integer.parseInt(temp[1]);
			}
			Vertex[] V = new Vertex[v];
			for (Integer i = 0; i < e; i++) {
				if (sc.hasNextLine()) {
					temp = new String[2];
					temp = sc.nextLine().split(" ");
					if(Integer.parseInt(temp[0]) > Integer.parseInt(temp[1])) {
						String local = temp[0];
						temp[0] = temp[1];
						temp[1] = local;
					}
					if (V[Integer.parseInt(temp[0])] == null) {
						V[Integer.parseInt(temp[0])] = new Vertex(
								Integer.parseInt(temp[0]), new Neighbor(
										Integer.parseInt(temp[1]), null));
					} else { // already there is a Neighbor
						Neighbor tempNeighbor = V[Integer.parseInt(temp[0])].nbr
								.getLast();
						tempNeighbor.next = new Neighbor(
								Integer.parseInt(temp[1]), null);
					}
				}
			}
			for (Integer i = 0; i < v ; i++) {
				fin[i] = -1;
				if (V[i] == null)
					V[i] = new Vertex(i, null);
			}
			if(cycleCheck(V,v)) 
				System.out.println("NO");
			else 
				System.out.println("YES");
		} catch (NumberFormatException nfe) {
			System.out.println("Illegal number Format !!");
			System.exit(1);
		} catch (ArrayIndexOutOfBoundsException aobe) {
			System.out
					.println("Array out of Bound Exception, check the # of numbers !!");
			System.exit(1);
		} catch (Exception ex) {
			System.out.println("Other Exception !!");
			System.exit(1);
		}
	}

	/**
	 * Check for connections for each vertex
	 * 
	 * @param V
	 *            - array of Vertex containing the adjacency list
	 * @param v
	 *            - number of vertices
	 * @return - number of connetcted componenets
	 */
	public static boolean cycleCheck(Vertex[] V, int v) {
		Integer reach = 1;
		for (int i = 0; i < v ; i++) {
			if (V[i].isReached() != reach) {
				if(!DFS(V,i)) {
					return false;
				}
			}
		}
		return true;
	}

	public static boolean DFS(Vertex[] V, Integer i) {
		V[i].setReach(1);
		Neighbor current = V[i].nbr;
		while (current != null) {
			if (V[current.nbrvalue].isReached() != 1)
				return DFS(V, current.nbrvalue);
			else if((V[current.nbrvalue].isReached() == 1) && (current.nbrvalue != i)) {
				if(fin[current.nbrvalue] != -1)
					return false; //cycle
			}
			current = current.next;
		}
		fin[i] = time;
		time++;
		return true;
	}
}



/**
 * 
 * @author Karthikeyan Karur Balu Venkat Sasank Donavalli
 * 
 *         Vertex Class
 * 
 */
class Vertex {
	Integer value;
	Integer reach = -1;
	Neighbor nbr;

	/**
	 * Vertex constructor
	 * 
	 * @param value
	 *            - value of that vertex
	 * @param nbr
	 *            - Neighbor list
	 */
	Vertex(Integer value, Neighbor nbr) {
		this.value = value;
		this.nbr = nbr;
	}

	/**
	 * If already reached
	 * 
	 * @return
	 */
	Integer isReached() {
		return this.reach;
	}

	/**
	 * set reach
	 */
	void setReach(Integer reach) {
		this.reach = reach;
	}
}

/**
 * Neighbor : adjacency list
 * 
 * @author
 * 
 */
class Neighbor {
	Integer nbrvalue;
	Neighbor next;
	static Neighbor last;

	/**
	 * adjacency list : constructor
	 * 
	 * @param nbrvalue
	 * @param next
	 */
	Neighbor(Integer nbrvalue, Neighbor next) {
		this.nbrvalue = nbrvalue;
		this.next = next;
		last = this;
	}

	/**
	 * store the last element always
	 * 
	 * @return
	 */
	Neighbor getLast() {
		return last;
	}
}


