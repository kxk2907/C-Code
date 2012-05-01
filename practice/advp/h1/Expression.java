/**
 * Evaluate the arithmetic expressions involving +,-,%,*,/,^ in the order
 *
 *
 * @version   $Id: Expression.java,v 1.1 2010/09/12 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu)
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/12 12:58:00  kxk2907
 *      Initial revision
 *
 */

//Import statements
import java.util.Vector; //For using vectors
import java.util.Stack;	 // Using Stack for parsing and retrieving
import java.lang.Math;   // Using Math function pow

/*
 * This Program accept arithmetic opertaions as input and provides the result.
 * Operations allowed : +,-,%,*,/,^ (Precedence left to right always)
 * 
 * Logic used : 1. Read the expression and put onto a Vector so that each element 
 * 				   in vector is either an operator or a number 
 * 					eg : For expression 2 ^ 3 ^ 2 * 5 * 6 + 8 * 5 + 100 * 2 + 3 
 * 						Limitation : (spaces between operator and number is a must)
 * 						[2, ^, 3, ^, 2, *, 5, *, 6, +, 8, *, 5, +, 100, *, 2, +, 3] 
 * 				2. Push the operators and numbers onto a different stacks 
 * 					operators -> operatorStack
 * 					numbers  -> numberStack
 * 				3. For Each operators in array operators[] based on the order of precedence match for 
 * 				   operators in operatorStack and pick the corresponding numbers from the number stack 
 * 				   and calculate the required result and store it back to numberStack. Remove the operator 
 * 				   from the operatorStack after a particular operation. 
 * 				4. Once all the operations are over retrieve the final value left in the numberStack when 
 * 				   the operatorStack is empty.
 * 
 * NOTE : I have used '.' instead of '*' for multiplication since * operator when read through command line
 * 		  is taking entire details of the code. For example .classpath etc which means * is understood in a 
 * 		  different way.
 */

//Class Expression starts here 
class Expression {	
	 public static Stack<Double> numberStack = new Stack<Double>();			//Stack for numbers
     public static Stack<String> operatorStack = new Stack<String>();			//Stack for operators
     public static String[] operators = { "+", "-", "%", "*", "/", "^" };		//String of operators

 public static void main (String args []) {						//main program
       Vector<String> aLine = new Vector<String>(); 					//Vector for reading inputs
     for ( String arg: args ) 
             aLine.add(arg.equals(".") ? "*" : arg) ;					//'.' is used refer NOTE 
     if ( aLine.size() > 0 )
             System.out.println("java Expression :" + aLine + "\n and the answer is: \n" + calculate(aLine) + "\n");	//calculate
 }
 //calculate Method starts here : Vector of type String is passed as an argument
 public static double calculate(Vector<String> aLine) {
	 for (int i=0;i<aLine.size();i++){							
		// For each vector push elements into numberStack and operatorStack 
		 if(aLine.elementAt(i).equals("+") || aLine.elementAt(i).equals("-") ||  
				 aLine.elementAt(i).equals("%") || aLine.elementAt(i).equals("*") ||	
				 aLine.elementAt(i).equals("/") || aLine.elementAt(i).equals("^")){
			 operatorStack.push(aLine.elementAt(i));
		 }
		 else {
			 //System.out.println(aLine.elementAt(i));
			try{ 
			 numberStack.push((Double.valueOf(aLine.elementAt(i))));	//If the element at that position 
			}catch(NumberFormatException nfe){				// is not a number throw an exception
				 System.out.println(aLine.elementAt(i));
				System.out.println("inside number format exception");
			}
			}
	 }
	 for(int j=5;j>=0;j--){							//Main logic for calculation starts here
		 double num1=0.0,num2=0.0,regexp = 0.0;					//num1 and num2 from numberStack 
		 int k = operatorStack.size();
		 boolean flag = false;						// flag is required to determine when and element 
		 for (int i = 0;i<k;i++) {					// so that it is useful to decrement the stack size
			 if(flag) {
				 i = i - 1;
				 k = k - 1;
				 flag = false;
			 }
			 if(operatorStack.elementAt(i).equals(operators[j])) {	// if operatorStack and operators[j] are equal
 				try { 
				 num1 = numberStack.elementAt(i);
				 num2 = numberStack.elementAt(i+1);
				}
				catch (Exception e)
					{
						System.out.println("\nArray out of Bound Exception occured !! \n You have entered a wrong input \n ");
					}
				switch (j){					//Do operation based on the operator
				 	case 0:
					 	regexp = num1 + num2;
					 	break;
				 	case 1:
				 		regexp = num1 - num2;
				 		break;
				 	case 2:
				 		regexp = num1 % num2;
				 		break;
				 	case 3:
				 		regexp = num1 * num2;
				 		break;
				 	case 4:
				 		regexp = num1 / num2;
				 		break;
				 	case 5:
				 		regexp = Math.pow(num1, num2);
				 		break;
				 }
				 operatorStack.remove(i);		// After operation remove operator from operatorStack
				 numberStack.remove(i);			// Remove numbers num1 and num2 from numberStack
				 numberStack.remove(i);
				 numberStack.add(i,regexp);		//Add the result from numberStack
				 flag = true;				// Set flag = true so that say number is removed
			 }	 
		 }
		 
	 }	
	 return numberStack.pop();					// Last number from the number stack to be popped
 }
}
