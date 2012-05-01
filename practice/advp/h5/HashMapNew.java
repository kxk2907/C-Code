/**
 * Find word count in a file.
 *
 *
 * @version   $Id: HashMapNew.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
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
import java.io.InputStreamReader;
import java.util.EmptyStackException;
import java.util.Scanner;

/*
 * Implement a Hash Map <key,value> pair 
 * Method Used : 
 * 1. using prime number for buckets 
 * 2. each bucket is a binary tree structure 
 */
public class HashMapNew implements AnotherHashMap {
    final static int prime = 107;
    ListHashCode[] lhc = new ListHashCode[prime];
	public static void main(String args[]) {
		HashMapNew hmn = new HashMapNew();
		String choice = new String();							// User interactive choices
		
		try {
		while (true) {
			System.out.println("\n ----------------------------------------------------------------- ");
			System.out.println("Enter your choice "); 
			System.out.println("1. Insert elements ");
			System.out.println("2. Check if the key is available ");
			System.out.println("3. Check if the value is avaialbel ");
			System.out.println("4. Get the value for the key if available ");
			System.out.println("5. Enter the key/value pair to be removed ");
			System.out.println("6. find the # of the hash tree");
			System.out.println("7. Clear Hash Map ");
			System.out.println("8. First/Last key in the hashmap ");
			System.out.println("9. Exit ");
			System.out.println(" ------------------------------------------------------------------ \n");
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			choice = br.readLine();
			
			if(choice.equals("1")) {
				System.out.println("\nEnter elements of the form Key1 value1 Key2 Value2 Key3 Value3");
				String stemp;
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				stemp = br1.readLine();
			    Scanner inputNoArgs = new Scanner(stemp);
			    while(inputNoArgs.hasNext()) {
			    	hmn.put((String)inputNoArgs.next(), (Object)inputNoArgs.next());
			    }
			}
			else if(choice.equals("2")) {
				System.out.println("\nCheck if the key is available : ");
				String stemp;
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				stemp = br1.readLine();
				boolean flag = hmn.containsKey(stemp);
				if(flag) {
					 System.out.println("Yipee !! yor key " + stemp + " is present in the hash map");
				}
				else {
					System.out.println("Sorry buddy unavailable !!");
				}
			}
			else if(choice.equals("3")) {
				System.out.println("\nCheck if the value is available : ");
				String stemp;
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				stemp = br1.readLine();
				boolean flag = hmn.containsValue((Object)stemp);
				if(flag) {
					 System.out.println("Yipee !! yor value " + stemp + " is present in the hash map");
				}
				else {
					System.out.println("Sorry buddy unavailable !!");
				}
			}
			else if(choice.equals("4")) {
				System.out.println("\nEnter the key : ");
				String stemp;
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				stemp = br1.readLine();
				Object obj = null;
				obj = hmn.get(stemp);
				if(obj != null) {
					 System.out.println("Yipee !! yor value for key" + stemp + " is " + obj);
				}
				else {
					System.out.println("Sorry buddy unavailable !! key unavailable");
				}
			}
			else if(choice.equals("5")) {
				System.out.println("\nEnter the key to be removed : ");
				String stemp;
				BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
				stemp = br1.readLine();
				Object obj = null;
				obj = hmn.remove(stemp);
				if(obj != null) {
					 System.out.println("Yipee !! yor key and value removed " + stemp + " : " + obj);
				}
				else {
					System.out.println("Sorry buddy unavailable !! key unavailable");
				}
			}
			else if (choice.equals("6")) {
				System.out.println("# of hash maps   :: " + hmn.size());		// Display size
			}
			else if (choice.equals("7")) { 
				hmn.clear();
				System.out.println("hash Map is cleared!!");
			}
			else if(choice.equals("8")) {
					 System.out.println("First Key in the hash map : " + hmn.firstKey());
					 System.out.println("Last Key in the hash map : " + hmn.lastKey());
			}
			else if(choice.equals("9")) {
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
	

	public HashMapNew() { // initialize
		for(int i=0;i<prime;i++) {
			lhc[i] = new ListHashCode();
		}
	}
	public Object put(String key, Object value) { // put method puts to the particular tree and value 
		int hashValue =  HashMapNew.hashCode(key);
		if(lhc[hashValue].getTreeSet() != null) {
			TreeSetHash tree = lhc[hashValue].getTreeSet();
			TreeSetHash valueTree = lhc[hashValue].getValueTreeSet();
			valueTree.add(value,key);
			tree.add(key, value);
			lhc[hashValue].setTreeSetHash(tree);
			lhc[hashValue].setTreeSetValue(valueTree);
		}
		else {
			TreeSetHash tree = new TreeSetHash();
			TreeSetHash valueTree = new TreeSetHash();
			tree.add(key,value);
			valueTree.add(value,key);
			lhc[hashValue].setTreeSetHash(tree);
			lhc[hashValue].setTreeSetValue(valueTree);
		}
		return null;
	}

	public static int hashCode(String s1) { // hash code method 
		long sum = 0;
		for(int i = 0;i<s1.length();i++) 
			sum = sum + (long)s1.charAt(i)*10*(i+1);
		return (int)(sum % prime);		
	}
	

	public boolean containsKey(String key) { // check if the key is present or nto
		int hashValue =  HashMapNew.hashCode(key);
		if(lhc[hashValue].getTreeSet() == null) 
			return false;
		else {
			TreeSetHash tree = lhc[hashValue].getTreeSet();
			if(tree.contains(key))
				return true;
			else 
				return false;
		}
	}

	public void clear() { // clear the tree 
		for(int i = 0;i<prime;i++) {
			if(lhc[i].getTreeSet() != null) {
				TreeSetHash tree = lhc[i].getTreeSet();
				tree.clear();
				lhc[i].setTreeSetHash(tree);
				lhc[i].setTreeSetHash(null);
				lhc[i].setHashvalue(0);
			}
		}
	}
	
	public boolean containsValue(Object value) {  // check if the tree contains value or not
		for(int i = 0;i<prime;i++) {
			if(lhc[i].getValueTreeSet() != null) {
				TreeSetHash valueTree = lhc[i].getValueTreeSet();
				if(valueTree.contains(value))
					return true;
			}
		}
		return false;
	}

	public Object get(String key) { // return value for the particular key
		int hashValue =  HashMapNew.hashCode(key);
		if(lhc[hashValue].getTreeSet() == null) 
			return "Key Not Present !!";
		else {
			TreeSetHash valueTree = lhc[hashValue].getTreeSet();
			if(valueTree.contains(key)) {
				return valueTree.getVal(key);
			}
		}
		return null;
	}
	
	public Object remove(String key) { // remove a key value pair 
		int hashValue =  HashMapNew.hashCode(key);
		Object obj = null;
		if(lhc[hashValue].getTreeSet() == null) 
			return "Key Not Present !!";
		else {
			TreeSetHash tree = lhc[hashValue].getTreeSet();
			TreeSetHash valueTree = lhc[hashValue].getValueTreeSet();
			if(tree.contains(key)) {
				obj = tree.getVal(key);
				tree.remove(key);
				valueTree.remove(obj);
				lhc[hashValue].setTreeSetHash(tree);
				lhc[hashValue].setTreeSetValue(valueTree);
				return obj;
			}
		}
		return "Removal unsuccessful !!";
	}
	

	public Object firstKey() {   // first and last key methods
		Object objPrev = null;
		Object objNow = null;
		for(int i = 0;i<prime;i++) {
			TreeSetHash tree = lhc[i].getTreeSet();
			if(tree != null ) {
				objNow = tree.getLeftMost(tree.getRoot());
				if(objNow != null && objPrev != null) {
					if(objPrev.toString().compareTo(objNow.toString()) > 0) {
						objPrev = objNow;
					}
				}
				else if(objPrev == null) 
					objPrev = objNow;
			}
		}
		return objPrev;
	}

	public Object lastKey() {
		Object objPrev = null;
		Object objNow = null;
		for(int i = 0;i<prime;i++) {
			TreeSetHash tree = lhc[i].getTreeSet();
			if(tree != null ) {
				objNow = tree.getRightMost(tree.getRoot());
				if(objNow != null && objPrev != null) {
					if(objPrev.toString().compareTo(objNow.toString()) < 0) {
						objPrev = objNow;
					}
				}
				else if(objPrev == null) 
					objPrev = objNow;
			}
		}
		return objPrev;
		
	}

	public int size() {			// size of the hash map
		int size = 0;
		for(int i = 0;i<prime;i++) {
			TreeSetHash tree = lhc[i].getTreeSet();
			if(tree != null) 
				size = size + tree.size();
		}
		return size;
	}	
}

class ListHashCode {
	private TreeSetHash tree;			// tree for key value 
	private TreeSetHash valueTree;		// tree for value key
	private int hashvalue;
	
	public ListHashCode() {
		tree = null;
		valueTree = null;
	}
	public ListHashCode(TreeSetHash tree, TreeSetHash valueTree, int hashvalue) {
		this.tree = tree;
		this.valueTree = valueTree;
		this.hashvalue = hashvalue;
	}
	public void setTreeSetHash(TreeSetHash tree) {		// accessor mutatot functions
		this.tree = tree;
	}
	public void setTreeSetValue(TreeSetHash valueTree) {
		this.valueTree = valueTree;
	}
	public void setHashvalue(int hashvalue) {
		this.hashvalue = hashvalue;
	}
	public TreeSetHash getTreeSet() {	// returns a tree set
		if(tree == null)
			return null;
		else 
			return this.tree;
	}
	public TreeSetHash getValueTreeSet() {
		if(valueTree == null)
			return null;
		else 
			return this.valueTree;
	}
	public int getValue() {
		return this.hashvalue;
	}
}

class TreeSetHash {		// contains tree nodes
	TreeNode1 treenode;
	public TreeSetHash() {
		treenode = null;
	}
	public TreeNode1 getRoot() {
		return treenode;
	}
	public boolean add(Object o,Object o1) { // add into the tree 
		if(treenode == null) {
			treenode = new TreeNode1(o,o1,null,null);
		}
		else {
			TreeNode1 existingnode = treenode;		
			TreeNode1 treeTemp = new TreeNode1(o,o1,null,null);
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

	public void clear() {		// clear from the tree
		TreeNode1 traverse = this.getRoot();
		TreeNode1 parent = this.getRoot();
		if(parent.value == null && parent.value1 == null && parent.left == null && parent.right == null)
			return;
		if(clear(parent,traverse)) {
			//System.out.println("Tree cleared");
		}else {
			//System.out.println("Tree not cleared");
		}
	}
	
	public boolean clear(TreeNode1 parent,TreeNode1 traverse) {
		if (traverse.getLeft() != null) {
			parent = traverse;
			traverse = traverse.getLeft();
			clear(parent,traverse);
			traverse.value = null;
			traverse.value1 = null;
			parent.left = null;
			traverse = parent;
		}
		if(traverse.getRight() != null) {
			parent = traverse;
			traverse = traverse.getRight();
			clear(parent,traverse);
			traverse.value = null;
			traverse.value1 = null;
			parent.right = null;
			traverse = parent;
		} 
		traverse.value = null;
		return true;
	}

	public boolean contains(Object o) {	// check if it is present or not
		TreeNode1 traverse = this.getRoot();
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

	public Object getVal(Object o) { // get value for a particular string 
		TreeNode1 traverse = this.getRoot();
		if(traverse.value == null)
			return false;
		while(traverse != null) {
			if(traverse.compareTo(o) == 0) {
				return traverse.value1;
			}
			else if(traverse.compareTo(o) > 0) {
				traverse = traverse.getLeft();
			}
			else {
				traverse = traverse.getRight();
			}
		}
		return "Value not present";
	}
	
	public boolean isEmpty() {
		TreeNode1 traverse = this.getRoot();
		if(traverse == null || (traverse.value == null && traverse.value1 == null && traverse.left == null && traverse.right == null))
			return true;
		return false;
	}

	// logic - take care of root and then take care of all leaves
	public boolean remove(Object o) { //remove method 
		TreeNode1 traverse = this.getRoot();
		TreeNode1 retrieve = this.getRoot();
		TreeNode1 retrieveParent = this.getRoot();
		TreeNode1 retrieveParent1 = this.getRoot();
		boolean where = true;
		if(!contains(o)) {
			return false;
		} 
		if(traverse.compareTo(o) == 0) {
			if(traverse.left == null && traverse.right == null) {
				traverse.value = null;
				traverse.value1 = null;
				return true;
			}
			else if(traverse.right == null) {
				retrieve = traverse;
				retrieve = retrieve.left;
				traverse.value = retrieve.value;
				traverse.value1 = retrieve.value1;
				traverse.left = retrieve.left;
				traverse.right = retrieve.right;
				return true;
			}
			else if(traverse.left == null) {
				retrieve = traverse;
				retrieve = retrieve.right;
				traverse.value = retrieve.value;
				traverse.value1 = retrieve.value1;
				traverse.left = retrieve.left;
				traverse.right = retrieve.right;
				return true;
			}
			else {
				retrieve = traverse;
				retrieve = retrieve.getRight();
				if(retrieve.left == null) {
					traverse.value = retrieve.value;
					traverse.value1 = retrieve.value1;
					traverse.right = retrieve.right;
					return true;
				}
				while(retrieve.left != null) {
					retrieveParent1 = retrieve;
					retrieve = retrieve.getLeft();
				}
				traverse.value = retrieve.value;
				traverse.value1 = retrieve.value1;
				//retrieveParent1.right = retrieve.right;
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
			traverse.value1 = null;
			if(where) 
				retrieveParent.left = null;
			else 
				retrieveParent.right = null;
			return true;
		}
		else if (traverse.right == null) {
			traverse.value = null;
			traverse.value1 = null;
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
			traverse.value1 = retrieve.value1;
			return true;
		}
	}

	public int size() {
		TreeNode1 traverse = this.getRoot();
		TreeNode1 parent = this.getRoot();
		int size = 0;
		if(parent.value == null && parent.value1 == null && parent.left == null && parent.right == null)
			return 0;
		return size(parent,traverse,size);
	}
	public int size(TreeNode1 parent,TreeNode1 traverse,int size) {	// returns size of the tree
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
	public Object getLeftMost(TreeNode1 traverse) {		// returns lowest in the tree to get the lowest key
		if(traverse == null) 
			return null;
		else {
			while(traverse.left != null) {
				traverse = traverse.left;
			}
			return (traverse.value);
		}
	}
	public Object getRightMost (TreeNode1 traverse) {// returns right most in the tree to get the highest key
		if(traverse == null) 
			return null;
		else {
			while(traverse.right != null) {
				traverse = traverse.right;
			}
			return (traverse.value);
		}
	}
}


class TreeNode1 implements Comparable<Object> {
	Object value;
	Object value1;
	TreeNode1 left;
	TreeNode1 right;
	
	public TreeNode1(Object value,Object value1,TreeNode1 left,TreeNode1 right) {
		this.value = value;
		this.value1 = value1;
		this.left = left;
		this.right = right;
	}
	
	public void setLeft(TreeNode1 left) {
		this.left = left;
	}
	public void setRight(TreeNode1 right) {
		this.right = right;
	}
	
	public Object getValue() {
		return(this.value);
	}
	
	public Object getValue1() {
		return(this.value1);
	}
	
	public TreeNode1 getLeft() {
		return(this.left);
	}
	public TreeNode1 getRight() {
		return(this.right);
	}	
	public int compareTo(Object arg0) {		// comparator for comparing things
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
