/*
 * VigenereCipherWriter.java
 *
 * Author : Karthikeyan Karur Balu
 * Version: 1.1 
 * Date : 12/13/2010 10:23 PM
 * 
 *
 */
import java.io.IOException;
import java.io.Writer;

/**
 * ViginereCipherWriter Code for Encoding the cipher text based on VigenereCipher.java
 * 
 * @author Karthikeyan Karur Balu
 * 
 * @version 1.1
 * 
 * {@value} Writer wr
 * 			String keywork // Cipher text 
 */
public class VigenereCipherWriter extends Writer{
	Writer wr;											//Writer 
	String keyword;										//Cipher text
	static int counter = 0;
	public VigenereCipherWriter(Writer wr,String keyword) 
		throws IllegalArgumentException{
			this.wr = wr;
			this.keyword = keyword;
	}

	@Override
	public void close() throws IOException {
		wr.close();
	}

	@Override
	public void flush() throws IOException {
		wr.flush();
	}

	@Override
	public void write(char[] arg0, int arg1, int arg2) throws IOException {
		String str = keyword.toUpperCase();					//Converting the cipher to uppercase to maintain case throughout for
		char[] done  = new char[arg0.length];				//decoding and encoding
		for(int i = arg1;i<arg0.length;i++) { 
			int temp = arg0[i];
			if((temp >= 65 && temp <= 90) || 				//Checking the range of the ASCII value
					(temp >= 97 && temp <= 122)) {  
				char ch = arg0[i];							//Below boolean to retain the state of cipher text, 
				boolean uporlowvalue = false;				//upper case - true / low case - false
				int key = counter  % str.length();			//walk through the key
				if(temp >= 65 && temp <= 90)  
					uporlowvalue = true;
				else if(temp >= 97 && temp <= 122) {
					ch = (char) (ch - 32);
					uporlowvalue = false;
				}
				char ch2 = str.charAt(key);
				int local = (ch - 65) + (ch2 - 65);
				local = local % 26;							//Encoding done here 
				if(uporlowvalue)
					local = local + 65;
				else 
					local = local + 97;
				done[i] = (char)local;
			}
			else 
				done[i] = arg0[i];
			counter ++;
			if(temp == 13) {
				counter --;
			}
		}
		wr.write(done, arg1, arg2);							//Calling Writer wr
	}
	
	@Override												
	public void write(int ci) throws IOException {			//Exact replica of the above one without any for loop
		String str = keyword.toUpperCase();
		int key = counter % str.length();
		if((ci >= 65 && ci <= 90) || (ci >= 97 && ci <= 122)) {  
			char ch = (char)ci;
			boolean uporlowvalue = false;		//up - true / low - false
			if(ci >= 65 && ci <= 90)  
				uporlowvalue = true;
			else if(ci >= 97 && ci <= 122) {
				ch = (char) (ch - 32);
				uporlowvalue = false;
			}
			char ch2 = str.charAt(key);
			int local = (ch - 65) + (ch2 - 65);
			local = local % 26;
			if(uporlowvalue)
				local = local + 65;
			else 
				local = local + 97;
			ci = (char)local;
		}
		counter ++;
		if(ci == 13) {
			counter --;
		}
		wr.write(ci);
	}
}
