


import java.io.IOException;
import java.io.ObjectInputStream;
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
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 7866;
    static String message;
    public static ArrayList<Message> receivedMessageList= new ArrayList<Message>();
    boolean clientAnwesend=true;
    InetAddress clientIP;
    int clientPort;

    public WorkerRunnable(Socket clientSocket, String serverText) {
        this.clientSocket = clientSocket;
        this.serverText   = serverText;
        this.clientIP= clientSocket.getInetAddress() ;
        this.clientPort=clientSocket.getPort();
    }

    public void run() {
	System.out.println("***Neue Client Verbindung steht: ["+clientSocket.getInetAddress() + " |Port:" + clientSocket.getPort() +"]" );
	ObjectInputStream ois=null;
	try {
		ois = new ObjectInputStream(clientSocket.getInputStream());
	    } catch (IOException e1) {
		// TODO Auto-generated catch block
		e1.printStackTrace();
	    }
        while(clientAnwesend==true){
            //creating socket and waiting for client connection
         //  Socket socket = server.accept();
         //   socket.setTcpNoDelay(true);
            //read from socket to ObjectInputStream object
            
            
            try{   
        	
        	
       
            //convert ObjectInputStream object to String
          
       
 Object obj =  ois.readObject();
            
            
 if (obj instanceof Message)
 {
     System.out.println("***Nachricht von: ["+clientSocket.getInetAddress() + " |Port:" + clientSocket.getPort()+"]"  );
	
 	// Cast object to a Vector
	messageIGot = (Message) obj;
	 receivedMessageList.add(messageIGot);
	//if(i%20==0){ HauptFenster.LinieLabel.setText("jooooo " +messageIGot.nextX);}
	System.out.println(messageIGot.toString());
	      	
	   
	
 }
 /*else
 {
     message = (String) ois.readObject();
     System.out.println("***Message Received: " + message);
     if(message.equalsIgnoreCase("exit")) break;
 }*/
  }catch(Exception e){
      System.out.println("***Client [" + clientIP + " |Port:"+clientPort + "] abgemeldet - schlieﬂe Thread; Grund: "+e.getMessage());
     //this.stop();
     // e.printStackTrace();
      //System.exit(0);
      clientAnwesend=false;
      //nichts bekommen
  }
            
            
            
       /*     
            //create ObjectOutputStream object
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            //write object to Socket
           // oos.writeObject(HauptFenster.spieler.move); //"Hi Client der Spieler befindet sich auf posX:"+
            oos.writeObject(new Message(HauptFenster.spieler));*/

            
           // System.out.println("***Message sent: Hi Client der Spieler befindet sich auf posX:"+HauptFenster.spieler.getX());
            // oos.writeObject("Hi Client deine Nachricht war:"+message);
            //close resources
    //    try{
          //  ois.close();
           // oos.close();
          //  socket.close();
     //   }catch(Exception e){
            
      //  }
            //terminate the server if client sends exit request
           try {
	    Thread.sleep(15);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
        }
	
	
      /*  try {
            InputStream input  = clientSocket.getInputStream();
            OutputStream output = clientSocket.getOutputStream();
            long time = System.currentTimeMillis();
            output.write(("HTTP/1.1 200 OK\n\nWorkerRunnable: " +
                    this.serverText + " - " +
                    time +
                    "").getBytes());
            output.close();
            input.close();
            System.out.println("Request processed: " + time);
        } catch (IOException e) {
            //report exception somewhere.
            e.printStackTrace();
        }*/
    }
}