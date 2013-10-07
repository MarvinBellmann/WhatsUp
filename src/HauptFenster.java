import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

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
	
	static String username = "AdminTest";
	static String serverIP = "localhost"; //SERVER IP!

	

	

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

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
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

				ChatFenster chatFenster = new ChatFenster();
				chatFenster.domain((String) table.getValueAt(
						table.getSelectedRow(), 2));
				System.out.println("Chatfenster mit "
						+ table.getValueAt(table.getSelectedRow(), 2)
						+ " geöffnet.");
			}
		});
		table.setModel(new DefaultTableModel(new Object[][] {
				{ "Bild1", "Online", "Marvin" },
				{ "Bild2", "Online", "Franz" },
				{ "Bild3", "Offline", "Lappen" }, }, new String[] {
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

		JLabel lblSvenole = new JLabel("Sven-Ole");
		lblSvenole.setHorizontalAlignment(SwingConstants.LEFT);
		lblSvenole.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblSvenole.setBounds((border * 2) + (69 + border), border, 90, 14);
		panelProfil.add(lblSvenole);

		JLabel label = new JLabel("Online");
		label.setHorizontalAlignment(SwingConstants.LEFT);
		label.setForeground(Color.GREEN);
		label.setFont(new Font("Tahoma", Font.BOLD, 14));
		label.setBounds((border * 2) + (69 + border), (border * 2) + 14, 90, 14);
		panelProfil.add(label);

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
	}
}
