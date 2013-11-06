package Client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import SendData.Message;
import SendData.SQLData;
import SendData.StartData;

public class LoginFrame {

	private JPanel contentPane;
	private JTextField txtName;
	private JPasswordField textPW;
	private JTextField textServer;
	private JFrame frame;
	Message sqlMessage;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	Socket socket;
	int counterReset = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager
							.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
					new LoginFrame();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
     */
	public LoginFrame() {
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage(
				getClass().getClassLoader().getResource("data/Logo.png")));
		frame.setTitle("Anmeldung");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(250, 340);
		frame.setLocation(frame.getLocation().x - (frame.getWidth() / 2),
				frame.getLocation().y - (frame.getHeight() / 2));
		frame.setVisible(true);

		contentPane = new GradientPanel(new Color(27, 130, 165), new Color(204,
				204, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane
				.setLayout(new MigLayout("wrap 1, fillx", "[]", "[]14px[][]"));

		JLabel lblWhatsup = new JLabel("");
		ImageIcon logo = new ImageIcon(getClass().getClassLoader().getResource(
				"data/Logo.png"));
		logo.setImage(logo.getImage().getScaledInstance(70, 70,
				Image.SCALE_DEFAULT));
		lblWhatsup.setIcon(logo);
		contentPane.add(lblWhatsup, "alignx center, aligny top, h 60px::60px");

		JLabel title = new JLabel("WAKenger!");
		title.setHorizontalAlignment(SwingConstants.CENTER);
		title.setFont(new Font("Tahoma", Font.BOLD, 16));
		title.setForeground(Color.white);
		contentPane.add(title, "span,alignx center,aligny top, h 18px::18px");

		JLabel lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblName);

		txtName = new JTextField();
		txtName.setText("");
		txtName.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						txtName.selectAll();
					}
				});
			}
		});
		contentPane.add(txtName, "growx");
		txtName.setColumns(10);

		JLabel lblPasswort = new JLabel("Passwort");
		lblPasswort.setHorizontalAlignment(SwingConstants.LEFT);
		lblPasswort.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblPasswort);

		textPW = new JPasswordField(25);
		textPW.setText("");
		textPW.setColumns(10);
		textPW.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						textPW.selectAll();
					}
				});
			}
		});

		textPW.getInputMap().put(KeyStroke.getKeyStroke("ENTER"), "doNothing");
		textPW.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {

				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					anmelden();
				}
			}

		});

		
		
		contentPane.add(textPW, "growx");
		
		
		
		
		
		
		
		
		
		

		JLabel lblServerip = new JLabel("Server-IP");
		lblServerip.setHorizontalAlignment(SwingConstants.LEFT);
		lblServerip.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblServerip);

		textServer = new JTextField();
		textServer.setText("localhost");// "localhost");
		textServer.addFocusListener(new java.awt.event.FocusAdapter() {
			public void focusGained(java.awt.event.FocusEvent evt) {
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						textServer.selectAll();
					}
				});
			}
		});
		textServer.setColumns(10);
		contentPane.add(textServer, "growx");

		JButton btnLogin = new JButton("Anmelden");
		btnLogin.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {

				anmelden();
			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(btnLogin, "split 2, growx");

		JButton btnRegister = new JButton("Registrieren");
		btnRegister.addActionListener(new ActionListener() {
			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {

				boolean registrierungsDatenAkzeptiert = false;

				try {
					InetAddress host = InetAddress.getByName(textServer
							.getText());// InetAddress.getLocalHost();
					Socket socket = new Socket(host.getHostName(), 7866);
					socket.setTcpNoDelay(true);
					socket.setSoTimeout(5000);
					ObjectOutputStream oos = new ObjectOutputStream(socket
							.getOutputStream());
					ObjectInputStream ois = new ObjectInputStream(socket
							.getInputStream());
					oos.writeObject(new StartData("Anmelder"));
					oos.writeObject(new SQLData("AnmeldeDbChecker",
							"Select * From user where username like '"
									+ txtName.getText() + "';"));
					Object obj = ois.readObject();
					if (obj instanceof Message) {
						sqlMessage = (Message) obj;
						System.out.println(sqlMessage.text);
					}
					if (sqlMessage.text.contains("DB->* Keine Einträge")) {
						registrierungsDatenAkzeptiert = true;
						System.out.println("darf inserten");
						oos.writeObject(new SQLData("AnmeldeDbChecker",
								"INSERT INTO user (username,password,create_time,status,picture) VALUES ('"
										+ txtName.getText() + "', SHA1('"
										+ textPW.getText()
										+ "'),now(),'Offline','1')"));
						oos.writeObject(new SQLData("AnmeldeDbChecker",
								"INSERT INTO contacts (username,contact) VALUES ('"
										+ txtName.getText() + "','Admin')"));

					}
					oos.close();
					ois.close();
					socket.close();

				} catch (Exception e) {
					System.out.println("Registrierung nich möglich: "
							+ e.getMessage());
				}

				if (registrierungsDatenAkzeptiert == false) {
					JOptionPane
							.showMessageDialog(null,
									"Registrierung Fehlgeschlagen! Nutzer bereits registriert? ServerIP falsch?");
				} else {
					JOptionPane.showMessageDialog(null,
							"Registrierung Erfolgreich! :)");

				}
			}
		});
		btnRegister.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(btnRegister);

		JLabel lblYourIp = new JLabel("Your IP: ");
		lblYourIp.setHorizontalAlignment(SwingConstants.LEFT);
		lblYourIp.setFont(new Font("Tahoma", Font.PLAIN, 10));
		contentPane.add(lblYourIp, "span,alignx center,aligny center");

		String yourip = null;
		try {
			yourip = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {

			e.printStackTrace();
		}

		lblYourIp.setText(lblYourIp.getText() + yourip);
	}

	protected void anmelden() {
		boolean anmeldeDatenAkzeptiert = false;

		try {
			InetAddress host = InetAddress.getByName(textServer
					.getText());// InetAddress.getLocalHost();
			System.out.println(host.getHostAddress());
			socket = new Socket(host.getHostAddress(), 7866);
			socket.setTcpNoDelay(true);
			socket.setSoTimeout(5000);
			oos = new ObjectOutputStream(socket.getOutputStream());
			ois = new ObjectInputStream(socket.getInputStream());
			oos.writeObject(new StartData("Anmelder"));
			oos.writeObject(new SQLData("AnmeldeDbChecker",
					"Select * From user where username like '"
							+ txtName.getText()
							+ "' and password = SHA1('"
							+ textPW.getText()
							+ "') and status like 'Offline';"));
			System.out.println("Warte auf ServerAntwort");
			Object obj = ois.readObject();
			System.out.println("ServerAntwort bekommen");
			if (obj instanceof Message) {
				sqlMessage = (Message) obj;
				System.out.println(sqlMessage.text);
			}
			if (sqlMessage.text.contains("DB->* Keine Einträge") == false) {
				anmeldeDatenAkzeptiert = true;
			}
			System.out.println("Schließe Verbindungen und Socket");
			oos.close();
			ois.close();
			socket.close();
			System.out.println("Verbindungen und Socket geschlossen");

		} catch (Exception e) {
			System.out.println("Anmeldung nich möglich: "
					+ e.getMessage());
			try {
				oos.close();
				ois.close();
				socket.close();
			} catch (Exception e2) {
				System.out
						.println("Fehler nach Fehlerwiedergutmachungsversuch: "
								+ e2.getMessage());
			}
		}

		if (anmeldeDatenAkzeptiert == true) {
			System.out
					.println("Anmeldung akzeptiert: Starte HauptFenster");

			try {
				new MainFrame(txtName.getText(), textPW.getText(),
						textServer.getText());
				MainFrame.frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
			frame.hide();
		} else {
			JOptionPane
					.showMessageDialog(
							null,
							"Anmeldung Fehlgeschlagen! Name oder Passwort vergessen? ServerIP falsch? Benutzer bereits angemeldet?");
		}
	}
}
