import java.util.Scanner;

public class Quadratic {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int number = 0;
		Integer array[] = null;
		String str[] = null;
		try {
			if(sc.hasNextLine()) 
				number = Integer.parseInt(sc.nextLine());
			if(number != 0) {
				array = new Integer[number];
				str = new String[number];
			}
			if(sc.hasNextLine())  
				str = sc.nextLine().split(" ");
			for(int i = 0;i<number;i++) 
				array[i] = Integer.parseInt(str[i]);
			quad(array,number);
			for(int i = 0;i<number;i++) 
				System.out.print(array[i] + " ");
			System.out.println();
		}
		catch(NumberFormatException nfe) {
			System.out.println("Illegal number Format !!");
			System.exit(1);
		}
		catch(ArrayIndexOutOfBoundsException aobe) {
			System.out.println("Array out of Bound Exception, check the # of numbers !!");
			System.exit(1);
		}
		catch(Exception e) {
			System.out.println("Other Exception !!");
			System.exit(1);
		}
	}
	public static void quad(Integer[] array,int number) {
		for(int i = 0;i<number;i++) {
			for(int j = i+1;j<number;j++) {
				if(array[i] > array[j]) {
					int temp = array[i];
					array[i] = array[j];
					array[j] = temp;
				}
			}
		}
	}
}
