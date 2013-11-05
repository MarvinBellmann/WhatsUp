package Client;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;

import net.miginfocom.swing.MigLayout;

public class ChatFrame {

	public JFrame frame;
	public JTextPane txtField;
	public String nameChat;
	public String status;
	public JLabel name_lbl;
	public JLabel online_lbl;
	private static int width = 500;
	private static int height = 370;
	public JTextArea txtPanel;
	public JPanel panel_1;
	JLabel lblMyself;
	JLabel lbl;
	JButton dataSend_btn;

	private Border raisedetched = BorderFactory.createEtchedBorder(
			EtchedBorder.RAISED, Color.darkGray, Color.lightGray);

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
	public ChatFrame(String name, String status) {
		this.nameChat = name;
		this.status = status;

		initialize();
		frame.setVisible(true);
		updateStatus();
	}

	public void updateStatus() {

		ImageIcon avatar = null;
		String statusneu = "";
		String userInTabelle = "";
		String avatarFile = "";
		for (int row = 0; row <= MainFrame.table.getRowCount() - 1; row++) {
			ContactCard card = (ContactCard) MainFrame.table.getValueAt(row, 0);
			userInTabelle = card.getName();
			if (userInTabelle.equalsIgnoreCase(nameChat)) {
				statusneu = card.getStatus();
				avatarFile = card
						.getAvatar()
						.getDescription()
						.substring(
								card.getAvatar().getDescription()
										.lastIndexOf('/'));
				System.out.println(avatarFile);
				avatar = new ImageIcon(getClass().getClassLoader().getResource(
						"data" + avatarFile));
				break;
			}
		}
		if (this.nameChat.equalsIgnoreCase("ServerDB") == false
				&& avatarFile.equals("")) {

		}
		lblMyself.setIcon(MainFrame.label_2.getIcon());
		lbl.setIcon(avatar);
		this.status = statusneu;
		online_lbl.setText(this.status);
		if (status.equals("Online")) {
			online_lbl.setForeground(Color.GREEN);
		} else {
			if (status.equals("Abwesend")) {
				online_lbl.setForeground(Color.YELLOW);
			} else {
				online_lbl.setForeground(Color.RED);
			}
		}

		if (online_lbl.getText().equalsIgnoreCase("Online") == false
				|| this.nameChat.equalsIgnoreCase("ServerDB")) {
			dataSend_btn.setEnabled(false);
		} else {
			dataSend_btn.setEnabled(true);
		}

	}

	public void updateStatusServerLost() {

		this.status = "Verb.Abbruch";
		online_lbl.setText(this.status);
		if (status.equals("Online")) {
			online_lbl.setForeground(Color.GREEN);
		} else {
			if (status.equals("Abwesend")) {
				online_lbl.setForeground(Color.YELLOW);
			} else {
				online_lbl.setForeground(Color.RED);
			}
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * 
	 * @return
	 */
	@SuppressWarnings("serial")
	private JFrame initialize() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getClassLoader().getResource("data/Logo.png")));
		frame.setTitle("Chat mit " + nameChat);
		if (MainFrame.frame.getX() - width - 7 <= 0) {
			frame.setLocation(
					MainFrame.frame.getX() + MainFrame.frame.getWidth() + 7,
					MainFrame.frame.getY());
		} else {
			frame.setLocation(MainFrame.frame.getX() - width - 7,
					MainFrame.frame.getY());
		}
		frame.setMinimumSize(new Dimension(width, height));
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);// EXIT_ON_CLOSE

		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {
				frame.setAlwaysOnTop(true);
				frame.setAlwaysOnTop(false);
			}
		});

		panel_1 = new GradientPanel(new Color(27, 130, 165), new Color(204,
				204, 255));
		panel_1.setLayout(new MigLayout("fill", "[][][]",
				"[80px::80px][30px::30px][30px::30px][][80px:n:80px][30px:n:30px]"));

		// TextPanel
		txtPanel = new JTextArea();
		txtPanel.setFont(new Font("Miriam", Font.PLAIN, 12));
		txtPanel.setEditable(false);
		txtPanel.setLineWrap(true);
		txtPanel.setWrapStyleWord(true);
		// txtPanel.setContentType("text/html; charset=UTF-8");

		JScrollPane sp = new JScrollPane(txtPanel);
		sp.add(txtPanel);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		sp.setViewportView(txtPanel);
		sp.setBorder(raisedetched);
		panel_1.add(sp, "span 2 4,grow");

		// Image
		lbl = new JLabel("");
		lbl.setIcon(new ImageIcon(getClass().getClassLoader().getResource(
				"data/loading.gif")));
		panel_1.add(lbl, "aligny top,wrap");

		// Name
		name_lbl = new JLabel("test");
		name_lbl.setForeground(Color.white);
		name_lbl.setHorizontalAlignment(SwingConstants.LEFT);
		name_lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		panel_1.add(name_lbl, "wrap");
		name_lbl.setText(nameChat);

		// Online-Status
		online_lbl = new JLabel("On");
		online_lbl.setHorizontalAlignment(SwingConstants.LEFT);
		online_lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		online_lbl.setText(status);
		if (status.equals("Online")) {
			online_lbl.setForeground(Color.GREEN);
		} else {
			if (status.equals("Abwesend")) {
				online_lbl.setForeground(Color.YELLOW);
			} else {
				online_lbl.setForeground(Color.RED);
			}
		}
		panel_1.add(online_lbl, "wrap 50px");

		// TextFeld
		txtField = new JTextPane() {
			public boolean getScrollableTracksViewportWidth() {
				return getUI().getPreferredSize(this).width <= getParent()
						.getSize().width;
			}
		};
		txtField.setContentType("text/html; charset=UTF-8");
		txtField.setFont(new Font("Miriam", Font.PLAIN, 12));
		txtField.setBorder(raisedetched);
		// txtField.getDocument().putProperty("filterNewlines", Boolean.TRUE);
		txtField.getInputMap()
				.put(KeyStroke.getKeyStroke("ENTER"), "doNothing");
		txtField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					// if(txtField.getText().length()<1)
					if (toTextString(txtField.getText()).length() > 0) {
						System.out.println("TTT"
								+ toTextString(txtField.getText()) + "TTT"
								+ toTextString(txtField.getText()).length());
						Client.send(MainFrame.username, nameChat,
								txtField.getText());
						txtField.setText("");
					}
					// ((Object) e).preventDefault();
				}
			}

		});
		panel_1.add(txtField, "cell 0 4, spanx 2,grow");

		// Ich-Image
		lblMyself = new JLabel("");
		lblMyself.setIcon(new ImageIcon(getClass().getClassLoader()
				.getResource("data/loading.gif")));
		panel_1.add(lblMyself, "aligny top,wrap");

		// Datei senden
		dataSend_btn = new JButton("Datei senden");
		dataSend_btn.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent arg0) {
				JFileChooser chooser = new JFileChooser();
				chooser.setCurrentDirectory(new File(MainFrame.desktopPath));
				int rueckgabeWert = chooser.showDialog(null, "Auswählen");

				if (rueckgabeWert == JFileChooser.APPROVE_OPTION) {

					String zuVerschickendeDatei = chooser.getSelectedFile()
							.getAbsolutePath();
					System.out.println("Die zu verschickende Datei ist: "
							+ zuVerschickendeDatei);
					Client.sendBytes(MainFrame.username, nameChat,
							zuVerschickendeDatei);

					String zuVerschickendeDateiedit = zuVerschickendeDatei
							.replace('\\', '/');
					String fileName = zuVerschickendeDateiedit.substring(
							zuVerschickendeDateiedit.lastIndexOf('/') + 1,
							zuVerschickendeDateiedit.length());
					Client.send(MainFrame.username, nameChat,
							"### Die Datei " + fileName
									+ " wird gesendet und auf dem Desktop von "
									+ nameChat + " gespeichert. ###");
				}
			}

		});
		dataSend_btn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(dataSend_btn);

		// Senden-Button
		JButton send_btn = new JButton("Senden");
		send_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if (toTextString(txtField.getText()).length() > 0) {
					Client.send(MainFrame.username, nameChat,
							txtField.getText());
					txtField.setText("");
				}
			}
		});
		send_btn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		panel_1.add(send_btn, "alignx right");

		frame.getContentPane().add(panel_1);
		frame.addWindowListener(new WindowAdapter() {

			public void windowClosing(WindowEvent e) {
				int index = 0;
				for (ChatFrame CF : MainFrame.chatFrameList) {
					if (CF.nameChat.equals(nameChat)) {
						break;
					}
					index++;
				}
				MainFrame.chatFrameList.remove(MainFrame.chatFrameList
						.get(index));
			}

		});
		return frame;
	}

	public String getLastWord() {
		String lastWord = null;
		return lastWord;
	}

	@SuppressWarnings("deprecation")
	public void hiding() {
		// TODO Auto-generated method stub
		frame.hide();
	}

	public String toTextString(String text) {
		// html to string umformung der nachricht
		String kurztext = text;
		Pattern p = Pattern.compile("<(.*?)>");
		Matcher m = p.matcher(kurztext);
		while (m.find()) {
			kurztext = kurztext.replaceAll(m.group(), "");
		}
		kurztext = kurztext
				.replaceAll(System.getProperty("line.separator"), ""); // ("\\\n",
																		// "");//Replace(Nz(meinString,
																		// ""),
																		// vbCrLf,
																		// "")//string=
																		// string.replaceAll("\\\n",
																		// "<br />");
		kurztext = kurztext.replaceAll("    ", " ");
		kurztext = kurztext.replaceAll("   ", " ");
		kurztext = kurztext.replaceAll("  ", " ");
		kurztext = kurztext.replaceAll("  ", " ");
		if (kurztext.substring(0, 1).equalsIgnoreCase(" ")) {
			kurztext = kurztext.substring(1);
		}

		return (kurztext);// + System.getProperty("line.separator")
	}

}
