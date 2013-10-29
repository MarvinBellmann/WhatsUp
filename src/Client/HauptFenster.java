package Client;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
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
	static JFrame avatarFrame;
	static GradientPanel mainPanel;
	static String desktopPath;
	public static int messagesLeft = 10;
	private static int width = 250;
	private static int height = 573;
	static JTable table;
	static String i1 = "src/data/1.png";
	static String i2 = "src/data/2.png";
	static String i3 = "src/data/3.png";
	static String i4 = "src/data/4.png";
	static String i5 = "src/data/5.png";
	static String i6 = "src/data/6.png";
	static String i7 = "src/data/7.png";
	static String i8 = "src/data/8.png";
	static String i9 = "src/data/9.png";
	static String iDB = "src/data/database.png";
	public static JLabel label_2;
	JLabel lblUsername;
	JButton btnKontaktSuche;
	ImageIcon addIcon = new ImageIcon("src/data/add.png");
	ImageIcon removeIcon = new ImageIcon("src/data/remove.png");
	static boolean byteUebertragungsBeschuetzer = false;

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
		// text-String ändern in html-fähigen code
		//
		for (ChatFenster c : ChatFensterList) {
			System.out.println(c.txtPanel.getText());
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
						new String[] { "Kontakte" }));
				model = (DefaultTableModel) table.getModel();
				System.out.println("### Tablelleneinträge gelöscht!");
			}

			if (username.equals("Admin")) {
				ImageIcon avatarDB = new ImageIcon(iDB);
				model.addRow(new Object[] { new ContactCard(avatarDB, "Online",
						"ServerDB") });
			}

			for (int i = 0; i < gesplittet.length; i = i + 3) {
				ImageIcon avatarImage = new ImageIcon();
				String filename;
				switch (gesplittet[i + 2]) {
				case "1":
					filename = i1;
					break;
				case "2":
					filename = i2;
					break;
				case "3":
					filename = i3;
					break;
				case "4":
					filename = i4;
					break;
				case "5":
					filename = i5;
					break;
				case "6":
					filename = i6;
					break;
				case "7":
					filename = i7;
					break;
				case "8":
					filename = i8;
					break;
				case "9":
					filename = i9;
					break;
				default:
					filename = i1;
				}
				avatarImage = new ImageIcon(filename);
				model.addRow(new Object[] { new ContactCard(avatarImage,
						gesplittet[i + 1], gesplittet[i]) });
			}

			System.out
					.println("### Tablelleneinträge inkl neuer Stati geladen aus DB!");

			for (ChatFenster CF : ChatFensterList) {
				CF.UpdateStatus();
			}

			for (int row = 0; row < table.getRowCount(); row++) {
				int rowHeight = table.getRowHeight();

				Component comp = table.prepareRenderer(
						table.getCellRenderer(row, 0), row, 0);
				rowHeight = Math.max(rowHeight, comp.getPreferredSize().height);
				table.setRowHeight(row, rowHeight);
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

			ContactCard card = (ContactCard) model.getValueAt(i, 0);
			card.setStatus("Verb. Abbruch");
			card.setColor1(Color.white);
			card.setColor2(Color.gray);
		}
		table.repaint();
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
		desktopPath = System.getProperty("user.home") + "/Desktop";
		desktopPath = desktopPath.replace("\\", "/");
		System.out.println("*** Anmeldungsversuch als: " + username);

		frame = new JFrame();
		frame.setResizable(false);
		// frame.setR
		// frame.setMaximumSize(new Dimension(width+1, height+100));
		// frame.setMinimumSize(new Dimension(width, height-100));
		// Rectangle bounds = new Rectangle(100, 50, width, width+10);
		// setMaximizedBounds(bounds);
		// frame.setMaximizedBounds(bounds);

		frame.setBounds(ge.getMaximumWindowBounds().width - width - 20,
				(ge.getMaximumWindowBounds().height - height) / 6, width,
				height);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		JMenuBar menuBar = new JMenuBar() {
			Color bgColor = new Color(27, 130, 165);// Color.BLUE.brighter();

			/*
			 * public void setColor(Color color) { bgColor=color; }
			 */
			@Override
			protected void paintComponent(Graphics g) {
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
		JMenuItem mMenuitem1 = new JMenuItem("Beenden");
		mMenuitem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		mMenu.add(mMenuitem1);

		// //////////////////////////////////////////////////////////////////////////

		JMenu mEinstellungen = new JMenu("Einstellungen");
		mEinstellungen.setForeground(Color.WHITE);
		menuBar.add(mEinstellungen);
		JMenuItem mEinstellungenitem1 = new JMenuItem("Größe 3");
		mEinstellungenitem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				height = 494;
				frame.setSize(frame.getWidth(), height);
			}
		});
		mEinstellungen.add(mEinstellungenitem1);

		JMenuItem mEinstellungenitem2 = new JMenuItem("Größe 4");
		mEinstellungenitem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				height = 578 - 5;
				frame.setSize(frame.getWidth(), height);
			}
		});
		mEinstellungen.add(mEinstellungenitem2);

		JMenuItem mEinstellungenitem3 = new JMenuItem("Größe 5");
		mEinstellungenitem3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				height = 658 - 5;
				frame.setSize(frame.getWidth(), height);
			}
		});
		mEinstellungen.add(mEinstellungenitem3);

		JMenuItem mEinstellungenitem4 = new JMenuItem("Größe 6");
		mEinstellungenitem4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				height = 740 - 3;
				frame.setSize(frame.getWidth(), height);
			}
		});
		mEinstellungen.add(mEinstellungenitem4);

		JMenuItem mEinstellungenitem5 = new JMenuItem("Größe 7");
		mEinstellungenitem5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				height = 820;
				frame.setSize(frame.getWidth(), height);
			}
		});
		mEinstellungen.add(mEinstellungenitem5);

		// //////////////////////////////////////////////////////////////////////////

		JMenu mKontakte = new JMenu("Kontakte");
		mKontakte.setForeground(Color.WHITE);
		menuBar.add(mKontakte);

		JMenuItem mKontakteitem1 = new JMenuItem("Zur Kontaktliste hinzufügen");
		mKontakteitem1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnKontaktSuche.setIcon(addIcon);
			}
		});
		mKontakte.add(mKontakteitem1);
		JMenuItem mKontakteitem2 = new JMenuItem("Aus Kontaktliste löschen");
		mKontakteitem2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				btnKontaktSuche.setIcon(removeIcon);
			}
		});
		mKontakte.add(mKontakteitem2);

		// //////////////////////////////////////////////////////////////////////////

		if (username.equalsIgnoreCase("Admin")) {

			JMenu mSQL = new JMenu("SQL");
			mSQL.setForeground(Color.YELLOW);
			menuBar.add(mSQL);

			JMenuItem item8 = new JMenuItem("SQL Tips");
			item8.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					JOptionPane
							.showMessageDialog(
									null,
									"-Alle Benutzerdaten ausgeben: SELECT * from user order by username\n"
											+ "\n"
											+ "-Benutzer bannen: DELETE from user where username like 'XXX'\n"
											+ "                                   DELETE from contacts where username like 'XXX' or contact like 'XXX'\n"
											+ "\n"
											+ "-Benutzer Pw ändern: UPDATE user set password = '????' where username like 'XXX'\n"
											+ "\n"
											+ "-Kontakt anlegen: INSERT IGNORE INTO contacts (username, contact) values ('XXX','YYY')\n"
											+ "                                  INSERT IGNORE INTO contacts (username, contact) values ('YYY','XXX')\n"
											+ "\n"
											+ "-Kontakt löschen: DELETE from contacts where username like 'XXX' and contact like 'YYY')\n"
											+ "                                  DELETE from contacts where username like 'YYY' and contact like 'XXX')");
				}
			});
			mSQL.add(item8);
		}

		frame.setJMenuBar(menuBar);

		mainPanel = new GradientPanel(new Color(27, 130, 165), new Color(204,
				204, 255));
		mainPanel
				.setLayout(new MigLayout("fill", "[80px:n:80px][]",
						"[30px:n:30px][45px::45px,top][20px::20px,bottom][30px::30px][]"));

		label_2 = new JLabel("");
		label_2.setIcon(new ImageIcon("src/data/loading.gif"));
		label_2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
		label_2.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (avatarFrame == null || avatarFrame.isVisible() == false) {
					int aWidth = 400;
					int aHeight = 320;
					avatarFrame = new JFrame();
					if (HauptFenster.frame.getX() - aWidth - 7 <= 0) {
						avatarFrame.setLocation(HauptFenster.frame.getX()
								+ HauptFenster.frame.getWidth() + 7,
								HauptFenster.frame.getY());
					} else {
						avatarFrame.setLocation(HauptFenster.frame.getX()
								- aWidth - 7, HauptFenster.frame.getY());
					}
					avatarFrame.setResizable(false);
					avatarFrame.setSize(aWidth, aHeight);
					avatarFrame.setVisible(true);

					GradientPanel avatarPanel = new GradientPanel(new Color(27,
							130, 165), new Color(204, 204, 255));
					avatarPanel.setLayout(new MigLayout("fill, wrap 3"));

					JLabel avatarImage1 = new JLabel("");
					avatarImage1.setIcon(new ImageIcon(i1));
					avatarImage1.setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
					avatarImage1.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
						    // Datenbankeintrag
						    Client.sendSQL(new SQLData(
							    "UPDATE user set status='Online' where username like '"
								    + username + "'", 'i'));

						    Client.sendSQL(new SQLData(
							    "UPDATE user set picture='1' where username like '"
								    + username + "'", 'i'));

						    label_2.setIcon(new ImageIcon(i1));
						    avatarFrame.hide();
						}
					});
					avatarPanel.add(avatarImage1);

					JLabel avatarImage2 = new JLabel("");
					avatarImage2.setIcon(new ImageIcon(i2));
					avatarImage2.setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
					avatarImage2.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
						    // Datenbankeintrag
						    Client.sendSQL(new SQLData(
							    "UPDATE user set status='Online' where username like '"
								    + username + "'", 'i'));

						    Client.sendSQL(new SQLData(
							    "UPDATE user set picture='2' where username like '"
								    + username + "'", 'i'));

						    label_2.setIcon(new ImageIcon(i2));
						    avatarFrame.hide();
						}
					});
					avatarPanel.add(avatarImage2);

					JLabel avatarImage3 = new JLabel("");
					avatarImage3.setIcon(new ImageIcon(i3));
					avatarImage3.setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
					avatarImage3.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
						    // Datenbankeintrag
						    Client.sendSQL(new SQLData(
							    "UPDATE user set status='Online' where username like '"
								    + username + "'", 'i'));

						    Client.sendSQL(new SQLData(
							    "UPDATE user set picture='3' where username like '"
								    + username + "'", 'i'));

						    label_2.setIcon(new ImageIcon(i3));
						    avatarFrame.hide();
						}
					});
					avatarPanel.add(avatarImage3);

					JLabel avatarImage4 = new JLabel("");
					avatarImage4.setIcon(new ImageIcon(i4));
					avatarImage4.setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
					avatarImage4.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
						    // Datenbankeintrag
						    Client.sendSQL(new SQLData(
							    "UPDATE user set status='Online' where username like '"
								    + username + "'", 'i'));

						    Client.sendSQL(new SQLData(
							    "UPDATE user set picture='4' where username like '"
								    + username + "'", 'i'));

						    label_2.setIcon(new ImageIcon(i4));
						    avatarFrame.hide();
						}
					});
					avatarPanel.add(avatarImage4);

					JLabel avatarImage5 = new JLabel("");
					avatarImage5.setIcon(new ImageIcon(i5));
					avatarImage5.setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
					avatarImage5.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
						    // Datenbankeintrag
						    Client.sendSQL(new SQLData(
							    "UPDATE user set status='Online' where username like '"
								    + username + "'", 'i'));

						    Client.sendSQL(new SQLData(
							    "UPDATE user set picture='5' where username like '"
								    + username + "'", 'i'));

						    label_2.setIcon(new ImageIcon(i5));
						    avatarFrame.hide();
						}
					});
					avatarPanel.add(avatarImage5);

					JLabel avatarImage6 = new JLabel("");
					avatarImage6.setIcon(new ImageIcon(i6));
					avatarImage6.setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
					avatarImage6.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
						    // Datenbankeintrag
						    Client.sendSQL(new SQLData(
							    "UPDATE user set status='Online' where username like '"
								    + username + "'", 'i'));

						    Client.sendSQL(new SQLData(
							    "UPDATE user set picture='6' where username like '"
								    + username + "'", 'i'));

						    label_2.setIcon(new ImageIcon(i6));
						    avatarFrame.hide();
						}
					});
					avatarPanel.add(avatarImage6);

					JLabel avatarImage7 = new JLabel("");
					avatarImage7.setIcon(new ImageIcon(i7));
					avatarImage7.setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
					avatarImage7.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
						    // Datenbankeintrag
						    Client.sendSQL(new SQLData(
							    "UPDATE user set status='Online' where username like '"
								    + username + "'", 'i'));

						    Client.sendSQL(new SQLData(
							    "UPDATE user set picture='7' where username like '"
								    + username + "'", 'i'));

						    label_2.setIcon(new ImageIcon(i7));
						    avatarFrame.hide();
						}
					});
					avatarPanel.add(avatarImage7);

					JLabel avatarImage8 = new JLabel("");
					avatarImage8.setIcon(new ImageIcon(i8));
					avatarImage8.setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
					avatarImage8.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
						    // Datenbankeintrag
						    Client.sendSQL(new SQLData(
							    "UPDATE user set status='Online' where username like '"
								    + username + "'", 'i'));

						    Client.sendSQL(new SQLData(
							    "UPDATE user set picture='8' where username like '"
								    + username + "'", 'i'));

						    label_2.setIcon(new ImageIcon(i8));
						    avatarFrame.hide();
						}
					});
					avatarPanel.add(avatarImage8);

					JLabel avatarImage9 = new JLabel("");
					avatarImage9.setIcon(new ImageIcon(i9));
					avatarImage9.setCursor(Cursor
							.getPredefinedCursor(Cursor.HAND_CURSOR));
					avatarImage9.addMouseListener(new MouseAdapter() {
						@Override
						public void mouseClicked(MouseEvent e) {
						    // Datenbankeintrag
						    Client.sendSQL(new SQLData(
							    "UPDATE user set status='Online' where username like '"
								    + username + "'", 'i'));

						    Client.sendSQL(new SQLData(
							    "UPDATE user set picture='9' where username like '"
								    + username + "'", 'i'));

						    label_2.setIcon(new ImageIcon(i9));
						    avatarFrame.hide();
						}
					});
					avatarPanel.add(avatarImage9);

					avatarFrame.setContentPane(avatarPanel);
				}
			}
		});
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

		btnKontaktSuche = new JButton("");
		btnKontaktSuche.setIcon(addIcon);
		btnKontaktSuche.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				if (txtSuche.getText().equalsIgnoreCase(lblUsername.getText()) == false
						&& txtSuche.getText().equalsIgnoreCase("admin") == false) {

					if (btnKontaktSuche.getIcon().equals(addIcon)) {
						Client.sendSQL(new SQLData(
								"Select * From user where username like '"
										+ txtSuche.getText() + "';", 'h',
								username));
					} else {
						Client.sendSQL(new SQLData(
								"Select * From user where username like '"
										+ txtSuche.getText() + "';", 'l',
								username));

					}

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
		DefaultTableModel model = new DefaultTableModel();
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

				int modelRow = convertRowIndexToModel(row);
				ContactCard card = (ContactCard) getModel().getValueAt(
						modelRow, 0);
				if ("Online".equals(card.getStatus())) {
					card.setColor1(Color.green);
					card.setColor2(Color.green.darker());
					c.setFont(new Font("Miriam", Font.BOLD, 14));
				}
				if ("Offline".equals(card.getStatus())) {
					card.setColor1(new Color(245, 30, 30));
					card.setColor2(new Color(220, 20, 20));
					c.setFont(new Font("Miriam", Font.PLAIN, 14));
				}

				return c;
			}

		};
		table.setDefaultRenderer(ContactCard.class, new ContactCardRenderer());
		table.setPreferredScrollableViewportSize(table.getPreferredSize());
		table.getSelectionModel().setSelectionMode(
				ListSelectionModel.SINGLE_SELECTION);
		table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		table.setFocusable(false);
		table.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent arg0) {
				ContactCard card = (ContactCard) table.getValueAt(
						table.getSelectedRow(), 0);
				boolean abbruch = false;

				for (ChatFenster CF : ChatFensterList) {
					if (CF.nameGespraech.equalsIgnoreCase(card.getName())) {
						abbruch = true;
					}
				}
				if (abbruch == false) {
					ChatFenster c = new ChatFenster(card.getName(), card
							.getStatus());
					ChatFensterList.add(c);
					System.out.println("### Chatfenster mit " + card.getName()
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
				"Wollen sie den Benutzer (" + txtSuche.getText()
						+ ") zu Ihrer Kontaktliste hinzufuegen?",
				"Benutzer gefunden! Hinzufuegen?",
				JOptionPane.YES_NO_CANCEL_OPTION);
		if (ok == JOptionPane.YES_OPTION) {

			Client.sendSQL(new SQLData(
					"UPDATE user set status='Online' where username like '"
							+ username + "'", 'i'));

			Client.sendSQL(new SQLData(
					"INSERT IGNORE INTO contacts (username,contact) values ('"
							+ username + "','" + txtSuche.getText() + "') ",
					'i', username));
			Client.sendSQL(new SQLData(
					"INSERT IGNORE INTO contacts (username,contact) values ('"
							+ txtSuche.getText() + "','" + username + "')",
					'i', username));

			txtSuche.setText("");
		}
	}

	public static void PictureUpdater(String picID) {

		String shortendPicID = picID.substring(0, 1);
		System.out.println("picID:" + picID + "|" + shortendPicID);
		ImageIcon avatarImage = new ImageIcon();
		String filename;
		switch (shortendPicID) {
		case "1":
			System.out.println("pic1");
			filename = i1;
			break;
		case "2":
			System.out.println("pic2");
			filename = i2;
			break;
		case "3":
			System.out.println("pic3");
			filename = i3;
			break;
		case "4":
			System.out.println("pic4");
			filename = i4;
			break;
		case "5":
			System.out.println("pic5");
			filename = i5;
			break;
		case "6":
			System.out.println("pic6");
			filename = i6;
			break;
		case "7":
			System.out.println("pic7");
			filename = i7;
			break;
		case "8":
			System.out.println("pic8");
			filename = i8;
			break;
		case "9":
			System.out.println("pic9");
			filename = i9;
			break;
		default:
			System.out.println("pic1");
			filename = i1;
		}
		avatarImage = new ImageIcon(filename);
		label_2.setIcon(avatarImage);

	}

	public static void KontaktLoeschen() {
		int ok = JOptionPane.showConfirmDialog(null,
				"Wollen sie den Benutzer (" + txtSuche.getText()
						+ ") aus Ihrer Kontaktliste loeschen?",
				"Benutzer gefunden! Loeschen?",
				JOptionPane.YES_NO_CANCEL_OPTION);
		if (ok == JOptionPane.YES_OPTION) {

			Client.sendSQL(new SQLData(
					"UPDATE user set status='Online' where username like '"
							+ username + "'", 'i'));

			Client.sendSQL(new SQLData(
					"DELETE from contacts where username like '" + username
							+ "' and contact like'" + txtSuche.getText() + "'",
					'i', username));
			Client.sendSQL(new SQLData(
					"DELETE from contacts where username like '"
							+ txtSuche.getText() + "' and contact like'"
							+ username + "'", 'i', username));

			
			//txtSuche.getText()
			int index = 0;
			for (ChatFenster CF : HauptFenster.ChatFensterList) {
				if (CF.nameGespraech.equalsIgnoreCase(txtSuche.getText())) {
				    CF.hiding();
				    break;
				}
				index++;
				
			}
			HauptFenster.ChatFensterList
			.remove(HauptFenster.ChatFensterList.get(index));
			
			txtSuche.setText("");
		}
	}

	public static void loescheCF(ChatFenster CFchatFenster) {
	    // TODO Auto-generated method stub
	/*    int index=0;
	    for (ChatFenster CF : ChatFensterList) {
		if (CF.nameGespraech.equals(chatFenster.nameGespraech)) {
			break;
		}
		index++;
	}
	HauptFenster.ChatFensterList.remove(HauptFenster.ChatFensterList.get(index));*/
	    ChatFensterList.remove(CFchatFenster);
	}
}
