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
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

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
	private final String destination = "C:\\Users\\Marv\\Desktop\\";
	//Clé privée permettant le decryptage 
	private PublicKey publicKey;
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
	 * Permet le Client de récupérer la clé public
	 * @param path
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws IOException
	 */
	public void setPublicKey() throws NoSuchAlgorithmException, InvalidKeySpecException, IOException{
		Path path = Paths.get("C:\\Users\\Marv\\Desktop\\publicKey.txt");
		byte[] data = Files.readAllBytes(path);
		KeyFactory f = KeyFactory.getInstance("RSA");
		PublicKey publicKey = f.generatePublic(new X509EncodedKeySpec(data));
		this.publicKey = publicKey;
	}
	/**
	 * Permet de vérifier si une fonction est bonne.
	 * @param data
	 * @param s
	 * @return
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws SignatureException
	 */
	public boolean verifySign(byte[]data, byte[] s) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException{
		Signature rsa = Signature.getInstance("SHA1withRSA");
		rsa.initVerify(publicKey);
		rsa.update(data);
		return rsa.verify(s);
		
		
	}

	/**
	 * Permet d'enregister l'emploi du temps dans un fichier et de dire si la signature est vérifié. 
	 * @throws IOException
	 * @throws SignatureException 
	 * @throws NoSuchAlgorithmException 
	 * @throws InvalidKeyException 
	 */
	public void getEDTV1() throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException{
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
				amount = input.read();
				outFile.write(amount);
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
	public void getEDTV2() throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException{
		try{
			File fichier  = new File(destination+"EDT.txt");
			fichier.createNewFile();
			FileOutputStream outFile = new FileOutputStream(destination+"EDT.txt");
			FileOutputStream outFile2 = new FileOutputStream(destination+"signature.txt");
			
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
				amount = input.read();
				outFile.write(amount);
			}
			amount = 888;
			while(!input.ready()){
				amount ++;
				if (amount>10000){
					amount = 1;
				}
			}
			while((amount != -1) && (input.ready() == true)){
				System.out.println("On récupère la signature");
				amount = input.read();
				outFile2.write(amount);
			}
			if(publicKey == null){
				this.setPublicKey();
			}
			
			System.out.println("Fin coté client !");
			Path path = Paths.get(destination+"EDT.txt");
			byte[] file = Files.readAllBytes(path);
			Path path2 = Paths.get(destination+"signature.txt");
			byte[] file2 = Files.readAllBytes(path2);
			if(verifySign(file, file2)){
				System.out.println("La signature a été vérifié");
			}else{
				System.out.println("La signature n'est pas bonne");
			}
			
			outFile.close();
			outFile2.close();
			this.close();

		}catch(IOException e){
			e.printStackTrace();
			System.out.println("now to investigate this IO issue");
			System.exit(-1);
		}
	}
}
