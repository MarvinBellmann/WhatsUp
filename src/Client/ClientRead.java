package Client;

import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import javax.swing.JOptionPane;

import SendData.Message;

public class ClientRead extends Thread {

    Socket socket = null;
    ObjectInputStream ois = null;
    static Message messageIGot;
    boolean ServerAnwesend = true;

    public void run() {
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
	Object obj = ois.readObject();

	if (obj instanceof Message) {
	    messageIGot = (Message) obj;
	    System.out.println("### Message vom Server: "
		    + messageIGot.toString());

	    if (messageIGot.from.equals("KontaktDBAntwort")) {
		HauptFenster.KontaktListeUpdater(messageIGot.text,
			messageIGot.typ);
	    } else {


		if (messageIGot.from.equals("PictureDBAntwort")){
		    HauptFenster.PictureUpdater(messageIGot.text);
		    //HauptFenster.PictureUpdater(picID);

		}else{
		    
		    if (messageIGot.from.equals("HinzufuegenDBAntwort")){
			System.out.println(messageIGot.text);
			    if(messageIGot.text.contains("Keine Eintr")==false){
				HauptFenster.KontaktHinzufuegen();
			    }else{
				JOptionPane.showMessageDialog(null,"Benutzer wurde in der Datenbank nicht gefunden. Verschrieben?");
	
			    }
			    //HauptFenster.PictureUpdater(picID);

			}else{
		   
		    
			    if (messageIGot.from.equals("LoeschenDBAntwort")){
					System.out.println(messageIGot.text);
					    if(messageIGot.text.contains("Keine Eintr")==false){
						HauptFenster.KontaktLoeschen();
					    }else{
						JOptionPane.showMessageDialog(null,"Benutzer wurde in der Datenbank nicht gefunden. Verschrieben?");
			
					    }
					    //HauptFenster.PictureUpdater(picID);

					}else{
		    
		    
		    
		    
		    boolean checkopen = true;
		    for (ChatFenster CF : HauptFenster.ChatFensterList) {
			if (CF.nameGespraech.equalsIgnoreCase(messageIGot.from)) {
			    checkopen = false;
			    break;
			}
		    }

		    if (checkopen == true) {
			String statusfrom = "";
			String fromfrom = "";

			for (int row = 0; row <= HauptFenster.table.getRowCount() - 1; row++) {
			    fromfrom = (String) HauptFenster.table.getValueAt(row,
				    2);
			    if (fromfrom.equalsIgnoreCase(messageIGot.from)) {
				statusfrom = (String) HauptFenster.table
					.getValueAt(row, 1);
				break;

			    }
			}
			if (statusfrom == ""
				&& messageIGot.from.equalsIgnoreCase("ServerDB")) {
			    statusfrom = "Online";
			}
			//ImageIcon i = new ImageIcon();
			ChatFenster c = new ChatFenster(messageIGot.from,
				statusfrom);
			HauptFenster.ChatFensterList.add(c);
			System.out.println("### Chatfenster mit "
				+ messageIGot.from + " geöffnet.");
		    }
		    HauptFenster.Chatparser(messageIGot.from, messageIGot.toText());




		}
			}
		}
	    }
	}
    }
    
    
    
    
    
    
    
    
    
    
}
