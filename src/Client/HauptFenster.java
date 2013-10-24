package Client;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import SendData.SQLData;

public class HauptFenster {

	static String username;
	private Border raisedetched = BorderFactory.createEtchedBorder(
			EtchedBorder.RAISED, Color.darkGray, Color.lightGray);

	static JFrame frame;
	static String desktopPath;
	public static int messagesLeft = 10;
	private static int width = 250;
	private static int height = 587;
	private static int border = 5;
	static JTable table;
	static ImageIcon i1;
	static ImageIcon i2;
	static ImageIcon i3;
	static ImageIcon i4;
	static ImageIcon i5;
	static ImageIcon i6;
	static ImageIcon i7;
	static ImageIcon i8;
	static ImageIcon i9;
	static ImageIcon iDB;
	public static JLabel label_2;
	JLabel lblUsername ;
	
	private JTextField txtSuche;
	public static JLabel statuslabel;
	int startX, startY;
	public static ArrayList<ChatFenster> ChatFensterList = new ArrayList<ChatFenster>();

	static String serverIP = "localhost";

	/**
	 * Create the application.
	 */
	public HauptFenster(String user, String pw, String server, int x, int y) {
		username = user;
		serverIP = server;
		startX = x;
		startY = y;
		initialize();

		Client client = new Client(serverIP);
		client.setName("1A clientThread");
		client.start();
	}

	public HauptFenster() {
		initialize();
	}

	public static void Chatparser(String sfrom, String text) {
		for (ChatFenster c : ChatFensterList) {
			if (c.nameGespraech.equalsIgnoreCase(sfrom)) {
				if (c.txtPanel.getText().equals("")) {
					c.txtPanel.setText(text);
					final ChatFenster cstatic = c;
					EventQueue.invokeLater(new Runnable() {

						@Override
						public void run() {
							cstatic.frame.setAlwaysOnTop(true);
							cstatic.frame.toFront();
							cstatic.frame.repaint();
							cstatic.frame.setAlwaysOnTop(false);
						}
					});
				} else {
					c.txtPanel.setText(c.txtPanel.getText() + "\n" + text);
					c.txtPanel.setCaretPosition(c.txtPanel.getDocument()
							.getLength());
				}
			}
		}
	}

	public static void Chatparserecho(String sto, String text) {
		for (ChatFenster c : ChatFensterList) {
			if (c.nameGespraech.equalsIgnoreCase(sto)) {
				if (c.txtPanel.getText().equals("")) {
					c.txtPanel.setText(text);
				} else {
					c.txtPanel.setText(c.txtPanel.getText() + "\n" + text);
					c.txtPanel.setCaretPosition(c.txtPanel.getDocument()
							.getLength());
				}
			}
		}
	}

	public static void KontaktListeUpdater(String text, char typ) {

		try {
			Pattern p = Pattern.compile("[/.,]");
			String[] gesplittet = p.split(text);
			DefaultTableModel model = (DefaultTableModel) table.getModel();

			int rowCount = model.getRowCount();

			if (rowCount > 0) {

				table.setModel(new DefaultTableModel(new Object[][] {,},
						new String[] { "Profilbild", "Status", "Name" }));
				model = (DefaultTableModel) table.getModel();
				System.out.println("### Tablelleneinträge gelöscht!");
			}

			if (username.equals("Admin")) {
				model.addRow(new Object[] { iDB, "Online", "ServerDB" });
			}

			for (int i = 0; i < gesplittet.length; i = i + 3) {
			    
			   // case 
			    ImageIcon avatarImage = new ImageIcon();
				    switch(gesplittet[i + 2]){ 
				        case "1": 
				          //  System.out.println("pic1"); 
				            avatarImage =i1;
				            break; 
				        case "2": 
				          //  System.out.println("pic2"); 
				            avatarImage =i2;
				            break; 
				        case "3": 
				           // System.out.println("pic3"); 
				            avatarImage =i3;
				            break; 
				        case "4": 
				          //  System.out.println("pic4"); 
				            avatarImage =i4;
				            break; 
				        case "5": 
				           // System.out.println("pic5"); 
				            avatarImage =i5;
				            break; 
				        case "6": 
				          //  System.out.println("pic6"); 
				            avatarImage =i6;
				            break; 
				        case "7": 
				          //  System.out.println("pic7"); 
				            avatarImage =i7;
				            break; 
				        case "8": 
				          //  System.out.println("pic8"); 
				            avatarImage =i8;
				            break; 
				        case "9": 
				          //  System.out.println("pic9"); 
				            avatarImage =i9;
				            break; 
				        default: 
				          //  System.out.println("pic1"); 
				            avatarImage =i1;
				        } 
				    
				    
				model.addRow(new Object[] { avatarImage, gesplittet[i + 1],
						gesplittet[i] });
			}

			System.out
					.println("### Tablelleneinträge inkl neuer Stati geladen aus DB!");

			for (ChatFenster CF : ChatFensterList) {
				CF.UpdateStatus();
			}

			for (int row = 0; row < table.getRowCount(); row++) {
				int rowHeight = table.getRowHeight();
				int rowWidth = 50;

				for (int column = 0; column < table.getColumnCount(); column++) {
					Component comp = table.prepareRenderer(
							table.getCellRenderer(row, column), row, column);
					rowHeight = Math.max(rowHeight,
							comp.getPreferredSize().height);
					rowWidth = Math.max(rowHeight,
							comp.getPreferredSize().width);
				}

				table.setRowHeight(row, rowHeight);
				table.getColumnModel().getColumn(0).setPreferredWidth(rowWidth);
			}
			
			table.repaint();

		} catch (Exception e) {
			System.out
					.println("KontaktListeUpdater-problem: " + e.getMessage());
		}

	}

	public static void StatusTabelleServerLost() {

		DefaultTableModel model = (DefaultTableModel) table.getModel();
		int rowCount = model.getRowCount();
		for (int i = rowCount - 1; i >= 0; i--) {

			model.setValueAt("Verb.Abbruch", i, 1);

		}
	}

	public static void StatusChanger() throws InterruptedException {

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
	@SuppressWarnings("serial")
	private void initialize() {
		i1 = new ImageIcon(getClass().getResource("/data/1.jpg"));
		i2 = new ImageIcon(getClass().getResource("/data/2.png"));
		i3 = new ImageIcon(getClass().getResource("/data/3.jpg"));
		i4 = new ImageIcon(getClass().getResource("/data/4.jpg"));
		i5 = new ImageIcon(getClass().getResource("/data/5.jpg"));
		i6 = new ImageIcon(getClass().getResource("/data/6.jpg"));
		i7 = new ImageIcon(getClass().getResource("/data/7.jpg"));
		i8 = new ImageIcon(getClass().getResource("/data/8.png"));
		i9 = new ImageIcon(getClass().getResource("/data/9.jpg"));
		iDB = new ImageIcon(getClass().getResource("/data/database.png"));
		//i91 = new ImageIcon(getClass().getResource("/data/2.png"));
		
		
		
		desktopPath = System.getProperty("user.home") + "/Desktop";
		desktopPath = desktopPath.replace("\\", "/");
		System.out.println("*** Anmeldungsversuch als: " + username);

		frame = new JFrame() {
			// private void BringToFront() {
			// java.awt.EventQueue.invokeLater(new Runnable() {
			// @Override
			// public void run() {
			// if (frame != null) {
			// frame.toFront();
			// frame.repaint();
			// }
			// }
			// });
			// }
		};
		frame.setResizable(false);
		frame.setBounds(startX, startY, width, height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		DefaultTableModel model = new DefaultTableModel(new Object[][] {,},
				new String[] { "Profilbild", "Status", "Name" });

		table = new JTable(model) {

			public Class<?> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}

			public boolean isCellEditable(int x, int y) {
				return false;
			}
			
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column)
			{
				Component c = super.prepareRenderer(renderer, row, column);

				//  Color row based on a cell value

				if (!isRowSelected(row))
				{
					//c.setBackground(getBackground());
					int modelRow = convertRowIndexToModel(row);
					String type = (String)getModel().getValueAt(modelRow, 1);
					if ("Online".equals(type)){ c.setBackground(new Color(190, 250, 190)); c.setForeground(Color.GREEN.darker()); c.setFont(new Font("Miriam", Font.BOLD, 14));}
					if ("Offline".equals(type)) {c.setBackground(new Color(250, 190, 190));c.setForeground(Color.RED.darker()); c.setFont(new Font("Miriam", Font.PLAIN, 14));}
					//table.repaint();
				}

				return c;
			}

		};
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		table.setBounds(border, 130 + (border * 4), width - 15, 400);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {

				boolean abbruch = false;

				for (ChatFenster CF : ChatFensterList) {
					if (CF.nameGespraech.equalsIgnoreCase((String) table
							.getValueAt(table.getSelectedRow(), 2))) {
						abbruch = true;
					}
				}
				if (abbruch == false) {
					ChatFenster c = new ChatFenster((String) table.getValueAt(
							table.getSelectedRow(), 2), (String) table
							.getValueAt(table.getSelectedRow(), 1));

					ChatFensterList.add(c);
					System.out.println("### Chatfenster mit "
							+ table.getValueAt(table.getSelectedRow(), 2)
							+ " geöffnet.");
				}
			}
		});

		JScrollPane scrollPane = new JScrollPane(table);
		scrollPane.setBounds(border, 130 + (border * 4), width - 15, 400);
		scrollPane.setBorder(raisedetched);
		scrollPane.setBackground(new Color(190, 190, 255));

		frame.getContentPane().add(scrollPane);

		// ProfilPanel
		JPanel panelProfil = new JPanel();
		panelProfil.setBounds(border, border, width - 15, 80);
		panelProfil.setLayout(null);
		panelProfil.setOpaque(false);

		label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon(HauptFenster.class
				.getResource("/data/1.jpg")));
		label_2.setBounds(border, border, 69, 69);
		panelProfil.add(label_2);

		lblUsername = new JLabel(username);
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUsername.setForeground(Color.white);
		lblUsername.setBounds((border * 2) + (69 + border), border, 90, 14);

		panelProfil.add(lblUsername);

		statuslabel = new JLabel("Offline");
		statuslabel.setHorizontalAlignment(SwingConstants.LEFT);
		statuslabel.setForeground(Color.RED);
		statuslabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		statuslabel.setBounds((border * 2) + (69 + border), (border * 2) + 14,
				90, 14);
		panelProfil.add(statuslabel);

		frame.getContentPane().add(panelProfil);

		// KontaktPanel
		JPanel panelKontakt = new JPanel();
		panelKontakt.setBounds(border, 80 + (border * 2), width - 15, 50);
		panelKontakt.setLayout(null);
		panelKontakt.setOpaque(false);

		JLabel lblKontakte = new JLabel("Kontakte");
		lblKontakte.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblKontakte.setForeground(Color.white);
		lblKontakte.setHorizontalAlignment(SwingConstants.CENTER);
		lblKontakte.setBounds(border, border, width - (border * 5), 14);

		panelKontakt.add(lblKontakte);

		txtSuche = new JTextField();
		//txtSuche.setForeground(Color.LIGHT_GRAY);
		txtSuche.setText("");
		txtSuche.setBorder(raisedetched);
		txtSuche.setHorizontalAlignment(SwingConstants.CENTER);
		txtSuche.setBounds(border, (border * 2) + 14, width - (border * 30), 20);
		panelKontakt.add(txtSuche);
		
		
		JButton btnKontaktSuche = new JButton("Hinzufügen");
		btnKontaktSuche.setBounds(border*21, (border * 2) + 14, 120, 20);
		btnKontaktSuche.addActionListener(new ActionListener() {

		    @Override
		    public void actionPerformed(ActionEvent arg0) {
			
			
			if(txtSuche.getText().equalsIgnoreCase(lblUsername.getText())==false){
			 Client.sendSQL(new SQLData("UPDATE user set status='Online' where username like '"+username+"'",'i'));
			    
			   Client.sendSQL(new SQLData("INSERT IGNORE INTO contacts (username,contact) values ('"+ username+ "','"+txtSuche.getText()+"') ",'i',username));
			   Client.sendSQL(new SQLData("INSERT IGNORE INTO contacts (username,contact) values ('"+ txtSuche.getText()+ "','"+username+"')",'i',username));
				
			   txtSuche.setText("");
			}
			 // Client.sendSQL(new SQLData("INSERT INTO contacts (username,contact) values ('"+ username+ "','"+txtSuche.getText()+"') Where NOT EXISTS(SELECT * from contacts where username like '"+ username+ "' and contact like '"+txtSuche.getText()+"')",'i',username));
			   // Client.sendSQL(new SQLData("INSERT INTO contacts (username,contact) values ('"+ txtSuche.getText()+ "','"+username+"') Where NOT EXISTS(SELECT * from contacts where username like '"+ txtSuche.getText()+ "' and contact like '"+username+"')",'i',username));
				
			    
			 //   Client.sendSQL(new SQLData("IF NOT EXISTS (SELECT * from contacts where username like '"+ username+ "' and contact like '"+txtSuche.getText()+"') THEN INSERT INTO contacts (username,contact) values ('"+ username+ "','"+txtSuche.getText()+"') END IF",'i',username));
			  //  Client.sendSQL(new SQLData("IF NOT EXISTS (SELECT * from contacts where username like '"+ txtSuche.getText()+ "' and contact like '"+username+"') THEN INSERT INTO contacts (username,contact) values ('"+ txtSuche.getText()+ "','"+username+"') END IF",'i',username));
				   
	
			    
			    //IF NOT EXISTS (SELECT xyz FROM TABLE WHERE col = 'xyz') THEN
		                //INSERT INTO....
		               // ELSE
		               //                 UPDATE....
		               // END IF;
			    
			    
			   
			   
			  //  MultiThreadedServer.sqlBefehlsListe.add(new SQLData("UPDATE user set status='Online' where username like '"+this.user+"'",'n'));
				
		    
		    }
		    
		});
		btnKontaktSuche.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelKontakt.add(btnKontaktSuche);
		
		
		if(username.equalsIgnoreCase("Admin")){
		    btnKontaktSuche.setEnabled(false);
		    txtSuche.setText("Admin hat alle");
		    txtSuche.setEnabled(false);
		    txtSuche.setEditable(false);
		}
		

		frame.getContentPane().add(panelKontakt);
		frame.add(new GradientPanel(new Color(27, 130, 165), new Color(204, 204,
				255), width, height));
	}

	public static void PictureUpdater(String picID) {
	    
	
	    String shortendPicID=picID.substring(0,1);
	    System.out.println("picID:" + picID +"|"+shortendPicID);
	    ImageIcon avatarImage = new ImageIcon();
	    switch(shortendPicID){ 
	        case "1": 
	            System.out.println("pic1"); 
	            avatarImage =i1;
	            break; 
	        case "2": 
	            System.out.println("pic2"); 
	            avatarImage =i2;
	            break; 
	        case "3": 
	            System.out.println("pic3"); 
	            avatarImage =i3;
	            break; 
	        case "4": 
	            System.out.println("pic4"); 
	            avatarImage =i4;
	            break; 
	        case "5": 
	            System.out.println("pic5"); 
	            avatarImage =i5;
	            break; 
	        case "6": 
	            System.out.println("pic6"); 
	            avatarImage =i6;
	            break; 
	        case "7": 
	            System.out.println("pic7"); 
	            avatarImage =i7;
	            break; 
	        case "8": 
	            System.out.println("pic8"); 
	            avatarImage =i8;
	            break; 
	        case "9": 
	            System.out.println("pic9"); 
	            avatarImage =i9;
	            break; 
	        default: 
	            System.out.println("pic1"); 
	            avatarImage =i1;
	        } 
	    label_2.setIcon(avatarImage);
	    
	}
}
