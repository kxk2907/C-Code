/*
 * VigenereCipherExtra.java
 *
 * Version:
 *  $Id$
 * Revisions:
 *  $Log$
 *
 */

import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Reader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.Writer;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.BufferedWriter;
import java.io.CharArrayWriter;

import java.nio.CharBuffer;
import java.util.Arrays;
import java.util.Random;

/**
 * A Vigenere cipher program.
 *
 * @author Matthew Fluet (mtf)
 */
public class VigenereCipherExtra {

    private static void usage() {
        System.err.println("Usage: java VigenereCipherExtra encode|decode <keyword> [<input-file> [<output-file>]]");
        System.exit(1);
    }

    private enum Command { Encode, Decode };

    /**
     * The main method of a program to encode or decode using a Vigenere cipher.
     * 
     * <pre>
     * Usage: java VigenereCipherExtra encode|decode &lt;keyword&gt; [&lt;input-file&gt; [&lt;output-file&gt;]]
     * </pre>
     *
     * @param args The command line arguments
     * <ul>
     * <li> {@code encode|decode}: Command indicating whether the input should be encoded or decoded to the output.
     * <li> {@code keyword}: The Vigenere cipher keyword.  Although the {@code keyword} may include both lowercase and uppercase Roman alphabetic characters, the keyword is used in a case-insensitive manner.  That is, both the character {@code 'c'} and the character {@code 'C'} in the keyword induce a rotation of 3.
     * <li> {@code input-file} (optional): The input file (uses {@link System#in System.in} if omitted).
     * <li> {@code output-file} (optional): The output file (uses {@link System#out System.out} if omitted).
     * </ul>
     */
    public static void main(String[] args) {
        if ( args.length < 2 || args.length > 4 ) {
            usage();
        }
        Command cmd = null;
        if ( args[0].equals("encode") ) {
            cmd = Command.Encode;
        } else if ( args[0].equals("decode") ) {
            cmd = Command.Decode;
        } else {
            usage();
        }
        String keyword = args[1];
        Reader rd = null;
        Writer wr = null;
        try {
            if ( args.length >= 3 ) {
                rd = new FileReader(new File(args[2]));
            } else {
                rd = new InputStreamReader(System.in);
            }
            if ( args.length >= 4 ) {
                wr = new FileWriter(new File(args[3]));
            } else {
                wr = new OutputStreamWriter(System.out);
            }
            BufferedReader brd = new BufferedReader(rd,8192);
            rd = brd;
            wr = new BufferedWriter(wr,8192);
            switch (cmd) {
            case Encode:
                wr = new VigenereCipherWriter(wr, keyword);
                break;
            case Decode:
                rd = new VigenereCipherReader(rd, keyword);
                break;
            }
            Writer owr = wr;
            CharArrayWriter caw = null;
            Random rnd = new Random(42);
            boolean eof = false;
            boolean marked = false;
            int readAheadRemaining = 0;
            do {
                // Randomly choose reader/writer methods.
                switch (rnd.nextInt(7)) {
                case 0: {
                    int c = rd.read();
                    if (c == -1) {
                        eof = true;
                    } else {
                        if (marked) { 
                            readAheadRemaining -= 1; 
                        }
                        wr.write(c);
                    }
                    break;
                }
                case 1: {
                    int c = rd.read();
                    if (c == -1) {
                        eof = true;
                    } else {
                        if (marked) { 
                            readAheadRemaining -= 1; 
                        } 
                        wr.append((char)c);
                    }
                    break;
                }
                case 2: {
                    char[] cbuf = new char[rnd.nextInt(1024)];
                    int rlen = rd.read(cbuf);
                    if (rlen == -1) {
                        eof = true;
                    } else {
                        if (marked) { 
                            readAheadRemaining -= rlen; 
                        }
                        cbuf = Arrays.copyOfRange(cbuf, 0, rlen);
                        char[] dcbuf = Arrays.copyOf(cbuf, cbuf.length);
                        wr.write(cbuf);
                        if (! Arrays.equals(cbuf, dcbuf)) {
                            throw new AssertionError("wr.write(cbuf) modified cbuf");
                        }
                    }
                    break;
                }
                case 3: {
                    char[] cbuf = new char[rnd.nextInt(1024)];
                    int off = rnd.nextInt(cbuf.length);
                    int len = rnd.nextInt(cbuf.length - off);
                    int rlen = rd.read(cbuf, off, len);
                    if (rlen == -1) {
                        eof = true; 
                   } else {
                        if (marked) { 
                            readAheadRemaining -= rlen; 
                        }
                        char[] dcbuf = Arrays.copyOf(cbuf, cbuf.length);
                        wr.write(cbuf, off, rlen);
                        if (! Arrays.equals(cbuf, dcbuf)) {
                            throw new AssertionError("wr.write(cbuf, off, rlen) modified cbuf");
                        }
                    }
                    break;
                }
                case 4: {
                    CharBuffer target = CharBuffer.allocate(rnd.nextInt(1024));
                    CharBuffer dtarget = target.duplicate();
                    int rlen = rd.read(target);
                    if (rlen == -1) {
                        eof = true;
                    } else {
                        if (marked) { 
                            readAheadRemaining -= rlen; 
                        }
                        wr.append(dtarget,0,rlen);
                    }
                    break;
                }
                case 5: {
                    if (brd.markSupported() != rd.markSupported()) {
                        throw new AssertionError("rd.markSupported() disagrees with underlying reader.");
                    }
                    if (rd.markSupported() && ! marked) {
                        rd.mark(4096);
                        marked = true;
                        readAheadRemaining = 4096;
                        caw = new CharArrayWriter();
                        wr = caw;
                    }
                    break;
                }
                case 6: {
                    if (brd.ready() != rd.ready()) {
                        throw new AssertionError("rd.ready() disagrees with underlying reader.");
                    }
                }
                }
                if (marked && (eof || readAheadRemaining <= 1024)) {
                    caw.close();
                    char[] cbuf1 = caw.toCharArray();
                    caw = null;
                    char[] cbuf3 = Arrays.copyOfRange(cbuf1, cbuf1.length / 3, 2 * (cbuf1.length / 3));

                    rd.reset();
                    char[] cbuf2 = new char[cbuf1.length];
                    int rlen2 = rd.read(cbuf2);
                    if ((rlen2 != cbuf1.length) || (! Arrays.equals(cbuf1, cbuf2))) {
                        throw new AssertionError("rd.read(char[]) after rd.reset() after rd.mark(int) did not return identical data.");
                    }

                    rd.reset();
                    rd.skip(cbuf1.length / 3);
                    char[] cbuf4 = new char[cbuf3.length];
                    int rlen4 = rd.read(cbuf4);
                    if ((rlen4 != cbuf3.length) || (! Arrays.equals(cbuf3, cbuf4))) {
                        throw new AssertionError("rd.read(char[]) after rd.skip(int) after rd.reset() after rd.mark(int) did not return identical data.");
                    }

                    rd.reset();
                    marked = false;
                    eof = false;
                    wr = owr;
                }
            } while (! eof);
        } catch (FileNotFoundException e) {
            System.err.println(e);
        } catch (IOException e) {
            System.err.println(e);
        } finally {
            try {
                if (rd != null) { 
                    rd.close(); 
                }
            } catch (IOException e) {
                System.err.println(e);
            }
            try {
                if (wr != null) { 
                    wr.close(); 
                }
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }


}

