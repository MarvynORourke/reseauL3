package projet;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class mainClient {
		
		public static void main(String[] args) {
			
			Socket socket;
			PrintWriter out;
			BufferedReader input;
			File response;

			try {
			
			     socket = new Socket(InetAddress.getLocalHost(),4444);
			     out = new PrintWriter(socket.getOutputStream(),true);
			     input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			     out.write(args[1]);
			     out.flush();
			     System.out.println("Numéro étudiant envoyé au serveur");
			     
			     input.readLine();
		         socket.close();

			}catch (UnknownHostException e) {
				
				System.out.println("Destination unknow");
				System.exit(-1);
			}catch (IOException e) {
				System.out.println("now to investigate this IO issue");
				System.exit(-1);
			}

	}


}
