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
		
		System.out.println("Veuillez entrer le numéro etudiant");
		int num = sc.nextInt();
		sc.nextLine();
		
		System.out.println("Veuillez entrer le path où se touvent les fichiers (publicKey.txt,...)");
		String destination = sc.nextLine();
		
		System.out.println("Veuillez entrez la version du server (1/2)");
		int version = sc.nextInt();
		sc.nextLine();

		Client client = new Client(ip, 4444, num, destination);
		if(version == 1){
			System.out.println("La version utilisée est la \"1\". Le client va maintenant se connecter et récupérer l'EDT.");
			client.getConnection();
			client.getEDTV1();
		}else if(version ==2){
			System.out.println("La version utilisée est la \"2\". Le client va maintenant se connecter, récupérer l'EDT et vérifier la signature de l'EDT signé.");
			client.getConnection();
			client.getEDTV2();
		}
		System.out.println("L'échange est terminé. Tous les fichiers ont été modifiés comme il se doit.");
	}

}
