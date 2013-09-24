import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
 


public class Server{// extends Thread{
    
    
    public static void main(String[] args) {
	try {
	    mach();
	} catch (ClassNotFoundException | IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }
    
    static Message messageIGot;
    //static ServerSocket variable
    private static ServerSocket server;
    //socket server port on which it will listen
    private static int port = 7866;
    static String message;
    
  /*  public void run(){
	try {
	    mach();
	} catch (ClassNotFoundException | IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
    }*/
    
    public static void mach() throws IOException, ClassNotFoundException{
 
        //create the socket server object
        server = new ServerSocket(port);
        //keep listens indefinitely until receives 'exit' call or program terminates
        System.out.println("Waiting for client request." + " Meine IP ist: "+ InetAddress.getLocalHost().getHostAddress());
        
        while(true){
            //creating socket and waiting for client connection
           Socket socket = server.accept();
            socket.setTcpNoDelay(true);
            //read from socket to ObjectInputStream object
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            //convert ObjectInputStream object to String
          
  try{          
 Object obj =  ois.readObject();
            
            
 if (obj instanceof Message)
 {
 	// Cast object to a Vector
	messageIGot = (Message) obj;
	//if(i%20==0){ HauptFenster.LinieLabel.setText("jooooo " +messageIGot.nextX);}
	System.out.println(messageIGot.toString());
	      	
	   
	
 }
 else
 {
     message = (String) ois.readObject();
     System.out.println("***Message Received: " + message);
     if(message.equalsIgnoreCase("exit")) break;
 }
  }catch(Exception e){
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
            ois.close();
           // oos.close();
            socket.close();
            //terminate the server if client sends exit request
           try {
	    Thread.sleep(5);
	} catch (InterruptedException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
        }
        System.out.println("Shutting down Socket server!!");
        //close the ServerSocket object
        server.close();
    }
   
     
}