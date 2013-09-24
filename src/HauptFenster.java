import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;


public class HauptFenster {

    static JFrame frame;
    private JTable table;
    private JTextField txtSuche;
    static String username="AdminTest";

    /**
     * Launch the application.
     */
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
	
	
	
	
	
	//Server server = new Server();
	Client client = new Client("localhost");
	
	
	
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
	frame.setBounds(1100, 100, 191, 400);
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.getContentPane().setLayout(null);
	
	JLabel label = new JLabel("Online");
	label.setHorizontalAlignment(SwingConstants.CENTER);
	label.setForeground(Color.GREEN);
	label.setFont(new Font("Tahoma", Font.BOLD, 14));
	label.setBounds(89, 34, 83, 14);
	frame.getContentPane().add(label);
	
	JScrollPane scrollPane = new JScrollPane();
	scrollPane.setBounds(14, 136, 158, 226);
	frame.getContentPane().add(scrollPane);
	
	table = new JTable();
	table.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
	table.addMouseListener(new MouseAdapter() {
		@Override
		public void mousePressed(MouseEvent arg0) {
		    
		    
		    ChatFenster chatFenster = new ChatFenster();
		    chatFenster.domain((String)table.getValueAt(table.getSelectedRow(),2));
		  System.out.println("Chatfenster mit "+table.getValueAt(table.getSelectedRow(),2) + " geöffnet.");
		    
		}
	});
	//table.setEditingColumn(false);
	//table.setE
	//table.
	table.setModel(new DefaultTableModel(
		new Object[][] {
			{"Bild1", "Online", "Marvin"},
			{"Bild2", "Online", "Franz"},
			{"Bild3", "Offline", "Lappen"},
		},
		new String[] {
			"Profilbild", "Status", "Name"
		}
	));
	scrollPane.setViewportView(table);
	
	JLabel lblKontakte = new JLabel("Kontakte");
	lblKontakte.setFont(new Font("Tahoma", Font.BOLD, 14));
	lblKontakte.setHorizontalAlignment(SwingConstants.CENTER);
	lblKontakte.setBounds(14, 97, 158, 14);
	frame.getContentPane().add(lblKontakte);
	
	JLabel label_2 = new JLabel("");
	label_2.setIcon(new ImageIcon(HauptFenster.class.getResource("/data/1.jpg")));
	label_2.setBounds(14, 14, 69, 69);
	frame.getContentPane().add(label_2);
	
	JLabel lblSvenole = new JLabel("Sven-Ole");
	lblSvenole.setHorizontalAlignment(SwingConstants.CENTER);
	lblSvenole.setFont(new Font("Tahoma", Font.BOLD, 14));
	lblSvenole.setBounds(89, 15, 84, 14);
	frame.getContentPane().add(lblSvenole);
	
	txtSuche = new JTextField();
	txtSuche.setForeground(Color.LIGHT_GRAY);
	txtSuche.setText("Suche");
	txtSuche.setBounds(14, 113, 158, 20);
	frame.getContentPane().add(txtSuche);
	txtSuche.setColumns(10);
	
	JPanel panel = new JPanel();
	panel.setBackground(Color.LIGHT_GRAY);
	panel.setForeground(Color.ORANGE);
	panel.setBounds(7, 94, 171, 271);
	frame.getContentPane().add(panel);
	
	JPanel panel_2 = new JPanel();
	panel_2.setForeground(Color.ORANGE);
	panel_2.setBackground(Color.LIGHT_GRAY);
	panel_2.setBounds(7, 7, 171, 82);
	frame.getContentPane().add(panel_2);
    }
}
