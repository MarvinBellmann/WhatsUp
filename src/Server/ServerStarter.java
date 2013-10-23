package Server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerStarter {
	static String serverip = null;
	static int port = 7866;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			serverip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		MultiThreadedServer server = new MultiThreadedServer(port);
		new Thread(server).start();
		System.out.println("Server gestartet. Server IP ist: [" + serverip
				+ " |Port:" + port + "]");
		System.out.println("Warte auf Clients...");
	}

}
