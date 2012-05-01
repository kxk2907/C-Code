/**
 * Traffic Lights implemenattion
 *
 *
 * @version   $Id: Lights.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) && Nipun Sud 
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/27 12:58:00  kxk2907
 *      Initial revision
 *
 */

/*
 * Each light is an object and a single thread controls each light. 
 * A master thread controls the order in which the light will go from red to green.
 */
public class Lights extends Thread {
	public synchronized void  run() {
		// all direction signal threads
		Signal north = new Signal("north","green","yellow");
		Signal south = new Signal("south","green","yellow");
		Signal east = new Signal("east","red","yellow");
		Signal west = new Signal("west","red","yellow");
		for(int i=0;i<5;i++) {
			System.out.println("At instance : " + (i+1));
			north.run();				// startting all threads
			south.run();
			east.run();
			west.run();
			System.out.println("Traffic flowing through !!");
			try {
				sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			north.run();				//changing the signal
			south.run();
			east.run();
			west.run();
			System.out.println("Traffic stopped since all are yellow !!");
			try {
				sleep(700);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println();
		}
		north.interrupt();			//interrupt when the master thread is ended
		south.interrupt();
		east.interrupt();
		west.interrupt();
		System.out.println("Traffic System Ends !! ");
	}
	public static void main(String args[]) {
		Lights master = new Lights();
		master.start();							// master thread controls others threads
	}
}

class Signal extends Thread {
	String name;
	String current;
	String previous;
	Signal(String name,String current,String previous) {
		this.name = name;
		this.current = current;
		this.previous = previous;
	}
	public void run() { 
		//swap the signals 
		if(this.current.equals("green") || this.current.equals("red")) {	
			System.out.println("Traffic at " + this.name + " is " + this.current);
			this.previous = this.current;
			this.current = "yellow";
		}
		// swap if it is green
		else if(this.current.equals("yellow")) {
			System.out.println("Traffic at " + this.name + " is " + this.current);		
			if(this.previous.equals("green")) {
				try {
					sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				this.previous = this.current;
				this.current = "red";
			}
			else {
				this.previous = this.current;
				this.current = "green";
			}
		}
	}
}


