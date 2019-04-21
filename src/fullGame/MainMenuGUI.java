package fullGame;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fullGame.PlayerSelectScreen;
import jdbc.JDBConnector;

@SuppressWarnings("serial")
public class MainMenuGUI extends JFrame {

	private JPanel contentPane;
	private String Username = getUsername();
	private int UserXP;

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	/**
	 * Create the frame.
	 */
	public MainMenuGUI(String PassUsername) {
		
		setUsername(PassUsername);
		setUserXP(getUsernameXP(getUsername()));
		
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
		
		JLabel lblusername = new JLabel("Username: " + getUsername());
		lblusername.setFont(new Font("Tahoma", Font.PLAIN, 23));
		lblusername.setBounds(10, 11, 279, 28);
		panel.add(lblusername);
		
		JLabel lblxp = new JLabel(getUserXP() + "XP");
		lblxp.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblxp.setBounds(570, 7, 46, 39);
		panel.add(lblxp);
		
		JButton btnPlay = new JButton("Play");
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
				PlayerSelectScreen newFrayPlayerSelectScreen = new PlayerSelectScreen(getUsername());
				newFrayPlayerSelectScreen.getPassFrame().setVisible(true);
				//newFrayPlayerSelectScreen.contentPane.setVisible(true);
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
	
	public int getUserXP() {
		return UserXP;
	}

	public void setUserXP(int userXP) {
		UserXP = userXP;
	}

	private int getUsernameXP(String username) {
		try {
			Connection con = JDBConnector.getCon();
			String query1 = "SELECT UserXP from sql3282320.UserDataTable WHERE Username = ?";
			PreparedStatement ps = con.prepareStatement(query1);
			ps.setString(1, getUsername());
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
