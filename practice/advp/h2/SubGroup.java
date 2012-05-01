/**
 * N people attends meeting. All possible sub groups.
 *
 *
 * @version   $Id: SubGroup.java,v 1.1 2010/09/12 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu)
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/19 12:58:00  kxk2907
 *      Initial revision
 *
 */

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.Queue;

/*
 * This program accepts input from the user (set of strings) and prints all possible combination.
 * 
 * For eg : java SubGroup A B C D , prints all possible combinations
 * 
 *	Logic Used : 
 *			1. Store the inputs in the queue and Hashtable (for maintaining the Rank A = 1, B = 2, C = 3 ....)
 *			2. Pop out first element in the queue (let's say A and combine it with B and C to get AB and AC).
 *			3. Add AB and AC to queue. pop out B and combine with C (since C has more rank than B). Add BC
 *			4. pop out AB from queue and print ABC.
 * 
 */

class SubGroup {
	 public static Hashtable<String, Integer> inputHash = new Hashtable<String, Integer>(); // For maintaining Rank
	 public static void main (String args []) {
		 Queue<String> inputQueue = new LinkedList<String>();								// Mainitaing combinations
		 if(args.length == 0) {																// If no input given by user
			 String [] noInput = new String[4];
			 noInput[0] = "A";noInput[1] = "B";noInput[2] = "C";noInput[3] = "D";
			 System.out.println("  -- No inputs are provided --  Default : A B C D taken");
			 System.out.println("A \nB \nC \nD");
			 inputQueue.add("A");inputQueue.add("B");inputQueue.add("C");inputQueue.add("D");
			 inputHash.put("A",0);inputHash.put("B",1);inputHash.put("C",2);inputHash.put("D",3);
			 runner(inputQueue,noInput);
			 return;
		 }
		 else {
			 for (int iterator = 0;iterator<args.length;iterator++ ) {
				 System.out.println(args[iterator]);
				 inputQueue.add(args[iterator]);
				 inputHash.put(args[iterator], iterator);
			 }
		 }
		 runner(inputQueue,args);															// method for findding out combinations
	 }
	 public static void runner(Queue<String> inputQueue,String[] argsList) {
		 try {
		 while(!inputQueue.isEmpty()) {														// Until queue becomes empty
			 String temp = new String();
			 try {
				 temp = inputQueue.remove();
			 }
			 catch(Exception e) {
				 System.out.println("You are trying to remove items from an empty list !!");
			 }
			 for(int iterator=0;iterator<argsList.length;iterator++) {						// Combine combination with an element
				 if(getValue(temp.substring(temp.length()-1)) 
						 < getValue(argsList[iterator])){
				 	System.out.println(temp+argsList[iterator]);
				 	inputQueue.add(temp+argsList[iterator]);								//Add it back to the queue
				 }
			 }
		 }
		 }
		 catch(NullPointerException ne) {
			 System.out.println("Null pointer exception : you might have used * as a character as a character without \\");
			 System.exit(0);
		 }
		 catch(Exception e) {
			 System.out.println("Other exception occured buddy!! Check it why");
			 System.exit(0);
		 }
	 }
	 public static int getValue(String key) {												//returns rank of a member
		 return inputHash.get(key);
	 }
}
