package projet;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.util.Scanner;

public class mainServeur {

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {
        Scanner sc = new Scanner(System.in);
        System.out.println("S'il vous pla�t, entrez la version du serveur que"
                + " vous voulez utiliser :");
        int version = sc.nextInt();
        sc.nextLine();
        
        System.out.println("Veuillez signaler le path utilis� lors de cet �change :");
        String destination = sc.nextLine();
        
        Serveur mainServeur = new Serveur();
        System.out.println("Le serveur est maintenant en attente d'une connection client.");
        mainServeur.connect(version,destination);
        System.out.println("L'�change est termin�. Tous les fichiers ont �t� modifi�s comme il se doit.");
    }
}
