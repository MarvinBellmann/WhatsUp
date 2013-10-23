package Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;

public class FileSenderThread extends Thread {
	static FileInputStream fis;
	static Socket sock;
	static OutputStream os;
	static boolean stop;
	static String zuVerschickendeDatei = "C:/vwlmitschriften.pdf";

	public FileSenderThread(String zuVerschickendeDatei) {
		this.zuVerschickendeDatei = zuVerschickendeDatei;
	}

	public void run() {

		System.out.println("$$$ SenderServer gestartet");

		ServerSocket servsock = null;
		try {
			servsock = new ServerSocket(7867);
		} catch (IOException e) {
			e.printStackTrace();
		}
		File myFile = new File(zuVerschickendeDatei.replace('\\', '/'));

		System.out.println("$$$ Zu verschickende Datei: "
				+ myFile.getAbsolutePath());
		FileInputStream fis = null;
		OutputStream os = null;
		Socket sock = null;
		while (stop == false) {
			System.out.println("$$$ SenderServer wartet auf Client");
			try {

				sock = servsock.accept();

				byte[] mybytearray = new byte[1024];
				fis = new FileInputStream(myFile);
				os = sock.getOutputStream();
				FileChannel fileChannel = fis.getChannel();
				int count;
				System.out.println("$$$ Starting to Send KBytes: "
						+ fileChannel.size() / 1024);
				while ((count = fis.read(mybytearray)) >= 0) {
					os.write(mybytearray, 0, count);

				}
				System.out.println("$$$ Done!");
				os.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fis.close();
					os.close();
					sock.close();
					System.out.println("$$$ Stopped loop");
					stop = true;
				} catch (IOException e) {
					e.printStackTrace();
				}

				System.out.println("$$$ Socket closed");
			}
		}
	}
}
