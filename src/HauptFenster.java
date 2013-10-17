import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;

public class HauptFenster {

	static String username = "A"; //ändere hier
	private static int maxWidth = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	private static int maxHeight = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	private Border raisedetched = BorderFactory.createEtchedBorder(
		EtchedBorder.RAISED, Color.darkGray, Color.lightGray);
	
	static JFrame frame;
	public static int messagesLeft = 10;
	private static int width = 250;
	private static int height = 587;
	private static int border = 5;
	private static JTable table;
	private JTextField txtSuche;
	public static JLabel statuslabel;
	int startX,startY;
	//public static ChatFenster chatFenster;
	    public static ArrayList<ChatFenster> ChatFensterList= new ArrayList<ChatFenster>(); 

	static String serverIP = "localhost";//"localhost"; //SERVER IP!

	

	

	/**
	 * Launch the application.
	 */
	/*
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			 
			    
		                
		                
				try {
					HauptFenster window = new HauptFenster();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

		// Server server = new Server();
		Client client = new Client(serverIP);
	}
*/
	/**
	 * Create the application.
	 */
	public HauptFenster(String user, String pw, String Server, int x, int y) {
	    this.username=user;
	    this.serverIP=Server;
	    startX=x;
	    startY=y;
	    initialize();
	    
	    
	    Client client = new Client(serverIP);
	    client.setName("1A clientThread");
    	    client.start();
		//frame.setVisible(true);
		
	}
	
	public HauptFenster() {
	   
	    initialize();    
		
	}
	
	public static void Chatparser(String sfrom, String text){
	  //  System.out.println("Chatparser kriegt: "+sfrom + " : " + text);
	    for(ChatFenster c: ChatFensterList){
		if(c.nameGespraech.equalsIgnoreCase(sfrom)){
		 //   System.out.println("Client:" + username +" Chatparserecho kriegt: "+sfrom + " füer fenster " + c.nameGespraech + " text: " + text);
		    if(c.txtPanel.getText().equals("")){
		       c.txtPanel.setText(text);
		   }
		   else{
		    c.txtPanel.setText(c.txtPanel.getText()+"\n"+text);
		    c.txtPanel.setCaretPosition(c.txtPanel.getDocument().getLength());
			  //  chatFenster.panel_1.repaint();
		   }
		    
		}
	    }
	}
	
	
	public static void Chatparserecho(String sto, String text){
 	    
	   // System.out.println("Chatparserecho kriegt: "+sto);
	    for(ChatFenster c: ChatFensterList){
		if(c.nameGespraech.equalsIgnoreCase(sto)){
		  //  System.out.println("Client:" + username +" Chatparserecho kriegt: "+sto + " füer fenster " + c.nameGespraech + " text: " + text);
		    if(c.txtPanel.getText().equals("")){
			       c.txtPanel.setText(text);
			   }
			   else{
			    c.txtPanel.setText(c.txtPanel.getText()+"\n"+text);
			    c.txtPanel.setCaretPosition(c.txtPanel.getDocument().getLength());
				  //  chatFenster.panel_1.repaint();
			   }
			  //  chatFenster.panel_1.repaint();
		}
	    }
	}
	
	
	public static void KontaktListeUpdater(String text, char typ){
	    
	    
	    //public static ArrayList<String> gesplittet= new ArrayList<String>(); 

	    
	    Pattern p = Pattern.compile( "[/.,]" );
	    
	    String[] gesplittet = p.split(text);
	    System.out.println(gesplittet);
	    
	    //text.spl
	    DefaultTableModel model = (DefaultTableModel) table.getModel();
	    int index=0;
	    if(username.equals("Admin")){
		index++;
		if(username.equals("Admin")){
			
		    if(typ=='u'){model.addRow(new Object[]{"Bild", "Online", "ServerDB"});}}
	    
	    
	    }
	    for(String s: gesplittet){
		
	if(typ=='u'){model.addRow(new Object[]{"Bild", "Unbekannt",s});}
	if(typ=='s'){
	   
	    model.setValueAt(s, index, 1);
	    index++;
	}
	
	    }
	
	
	    
	}
	
	public static void StatusChanger() throws InterruptedException{
	   
	Thread.sleep(1000);	    
	    statuslabel.setForeground(Color.GREEN);	    	    
	    statuslabel.setText("O");
	Thread.sleep(120);
	    statuslabel.setText("On");
	Thread.sleep(120);
	    statuslabel.setText("Onl");
	Thread.sleep(120);
	    statuslabel.setText("Onli");
	Thread.sleep(120);
	    statuslabel.setText("Onlin");
	Thread.sleep(120);
	    statuslabel.setText("Online");
       
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
	    
	    System.out.println("Anmeldungsversuch als: " + username);
	    
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(startX,startY, width,height); //frame.setBounds(maxWidth - width - 50, (maxHeight - height) / 2, width,height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		table = new JTable();
		table.setBackground(new Color(190, 190, 255));
		table.setBounds(border, 130 + (border * 4), width - 15, 400);
		table.setBorder(raisedetched);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {

			    boolean abbruch = false;
			    
			    for( ChatFenster CF : ChatFensterList){
				
				System.out.println(CF.nameGespraech + " " +((String) table.getValueAt(table.getSelectedRow(), 2)));
				
				if(CF.nameGespraech.equalsIgnoreCase((String) table.getValueAt(table.getSelectedRow(), 2))){
				    
				    abbruch=true;
				}
			    }
			    if(abbruch==false){
			    ChatFenster c = new ChatFenster((String) table.getValueAt(table.getSelectedRow(), 2));
			   
			   
			    ChatFensterList.add(c);
				//ChatFenster chatFenster = new ChatFenster();
				//chatFenster.domain((String) table.getValueAt(
				//		table.getSelectedRow(), 2));
				System.out.println("Chatfenster mit "
						+ table.getValueAt(table.getSelectedRow(), 2)
						+ " geöffnet.");
			    }
			}
		});
		/*table.setModel(new DefaultTableModel(new Object[][] {
				{ "BildDB", "Online", "ServerDB" },
				{ "BildAd", "Online", "Admin" },
				{ "Bild1", "Online", "A" },
				{ "Bild2", "Online", "B" },
				{ "Bild3", "Online", "C" }, }, new String[] {
				"Profilbild", "Status", "Name" }));*/
		table.setModel(new DefaultTableModel(new Object[][] {, }, new String[] {"Profilbild", "Status", "Name" }));
		frame.add(table);
		
		

		// ProfilPanel
		JPanel panelProfil = new JPanel();
		panelProfil.setBounds(border, border, width - 15, 80);
		panelProfil.setLayout(null);
		panelProfil.setOpaque(false);

		JLabel label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon(HauptFenster.class
				.getResource("/data/1.jpg")));
		label_2.setBounds(border, border, 69, 69);
		panelProfil.add(label_2);

		JLabel lblUsername = new JLabel(this.username);
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUsername.setForeground(Color.white);
		lblUsername.setBounds((border * 2) + (69 + border), border, 90, 14);
		panelProfil.add(lblUsername);

		statuslabel = new JLabel("Offline");
		statuslabel.setHorizontalAlignment(SwingConstants.LEFT);
		statuslabel.setForeground(Color.RED);
		statuslabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		statuslabel.setBounds((border * 2) + (69 + border), (border * 2) + 14, 90, 14);
		panelProfil.add(statuslabel);

		frame.getContentPane().add(panelProfil);

		// KontaktPanel
		JPanel panelKontakt = new JPanel();
		// panelKontakt.setBackground(Color.LIGHT_GRAY);
		// panelKontakt.setForeground(Color.ORANGE);
		panelKontakt.setBounds(border, 80 + (border * 2), width - 15, 50);
		panelKontakt.setLayout(null);
		panelKontakt.setOpaque(false);

		JLabel lblKontakte = new JLabel("Kontakte aktualisieren");
		lblKontakte.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblKontakte.setForeground(Color.white);
		lblKontakte.setHorizontalAlignment(SwingConstants.CENTER);
		lblKontakte.setBounds(border, border, width - (border * 5), 14);
		lblKontakte.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
			   
			    Client.sendSQL(new SQLData("SELECT status from user where username not like '"+ username+ "' order by username",'k',username));
			}
		});
		
		
		//MultiThreadedServer.sqlBefehlsListe.add(new SQLData("SELECT status from user order by username",'k',this.user));
		    
		
		
		
		
		
		panelKontakt.add(lblKontakte);

		txtSuche = new JTextField();
		txtSuche.setForeground(Color.LIGHT_GRAY);
		txtSuche.setText("Suche");
		txtSuche.setBorder(raisedetched);
		txtSuche.setHorizontalAlignment(SwingConstants.CENTER);
		txtSuche.setBounds(border, (border * 2) + 14, width - (border * 5), 20);
		panelKontakt.add(txtSuche);

		frame.getContentPane().add(panelKontakt);
		//frame.add(new Gradients(Color.green.darker(), Color.green, width,height));
		frame.add(new Gradients(new Color(27, 130, 165),new Color(204, 204, 255),  width,height));
		
		//frame.setVisible(true);
		//frame.repaint();
		
		
		
		
		
		//Chatparser("A");
	}
}
