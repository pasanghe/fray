package Game;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Timer;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.RowFilter;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

import com.mysql.cj.jdbc.result.ResultSetMetaData;

import JDBC.JDBConnector;
import LoginandRegistrationGUIs.FrayLoginGUI;
import ObjectClasses.Player;

import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;

public class FrayPlayerSelectScreen {

	static JFrame frame;
	static JPanel contentPane;
	static JTable jtable_Display_Users;
	static JTable jtable_Display_SentGameInvites;
	static JTable jtable_Display_ReceivedGameInvites;
	static DefaultTableModel jDU = new DefaultTableModel();
	static DefaultTableModel jDIGI = new DefaultTableModel();
	static DefaultTableModel jDRGI = new DefaultTableModel();
	private JTextField searchField;
	private JLabel lblSearch;
	int UserXP;
	static String selectedRace;
	static Timer timeObj;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@SuppressWarnings("static-access")
			public void run() {
				try {
					FrayPlayerSelectScreen frame = new FrayPlayerSelectScreen();
					frame.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public FrayPlayerSelectScreen() {
		frame = new JFrame();
		frame.setBounds(100, 100, 700, 600);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		frame.setContentPane(contentPane);
		frame.setTitle(FrayLoginGUI.uTF.getText());
		
		timeObj = new Timer(true);
		timeObj.schedule(new UpdateCheck(), 0, 5000); // Runs a class that updates the user table every five seconds.
		// Find a better way to do this because the table flickers every given time. I dont know if this is ok to show the user.
		// Add the update statement in the UpdateCheck() class for every new method you make.
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter(){
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
					System.exit(0);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
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
		
		//int column = 0;//jtable_Display_Users.getSelectedColumn();
		//int row = jtable_Display_Users.getSelectedRow();
		//String value = jtable_Display_Users.getModel().getValueAt(row, column).toString();
		
		// Maybe a random number generator that when put into the display users table,
		// is given to the rows in the table and then this number is also sent to the server,
		// This can make it so that when the user clicks on the row, that number will be selected.
		// But this makes the problem that many users have different values for other users on the table
		
		/*
		 * The actual problem why we cannot select a row and have its values and we can use them is 
		 * because the rows arent actually on the jtable, rather the jtable is simply displaying out 
		 * the data to the user, nothing else. The actual data is store on the database.
		 * We need to find another way that gets the actual data from the server and then the user can
		 * user it in the jtable.
		 */
		
		//System.out.println(value);
		jtable_Display_Users.setEnabled(false); // If true than user can edit rows
		jtable_Display_Users.setBackground(Color.WHITE);
		
		JScrollPane scrollPane = new JScrollPane(jtable_Display_Users);
		scrollPane.setBounds(0, 0, 679, 400);
		UserTable.add(scrollPane);
		
		searchField = new JTextField();
		searchField.setBounds(84,419,170,29);
		//https://www.youtube.com/watch?v=DJEXpgLyAtQ
		searchField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {
				DefaultTableModel table = (DefaultTableModel) jtable_Display_Users.getModel();
				String search = searchField.getText();
				TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(table);
				jtable_Display_Users.setRowSorter(tr);
				tr.setRowFilter(RowFilter.regexFilter(search));
			}
		});	
		UserTable.add(searchField);
		searchField.setColumns(10);
		
		lblSearch = new JLabel("Search: ");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSearch.setBounds(10, 419, 91, 31);
		UserTable.add(lblSearch);
		
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
		
		searchField = new JTextField();
		searchField.setBounds(84,419,170,29);
		//https://www.youtube.com/watch?v=DJEXpgLyAtQ
		searchField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {
				DefaultTableModel table = (DefaultTableModel) jtable_Display_SentGameInvites.getModel();
				String search = searchField.getText();
				TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(table);
				jtable_Display_SentGameInvites.setRowSorter(tr);
				tr.setRowFilter(RowFilter.regexFilter(search));
			}
		});	
		SentGameInvitesTable.add(searchField);
		searchField.setColumns(10);
		
		lblSearch = new JLabel("Search: ");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSearch.setBounds(10, 419, 91, 31);
		SentGameInvitesTable.add(lblSearch);
		
		
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
		
		searchField = new JTextField();
		searchField.setBounds(84,419,170,29);
		//https://www.youtube.com/watch?v=DJEXpgLyAtQ
		searchField.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent evt) {
				DefaultTableModel table = (DefaultTableModel) jtable_Display_ReceivedGameInvites.getModel();
				String search = searchField.getText();
				TableRowSorter<DefaultTableModel> tr = new TableRowSorter<DefaultTableModel>(table);
				jtable_Display_ReceivedGameInvites.setRowSorter(tr);
				tr.setRowFilter(RowFilter.regexFilter(search));
			}
		});	
		ReceivedGameInvitesTable.add(searchField);
		searchField.setColumns(10);
		
		lblSearch = new JLabel("Search: ");
		lblSearch.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblSearch.setBounds(10, 419, 91, 31);
		ReceivedGameInvitesTable.add(lblSearch);
		
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

	static void displayUsersOut() { // Used to select all information from the SQLtable and pipe it into the JTables. Only code in this method is used from the video.
		//https://www.youtube.com/watch?v=WabQxSMBb8Q - Had to do heavy modification of code.
		try {
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
			jtable_Display_Users.setModel(jDU);		
		} catch (SQLException e) {
			e.printStackTrace(); // Fix exception error to display something out to user
		}
		
	}
	
	static void displaySentGameInvitesOut() { // Used to select all information from the SQLtable and pipe it into the JTables. Only code in this method is used from the video.
		//https://www.youtube.com/watch?v=WabQxSMBb8Q - Had to do heavy modification of code.
		try {
			Connection con = JDBConnector.getCon();
			String query1 = "SELECT * FROM sql3282320." + FrayLoginGUI.uTF.getText() +  "_SentGameInvitesTable ORDER BY Date DESC";
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
			jtable_Display_SentGameInvites.setModel(jDIGI);		/////////////
		} catch (SQLException e) {
			e.printStackTrace(); // Fix exception error to display something out to user
		}
		
	}
	
	static void displayReceivedGameInvitesOut() { // Used to select all information from the SQLtable and pipe it into the JTables. Only code in this method is used from the video.
		//https://www.youtube.com/watch?v=WabQxSMBb8Q - Had to do heavy modification of code.
		try {
			Connection con = JDBConnector.getCon();
			String query1 = "SELECT * FROM sql3282320." + FrayLoginGUI.uTF.getText() +  "_ReceivedGamedInvitesTable ORDER BY Date DESC";
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
			jtable_Display_ReceivedGameInvites.setModel(jDRGI);
		} catch (SQLException e) {
			e.printStackTrace(); // Fix exception error to display something out to user
		}
		
	}
	
	public void sendGameInvite(String username) { // This method will send game invite and adds it to the sent user's recieved. The game invite will be added to the sender's sent game invites table.
		try {
			Connection con = JDBConnector.getCon();
			String query = "INSERT INTO sql3282320." + username + "_ReceivedGamedInvitesTable(SenderUsername, SenderXP)  VALUES(?, ?);"; // Add new row to sepcecfic player's received game invites table.
			String query1 = "INSERT INTO sql3282320." + FrayLoginGUI.uTF.getText() + "_SentGameInvitesTable(SentToPlayerUsername, SentToPlayerXP) VALUES(?,?);"; //Add to your sentGame invites table.

			PreparedStatement ps;
			
			ps = con.prepareStatement(query);
			ps.setString(1, FrayLoginGUI.uTF.getText());
			ps.setString(2, String.valueOf(getUsernameXP(FrayLoginGUI.uTF.getText())));
			
			PreparedStatement ps1;
			ps1 = con.prepareStatement(query1);
			ps1.setString(1, username);
			ps1.setString(2, String.valueOf(getUsernameXP(username)));
			
			ps.execute();
			
			JOptionPane.showMessageDialog(null, "Game Invite Sent, Check your Sent Game Invites Table!");
			ps1.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(); // Make exception return be nicer
		}
		
	}
	
	private void acceptGameInvite(String username) {
		// Also need to find a way to start game for invite reciever, I mean we can just bring both users in?
		try {
			Connection con = JDBConnector.getCon();
			Statement myStat = con.createStatement();
			String sql = "UPDATE sql3282320." +FrayLoginGUI.uTF.getText()+"_ReceivedGamedInvitesTable SET GameInviteStatus = 'Accepted' WHERE SenderUsername = '" + username + "'";
			myStat.executeUpdate(sql);

			Statement myStat1 = con.createStatement();
			String sql1 = "UPDATE sql3282320." + username +"_SentGameInvitesTable SET GameInviteStatus = 'Accepted' WHERE SentToPlayerUsername = '" + FrayLoginGUI.uTF.getText() + "'";
			myStat1.executeUpdate(sql1);
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	static void acceptedSecondGameInvite(String username) {
		try {
			Connection con = JDBConnector.getCon();
			Statement myStat = con.createStatement();
			String sql = "UPDATE sql3282320." + username + "_ReceivedGamedInvitesTable SET SecondGameInviteStatus = 'Accepted' WHERE SenderUsername = '" + FrayLoginGUI.uTF.getText() + "'";
			myStat.executeUpdate(sql);
			
			Statement myStat1 = con.createStatement();
			String sql1 = "UPDATE sql3282320." +  FrayLoginGUI.uTF.getText() + "_SentGameInvitesTable SET SecondGameInviteStatus = 'Accepted' WHERE SentToPlayerUsername = '" + username + "'";
			myStat1.executeUpdate(sql1);
			
		} catch(SQLException e) {e.printStackTrace();}
	}
	
	@SuppressWarnings("static-access")
	static void sentGameInviteAccepted() {
		try {
			Connection con = JDBConnector.getCon();
			PreparedStatement ps;
			String sql = "SELECT SentToPlayerUsername FROM sql3282320." + FrayLoginGUI.uTF.getText() + "_SentGameInvitesTable WHERE GameInviteStatus = 'Accepted'";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
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
					acceptedSecondGameInvite(Username); /////////////////////////////////////////////////////////////////////////////// above
					inGameSetter(FrayLoginGUI.uTF.getText());
					while((selectedRace != "Elf") || (selectedRace != "Human") || (selectedRace != "Undead")) {
						
						selectedRace = JOptionPane.showInputDialog(null, "Please enter the race you want to play as againest " + Username + "; Elf, Human or Undead");
						if(selectedRace.equalsIgnoreCase("Elf")) {
							@SuppressWarnings("unused")
							Player newPlayer = new Player(FrayLoginGUI.uTF.getText(), "Elf");
							createCardGameDatabaseTable(FrayLoginGUI.uTF.getText(), Username);//////////
							timeObj.cancel();
							frame.dispose();	
							FrayGame newFrayGame = new FrayGame();
							newFrayGame.FrayGameGUI.setVisible(true);
							newFrayGame.contentPane.setVisible(true);
							System.out.println(selectedRace);
							break;
						}
						
						else if (selectedRace.equalsIgnoreCase("Human")) {
							@SuppressWarnings("unused")
							Player newPlayer = new Player(FrayLoginGUI.uTF.getText(), "Human");
							createCardGameDatabaseTable(FrayLoginGUI.uTF.getText(), Username);///////////////////
							timeObj.cancel();
							frame.dispose();
							FrayGame newFrayGame = new FrayGame();
							newFrayGame.FrayGameGUI.setVisible(true);
							newFrayGame.contentPane.setVisible(true);
							System.out.println(selectedRace);
							break;
						}
						
						else if (selectedRace.equalsIgnoreCase("Undead")) {
							@SuppressWarnings("unused")
							Player newPlayer = new Player(FrayLoginGUI.uTF.getText(), "Undead");
							createCardGameDatabaseTable(FrayLoginGUI.uTF.getText(), Username);/////////////////////
							timeObj.cancel();
							frame.dispose();
							FrayGame newFrayGame = new FrayGame();
							newFrayGame.FrayGameGUI.setVisible(true);
							newFrayGame.contentPane.setVisible(true);
							System.out.println(selectedRace);
							break;
						}
						System.out.println(selectedRace);
					}
					
					
				}
				
				if(confirm == JOptionPane.NO_OPTION) {
					PreparedStatement ps1;
					String sql1 = "DELETE FROM sql3282320." + FrayLoginGUI.uTF.getText() + "_SentGameInvitesTable WHERE SentToPlayerUsername = '" + Username + "'";
					ps1 = con.prepareStatement(sql1);
					ps1.execute();
					
					PreparedStatement ps2;
					String sql2 = "DELETE FROM sql3282320." + Username + "_ReceivedGamedInvitesTable WHERE SenderUsername = '" + FrayLoginGUI.uTF.getText()  + "'";
					ps2 = con.prepareStatement(sql2);
					ps2.execute();
					
				} // Tell the user who accepted the invite that sender rejected somehow and delete invite. Or just delete the invite and not tell the acceptor
				
			}
			
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	static void sentSecondGameInviteAccepted() {
		try {
			
			Connection con = JDBConnector.getCon();
			PreparedStatement ps1;
			String sql1 = "SELECT SenderUsername FROM sql3282320." + FrayLoginGUI.uTF.getText() + "_ReceivedGamedInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
			ps1 = con.prepareStatement(sql1);
			ResultSet myRs1 = ps1.executeQuery();
			
			while(myRs1.next()) {
				/*
				* Create a new card game database that has both users loaded in
				* and also close the player select screen. 
				* Also ask each player what character they want to play as.
				* The first rows will be for 'player1' and the second five rows will be for 'player2'.
				* The player numbers will be determined at random in this program somehow.
				*/
				String Username = myRs1.getString("SenderUsername");
				System.out.println(Username);
				inGameSetter(FrayLoginGUI.uTF.getText());
					while((selectedRace != "Elf") || (selectedRace != "Human") || (selectedRace != "Undead"))	{
							selectedRace = JOptionPane.showInputDialog(null, "Please enter the race you want to play as againest " + Username + "; Elf, Human or Undead.");// had FrayLogin.uTF.getText
							if(selectedRace.equalsIgnoreCase("Elf")) {
								@SuppressWarnings("unused")
								Player newPlayer = new Player(FrayLoginGUI.uTF.getText(), "Elf");
								timeObj.cancel();
								frame.dispose();
								FrayGame newFrayGame = new FrayGame();
								newFrayGame.FrayGameGUI.setVisible(true);
								newFrayGame.contentPane.setVisible(true);
								break;
							}
							
							else if (selectedRace.equalsIgnoreCase("Human")) {
								@SuppressWarnings("unused")
								Player newPlayer = new Player(FrayLoginGUI.uTF.getText(), "Human");
								timeObj.cancel();
								frame.dispose();
								FrayGame newFrayGame = new FrayGame();
								newFrayGame.FrayGameGUI.setVisible(true);
								newFrayGame.contentPane.setVisible(true);
								break;
							}
							
							else if (selectedRace.equalsIgnoreCase("Undead")) {
								@SuppressWarnings("unused")
								Player newPlayer = new Player(FrayLoginGUI.uTF.getText(), "Undead");
								timeObj.cancel();
								frame.dispose();
								FrayGame newFrayGame = new FrayGame();
								newFrayGame.FrayGameGUI.setVisible(true);
								newFrayGame.contentPane.setVisible(true);
								break;
							}
						} 	
					}	
		} catch(SQLException e) {
			e.printStackTrace();
		}
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
	
	static void createCardGameDatabaseTable(String playerOne, String playerTwo) { // Takes in two players and creates the game database
		// The database is made but the information - the commands in the for loops do not run - is not added to the table
		try {
			Connection con = JDBConnector.getCon();
			String sql = "CREATE TABLE " + playerOne + "_VS_" + playerTwo + "_FrayCardGame LIKE PlayerOne_VS_PlayerTwo_FrayCardGame;";
			PreparedStatement ps = con.prepareStatement(sql);
			ps.execute();
			
			String sql11 = "INSERT INTO sql3282320." + playerOne + "_VS_" + playerTwo + "_FrayCardGame(PlayerName,CardPositionInArrayList, CardHealthPoints) VALUES(?,?,?);";
			PreparedStatement ps11 = con.prepareStatement(sql11);
			ps11.setString(1, playerOne);
			ps11.setString(2, String.valueOf(0));
			ps11.setString(3, String.valueOf(30));
			ps11.execute();
			
			for(int i = 1; i < 9; i++) {
				String sql1 = "INSERT INTO sql3282320." + playerOne + "_VS_" + playerTwo + "_FrayCardGame(PlayerName,CardPositionInArrayList) VALUES(?,?);";
				PreparedStatement ps1 = con.prepareStatement(sql1);
				ps1.setString(1, playerOne);
				ps1.setString(2, String.valueOf(i));
				ps1.execute();
			}
			
			String sql22 = "INSERT INTO sql3282320." + playerOne + "_VS_" + playerTwo + "_FrayCardGame(PlayerName,CardPositionInArrayList, CardHealthPoints) VALUES(?,?,?);";
			PreparedStatement ps22 = con.prepareStatement(sql22);
			ps22.setString(1, playerTwo);
			ps22.setString(2, String.valueOf(0));
			ps22.setString(3, String.valueOf(30));
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
	
	static void inGameSetter(String username) {
		try {
			Connection con = JDBConnector.getCon();
			Statement myStat = con.createStatement();
			String sql = "UPDATE sql3282320.UserDataTable SET InGame = 'Yes' WHERE Username = '" + username + "';";
			myStat.executeUpdate(sql);
		} catch(SQLException e) {e.printStackTrace();}
	}
}