package projet;
import java.io.BufferedReader;
import java.io.File;
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
		    String ligne = input.readLine();
		    while(!ligne.equals("end")){
		    	if(ligne.equals("true")){
		    		connection = true;
		    	}
		    }
		    if(connection){
		    	getEDT();
		    }else{
		    	System.out.println("Vous ne pouvez pas vous identifier avec le numéro etudiant");
		    }
	    
		}catch (UnknownHostException e) {
			
			System.out.println("Destination unknow");
			System.exit(-1);
		}catch (IOException e) {
			System.out.println("now to investigate this IO issue");
			System.exit(-1);
		}
	}
	
	public void getEDT() throws IOException{
		try{
			RandomAccessFile inFile = new RandomAccessFile ("edt.txt","rw" ); 
			byte bb[] = new byte[1024];
		       int amount;
		       while((amount = input.read()) != -1){
		           inFile.write(bb,0,amount);
		           }
		       inFile.close();
		}catch(IOException e){
			System.out.println("now to investigate this IO issue");
			System.exit(-1);
		}
		
	}
	

		
		public static void main(String[] args) {			
		}
}
