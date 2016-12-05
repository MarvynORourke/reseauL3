package projet;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

public class mainClient {

	public static void main(String[] args) throws UnknownHostException, IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException, InvalidKeySpecException {
		Scanner sc = new Scanner(System.in);
		System.out.println("Entrez l'ip du server");
		String ip = sc.nextLine();
		System.out.println("Veuillez entrer le numéro etudiant");
		int num = sc.nextInt();
		Client client = new Client(ip, 4444, num);
		System.out.println("Veuillez entrez la version du server (1/2)");
		int version = sc.nextInt();
		if(version == 1){
			client.getConnection();
			client.getEDTV1();
		}else if(version ==2){
			client.getConnection();
			client.getEDTV2();
		}
	}

}
