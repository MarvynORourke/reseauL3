package projet;

import java.io.IOException;
import java.net.UnknownHostException;

public class mainClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Client client = new Client("192.168.56.1", 7777, 1);
		client.getConnection();
	}

}
