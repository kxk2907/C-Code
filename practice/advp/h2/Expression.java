/*
 * This Program accept arithmetic operations as input and provides the result.
 * Operations allowed : +,-,%,*,/,^,(,) (Precedence left to right always)
 * 
 * Logic Used : 1. Maintain two stacks, one for numbers and the other one for operators.
 * 				2. let's say expression 2+3*5. Here 3*5 has to be done first before 2 +,
 * 					As we read the input 2 goes to numberStack and + goes to operatorStack.
 * 					When 3 comes in, the next operator in the iteration is checked and if the
 * 					operator at the next i has more precedence than the operator (+) at the top
 * 					of the stack. Next operator has to be done first. 
 * 				3. Push * and evaluate the next number and trace back the operator Stack until
 * 					the stack becomes empty. return numberStack's last element.
 * 				4. If brackets are present, push "(" perform operation till the next ")". If ")" 
 * 					backtrack the stack until beginning of "(" is reached and put the number back to 
 * 					stack.
 * 				5. Finally, when all the bracketed items are flushed out. Perform the operation in 
 * 					the stack from right to left and finally return the result based on the 
 * 					precedence logic used above from right to left in the stack.
 * 
 * Sample Input : ( 2 + 3 . ( 5 ^ 2 . ( 6 + 6 . 9 / ( 3 + 3 + 3 ) + 4 . 5 ) . 2 + 3 ) . 5 + 2 . 3 )
 * 					Output : 24053.0
 * 
 * NOTE : I have used '.' instead of '*' for multiplication since * operator when read through command line
 * 		  is taking entire details of the code. For example .classpath etc which means * is understood in a 
 * 		  different way.
 */



import java.util.*;
import java.util.Hashtable;
import java.util.Vector; 
import java.util.Stack;	
import java.lang.Math;   

class Expression {	
	public static Stack<Double> numberStack = new Stack<Double>();				
    public static Stack<String> operatorStack = new Stack<String>();			
    public static Hashtable<String, Integer> operators = new Hashtable<String, Integer>();
    
    public static void assignValues () {							// Hash used to store precedence 
    	operators.put("+", 1);
    	operators.put("-", 2);
    	operators.put("%", 3);
    	operators.put("*", 4);
    	operators.put("/", 5);
    	operators.put("^", 6);
    	operators.put("(", 7);
    	operators.put(")", 8);
    }
    
    public static void main (String args []) {										//main program
    	assignValues();
    	Vector<String> aLine = new Vector<String>(); 							//Vector for reading inputs
    	for ( String arg: args ) 
            aLine.add(arg.equals(".") ? "*" : arg) ;							//'.' is used refer NOTE 
    	if ( aLine.size() > 0 )
            System.out.println(aLine + "\n" + " and the answer is: " + calculate(aLine) );	//calculate
    }

    public static double calculate(Vector<String> aLine) {
    	double temp = 0.0;
    	try {
    	for(int iterator = 0;iterator < aLine.size();iterator++) {
    		if(operators.containsKey(aLine.elementAt(iterator))) {     // If it is an operator
       					operatorStack.push(aLine.elementAt(iterator));
    					if(aLine.elementAt(iterator).equals(")")) {		// If the operator is ")"
    						operatorStack.pop();
    						if(operatorStack.peek().equals("(")) {		// In a case where ( 8 ) is given 
    																	// or result evaluated by (2 * 2 + 4)
								operatorStack.pop();
									if((iterator + 1) == aLine.size()) {	
										if(!operatorStack.isEmpty() && !numberStack.isEmpty()) {
											// Performing operation here 
											temp = operation(getRank(operatorStack.pop()),numberStack.pop(),numberStack.pop());
											numberStack.push(temp);
											int k = 0;
											while(!operatorStack.empty()) { // Until stack becomes empty evaluate based on precedence
																			//After reaching the end of the input
												temp = operation(getRank(operatorStack.elementAt(k)),
			    								numberStack.elementAt(k+1),numberStack.elementAt(k));
												numberStack.remove(k);
												numberStack.remove(k);
												operatorStack.remove(k);
												numberStack.add(k,temp);
											}
										}
									}
									else {									// If operator is not ")"
										if(!operatorStack.isEmpty() && !numberStack.isEmpty() && 
												!operatorStack.peek().equals("(") && !operatorStack.peek().equals(")")) {
											//Perform operation based on precedence
											if(getRank(operatorStack.peek()) <= getRank(aLine.elementAt(iterator+1)) 
													&& aLine.elementAt(iterator+1).equals(")")) {
												temp = operation(getRank(operatorStack.pop()),numberStack.pop(),numberStack.pop());
												numberStack.push(temp);
											}
										}
									}
    						}
    						else {								// If the top of the stack is not "("
    							while(!operatorStack.peek().equals("(")) {	// Perform operation until operator Stack contains "("
    								String s = new String();
        							double n;
        							// Following logic is used if "(" was immediate after the top
        							//	or if the stack contains 2 * 3 + 2 after evaluation, so that
        							// 	* should be done first and then +
    								if(!operatorStack.peek().equals("(")) { 
    									s = operatorStack.pop();
    									if(!operatorStack.peek().equals("(")) {
    										if(getRank(s) < getRank(operatorStack.peek())) {
    											n = numberStack.pop();
    											temp = operation(getRank(operatorStack.pop()),numberStack.pop(),numberStack.pop());
    	    									numberStack.push(temp);
    	    									operatorStack.push(s);
    	    									numberStack.push(n);
    										}
    										else {
    											temp = operation(getRank(s),numberStack.pop(),numberStack.pop());
    											numberStack.push(temp);
    										}
    									}
    									else {
    										temp = operation(getRank(s),numberStack.pop(),numberStack.pop());
    										numberStack.push(temp);
    									}
    								}
    							}
    							operatorStack.pop(); 
    							if(!operatorStack.empty() && !operatorStack.peek().equals("(") && !operatorStack.peek().equals(")")) {
    								temp = operation(getRank(operatorStack.pop()),numberStack.pop(),numberStack.pop());
									numberStack.push(temp);
    							}
    						}
    					}
    		}
    		else {			// If the input is a number
    			try{ 
    			numberStack.push(Double.valueOf(aLine.elementAt(iterator)));
    			}
    			catch(NumberFormatException nfe){									// is not a number throw an exception
   				 	System.out.println(aLine.elementAt(iterator));
   				 	System.out.println("inside number format exception");
    			}
    			if(!operatorStack.empty()){
    				if((iterator+1) == aLine.size()) {	// If end of the input is reached, evaluate based on precedence
    													// backtracking in the stack
    					if(!operatorStack.isEmpty() && !numberStack.isEmpty()) {
    						temp = operation(getRank(operatorStack.pop()),numberStack.pop(),numberStack.pop());
    						numberStack.push(temp);
    						int k = 0;
    						while(!operatorStack.empty()) {
    							if(!operatorStack.isEmpty() && !numberStack.isEmpty()) {
    								temp = operation(getRank(operatorStack.elementAt(k)),
    								numberStack.elementAt(k+1),numberStack.elementAt(k));
    							}
    							numberStack.remove(k);
    							numberStack.remove(k);
    							operatorStack.remove(k);
    							numberStack.add(k,temp);
    						
    						}
    					}
    				}
    				else {							// If the end is not reached
    					if(!operatorStack.peek().equals("(")) {			// Evaluate with top element if top is not (
    						if((getRank(operatorStack.peek()) >= getRank(aLine.elementAt(iterator+1))) 
    								|| aLine.elementAt(iterator+1).equals(")")) {
    							if(!operatorStack.isEmpty() && !numberStack.isEmpty()) {
    								temp = operation(getRank(operatorStack.pop()),numberStack.pop(),numberStack.pop());
    								numberStack.push(temp);   							
    							}
    						}
    					} 
    				}
    			}
    		}
    		
    	}
    	}
    	catch(ArrayIndexOutOfBoundsException ae) {
    		System.out.println("Invalid input expression, array out of bound");
    		System.exit(0);
    	}
    	catch (EmptyStackException ee) {
    		System.out.println("Empty stack coz two operators are adjacent (+ +) ");
    		System.exit(0);
    	}
    	catch (NullPointerException ne) {
    		System.out.println("Null pointer, you must have used +3 or -3 wwithout space " +
    				"between charcter and number");
    		System.exit(0);
    	}
    	catch (Exception e) {
    		System.out.println ("Other exception occured !! \n" + e);
    		System.exit(0);
    	}
		return numberStack.pop(); // Finally return the numberStack's last element.
    } // calculate1 ends
    
    public static int getRank (String op) {		//In hash (key,value) = key --> + value --> 1, returns value;
    	return(operators.get(op));
    }
       
    public static double operation (int rank, double num1, double num2) { // based on value perform opertaion
    	switch (rank) {
    	case 1:
    		return (num2 + num1);
    	case 2:
    		return (num2 - num1);
    	case 3:
    		return (num2 % num1);
    	case 4:
    		return (num2 * num1);
    	case 5:
    		if(num1 == 0) {
    			System.out.print("Trying to divide by 0, Program will exit shortly");
    			System.exit(0);
    		}
    		return (num2 / num1);	
    	case 6:
    		return  Math.pow(num2,num1);
    	}  	
    	return 0.0;
    }
} //class ends


