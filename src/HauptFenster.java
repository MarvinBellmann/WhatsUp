import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

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

	static String username = "B"; //�ndere hier
	private static int maxWidth = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getMaximumWindowBounds().width;
	private static int maxHeight = GraphicsEnvironment
			.getLocalGraphicsEnvironment().getMaximumWindowBounds().height;
	private Border raisedetched = BorderFactory.createEtchedBorder(
		EtchedBorder.RAISED, Color.darkGray, Color.lightGray);
	
	static JFrame frame;
	public static int messagesLeft = 10;
	private static int width = 250;
	private static int height = 600;
	private static int border = 5;
	private JTable table;
	private JTextField txtSuche;
	public static JLabel statuslabel;
	//public static ChatFenster chatFenster;
	    public static ArrayList<ChatFenster> ChatFensterList= new ArrayList<ChatFenster>(); 

	static String serverIP = "192.168.0.73";//"localhost"; //SERVER IP!

	

	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
			   /* String test;
			    String result;
			    test = "abc(abc)abc";
			    result = test.replaceAll("[()]", "");

			    System.out.println(result);*/
			    
		                
		                
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

	/**
	 * Create the application.
	 */
	public HauptFenster() {
		initialize();
	}
	
	public static void Chatparser(String str, String text){
	  //  chatFenster.txtPanel.setText("juhuuu");
	  //  chatFenster.panel_1.repaint();
	    for(ChatFenster c: ChatFensterList){
		if(c.nameGespraech.equalsIgnoreCase(str)){
		    c.txtPanel.setText(text);
			  //  chatFenster.panel_1.repaint();
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
		frame.setBounds(maxWidth - width - 50, (maxHeight - height) / 2, width,
				height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		table = new JTable();
		table.setBackground(Color.lightGray);
		table.setBounds(border, 130 + (border * 4), width - 15, 400);
		table.setBorder(raisedetched);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {

			    ChatFenster c = new ChatFenster();
			   
			    c.domain((String) table.getValueAt(table.getSelectedRow(), 2));
			    ChatFensterList.add(c);
				//ChatFenster chatFenster = new ChatFenster();
				//chatFenster.domain((String) table.getValueAt(
				//		table.getSelectedRow(), 2));
				System.out.println("Chatfenster mit "
						+ table.getValueAt(table.getSelectedRow(), 2)
						+ " ge�ffnet.");
			}
		});
		table.setModel(new DefaultTableModel(new Object[][] {
				{ "Bild1", "Online", "A" },
				{ "Bild2", "Online", "B" },
				{ "Bild3", "Offline", "C" }, }, new String[] {
				"Profilbild", "Status", "Name" }));
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

		JLabel lblSvenole = new JLabel(this.username);
		lblSvenole.setHorizontalAlignment(SwingConstants.LEFT);
		lblSvenole.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSvenole.setBounds((border * 2) + (69 + border), border, 90, 14);
		panelProfil.add(lblSvenole);

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

		JLabel lblKontakte = new JLabel("Kontakte");
		lblKontakte.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblKontakte.setHorizontalAlignment(SwingConstants.CENTER);
		lblKontakte.setBounds(border, border, width - (border * 5), 14);
		panelKontakt.add(lblKontakte);

		txtSuche = new JTextField();
		txtSuche.setForeground(Color.LIGHT_GRAY);
		txtSuche.setText("Suche");
		txtSuche.setBorder(raisedetched);
		txtSuche.setHorizontalAlignment(SwingConstants.CENTER);
		txtSuche.setBounds(border, (border * 2) + 14, width - (border * 5), 20);
		panelKontakt.add(txtSuche);

		frame.getContentPane().add(panelKontakt);
		frame.add(new Gradients(Color.green.darker(), Color.green, width,
				height));
		
		
		//Chatparser("A");
	}
}
