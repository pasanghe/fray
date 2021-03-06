package fullGame;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import jdbc.JDBConnector;

@SuppressWarnings("serial")
public class LoginGUI extends JFrame {

	private JPanel contentPane;
	JTextField uTF;
	private JPasswordField pTF;
	private Thread ConnectionCheckThread;
	
	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	private String Username;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoginGUI frame = new LoginGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public LoginGUI() {
		
		ConnectionCheckThread = new Thread(new JDBConnector());
		ConnectionCheckThread.start();
		
		setResizable(false);
		setBounds(552, 210, 870, 450);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		
		setContentPane(contentPane);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		JLabel lblUsername = new JLabel("Username: ");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		uTF = new JTextField();
		uTF.setColumns(10);
		
		JLabel lblPassword = new JLabel("Password: ");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 14));
		
		JButton btnLogin = new JButton("Login");
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					checkCredintals();
				} catch (IOException e1) {
					e1.printStackTrace(); // Make the error display out nicer
				}
			}
		});
		btnLogin.setFont(new Font("Tahoma", Font.PLAIN, 19));
		
		pTF = new JPasswordField();
		
		Box horizontalBox = Box.createHorizontalBox();
		
		JLabel label = new JLabel(new ImageIcon(LoginGUI.class.getResource("../images/FRAY.png")));
		
		JCheckBox chckbxShowPassword = new JCheckBox("Show Password");
		chckbxShowPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxShowPassword.isSelected()){
					pTF.setEchoChar((char)0);
				}else{
					pTF.setEchoChar('*');
				}
			}
		});
		
		JButton btnSignUp = new JButton("Sign Up");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				 dispose();
       			 SignUpGUI newFraySignUpGUI = new SignUpGUI();
       			 newFraySignUpGUI.setVisible(true);
			}
		});
		btnSignUp.setFont(new Font("Tahoma", Font.PLAIN, 19));
		
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(46)
					.addComponent(horizontalBox, GroupLayout.DEFAULT_SIZE, 818, Short.MAX_VALUE)
					.addGap(0))
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 854, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(176, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(lblPassword, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(lblUsername, GroupLayout.PREFERRED_SIZE, 111, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(pTF)
						.addComponent(uTF, GroupLayout.PREFERRED_SIZE, 200, GroupLayout.PREFERRED_SIZE))
					.addGap(64)
					.addComponent(btnLogin, GroupLayout.PREFERRED_SIZE, 165, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(btnSignUp, GroupLayout.PREFERRED_SIZE, 108, GroupLayout.PREFERRED_SIZE)
					.addContainerGap())
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap(427, Short.MAX_VALUE)
					.addComponent(chckbxShowPassword, GroupLayout.PREFERRED_SIZE, 168, GroupLayout.PREFERRED_SIZE)
					.addGap(269))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addComponent(label, GroupLayout.PREFERRED_SIZE, 287, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
					.addComponent(horizontalBox, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addGap(37)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(btnSignUp, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(uTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblUsername))
							.addGap(9)
							.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(lblPassword)
								.addComponent(pTF, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(btnLogin, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(chckbxShowPassword)
					.addGap(31))
		);
		contentPane.setLayout(gl_contentPane);
	}
	
	private void checkCredintals() throws IOException {
		boolean connectionEstablished = JDBConnector.isConnectionEstablished();
		if(connectionEstablished != false) {
			try {
				
				Connection con = JDBConnector.getCon(); 
				PreparedStatement ps = con.prepareStatement("Select * from sql3282320.UserDataTable where Username = ? and Password = ?");
				ps.setString(1, uTF.getText());
				setUsername(uTF.getText());
				ps.setString(2, String.valueOf(pTF.getPassword()));
				ResultSet result = ps.executeQuery();
				 if(result.next()){	 
	       			 /*
	       			  * This part of code updates the online status on the server.
	       			  */
	       			 java.sql.Statement myStat;
	    			 myStat = con.createStatement();
	    			 String sql = "UPDATE sql3282320.UserDataTable SET OnlineStatus = 'Online' WHERE Username = '" + uTF.getText() + "'";
	       			 myStat.executeUpdate(sql);
	       			 
	       			 dispose();
	       			 MainMenuGUI newFrayGameMainMenu = new MainMenuGUI(getUsername());
	       			 newFrayGameMainMenu.setVisible(true);
	       			 //newFrayGameMainMenu.contentPane.setVisible(true);
	       		 }
				 
				 else {
		       			JOptionPane.showMessageDialog(null, "Invalid username or password");
		       			uTF.setText("");
		       			pTF.setText("");
		       	}
			} catch (SQLException e1) {
				JOptionPane.showMessageDialog(null, "Connection Error! Please check your internet connection!", "Connection Error", 0);
				JDBConnector.setConnectionEstablished(false);
				e1.printStackTrace();
			}
		}
	}
}