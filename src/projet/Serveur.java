package projet;

import java.io.BufferedInputStream;
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
 /** Explication de la m�thode/constructeur/watheveryouwantlol
 * @param  pour donner l'information sur UN SEUL param�tre
 * @return ce que la fonction renvoie (si aucun renvoie, enlever return)
 * @throws UN SEUL type d'exception renvoyer (� faire pour chaque type)
 */

/**
 * * La classe Serveur est la classe qui permet de cr�er le serveur, de s'y
 * connecter, etc
 *
 * @author Romain
 */
public class Serveur {
    ArrayList<Integer> numEtudiants;
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
    //La paire de cl� publique/priv�
    private KeyPair keyPair = null;
    //La destination des fichiers
    private String destination;
    
    /**
     * * Le constructeur Serveur(). Il cr�e la ServerSocket sur le port 4444.
     */
    public Serveur() {
        this.numEtudiants = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9));
        try {
            //On initialise le ServerSocket, sur le port 4444 afin d'�couter toutes les requ�tes �mises sur ce port
            //On utilise ce num�ro de port car entre
            serverSocket = new ServerSocket(4444);
        } catch (IOException ex) {
            System.out.println("Could not listen on port 4444");
            System.exit(-1);
        }
    }

    /**
     * La m�thode connect() permet � un client de se connecter au serveur. Elle
     * oblige aussi le client � s'authentifier via un num�ro.
     *
     * @param s, la cha�ne de caract�re qui indique la version du serveur �
     * utiliser
     * @return boolean, indiquant si la connection s'est bien d�roul�e
     * @throws java.io.IOException
     */
    public boolean connect(int n, String path) throws IOException {
        try {
        	this.destination = path;
            //On lance la fonction qui met la ServerSocket en attente d'une requ�te
            clientSocket = serverSocket.accept();
            switch (n) {
                case 1:
                    authentification();
                    break;
                case 2:
                    authentificationV2();
                    break;
            }
            boolean closed = close();
            //On return un bool�en pour savoir si l'authentification a fonctionn�e
            return closed;
        } catch (IOException e) {
            System.out.println("Accept failed on port 4444");
            System.exit(-1);
            return false;
        }
    }

    /**
     * La m�thode close() permet de fermer le PrintWriter et le BufferedReader,
     * utiles pour communiquer avec le client. Elle ferme aussi les sockets du
     * client (Socket) et du serveur (ServerSocket)
     *
     * @return Un bool�en qui indique si toutes les fermetures ont r�ussies
     * @throws IOException si une fermeture a �chou�e
     */
    public boolean close() throws IOException {
        try {
            //On ferme tout
            if(!clientSocket.isClosed()){
                out.close();
                in.close();
                clientSocket.close();
            }
            serverSocket.close();
            //Bool�en pour savoir si close() a march�e
            return true;
        } catch (IOException e) {
            System.out.println("Close failed on port 4444");
            System.exit(-1);
            return false;
        }catch(NullPointerException e){
            return false;
        }
    }

    /**
     * La m�thode authentification() est utilis� pour authentifier un client et
     * obtenir l'emploi du temps, non sign�
     *
     */
    public void authentification() {
        try {
            //On r�cup�re ce que nous envoie le client
            InputStream inputClient = clientSocket.getInputStream();
            int numEtud = inputClient.read();
            //On v�rifie que le num�ro envoy� par le client existe (authentification)
            if (numEtudiants.contains(numEtud)) {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                authentifie = true;
                send(clientSocket.getOutputStream(), new FileInputStream(destination + "testEntree.txt"));
            } else {
            	System.out.println("Le client n'est pas authentifi� !");
                out.println(" Vous n'�tes pas authentifi� !");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    
    /**
     * La m�thode authentificationV2() est utilis� pour authentifier un clients
     * et obtenir l'emploi du temps, non sign� et sign�
     *
     */
    public void authentificationV2() {
        try {
            //On r�cup�re ce que nous envoie le client
            InputStream inputClient = clientSocket.getInputStream();
            int numEtud = inputClient.read();
            //On v�rifie que le num�ro envoy� par le client existe (authentification)
            if (numEtudiants.contains(numEtud)) {
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                authentifie = true;
                send(clientSocket.getOutputStream(), new FileInputStream(destination + "testEntree.txt"));
                out.close();
                out = new PrintWriter(clientSocket.getOutputStream(), true);
                signFile(destination + "testEntree.txt",destination + "signFile.txt");
            } else {
                System.out.println("Le client n'est pas authentifi� !");
                out.println(" Vous n'�tes pas authentifi� !");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /*/** La m�thode getEDTServeur() permet d'envoyer au client l'emploi du temps via la m�thode send()
     *  si le client est authentifi�
     * @throws IOException quelque chose a �chou�e
     *
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
     }*/
    /**
     * La m�thode send() permet d'envoyer une donn�e d'un InputStream vers un
     * OutputStream
     *
     * @param OutputStream outClient l� o� se trouve la donn�e � obtenir
     * @param InputStream inClient l� o� on doit �crire la donn�e
     * @throws IOException si l'envoie n'a pas abouti
     */
    private void send(OutputStream outClient, FileInputStream inFile) throws IOException {
        try {
            try (InputStream inFileStream = inFile) {
                byte[] buf = new byte[8192];
                int c;
                
                while ((c = inFileStream.read(buf, 0, buf.length)) > 0) {
                    outClient.write(buf, 0, c);
                }
                outClient.flush();
            }

        } catch (IOException e) {
            System.out.println("Could not send file on port 4444");
            System.exit(-1);
        }
    }

    /**
     * La m�thode generateKeys() permet de g�n�rer un couple de cl�s
     *
     * @throws NoSuchAlgorithmException
     */
    public void generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        keyPair = keyPairGenerator.generateKeyPair();
    }

    /**
     * La m�thode importPublicKey() permet d'importer une cl� publique dans un
     * fichier
     *
     * @param file, le fichier dans lequel la cl� publique vas � �tre import�e
     * @throws IOException,NoSuchAlgorithmException
     * @throws java.security.NoSuchAlgorithmException
     */
    public void importPublicKey(String file) throws IOException, NoSuchAlgorithmException {
        File f = new File(file);
        FileOutputStream outFile;
        if (keyPair == null) {
            generateKeys();
        }

        if (f.exists()) {
            outFile = new FileOutputStream(f);
        } else {
            f.createNewFile();
            outFile = new FileOutputStream(f);
        }

        PublicKey publicKey = keyPair.getPublic();

        byte[] key = publicKey.getEncoded();
        outFile.write(key);
        outFile.close();
    }

    /**
     * La m�thode signFile() permet de signer un fichier
     *
     * @param file, le fichier � signer
     * @param sign, le fichier acceuillant la signature
     * @throws java.io.IOException
     * @throws java.security.NoSuchAlgorithmException
     */
    public void signFile(String file, String sign) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidKeyException, SignatureException {

        File f = new File(file);
        File signFile = new File(sign);

        byte[] buffer = null;
        if (keyPair == null) {
            generateKeys();
        }

        PublicKey publicKey = keyPair.getPublic();
        importPublicKey(destination + "publicKey.txt");
        PrivateKey privateKey = keyPair.getPrivate();

        Signature rsa = Signature.getInstance("SHA1withRSA");
        rsa.initSign(privateKey);
        FileInputStream inSignFile = new FileInputStream(signFile);

        try (BufferedInputStream input = new BufferedInputStream(inSignFile)) {
            buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) >= 0) {
                rsa.update(buffer, 0, len);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        byte[] realSig = rsa.sign();

        // Save signature
        FileOutputStream outSignFile = new FileOutputStream(signFile);
        outSignFile.write(realSig);
        outSignFile.close();

        try {
            send(clientSocket.getOutputStream(), new FileInputStream(signFile));
        } catch (Exception e) {
            
        }
    }
}
