package Client;
import java.awt.Color;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import SendData.Message;

public class ClientRead extends Thread {

    Socket socket = null;
    ObjectInputStream ois = null;
    static Message messageIGot;
    boolean ServerAnwesend=true;

	public void run() {
		while (ServerAnwesend==true) {
			
		    
			try {
			    Empfange();
				Thread.sleep(100);
			} catch (Exception e) {
			    ServerAnwesend=false;
			  // e.printStackTrace();
			    System.out.println("Client read Problem. " + e.getMessage());
			    HauptFenster.statuslabel.setText("Offline");
				  HauptFenster.statuslabel.setForeground(Color.RED);
				  HauptFenster. StatusTabelleServerLost();
				  for(ChatFenster CF: HauptFenster.ChatFensterList){
					    CF.UpdateStatusServerLost();
					    }
			}

		}
	}

	ClientRead(Socket socket, ObjectInputStream ois) {
		this.socket = socket;
		this.ois = ois;

	}

	public void Empfange() throws ClassNotFoundException, IOException  {
	   	// IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	    	   
	    	   //  System.out.println("X<< Warte auf Message");
	    
	    
	    
	
	    
	    
	    
	              Object obj;
		    //try {
			obj = ois.readObject();
		   
	           //   System.out.println("<<< Message erhalten");
	               
	               
	            //   if(socket!=null){
	           	
	       	  if (obj instanceof Message)
	                 {
	               	// Cast object zur message
	             	messageIGot = (Message) obj;      	
	             	 System.out.println("### Message vom Server: " + messageIGot.toString());
	             	
	             	 
	             	//try {
	             	 if(messageIGot.from.equals("KontaktDBAntwort")){
	             	     
	             		 HauptFenster.KontaktListeUpdater(messageIGot.text,messageIGot.typ);
	             	    
	             	 }else{
	             	 
	             	 
	             	 boolean checkopen=true;
	             	 for(ChatFenster CF: HauptFenster.ChatFensterList){
    	             	 	if(CF.nameGespraech.equals(messageIGot.from))
    	             	 	{
    	             	 	   checkopen=false;
    	             	 	   break;
    	             	 	} 
    	             	     	             	
	                 }
	             	 
	             	 if(checkopen==true)
    	             	 {
	             	     String statusfrom="";
	             	     String fromfrom="";
	             	     
	             	     for (int row = 0; row <= HauptFenster.table.getRowCount() - 1; row++) {
	             		fromfrom=(String)HauptFenster.table.getValueAt(row, 2);
	             		if(fromfrom.equalsIgnoreCase(messageIGot.from)){
	             		statusfrom=(String)HauptFenster.table.getValueAt(row, 1);
	             		    break;
	             		    
	             		}
	             	     }
	             	     
	             	     if(statusfrom=="" && messageIGot.from.equalsIgnoreCase("ServerDB")){statusfrom="Online";}
	             	 
	             	     
    	             	     ChatFenster c = new ChatFenster(messageIGot.from,statusfrom);			    
    	             	     HauptFenster.ChatFensterList.add(c);  
    	             	System.out.println("### Chatfenster mit "+ messageIGot.from+ " geöffnet.");
    	             	 }
	             	
	             	 HauptFenster.Chatparser(messageIGot.from,messageIGot.toText());
	             	 }
	             	 
	             	 
	             	/* }catch(ArrayIndexOutOfBoundsException e){
	             		 System.out.println("Tableleerungsproblem: "+e.getMessage());
	             	     }*/
	             	 
	             	 
	             	 }
	       //        }
	          // }catch(Exception e1){
	    	//   e1.printStackTrace();
	    	//   System.out.println("read problem");
	         //  }
		    /*} catch (ClassNotFoundException | IOException |ArrayIndexOutOfBoundsException e) {
			
			System.out.println(e.getMessage());
		    }*/
	    	      
    	 
    	   
       	  // IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

}
