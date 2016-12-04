package projet;

import java.io.IOException;

public class mainServeur {

	public static void main(String[] args) throws IOException {
		Serveur mainServeur = new Serveur();
		mainServeur.connect();
	}
}