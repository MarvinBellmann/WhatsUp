package Client;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
//import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.FileChannel;

import javax.swing.JFrame;
import javax.swing.JLabel;

public class FileSenderThread extends Thread {
	static FileInputStream fis;
	static Socket sock;
	static OutputStream os;
	static boolean stop;
	static String sendingData = "C:/vwlmitschriften.pdf";
	ObjectOutputStream oos;

	public FileSenderThread(String data,Socket sock, ObjectOutputStream oos) {
		sendingData = data;
		this.oos=oos;
		//this.sock=sock;
	}

	public void run() {
		
		JFrame frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(10,10,200,100);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.setVisible(true);
		JLabel label_2 = new JLabel("Übertragen");
		label_2.setBounds(10, 10, 180, 30);
		frame.getContentPane().add(label_2);

	//	System.out.println("$$$ SenderServer gestartet");


		File myFile = new File(sendingData.replace('\\', '/'));

		System.out.println("$$$ Zu verschickende Datei: "
				+ myFile.getAbsolutePath());
		FileInputStream fis = null;
		//OutputStream os = null;

	//	while (stop == false) {
			//System.out.println("$$$ SenderServer wartet auf Client");
			try {

			//	sock = servsock.accept();

				byte[] mybytearray = new byte[1024];
				fis = new FileInputStream(myFile);
				//os = sock.getOutputStream();
				FileChannel fileChannel = fis.getChannel();
				int count;
				System.out.println("$$$ Starting to Send KBytes: "
						+ fileChannel.size() / 1024);
				while ((count = fis.read(mybytearray)) >= 0) {
					oos.write(mybytearray, 0, count);
					label_2.setText("Gesendet: "+fileChannel.position()/1024 +"/"+fileChannel.size()/1024+" kybte");
System.out.println(fileChannel.position()/1024 +"/"+fileChannel.size()/1024+" kybte");
				}
				System.out.println("$$$ Done!");
				oos.flush();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					fis.close();
					//os.close();
				//	sock.close();
					System.out.println("$$$ Stopped loop");
					stop = true;
				} catch (IOException e) {
					e.printStackTrace();
				}

			//	System.out.println("$$$ Socket closed");
			}
			System.out.println("senden fertig!");
			frame.hide();
		//}
			
		//	myFile.
			
	}
}
