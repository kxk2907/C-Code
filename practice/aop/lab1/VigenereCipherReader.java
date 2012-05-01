/*
 * VigenereCipherReader.java
 *
 * Author : Karthikeyan Karur Balu
 * Version: 1.1 
 * Date : 12/13/2010 10:23 PM
 * 
 *
 */
import java.io.IOException;
import java.io.Reader;

/**
 * ViginereCipherReader Code for Decoding the cipher text based on VigenereCipher.java
 * 
 * @author Karthikeyan Karur Balu
 * 
 * @version 1.1
 * 
 * {@value} Writer wr
 * 			String keywork // Cipher text 
 */
public class VigenereCipherReader extends Reader {
	Reader rd;											//Reader
	String keyword;										//Cipher text
	static int counter = 0;
	static int marker = 0;
	public VigenereCipherReader(Reader rd, String keyword) 
		throws IllegalArgumentException {
		this.rd = rd;
		this.keyword = keyword;
	}
	
	@Override
	public void close() throws IOException {
		rd.close();
	}

	@Override 
	public void mark(int readAheadLimit) throws IOException {		//Setting the marker
		marker = readAheadLimit;
		rd.mark(marker);
	}
	
	@Override 
	public boolean markSupported() {
		return rd.markSupported();
	}
	
	@Override
	public boolean ready() throws IOException {
		return rd.ready();
	}
	
	@Override 
	public void reset() throws IOException {					//Reset the marker to the initial position
		rd.reset();
	}
	
	@Override 
	public long skip(long n) throws IOException {
		return rd.skip(n);
	}

	@Override
	public int read(char[] cbuf, int off, int len) 
			throws IOException {
		String str = keyword.toUpperCase();						//Converting the cipher to uppercase to maintain case throughout for
		int i = off;											//decoding and encoding
		int read1 = rd.read(cbuf, off, len);
		for (i = off; i < len; i++, counter++) {
			int read = cbuf[i];
			if (read == -1)
				return -1;
			else if ((read >= 65 && read <= 90) || 
						read >= 97 && read <= 122) {			//Checking the range of the ASCII value
				int buf = read;
				if (counter >= keyword.length())
					counter = counter % keyword.length();
				char key = str.charAt(counter);					//Below boolean to retain the state of cipher text, 
				boolean uporlowvalue = false;					//upper case - true / low case - false
				if (buf >= 65 && buf <= 90)						//Key is use to walk through the key string
					uporlowvalue = true;
				else if (buf >= 97 && buf <= 122) {
					buf = buf - 32;
					uporlowvalue = false;
				}
				buf = buf - key;								//Decoding done here
				while (buf < 0)
					buf = buf + 26;
				buf = buf % 26;
				buf = buf + 65;
				if (!uporlowvalue)
					buf = buf + 32;
				cbuf[i] = (char) buf;
			} else {
				cbuf[i] = (char) read;
				if (counter >= keyword.length())				//Rotating the buffer and retaining the counter
					counter = counter % keyword.length();
			}
		}
		return read1;
	}
}
