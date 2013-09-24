import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread{

	static ServerSocket server;
	static Thread thread;

	public void run() {

		// Server-Struktur
		try {
			server = new ServerSocket(5000);
			System.out.println("Server wurde gestartet!");

		} catch (IOException e) {
			System.out.println("Server konnte den Port nicht erreichen!");
			System.exit(1);
		}

		// Client-Verbindung
		Socket client = null;
		PrintWriter writer = null;
		BufferedReader reader = null;
		try {
			client = server.accept();

			OutputStream out = client.getOutputStream();
			writer = new PrintWriter(out);

			InputStream in = client.getInputStream();
			reader = new BufferedReader(new InputStreamReader(in));

		} catch (IOException e) {
			System.out.println("Keine Verbindung zum Server möglich!");
			System.exit(1);
		}

		// Kommunikation
		String string = null;
		try {
			while ((string = reader.readLine()) != null) {
				String income = "Empfangen von " + client.getInetAddress()
						+ ": " + string;
				System.out.println(income);

				writer.write(client.getInetAddress() + ": " + string + "\n");
				writer.flush();
			}

			writer.close();
			reader.close();
		} catch (IOException e) {
			System.out.println("Fehler beim Lesen!");
		}

		try {
			System.out.println("Schlafen!!");
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Tot!");
	}

	public static void main(String[] args) {

		thread = new Thread(new Server());
		thread.start();

	}
}
