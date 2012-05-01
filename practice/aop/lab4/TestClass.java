
/**
 * $Id: TestClass.java,v 1.3 2011/01/29 22:28:44 jeh Exp $
 */

/**
 * Set up a few method calls in a simple test program to test a debugger.
 * 
 * @author James Heliotis
 */
public class TestClass {

    /**
     * Call a sequence of three methods, one static, and another one
     * with arguments, twice.
     * @param args The command line arguments are simply displayed.
     */
    public static void main(String[] args) {
        System.out.print( "TestClass command line arguments:" );
        for ( String s: args ) {
            System.out.print( ' ' + s );
        }
        System.out.println();
        
        TestClass tc = new TestClass();

        for (int i = 0; i < 2; i++) {
            smethod();
            tc.imethod1( i, "hello", new char[] { 'a', 'b', 'c' } );
            tc.imethod2();
        }
    }

    /*
     * The static method
     */
    public static void smethod() {
        System.out.println( "smethod" );
    }

    /*
     * The instance method with arguments
     * @param i a primitive value (non-object)
     * @param s an object
     * @param char_arry an array object
     */
    public void imethod1( int i, String s, char[] char_array ) {
        System.out.println( "imethod1" );
    }

    /*
     * The instance method without arguments
     */
    public void imethod2() {
        System.out.println( "imethod2" );
    }

}

