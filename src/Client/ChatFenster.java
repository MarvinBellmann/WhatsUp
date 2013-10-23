package Client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

public class ChatFenster {

	public JFrame frame;
	JTextPane txtField;
	public String nameGespraech;
	public String status;
	JLabel name_lbl;
	JLabel online_lbl;
	private static int width = 450;
	private static int border = 7;
	private static int height = 340;
	private static int pictureSize = 69;
	private static int textSize = 330;
	public JTextArea txtPanel;
	JPanel panel_1;

	private Border raisedetched = BorderFactory.createEtchedBorder(
			EtchedBorder.RAISED, Color.darkGray, Color.lightGray);
	private Border raisedbevel = BorderFactory.createRaisedBevelBorder();
	private Border loweredbevel = BorderFactory.createLoweredBevelBorder();
	private CompoundBorder compound = BorderFactory.createCompoundBorder(
			raisedbevel, loweredbevel);

	LineBorder brd = new LineBorder(null, 5, true);

	/**
	 * Launch the application.
	 */
	public void domain(String name) {
		frame.setVisible(true);
	}

	/**
	 * Create the application.
	 */
	public ChatFenster(String name, String status) {
		this.nameGespraech = name;
		this.status = status;

		initialize();
		frame.setVisible(true);
	}

	public void UpdateStatus() {

		String statusneu = "";
		String userInTabelle = "";

		for (int row = 0; row <= HauptFenster.table.getRowCount() - 1; row++) {
			userInTabelle = (String) HauptFenster.table.getValueAt(row, 2);
			if (userInTabelle.equalsIgnoreCase(nameGespraech)) {
				statusneu = (String) HauptFenster.table.getValueAt(row, 1);
				break;
			}
		}
		this.status = statusneu;
		online_lbl.setText(this.status);
		if (status.equals("Online")) {
			online_lbl.setForeground(Color.GREEN);
		} else {
			online_lbl.setForeground(Color.RED);
		}
	}

	public void UpdateStatusServerLost() {

		this.status = "Verb.Abbruch";
		online_lbl.setText(this.status);
		if (status.equals("Online")) {
			online_lbl.setForeground(Color.GREEN);
		} else {
			online_lbl.setForeground(Color.RED);
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @return
	 */
	@SuppressWarnings("serial")
	private JFrame initialize() {
		frame = new JFrame() {
			private void BringToFront() {
				java.awt.EventQueue.invokeLater(new Runnable() {
					@Override
					public void run() {
						if (frame != null) {
							frame.toFront();
							frame.repaint();
						}
					}
				});
			}
		};
		frame.setResizable(false);
		frame.setBounds(HauptFenster.frame.getX() - width - (border * 2),
				HauptFenster.frame.getY(), width, height);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);// EXIT_ON_CLOSE
		frame.getContentPane().setLayout(null);

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				frame.setAlwaysOnTop(true);
				frame.setAlwaysOnTop(false);
			}
		});

		panel_1 = new JPanel();
		panel_1.setForeground(Color.ORANGE);
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(border, border, width - (border * 3), height
				- (border * 6));
		panel_1.setLayout(null);
		panel_1.setOpaque(false);

		online_lbl = new JLabel("On");
		online_lbl.setHorizontalAlignment(SwingConstants.LEFT);

		online_lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		online_lbl.setBounds(textSize + (border * 2), pictureSize
				+ (border * 3) + 14, 70, 14);
		online_lbl.setText(status);

		if (status.equals("Online")) {
			online_lbl.setForeground(Color.GREEN);
		} else {
			online_lbl.setForeground(Color.RED);
		}

		panel_1.add(online_lbl);

		JButton send_btn = new JButton("Senden");
		send_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Client.send(HauptFenster.username, nameGespraech,
						txtField.getText());
				txtField.setText("");
			}
		});
		send_btn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		send_btn.setBounds(textSize + border - 100, (border * 5) + 240, 100, 20);
		panel_1.add(send_btn);

		JButton dataFetch_btn = new JButton("Empfange");
		dataFetch_btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				FileReceiverThread fr = new FileReceiverThread(txtField
						.getText());
				try {
					fr.domain();
				} catch (IOException e) {
					e.printStackTrace();
				}
				txtField.setText("");
			}

		});
		dataFetch_btn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		dataFetch_btn.setBounds(textSize + border - 330, (border * 5) + 240,
				100, 20);
		panel_1.add(dataFetch_btn);

		JButton dataSend_btn = new JButton("Datei Senden");
		dataSend_btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(HauptFenster.desktopPath));
				int rueckgabeWert = chooser.showDialog(null, "Auswählen");

				if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {

					String zuVerschickendeDatei = chooser.getSelectedFile()
							.getAbsolutePath();
					System.out.println("Die zu verschickende Datei ist: "
							+ zuVerschickendeDatei);
					FileSenderThread fs = new FileSenderThread(
							zuVerschickendeDatei);
					fs.setName("1A FileSenderThread");

					fs.start();
				}
			}

		});
		dataSend_btn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		dataSend_btn.setBounds(textSize + border - 240, (border * 5) + 240,
				130, 20);
		panel_1.add(dataSend_btn);

		txtField = new JTextPane() {
			public boolean getScrollableTracksViewportWidth() {
				return getUI().getPreferredSize(this).width <= getParent()
						.getSize().width;
			}
		};
		txtField.setFont(new Font("Miriam", Font.PLAIN, 12));
		txtField.setContentType("text/html");
		txtField.setBounds(border, (border * 4) + 160, textSize, 80);

		txtField.setBorder(raisedetched);
		txtField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_SPACE) {
					// Letztes Word auslesen
					// Nach Smiley Code aussuchen
					// ggf. Smiley Icon Kreieren
				}

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					Client.send(HauptFenster.username, nameGespraech,
							txtField.getText());
					txtField.setText("");
				}
			}

		});
		panel_1.add(txtField);

		txtPanel = new JTextArea();
		txtPanel.setFont(new Font("Miriam", Font.PLAIN, 12));
		txtPanel.setBounds(border, border, textSize, 160);
		txtPanel.setEditable(false);
		txtPanel.setLineWrap(true);
		txtPanel.setWrapStyleWord(true);

		JScrollPane sp = new JScrollPane(txtPanel);
		sp.add(txtPanel);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setViewportView(txtPanel);
		sp.setBounds(border, border, textSize, 160);
		sp.setBorder(raisedetched);
		panel_1.add(sp);

		JLabel label = new JLabel("");
		label.setIcon(new ImageIcon(ChatFenster.class
				.getResource("/data/2.png")));
		label.setBounds(textSize + (border * 2), border, pictureSize,
				pictureSize);
		panel_1.add(label);

		JLabel ichbild_lbl = new JLabel("");
		ichbild_lbl.setIcon(new ImageIcon(ChatFenster.class
				.getResource("/data/1.jpg")));
		ichbild_lbl.setBounds(textSize + (border * 2), 160 + (border * 4),
				pictureSize, pictureSize);
		panel_1.add(ichbild_lbl);

		name_lbl = new JLabel("test");
		name_lbl.setForeground(Color.white);

		name_lbl.setHorizontalAlignment(SwingConstants.LEFT);
		name_lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		name_lbl.setBounds(textSize + (border * 2), pictureSize + (border * 2),
				70, 14);
		panel_1.add(name_lbl);

		name_lbl.setText(nameGespraech);
		frame.getContentPane().add(panel_1);
		frame.add(new Gradients(new Color(27, 130, 165), new Color(204, 204,
				255), width, height));

		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				int index = 0;
				for (ChatFenster CF : HauptFenster.ChatFensterList) {
					if (CF.nameGespraech.equals(nameGespraech)) {
						break;
					}
					index++;
				}
				HauptFenster.ChatFensterList
						.remove(HauptFenster.ChatFensterList.get(index));
			}

		});
		return frame;
	}

	public String getLastWord() {
		String lastWord = null;
		return lastWord;
	}
}
