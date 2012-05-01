import java.util.Scanner;

public class hw5_problem2 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Integer v = 0,e = 0;
		String[] temp = new String[2];
		if(sc.hasNextLine()) {
			temp = sc.nextLine().split(" ");
			v = Integer.parseInt(temp[0]);
			e = Integer.parseInt(temp[1]);
		}
		Vertex[] V = new Vertex[v + 1];
		for(Integer i = 0;i<e;i++) {
			if(sc.hasNextLine()) {
				temp = new String[2];
				temp = sc.nextLine().split(" ");
				if(V[Integer.parseInt(temp[0])] == null) {
					V[Integer.parseInt(temp[0])] = new Vertex(Integer.parseInt(temp[0]),new Neighbor(Integer.parseInt(temp[1]),null));
				}
				else { // already there is a Neighbor
					Neighbor tempNeighbor = V[Integer.parseInt(temp[0])].nbr.getLast();
					tempNeighbor.next = new Neighbor(Integer.parseInt(temp[1]),null);
				}
			}
		}
		for(Integer i = 1;i < v+1;i++) {
			if(V[i] == null)
				V[i] = new Vertex(i,null);
		}
		System.out.println(connectCheck(V,v));
	}
	
	public static Integer connectCheck(Vertex[] V,Integer v) {
		Integer counter = 0;
		for(Integer i = 1;i<v+1;i++) {
			if(!V[i].isReached()) {
				DFS(V,i);
				counter ++;
			}
		}
		return counter;
	}
	
	public static void DFS(Vertex[] V,Integer i) {
		V[i].setReach();
		Neighbor current = V[i].nbr;
		while(current != null) {
			if(!V[current.nbrvalue].isReached())
				DFS(V,current.nbrvalue);
			current = current.next;
		}
	}
}

class Vertex {
	Integer value;
	boolean reach = false;
	Neighbor nbr;
	Vertex(Integer value,Neighbor nbr) {
		this.value = value;
		this.nbr = nbr;
	}
	boolean isReached() {
		return this.reach;
	}
	void setReach() {
		this.reach = true;
	}
}

class Neighbor {
	Integer nbrvalue;
	Neighbor next;
	static Neighbor last;
	Neighbor(Integer nbrvalue,Neighbor next) {
		this.nbrvalue = nbrvalue;
		this.next = next;
		last = this;
	}
	Neighbor getLast() {
		return last;
	}
}
