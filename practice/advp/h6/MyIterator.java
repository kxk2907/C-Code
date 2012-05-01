public interface MyIterator {		// Iterator interfaces
	public IteratorList iterator();
	public boolean hasNext();
	public Object next();
	public void remove1(Object obj);
}

class IteratorList {				// Iterator class details
	Object element;
	IteratorList next;
	
	public IteratorList() {
		this.element = null;
		this.next = null;
	}
	public IteratorList(Object element,IteratorList next) {	//constructor to iterator class 
		this.element = element;
		this.next = next;
	}
	public IteratorList putInto(Object element,IteratorList it1) {	//put into the list
		IteratorList temp = it1;
		if(it1.getNext() == null && it1.element == null) {
			it1 = new IteratorList(element,null);
			return it1;
		}
		if(temp.element.equals(element)) 
			return it1;
		while(temp.getNext() != null) {
			if(temp.element.equals(element)) 
				return it1;
			temp = temp.getNext();
		}
		if(temp.element.equals(element)) 
			return it1;
		temp.next = new IteratorList(element,null);
		return(it1);
	}
	
	public IteratorList remove(Object ele,IteratorList it1) { // remove from the list
		IteratorList parent = it1;
		IteratorList traverse = it1;
		if(traverse.element.equals(ele)) {
			traverse.element = null;
			it1 = traverse.next;
			return it1;
		}
		else {
			while(!traverse.element.equals(ele)) {
				parent = traverse;
				traverse = traverse.next;
			}
			if(traverse.next == null) {
				parent.next = null;
				return it1;
			}
			else {
				parent.next = traverse.next;
				return it1;
			}
		}
	}
	public IteratorList getNext() {		// get next
		return this.next;
	}
	public Object getElement() {
		return this.element;
	}
}
