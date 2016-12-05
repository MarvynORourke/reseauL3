package projet;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class mainClient {

	public static void main(String[] args) throws UnknownHostException, IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException, NoSuchProviderException {
		Scanner sc = new Scanner(System.in);
		
		System.out.println("Entrez l'ip du server");
		String ip = sc.nextLine();
		
		System.out.println("Veuillez entrer le num�ro etudiant");
		int num = sc.nextInt();
		sc.nextLine();
		
		System.out.println("Veuillez entrer le path o� se touvent les fichiers (publicKey.txt,...)");
		String destination = sc.nextLine();
		
		System.out.println("Veuillez entrez la version du server (1/2)");
		int version = sc.nextInt();
		sc.nextLine();

		Client client = new Client(ip, 4444, num, destination);
		if(version == 1){
			System.out.println("La version utilis�e est la \"1\". Le client va maintenant se connecter et r�cup�rer l'EDT.");
			client.getConnection();
			client.getEDTV1();
		}else if(version ==2){
			System.out.println("La version utilis�e est la \"2\". Le client va maintenant se connecter, r�cup�rer l'EDT et v�rifier la signature de l'EDT sign�.");
			client.getConnection();
			client.getEDTV2();
		}
		System.out.println("L'�change est termin�. Tous les fichiers ont �t� modifi�s comme il se doit.");
	}

}
