package Game;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import JDBC.JDBConnector;
import LoginandRegistrationGUIs.FrayLoginGUI;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Font;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;

@SuppressWarnings("serial")
public class FrayGameMainMenu extends JFrame {

	public JPanel contentPane;
	static String Username = FrayLoginGUI.uTF.getText();
	private int UserXP = getUsernameXP(Username);
	//private String Username;
	//private int UserXP;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FrayGameMainMenu frame = new FrayGameMainMenu();
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
	public FrayGameMainMenu() {
		
		setBounds(100, 100, 874, 534);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent we) {
			int value = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?");
			
			if(value == JOptionPane.YES_OPTION) {
				Connection con = JDBConnector.getCon();
				 /*
     			  * This part of code updates the online status on the server.
     			  */ 
     			 try {
     				java.sql.Statement myStat;
					myStat = con.createStatement();
					String sql = "UPDATE sql3282320.UserDataTable SET OnlineStatus = 'Offline' WHERE Username = '" + Username + "'";
					myStat.executeUpdate(sql);
					System.exit(0);
				} catch (SQLException e1) {
					e1.printStackTrace(); // Make error nicer
				}
				System.exit(0);
			}
			
			else {//Do Nothing
			}
				}
			});
		
		JPanel panel = new JPanel();
		panel.setBounds(-4, 0, 862, 86);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JLabel lblusername = new JLabel("Username: " + Username);
		lblusername.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblusername.setBounds(10, 11, 279, 28);
		panel.add(lblusername);
		
		JLabel lblxp = new JLabel(UserXP + "XP");
		lblxp.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblxp.setBounds(570, 7, 46, 39);
		panel.add(lblxp);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				FrayPlayerSelectScreen newFrayPlayerSelectScreen = new FrayPlayerSelectScreen();
				newFrayPlayerSelectScreen.frame.setVisible(true);
				newFrayPlayerSelectScreen.contentPane.setVisible(true);
			}
		});
		btnPlay.setBounds(322, 102, 210, 92);
		contentPane.add(btnPlay);
		
		JButton btnCredits = new JButton("Credits");
		btnCredits.setBounds(322, 205, 210, 92);
		contentPane.add(btnCredits);
		
		JButton btnExit = new JButton("Exit"); // Logout Code goes here
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Connection con = JDBConnector.getCon();
				 /*
      			  * This part of code updates the online status on the server.
      			  */ 
      			 try {
      				java.sql.Statement myStat;
					myStat = con.createStatement();
					String sql = "UPDATE sql3282320.UserDataTable SET OnlineStatus = 'Offline' WHERE Username = '" + Username + "'";
					myStat.executeUpdate(sql);
					System.exit(0);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnExit.setBounds(322, 308, 210, 92);
		contentPane.add(btnExit);
		
		JLabel backgroundLabel = new JLabel("");
		backgroundLabel.setBounds(0, 0, 858, 495);
		contentPane.add(backgroundLabel);
	}
	
	private int getUsernameXP(String username) {
		try {
			Connection con = JDBConnector.getCon();
			String query1 = "SELECT UserXP from sql3282320.UserDataTable WHERE Username = ?";
			PreparedStatement ps = con.prepareStatement(query1);
			ps.setString(1, username);
			ResultSet myRs = ps.executeQuery();
			while(myRs.next()) {
				UserXP = myRs.getInt("UserXP");
			}
		} catch (SQLException e) {
			e.printStackTrace(); // Make the Exception return be nicer.
		}
		
		return UserXP;
	}

}