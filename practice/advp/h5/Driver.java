/**
 * Find performance evaluation for Tree Set 
 *
 *
 * @version   $Id: Driver.java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) && Nipun Sud 
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/27 12:58:00  kxk2907
 *      Initial revision
 *
 */

import java.util.*;

public class Driver {
    final int MAX_TYPE = 2;
    final static int MAX_ELEMENTS = 1000;
    Object allObjects[] = new Object[MAX_ELEMENTS];
    AnotherTreeSet aTreeSet = new TreeSetNew();
    long milliSeconds = 0;
    int objectKind = 0;
    Object first, middle, last;
   

    public Driver() {
    }

    public void init()  {
        milliSeconds = System.currentTimeMillis();
    }
    public void end()   {
        System.out.println("Time for all:       " + ( System.currentTimeMillis() - milliSeconds) );
    }

    public Object generateString()      {			// crearint string objects
        return new String( new Date().toString() );
    }
    public Object generateObject()      {		// creating objects 
        return new Object();
    }
    public Object generateMeanTreeSetObject()   {	// creating tree objects
        return new TreeSetNew();
    }

    public Object objectGenerator()     {
        if ( objectKind == 0 )
                return  generateObject();
        else if ( objectKind == 1 )
                return  generateString();
        else if ( objectKind == 2 )					// kind of testing 
                return generateMeanTreeSetObject();
        else    {
                System.out.println("unkown object type - rip");
                System.exit(1);
        }
        return null;
    }
    public void addTest()       {
        for ( int index = 0; index < MAX_ELEMENTS ; index ++ )  {
                        aTreeSet.add( allObjects[index] = objectGenerator() );
        }
    }
    public void containsTest()  {
        for ( int index = 0; index < MAX_ELEMENTS ; index ++ )  {
                        aTreeSet.contains( objectGenerator() );
        }
        for ( int index = 0; index < MAX_ELEMENTS; index ++ )   {
                        aTreeSet.contains( allObjects[index] );
        }
    }
    public void isEmptyTest()   {
        for ( int index = 0; index < MAX_ELEMENTS ; index ++ )  {
                        aTreeSet.isEmpty();
        }
    }
    public void sizeTest()      {
        for ( int index = 0; index < MAX_ELEMENTS ; index ++ )  {
                        aTreeSet.size();
        }
    }
    public void removeTest()    {
        for ( int index = 0; index < MAX_ELEMENTS ; index ++ )  {
                        aTreeSet.remove( objectGenerator() );
        }
        for ( int index = 0; index < MAX_ELEMENTS; index ++ )   {
                        aTreeSet.remove( allObjects[index] );
        }
    }
   
   
   public void testOneKind()    {
        long milliSeconds = System.currentTimeMillis();
                for ( int index = 0; index <= MAX_TYPE; index ++ )      {
                        objectKind = index;     
                        addTest();
                }
        System.out.println("\t" + objectKind + ":add:           " + ( System.currentTimeMillis() - milliSeconds) );
        milliSeconds = System.currentTimeMillis();
                containsTest();
        System.out.println("\t" + objectKind + ":contains:      " + ( System.currentTimeMillis() - milliSeconds) );
        milliSeconds = System.currentTimeMillis();
                isEmptyTest();
        System.out.println("\t" + objectKind + ":isEmpty:       " + ( System.currentTimeMillis() - milliSeconds) );
        milliSeconds = System.currentTimeMillis();
                sizeTest();
        System.out.println("\t" + objectKind + ":size:          " + ( System.currentTimeMillis() - milliSeconds) );
        milliSeconds = System.currentTimeMillis();
                removeTest();
        System.out.println("\t" + objectKind + ":remove:        " + ( System.currentTimeMillis() - milliSeconds) );
   }
   public void testIt() {
        for ( int index = 0; index <= MAX_TYPE; index ++ )      {
                objectKind = index;     
                long milliSeconds = System.currentTimeMillis();
                testOneKind();
                System.out.println(objectKind + ":all:          " + ( System.currentTimeMillis() - milliSeconds) );
        }
   }

   public static void main(String args[] )      {
	   System.out.println("Performance metrics for Tree Set : ");
        Driver aDriver = new Driver();
        aDriver.init();
        aDriver.testIt();
        aDriver.end();
        System.exit(0);
   }
}

