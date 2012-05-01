/**
 * Find performance evaluation for Hash Map
 *
 *
 * @version   $Id: Driver1java,v 1.1 2010/09/27 12:58:00 kxk2907 Exp kxk2907 $
 *
 * @author    kxk2907 (Karthikeyan Karur Balu) && Nipun Sud 
 *
 * Revisions:
 *
 *      Revision 1.1  2010/09/27 12:58:00  kxk2907
 *      Initial revision
 *
 */

import java.util.Date;

public class Driver1 {
    final int MAX_TYPE = 2;
    // final int MAX_ELEMENTS = 1000000;
    final static int MAX_ELEMENTS = 1000;
    Object allObjects[] = new Object[MAX_ELEMENTS];
    AnotherHashMap aTreeSet = new HashMapNew();
    long milliSeconds = 0;
    int objectKind = 0;
    Object first, middle, last;
   

    public Driver1() {
    }

    public void init()  {
        milliSeconds = System.currentTimeMillis();
    }
    public void end()   {
        System.out.println("Time for all:       " + ( System.currentTimeMillis() - milliSeconds) );
    }

    public Object generateString()      {
        return new String( new Date().toString() );
    }
    public Object generateObject()      {
        return new Object();
    }
    public Object generateMeanTreeSetObject()   {
        return new HashMapNew();
    }

    public Object objectGenerator()     {
        if ( objectKind == 0 )
                return  generateObject();
        else if ( objectKind == 1 )
                return  generateString();
        else if ( objectKind == 2 )
                return generateMeanTreeSetObject();
        else    {
                System.out.println("unkown object type - rip");
                System.exit(1);
        }
        return null;
    }
    public void addTest()       {
        for ( int index = 0; index < MAX_ELEMENTS ; index ++ )  {
                        aTreeSet.put( (String)(allObjects[index] = objectGenerator()),(Object)index );
        }
    }
    public void containsTest()  {
        for ( int index = 0; index < MAX_ELEMENTS ; index ++ )  {
                        aTreeSet.containsKey( (String)objectGenerator() );
                        aTreeSet.containsValue( (Object)index );
        }
        for ( int index = 0; index < MAX_ELEMENTS; index ++ )   {
                        aTreeSet.containsKey( (String)allObjects[index] );
                        aTreeSet.containsValue( (Object)index );
        }
    }
    public void sizeTest()      {
        for ( int index = 0; index < MAX_ELEMENTS ; index ++ )  {
                        aTreeSet.size();
        }
    }
    public void removeTest()    {
        for ( int index = 0; index < MAX_ELEMENTS ; index ++ )  {
             			aTreeSet.remove((String)objectGenerator());
        }
        for ( int index = 0; index < MAX_ELEMENTS; index ++ )   {
                       aTreeSet.remove( (String)allObjects[index] );
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
	   System.out.println("Performance metrics for Hash Map : ");
        Driver aDriver = new Driver();
        aDriver.init();
        aDriver.testIt();
        aDriver.end();
        System.exit(0);
   }
}

