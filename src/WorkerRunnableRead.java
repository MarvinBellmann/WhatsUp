import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class WorkerRunnableRead extends Thread {

    Socket clientSocket = null;
    ObjectInputStream ois = null;
    static Message messageIGot;
    static StartData startdata;
    public String user;
    boolean clientAnwesend=true;
    WorkerRunnable w;

	public void run() {
		while (clientAnwesend==true) {
			
			try {
			    Empfange();
				Thread.sleep(100);
			} catch (Exception e) {
			    e.printStackTrace();
			    clientAnwesend=false;
			}

		}
	}

	WorkerRunnableRead(Socket socket, ObjectInputStream ois, WorkerRunnable w) {
		this.clientSocket = socket;
		this.ois = ois;
		this.w=w;

	}

	public void Empfange() throws ClassNotFoundException, IOException {
	   	// IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	             //  System.out.println("X<< Warte auf Message");
		       Object obj =  ois.readObject();
		     //  System.out.println("<<< Message erhalten");
	                          
	               if (obj instanceof Message)
	               {
	                   System.out.print("<<< Nachricht von Cl: ["+clientSocket.getInetAddress() + " |Port:" + clientSocket.getPort()+"] "  );
	              	   messageIGot = (Message) obj;	              	
	                   MultiThreadedServer.messageList.add(messageIGot);	              	
	                   System.out.println(messageIGot.toString());
	              	//Thread.sleep(15);
	               }
	               if (obj instanceof StartData)
	               {
	                   startdata = (StartData) obj;
	                   this.user=startdata.user;
	                   w.user=startdata.user;
	                   System.out.println("*** Client nennt sich: " + this.user);
	               }
	               
       	  // IN PROGRESS !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	}

}
