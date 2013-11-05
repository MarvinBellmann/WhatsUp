package Client;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
//import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import SendData.ByteData;
import SendData.Message;
import SendData.SQLData;
import SendData.StartData;

public class Client extends Thread {

	static String serverIP;
	String loginUser;
	String loginPw;
	public static ArrayList<Message> messageList = new ArrayList<Message>();
	public static ArrayList<SQLData> sqlCommandsList = new ArrayList<SQLData>();
	public static ArrayList<ByteData> byteList = new ArrayList<ByteData>();
	static boolean connection;
	static String unknown = "Noch ungewiss";
	boolean onlyLoginClient = false;
	static boolean running = true;
	static Socket socket;
	static ObjectOutputStream oos = null;

	public static void send(String From, String To, String Text) {
		messageList.add(new Message(From, To, Text));
	}

	public static void sendSQL(SQLData sqldata) {
		sqlCommandsList.add(sqldata);
	}

	public Client(String sIP) {
		serverIP = sIP;
	}

	public Client(String sIP, String user, String pw) {
		serverIP = sIP;
		this.loginUser = user;
		this.loginPw = pw;
		onlyLoginClient = true;
	}

	public void run() {
		try {
			work();
		} catch (ClassNotFoundException | IOException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void work() throws UnknownHostException, IOException,
			ClassNotFoundException, InterruptedException {
		System.out.println("*** Verbinde an: "
				+ InetAddress.getByName(serverIP));
		InetAddress host = InetAddress.getByName(serverIP);// InetAddress.getLocalHost();
		socket = null;

		ObjectInputStream ois = null;

		while (running == true) {

			if (MainFrame.byteSendingProtector == false) {

				try {

					if (socket == null) {
						socket = new Socket(host.getHostName(), 7866);
						socket.setTcpNoDelay(true);
						oos = new ObjectOutputStream(socket.getOutputStream());
						ois = new ObjectInputStream(socket.getInputStream());
					}

					if (socket != null && connection == false) {
						System.out.println("*** Erfolgreich verbunden.");
						MainFrame.statusChanger();
						connection = true;
						oos.writeObject(new StartData(MainFrame.username));
						ClientRead clientReaderThread = new ClientRead(socket,
								ois);
						clientReaderThread.setName("1A clientReadThread");
						clientReaderThread.start();

					}

					if (messageList.size() > 0) {
						oos.writeObject(messageList.get(messageList.size() - 1));
						// oos.wr
						System.out.println(">>> Message send to "
								+ messageList.get(messageList.size() - 1).to
								+ " ("
								+ messageList.get(messageList.size() - 1).date
								+ ")");

						MainFrame.chatparserecho(messageList.get(messageList
								.size() - 1).to,
								messageList.get(messageList.size() - 1)
										.toText());

						messageList.remove(messageList.size() - 1);
					}

					if (sqlCommandsList.size() > 0) {
						oos.writeObject(sqlCommandsList.get(sqlCommandsList
								.size() - 1));
						sqlCommandsList.remove(sqlCommandsList.size() - 1);
					}

					if (byteList.size() > 0) {
						String dataa = byteList.get(byteList.size() - 1).dateiname;
						System.out.println(dataa);
						oos.writeObject(byteList.get(byteList.size() - 1));
						byteList.remove(byteList.size() - 1);
						System.out.println("ByteData send");
						FileSenderThreadClient fs = new FileSenderThreadClient(
								dataa, socket, oos);
						fs.setName("1A FileSenderThread");

						fs.start();
					}

					unknown = "Erfolg";
				} catch (Exception e) {
					unknown = "Fehschlag";
					MainFrame.statusLbl.setText("Offline");
					MainFrame.statusLbl.setForeground(Color.RED);
					System.out
							.println("Verbindungs Error! Server Offline? Neuversuch in 5 Sekunden. Error:"
									+ e.getMessage());
					Thread.sleep(5000);
				}
			}

			Thread.sleep(60);// 100

		}
	}

	public static void sendBytes(String bfrom, String bto,
			String zuVerschickendeDatei) {
		// TODO Auto-generated method stub
		File myFile = new File(zuVerschickendeDatei.replace('\\', '/'));
		FileInputStream fis;
		long count = 0;
		try {
			fis = new FileInputStream(myFile);
			FileChannel fileChannel = fis.getChannel();
			count = fileChannel.size();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// os = sock.getOutputStream();

		byteList.add(new ByteData(bfrom, bto, zuVerschickendeDatei.replace(
				'\\', '/'), count));

		// oos.writeObject(new ByteData("zuVerschickendeDatei"));

	}
}
