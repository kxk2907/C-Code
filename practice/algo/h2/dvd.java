import java.util.Scanner;

public class dvd {
	public static void main(String args[]) {
		Scanner sc = new Scanner(System.in);
		int number = 0;											//input 'n' array size 
		Integer array[] = null;
		String str[] = null;
		Integer sum = 0;
		Integer quotient = 0;
		try {
			if(sc.hasNextLine()) 
				number = Integer.parseInt(sc.nextLine());		//1st line size of array
			if(number != 0) {
				array = new Integer[number];
				str = new String[number];
			}
			if(sc.hasNextLine())  								//2nd line contents of array
				str = sc.nextLine().split(" ");
			
			for(int i = 0;i<number;i++) { 						//Read input and pushing to integer array 
				array[i] = Integer.parseInt(str[i]);
				sum = sum + array[i];
			}
			
			if((sum % number) != 0) {
				System.out.println("IMPOSSIBLE");
				System.exit(1);
			}
			else 
				System.out.println("POSSIBLE");
			quotient = sum/number;
			Possibilities(array,number,quotient);
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
	
	public static void Possibilities(Integer[] array, Integer number, Integer quotient) {
		Integer current = 0;
		Integer cumulative = 0;
		LinkedList ll = new LinkedList();
		Integer counter = 0;
		for(int i = 0 ; i < number ; i++ ){
			current = 0;
			current = array[i] - quotient;
			if(current > 0) {
				if(cumulative < 0) {
					int temp = array[i] + cumulative;
					if(temp <= 0) {
						int temp1 = array[i];
						array[i-1] = array[i-1] + array[i];
						array[i] = 0;
						//cumulative = cumulative + temp1; correct this
						ll.add(i,i-1,temp1);
						counter ++;
						if(i > 0)
							i = i - 2;
					}
					else if (temp > 0) {
						array[i-1] = array[i-1] - cumulative; 
						array[i] = array[i] + cumulative;
						ll.add(i,i-1,(-cumulative));
						counter ++;
						cumulative = 0;
						current = array[i] - quotient;
						cumulative = current;
						if(array[i-1] > quotient) {
							if(i > 0)
								i = i - 2;
							cumulative = 0;
						}
						else if(array[i-1] == quotient) {
							if(cumulative > 0) {
								array[i+1] = array[i+1] + cumulative;
								array[i] = array[i] - cumulative;
								ll.add(i,i+1,cumulative);
								counter ++;
								cumulative = 0;
							}
						}
					}
				}
				else if(cumulative == 0) { 
					if (i != 0) {
						if (array[i - 1] < quotient) {
							int temp = array[i] - quotient;
							array[i] = array[i] - temp;
							array[i - 1] = array[i - 1] + temp;
							ll.add(i, i - 1, temp);
							counter ++;
							if (array[i - 1] > quotient) {
								if (i > 0)
									i = i - 2;
								cumulative = 0;
							}
						} else {
							array[i + 1] = array[i + 1] + current;
							array[i] = array[i] - current;
							ll.add(i, i + 1, current);
							counter ++;
						}
					} else {
						array[i + 1] = array[i + 1] + current;
						array[i] = array[i] - current;
						ll.add(i, i + 1, current);
						counter ++;
					}
				}
			}
			else if(current <= 0) {
				cumulative = cumulative + current;
			}
		} 
		LinkedList tempList = ll;
		System.out.println(counter);
		while(tempList != null) {
			tempList.log();
			tempList = tempList.next;
		}
		System.out.println();
	}
}

class LinkedList {
	Integer from = null;
	Integer to = null;
    Integer value = null;
    LinkedList next = null;
    void add( Integer from, Integer to, Integer value ){
            if( this.value == null ) {
            	this.from = from;
            	this.to = to;
                this.value = value;
            }
            else{
                    if(next == null)
                            next = new LinkedList();
                    next.add(from,to,value);
            }
    }
    void log() {
    		System.out.println((this.from+1) + " " + (this.to + 1) + " " + this.value);
	}
}


