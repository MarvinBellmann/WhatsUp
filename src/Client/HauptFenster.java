package Client;

import java.awt.Color;
import java.awt.Component;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.regex.Pattern;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
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
	public static JLabel label_2;
	
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
				model.addRow(new Object[] { i1, "Online", "ServerDB" });
			}

			for (int i = 0; i < gesplittet.length; i = i + 3) {
			    
			   // case 
			    ImageIcon AvatarImage = new ImageIcon();
				    switch(gesplittet[i + 2]){ 
				        case "1": 
				            System.out.println("pic1"); 
				            AvatarImage =i1;
				            break; 
				        case "2": 
				            System.out.println("pic2"); 
				            AvatarImage =i2;
				            break; 
				        default: 
				            System.out.println("pic1"); 
				            AvatarImage =i1;
				        } 
				    
				    
				model.addRow(new Object[] { AvatarImage, gesplittet[i + 1],
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
		i1 = new ImageIcon(getClass().getResource("/data/2.png"));
		i2 = new ImageIcon(getClass().getResource("/data/1.jpg"));
		i3 = new ImageIcon(getClass().getResource("/data/2.png"));
		i4 = new ImageIcon(getClass().getResource("/data/2.png"));
		i5 = new ImageIcon(getClass().getResource("/data/2.png"));
		i6 = new ImageIcon(getClass().getResource("/data/2.png"));
		i7 = new ImageIcon(getClass().getResource("/data/2.png"));
		i8 = new ImageIcon(getClass().getResource("/data/2.png"));
		i9 = new ImageIcon(getClass().getResource("/data/2.png"));
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

		JLabel lblUsername = new JLabel(username);
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
		txtSuche.setForeground(Color.LIGHT_GRAY);
		txtSuche.setText("Suche");
		txtSuche.setBorder(raisedetched);
		txtSuche.setHorizontalAlignment(SwingConstants.CENTER);
		txtSuche.setBounds(border, (border * 2) + 14, width - (border * 5), 20);
		panelKontakt.add(txtSuche);

		frame.getContentPane().add(panelKontakt);
		frame.add(new GradientPanel(new Color(27, 130, 165), new Color(204, 204,
				255), width, height));
	}
}
