import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

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
			    System.out.println("Client read Problem.");
			}

		}
	}

	ClientRead(Socket socket, ObjectInputStream ois) {
		this.socket = socket;
		this.ois = ois;

	}

	public void Empfange() throws ClassNotFoundException, IOException {
	   	// IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	    	   
	    	   //  System.out.println("X<< Warte auf Message");
	              Object obj =  ois.readObject();
	           //   System.out.println("<<< Message erhalten");
	               
	               
	            //   if(socket!=null){
	           	
	       	  if (obj instanceof Message)
	                 {
	               	// Cast object zur message
	             	messageIGot = (Message) obj;      	
	             	 System.out.println("Message vom Server: " + messageIGot.toString());
	             	 //HauptFenster.chatFenster.
	             	// HauptFenster.chatFenster.txtPanel.setText(messageIGot.toString());
	             	 HauptFenster.Chatparser(messageIGot.from,messageIGot.toText());
	            	}
	       //        }
	          // }catch(Exception e1){
	    	//   e1.printStackTrace();
	    	//   System.out.println("read problem");
	         //  }
	    	   
	    	  

	    	       

	    	      
    	 
    	   
       	  // IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

}
