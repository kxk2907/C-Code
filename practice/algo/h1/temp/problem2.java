import java.util.Scanner;

/**
 * 
 */

/**
 * @author Shanky
 *
 */
public class problem2 {

	public static void main( String args[] ){
		Scanner sc = new Scanner(System.in );
		int n = 0,k;
		if(sc.hasNextLine())
			n = Integer.parseInt(sc.nextLine());
		//System.out.println("n = " + n);
		Integer b[][] = new Integer[n][3];
		LLT c[] = new LLT[n];
		LLT d[] = new LLT[n];
		String str[] = null;
		//System.out.println("Numbers are :\n");
		if(sc.hasNextLine())
			str = sc.nextLine().split(" ");
		for( int i = 0; i < n; i++ ){
			k = Integer.parseInt(str[i]);
			//System.out.print(k + "  ");
			b[i][0] = k;
			b[i][1] = (k % (n));
			k = k/n;
			b[i][2] = (k % (n));
			//System.out.println("--->" + b[i][1] + "--" + b[i][2]);
			if( k / n != 0 ){
				System.out.println("Input Error: Input exceeds n2 - 1 !!!");
				System.exit(1);
			}
		}
		for( int i = 0; i < n; i++){
			c[i] = new LLT();
			d[i] = new LLT();
		}
		//System.out.println();
		for( int i = 0; i < n; i++){
			c[b[i][1]].add(i);
			//System.out.println("------------"+b[i][1] + " " + b[i][0]);
		}
		k=0;
		for( int i = 0; i < n && k < n; i++){
			LLT temp = c[i];
			while( temp != null && temp.value != null ){
				//System.out.println("--"+b[temp.value][0] + " + " + b[temp.value][2]);
				d[b[temp.value][2]].add(temp.value);
				k++;
				temp = temp.next;
			}
		}
		//System.out.println("After sorting...\n");
		k = 0;
		for( int i = 0; i < n && k < n; i++){
			LLT temp = d[i];
			while( temp!= null && temp.value != null){
				System.out.print( b[temp.value][0] + " ");
				k++;
				temp = temp.next;
			}
		}
	}
}
class LLT {
	Integer value = null;
	LLT next = null;
	void add( int value ){
		if( this.value == null )
			this.value = value;
		else{
			if(next == null)
				next = new LLT();
			next.add( value );
		}
	}
	
	void delete(){
		
	}

}
