package projet;

import java.io.IOException;
import java.net.UnknownHostException;

public class mainClient {

	public static void main(String[] args) throws UnknownHostException, IOException {
		Client client = new Client("192.168.1.29", 4444, 1);
		client.getConnection();
	}

}
