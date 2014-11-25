import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;


public class Client {
	private static final int DEFAULT_PORT = 3333;
	private static final int SERVER_SOCKET_TIMEOUT = 20000;
	
	
	public static void main(String... strings){
		String targetIP = "localhost";
		int targetPort = DEFAULT_PORT;
		
		if(strings != null){
			if(strings.length >= 2 && strings[1] != null ){
				targetIP = strings[1];
			}
			if( strings.length >= 3 && strings[2] != null){
				try{
					targetPort = Integer.parseInt(strings[1]);
				}catch(NumberFormatException e){
					System.out.println("Second parameter needs to be the port number.");
					System.exit(0);
				}
			}
		}
		
		try {
			Socket socket = new Socket(targetIP, targetPort);
			socket.setSoTimeout(SERVER_SOCKET_TIMEOUT);
			OutputStream oS = socket.getOutputStream();
			ObjectOutputStream oOut = new ObjectOutputStream(oS);
			if(strings.length >= 1 && strings[0] != null) oOut.writeUTF(strings[0]);
			else oOut.writeUTF("No string provided...");
			oOut.flush();
			
			oOut.close();
			oS.close();
			socket.close();
			
		} catch (UnknownHostException e) {

			e.printStackTrace();
		} catch (IOException e) {

			e.printStackTrace();
		}
		
		
		
	}
}
