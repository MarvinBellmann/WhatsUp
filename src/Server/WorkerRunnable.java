package Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;

import Client.FileSenderThreadClient;
import SendData.ByteData;
import SendData.Message;
import SendData.StartData;

public class WorkerRunnable implements Runnable {

	protected Socket clientSocket = null;
	protected String serverText = null;
	static Message messageIGot;
	static StartData startdata;
	static String message;
	public static ArrayList<Message> receivedMessageList = new ArrayList<Message>();
	ArrayList<Message> messageListChecken = new ArrayList<Message>();
	boolean clientAnwesend = true;
	InetAddress clientIP;
	int clientPort;
	public String user;
	boolean gecheckt;
	int genutztegecheckt = 1;
	boolean byteUebertragungsBeschuetzer=false;

	public WorkerRunnable(Socket clientSocket, String serverText) {
		this.clientSocket = clientSocket;
		this.serverText = serverText;
		this.clientIP = clientSocket.getInetAddress();
		this.clientPort = clientSocket.getPort();
	}

	public void run() {
		ServerStart.SystemWriteLogln("*** Anmeldung Client: ["
				+ clientSocket.getInetAddress() + " |Port:"
				+ clientSocket.getPort() + "]");
		ObjectInputStream ois = null;
		ObjectOutputStream oos = null;
		try {
			ois = new ObjectInputStream(clientSocket.getInputStream());

			if (clientAnwesend == true ) {
				WorkerRunnableRead serverReaderThread = new WorkerRunnableRead(
						clientSocket, ois, this);
				serverReaderThread.setName("1A serverReaderThread");
				serverReaderThread.start();
				
				/*boolean nichtErneutEinfügen=false;
				for(WorkerRunnableRead wr: MultiThreadedServer.AngemeldeteWorkerRunnableRead){
				    if(wr.user.equalsIgnoreCase(user)){
					nichtErneutEinfügen=true;
				    }
				    
				
				}
				
				if(nichtErneutEinfügen==false){
				    MultiThreadedServer.AngemeldeteWorkerRunnableRead
					.add(serverReaderThread);
				}
				ServerStart.SystemWriteLogln("ZZZZZZZZZZZZZZZZZZZZZZ size workerrunnablelis:"+MultiThreadedServer.AngemeldeteWorkerRunnableRead.size());
			*/
			}

			oos = new ObjectOutputStream(clientSocket.getOutputStream());
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		while (clientAnwesend == true) {
			
			if(byteUebertragungsBeschuetzer==false){

			try {
				messageListChecken.clear();
				messageListChecken.addAll(MultiThreadedServer.messageList);
				int index = 0;

				for (Message m : messageListChecken) {

					if (m.to.equalsIgnoreCase(this.user)) {
						oos.writeObject(m);
						ServerStart.SystemWriteLogln("<<< Message weitergeleitet an "
								+ this.user + "!");
						MultiThreadedServer.messageList.remove(index);
						break;
					}

					index++;
				}
				
				
				
				if (MultiThreadedServer.byteList.size() > 0) {
				//	for (ByteData b : MultiThreadedServer.byteList) {
					if (MultiThreadedServer.byteList.get(MultiThreadedServer.byteList.size() - 1).to.equalsIgnoreCase(this.user)) {
						String dataa=MultiThreadedServer.byteList.get(MultiThreadedServer.byteList.size() - 1).dateiname;
						System.out.println(dataa);
						oos.writeObject(MultiThreadedServer.byteList.get(MultiThreadedServer.byteList.size() - 1));
						MultiThreadedServer.byteList.remove(MultiThreadedServer.byteList.size() - 1);
						System.out.println("ByteData send");
						FileSenderThreadServer fs = new FileSenderThreadServer(
								dataa,clientSocket,oos);
						fs.setName("1A FileSenderSeverThread");

						fs.start();
					}
				//	}
				}
				
				
				
				
				
			} catch (Exception e) {
				ServerStart.SystemWriteLogln("!!! Abmeldung Client: [" + clientIP
						+ " |Port:" + clientPort
						+ "] - schließe Thread; Grund: " + e.getMessage());
				clientAnwesend = false;
			}
			try {
				Thread.sleep(15);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	}
}