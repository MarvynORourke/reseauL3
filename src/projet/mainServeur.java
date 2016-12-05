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
        System.out.println("S'il vous plaît, entrez la version du serveur que"
                + "vous voulez utiliser :");
        int version = sc.nextInt();
        Serveur mainServeur = new Serveur();
        mainServeur.connect(version);
    }
}
