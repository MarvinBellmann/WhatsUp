package Client;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
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
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;

import net.miginfocom.swing.MigLayout;
import SendData.SQLData;

public class HauptFenster {

	public static GraphicsEnvironment ge = GraphicsEnvironment
			.getLocalGraphicsEnvironment();

	static String username;
	private Border raisedetched = BorderFactory.createEtchedBorder(
			EtchedBorder.RAISED, Color.darkGray, Color.lightGray);

	static JFrame frame;
	static GradientPanel mainPanel;
	static String desktopPath;
	public static int messagesLeft = 10;
	private static int width = 250;
	private static int height = 578;
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
	JLabel lblUsername;
	JButton btnKontaktSuche;

	private static JTextField txtSuche;
	public static JLabel statuslabel;
	public static ArrayList<ChatFenster> ChatFensterList = new ArrayList<ChatFenster>();

	static String serverIP = "localhost";

	/**
	 * Create the application.
	 */
	/**
	 * @wbp.parser.constructor
	 */
	public HauptFenster(String user, String pw, String server) {
		username = user;
		serverIP = server;
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
				ImageIcon avatarImage = new ImageIcon();
				switch (gesplittet[i + 2]) {
				case "1":
					avatarImage = i1;
					break;
				case "2":
					avatarImage = i2;
					break;
				case "3":
					avatarImage = i3;
					break;
				case "4":
					avatarImage = i4;
					break;
				case "5":
					avatarImage = i5;
					break;
				case "6":
					avatarImage = i6;
					break;
				case "7":
					avatarImage = i7;
					break;
				case "8":
					avatarImage = i8;
					break;
				case "9":
					avatarImage = i9;
					break;
				default:
					avatarImage = i1;
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
		// i91 = new ImageIcon(getClass().getResource("/data/2.png"));

		desktopPath = System.getProperty("user.home") + "/Desktop";
		desktopPath = desktopPath.replace("\\", "/");
		System.out.println("*** Anmeldungsversuch als: " + username);

		frame = new JFrame();
		frame.setResizable(false);
		//frame.setR
		//frame.setMaximumSize(new Dimension(width+1, height+100));  
		// frame.setMinimumSize(new Dimension(width, height-100));
		//Rectangle bounds = new Rectangle(100, 50, width, width+10);
	       // setMaximizedBounds(bounds);
	       // frame.setMaximizedBounds(bounds);  
		
	         
		frame.setBounds(ge.getMaximumWindowBounds().width - width - 20,
				(ge.getMaximumWindowBounds().height - height) / 4, width,
				height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		
		
		
		
		
		
		
		JMenuBar menuBar = new JMenuBar(){
			Color bgColor=new Color(27, 130, 165);//Color.BLUE.brighter();
        	public void setColor(Color color)
            {
                bgColor=color;
            }

            @Override
            protected void paintComponent(Graphics g)
            {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setColor(bgColor);
                g2d.fillRect(0, 0, getWidth() - 1, getHeight() - 1);

            }
        };
        menuBar.setBorder(null);
        menuBar.setBackground(new Color(27, 130, 165));
        
        JMenu mMenu = new JMenu("Menü");
        mMenu.setForeground(Color.WHITE);
        menuBar.add(mMenu);
        JMenuItem item1 = new JMenuItem("Beenden");
        item1.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        mMenu.add(item1);
        
        ////////////////////////////////////////////////////////////////////////////
        
        JMenu mEinstellungen = new JMenu("Einstellungen");
        mEinstellungen.setForeground(Color.WHITE);
        menuBar.add(mEinstellungen);
        JMenuItem item2 = new JMenuItem("Größe 1");
        item2.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                height=496-5;
                frame.setSize(frame.getWidth(), height);
            }
        });
        mEinstellungen.add(item2);
        
        JMenuItem item3 = new JMenuItem("Größe 2");
        item3.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	height=578-5;
                frame.setSize(frame.getWidth(), height);
            }
        });
        mEinstellungen.add(item3);
        
        JMenuItem item4 = new JMenuItem("Größe 3");
        item4.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	height=658-5;
                frame.setSize(frame.getWidth(), height);
            }
        });
        mEinstellungen.add(item4);
        
        JMenuItem item5 = new JMenuItem("Größe 4");
        item5.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	height=740-5;
                frame.setSize(frame.getWidth(), height);
            }
        });
        mEinstellungen.add(item5);
        
        
        ////////////////////////////////////////////////////////////////////////////
        
        JMenu mKontakte = new JMenu("Kontakte");
        mKontakte.setForeground(Color.WHITE);
        menuBar.add(mKontakte);
        
        JMenuItem item6 = new JMenuItem("Kontakt löschen");
        item6.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	btnKontaktSuche.setText("-");
            }
        });
        mKontakte.add(item6);
        JMenuItem item7 = new JMenuItem("Kontakt hinzufügen");
        item7.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	btnKontaktSuche.setText("+");
            }
        });
        mKontakte.add(item7);
        
 ////////////////////////////////////////////////////////////////////////////
        
        
        if(username.equalsIgnoreCase("Admin")){
        
        JMenu mSQL = new JMenu("SQL");
        mSQL.setForeground(Color.YELLOW);
        menuBar.add(mSQL);
        
        JMenuItem item8 = new JMenuItem("SQL Tips");
        item8.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
            	
            }
        });
        mSQL.add(item8);
        }
        
        
        
        
        
        
        frame.setJMenuBar(menuBar);
		
		
		
		

		mainPanel = new GradientPanel(new Color(27, 130, 165), new Color(204,
				204, 255));
		mainPanel
				.setLayout(new MigLayout("fill", "[80px:n:80px][]",
						"[30px:n:30px][45px::45px,top][40px::40px,bottom][30px::30px][]"));

		label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon(HauptFenster.class
				.getResource("/data/1.jpg")));
		mainPanel.add(label_2, "span 1 2");

		lblUsername = new JLabel(username);
		lblUsername.setHorizontalAlignment(SwingConstants.LEFT);
		lblUsername.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblUsername.setForeground(Color.white);
		mainPanel.add(lblUsername, "wrap");

		statuslabel = new JLabel("Offline");
		statuslabel.setHorizontalAlignment(SwingConstants.LEFT);
		statuslabel.setForeground(Color.RED);
		statuslabel.setFont(new Font("Tahoma", Font.BOLD, 14));
		mainPanel.add(statuslabel, "wrap");

		JLabel lblKontakte = new JLabel("Kontakte");
		lblKontakte.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblKontakte.setForeground(Color.white);
		lblKontakte.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(lblKontakte, "spanx 2, growx, wrap");

		txtSuche = new JTextField();
		txtSuche.setText("");
		txtSuche.setBorder(raisedetched);
		txtSuche.setHorizontalAlignment(SwingConstants.CENTER);
		mainPanel.add(txtSuche, "growx, spanx 2, split2");

		btnKontaktSuche = new JButton("+");
		btnKontaktSuche.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

			    if (txtSuche.getText().equalsIgnoreCase(lblUsername.getText()) == false) {

				/*JFrame dt = new JFrame();
				dt.setLocation(100,100);
				dt.setSize(350,200);
				dt.setTitle("Dialog-Test");
				dt.show();*/
				
				
				Client.sendSQL(new SQLData("Select * From user where username like '"
							+ txtSuche.getText()
							+ "';",'h',username));
				
				
				
			    }
			}

		});
		btnKontaktSuche.setFont(new Font("ARIAL BLACK", Font.BOLD, 10));
		mainPanel.add(btnKontaktSuche, "wrap, w 50px::50px");

		if (username.equalsIgnoreCase("Admin")) {
			btnKontaktSuche.setEnabled(false);
			txtSuche.setText("Admin hat alle");
			txtSuche.setEnabled(false);
			txtSuche.setEditable(false);
		}

		// Tabelle
		DefaultTableModel model = new DefaultTableModel(new Object[][] {,},
				new String[] { "Profilbild", "Status", "Name" });

		table = new JTable(model) {

			public Class<?> getColumnClass(int column) {
				return getValueAt(0, column).getClass();
			}

			public boolean isCellEditable(int x, int y) {
				return false;
			}

			public Component prepareRenderer(TableCellRenderer renderer,
					int row, int column) {
				Component c = super.prepareRenderer(renderer, row, column);

				// Color row based on a cell value

				// if (!isRowSelected(row)) {
				// c.setBackground(getBackground());
				int modelRow = convertRowIndexToModel(row);
				String type = (String) getModel().getValueAt(modelRow, 1);
				if ("Online".equals(type)) {
					c.setBackground(new Color(190, 250, 190));
					c.setForeground(Color.GREEN.darker());
					c.setFont(new Font("Miriam", Font.BOLD, 14));
				}
				if ("Offline".equals(type)) {
					c.setBackground(new Color(250, 190, 190));
					c.setForeground(Color.RED.darker());
					c.setFont(new Font("Miriam", Font.PLAIN, 14));
				}
				// table.repaint();
				// }

				return c;
			}

		};
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setFocusable(false);
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
		scrollPane.setBorder(raisedetched);
		scrollPane.setBackground(new Color(190, 190, 255));
		mainPanel.add(scrollPane, "spanx 2,grow");

		frame.getContentPane().add(mainPanel);
	}

	
	public static void KontaktHinzufuegen() {
	    
		
		int ok = JOptionPane.showConfirmDialog(null, 
			"Hinzufügen?","Benutzer gefunden!", 
			JOptionPane.YES_NO_CANCEL_OPTION);
		if (ok == JOptionPane.YES_OPTION) {


		    Client.sendSQL(new SQLData(
			    "UPDATE user set status='Online' where username like '"
				    + username + "'", 'i'));

		    Client.sendSQL(new SQLData(
			    "INSERT IGNORE INTO contacts (username,contact) values ('"
				    + username + "','" + txtSuche.getText()
				    + "') ", 'i', username));
		    Client.sendSQL(new SQLData(
			    "INSERT IGNORE INTO contacts (username,contact) values ('"
				    + txtSuche.getText() + "','" + username
				    + "')", 'i', username));

		    txtSuche.setText("");
		}
		}
	
	
	public static void PictureUpdater(String picID) {

		String shortendPicID = picID.substring(0, 1);
		System.out.println("picID:" + picID + "|" + shortendPicID);
		ImageIcon avatarImage = new ImageIcon();
		switch (shortendPicID) {
		case "1":
			System.out.println("pic1");
			avatarImage = i1;
			break;
		case "2":
			System.out.println("pic2");
			avatarImage = i2;
			break;
		case "3":
			System.out.println("pic3");
			avatarImage = i3;
			break;
		case "4":
			System.out.println("pic4");
			avatarImage = i4;
			break;
		case "5":
			System.out.println("pic5");
			avatarImage = i5;
			break;
		case "6":
			System.out.println("pic6");
			avatarImage = i6;
			break;
		case "7":
			System.out.println("pic7");
			avatarImage = i7;
			break;
		case "8":
			System.out.println("pic8");
			avatarImage = i8;
			break;
		case "9":
			System.out.println("pic9");
			avatarImage = i9;
			break;
		default:
			System.out.println("pic1");
			avatarImage = i1;
		}
		label_2.setIcon(avatarImage);

	}
}
