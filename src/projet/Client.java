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

	//La socket qui permet d'ouvrir la porte d'un serveur
	private Socket socket;
	//Permet d'écrire des trucs au server
	private PrintWriter out;
	//Permet de lire des trucsdu server
	private BufferedReader input;
	//L'adresse ip du Client
	private String ip;
	//Le port du Client
	private int port;
	//Le numéro étudiant du Client
	private int numeroEtud;
	//Fichier de destination
	private final String destination = "C:\\Users\\Romain\\git\\reseauL3\\Ressources\\";
	
	/**
	 * Construteur de la classe client qui initialise l'ip,le port et le numéro etudiant
	 * @param ip
	 * @param port
	 * @param numeroEtud
	 */
	public Client(String ip, int port, int numeroEtud){
		this.ip=ip;
		this.port=port;
		this.numeroEtud=numeroEtud;
	}

	/**
	 * Permet au Client de se connecter au serveur grâce à son numéro étudiant.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
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
	
	/** La méthode close() permet de fermer le PrintWriter et le BufferedReader.
	 * Elle ferme aussi la socket.
	 * @return Un booléen qui indique si toutes les fermetures ont réussies
	 * @throws IOException si une fermeture a échouée
	 */
	public boolean close() throws IOException{
		try{
			//On ferme tout
			out.close();
			input.close();
			socket.close();
			//Booléen pour savoir si close() a marchée
			return true;
		}catch(IOException e){
			System.out.println("Close failed on port 4444");     
			System.exit(-1) ;
			return false;
		}
	}

	/**
	 * Permet d'enregister l'emploi du temps dans un fichier/
	 * @throws IOException
	 */
	public void getEDT() throws IOException{
		try{
			File fichier  = new File(destination+"EDT.txt");
			fichier.createNewFile();
			FileOutputStream outFile = new FileOutputStream(destination+"EDT.txt");
			
			int amount = 888;
			while(!input.ready()){
				amount ++;
				if (amount>10000){
					amount = 1;
				}
			}
			System.out.println("Coté client !");			
			
			while((amount != -1) && (input.ready() == true)){
				System.out.println("On écris dans le fichier");
				System.out.println(amount);
				amount = input.read();
				outFile.write(amount);
				System.out.println(amount);
			}
			System.out.println("Fin coté client !");
			
			outFile.close();
			this.close();

		}catch(IOException e){
			e.printStackTrace();
			System.out.println("now to investigate this IO issue");
			System.exit(-1);
		}
	}
}
