package debug;

/**
 * @author Karthikeyan Karur Balu
 * 
 * Lab - 4 Submission
 * Java Debugger 
 * 
 * The following aspectJ code advices all the methods in the 
 * sample test class code and act as a debugger tool
 */

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.CodeSignature;

/**
 * 
 * @author Karthikeyan Karur Balu
 * 
 *         AspectJ : AJDebug
 * 
 *         Usage / how to execute : java AJDebug <class name> <arguments>
 * 
 *         Methods advised : All the methods in TestClass
 * 
 */
public aspect AJDebug {

	static Class<?> classname; // Name of the class input
	static Method[] method; // Method names of class input
	// Hash map of all the method names
	static HashMap<String, Integer> methodHash = new HashMap<String, Integer>();
	static String breakPoint = ""; // Breakpoint captured
	static boolean setNext = false; // when user enters "next"
	static boolean setGo = false; // when user enters "go"
	static Scanner sc = null;

	/**
	 * Pointcut to capture all methods in the class given as input argument
	 */
	pointcut everything():
		call(* *.*(..)) && 
		!within(debug.AJDebug);

	/**
	 * Pointcut to capture to Main method in the class given as input argument
	 * 
	 * @param args
	 *            - input argument to the main method
	 */
	pointcut executeMain(String[] args):
		execution(* *.main(..)) &&
		!within(debug.AJDebug) &&
		args(args);

	/**
	 * Adviced before the main method - details of prompt under commandPrompt
	 * 
	 * @param args
	 *            - input argument to the main method
	 */
	before(String[] args): executeMain(args) {
		Signature sig = thisJoinPoint.getSignature();
		Object[] getArgs = thisJoinPoint.getArgs();
		commandPrompt(sig, getArgs);
	}

	/**
	 * Adviced on every method of the class - details of prompt under
	 * commandPrompt
	 */
	before(): everything() {
		Signature sig = thisJoinPoint.getSignature();
		Object[] getArgs = thisJoinPoint.getArgs();
		commandPrompt(sig, getArgs);
	}

	/**
	 * Method responsible for command prompt used in above advices
	 * 
	 * @param sig
	 *            - thisJoinPoint Signature that was captured earlier
	 * @param getArgs
	 *            - Arguments to any method captured using thisJoinPoint
	 */
	public static void commandPrompt(Signature sig, Object[] getArgs) {
		if ((methodHash.containsKey(sig.getName())) // execute only on valid
													// class methods and
				&& (breakPoint.equals(sig.getName()) || setNext == true)) {
			if (!setNext)
				breakPoint = sig.getName();
			boolean flag = true; // flag to control infinite command line
			while (flag) {
				if (setGo || setNext) {
					System.out.println("break at " + sig.toLongString());
					setGo = false;
					setNext = false;
				}
				System.out.print("> ");
				String[] array = input().split(" ");
				if (array[0].equals("break")) {
					Break(array);
				} else if (array[0].equals("clear")) {
					Clear();
				} else if (array[0].equals("go")) {
					setGo = true;
					flag = false;
				} else if (array[0].equals("next")) {
					setNext = true;
					flag = false;
				} else if (array[0].equals("quit")) {
					// sc.close();
					System.exit(0);
				} else if (array[0].equals("show")) {
					Show(array, sig, getArgs);
				} else {
					Usage(array[0]);
				}
			}
		} else {

		}
	}

	/**
	 * Wrong usage check - self explanatory
	 * 
	 * @param arg
	 *            - string entered
	 */
	public static void Usage(String arg) {
		System.out.println("UnknownCommand: " + "\"" + arg + "\"");
		System.out.println("Legal commands are");
		System.out
				.println("> break class-name method-name : Set the breakpoint.");
		System.out.println("> clear : Clear the breakpoint.");
		System.out.println("> go : Continue executing the program.");
		System.out
				.println("> next : Execute until the next method execution starts.");
		System.out
				.println("> quit : Quit the debugger. (This kills the program.)");
		System.out
				.println("> show paramater-name : Display the value of a method's parameter.");
		System.out.println();
	}

	/**
	 * When break command is executed
	 * 
	 * @param array
	 *            - input command eg : break TestClass imethod1
	 */
	public static void Break(String[] array) {
		if (array.length == 3) {
			if (array[1].equals(classname.getName())) {
				for (int i = 0; i < method.length; i++) {
					if (method[i].getName().equals(array[2])) {
						breakPoint = array[2];
						return;
					}
				}
				System.out.println("Invalid breakpoint !!");
			} else {
				System.out.println("ERROR : Invalid class name !! ");
			}
		} else {
			System.out
					.println("Invalid usage : Correct usage : break class-name method-name ");
		}
	}

	/**
	 * Clear any break points
	 */
	public static void Clear() {
		breakPoint = "";
	}

	/**
	 * Show parameters in a particular method
	 * 
	 * @param array
	 *            - input command excution : eg : show i
	 * @param sig
	 *            - signature captured from the thisJoinPoint
	 * @param values
	 *            - thisJoinPoint args captured and sent here
	 */
	public static void Show(String[] array, Signature sig, Object[] values) {
		String[] paramNames = ((CodeSignature) sig).getParameterNames();
		if (array.length == 2) {
			for (int i = 0; i < paramNames.length; i++) {
				if (array[1].equals(paramNames[i])) {
					System.out.println(array[1] + " = " + values[i]);
					return;
				}
			}
			System.out.println("No parameter of " + classname + "."
					+ sig.getName() + " named " + "\"" + array[1] + "\".");
		} else {
			System.out
					.println("Invalid Usage : correct usage : show paramater-name ");
		}
	}

	/**
	 * Creating a static Scanner : wait until the scanner has any line
	 * @return - String that is read()
	 */
	public static String input() {
		String local = null;
		while (!sc.hasNextLine());
		local = sc.nextLine();
		return local;
	}

	/**
	 * Main class of AspectJ : AJDebug
	 * 
	 * @param args
	 *            - input command line arguments passed
	 * @throws ClassNotFoundException
	 *             - throws if args[1] is not present
	 */
	public static void main(String[] args) throws ClassNotFoundException {
		if (args.length < 1) {
			System.out.println("Usage : java AJDebug <class name> <arguments>");
			System.exit(0);
		} else {
			try {
				classname = Class.forName(args[0]);
				method = classname.getDeclaredMethods();
				for (int i = 0; i < method.length; i++)
					methodHash.put(method[i].getName(), i);
				Class<?>[] argTypes = new Class[] { String[].class };
				Method main = classname.getDeclaredMethod("main", argTypes);
				boolean flag = true;
				String[] mainArgs = Arrays.copyOfRange(args, 1, args.length);
				sc = new Scanner(System.in);
				while (flag) {
					System.out.print("> ");
					String[] array = input().split(" ");
					if (array[0].equals("break")) {
						Break(array);
					} else if (array[0].equals("clear")) {
						Clear();
					} else if (array[0].equals("go")) {
						setGo = true;
						flag = false;
					} else if (array[0].equals("next")) {
						setNext = true;
						flag = false;
					} else if (array[0].equals("quit")) {
						System.exit(0);
					} else if (array[0].equals("show")) {
						// Show(array);
					} else {
						Usage(array[0]);
					}
				}
				main.invoke(null, (Object) mainArgs);
				System.out.println("Normal program termination.");
			} catch (ClassNotFoundException x) {
				x.printStackTrace();
			} catch (IllegalAccessException x) {
				x.printStackTrace();
			} catch (InvocationTargetException x) {
				x.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
	}
}
