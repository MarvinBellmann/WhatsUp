package Client;

import java.awt.EventQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import net.miginfocom.swing.MigLayout;

public class testframe extends JFrame {

	private JPanel contentPane;
	private JTextField txtBenutzer;
	private JTextField textField;
	private JTextField textField_1;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					testframe frame = new testframe();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public testframe() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 252, 239);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new MigLayout("", "[226px]",
				"[22px][22px][22px][22px][22px][22px][22px][27px]"));

		JLabel lblWakenger = new JLabel("WAKenger");
		lblWakenger.setHorizontalAlignment(SwingConstants.CENTER);
		contentPane.add(lblWakenger, "cell 0 0,grow");

		JLabel lblName = new JLabel("Name");
		contentPane.add(lblName, "cell 0 1,grow");

		txtBenutzer = new JTextField();
		txtBenutzer.setText("Benutzer");
		contentPane.add(txtBenutzer, "cell 0 2,grow");
		txtBenutzer.setColumns(10);

		JLabel lblPasswort = new JLabel("Passwort");
		contentPane.add(lblPasswort, "cell 0 3,grow");

		textField = new JTextField();
		textField.setText("1234");
		contentPane.add(textField, "cell 0 4,grow");
		textField.setColumns(10);

		JLabel lblIp = new JLabel("IP#");
		contentPane.add(lblIp, "cell 0 5, grow");

		textField_1 = new JTextField();
		textField_1.setText("192.168.0.1");
		contentPane.add(textField_1, "cell 0 5,grow");
		textField_1.setColumns(10);

		JButton btnAnmelden = new JButton("Anmelden");
		contentPane.add(btnAnmelden, "flowx,cell 0 7");

		JButton btnNewButton = new JButton("New button");
		contentPane.add(btnNewButton, "cell 0 7");
	}
}
