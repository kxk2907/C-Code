import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

class TCPServer {
	public static void main(String args[]) throws Exception {
		String clientSentence, capitalizedSent;
		ServerSocket welcomesocket = new ServerSocket(4444);
		while(true) {
			Socket connectionSocket = welcomesocket.accept();
			BufferedReader fromClient = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream toClient = new DataOutputStream(connectionSocket.getOutputStream());
			clientSentence = fromClient.readLine();
			capitalizedSent = clientSentence.toUpperCase() + '\n';
			toClient.writeBytes(capitalizedSent);
		}
	}
}
