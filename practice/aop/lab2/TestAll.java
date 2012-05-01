/*
 * TestAll.java
 * 
 * $Id: TestAll.java,v 1.2 2010/12/19 19:36:33 jeh Exp $
 *
 */

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.Writer;
import java.io.FileWriter;
import java.io.OutputStreamWriter;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Random;

/**
 * A file copying program.
 *
 * @author Matthew Fluet (mtf)
 * @author James Heliotis (jeh)
 */
public class TestAll {

    private static void usage( String[] args ) {
        if ( args.length < 1 || args.length > 3 ) {
            System.err.println(
                "Usage: java TestAll method# [<input-file> [<output-file>]]");
            System.exit(1);
        }
        try {
            Integer.parseInt( args[ 0 ] );
        }
        catch ( NumberFormatException e ) {
            System.err.println(
                "Usage: java TestAll method# [<input-file> [<output-file>]]");
            System.exit(1);
        }
    }

    /**
     * Copy a file from input to output.
     */
    public static void main(String[] args) {
        usage( args );
        int method = Integer.parseInt( args[ 0 ] );
        Reader rd = null;
        Writer wr = null;
        try {
            if ( args.length >= 2 ) {
                rd = new BufferedReader(new FileReader(new File(args[1])));
            } else {
                rd = new InputStreamReader(System.in);
            }
            if ( args.length >= 3 ) {
                wr = new FileWriter(new File(args[2]));
            } else {
                wr = new OutputStreamWriter(System.out);
            }
            if ( !rd.markSupported() && method == 8 ) {
                System.err.println("Mark not supported for this Reader.");
                method = 2;
            }
            Random rnd = new Random(42);
            boolean eof = false;
            do {
                // Choose reader method based on command line arg.
                switch ( method ) {
                case 0: { // read 1, write 1
                        int c = rd.read();
                        if (c == -1) {
                            eof = true;
                        }
                        else {
                            wr.write(c);
                        }
                        break;
                    }
                case 1: { // read 1, append 1
                        int c = rd.read();
                        if (c == -1) {
                            eof = true;
                        }
                        else {
                            wr.append((char)c);
                        }
                        break;
                    }
                case 2: { // read array, write array
                    char[] cbuf = new char[rnd.nextInt(1024)];
                    int rlen = rd.read(cbuf);
                    if (rlen == -1) {
                        eof = true;
                    }
                    else {
                        cbuf = Arrays.copyOfRange(cbuf, 0, rlen);
                        char[] dcbuf = Arrays.copyOf(cbuf, cbuf.length);
                        wr.write(cbuf);
                        if (! Arrays.equals(cbuf, dcbuf)) {
                            throw new AssertionError(
                                "wr.write(cbuf) modified cbuf");
                        }
                    }
                    break;
                }
                case 3: { // read partial array, write partial array
                        char[] cbuf = new char[rnd.nextInt(1024)];
                        int off = rnd.nextInt(cbuf.length);
                        int len = rnd.nextInt(cbuf.length - off);
                        int rlen = rd.read(cbuf, off, len);
                        if (rlen == -1) {
                            eof = true; 
                        }
                        else {
                            char[] dcbuf = Arrays.copyOf(cbuf, cbuf.length);
                            wr.write(cbuf, off, rlen);
                            if (! Arrays.equals(cbuf, dcbuf)) {
                                throw new AssertionError(
                                    "wr.write(cbuf, off, rlen) modified cbuf");
                            }
                        }
                        break;
                    }
                case 4: { // read CharBuffer, write CharBuffer
                        CharBuffer target =
                            CharBuffer.allocate(rnd.nextInt(1024));
                        int rlen = rd.read(target);
                        if (rlen == -1) {
                            eof = true;
                        }
                        else {
                            target.rewind();
                            CharBuffer dtarget = CharBuffer.allocate(rlen);
                            dtarget.append(target, 0, rlen);
                            dtarget.rewind();
                            wr.append(dtarget);
                        }
                        break;
                    }
                case 5: { // read CharBuffer, write partial CharBuffers
                        CharBuffer target =
                            CharBuffer.allocate(rnd.nextInt(1024));
                        int rlen = rd.read(target);
                        if (rlen == -1) {
                            eof = true;
                        }
                        else {
                            target.rewind();
                            int len1 = rlen / 3;
                            int len2 = len1;
                            int len3 = rlen - ( len1 + len2 );
                            if ( len1 > 0 ) {
                                CharBuffer dtarget = CharBuffer.allocate(rlen);
                                dtarget.append(target, 0, len1);
                                dtarget.rewind();
                                wr.append(dtarget,0,len1);
                            }
                            if ( len2 > 0 ) {
                                CharBuffer dtarget = CharBuffer.allocate(rlen);
                                for ( int i=0; i < len1; ++i ) dtarget.put('X');
                                dtarget.append(target, len1, len1+len2);
                                dtarget.rewind();
                                wr.append(dtarget,len1,len1+len2);
                            }
                            if ( len3 > 0 ) {
                                CharBuffer dtarget = CharBuffer.allocate(rlen);
                                for ( int i=0; i < len1+len2; ++i ) {
                                    dtarget.put('Q');
                                }
                                dtarget.append(target, len1+len2, rlen);
                                dtarget.rewind();
                                wr.append(dtarget,len1+len2,rlen);
                            }
                        }
                        break;
                    }
                case 6: { // read array, write String
                        char[] cbuf = new char[rnd.nextInt(1024)];
                        int rlen = rd.read(cbuf);
                        if (rlen == -1) {
                            eof = true;
                        }
                        else {
                            String sbuf = new String(cbuf, 0, rlen);
                            wr.write(sbuf);
                        }
                        break;
                    }
                case 7: { // read partial array, write partial String
                        char[] cbuf = new char[rnd.nextInt(1024)];
                        int off = rnd.nextInt(cbuf.length);
                        int len = rnd.nextInt(cbuf.length - off);
                        int rlen = rd.read(cbuf, off, len);
                        if (rlen == -1) {
                            eof = true; 
                        }
                        else {
                            String sbuf = new String(cbuf);
                            wr.write(sbuf, off, rlen);
                        }
                        break;
                    }
                case 8: { // mark read array, reset, read array, write array
                        int numChars = rnd.nextInt(1024);
                        char[] cbuf = new char[numChars];
                        rd.mark(1024);
                        int rlen = rd.read(cbuf);
                        if (rlen == -1) {
                            eof = true;
                        }
                        else {
                            rd.reset();
                            char[] cbuf2 = new char[numChars];
                            int rlen2 = rd.read(cbuf2);
                            // Compare duplicate reads
                            for ( int i=0; i < Math.min(rlen,rlen2); ++i ) {
                                if (cbuf[i]!=cbuf2[i]) {
                                    throw new AssertionError(
                                        "Problem with mark/reset");
                                }
                            }
                            cbuf2 = Arrays.copyOfRange(cbuf2, 0, rlen2);
                            char[] dcbuf = Arrays.copyOf(cbuf2, rlen2);
                            wr.write(cbuf2);
                            if (! Arrays.equals(cbuf2, dcbuf)) {
                                throw new AssertionError(
                                    "wr.write(cbuf2) modified cbuf");
                            }
                        }
                        break;
                    }
                }
            } while (! eof);
        }
        catch (FileNotFoundException e) {
            System.err.println(e);
        }
        catch (IOException e) {
            System.err.println(e);
        }
        finally {
            try {
                if (rd != null) { 
                    rd.close(); 
                }
            }
            catch (IOException e) {
                System.err.println(e);
            }
            try {
                if (wr != null) { 
                    wr.close(); 
                }
            }
            catch (IOException e) {
                System.err.println(e);
            }
        }
    }


}


