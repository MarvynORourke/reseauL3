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
			
			int amount = 888;
			while(!input.ready()){
				amount ++;
				if (amount>10000){
					amount = 1;
				}
			}
			System.out.println("Cot� client !");			
			
			while((amount != -1) && (input.ready() == true)){
				System.out.println("On �cris dans le fichier");
				System.out.println(amount);
				amount = input.read();
				outFile.write(amount);
				System.out.println(amount);
			}
			System.out.println("Fin cot� client !");
			
			outFile.close();

		}catch(IOException e){
			e.printStackTrace();
			System.out.println("now to investigate this IO issue");
			System.exit(-1);
		}
	}
}
