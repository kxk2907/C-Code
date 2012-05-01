/**
 * Find word count in a file.
 *
 *
 * @version   $Id: TreeSetNew.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) && Nipun Sud 
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/27 12:58:00  kxk2907
 *      Initial revision
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.EmptyStackException;
import java.util.Scanner;


/*
 * Implementation of binary tree with methods insert, search and display no of nodes and height
 * 
 * Method used : 
 * 1. Create a node and create a left and right subnode within itself
 * 2. Have accesser and mutator methods for the node
 */


/*
 * TreeSetNew class 
 */
public class TreeSetNew implements AnotherTreeSet {
	TreeNode treenode;
	
	//default constructor
	public TreeSetNew() {
	}
	//accepts array of objects
	public TreeSetNew(Object[] objArray) {
		for(int i = 0;i<objArray.length;i++) 
			add(objArray[i]);
	}
	//get root
	public TreeNode getRoot() {
		return treenode;
	}
	//returns a boolean if the objects are added 
	// create a node if the node is null or go to left or go to the right 
	public boolean add(Object o) {
		if(treenode == null) {
			treenode = new TreeNode(o,null,null);
		}
		else {
			TreeNode existingnode = treenode;		
			TreeNode treeTemp = new TreeNode(o,null,null);
			while(existingnode.getValue() != null) {
				if(existingnode.compareTo(treeTemp.getValue()) == 0) {
					return true;
				}
				else if(existingnode.compareTo(treeTemp.getValue()) > 0) {
					if(existingnode.getLeft()== null ){
						existingnode.setLeft(treeTemp);
						return true;
					}
					else {
						existingnode = existingnode.getLeft();
					}
				}
				else {
					if(existingnode.getRight() == null){
						existingnode.setRight(treeTemp);
						return true;
					}
					else {
						existingnode = existingnode.getRight();
					}
				}
 			}
		}
		return false;
	}

	//clear all the nodes in the tree
	public void clear() {
		TreeNode traverse = this.getRoot();
		TreeNode parent = this.getRoot();
		if(parent.value == null && parent.left == null && parent.right == null)
			return;
		if(clear(parent,traverse)) {
			System.out.println("Tree cleared");
		}else {
			System.out.println("Tree not cleared");
		}
	}
	//actual clear method
	public boolean clear(TreeNode parent,TreeNode traverse) {
		if (traverse.getLeft() != null) {
			parent = traverse;
			traverse = traverse.getLeft();
			clear(parent,traverse);
			traverse.value = null;
			parent.left = null;
			traverse = parent;
		}
		if(traverse.getRight() != null) {
			parent = traverse;
			traverse = traverse.getRight();
			clear(parent,traverse);
			traverse.value = null;
			parent.right = null;
			traverse = parent;
		} 
		traverse.value = null;
		return true;
	}

	//to check if it contains the object or not 
	public boolean contains(Object o) {
		TreeNode traverse = this.getRoot();
		if(traverse.value == null)
			return false;
		while(traverse != null) {
			if(traverse.compareTo(o) == 0) {
				return true;
			}
			else if(traverse.compareTo(o) > 0) {
				traverse = traverse.getLeft();
			}
			else {
				traverse = traverse.getRight();
			}
		}
		return false;
	}

	//to check if the tree is empty or not
	public boolean isEmpty() {
		TreeNode traverse = this.getRoot();
		if(traverse == null || (traverse.value == null && traverse.left == null && traverse.right == null))
			return true;
		return false;
	}

	//remove from the tree
	public boolean remove(Object o) {
		TreeNode traverse = this.getRoot();
		TreeNode retrieve = this.getRoot();
		TreeNode retrieveParent = this.getRoot();
		TreeNode retrieveParent1 = this.getRoot();
		boolean where = true;
		if(!contains(o)) {
			return false;
		} 
		if(traverse.compareTo(o) == 0) {
			if(traverse.left == null && traverse.right == null) {
				traverse.value = null;
				return true;
			}
			else if(traverse.right == null) {
				retrieve = traverse;
				retrieve = retrieve.left;
				traverse.value = retrieve.value;
				traverse.left = retrieve.left;
				traverse.right = retrieve.right;
				return true;
			}
			else if(traverse.left == null) {
				retrieve = traverse;
				retrieve = retrieve.right;
				traverse.value = retrieve.value;
				traverse.left = retrieve.left;
				traverse.right = retrieve.right;
				return true;
			}
			else {
				retrieve = traverse;
				retrieve = retrieve.getRight();
				if(retrieve.left == null) {
					traverse.value = retrieve.value;
					traverse.right = retrieve.right;
					return true;
				}
				while(retrieve.left != null) {
					retrieveParent1 = retrieve;
					retrieve = retrieve.getLeft();
				}
				traverse.value = retrieve.value;
				retrieveParent1.left = null;
				return true;
			}
		}
		while(traverse.compareTo(o) != 0) {
			if(traverse.compareTo(o) > 0) {
				retrieveParent = traverse;
				traverse = traverse.getLeft();
				where = true;
			}
			else if(traverse.compareTo(o) < 0) {
				retrieveParent = traverse;
				traverse = traverse.getRight();
				where = false;
			}
		}
		if(traverse.right == null && traverse.left == null) {
			traverse.value = null;
			if(where) 
				retrieveParent.left = null;
			else 
				retrieveParent.right = null;
			return true;
		}
		else if (traverse.right == null) {
			traverse.value = null;
			if(where) 
				retrieveParent.left = traverse.left;
			else 
				retrieveParent.right = traverse.left;
			return true;
		}
		else  {
			retrieve = traverse;
			retrieve = retrieve.getRight();
			if(retrieve.left == null) {
				if(where) 
					retrieveParent.left = retrieve;
				else 
					retrieveParent.right = retrieve;
				return true;
			}
			while(retrieve.left != null) {
				retrieveParent1 = retrieve;
				retrieve = retrieve.getLeft();
			}
			retrieveParent1.left = retrieve.right;
			traverse.value = retrieve.value;
			return true;
		}
	}

//to find the size of the tree
	public int size() {
		TreeNode traverse = this.getRoot();
		TreeNode parent = this.getRoot();
		int size = 0;
		if(parent.value == null && parent.left == null && parent.right == null)
			return 0;
		return size(parent,traverse,size);
	}
	public int size(TreeNode parent,TreeNode traverse,int size) {
		if(traverse != null) {
			size ++;
		}
		if (traverse.getLeft() != null) {
			parent = traverse;
			traverse = traverse.getLeft();
			size = size(parent,traverse,size);
			traverse = parent;
		}
		if(traverse.getRight() != null) {
			parent = traverse;
			traverse = traverse.getRight();
			size = size(parent,traverse,size);
			traverse = parent;
		} 
		return size;
	}
	
	public static void main(String args[]) throws IOException {
		TreeSetNew tsn = new TreeSetNew();
		//int length = args.length;
		String choice = new String();							// User interactive choices
		
		try {
		while (true) {
			System.out.println("\n ----------------------------------------------------------------- ");
			System.out.println("Enter your choice "); 
			System.out.println("1. Insert elements ");
			System.out.println("2. Search the tree for the presence of an element ");
			System.out.println("3. Display tree stats  # of nodes)");
			System.out.println("4. Check if the tree is empty");
			System.out.println("5. Clear the tree set");
			System.out.println("6. Removed an object from the tree set");
			System.out.println("7. Exit");
			System.out.println(" ------------------------------------------------------------------ \n");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			choice = br.readLine();
			
			if(choice.equals("1")) {
				System.out.println("\nEnter element of the form 5 6 7 8 or 1 element also");
				String stemp;
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				stemp = br1.readLine();
			    Scanner inputNoArgs = new Scanner(stemp);
			    while(inputNoArgs.hasNext()) {
			    	tsn.add((Object)inputNoArgs.next()); // Insert nodes
			    }
			}
			else if(choice.equals("2")) {
				System.out.println("\nEnter the Object to be searched");
				String stemp;
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				stemp = br1.readLine();
				boolean flag = tsn.contains((Object)stemp); // Search elements
				if(flag) {
					 System.out.println("Yipee !! your number " + stemp + " is present ");
				}
				else {
					System.out.println("Sorry buddy unavailable !!");
				}
			}
			else if (choice.equals("3")) {
				System.out.println("# nodes in the tree   :: " + tsn.size());		// Display size
			}
			else if (choice.equals("4")) {
				if(tsn.isEmpty()) 
					System.out.println("Tree Set is empty !!");
				else 
					System.out.println("Tree Set is not Empty !!");
			}
			else if (choice.equals("5")) { 
				tsn.clear();
				System.out.println("Tree Set is cleared!!");
			}
			else if (choice.equals("6")) {
				System.out.println("\nEnter the object to be removed : ");
				String stemp;
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				stemp = br1.readLine();
				if(tsn.contains((Object)stemp)) {
					tsn.remove((Object)stemp);
					System.out.println(stemp + " is removed from the tree");
				}
				else {
					System.out.println(stemp + "is not present in your tree set");
				}
			}
			else if(choice.equals("7")) {
				System.out.println("Exiting Shortly !!");
				System.exit(0);
			}
			else {
				System.out.println("\n Incorrect choice, please try again");
			}
		}
		}
		catch (NumberFormatException nfe) {
			System.out.println("Illegal number fomat !!" );
			System.exit(0);
		}
		catch (EmptyStackException ee) {
    		System.out.println("Empty stack !! ");
    		System.exit(0);
    	}
    	catch (NullPointerException ne) {
    		System.out.println("Null pointer, no elements in the bst, enter some nuo's");
    		System.exit(0);
    	}
    	catch (Exception e) {
    		System.out.println ("Other exception occured !! \n" + e);
    		System.exit(0);
    	}
	}

}


class TreeNode implements Comparable<Object> {
	Object value;
	TreeNode left;
	TreeNode right;
	
	public TreeNode(Object value,TreeNode left,TreeNode right) {
		this.value = value;
		this.left = left;
		this.right = right;
	}
	
	public void setLeft(TreeNode left) {
		this.left = left;
	}
	public void setRight(TreeNode right) {
		this.right = right;
	}
	
	public Object getValue() {
		return(this.value);
	}
	
	public TreeNode getLeft() {
		return(this.left);
	}
	public TreeNode getRight() {
		return(this.right);
	}	
	//object comparator to find comparison between the Objects 
	public int compareTo(Object arg0) {
		boolean flag = false;
		try {
			Double.parseDouble(arg0.toString());
			Double.parseDouble(value.toString());
		}
		catch(NumberFormatException nfe) {
			flag = true;
		}
		if(flag) {
			if(value.toString().compareTo(arg0.toString()) < 0)
				return -1;
			else if(value.toString().compareTo(arg0.toString()) > 0) 
				return 1;
			return 0;
		}
		if(Double.parseDouble(value.toString()) < Double.parseDouble(arg0.toString())) 
			return -1;
		else if(Double.parseDouble(value.toString()) > Double.parseDouble(arg0.toString()))
			return 1;
		return 0;
	}
}

