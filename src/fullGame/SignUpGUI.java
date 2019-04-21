package fullGame;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import jdbc.JDBConnector;
/**
 * @author Gagandeep Ghotra, Sang Heon Park, Zain Razvi, Lee Fyye
 * This class displays out a JFrame that allows the user 
 * to create a new account. This new account is sent to the database
 * and the user is sent back to the login page.
 */
@SuppressWarnings("serial")
public class SignUpGUI extends JFrame {

	private JPanel contentPane;
	private JTextField uTF; // Username text field
	private JPasswordField pTF; // Password text field

	/**
	 * Create the frame.
	 */
	public SignUpGUI() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 499);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblSignUp = new JLabel("Sign Up");
		lblSignUp.setFont(new Font("Tahoma", Font.PLAIN, 22));
		lblSignUp.setBounds(173, 11, 107, 62);
		contentPane.add(lblSignUp);
		
		uTF = new JTextField();
		uTF.setBounds(172, 150, 179, 20);
		contentPane.add(uTF);
		uTF.setColumns(10);
		
		pTF = new JPasswordField();
		pTF.setBounds(172, 199, 179, 20);
		contentPane.add(pTF);
		
		JLabel lblUsername = new JLabel("Username:");
		lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblUsername.setBounds(87, 150, 98, 14);
		contentPane.add(lblUsername);
		
		JLabel lblPassword = new JLabel("Password:");
		lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblPassword.setBounds(87, 199, 98, 14);
		contentPane.add(lblPassword);
		
		JButton btnSignUp = new JButton("Create my account");
		btnSignUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					signUp();
				} catch (IOException e) {
					e.printStackTrace(); // Make exception nicer
				}
			}
		});
		btnSignUp.setBounds(131, 339, 179, 74);
		contentPane.add(btnSignUp);
		
		JCheckBox chckbxShowPassword = new JCheckBox("Show Password");
		chckbxShowPassword.setBounds(270, 226, 158, 23);
		chckbxShowPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(chckbxShowPassword.isSelected()){
					pTF.setEchoChar((char)0);
				}else{
					pTF.setEchoChar('*');
				}
			}
		});
		contentPane.add(chckbxShowPassword);
		
		JButton btnBack = new JButton("Back");
		btnBack.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				LoginGUI newFrayLoginGUI = new LoginGUI();
				newFrayLoginGUI.setVisible(true);
				newFrayLoginGUI.getContentPane().setVisible(true);
			}
		});
		btnBack.setBounds(0, 0, 128, 73);
		contentPane.add(btnBack);
	}
	
	private void signUp() throws IOException{
		boolean connectionEstablished = JDBConnector.isConnectionEstablished();
		System.out.println(connectionEstablished);
		if(connectionEstablished != false) {	
			try {
				Connection con = JDBConnector.getCon();
				String query1 = "INSERT INTO sql3282320.UserDataTable(Username, Password) VALUES(?, ?)";
				PreparedStatement ps = con.prepareStatement(query1);
				ps.setString(1, uTF.getText());
				ps.setString(2, String.valueOf(pTF.getPassword()));
				ps.execute();
				JOptionPane.showMessageDialog(null, "Account created successfully! You can go login now!");
				
				String query2 = "CREATE TABLE " + uTF.getText() + "_SentGameInvitesTable LIKE User_SentGameInvitesTable;";
		
				
				String query3 = "CREATE TABLE " + uTF.getText() + "_ReceivedGamedInvitesTable LIKE User_ReceivedGameInvitesTable;";
			
				PreparedStatement ps1 = con.prepareStatement(query2); // Runs the create new table command for that user (query2)
				PreparedStatement ps2 = con.prepareStatement(query3); // Runs the create new table command for that user (query3)
				ps1.execute();
				ps2.execute();
				
				dispose();
				LoginGUI newFrayLoginGUI = new LoginGUI();
				newFrayLoginGUI.setVisible(true);
				//newFrayLoginGUI.contentPane.setVisible(true);
			} catch (SQLException e) {
				e.printStackTrace(); // Add a nicer sqlexception to be displayed out to the user
			}
		}
	}
}
