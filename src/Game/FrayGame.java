package Game;

import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import JDBC.JDBConnector;
import LoginandRegistrationGUIs.FrayLoginGUI;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.Font;

public class FrayGame {

	static JFrame FrayGameGUI;
	JPanel contentPane;
	static Timer timeObj;
	static String opponentUsername;

	public static String getOpponentUsername() {
		return opponentUsername;
	}

	public static void setOpponentUsername(String opponentUsername) {
		FrayGame.opponentUsername = opponentUsername;
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					@SuppressWarnings("unused")
					FrayGame frame = new FrayGame();
					FrayGame.FrayGameGUI.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrayGame() {
		
		timeObj = new Timer(true);
		timeObj.schedule(new UpdateCheckerInGame(), 0, 1000);
		
		FrayGameGUI = new JFrame();
		FrayGameGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FrayGameGUI.setBounds(100, 100, 1200, 800);
		FrayGameGUI.setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		FrayGameGUI.setContentPane(contentPane);
		contentPane.setLayout(null);
		
		FrayGameGUI.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		FrayGameGUI.addWindowListener(new WindowAdapter(){ // Closes system, we only need to close this window.
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
					String sql = "UPDATE sql3282320.UserDataTable SET OnlineStatus = 'Offline' WHERE Username = '" + FrayGameMainMenu.Username + "'";
					myStat.executeUpdate(sql);
					FrayGameGUI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					FrayGameGUI.dispose();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace(); // Make the error display out nicer
				}
     			
			}
			
			else {//Do Nothing
				}
				}
			});
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 1194, 148);
		contentPane.add(panel);
		
		JLabel lblenemyhealth = new JLabel("*EnemyHealth*");
		panel.add(lblenemyhealth);
		
		JLabel lblenemyraceimage = new JLabel("*EnemyRaceImage*");
		panel.add(lblenemyraceimage);
		
		JLabel middleLineDiv = new JLabel("");
		middleLineDiv.setBounds(0, 338, 1194, 14);
		contentPane.add(middleLineDiv);
		
		JLabel cardDeckFractionLabel = new JLabel("4\r\n/\r\n30");
		cardDeckFractionLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cardDeckFractionLabel.setBounds(160, 651, 46, 72);
		contentPane.add(cardDeckFractionLabel);
		
		JButton btnCard1 = new JButton("Card 1");
		btnCard1.setToolTipText("rawr");
		btnCard1.setBounds(304, 623, 119, 148);
		contentPane.add(btnCard1);
		btnCard1.addMouseListener(new java.awt.event.MouseAdapter() {
	        public void mouseEntered(java.awt.event.MouseEvent evt) { // Increases button size on mouse over                
	        	btnCard1.setSize(500,100);                
	        }
	        public void mouseExited(java.awt.event.MouseEvent evt) { // Decreases button size to original size on mouse out
	        	btnCard1.setSize(119, 148);
	        }
	    });
		
		JButton btnCard2 = new JButton("Card 2");
		btnCard2.setToolTipText("jhkhj");
		btnCard2.setBounds(433, 623, 119, 148);
		contentPane.add(btnCard2);
		
		JButton btnCard3 = new JButton("Card 3");
		btnCard3.setToolTipText("hkhkh");
		btnCard3.setBounds(562, 623, 119, 148);
		contentPane.add(btnCard3);
		
		JButton btnCard4 = new JButton("Card 4");
		btnCard4.setToolTipText("khkh");
		btnCard4.setBounds(691, 623, 119, 148);
		contentPane.add(btnCard4);
		
		JButton btnCard5 = new JButton("Card 5");
		btnCard5.setToolTipText("yk");
		btnCard5.setBounds(820, 623, 119, 148);
		contentPane.add(btnCard5);
		
		JButton yourFieldCard1 = new JButton("Card 1");
		yourFieldCard1.setToolTipText("jhkhj");
		yourFieldCard1.setBounds(162, 464, 119, 148);
		contentPane.add(yourFieldCard1);
		
		JButton yourFieldCard2 = new JButton("Card 2");
		yourFieldCard2.setToolTipText("khkh");
		yourFieldCard2.setBounds(314, 464, 119, 148);
		contentPane.add(yourFieldCard2);
		
		JButton yourFieldCard3 = new JButton("Card 3");
		yourFieldCard3.setToolTipText("yk");
		yourFieldCard3.setBounds(459, 464, 119, 148);
		contentPane.add(yourFieldCard3);
		
		JButton yourFieldCard4 = new JButton("Card 4");
		yourFieldCard4.setToolTipText("yk");
		yourFieldCard4.setBounds(607, 464, 119, 148);
		contentPane.add(yourFieldCard4);
		
		JButton yourFieldCard5 = new JButton("Card 5");
		yourFieldCard5.setToolTipText("yk");
		yourFieldCard5.setBounds(757, 464, 119, 148);
		contentPane.add(yourFieldCard5);
		
		JButton yourFieldCard6 = new JButton("Card 6");
		yourFieldCard6.setToolTipText("yk");
		yourFieldCard6.setBounds(907, 464, 119, 148);
		contentPane.add(yourFieldCard6);
		
		JButton enemyFieldCard1 = new JButton("Card 1");
		enemyFieldCard1.setToolTipText("jhkhj");
		enemyFieldCard1.setBounds(160, 159, 119, 148);
		contentPane.add(enemyFieldCard1);
		
		JButton enemyFieldCard2 = new JButton("Card 2");
		enemyFieldCard2.setToolTipText("khkh");
		enemyFieldCard2.setBounds(312, 159, 119, 148);
		contentPane.add(enemyFieldCard2);
		
		JButton enemyFieldCard3 = new JButton("Card 3");
		enemyFieldCard3.setToolTipText("yk");
		enemyFieldCard3.setBounds(457, 159, 119, 148);
		contentPane.add(enemyFieldCard3);
		
		JButton enemyFieldCard4 = new JButton("Card 4");
		enemyFieldCard4.setToolTipText("yk");
		enemyFieldCard4.setBounds(605, 159, 119, 148);
		contentPane.add(enemyFieldCard4);
		
		JButton enemyFieldCard5 = new JButton("Card 5");
		enemyFieldCard5.setToolTipText("yk");
		enemyFieldCard5.setBounds(755, 159, 119, 148);
		contentPane.add(enemyFieldCard5);
		
		JButton enemyFieldCard6 = new JButton("Card 6");
		enemyFieldCard6.setToolTipText("yk");
		enemyFieldCard6.setBounds(905, 159, 119, 148);
		contentPane.add(enemyFieldCard6);
		
		JLabel lblyourhealth = new JLabel("*YourHealth*");
		lblyourhealth.setBounds(142, 738, 164, 14);
		contentPane.add(lblyourhealth);
		
		JLabel lblusername = new JLabel(FrayLoginGUI.uTF.getText());
		lblusername.setForeground(Color.WHITE);
		lblusername.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblusername.setBounds(20, 735, 102, 14);
		contentPane.add(lblusername);
		
		JLabel CharacterPotrait = new JLabel(new ImageIcon(FrayLoginGUI.class.getResource("/Resources/Images/HumanGeneral2.png")));
		CharacterPotrait.setBounds(0, 623, 134, 148);
		contentPane.add(CharacterPotrait);
		
		JLabel yourTableBackground = new JLabel(new ImageIcon(FrayLoginGUI.class.getResource("/Resources/Images/CardTable.png")));
		yourTableBackground.setBounds(0, 623, 1194, 148);
		contentPane.add(yourTableBackground);
		
		JLabel grassTexture = new JLabel(new ImageIcon(FrayGame.class.getResource("/Resources/Images/PlayingField2.jpg")));
		grassTexture.setBounds(0, 0, 1194, 771);
		contentPane.add(grassTexture);
		
		//btnClickMe.addActionListener(new ActionListener() { 
		//	public void actionPerformed(ActionEvent e) {
		//		JOptionPane.showMessageDialog(null, "HELLO");
		//	}
		//});
	}
	
	@SuppressWarnings("static-access")
	static void inGameStatusCheck() {
		try {
			Connection con = JDBConnector.getCon();
			
			PreparedStatement ps;
			String sql = "SELECT SentToPlayerUsername FROM sql3282320." + FrayLoginGUI.uTF.getText() + "_SentGameInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
			while(myRs.next()) {
				setOpponentUsername(myRs.getString("SentToPlayerUsername"));
				System.out.println("1" + getOpponentUsername());
				
				inGameStatusCheckSQL();
			}
		} catch(SQLException e) {e.printStackTrace();}
	}
	
	@SuppressWarnings("static-access")
	static void inGameStatusCheckPlayer2() {
		try {
			Connection con = JDBConnector.getCon();
			
			PreparedStatement ps;
			String sql = "SELECT SenderUsername FROM sql3282320." + FrayLoginGUI.uTF.getText() + "_ReceivedGamedInvitesTable WHERE SecondGameInviteStatus = 'Accepted';";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
			while(myRs.next()) {
				setOpponentUsername(myRs.getString("SenderUsername"));
				System.out.println("1" + getOpponentUsername());
				
				inGameStatusCheckSQL();
			}
		} catch(SQLException e) {e.printStackTrace();}
	}
	
	@SuppressWarnings("static-access")
	static void inGameStatusCheckSQL() {
		try {
			Connection con = JDBConnector.getCon();
			PreparedStatement ps2;
			String sql2 = "SELECT OnlineStatus FROM sql3282320.UserDataTable WHERE Username = '" + opponentUsername + "';";
			ps2 = con.prepareStatement(sql2);
			ResultSet myRs2 = ps2.executeQuery();
			
			while(myRs2.next()) {
					String opponentOnlineStatus = myRs2.getString("OnlineStatus");
				
					if(opponentOnlineStatus.equalsIgnoreCase("Offline")) {
							Statement myStat = con.createStatement();
							String sql3 = "UPDATE sql3282320.UserDataTable SET InGame = 'No' WHERE Username = '" + FrayLoginGUI.uTF.getText() + "';";
							myStat.executeUpdate(sql3);
				
							PreparedStatement ps3;
							String sql4 = "SELECT UserXP FROM sql3282320.UserDataTable WHERE Username = '" + FrayLoginGUI.uTF.getText() + "';";
							ps3 = con.prepareStatement(sql4);
							ResultSet myRs3 = ps3.executeQuery();
					
							while(myRs3.next()) {
								int userXP = myRs3.getInt("UserXP");
								userXP = userXP + 10;
							
								if(userXP < 0) {
									userXP = 0;
								}
					
								Statement myStat1 = con.createStatement();
								String sql5 = "UPDATE sql3282320.UserDataTable SET UserXP = '" + userXP + "' WHERE Username = '" + FrayLoginGUI.uTF.getText() + "';";
								myStat1.executeUpdate(sql5);
							}
					
							Statement myStat2 = con.createStatement();
							String sql6 = "UPDATE sql3282320.UserDataTable SET InGame = 'No' WHERE Username = '" + opponentUsername + "';";
							myStat2.executeUpdate(sql6);
					
							PreparedStatement ps4;
							String sql7 = "SELECT UserXP FROM sql3282320.UserDataTable WHERE Username = '" + opponentUsername + "';";
							ps4 = con.prepareStatement(sql7);
							ResultSet myRs4 = ps4.executeQuery();
					
							while(myRs4.next()) {
								int userXP = myRs4.getInt("UserXP");
								userXP = userXP - 10;
								
								if(userXP < 0) {
									userXP = 0;
								}
						
								Statement myStat3 = con.createStatement();
								String sql8 = "UPDATE sql3282320.UserDataTable SET UserXP = '" + userXP + "' WHERE Username = '" + opponentUsername + "';";
								myStat3.executeUpdate(sql8);
							}
						
							try {
								PreparedStatement ps5;
								String sql9 = "DROP TABLE sql3282320." + FrayLoginGUI.uTF.getText() + "_VS_" + opponentUsername + "_FrayCardGame;";
								ps5 = con.prepareStatement(sql9);
								ps5.execute();
							} catch (java.sql.SQLSyntaxErrorException e) {
								PreparedStatement ps55;
								String sql99 = "DROP TABLE sql3282320." + opponentUsername + "_VS_" + FrayLoginGUI.uTF.getText() + "_FrayCardGame;";
								ps55 = con.prepareStatement(sql99);
								ps55.execute();
							}
						
							PreparedStatement ps6;
							String sql10 = "DELETE FROM sql3282320." + opponentUsername + "_ReceivedGamedInvitesTable WHERE SenderUsername = '" + FrayLoginGUI.uTF.getText() + "';";
							ps6 = con.prepareStatement(sql10);
							ps6.execute();
							
							PreparedStatement ps66;
							String sql100 = "DELETE FROM sql3282320." + FrayLoginGUI.uTF.getText() + "_ReceivedGamedInvitesTable WHERE SenderUsername = '" + opponentUsername + "';";
							ps66 = con.prepareStatement(sql100);
							ps66.execute();
					
							PreparedStatement ps7;
							String sql11 = "DELETE FROM sql3282320." + FrayLoginGUI.uTF.getText() + "_SentGameInvitesTable WHERE SentToPlayerUsername = '" + opponentUsername + "';";
							ps7 = con.prepareStatement(sql11);
							ps7.execute();
							
							PreparedStatement ps77;
							String sql111 = "DELETE FROM sql3282320." + opponentUsername + "_SentGameInvitesTable WHERE SentToPlayerUsername = '" + FrayLoginGUI.uTF.getText() + "';";
							ps77 = con.prepareStatement(sql111);
							ps77.execute();
				
							JOptionPane.showMessageDialog(null, "The Game Between " + opponentUsername + " and " + FrayLoginGUI.uTF.getText() + " has ended!"); // Add who won.
							timeObj.cancel();
							FrayGameGUI.dispose();
							FrayPlayerSelectScreen returnBackToPlayerSelectScreen = new FrayPlayerSelectScreen();
							returnBackToPlayerSelectScreen.frame.setVisible(true);
							returnBackToPlayerSelectScreen.contentPane.setVisible(true);
					}
			 }
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
}