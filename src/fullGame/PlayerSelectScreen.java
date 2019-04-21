package fullGame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import fullGame.FrayCardGame;
import fullGame.PlayerSelectScreen;
import objectClasses.Player;
import jdbc.JDBConnector;
/**
 * @author Gagandeep Ghotra, Sang Heon Park, Zain Razvi, Lee Fyye
 * This class displays out a JFrame that allows the user to see
 * all online and offline users and invite one of them to play a game.
 * 
 * In this class there is going to be methods with the same name
 * just the name could have 'player2' or just 2.
 * Keep in mind, even though the methods or code have the same purpose, they both 
 * are meant for connecting to the same database table and doing a task
 * using different 'perspectives'.
 * Example: attack1 is you vs enemy. 
 * Attack1 method/code lets player 1 connect to the db. 
 * attack2 is enemy vs you.
 * Attack2 method/code lets player 2 connect to the db. 
 */
public class PlayerSelectScreen {

	private JPanel contentPane;
	private String Username; // Username variable
	private Timer timerObjUpdatingTables1; // A timer object that will be used to repeat methods every few seconds.
	private JTable jtable_Display_Users, 
			jtable_Display_SentGameInvites,
			jtable_Display_ReceivedGameInvites; // JTables for displaying users, sent game invites, and received game invites.
	private DefaultTableModel jDU = new DefaultTableModel(); // This will be updated in methods and than fed to the JTable
	private DefaultTableModel jDIGI = new DefaultTableModel(); // This will be updated in methods and than fed to the JTable
	private DefaultTableModel jDRGI = new DefaultTableModel(); // This will be updated in methods and than few to the JTable
	int UserXP; // User xp field
	private JFrame passFrame; // Main JFrame

	public PlayerSelectScreen(String PassUsername) {
		setUsername(PassUsername);		
		runGUI();		
	}
	
	public void runGUI() {
		setPassFrame(new JFrame());
		getPassFrame().setBounds(100, 100, 700, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		getPassFrame().setContentPane(contentPane);
		getPassFrame().setTitle(getUsername());
		
		timerObjUpdatingTables1 = new Timer(true);
		timerObjUpdatingTables1.schedule(new TimerTask() {
			@Override
			public void run() {
				try {
					setjDU(displayUsersOut(jDU));
					jtable_Display_Users.setModel(getjDU());
					setjDIGI(displaySentGameInvitesOut(jDIGI, getUsername()));
					jtable_Display_SentGameInvites.setModel(getjDIGI());
					setjDRGI(displayReceivedGameInvitesOut(jDRGI, getUsername()));
					jtable_Display_ReceivedGameInvites.setModel(getjDRGI());
					sentGameInviteAccepted(getUsername(), PlayerSelectScreen.this.getPassFrame(), this);
					sentSecondGameInviteAccepted(getUsername(), PlayerSelectScreen.this.getPassFrame(), this);
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
		}, 0, 5000); 
		getPassFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getPassFrame().addWindowListener(new WindowAdapter(){
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
					String sql = "UPDATE sql3282320.UserDataTable SET OnlineStatus = 'Offline' WHERE Username = '" + getUsername() + "'";
					myStat.executeUpdate(sql);
					System.exit(0);
				} catch (SQLException e1) {
					e1.printStackTrace(); // Make the error display out nicer
				}
				System.exit(0);
			}
			
			else {//Do Nothing
			}
				}
			});
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		contentPane.add(tabbedPane);
		
		JPanel UserTable = new JPanel();
		tabbedPane.addTab("Account Information Table", null, UserTable, null);
		UserTable.setLayout(null);
		
		JButton btnSendGameInvite = new JButton("Send Game Invite");
		btnSendGameInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = JOptionPane.showInputDialog(null, "Please input the username that you want to send the invite to.");
				sendGameInvite(username);
			}
		});
		btnSendGameInvite.setBounds(314, 419, 170, 67);
		UserTable.add(btnSendGameInvite);
		
		jtable_Display_Users = new JTable();
		jtable_Display_Users.setRowSelectionAllowed(false); // If on then user can move columns
		
		jtable_Display_Users.setEnabled(false); // If true than user can edit rows
		jtable_Display_Users.setBackground(Color.WHITE);
		
		JScrollPane scrollPane = new JScrollPane(jtable_Display_Users);
		scrollPane.setBounds(0, 0, 679, 400);
		UserTable.add(scrollPane);
		
		// Sent Game Invites Table
		
		JPanel SentGameInvitesTable = new JPanel();
		tabbedPane.addTab("Sent Game Invites Table", null, SentGameInvitesTable, null);
		SentGameInvitesTable.setLayout(null);
		
		jtable_Display_SentGameInvites = new JTable();
		jtable_Display_SentGameInvites.setRowSelectionAllowed(false); // If on then user can move columns	
		jtable_Display_SentGameInvites.setEnabled(false); // If true than user can edit rows
		jtable_Display_SentGameInvites.setBackground(Color.WHITE);
		
		JScrollPane scrollPane1 = new JScrollPane(jtable_Display_SentGameInvites);
		scrollPane1.setBounds(0, 0, 679, 400);
		SentGameInvitesTable.add(scrollPane1);	
		
		// Received Game Invites Table
		
		JPanel ReceivedGameInvitesTable = new JPanel();
		tabbedPane.addTab("Incoming Game Invites Table", null, ReceivedGameInvitesTable, null);
		ReceivedGameInvitesTable.setLayout(null);
		
		jtable_Display_ReceivedGameInvites = new JTable();
		jtable_Display_ReceivedGameInvites.setRowSelectionAllowed(false); // If on then user can move columns	
		jtable_Display_ReceivedGameInvites.setEnabled(false); // If true than user can edit rows
		jtable_Display_ReceivedGameInvites.setBackground(Color.WHITE);
		
		JScrollPane scrollPane2 = new JScrollPane(jtable_Display_ReceivedGameInvites);
		scrollPane2.setBounds(0, 0, 679, 400);
		ReceivedGameInvitesTable.add(scrollPane2);
		
		JButton btnAcceptGameInvite = new JButton("Accept Game Invite");
		btnAcceptGameInvite.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String username = JOptionPane.showInputDialog(null, "Please input the username that you want to accept the invite of.");
				acceptGameInvite(username);
			}
		});
		btnAcceptGameInvite.setBounds(287, 422, 170, 48);
		ReceivedGameInvitesTable.add(btnAcceptGameInvite);
	}
	
	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public DefaultTableModel getjDU() {
		return jDU;
	}

	public void setjDU(DefaultTableModel jDU) {
		this.jDU = jDU;
	}

	public DefaultTableModel getjDIGI() {
		return jDIGI;
	}

	public void setjDIGI(DefaultTableModel jDIGI) {
		this.jDIGI = jDIGI;
	}

	public DefaultTableModel getjDRGI() {
		return jDRGI;
	}

	public void setjDRGI(DefaultTableModel jDRGI) {
		this.jDRGI = jDRGI;
	}
	
	DefaultTableModel displayUsersOut(DefaultTableModel jDU) throws SQLException { 
		// Used to select all information from the SQLtable and pipe it into the JTables. Only code in this method is used from the video.
		//https://www.youtube.com/watch?v=WabQxSMBb8Q - Had to do heavy modification of code.
			jDU.setRowCount(0);
			Connection con = JDBConnector.getCon();
			String query1 = "SELECT id, Date, Username, OnlineStatus, InGame, UserXP FROM sql3282320.UserDataTable ORDER BY OnlineStatus DESC";
			Statement myStat = (Statement) con.createStatement();
			ResultSet myRs = ((java.sql.Statement) myStat).executeQuery(query1);
			ResultSetMetaData rsmetadata = (ResultSetMetaData) myRs.getMetaData();
			
			int columns = rsmetadata.getColumnCount();
			Vector<String> columns_name = new Vector<String>();
			Vector<String> data_rows = new Vector<String>();
			
			for(int i = 1; i <= columns; i++) {
				columns_name.addElement(rsmetadata.getColumnName(i));
			}
			
			jDU.setColumnIdentifiers(columns_name);
			
			while(myRs.next()) {
				data_rows = new Vector<String>();
				for(int j = 1; j <= columns; j++) {
					data_rows.addElement(myRs.getString(j));
				}
				jDU.addRow(data_rows);
			}
			return jDU;	
	}
	
	DefaultTableModel displaySentGameInvitesOut(DefaultTableModel jDIGI, String Username) throws SQLException {
		// Used to select all information from the SQLtable and pipe it into the JTables. Only code in this method is used from the video.
		//https://www.youtube.com/watch?v=WabQxSMBb8Q - Had to do heavy modification of code.
			jDIGI.setRowCount(0);
			Connection con = JDBConnector.getCon();
			String query1 = "SELECT * FROM sql3282320." + Username +  "_SentGameInvitesTable ORDER BY Date DESC";
			Statement myStat = (Statement) con.createStatement();
			ResultSet myRs = ((java.sql.Statement) myStat).executeQuery(query1);
			ResultSetMetaData rsmetadata = (ResultSetMetaData) myRs.getMetaData();
			
			int columns = rsmetadata.getColumnCount();
			Vector<String> columns_name = new Vector<String>();
			Vector<String> data_rows = new Vector<String>();
			
			for(int i = 1; i <= columns; i++) {
				columns_name.addElement(rsmetadata.getColumnName(i));
			}
			
			jDIGI.setColumnIdentifiers(columns_name);
			
			while(myRs.next()) {
				data_rows = new Vector<String>();
				for(int j = 1; j <= columns; j++) {
					data_rows.addElement(myRs.getString(j));
				}
				jDIGI.addRow(data_rows);
			}
		return jDIGI;
	}
	
	DefaultTableModel displayReceivedGameInvitesOut(DefaultTableModel jDRGI, String Username) throws SQLException { 
		// Used to select all information from the SQLtable and pipe it into the JTables. Only code in this method is used from the video.
		//https://www.youtube.com/watch?v=WabQxSMBb8Q - Had to do heavy modification of code.
			jDRGI.setRowCount(0);
			Connection con = JDBConnector.getCon();
			String query1 = "SELECT * FROM sql3282320." + Username +  "_ReceivedGamedInvitesTable ORDER BY Date DESC";
			Statement myStat = (Statement) con.createStatement();
			ResultSet myRs = ((java.sql.Statement) myStat).executeQuery(query1);
			ResultSetMetaData rsmetadata = (ResultSetMetaData) myRs.getMetaData();
			
			int columns = rsmetadata.getColumnCount();
			Vector<String> columns_name = new Vector<String>();
			Vector<String> data_rows = new Vector<String>();
			
			for(int i = 1; i <= columns; i++) {
				columns_name.addElement(rsmetadata.getColumnName(i));
			}
			
			jDRGI.setColumnIdentifiers(columns_name);
			
			while(myRs.next()) {
				data_rows = new Vector<String>();
				for(int j = 1; j <= columns; j++) {
					data_rows.addElement(myRs.getString(j));
				}
				jDRGI.addRow(data_rows);
			}
			return jDRGI;
	}
	
	public void sendGameInvite(String username) { 
		// This method will send game invite and adds it to the sent user's recieved. The game invite will be added to the sender's sent game invites table.
		try {
			Connection con = JDBConnector.getCon();
			String query = "INSERT INTO sql3282320." + username + "_ReceivedGamedInvitesTable(SenderUsername, SenderXP)  VALUES(?, ?);"; // Add new row to sepcecfic player's received game invites table.
			String query1 = "INSERT INTO sql3282320." + getUsername() + "_SentGameInvitesTable(SentToPlayerUsername, SentToPlayerXP) VALUES(?,?);"; //Add to your sentGame invites table.

			PreparedStatement ps;
			
			ps = con.prepareStatement(query);
			ps.setString(1, getUsername());
			ps.setString(2, String.valueOf(getUsernameXP(getUsername())));
			
			PreparedStatement ps1;
			ps1 = con.prepareStatement(query1);
			ps1.setString(1, username);
			ps1.setString(2, String.valueOf(getUsernameXP(getUsername())));
			
			ps.execute();
			
			JOptionPane.showMessageDialog(null, "Game Invite Sent, Check your Sent Game Invites Table!");
			ps1.execute();
		} catch (SQLException e) {
			e.printStackTrace(); // Make exception return be nicer
		}
		
	}
	
	private void acceptGameInvite(String username) { // Allows user to accept game invite
		try {
			Connection con = JDBConnector.getCon();
			Statement myStat = con.createStatement();
			String sql = "UPDATE sql3282320." +getUsername()+"_ReceivedGamedInvitesTable SET GameInviteStatus = 'Accepted' WHERE SenderUsername = '" + username + "'";
			myStat.executeUpdate(sql);

			Statement myStat1 = con.createStatement();
			String sql1 = "UPDATE sql3282320." + username +"_SentGameInvitesTable SET GameInviteStatus = 'Accepted' WHERE SentToPlayerUsername = '" + getUsername() + "'";
			myStat1.executeUpdate(sql1);
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	void acceptedSecondGameInvite(String username, String Player1Username) { // Allows user to accept second game invite - after sender accepts.
		try {
			Connection con = JDBConnector.getCon();
			Statement myStat = con.createStatement();
			String sql = "UPDATE sql3282320." + username + "_ReceivedGamedInvitesTable SET SecondGameInviteStatus = 'Accepted' WHERE SenderUsername = '" + Player1Username + "'";
			myStat.executeUpdate(sql);
			
			Statement myStat1 = con.createStatement();
			String sql1 = "UPDATE sql3282320." +  Player1Username + "_SentGameInvitesTable SET SecondGameInviteStatus = 'Accepted' WHERE SentToPlayerUsername = '" + username + "'";
			myStat1.executeUpdate(sql1);
			
		} catch(SQLException e) {e.printStackTrace();}
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
	
	void sentGameInviteAccepted(String Player1Username, JFrame frame, TimerTask timeObj) {
		try {
			Connection con = JDBConnector.getCon();
			PreparedStatement ps;
			String sql = "SELECT SentToPlayerUsername FROM sql3282320." + Player1Username + "_SentGameInvitesTable WHERE GameInviteStatus = 'Accepted'";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			String selectedRace = "";
			while(myRs.next()) {
				/*
				* Create a new card game database that has both users loaded in
				* and also close the player select screen. 
				* Also ask each player what character they want to play as.
				* The first rows will be for 'player1' and the second five rows will be for 'player2'.
				* The player numbers will be determined at random in this program somehow.
				*/
				String Username = myRs.getString("SentToPlayerUsername");
				int confirm = JOptionPane.showConfirmDialog(null, Username + " has accepted your invite, want to play?");
				
				if(confirm == JOptionPane.YES_OPTION) {
					acceptedSecondGameInvite(Username, Player1Username);
					inGameSetter(Player1Username);
					while((selectedRace != "Elf") || (selectedRace != "Human") || (selectedRace != "Undead")) {
						
						selectedRace = JOptionPane.showInputDialog(null, "Please enter the race you want to play as againest " + Username + "; Elf, Human.");
						if(selectedRace.equalsIgnoreCase("Elf")) {
							Player newPlayer = new Player(Player1Username, "Elf");
							//FrayCardGame.importCardsIntoYourHands(newPlayer);
							createCardGameDatabaseTable(Player1Username, Username);
							timeObj.cancel();
							frame.dispose();	
							FrayCardGame newFrayGame = new FrayCardGame(newPlayer);
							newFrayGame.getGameFrame().setVisible(true);
							break;
						}
						
						else if (selectedRace.equalsIgnoreCase("Human")) {
							Player newPlayer = new Player(Player1Username, "Human");		
							//FrayCardGame.importCardsIntoYourHands(newPlayer);
							createCardGameDatabaseTable(Player1Username, Username);
							timeObj.cancel();
							frame.dispose();
							FrayCardGame newFrayGame = new FrayCardGame(newPlayer);
							newFrayGame.getGameFrame().setVisible(true);
							break;
						}
						
						else if (selectedRace.equalsIgnoreCase("Undead")) {
							Player newPlayer = new Player(Player1Username, "Undead");
							//FrayCardGame.importCardsIntoYourHands(newPlayer);
							createCardGameDatabaseTable(Player1Username, Username);
							timeObj.cancel();
							frame.dispose();
							FrayCardGame newFrayGame = new FrayCardGame(newPlayer);
							newFrayGame.getGameFrame().setVisible(true);
							break;
						}
					}
					
					
				}
				
				if(confirm == JOptionPane.NO_OPTION) {
					PreparedStatement ps1;
					String sql1 = "DELETE FROM sql3282320." + Player1Username + "_SentGameInvitesTable WHERE SentToPlayerUsername = '" + Username + "'";
					ps1 = con.prepareStatement(sql1);
					ps1.execute();
					
					PreparedStatement ps2;
					String sql2 = "DELETE FROM sql3282320." + Username + "_ReceivedGamedInvitesTable WHERE SenderUsername = '" + Player1Username  + "'";
					ps2 = con.prepareStatement(sql2);
					ps2.execute();
					
				} // Tell the user who accepted the invite that sender rejected somehow and delete invite. Or just delete the invite and not tell the acceptor
				
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	 void sentSecondGameInviteAccepted(String Player1Username, JFrame frame, TimerTask timeObj) {
		try {
			
			Connection con = JDBConnector.getCon();
			PreparedStatement ps1;
			String sql1 = "SELECT SenderUsername FROM sql3282320." + Player1Username + "_ReceivedGamedInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
			ps1 = con.prepareStatement(sql1);
			ResultSet myRs1 = ps1.executeQuery();
			String selectedRace = "";
			while(myRs1.next()) {
				/*
				* Create a new card game database that has both users loaded in
				* and also close the player select screen. 
				* Also ask each player what character they want to play as.
				* The first rows will be for 'player1' and the second five rows will be for 'player2'.
				* The player numbers will be determined at random in this program somehow.
				*/
				String Username = myRs1.getString("SenderUsername");
				
				inGameSetter(Player1Username);
					while((selectedRace != "Elf") || (selectedRace != "Human") || (selectedRace != "Undead"))	{
							selectedRace = JOptionPane.showInputDialog(null, "Please enter the race you want to play as againest " + Username + "; Elf, Human.");// had FrayLogin.uTF.getText
							if(selectedRace.equalsIgnoreCase("Elf")) {
								Player newPlayer = new Player(Player1Username, "Elf");
								timeObj.cancel();
								frame.dispose();
								FrayCardGame newFrayGame = new FrayCardGame(newPlayer);
								newFrayGame.getGameFrame().setVisible(true);
								break;
							}
							
							else if (selectedRace.equalsIgnoreCase("Human")) {
								Player newPlayer = new Player(Player1Username, "Human");
								timeObj.cancel();
								frame.dispose();
								FrayCardGame newFrayGame = new FrayCardGame(newPlayer);
								newFrayGame.getGameFrame().setVisible(true);
								break;
							}
							
							else if (selectedRace.equalsIgnoreCase("Undead")) {
								Player newPlayer = new Player(Player1Username, "Undead");
								timeObj.cancel();
								frame.dispose();
								FrayCardGame newFrayGame = new FrayCardGame(newPlayer);
								newFrayGame.getGameFrame().setVisible(true);
								break;
							}
						} 	
					}	
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	void createCardGameDatabaseTable(String playerOne, String playerTwo) { // Takes in two players and creates the game database
		// The database is made but the information - the commands in the for loops do not run - is not added to the table
		try {
			Connection con = JDBConnector.getCon();
			String sql = "CREATE TABLE " + playerOne + "_VS_" + playerTwo + "_FrayCardGame LIKE PlayerOne_VS_PlayerTwo_FrayCardGame;";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.execute();
			
			String sql11 = "INSERT INTO sql3282320." + playerOne + "_VS_" + playerTwo + "_FrayCardGame(PlayerName,CardPositionInArrayList, CardHealthPoints, CardName, CardType) VALUES(?,?,?,?,?);";
			PreparedStatement ps11 = con.prepareStatement(sql11);
			ps11.setString(1, playerOne);
			ps11.setString(2, String.valueOf(0));
			ps11.setString(3, String.valueOf(30));
			ps11.setString(4, playerOne);
			ps11.setString(5, "Minions");
			ps11.execute();
			
			for(int i = 1; i < 9; i++) {
				String sql1 = "INSERT INTO sql3282320." + playerOne + "_VS_" + playerTwo + "_FrayCardGame(PlayerName,CardPositionInArrayList) VALUES(?,?);";
				PreparedStatement ps1 = con.prepareStatement(sql1);
				ps1.setString(1, playerOne);
				ps1.setString(2, String.valueOf(i));
				ps1.execute();
			}
			
			String sql22 = "INSERT INTO sql3282320." + playerOne + "_VS_" + playerTwo + "_FrayCardGame(PlayerName,CardPositionInArrayList, CardHealthPoints, CardName, CardType) VALUES(?,?,?,?,?);";
			PreparedStatement ps22 = con.prepareStatement(sql22);
			ps22.setString(1, playerTwo);
			ps22.setString(2, String.valueOf(10));
			ps22.setString(3, String.valueOf(30));
			ps22.setString(4, playerTwo);
			ps22.setString(5, "Minions");
			ps22.execute();
			for(int i = 1; i < 9; i++) {
				String sql2 = "INSERT INTO sql3282320." + playerOne + "_VS_" + playerTwo + "_FrayCardGame(PlayerName,CardPositionInArrayList) VALUES(?,?);";
				PreparedStatement ps2 = con.prepareStatement(sql2);
				ps2.setString(1, playerTwo);
				ps2.setString(2, String.valueOf(i));
				ps2.execute();
			}
			
		} catch(SQLException e) {e.printStackTrace();}
	}
	
	void inGameSetter(String username) {
		try {
			Connection con = JDBConnector.getCon();
			Statement myStat = con.createStatement();
			String sql = "UPDATE sql3282320.UserDataTable SET InGame = 'Yes' WHERE Username = '" + username + "';";
			myStat.executeUpdate(sql);
		} catch(SQLException e) {e.printStackTrace();}
	}

	public JFrame getPassFrame() {
		return passFrame;
	}

	public void setPassFrame(JFrame passFrame) {
		this.passFrame = passFrame;
	}

}
