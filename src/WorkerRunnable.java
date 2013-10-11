


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**

 */
public class WorkerRunnable implements Runnable{

    protected Socket clientSocket = null;
    protected String serverText   = null;
    static Message messageIGot;
    static StartData startdata;
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 7866;
    static String message;
    public static ArrayList<Message> receivedMessageList= new ArrayList<Message>();
    ArrayList<Message> messageListChecken= new ArrayList<Message>(); 
    boolean clientAnwesend=true;
    InetAddress clientIP;
    int clientPort;
    public String user;
    boolean gecheckt;
    int genutztegecheckt=1;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
        this.clientIP= clientSocket.getInetAddress() ;
        this.clientPort=clientSocket.getPort();
    }

    public void run() {
	System.out.println("*** Anmeldung Client: ["+clientSocket.getInetAddress() + " |Port:" + clientSocket.getPort() +"]" );
	ObjectInputStream ois = null;
	ObjectOutputStream oos = null;
	try {
		ois = new ObjectInputStream(clientSocket.getInputStream());
		
		
		if(clientAnwesend==true){
			WorkerRunnableRead serverReaderThread = new WorkerRunnableRead(clientSocket,ois,this);
			serverReaderThread.setName("1A serverReaderThread");
			serverReaderThread.start();
		}
		
		
		oos = new ObjectOutputStream(clientSocket.getOutputStream());
	    } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
	
	
        while(clientAnwesend==true){
            
            try{   
 // IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
        	messageListChecken.clear();
        	messageListChecken.addAll(MultiThreadedServer.messageList);
        	int index=-1;
 for (Message m : messageListChecken){
   index++;
     if (m.to.equalsIgnoreCase(this.user)){
	
	//     System.out.println("An: ("+m.to+ ") Derzeitiger Client:(" +this.user+")");
	     oos.writeObject(m);
	     System.out.println("<>< Message weitergeleitet!");
	     gecheckt=true;
	     break;
	     // MultiThreadedServer.messageList.remove(m); //SINNVOLLER!
      //   }
       
     }     
     
  
     
     
 }
 if(gecheckt==true){
     MultiThreadedServer.messageList.remove(index);
     gecheckt=false;
 }
 
 // IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
 
 
  }catch(Exception e){
      System.out.println("!!! Abmeldung Client: [" + clientIP + " |Port:"+clientPort + "] - schließe Thread; Grund: "+e.getMessage());
    //  e.printStackTrace();
      //System.exit(0);
      clientAnwesend=false;

  }
            
        
           try {
	    Thread.sleep(15);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
        }
	
	
    }
}