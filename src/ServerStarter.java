import java.net.InetAddress;
import java.net.UnknownHostException;



public class ServerStarter {

    
    
    static String serverip=null;
    /**
     * @param args
     */
    public static void main(String[] args) {
	
	try {
	    serverip=InetAddress.getLocalHost().getHostAddress();
	} catch (UnknownHostException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	// TODO Auto-generated method stub
	MultiThreadedServer server = new MultiThreadedServer(7866);
	new Thread(server).start();
System.out.println("Server gestartet. Server IP ist: "+ serverip);
System.out.println("Warte auf Clients...");


	/*try {
	    Thread.sleep(1150 * 1000);
	} catch (InterruptedException e) {
	    e.printStackTrace();
	}
	System.out.println("Stopping Server");
	server.stop();*/
    }

}
