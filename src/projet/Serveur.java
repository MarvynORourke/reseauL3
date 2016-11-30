package projet;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;


/*
//Un exemple de javadoc
/** Explication de la m�thode/constructeur/watheveryouwantlol
 * @param  pour donner l'information sur UN SEUL param�tre
 * @return ce que la fonction renvoie (si aucun renvoie, enlever return)
 * @throws UN SEUL type d'exception renvoyer (� faire pour chaque type)
 */


/*** La classe Serveur est la classe qui permet de cr�er le serveur, de s'y connecter, etc
 * 
 * @author Romain
 *
 */
public class Serveur {

	//La liste des num�ros d'�tudiants
	ArrayList<Integer> numEtudiants = new ArrayList<Integer>(Arrays.asList(1,2,3,4,5,6,7,8,9));
	//La socket du serveur (pour attendre une requ�te)
	ServerSocket serverSocket = null;
	//La socket du client (pour h�berger une requ�te avec un client)
	Socket clientSocket = null;
	//Le PrinWriter, pour envoyer des trucs au client
	PrintWriter out;
	//Le BufferedReader, pour lire des trucs envoy�s par le client
	BufferedReader in;
	//Un bool�en pour savoir si le client est authentifi�
	boolean authentifie = false;

	/*** Le constructeur Serveur().
	 * Il cr�e la ServerSocket sur le port 4444.
	 *
	 */
	public Serveur(){
		try {
			//On initialise le ServerSocket, sur le port 4444 afin d'�couter toutes les requ�tes �mises sur ce port
			serverSocket = new ServerSocket(4444);
		} catch (IOException ex) {
			System.out.println("Could not listen on port 4444");
			System.exit(-1) ;
		}
	}

	/** La m�thode connect() permet � un client de se connecter au serveur.
	 *  Elle oblige aussi le client � s'authentifier via un num�ro.
	 * @return Un bool�en qui indique si la connexion s'est bien d�roul�e
	 * @throws IOException si la m�thode accept() a �chou�e
	 */
	public boolean connect() throws IOException{
		try{
			//On lance la fonction qui met la ServerSocket en attente d'une requ�te
			clientSocket = serverSocket.accept();
			//On lance la fonction qui permet � l'utilisateur d'�tre authentifi�
			authentification();
			//On return un bool�en pour savoir si l'authentification a fonctionn�e
			return true;
		}catch(IOException e){
			System.out.println("Accept failed on port 4444");     
			System.exit(-1) ;
			return false;
		}
	}

	/** La m�thode close() permet de fermer le PrintWriter et le BufferedReader, utiles pour communiquer
	 * avec le client. Elle ferme aussi les sockets du client (Socket) et du serveur (ServerSocket)
	 * @return Un bool�en qui indique si toutes les fermetures ont r�ussies
	 * @throws IOException si une fermeture a �chou�e
	 */
	public boolean close() throws IOException{
		try{
			//On ferme tout
			out.close();
			in.close();
			clientSocket.close();
			serverSocket.close();
			//Bool�en pour savoir si close() a march�e
			return true;
		}catch(IOException e){
			System.out.println("Close failed on port 4444");     
			System.exit(-1) ;
			return false;
		}
	}

	/** La m�thode authentification() est utilis� pour authentifier un clients
	 * @throws Exception si quelque chose a �chou�e
	 */
	public void authentification(){
		try{
			//On r�cup�re ce que nous envoie le client
			InputStream inputClient = clientSocket.getInputStream();
			int numEtud = inputClient.read();
			//On v�rifie que le num�ro envoy� par le client existe (authentification)
			if (numEtudiants.contains(numEtud)){
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				out.write("Vous �tes authentifi�.");
				out.print("Vous �tes authentifi�.");
				authentifie = true;
				send(clientSocket.getOutputStream(),new FileInputStream("C:\\Users\\Romain\\git\\reseauL3\\Ressources\\testEntr�e.txt"));
			}else{
				System.out.println("Vous n'�tes pas authentifi� !");
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	/** La m�thode getEDTServeur() permet d'envoyer au client l'emploi du temps via la m�thode send()
	 *  si le client est authentifi�
	 * @throws IOException quelque chose a �chou�e
	 */
	public void getEDTServeur() throws IOException{
		try{
			//Si le client est authentifi�, on envoie l'emploi du temps via la fonction send()
			if(authentifie == true){
				send(clientSocket.getOutputStream(),new FileInputStream("C:\\Users\\Romain\\git\\reseauL3\\Ressources\\testEntr�e.txt"));
			}else{//Sinon on pr�vient le client qu'il n'est pas authentifi�
				out.flush();
				out.print("Vous n'�tes pas authentifi�");
			}
		}catch(IOException e){
			System.out.println("Quelque chose a mal tourn� lol");     
			System.exit(-1);
		}
	}

	/** La m�thode send() permet d'envoyer une donn�e d'un InputStream vers un OutputStream
	 * @param OutputStream outClient l� o� se trouve la donn�e a obtenir
	 * @param InputStream inClient l� o� on doit �crire la donn�e
	 * @throws IOException si l'envoie n'a pas abouti
	 */
	private void send(OutputStream outClient, FileInputStream inFile) throws IOException{
		try{
			System.out.println("D�but cot� serveur !");
			InputStream inFileStream = inFile;
			byte[] buf = new byte[8192];
			int c;

			while ((c = inFileStream.read(buf, 0, buf.length)) > 0) {
				System.out.println(c);
				outClient.write(buf, 0, c);
				System.out.println(c);
			}
			System.out.println(c);

			outClient.flush();
			inFileStream.close();
			System.out.println("Fin cot� serveur !");
			
			
//			File f = new File("C:\\Users\\Romain\\git\\reseauL3\\Ressources\\testEntr�e.txt");
//
//			byte[] buf = new byte[8192];
//
//			InputStream is = new FileInputStream(f);
//
//			int c = 0;
//
//			while ((c = is.read(buf, 0, buf.length)) > 0) {
//				out.write(buf, 0, c);
//			}
//
//			out.flush();
//			is.close();
			
			
			
			
		}catch(IOException e){
			System.out.println("Could not send file on port 4444");
			System.exit(-1) ;
		}
	}

	/** 
	 * 
	 * @param socket
	 * @return
	 * @throws IOException
	 */
	private String getMessage(Socket socket) throws IOException{
		//On ouvre le canal d'entr�e
		BufferedInputStream in = new BufferedInputStream(socket.getInputStream());
		//On cr�e un StringBuilder
		StringBuilder sb = new StringBuilder(100);
		//On cr�e un tableau de bytes
		byte[] bytes = new byte[1024<<8];
		//On lit ce qu'il y a sur l'InputStream de la socket entr�e en param�tre
		int bytesRead = in.read(bytes);
		//On construit la string � renvoyer
		sb.append(new String(bytes,0,bytesRead,"UTF-8"));
		System.out.println("Civoi le num etud mais dans getMessage(): "+sb.toString());
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
			//Attente de la requ�te du client
			clientSocket = serverSocket.accept();
			//Ouverture du canal de sortie vers le client
			out = new PrintWriter(clientSocket.getOutputStream(), true);
			//R�cup�ration du message envoy� par le client
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