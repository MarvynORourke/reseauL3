package projet;

import java.io.IOException;
import java.net.UnknownHostException;

public class mainClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Client client = new Client("192.168.56.1", 4444, 1);
		client.getConnection();
		client.getEDT();
	}

}
