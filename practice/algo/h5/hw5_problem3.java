/**
 * 
 */


import java.util.Scanner;

/**
 * @author Shanky
 *
 */
public class hw5_problem3 {
	
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in );
		Integer n = 0, m = 0;
		String[] mn;
		if(sc.hasNextLine()){
			mn = sc.nextLine().split(" ");
			m = Integer.parseInt(mn[0]);
			n = Integer.parseInt(mn[1]);
		}
		Integer[][] maze = new Integer[n][n];
		for(int i = 0;i<m;i++) {
			if(sc.hasNextLine()) {
				String[] column = sc.nextLine().split(" ");
				for(int j = 0;j<n;j++) 
					maze[i][j] = Integer.parseInt(column[j]);
			}
		}
		distance( maze, m, n );
	}

	/**
	 * @param maze
	 * @param m
	 * @param n
	 */
	private static void distance(Integer[][] maze, Integer m, Integer n) {
		boolean[][] traversed = new boolean[m][n];
		Node source;
		Integer starti=-1, startj=-1;
		for(int i = 0;i<m;i++) {
			for(int j = 0;j<n;j++){
				if(maze[i][j] == 2){
					starti = i;
					startj = j;
				}
				if(maze[i][j] == 3){
					Node.finishi = i;
					Node.finishj = j;
				}
			}
		}
		if( starti == -1 ){
			System.out.println("input error !!!1");
			System.exit(1);
		}
		source = new Node(starti, startj, 0);
		Node.end = source;
		traversed[starti][startj] = true;
		while(true){
			try{
			if( !traversed[source.x - 1][source.y] && maze[source.x - 1][source.y] != 1  ){
				source.add(source.x - 1,source.y,source.distance+1);
				traversed[source.x - 1][source.y] = true;
			}
			}
			catch(ArrayIndexOutOfBoundsException e ){}
			try{
			if( !traversed[source.x + 1][source.y] && maze[source.x + 1][source.y] != 1  ){
				source.add(source.x + 1,source.y,source.distance+1);
				traversed[source.x + 1][source.y] = true;
			}
			}
			catch(ArrayIndexOutOfBoundsException e ){}
			try{
			if( !traversed[source.x][source.y - 1] && maze[source.x][source.y - 1] != 1  ){
				source.add(source.x,source.y - 1,source.distance+1);
				traversed[source.x][source.y - 1] = true;
			}
			}
			catch(ArrayIndexOutOfBoundsException e ){}
			try{
			if( !traversed[source.x][source.y + 1] && maze[source.x][source.y + 1] != 1  ){
				source.add(source.x,source.y + 1,source.distance+1);
				traversed[source.x][source.y + 1] = true;
			}
			}
			catch(ArrayIndexOutOfBoundsException e ){}
			if(source.nxt != null)
				source = source.nxt;
			else {
				System.out.println("No such Path defined !!");
				System.exit(1);
			}
		}
	}
}


 class Node{
	 static Node end = null;
	 static int finishi, finishj;
	 Node nxt=null;
	 long distance;
	 int x, y;
	 Node(){
		 //end = this;
	 }
	 /**
	 * @param x2
	 * @param y2
	 * @param distance2
	 */
	public Node(int x2, int y2, long distance2) {
		x = x2; y = y2; distance = distance2;
		if(finishi == x && finishj == y){
			System.out.println(distance);
			System.exit(1);
		}
	}
	public void add(int x, int y, long distance){
		end.nxt = new Node(x, y, distance);
		end = end.nxt; 
	 }
 }

