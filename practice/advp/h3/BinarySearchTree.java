/**
 * Find word count in a file.
 *
 *
 * @version   $Id: BinarySearchTree.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
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
 * BinarySearchTree class 
 */
class BinarySearchTree implements BinarySearchTreeInterface {
	Node treenode;
	static int hi = 0;
	static int si = 0;
	
	public static void main(String args[]) throws IOException { // Read inputs from user invalid inputs throws IOException
		BinarySearchTree bst = new BinarySearchTree();
		int length = args.length;
		String choice = new String();							// User interactive choices
		for(String arg : args) 
			bst.insert(Integer.parseInt(arg));					// Convert string to integer
		
		try {
		while (true) {
			if(length == 0) {
				length ++;
				System.out.println("\nYou might have to enter the nodes !! accepted form :: number seperated by spaces ");
				String stemp;
				BufferedReader br = new BufferedReader(new InputStreamReader(System.in)); // reading inputs using buffered reader
				stemp = br.readLine();
			    Scanner inputNoArgs = new Scanner(stemp);
			    while(inputNoArgs.hasNext()) {
			    	bst.insert(Integer.parseInt(inputNoArgs.next()));
			    }
			}
			System.out.println("\n ----------------------------------------------------------------- ");
			System.out.println("Enter your choice "); 
			System.out.println("1. Insert more elements ");
			System.out.println("2. Search the tree for the presence of an element ");
			System.out.println("3. Display tree stats (height + no of nodes)");
			System.out.println("4. Exit the program ");
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
			    	bst.insert(Integer.parseInt(inputNoArgs.next())); // Insert nodes
			    }
			}
			else if(choice.equals("2")) {
				System.out.println("\nEnter the number to be searched");
				String stemp;
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				stemp = br1.readLine();
				boolean flag = bst.contains(Integer.parseInt(stemp)); // Search elements
				if(flag) {
					 System.out.println("Yipee !! your number " + stemp + " is present ");
				}
				else {
					System.out.println("Sorry buddy unavailable !!");
				}
			}
			else if (choice.equals("3")) {
				System.out.println("\nHeight of the tree    :: " + bst.height());	// Display height 
				System.out.println("# nodes in the tree   :: " + bst.size());		// Display size
				hi = 0;
				si = 0;
			}
			else if (choice.equals("4")) {
				System.out.println("\nProgram will exit shortly !! Bye Bye \n");
				System.exit(1);
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
	
	public Node getRoot() {
		return treenode;
	}
	
	/*
	 * insert a node : 
	 * 1. if node is null enter the value in the current position
	 * 2. else copy the contents and traverse the node
	 * 	   while the nodes value is not null traverse based on the input value 
	 * 	   if data < current traverse to left or traverse to right
	 */
	public void insert(Integer data) {
		if(treenode == null) {
			treenode = new Node(data.toString(),null,null);
		}
		else {
			Node existingnode = treenode;		
			while(existingnode.getValue() != null) {
				if(data <= Integer.parseInt(existingnode.getValue())) {
					if(existingnode.getLeft()== null ){
						existingnode.setLeft(new Node(data.toString(),null,null));
						return;
					}
					else {
						existingnode = existingnode.getLeft();
					}
				}
				else {
					if(existingnode.getRight() == null){
						existingnode.setRight(new Node(data.toString(),null,null));
						return;
					}
					else {
						existingnode = existingnode.getRight();
					}
				}
 			}
		}
	}
	
	/*
	 * Using size(method overriding) to find the size 
	 */
	public int size() {	
		Node traverse = this.getRoot();
		Node parent = this.getRoot();
		if(parent == null)
			return 0;
		return size(parent,traverse);
	}

	/*
	 * Always keep track of a parent use recursion 
	 */
	public int size(Node parent,Node traverse) {
		if(traverse != null) {
			si ++;
		}
		if (traverse.getLeft() != null) {
			parent = traverse;
			traverse = traverse.getLeft();
			size(parent,traverse);
			traverse = parent;
		}
		if(traverse.getRight() != null) {
			parent = traverse;
			traverse = traverse.getRight();
			size(parent,traverse);
			traverse = parent;
		} 
		return si;
	}
	
	/*
	 * using method over riding
	 */
	public int height() {
		Node traverse = this.getRoot();
		Node parent = this.getRoot();
		return height(parent,traverse,0);
	}
	
	/*
	 * recursively calculate left and right and always at any time keep track of the 
	 * parent and the current node. Using static variable 'hi' 
	 */
	public int height(Node parent,Node traverse,int currentHeight) {
		if(traverse != null) {
			if(currentHeight >= hi)
				hi = currentHeight;
		}
		if (traverse.getLeft() != null) {
			parent = traverse;
			traverse = traverse.getLeft();
			hi = height(parent,traverse,currentHeight + 1);
			traverse = parent;
		}
		if(traverse.getRight() != null) {
			parent = traverse;
			traverse = traverse.getRight();
			hi = height(parent,traverse,currentHeight + 1);
			traverse = parent;
		} 
		return hi;
	}

	/*
	 * traverse everynode based on the target value if less go left or else go right
	 */
	public boolean contains(Integer target) {
		Node traverse = this.getRoot();
		while(traverse != null) {
			if(target.equals(Integer.parseInt(traverse.getValue()))) {
				return true;
			}
			else if(target <= Integer.parseInt(traverse.getValue())) {
				traverse = traverse.getLeft();
			}
			else {
				traverse = traverse.getRight();
			}
		}
		return false;
	}
}

/*
 * Defniition of a node
 */
class Node {
	String value;
	Node left;
	Node right;
	
	public Node(String value,Node left,Node right) {
		this.value = value;
		this.left = left;
		this.right = right;
	}
	
	public void setLeft(Node left) {
		this.left = left;
	}
	public void setRight(Node right) {
		this.right = right;
	}
	
	public String getValue() {
		return(this.value);
	}
	public Node getLeft() {
		return(this.left);
	}
	public Node getRight() {
		return(this.right);
	}	
}


