import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;


public class AnmeldeFenster {

    private JPanel contentPane;
    private JTextField txtName;
    private JTextField textPW;
    private JTextField textServer;
    private JFrame frame;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
	EventQueue.invokeLater(new Runnable() {
	    public void run() {
		try {
		    AnmeldeFenster frame = new AnmeldeFenster();
		    
		} catch (Exception e) {
		    e.printStackTrace();
		}
	    }
	});
    }

    /**
     * Create the frame.
     */
    public AnmeldeFenster() {
	frame = new JFrame();
	frame.setResizable(false);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setBounds(frame.getGraphicsConfiguration().getBounds().width/2 - 120, 100, 254, 268);
	frame.setVisible(true);
	
	contentPane = new JPanel();
	contentPane.setBackground(new Color(0, 255, 127));
	contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
	frame.setContentPane(contentPane);
	contentPane.setLayout(null);
	
	JLabel lblWhatsup = new JLabel("Whatsup!");
	lblWhatsup.setHorizontalAlignment(SwingConstants.CENTER);
	lblWhatsup.setFont(new Font("Tahoma", Font.BOLD, 16));
	lblWhatsup.setBounds(10, 0, 228, 32);
	contentPane.add(lblWhatsup);
	
	JLabel lblAnmeldung = new JLabel("Anmeldung");
	lblAnmeldung.setHorizontalAlignment(SwingConstants.CENTER);
	lblAnmeldung.setFont(new Font("Tahoma", Font.BOLD, 16));
	lblAnmeldung.setBounds(10, 28, 228, 20);
	contentPane.add(lblAnmeldung);
	
	JLabel lblName = new JLabel("Name");
	lblName.setHorizontalAlignment(SwingConstants.LEFT);
	lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
	lblName.setBounds(10, 59, 213, 20);
	contentPane.add(lblName);
	
	JLabel lblPasswort = new JLabel("Passwort");
	lblPasswort.setHorizontalAlignment(SwingConstants.LEFT);
	lblPasswort.setFont(new Font("Tahoma", Font.PLAIN, 14));
	lblPasswort.setBounds(10, 100, 213, 20);
	contentPane.add(lblPasswort);
	
	JLabel lblServerip = new JLabel("Server IP");
	lblServerip.setHorizontalAlignment(SwingConstants.LEFT);
	lblServerip.setFont(new Font("Tahoma", Font.PLAIN, 14));
	lblServerip.setBounds(10, 142, 213, 20);
	contentPane.add(lblServerip);
	
	txtName = new JTextField();
	txtName.setText("A");
	txtName.setBounds(10, 78, 228, 20);
	contentPane.add(txtName);
	txtName.setColumns(10);
	
	textPW = new JTextField();
	textPW.setText("1234");
	textPW.setColumns(10);
	textPW.setBounds(10, 120, 228, 20);
	contentPane.add(textPW);
	
	textServer = new JTextField();
	textServer.setText("localhost");
	textServer.setColumns(10);
	textServer.setBounds(10, 161, 151, 20);
	contentPane.add(textServer);
	
	JButton btnAnmelden = new JButton("Anmelden");
	btnAnmelden.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		    //Anmeldung
		    
		  // HauptFenster window = new HauptFenster(txtName.getText(),textPW.getText(),textServer.getText());
		   
		    EventQueue.invokeLater(new Runnable() {
			public void run() {
			
			    
		                
		                
				try {
				    HauptFenster window = new HauptFenster(txtName.getText(),textPW.getText(),textServer.getText());
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		    frame.hide();
		}
	});
	btnAnmelden.setFont(new Font("Tahoma", Font.BOLD, 12));
	btnAnmelden.setBounds(10, 195, 106, 23);
	contentPane.add(btnAnmelden);
	
	JButton btnRegistrieren = new JButton("Registrieren");
	btnRegistrieren.setFont(new Font("Tahoma", Font.BOLD, 12));
	btnRegistrieren.setBounds(117, 195, 121, 23);
	contentPane.add(btnRegistrieren);
	
	JButton btnPing = new JButton("Ping");
	btnPing.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		    
		    boolean pingchecker = false;
		    try{
			   //pingchecker= ping(textServer.getText());
			  pingchecker = (InetAddress.getByName(textServer.getText()).isReachable(5000));
		    	}catch(Exception e){
				//e.printStackTrace();
		    	    System.out.println("Konnte nicht anpingen!");
			}
		
		    JOptionPane.showMessageDialog(null, "Ping erfolgreich: "+pingchecker);
		    
		   
		    }
	});
	btnPing.setBounds(166, 160, 72, 23);
	contentPane.add(btnPing);
	
	JLabel lblYourIp = new JLabel("Your IP: ");
	lblYourIp.setHorizontalAlignment(SwingConstants.LEFT);
	lblYourIp.setFont(new Font("Tahoma", Font.PLAIN, 10));
	lblYourIp.setBounds(10, 220, 228, 20);
	contentPane.add(lblYourIp);
	
	
	
	String yourip =null;
	try {
	   yourip=InetAddress.getLocalHost().getHostAddress();
	} catch (UnknownHostException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}
	
	
	lblYourIp.setText(lblYourIp.getText()+yourip);
    }
    
    
   /* private static boolean ping(String host) throws IOException, InterruptedException {
	    boolean isWindows = System.getProperty("os.name").toLowerCase().contains("win");

	    ProcessBuilder processBuilder = new ProcessBuilder("ping", isWindows? "-n" : "-c", "1", host);
	    Process proc = processBuilder.start();

	    int returnVal = proc.waitFor();
	    return returnVal == 0;
	    
	}*/
    
}
