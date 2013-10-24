package Client;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;
import SendData.Message;
import SendData.SQLData;
import SendData.StartData;

public class AnmeldeFenster {

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
					new AnmeldeFenster();

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
     */
	public AnmeldeFenster() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setSize(250, 260);
		frame.setLocation(frame.getLocation().x - (frame.getWidth() / 2),
				frame.getLocation().y - (frame.getHeight() / 2));
		frame.setVisible(true);

		contentPane = new GradientPanel(new Color(27, 130, 165), new Color(204,
				204, 255));
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		frame.setContentPane(contentPane);
		contentPane
				.setLayout(new MigLayout("wrap 1, fillx", "[]", "[]14px[][]"));

		JLabel lblWhatsup = new JLabel("WAK-enger!");
		lblWhatsup.setHorizontalAlignment(SwingConstants.CENTER);
		lblWhatsup.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblWhatsup.setForeground(Color.white);
		contentPane.add(lblWhatsup, "span,alignx center,aligny center");

		JLabel lblName = new JLabel("Name");
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		lblName.setFont(new Font("Tahoma", Font.PLAIN, 14));
		contentPane.add(lblName);

		txtName = new JTextField();
		txtName.setText("Admin");
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
		textPW.setText("1234");
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

		JButton btnAnmelden = new JButton("Anmelden");
		btnAnmelden.addActionListener(new ActionListener() {

			@SuppressWarnings("deprecation")
			public void actionPerformed(ActionEvent arg0) {

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
									+ "' and password like '"
									+ textPW.getText()
									+ "' and status like 'Offline';"));
					System.out.println("Warte auf ServerAntwort");
					Object obj = ois.readObject();
					System.out.println("ServerAntwort bekommen");
					if (obj instanceof Message) {
						sqlMessage = (Message) obj;
						System.out.println(sqlMessage.text);
					}
					if (sqlMessage.text.contains("DB->* Keine Eintr�ge") == false) {
						anmeldeDatenAkzeptiert = true;
					}
					System.out.println("Schlie�e Verbindungen und Socket");
					oos.close();
					ois.close();
					socket.close();
					System.out.println("Verbindungen und Socket geschlossen");

				} catch (Exception e) {
					System.out.println("Anmeldung nich m�glich: "
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
						new HauptFenster(txtName.getText(), textPW.getText(),
								textServer.getText());
						HauptFenster.frame.setVisible(true);
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
		});
		btnAnmelden.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(btnAnmelden, "split 2, growx");

		JButton btnRegistrieren = new JButton("Registrieren");
		btnRegistrieren.addActionListener(new ActionListener() {
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
					if (sqlMessage.text.contains("DB->* Keine Eintr�ge")) {
						registrierungsDatenAkzeptiert = true;
						System.out.println("darf inserten");
						oos.writeObject(new SQLData("AnmeldeDbChecker",
								"INSERT INTO user (username,password,create_time,status,picture) VALUES ('"
										+ txtName.getText() + "','"
										+ textPW.getText()
										+ "',now(),'Offline','1')"));
						oos.writeObject(new SQLData("AnmeldeDbChecker",
								"INSERT INTO contacts (username,contact) VALUES ('"
										+ txtName.getText() + "','Admin')"));

					}
					oos.close();
					ois.close();
					socket.close();

				} catch (Exception e) {
					System.out.println("Registrierung nich m�glich: "
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
		btnRegistrieren.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPane.add(btnRegistrieren);

		// JButton btnPing = new JButton("Ping");
		// btnPing.addActionListener(new ActionListener() {
		// public void actionPerformed(ActionEvent arg0) {
		// try {
		// InetAddress host = InetAddress.getByName(textServer
		// .getText());// InetAddress.getLocalHost();
		// Socket socket = new Socket(host.getHostName(), 7866);
		// ObjectOutputStream oos = new ObjectOutputStream(socket
		// .getOutputStream());
		// ObjectInputStream ois = new ObjectInputStream(socket
		// .getInputStream());
		// oos.close();
		// ois.close();
		// socket.close();
		// JOptionPane.showMessageDialog(null, "Ping erfolgreich!");
		//
		// } catch (Exception e) {
		// System.out.println("Konnte nicht anpingen!");
		// JOptionPane
		// .showMessageDialog(null,
		// "Ping fehlgeschlagen. Es l�uft kein JavaServer auf der IP");
		//
		// }
		//
		// }
		// });
		//
		// contentPane.add(btnPing, "cell 0 5,grow");

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
}
