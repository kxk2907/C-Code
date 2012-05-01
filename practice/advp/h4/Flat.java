/**
 * Illustrate Interface 
 *
 *
 * @version   $Id: Flat.java,v 1.1 2010/10/03 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) and Nipun Sud
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/12 12:58:00  kxk2907
 *      Initial revision
 *
 */

/*
 * Main Class : Flat class describes the livingRoom and kitchen
 * specs shows details about the bedRoom, living room and kitchen
 */
public class Flat {
	String name;
	public Flat () {
		this.name = "Default Flat";
	}
	public Flat(String name) {
		this.name = name;
	}
	public void livingRoom() {
		System.out.println("You have entered " + name + "'s living room !!");
	}
	public void kitchen () {
		System.out.println("You have entered " + name + "'s kitchen !!");
	}
	public void specs() {
		livingRoom();
		bedRoom();
		kitchen();
	}
	public void bedRoom () {
		System.out.println("Default Bedroom");
	}
	public static void main(String args[]) {
		FlatModel100 Flat100_1 = new FlatModel100("Apt #5");
		FlatModel101 Flat101_1 = new FlatModel101("Apt #6");
		Flat100_1.specs();
		System.out.println();
		Flat101_1.specs();
	}
}

/*
 * Implements from flatSpecifications, BedRoom Property : but size of the bedroom is different
 * 10 X 10 is the size 
 */
class FlatModel101 extends Flat implements flatSpecifications {
	public FlatModel101(String name) {
		super(name);
	}
	public void bedRoom() {
		System.out.println("You have entered " + super.name + "'s bed room of model : Size 10 X 10 : " + this.getClass().getName());
	}
}

/*
 * Implements from flatSpecifications, BedRoom Property : but size of the bedroom is different
 * 20 X 20 is the size
 */
class FlatModel100 extends Flat implements flatSpecifications {
	public FlatModel100(String name) {
		super(name);
	}
	public void bedRoom() {
		System.out.println("You have entered " + super.name + "'s bed room of model : Size 20 X 20 : " + this.getClass().getName());
	}
}

interface flatSpecifications {
	public void bedRoom();
}
