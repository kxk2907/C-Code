/**
 * Tree Set with iterator implementation
 *
 *
 * @version   $Id: Deadlock.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) && Nipun Sud 
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/27 12:58:00  kxk2907
 *      Initial revision
 *
 */

public class Deadlock extends Thread {
	static Locker locker1 = new Locker("locker1");
    static Locker locker2 = new Locker("locker2");
    public void run() {
    	locker1.primary(locker2);
    	locker2.primary(locker1);
    }
	public static void main(String args[]) {
	     Deadlock d1 = new Deadlock();
	     Deadlock d2 = new Deadlock();
	     Deadlock d3 = new Deadlock();
	     Deadlock d4 = new Deadlock();
	     d1.start();
	     d2.start();
	     d3.start();
	     d4.start();
	}
}
/*
 * Send locker1 as an object to locker2.
 * Deadlock !!
 */
class Locker {
    private String lname;
    public Locker(String lname) {
        this.lname = lname;
    }
    public String getLockerName() {
    	return this.lname;
    }
    public synchronized void primary(Locker locker) {
        System.out.println(this.lname + "," +locker.getLockerName());
        locker.secondary(this);
    }
    public synchronized void secondary(Locker locker) {
        System.out.println(this.lname + "," + locker.getLockerName());
    }
}
