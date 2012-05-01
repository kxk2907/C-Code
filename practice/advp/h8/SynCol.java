/**
 * Tree Set with iterator implementation
 *
 *
 * @version   $Id: SynCol.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) && Nipun Sud 
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/27 12:58:00  kxk2907
 *      Initial revision
 *
 */

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.Vector;

/*
 * Create a static collection and add synchronized method for the collection to synchronize it
 */
public class SynCol extends Thread {
	public static Vector<Integer> al = new Vector<Integer>();
	public static Collection<Integer> c;
	public String name;
	public SynCol(String name) {
		this.name = name;
		c =  Collections.synchronizedCollection(al);	// Static Synchronized collection
	}
	public void perform() {
		synchronized(c) {
		Iterator<Integer> it = c.iterator(); 			// Using iterator to show the results
		int rand = (int)(Math.random() * 1000);
			if(rand % 2 == 0) {							// If even, add the number to the collection
				c.add(rand);
				System.out.println("Thread : " + name + " Number Added : " + rand);
			}
			else {
				if(it.hasNext()) {						// If odd, remove the number from the collection
					int temp = it.next();
					System.out.println("Thread : " + name + " Number Removed : " + temp);
					c.remove(temp);
				}
			}
		}
	}
	public void run()  {							
		for(int i = 0;i<1000;i++) {						// Repeat 1000 times for each thread
			perform();
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void main(String args[]) {	
		/*
		 * Creating 4 threads
		 */
		SynCol s1 = new SynCol("s1");
		SynCol s2 = new SynCol("s2");
		SynCol s3 = new SynCol("s3");
		SynCol s4 = new SynCol("s4");
		s1.start();
		s2.start();
		s3.start();
		s4.start();
	}
}
