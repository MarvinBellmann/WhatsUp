import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
 

public class Client extends Thread{
    
 static String serverIP;   
 String anmeldeuser;
 String anmeldepw;
// static Message messageIGot;
 public static ArrayList<Message> messageList= new ArrayList<Message>(); 
 static boolean verbindungscheck;
 static String erfolg = "Noch ungewiss";
 boolean nurAnmeldeClient=false;
 static boolean amLaufen = true;
 
 	public static void send(String From, String To, String Text){
	 
 	    //System.out.println(From + " " + To );
	    messageList.add(new Message(From,To,Text));
 	    
	    //System.out.println(">>> Message added");// " + Text + " size: " + messageList.size());
	}

    public Client(String serverIP){
	this.serverIP = serverIP;
	
	 /* try {
			mach();
		   } catch ( ClassNotFoundException | IOException | InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		    }*/
    }
    
    public Client(String serverIP, String user, String pw){
	this.serverIP = serverIP;
	this.anmeldeuser=user;
	this.anmeldepw=pw;
	nurAnmeldeClient=true;
	
    }
    
    public void run(){
	//System.out.println("runnt");
	    try {
		mach();
	   } catch ( ClassNotFoundException | IOException | InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
    }
    
    public static void mach() throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
       System.out.println("*** Verbinde an: " + InetAddress.getByName(serverIP));
	InetAddress host = InetAddress.getByName(serverIP);//InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
      //  Object obj =  null;
        
       while(amLaufen==true){
            //establish socket connection to server
	   
	   try{	       
	     
	       if(socket==null){   
		   socket = new Socket(host.getHostName(), 7866); 
		   socket.setTcpNoDelay(true); 
		   oos = new ObjectOutputStream(socket.getOutputStream());
		   ois = new ObjectInputStream(socket.getInputStream());
		   
		  // oos.writeObject(new StartData(HauptFenster.username)); 
		   }
	      
	       //ERFOLGREICH VERBINDUNG: DANN EINMAL FOLGENDES
            if(socket!=null && verbindungscheck==false){
        	System.out.println("*** Erfolgreich verbunden.");
        	HauptFenster.StatusChanger();
        	verbindungscheck=true;
        	oos.writeObject(new StartData(HauptFenster.username)); 
        	ClientRead clientReaderThread = new ClientRead(socket,ois);
        	clientReaderThread.setName("1A clientReadThread");
        	clientReaderThread.start();
        	
            }
            ////////////////////////////////////////  
    	
            
            //write to socket using ObjectOutputStream           
                 
        if(messageList.size()>0){
        	  //    System.out.println(messageList.size());
        	oos.writeObject(messageList.get(messageList.size()-1));        	
                System.out.println(">>> Message send to "+messageList.get(messageList.size()-1).to +" ("+messageList.get(messageList.size()-1).date+")");
                                  	 
            	 
            	 HauptFenster.Chatparserecho(messageList.get(messageList.size()-1).to,messageList.get(messageList.size()-1).toText());               
                                
                
                messageList.remove(messageList.size()-1);
         }
            
        //Hier auskommentiertes
        erfolg="Erfolg";
       }catch(Exception e){
	  erfolg="Fehschlag";
       System.out.println("Verbindungs Error! Server Offline? Neuversuch in 5 Sekunden. Error:" + e.getMessage());
      // e.printStackTrace();
       Thread.sleep(5000);
       }
	   
	   
            Thread.sleep(60);//100
        }
    }
     
  

}




