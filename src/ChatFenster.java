import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;

public class ChatFenster {

	private JFrame frame;
	private JTextPane txtField;
	public static String nameGespraech;
	static JLabel name_lbl;

	/**
	 * Launch the application.
	 */
	public static void domain(String name) {
		nameGespraech = name;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatFenster window = new ChatFenster();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public ChatFenster() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(HauptFenster.frame.getX() - 464,
				HauptFenster.frame.getY(), 452, 339);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);// EXIT_ON_CLOSE
		frame.getContentPane().setLayout(null);

		JLabel online_lbl = new JLabel("Online");
		online_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		online_lbl.setForeground(Color.GREEN);
		online_lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		online_lbl.setBounds(361, 124, 66, 14);
		frame.getContentPane().add(online_lbl);

		JButton send_btn = new JButton("Senden");
		send_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				Client.send(HauptFenster.username, name_lbl.getText(),
						txtField.getText());
				txtField.setText("");

			}
		});
		send_btn.setFont(new Font("Tahoma", Font.PLAIN, 7));
		send_btn.setBounds(294, 286, 57, 13);
		frame.getContentPane().add(send_btn);

		txtField = new JTextPane();
		txtField.setContentType("text/html");
		txtField.setBounds(19, 206, 332, 79);
		txtField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					// Letztes Word auslesen
					// Nach Smiley Code aussuchen
					// ggf. Smiley Icon Kreieren
				}
			}
		});
		frame.getContentPane().add(txtField);

		JTextArea txtPanel = new JTextArea();
		txtPanel.setFont(new Font("Miriam", Font.PLAIN, 14));
		txtPanel.setText("Marvin (10:26): Hey diggie ");
		txtPanel.setBounds(19, 29, 332, 160);
		frame.getContentPane().add(txtPanel);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(ChatFenster.class
				.getResource("/data/2.png")));
		label.setBounds(358, 31, 69, 69);
		frame.getContentPane().add(label);

		JLabel ichbild_lbl = new JLabel("");
		ichbild_lbl.setIcon(new ImageIcon(ChatFenster.class
				.getResource("/data/1.jpg")));
		ichbild_lbl.setBounds(358, 210, 69, 69);
		frame.getContentPane().add(ichbild_lbl);

		name_lbl = new JLabel("test");
		name_lbl.setHorizontalAlignment(SwingConstants.CENTER);
		name_lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		name_lbl.setBounds(357, 104, 72, 14);
		frame.getContentPane().add(name_lbl);

		JPanel panel_1 = new JPanel();
		panel_1.setForeground(Color.ORANGE);
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(10, 22, 424, 278);
		frame.getContentPane().add(panel_1);

		JPanel panel_3 = new JPanel();
		panel_3.setForeground(Color.ORANGE);
		panel_3.setBackground(Color.LIGHT_GRAY);
		panel_3.setBounds(19, 11, 408, 10);
		frame.getContentPane().add(panel_3);

		name_lbl.setText(nameGespraech);
	}

	public String getLastWord() {
		String lastWord = null;

		return lastWord;
	}
}
