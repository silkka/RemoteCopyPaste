import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private static final int DEFAULT_PORT = 3333;
	private static final int SERVER_SOCKET_TIMEOUT = 20000;
	public static void main(String... strings){
		int port = DEFAULT_PORT;
		if(strings.length != 0 && strings[0] != null ) 
			try{
				port = Integer.parseInt(strings[0]);
			}catch (NumberFormatException e){
				System.out.println("Port should be a number");
			}
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			serverSocket.setSoTimeout(SERVER_SOCKET_TIMEOUT);
			System.out.println("Waiting for client...");
			Socket clientSocket = serverSocket.accept();
			System.out.println("Connected.");
			new ServerHandler(clientSocket).start();
			serverSocket.close();
			
			
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
	
	
}

class ServerHandler extends Thread{
	private static final int SOCKET_TIMEOUT = 5000;
	private Socket clientSocket;
	private InputStream clientInputStream;
	private ObjectInputStream clientObjectInputStream;
	
	public ServerHandler(Socket clientSocket) {
		this.clientSocket = clientSocket;
		
	}
	@Override
	public void run(){
		try {
			clientSocket.setSoTimeout(SOCKET_TIMEOUT);
			setClientInputStream(clientSocket.getInputStream());
			setClientObjectInputStream(new ObjectInputStream(getClientInputStream()));
			
			String received = getClientObjectInputStream().readUTF();
			
			StringSelection stringSelection = new StringSelection (received);

			Clipboard clpbrd = Toolkit.getDefaultToolkit ().getSystemClipboard ();
			clpbrd.setContents(stringSelection, null);
			System.out.println("\"" + received + "\" was copied to clipboard. Exiting");
			
			getClientObjectInputStream().close();
			getClientInputStream().close();
			clientSocket.close();
			
			
		} catch (Exception e) {
			System.exit(0);
		}

	}


	public Socket getClientSocket() {
		return clientSocket;
	}
	public void setClientSocket(Socket clientSocket) {
		this.clientSocket = clientSocket;
	}
	public InputStream getClientInputStream() {
		return clientInputStream;
	}
	public void setClientInputStream(InputStream clientInputStream) {
		this.clientInputStream = clientInputStream;
	}
	public ObjectInputStream getClientObjectInputStream() {
		return clientObjectInputStream;
	}
	public void setClientObjectInputStream(ObjectInputStream clientObjectInputStream) {
		this.clientObjectInputStream = clientObjectInputStream;
	}
	
	
	
}