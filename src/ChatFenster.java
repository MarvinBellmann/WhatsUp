import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

	private JFrame frame;
	JTextPane txtField;
	public String nameGespraech;
	static JLabel name_lbl;
	private static int width = 450;
	private static int border = 7;
	private static int height = 340;
	private static int pictureSize = 69;
	private static int textSize = 330;
	public  JTextArea txtPanel;
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
		//System.out.println(nameGespraech);
		/*EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ChatFenster window = new ChatFenster();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});*/
	}

	/**
	 * Create the application.
	 */
	public ChatFenster(String name) {
	    this.nameGespraech = name;
	   
		initialize();
		 frame.setVisible(true);
		//domain(name);
	}
	
	

	/**
	 * Initialize the contents of the frame.
	 * @return 
	 */
	
	//private void
	
	
	
	private JFrame initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(HauptFenster.frame.getX() - width - (border * 2),
				HauptFenster.frame.getY(), width, height);
		frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);// EXIT_ON_CLOSE
		frame.getContentPane().setLayout(null);

		panel_1 = new JPanel();
		panel_1.setForeground(Color.ORANGE);
		panel_1.setBackground(Color.LIGHT_GRAY);
		panel_1.setBounds(border, border, width - (border * 3), height
				- (border * 6));
		panel_1.setLayout(null);
		panel_1.setOpaque(false);

		JLabel online_lbl = new JLabel("Online");
		online_lbl.setHorizontalAlignment(SwingConstants.LEFT);
		online_lbl.setForeground(Color.GREEN);
		online_lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		online_lbl.setBounds(textSize + (border * 2), pictureSize
				+ (border * 3) + 14, 70, 14);
		panel_1.add(online_lbl);

		JButton send_btn = new JButton("Senden");
		send_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

			  //  if(txtField.getText().equals("\n")==false && txtField.getText().equals("")==false){
				Client.send(HauptFenster.username,nameGespraech,txtField.getText());
				txtField.setText("");
			   // }

			}
		});
		send_btn.setFont(new Font("Tahoma", Font.PLAIN, 12));
		send_btn.setBounds(textSize + border - 100, (border * 5) + 240, 100, 20);
		panel_1.add(send_btn);

		
		txtField = new JTextPane(){
		    public boolean getScrollableTracksViewportWidth()
		    {
		        return getUI().getPreferredSize(this).width 
		            <= getParent().getSize().width;
		    }
		};
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
				   
				  //  if(txtField.getText().equals("\n")==false && txtField.getText().equals("")==false){
				    Client.send(HauptFenster.username, nameGespraech,txtField.getText());
				   //System.out.println(HauptFenster.username + " an "+  name_lbl.getText());
				    txtField.setText("");
				  //  }
				   
				}
				 
			}
		});		
		panel_1.add(txtField);

		
		txtPanel = new JTextArea();
		txtPanel.setFont(new Font("Miriam", Font.PLAIN, 14));
		//txtPanel.setText("Test (XX:XX:XX): Hey diggie ");
		txtPanel.setBounds(border, border, textSize, 160);
		txtPanel.setEditable(false);
		//txtPanel.setBorder(raisedetched);		
		txtPanel.setLineWrap(true);
		txtPanel.setWrapStyleWord(true);
		//panel_1.add(txtPanel);
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
		name_lbl.setHorizontalAlignment(SwingConstants.LEFT);
		name_lbl.setFont(new Font("Tahoma", Font.BOLD, 14));
		name_lbl.setBounds(textSize + (border * 2), pictureSize + (border * 2),
				70, 14);
		panel_1.add(name_lbl);

		name_lbl.setText(nameGespraech);
		frame.getContentPane().add(panel_1);
		//frame.add(new Gradients(Color.green.darker(), Color.green, width,height));
		frame.add(new Gradients(Color.green.darker(), new Color(0, 255, 127), width,height));
		
		
		frame.addWindowListener(new WindowAdapter() {
	            public void windowClosing(WindowEvent e) {
	                //ExitAction.getInstance().actionPerformed(null);
	        	
	        	
	        	int index=0;
	        	for(ChatFenster CF: HauptFenster.ChatFensterList){
	        	    
	        	    if(CF.nameGespraech.equals(nameGespraech)){
	        		
	        		break;
	        		//HauptFenster.ChatFensterList.remove(CF);
	        	    }
	        	    index++;
	        	}
	        	
	        	
	        	HauptFenster.ChatFensterList.remove(HauptFenster.ChatFensterList.get(index));
	            }
	       });
		return frame;
	}

	public String getLastWord() {
		String lastWord = null;

		return lastWord;
	}
}
