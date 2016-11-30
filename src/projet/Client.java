package projet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	private Socket socket;
	private PrintWriter out;
	private BufferedReader input;
	private String ip;
	private int port;
	private int numeroEtud;

	public Client(String ip, int port, int numeroEtud){
		this.ip=ip;
		this.port=port;
		this.numeroEtud=numeroEtud;
	}

	public void getConnection() throws UnknownHostException, IOException{
		try{
			boolean connection = false;
			socket = new Socket(ip,port);
			out = new PrintWriter(socket.getOutputStream(),true);
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			out.write(numeroEtud);
			out.flush();
			//		    String ligne = input.readLine();
			//		    while(!ligne.equals("end")){
			//		    	if(ligne.equals("true")){
			//		    		connection = true;
			//		    	}
			//		    }
			if(true){
				getEDT();
			}else{
				System.out.println("Vous ne pouvez pas vous identifier avec le numéro etudiant");
			}

		}catch (UnknownHostException e) {

			System.out.println("Destination unknow");
			System.exit(-1);
		}catch (IOException e) {
			e.printStackTrace();
			System.out.println("now to investigate this IO issue");
			System.exit(-1);
		}
	}

	public void getEDT() throws IOException{
		try{
			FileOutputStream outFile = new FileOutputStream("C:\\Users\\Romain\\git\\reseauL3\\Ressources\\testSortie.txt");
			
			int amount = 1;
			System.out.println("Coté client !");
			while((amount != -1) && (input.ready() == true)){
				System.out.println("On écris...");
				System.out.println(amount);
				amount = input.read();
				outFile.write(amount);
				System.out.println(amount);
			}
			System.out.println("Fin coté client !");
			
			
//			int c = 1;
//			while((c != -1) && (in.ready() == true)){
//				if (in.ready() == true){
//					c = in.read();
//				}
//				outFile.write(c);					
//			}
			
			
			
			outFile.close();



			//		       int c = 1;
			//				while((c != -1) && (in.ready() == true)){
			//					if (in.ready() == true){
			//						c = in.read();
			//					}
			//					outFile.write(c);					
			//				}




		}catch(IOException e){
			e.printStackTrace();
			System.out.println("now to investigate this IO issue");
			System.exit(-1);
		}
	}
}
