package Server;

import java.awt.Color;
import java.awt.Font;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.text.DefaultCaret;

public class ServerStart {
    
    
    static String serverip = null;
    static int port = 7866;
    private static JPanel contentPane;
    static JTextPane textPane;
    static int  counter=0;
    
    
   public static void main(String[] args) {
    

    
	JFrame frmWakengerServerConsole = new JFrame();
	frmWakengerServerConsole.setTitle("WAK-enger! Server");
	frmWakengerServerConsole.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frmWakengerServerConsole.setBounds(20, 20, 544, 482);
	contentPane = new JPanel();
	contentPane.setBorder(new LineBorder(new Color(0, 0, 0)));
	frmWakengerServerConsole.setContentPane(contentPane);
	
	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setViewportBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
	scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
	scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
	
	textPane = new JTextPane(){
	    public boolean getScrollableTracksViewportWidth() {
		return getUI().getPreferredSize(this).width <= getParent()
				.getSize().width;
	}
	};
	scrollPane.setViewportView(textPane);
	textPane.setBackground(Color.WHITE);
	textPane.setText("");
	DefaultCaret caret = (DefaultCaret)textPane.getCaret();
	caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
	contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.X_AXIS));
	contentPane.add(scrollPane);
	
	JLabel lblNewLabel = new JLabel("WAK-enger! Server Console Output:");
	lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
	lblNewLabel.setBackground(Color.LIGHT_GRAY);
	lblNewLabel.setForeground(Color.BLACK);
	scrollPane.setColumnHeaderView(lblNewLabel);
	lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
	

	try {
		serverip = InetAddress.getLocalHost().getHostAddress();
	} catch (UnknownHostException e) {
		e.printStackTrace();
	}
	MultiThreadedServer server = new MultiThreadedServer(port);
	new Thread(server).start();
	SystemWriteLogln("Server gestartet. Server IP ist: [" + serverip
			+ " |Port:" + port + "]");
	SystemWriteLogln("Warte auf Clients...");
		    frmWakengerServerConsole.setVisible(true);
		
    }
   
   
   
   public static void SystemWriteLogln(String entry){
       counter++;
       System.out.println(entry);
       textPane.setText(textPane.getText()+entry +"\n"); //+"["+counter+"] "
      
   }
   
   public static void SystemWriteLog(String entry){
       counter++;
       System.out.print(entry);
       textPane.setText(textPane.getText()+entry);
      
       //textPane.add
   }
   
   public static void SystemWriteErrorLogln(Exception e){
       counter++;
       System.err.println(e);
       textPane.setText(textPane.getText()+"ERROOOOORRRR" +"\n");//+"["+counter+"] "
   }
   
   
   
}

















