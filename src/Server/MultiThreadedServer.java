package Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import SendData.Message;
import SendData.SQLData;

public class MultiThreadedServer implements Runnable {

	protected int serverPort = 7866;
	protected ServerSocket serverSocket = null;
	protected boolean isStopped = false;
	protected Thread runningThread = null;
	public static ArrayList<String> AngemeldeteUser = new ArrayList<String>();
	public static ArrayList<WorkerRunnableRead> AngemeldeteWorkerRunnableRead = new ArrayList<WorkerRunnableRead>();
	public static ArrayList<Message> messageList = new ArrayList<Message>();
	public static ArrayList<SQLData> sqlBefehlsListe = new ArrayList<SQLData>();

	public MultiThreadedServer(int port) {
		this.serverPort = port;

		DBConnectorThread dBConnectorThread = new DBConnectorThread();
		dBConnectorThread.setName("1A clientReadThread");
		dBConnectorThread.start();
	}

	public void run() {
		synchronized (this) {
			this.runningThread = Thread.currentThread();
		}
		openServerSocket();
		while (!isStopped()) {
			Socket clientSocket = null;
			try {
				clientSocket = this.serverSocket.accept();
				// if(this.serverSocket.)
			} catch (IOException e) {
				if (isStopped()) {
					System.out.println("Server Stopped.");
					return;
				}
				throw new RuntimeException("Error accepting client connection",
						e);
			}
			new Thread(new WorkerRunnable(clientSocket, "Multithreaded Server"))
					.start();
		}
		System.out.println("Server Stopped.");
	}

	private synchronized boolean isStopped() {
		return this.isStopped;
	}

	public synchronized void stop() {
		this.isStopped = true;
		try {
			this.serverSocket.close();
		} catch (IOException e) {
			throw new RuntimeException("Error closing server", e);
		}
	}

	private void openServerSocket() {
		try {
			this.serverSocket = new ServerSocket(this.serverPort);
		} catch (IOException e) {
			throw new RuntimeException("Cannot open port 7866", e);
		}
	}

}