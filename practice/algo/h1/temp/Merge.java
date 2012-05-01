import java.util.Scanner;

public class Merge {
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
			mergeSort(array,0,number-1);
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
	public static void mergeSort(Integer[] array,int p,int r) {
		int q = 0;
		if(p < r) {
			q = (p+r)/2;
			mergeSort(array,p,q);
			mergeSort(array,q+1,r);
			merge(array,p,q,r);
		}
	}
	public static void merge(Integer[] array,int p,int q,int r) {
		int n1 = q-p+1;
		int n2 = r-q;
		Integer[] A = new Integer[n1];
		Integer[] B = new Integer[n2];
		for(int i = 0;i<n1;i++) 
			A[i] = array[i+p];
		for(int i = 0;i<n2;i++)
			B[i] = array[i+q+1];
		for(int i=p,j=0,k=0;i<=r;i++) {
			if(j < n1 && k < n2 ) {
				if(A[j] < B[k]) {
					array[i] = A[j];
					j++;
				}
				else {
					array[i] = B[k];
					k++;
				}
			}
			else if(j >= n1 && k >= n2) 
				return;
			else {
				if(j >= n1) {
					array[i] = B[k];
					k++;
				}
				else if(k >= n2) {
					array[i] = A[j];
					j++;
				}
			}
		}
		return;
	}
}

