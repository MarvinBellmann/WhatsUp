package Client;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.channels.FileChannel;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import SendData.ByteData;
import SendData.Message;

public class ClientRead extends Thread {

	Socket socket = null;
	ObjectInputStream ois = null;
	static Message messageIGot;
	boolean ServerAnwesend = true;
	InputStream is = null;
	FileOutputStream fos = null;
	File ff = null;
	static String desktopPath = null;
	boolean byteUebertragungsBeschuetzer = false;

	public void run() {
		try {
			desktopPath = System.getProperty("user.home") + "/Desktop";
			desktopPath = desktopPath.replace("\\", "/");
		} catch (Exception e) {
			System.out.println("Exception caught =" + e.getMessage());
		}
		while (ServerAnwesend == true) {

			try {
				recieve();
				Thread.sleep(100);
			} catch (Exception e) {
				ServerAnwesend = false;
				System.out.println("Client read Problem. " + e.getMessage());
				MainFrame.statusLbl.setText("Offline");
				MainFrame.statusLbl.setForeground(Color.RED);
				MainFrame.statusTableServerLost();
				for (ChatFrame CF : MainFrame.chatFrameList) {
					CF.updateStatusServerLost();
				}
			}

		}
	}

	ClientRead(Socket socket, ObjectInputStream ois) {
		this.socket = socket;
		this.ois = ois;

	}

	public void recieve() throws ClassNotFoundException, IOException {

		if (MainFrame.byteSendingProtector == false) {
			Object obj = ois.readObject();

			if (obj instanceof Message) {
				messageIGot = (Message) obj;
				System.out.println("### Message vom Server: "
						+ messageIGot.toString());

				if (messageIGot.from.equals("KontaktDBAntwort")) {
					MainFrame.kontaktListeUpdater(messageIGot.text,
							messageIGot.typ);
				} else {

					if (messageIGot.from.equals("PictureDBAntwort")) {
						MainFrame.pictureUpdater(messageIGot.text);
						// HauptFenster.PictureUpdater(picID);

					} else {

						if (messageIGot.from.equals("HinzufuegenDBAntwort")) {
							System.out.println(messageIGot.text);
							if (messageIGot.text.contains("Keine Eintr") == false) {
								MainFrame.addContact();
							} else {
								JOptionPane
										.showMessageDialog(null,
												"Benutzer wurde in der Datenbank nicht gefunden. Verschrieben?");

							}
							// HauptFenster.PictureUpdater(picID);

						} else {

							if (messageIGot.from.equals("LoeschenDBAntwort")) {
								System.out.println(messageIGot.text);
								if (messageIGot.text.contains("Keine Eintr") == false) {
									MainFrame.removeContact();
								} else {
									JOptionPane
											.showMessageDialog(null,
													"Benutzer wurde in der Datenbank nicht gefunden. Verschrieben?");

								}
								// HauptFenster.PictureUpdater(picID);

							} else {
								
								//if(MainFrame.tableLoaded ==true){

								boolean checkopen = true;
								for (ChatFrame CF : MainFrame.chatFrameList) {
									if (CF.nameChat
											.equalsIgnoreCase(messageIGot.from)) {
										checkopen = false;
										break;
									}
								}

								if (checkopen == true) {
									String statusfrom = "";
									String fromfrom = "";

									for (int row = 0; row <= MainFrame.table
											.getRowCount() - 1; row++) {
										ContactCard card = (ContactCard) MainFrame.table
												.getValueAt(row, 0);
										fromfrom = card.getName();
										if (fromfrom
												.equalsIgnoreCase(messageIGot.from)) {
											statusfrom = card.getStatus();
											break;

										}
									}
									if (statusfrom == ""
											&& messageIGot.from
													.equalsIgnoreCase("ServerDB")) {
										statusfrom = "Online";
									}
									// ImageIcon i = new ImageIcon();
									ChatFrame c = new ChatFrame(
											messageIGot.from, statusfrom);
									MainFrame.chatFrameList.add(c);
									System.out.println("### Chatfenster mit "
											+ messageIGot.from + " geöffnet.");
								}
								MainFrame.chatparser(messageIGot.from,
										messageIGot.toText());

							
							//}
							
							
							
							}
						}
					}
				}
			}
			if (obj instanceof ByteData) {
				
				
				
				JFrame frame = new JFrame();
				frame.setResizable(false);
				frame.setBounds(MainFrame.ge.getMaximumWindowBounds().width/2-190,MainFrame.ge.getMaximumWindowBounds().height/2-100,380,130);//(10, 10, 210, 70);
				frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
				frame.setVisible(true);
				JLabel label_2 = new JLabel("Übertragen");
				label_2.setBounds(5, 10, 370, 30);
				label_2.setHorizontalAlignment( SwingConstants.CENTER );
				frame.getContentPane().add(label_2);
				frame.getContentPane().setLayout(null);
				
				JButton btnLogin = new JButton("Datei Öffnen");
				btnLogin.setEnabled(false);
				btnLogin.setBounds(20, 50, 330, 40);
				btnLogin.addActionListener(new ActionListener() {

					public void actionPerformed(ActionEvent arg0) {
					    	try{
					    	Desktop dt = Desktop.getDesktop();
						dt.open(ff);
					    	}catch(Exception e){
					    	    e.printStackTrace();
					    	}
						
					}
				});
				btnLogin.setFont(new Font("Tahoma", Font.BOLD, 12));
				frame.getContentPane().add(btnLogin);
				
				
				
				frame.setAlwaysOnTop(true);
				frame.toFront();
				frame.repaint();
				frame.setAlwaysOnTop(false);
				
				
				MainFrame.byteSendingProtector = true;
				ByteData bytedata = (ByteData) obj;
				System.out.println("1");
				byte[] mybytearray = new byte[1024];
				try {

					System.out.println("2");
					// is = clientSocket.getInputStream();

					String fileName = bytedata.dateiname.substring(
							bytedata.dateiname.lastIndexOf('/') + 1,
							bytedata.dateiname.length());

					System.out
							.println(desktopPath + "/" + "WAKenger_received_file_" + fileName);
					ff = new File(desktopPath + "/" + "WAKenger_received_file_" + fileName);// receivedData.png");
					ff.createNewFile();
					System.out.println("3");
					System.out.println("Write Received Data to: "
							+ ff.getAbsolutePath());

					fos = new FileOutputStream(ff);
					FileChannel fileChannel = fos.getChannel();

					// DecimalFormat df = new DecimalFormat("0.0000");
					int count;
					boolean uebertragen = false;
					// while ((count = ois.read(mybytearray)) >= 1023) {
					while (uebertragen == false) {
						count = ois.read(mybytearray);
						System.out.println("Data Transferred: "
								+ fileChannel.size() / 1024 + " KBytes / "
								+ (bytedata.bytes / 1024));
						
						
						label_2.setText("Empfangen von "+bytedata.from+": " 
								+ fileChannel.size() / 1024 + " KBytes / "
								+ (bytedata.bytes / 1024));
						
						// + df.format(fileChannel.size() / 1024.00 / 1024.00)
						// + " MByte");
						// System.out.println(ois.read(mybytearray));

						if (fileChannel.size() / 1024 == bytedata.bytes / 1024) {
							uebertragen = true;
						}
						fos.write(mybytearray, 0, count);
					}
					System.out.println("Wrote Received Data to: "
							+ ff.getAbsolutePath());
					
					
					label_2.setText("Empfangen von "+bytedata.from+": " 
							+ fileChannel.size() / 1024 + " KBytes / "
							+ (bytedata.bytes / 1024) + ". Erfolgreich!");
					
				} catch (Exception e) {
					System.out.println(e.getMessage());
					fos.close();
					// is.close();
					// sock.close();

				} finally {
					System.out.println("Done.");

					fos.close();
					// is.close();
					// sock.close();
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					//Desktop dt = Desktop.getDesktop();
					//dt.open(ff);
					btnLogin.setEnabled(true);
				}
				MainFrame.byteSendingProtector = false;
			}
		}
	}
}
