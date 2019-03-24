package LoginandRegistrationGUIs;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import Game.FrayGameMainMenu;
import JDBC.JDBConnector;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JRadioButton;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;


public class FraySignUpGUI extends JFrame {

	JPanel contentPane;
	private JTextField uTF;
	private JPasswordField pTF;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FraySignUpGUI frame = new FraySignUpGUI();
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
	public FraySignUpGUI() {
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
				FrayLoginGUI newFrayLoginGUI = new FrayLoginGUI();
				newFrayLoginGUI.setVisible(true);
				newFrayLoginGUI.getContentPane().setVisible(true);
			}
		});
		btnBack.setBounds(0, 0, 128, 73);
		contentPane.add(btnBack);
	}
	
	private void signUp() throws IOException{
			//JDBCTest.testMethod();
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
					
					//String query2 = "INSERT INTO " + uTF.getText() + "_SentGameInvitesTable SELECT * FROM User_SentGameInvitesTable;";
					String query2 = "CREATE TABLE " + uTF.getText() + "_SentGameInvitesTable LIKE User_SentGameInvitesTable;";
					/* This copies the sent game invites table and creates a new table that is under the username of the user
					 * with the same column and data structure.
					 */
					
					String query3 = "CREATE TABLE " + uTF.getText() + "_ReceivedGamedInvitesTable LIKE User_ReceivedGameInvitesTable;";
					//String query3 = "INSERT INTO " + uTF.getText() + "_ReceivedGameInvitesTable SELECT * FROM User_ReceivedGameInvitesTable;";
					// Create an sql command that creates a new sql database table which checks for game invites. 
					/* For the game invites, the sender can send it to the user's specific game invite database. There could be one column that is named response.
					 * When the recieving user accepts the gameinvite, the response is changed from undecieded to accpeted or declined if the user declines.
					 * If the user declines, the program will see that and delete that specific row, it is also deleted on the sender's sent game invites table.
					 * There can be two tables; sent game invites (which stores the sent game invites, that originate from the show players tab), and recieved game invites.
					 * If accepted, then the response column in the specific game invite is changed from undecied to accepted. And a new database table is made. 
					 * 
					 * This table's refresh will be running on a new thread which is started in the frayplayerselectscreen.
					 * This new thread will keep on refreshing/running forever, checking for new rows that are added to the field.
					 * When a new row is added to this user's game invite table, The user can see the name of the person who invited and accept
					 * that invite somehow (JOptionpane or click on row and click button). Once accepted a method will be run that will send a accepted packet
					 * back to the game invite sender and the sender's program will now create a new databasetable which is *senderusername*_vs_*recieverusername*.
					 * The first five rows of this table will have the arraylist of the senderusername's gamefield, and the other five rows will be the arraylist of the 
					 * reciever's game field. The row will have this information:
					 * 		id, PlayerName, CardName, CardAttackPoints, CardArmorPoints, CardHealthPoints, cardClass, cardType, energyCost, cardState, spellValueHealth, spellValueAttack
					 * 
					 * Also you can have sort by online status by default on the online players. All sent game invites are deleted when the user goes offline.
					 */
					PreparedStatement ps1 = con.prepareStatement(query2); // Runs the create new table command for that user (query2)
					PreparedStatement ps2 = con.prepareStatement(query3); // Runs the create new table command for that user (query3)
					ps1.execute();
					ps2.execute();
					
					dispose();
					FrayLoginGUI newFrayLoginGUI = new FrayLoginGUI();
					newFrayLoginGUI.setVisible(true);
					newFrayLoginGUI.contentPane.setVisible(true);
				} catch (SQLException e) {
					e.printStackTrace(); // Add a nicer sqlexception to be displayed out to the user
				}
			}
	}
	
}