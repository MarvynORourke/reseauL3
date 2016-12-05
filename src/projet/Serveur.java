package projet;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.security.*;


/*
//Un exemple de javadoc
/** Explication de la méthode/constructeur/watheveryouwantlol
 * @param  pour donner l'information sur UN SEUL paramètre
 * @return ce que la fonction renvoie (si aucun renvoie, enlever return)
 * @throws UN SEUL type d'exception renvoyer (à faire pour chaque type)
 */


/*** La classe Serveur est la classe qui permet de créer le serveur, de s'y connecter, etc
 * 
 * @author Romain
 *
 */
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
	//La paire de clé publique/privé
	private KeyPair keyPair = null;

	/*** Le constructeur Serveur().
	 * Il crée la ServerSocket sur le port 4444.
	 */
	public Serveur(){
		try {
			//On initialise le ServerSocket, sur le port 4444 afin d'écouter toutes les requêtes émises sur ce port
			//On utilise ce numéro de port car entre
			serverSocket = new ServerSocket(4444);
		} catch (IOException ex) {
			System.out.println("Could not listen on port 4444");
			System.exit(-1) ;
		}
	}

	/** La méthode connect() permet à un client de se connecter au serveur.
	 *  Elle oblige aussi le client à s'authentifier via un numéro.
	 * @return Un booléen qui indique si la connexion s'est bien déroulée
	 * @throws IOException si la méthode accept() a échouée
	 */
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

	/** La méthode close() permet de fermer le PrintWriter et le BufferedReader, utiles pour communiquer
	 * avec le client. Elle ferme aussi les sockets du client (Socket) et du serveur (ServerSocket)
	 * @return Un booléen qui indique si toutes les fermetures ont réussies
	 * @throws IOException si une fermeture a échouée
	 */
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

	/** La méthode authentification() est utilisé pour authentifier un clients
	 * @throws Exception si quelque chose a échouée
	 */
	public void authentification(){
		try{
			//On récupère ce que nous envoie le client
			InputStream inputClient = clientSocket.getInputStream();
			int numEtud = inputClient.read();
			//On vérifie que le numéro envoyé par le client existe (authentification)
			if (numEtudiants.contains(numEtud)){
				out = new PrintWriter(clientSocket.getOutputStream(), true);
				out.write("Vous êtes authentifié.");
				out.print("Vous êtes authentifié.");
				authentifie = true;
				send(clientSocket.getOutputStream(),new FileInputStream("C:\\Users\\Romain\\git\\reseauL3\\Ressources\\testEntrée.txt"));
				close();
			}else{
				System.out.println("Vous n'êtes pas authentifié !");
			}
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	/*/** La méthode getEDTServeur() permet d'envoyer au client l'emploi du temps via la méthode send()
	 *  si le client est authentifié
	 * @throws IOException quelque chose a échouée
	 *
	public void getEDTServeur() throws IOException{
		try{
			//Si le client est authentifié, on envoie l'emploi du temps via la fonction send()
			if(authentifie == true){
				send(clientSocket.getOutputStream(),new FileInputStream("C:\\Users\\Romain\\git\\reseauL3\\Ressources\\testEntrée.txt"));
			}else{//Sinon on prévient le client qu'il n'est pas authentifié
				out.flush();
				out.print("Vous n'êtes pas authentifié");
			}
		}catch(IOException e){
			System.out.println("Quelque chose a mal tourné lol");     
			System.exit(-1);
		}
	}*/

	/** La méthode send() permet d'envoyer une donnée d'un InputStream vers un OutputStream
	 * @param OutputStream outClient là où se trouve la donnée a obtenir
	 * @param InputStream inClient là où on doit écrire la donnée
	 * @throws IOException si l'envoie n'a pas abouti
	 */
	private void send(OutputStream outClient, FileInputStream inFile) throws IOException{
		try{
			System.out.println("Début coté serveur !");
			InputStream inFileStream = inFile;

			byte[] buf = new byte[8192];
			int c;

			while ((c = inFileStream.read(buf, 0, buf.length)) > 0) {
				System.out.println("On écris vers le client");
				System.out.println("buf : ");
				System.out.println(buf);
				System.out.println("c : ");
				System.out.println(c);
				outClient.write(buf, 0, c);
			}
			System.out.println(c);
			outClient.flush();
			inFileStream.close();
			System.out.println("Fin coté serveur !");

		}catch(IOException e){
			System.out.println("Could not send file on port 4444");
			System.exit(-1) ;
		}
	}

	/** La méthode generateKeys() permet de générer un couple de clés
	 * @return keyPair, une paire de clé publique/privée
	 * @throws NoSuchAlgorithmException 
	 */

	private KeyPair generateKeys() throws NoSuchAlgorithmException{
		KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
		keyPairGenerator.initialize(1024);
		keyPair = keyPairGenerator.generateKeyPair();
		return keyPair;
	}

	/** La méthode importPublicKey() permet d'importer une clé publique dans un fichier
	 * @throws IOException 
	 */

	private void importPublicKey(String file) throws IOException{
		File f = new File(file);
		FileOutputStream outFile = null;
		if(f.exists() && !f.isDirectory()) { 
			outFile = new FileOutputStream("C:\\Users\\Romain\\git\\reseauL3\\Ressources\\publicKey.txt");
		}else{
			f.createNewFile();
			outFile = new FileOutputStream("C:\\Users\\Romain\\git\\reseauL3\\Ressources\\publicKey.txt");
		}

		PublicKey publicKey = keyPair.getPublic();
		String publicKeyString =  publicKey.toString();
	}
}