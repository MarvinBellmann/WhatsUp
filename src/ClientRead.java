import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientRead extends Thread {

    Socket socket = null;
    ObjectInputStream ois = null;
    static Message messageIGot;

	public void run() {
		while (true) {
			
			try {
			    Empfange();
				Thread.sleep(100);
			} catch (Exception e) {
			    e.printStackTrace();
			}

		}
	}

	ClientRead(Socket socket, ObjectInputStream ois) {
		this.socket = socket;
		this.ois = ois;

	}

	public void Empfange() throws ClassNotFoundException, IOException {
	   	// IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	    	   
           
	    	      // for(;;) System.out.println(“Stop the world!”);
	    	       
	    	       
	    	       
	    	  // try{ 
	    	       System.out.println("X<< Warte auf Message");
	              Object obj =  ois.readObject();
	               System.out.println("<<< Message erhalten");
	               
	               
	               if(socket!=null){
	           	
	       	  if (obj instanceof Message)
	                 {
	               	// Cast object zur message
	             	messageIGot = (Message) obj;      	
	             	 System.out.println("Message vom Server: " + messageIGot.toString());
	            	}
	               }
	          // }catch(Exception e1){
	    	//   e1.printStackTrace();
	    	//   System.out.println("read problem");
	         //  }
	    	   
	    	  

	    	       

	    	      
    	 
    	   
       	  // IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

}
