import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
 

public class Client{
    
 static String serverIP;   
 static Message messageIGot;
 public static ArrayList<Message> messageList= new ArrayList<Message>(); 
 
 	public static void send(String From, String To, String Text){
	  //  System.out.println("From: "+From+" |To: " + To + " |message: " + Text);
	   // Message message = new Message(From,To,Text);
	    messageList.add(new Message(From,To,Text));
	    
	    System.out.println("message added " + Text + " size: " + messageList.size());
	}

    public Client(String serverIP) {
	this.serverIP = serverIP;
	
	System.out.println("runnt");
	    try {
		mach();
	   } catch ( ClassNotFoundException | IOException | InterruptedException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	    }
    }
    
    public static void mach() throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
       System.out.println("verbinde an: " + InetAddress.getByName(serverIP));
	InetAddress host = InetAddress.getByName(serverIP);//InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
       while(true){//for(int i=0;i<1500000;i++){
            //establish socket connection to server
            socket = new Socket(host.getHostName(), 7866);
            
            socket.setTcpNoDelay(true);
            
            ////////////////////////////////////////          
            //write to socket using ObjectOutputStream
           
           
          
        	 oos = new ObjectOutputStream(socket.getOutputStream());
               // System.out.println("message send");
        	  if(messageList.size()>0){
        	  //    System.out.println(messageList.size());
        	      oos.writeObject(messageList.get(messageList.size()-1));
        	  
        	// oos.writeObject(new Message("Test1","Test2","Testcounter"+HauptFenster.messagesLeft)); //Hauptfenstermessageliste einpflegen!!!!!!!!!!
              //  oos.writeObject("nerv");
                System.out.println("message send");
                messageList.remove(messageList.size()-1);
        	  }
              // 
          //          }else{
         //       	System.out.println("noch nichts");
                 //   }
          //
            
       ////////////////////////////////////////     
           
            //read the server response message            
         /*   try{
            ois = new ObjectInputStream(socket.getInputStream());
            Object obj =  ois.readObject();
           
            if (obj instanceof Message)
            {
          	// Cast object zur message
        	messageIGot = (Message) obj;      	
        	 System.out.println("Message vom Server: " + messageIGot.text);
       		}
            
                        
            //////////////////////////////////////////////////
            //close resources
            ois.close();       
            
            }
            catch(Exception e){
        	e.printStackTrace();
        	//System.out.println("keine neue nachricht");
            }*/
            oos.close();
            Thread.sleep(10);//100
        }
    }
    
    
  

}
