/**
 * Find point clouds in 3D space.
 *
 *
 * @version   $Id: PointCloud.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) && Nipun Sud 
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/27 12:58:00  kxk2907
 *      Initial revision
 *
 */

import java.util.EmptyStackException;
import java.util.Hashtable;
import java.util.Scanner;
import java.util.Stack;
	
/*
 * Based on the tolerance value find out points in 3D space 
 * 
 * Logic Used : 
 * 	1. Point class with variables x,y and z used for quadrants;
 *  2. Find the distance between any two points and check the tolerance and if it is within
 *     add it to the cloud. Else create a new cloud. If another points acts as a bridge between 
 *     2 clouds combin two clouds.
 *  3. Hashtable is used for Clouds with stack of points as  values at each . 
 */

class PointCloud {
	//main method 
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		PointCloud pc = new PointCloud();
		Hashtable<Integer, Points> allCoordinates = new Hashtable<Integer, Points>();  // Accepted input from user
		Stack<Points> cloudStack = new Stack<Points>();								   // Stack for storiing all points
		Hashtable<Integer, Stack<Points>> Clouds = new Hashtable<Integer, Stack<Points>>(); // Hashtable for storiing all cloud stacks
		Points p1;
		int number = 0;
		double dist = 0.0;
		try {
		dist = Double.valueOf(args[0]);		// accepted tolerance got from user.									
		}
		catch(ArrayIndexOutOfBoundsException aibe ) {
			System.out.println("Please enter the tolerance !!");
			System.exit(0);
		}
		while(sc.hasNext()) {  				// Coordinates accepted until empty
			String temp = sc.nextLine();	
			String[] ptsString = temp.split(" ");
			Points ptemp = new Points(Double.valueOf(ptsString[0]),
					Double.valueOf(ptsString[1]),Double.valueOf(ptsString[2]));			// point constructor
			allCoordinates.put(number, ptemp);						// put in hashtable of all coordinates
			number ++;
		}
		cloudStack.push(allCoordinates.get(0));						// put first co-ordinate in cloud hashtable
		Clouds.put(0, cloudStack);
		try {
		for(int i = 1;i<allCoordinates.size();i++) {
			p1 = allCoordinates.get(i);
			int matchCloudNumber = -1;								// points added to clouds has to be checked 
			Points matchPoint = null;								// with other clouds for convergence
			boolean flag = true;									// boolean to determine addition to exisitng cloud
			for(int j = 0;j<Clouds.size();j++) {
				Stack<Points> tempCloudStack = new Stack<Points>();
				if(Clouds.get(j) != null) {							
					tempCloudStack = Clouds.get(j);
					for(int k = 0;k<tempCloudStack.size();k++) {	// Stack traversal in a particular cloud to check presence
						if(pc.distance(p1, tempCloudStack.elementAt(k)) < dist) { //belongs to a cloud
							tempCloudStack.push(p1);
							flag = false;
							matchPoint = p1;						
							k = tempCloudStack.size() + 1;
						}
					}	
					if(!flag) {
						matchCloudNumber = j;
						j = Clouds.size() + 1;
					}
				}
			}
			if(flag) {			// if not belong to a cloud - create a new cloud
				Stack<Points> tempCloudStackNext = new Stack<Points>();
				tempCloudStackNext.push(p1);
				Clouds.put(Clouds.size(),tempCloudStackNext);
			}
			else {				// belong to a cloud - check with remaining cloud for convergence
				if(!(matchCloudNumber == -1)) {
					boolean present = false;
					for(int p= matchCloudNumber + 1;p<Clouds.size();p++) { // from current match cloud to all clouds
						Stack<Points> tempCloudStack = new Stack<Points>();
						tempCloudStack = Clouds.get(p);
						if(Clouds.get(p) != null) {
							if(!tempCloudStack.isEmpty()) {
								for(int k = 0; k<tempCloudStack.size();k++) {
									if(pc.distance(matchPoint,tempCloudStack.elementAt(k)) < dist) {
										present = true;			// if atleast one proximity in the point stack in cloud
										k = tempCloudStack.size() + 1;
									}
								}
							}
						}
						if(present) {	// above presence 
							Stack<Points> checkCloudStack = new Stack<Points>();	// add entire contents of cloud to matchcloud and flush out 
							checkCloudStack = Clouds.get(matchCloudNumber);
							while(!tempCloudStack.isEmpty()) {
								Points tempRemove = tempCloudStack.pop();
								checkCloudStack.push(tempRemove);
							}
							Clouds.put(matchCloudNumber, checkCloudStack);
							Clouds.remove(p);
							present = false;
						}
					}
				}
			}
		}
		}
		catch(ArrayIndexOutOfBoundsException ae) {
    		System.out.println("Array Out of bound !!");
    		System.exit(0);
    	}
    	catch (EmptyStackException ee) {
    		System.out.println("Attempted stack is empty !!");
    		System.exit(0);
    	}
    	catch (NullPointerException ne) {
    		System.out.println("Null pointer exception !!");
    		System.exit(0);
    	}
    	catch (Exception e) {
    		System.out.println ("Other exception occured !! \n" + e);
    		System.exit(0);
    	}
		int clouds_total = 0;
		System.out.println("Duplicate co-ordinates are excluded in the cloud !!");
		for(int i = 0;i<Clouds.size();i++) {
			if(Clouds.get(i) != null) {
				if(pc.isPresent(allCoordinates,Clouds.get(i).get(0))) {
					System.out.println("Cloud : " + clouds_total + " contains : " + Clouds.get(i).size());
					clouds_total++;
				}
			}
		}
		System.out.println("Total Clouds : " + clouds_total + " without excluding duplicates " + allCoordinates.size());
	}
	
	/*
	 * Method to find the distance betwen two points in 3D space
	 */
	public double distance (Points p1, Points p2) {	// distance between two points
		double xd = p1.getX() - p2.getX();
		double yd = p1.getY() - p2.getY();
		double zd = p1.getZ() - p2.getZ();
		return(Math.sqrt(xd*xd + yd*yd + zd*zd));
	}
	
	/*
	 * Eliminate junk values in Cloud
	 */
	public boolean isPresent (Hashtable<Integer, Points> allCoordinates, Points pt) {	// eliminate junk values
		if(allCoordinates.containsValue(pt)) {
			return true;
		}
		return false;
	}
}

/*
 * Point class (Coordinates x,y and z)
 */
class Points {
	double x,y,z;
	Points(double x,double y, double z){
		this.x = x;
		this.y = y;
		this.z = z;
	}
	double getX () {
		return this.x;
	}
	double getY () {
		return this.y;
	}
	double getZ () {
		return this.z;
	}
}
