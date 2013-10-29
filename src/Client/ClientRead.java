package Client;

import java.awt.Color;
import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.nio.channels.FileChannel;

import javax.swing.JOptionPane;

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
				Empfange();
				Thread.sleep(100);
			} catch (Exception e) {
				ServerAnwesend = false;
				System.out.println("Client read Problem. " + e.getMessage());
				HauptFenster.statuslabel.setText("Offline");
				HauptFenster.statuslabel.setForeground(Color.RED);
				HauptFenster.StatusTabelleServerLost();
				for (ChatFenster CF : HauptFenster.ChatFensterList) {
					CF.UpdateStatusServerLost();
				}
			}

		}
	}

	ClientRead(Socket socket, ObjectInputStream ois) {
		this.socket = socket;
		this.ois = ois;

	}

	public void Empfange() throws ClassNotFoundException, IOException {

		if (HauptFenster.byteUebertragungsBeschuetzer == false) {
			Object obj = ois.readObject();

			if (obj instanceof Message) {
				messageIGot = (Message) obj;
				System.out.println("### Message vom Server: "
						+ messageIGot.toString());

				if (messageIGot.from.equals("KontaktDBAntwort")) {
					HauptFenster.KontaktListeUpdater(messageIGot.text,
							messageIGot.typ);
				} else {

					if (messageIGot.from.equals("PictureDBAntwort")) {
						HauptFenster.PictureUpdater(messageIGot.text);
						// HauptFenster.PictureUpdater(picID);

					} else {

						if (messageIGot.from.equals("HinzufuegenDBAntwort")) {
							System.out.println(messageIGot.text);
							if (messageIGot.text.contains("Keine Eintr") == false) {
								HauptFenster.KontaktHinzufuegen();
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
									HauptFenster.KontaktLoeschen();
								} else {
									JOptionPane
											.showMessageDialog(null,
													"Benutzer wurde in der Datenbank nicht gefunden. Verschrieben?");

								}
								// HauptFenster.PictureUpdater(picID);

							} else {

								boolean checkopen = true;
								for (ChatFenster CF : HauptFenster.ChatFensterList) {
									if (CF.nameGespraech
											.equalsIgnoreCase(messageIGot.from)) {
										checkopen = false;
										break;
									}
								}

								if (checkopen == true) {
									String statusfrom = "";
									String fromfrom = "";

									for (int row = 0; row <= HauptFenster.table
											.getRowCount() - 1; row++) {
										ContactCard card = (ContactCard) HauptFenster.table
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
									ChatFenster c = new ChatFenster(
											messageIGot.from, statusfrom);
									HauptFenster.ChatFensterList.add(c);
									System.out.println("### Chatfenster mit "
											+ messageIGot.from + " geöffnet.");
								}
								HauptFenster.Chatparser(messageIGot.from,
										messageIGot.toText());

							}
						}
					}
				}
			}
			if (obj instanceof ByteData) {
				HauptFenster.byteUebertragungsBeschuetzer = true;
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
							.println(desktopPath + "/" + "client_" + fileName);
					ff = new File(desktopPath + "/" + "client_" + fileName);// receivedData.png");
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
					Desktop dt = Desktop.getDesktop();
					dt.open(ff);
				}
				HauptFenster.byteUebertragungsBeschuetzer = false;
			}
		}
	}
}
