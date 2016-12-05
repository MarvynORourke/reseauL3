package projet;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;

public class mainClient {

	public static void main(String[] args) throws UnknownHostException, IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException {
		Client client = new Client("192.168.43.70", 4444, 1);
		client.getConnection();
		client.getEDTV1();
	}

}
