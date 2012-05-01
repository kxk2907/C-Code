
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class TCPClient {
	public static void main(String args[]) throws IOException {
	 while(true) {	
		String sentence, modifiedSent;
		BufferedReader b1 = new BufferedReader(new InputStreamReader(System.in));
		Socket clientSocket = new Socket("joplin.cs.rit.edu",4444);
		DataOutputStream outToServer = new DataOutputStream(clientSocket.getOutputStream());
	 	BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));	
		sentence = b1.readLine();	
		outToServer.writeBytes(sentence + '\n');
		modifiedSent = inFromServer.readLine();
		System.out.println("Output : " + modifiedSent);
		clientSocket.close();	
 	 }	
	}
}
