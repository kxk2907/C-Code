/**
 * Modified Hello, World program that prints the user name of the person who executes. 
 *
 *
 * @version   $Id: Hello.java,v 1.1 2010/09/12 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu)
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/12 12:58:00  kxk2907
 *      Initial revision
 *
 */

import java.util.*;   // Import required for Properties class. 
import java.lang.System; //Import required for System properties.

/**
 * This program prints the name of the person who executes it.
 * It uses System property "user.name" to get the name
 * Tested with : windows + SUNOS
 */

//Class Hello Starts 
class Hello {
    public static void main (String args []) {  	// main program
    	Properties p1 = System.getProperties();  	//Get the System properties and create a Properties instance to it
        System.out.println("Hello " + p1.getProperty("user.name")+ " !!"); //print the user.name property "here kxk2907"
        System.out.println();
        
        
       // Suppose to get all the properties listed use the following code snippet 
       // Got it from :  http://exampledepot.com/egs/java.lang/GetAllSysProps.html
       
           /*Enumeration e1 = p1.propertyNames();
        	for (; e1.hasMoreElements(); ) {
             String propName = (String)e1.nextElement();
             String propValue = (String)p1.get(propName);
             System.out.println(propName + propValue);
        	}*/
    }
}
//Class Hello Ends
