package projet;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;

public class Serveur {

	//La liste des numéros d'étudiants
	ArrayList<Integer> numEtudiants = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
	//La socket du serveur (pour attendre une requête)
	ServerSocket serverSocket = null;
	//La socket du client (pour héberger une requête avec un client)
	Socket clientSocket = null;
	//Le PrinWriter, pour envoyer des trucs au client
	PrintWriter out;
	//Le BufferedReader, pour lire des trucs envoyés par le client
	BufferedReader in;
	//Un booléen pour savoir si le client est authentifié
	boolean authentifie = false;

	public Serveur(){
		try {
			//On initialise le ServerSocket, sur le port 4444 afin d'écouter toutes les requêtes émises sur ce port
			serverSocket = new ServerSocket(4444);
		} catch (IOException ex) {
			System.out.println("Could not listen on port 4444");
			System.exit(-1) ;
		}
	}

	public boolean connect() throws IOException{
		try{
			//On lance la fonction qui met la ServerSocket en attente d'une requête
			clientSocket = serverSocket.accept();
			//On lance la fonction qui permet à l'utilisateur d'être authentifié
			authentification();
			//On return un booléen pour savoir si l'authentification a fonctionnée
			return true;
		}catch(IOException e){
			System.out.println("Accept failed on port 4444");     
			System.exit(-1) ;
			return false;
		}
	}

	public boolean close() throws IOException{
		try{
			//On ferme tout
			out.close();
			in.close();
			clientSocket.close();
			serverSocket.close();
			//Booléen pour savoir si close() a marchée
			return true;
		}catch(IOException e){
			System.out.println("Close failed on port 4444");     
			System.exit(-1) ;
			return false;
		}
	}

	public void authentification(){
		try{
			//On récupère ce que nous envoie le client
			String message = getMessage(clientSocket);
			//On vérifie que le numéro envoyé par le client existe (authentification)
			if (numEtudiants.contains(message)){
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				out.write("Vous êtes authentifié.");
				out.print("Vous êtes authentifié.");
				authentifie = true;
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	public void getEDTServeur() throws IOException{
		//Si le client est authentifié, on envoie l'emploi du temps via la fonction send()
		if(authentifie == true){
			send(clientSocket.getOutputStream(),new FileInputStream("./test.txt"));
		}else{//Sinon on prévient le client qu'il n'est pas authentifié
			out.flush();
			out.print("Vous n'êtes pas authentifié");
		}
	}

	private void send(OutputStream outClient, InputStream inClient) throws IOException{
		try{
			//On crée un tableau de bytes
			byte[] buf = new byte[1024];
			int n;
			//Tant que InputStream est plein, on transfère les données de inClient vers le OutputStream, outClient
			while((n=inClient.read(buf))!=-1){
				outClient.write(buf,0,n);
			}
		}catch(IOException e){
			System.out.println("Could not send file on port 4444");
			System.exit(-1) ;
		}

	}

	private String getMessage(Socket socket) throws IOException{
		//On ouvre le canal d'entrée
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		//On crée un StringBuilder
		StringBuilder sb = new StringBuilder(100);
		//On crée un tableau de bytes
		byte[] bytes = new byte[1024<<8];
		//On lit ce qu'il y a sur l'InputStream de la socket entrée en paramètre
		int bytesRead = in.read(bytes);
		//On construit la string à renvoyer
		sb.append(new String(bytes,0,bytesRead,"UTF-8"));
		return sb.toString();
	}

	/*
	public void serveurSock(){
		ServerSocket serverSocket = null;
		try {
			serverSocket = new ServerSocket(4444);
		} catch (IOException ex) {
			System.out.println("Could not listen on port 4444");
			System.exit(-1) ;
		}

		Socket clientSocket = null;


		try{
			//Attente de la requête du client
			clientSocket = serverSocket.accept();
			//Ouverture du canal de sortie vers le client
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			//Récupération du message envoyé par le client
			String message = getMessage(clientSocket);
			if (numEtudiants.contains(message)){
				transfert(new FileInputStream("./test.txt"),clientSocket.getOutputStream(),true); //Envoie du fichier test.txt vers le client
			}
		}
		catch(IOException e){
			System.out.println("Accept failed on port 4444");     
			System.out.println(-1);
		}

	}



	public static void transfert(InputStream in, OutputStream out, boolean closeOnExit) throws IOException {
		byte buf[] = new byte[1024];
		int n;
		while((n=in.read(buf))!=-1){
			out.write(buf,0,n);
		}

		if (closeOnExit)
		{
			in.close();
			out.close();
		}
	}*/
}