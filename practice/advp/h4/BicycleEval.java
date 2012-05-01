/**
 * Illustrate standard Inheritance 
 *
 *
 * @version   $Id: PI.java,v 1.1 2010/09/12 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu)
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/12 12:58:00  kxk2907
 *      Initial revision
 *
 */

/*
 *  Main Class : BicycleEval
 *  Create objects for Bicycle --> Bike --> MountainBike
 */
public class BicycleEval {	
	public static void main(String args[]) {
		Bicycle b1 = new Bicycle("Bicycle",10,4);
		Bike b2 = new Bike("Bike",80,5,15);
		MountainBike b3 = new MountainBike("MountainBike",60,6,12,1500);
		b1.basicBicycle();
		System.out.println();
		b2.basicBike();
		System.out.println();		
		b3.basicMountainBike();
	}
}

/*
 * Bicycle can have its own objects and has three properties of bicycle
 */
class Bicycle {
	String model;
	int maxSpeed, gear;
	//assigning the property through constructor
	public Bicycle(String model,int maxSpeed,int gear) {
		this.model = model;
		this.maxSpeed = maxSpeed;
		this.gear = gear;
	}
	//displaying the properties
	public void basicBicycle() {
		System.out.println("Model : " + model);
		System.out.println("MaxSpeed : " + maxSpeed + " mph");
		System.out.println("# of Gears : " + gear);
	}
}
/*
 * Bike extends Bicycle and it inherits properties from Bicycle and got its own properties.
 * mileage is one property of Bike
 */
class Bike extends Bicycle {
	int mileage;
	public Bike(String model, int maxSpeed, int gear, int mileage) {
		super(model,maxSpeed,gear);
		this.mileage = mileage;
	}
	//Unique property
	public void basicBike() {
		super.basicBicycle();
		System.out.println("Mileage " + mileage + " mi per ga");
	}
}
/*
 * MountainBike extends Bike which in-turn inherits from Bicycle 
 * suspension is the property of Bike
 */
class MountainBike extends Bike{
	int suspension;
	public MountainBike(String model, int maxSpeed, int gear, int mileage,int suspension) {
		super(model,maxSpeed,gear,mileage);
		this.suspension = suspension;
	}
	//Unique property
	public void basicMountainBike() {
		super.basicBike();
		System.out.println("Suspension " + suspension + " N/mm");
	}
}

