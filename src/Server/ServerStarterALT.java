package Server;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerStarterALT {
	static String serverip = null;
	static int port = 7866;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

	
		
		
		/*JTextPane txtField = new JTextPane(){
			public boolean getScrollableTracksViewportWidth() {
				return getUI().getPreferredSize(this).width <= getParent()
						.getSize().width;
			}
		};
		txtField.setFont(new Font("Miriam", Font.PLAIN, 12));
		txtField.setContentType("text/html");
		txtField.setBounds(10, 10, 110, 160);
		//txtField.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		frame.getContentPane().add(txtField);*/
		
		
		
	    
	    
		try {
			serverip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		MultiThreadedServer server = new MultiThreadedServer(port);
		new Thread(server).start();
		System.out.println("Server gestartet. Server IP ist: [" + serverip
				+ " |Port:" + port + "]");
		System.out.println("Warte auf Clients...");
	}

}
