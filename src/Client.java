import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	static Socket client = null;
	PrintWriter writer = null;
	BufferedReader reader = null;
	
	public static void send(String From, String To, String Text){
	    System.out.println("From: "+From+" |To: " + To + " |message: " + Text);
	    Message message = new Message(From,To,Text);
	}

	public Client(String serverIP) {

		// Client-Struktur
		try {
			client = new Socket(serverIP, 5000);
			System.out.println("Client wurde gestartet!");

		} catch (UnknownHostException e) {
			System.out.println("Host wurde nicht gefunden!");
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Client konnte nicht gestartet werden!");
			System.exit(1);
		}

		// Kommunikation
		try {
			OutputStream out = client.getOutputStream();
			writer = new PrintWriter(out);

			InputStream in = client.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in));

		} catch (IOException e) {
			System.out.println("Keine Verbindung zum Server möglich!");
			System.exit(1);
		}

		writer.write("Hallo Server!" + "\n");
		writer.flush();

		try {
			String string = null;
			while ((string = reader.readLine()) != null) {
				System.out.println(string);
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			System.out.println("Fehler beim Beenden!");
		}

	}

}
