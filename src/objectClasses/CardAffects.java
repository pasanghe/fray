package objectClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JOptionPane;

import jdbc.JDBConnector;

/**
 * @author Gagandeep, Ghotra
 */

public class CardAffects {
	
	private String username;
	private String opponentUsername;
	
	public CardAffects() {}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void attack1(String yourCard, String enemyCard, String username) {
		setUsername(username);
	
		// Pass index value for your and enemy card, and then use for loop to find the obj of that card and then do yo thing.		
			try {
				Connection con = JDBConnector.getCon();
				
				PreparedStatement ps;
				String sql = "SELECT SentToPlayerUsername FROM sql3282320." + getUsername() + "_SentGameInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
				ps = con.prepareStatement(sql);
				ResultSet myRs = ps.executeQuery();
				
				while(myRs.next()) {
					setOpponentUsername(myRs.getString("SentToPlayerUsername"));
					PreparedStatement ps1;
					String sql1 = "Select * FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE CardName = '" + yourCard + "'";
					ps1 = con.prepareStatement(sql1);
					ResultSet myRs1 = ps1.executeQuery();
					
					while(myRs1.next()) {
						String yourcardName = myRs1.getString("CardName");
						int yourattackPoints = myRs1.getInt("CardAttackPoints");
						int yourhealthPoints = myRs1.getInt("CardHealthPoints");
						String yourcardType = myRs1.getString("CardType");
						//cardViewer.setLblerb(myRs1.getInt("SpellValueCost"));//fullpassList.get(index1).getSpellValueCost());
						//cardViewer.setLblarb(myRs1.getInt("SpellValueArmor"));//fullpassList.get(index1).getSpellValueArmor());
						
						PreparedStatement ps2;
						String sql2 = "Select * FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE CardName = '" + enemyCard + "'";
						ps2 = con.prepareStatement(sql2);
						ResultSet myRs2 = ps2.executeQuery();
							while(myRs2.next()) {
								String enemycardName = myRs2.getString("CardName");
								int enemyattackPoints = myRs2.getInt("CardAttackPoints");
								int enemyhealthPoints = myRs2.getInt("CardHealthPoints");
								String enemycardType = myRs2.getString("CardType");
								//cardViewer.setLblerb(myRs1.getInt("SpellValueCost"));//fullpassList.get(index1).getSpellValueCost());
								//cardViewer.setLblarb(myRs1.getInt("SpellValueArmor"));//fullpassList.get(index1).getSpellValueArmor());
								
								if(yourcardType.equals(enemycardType)) {
					
									int yAttacke = enemyhealthPoints - yourattackPoints; // Set this as the updated enemy card health points
		
									JOptionPane.showMessageDialog(null, "Enemy Attack Points: " + enemyattackPoints + ", enemy health points: " + enemyhealthPoints);
									int eAttacky = yourhealthPoints - enemyattackPoints; // Set this as the updated your card health points
							
									if(yAttacke <= 0) {yAttacke = 0;}
									if(eAttacky <= 0) {eAttacky = 0;}
									Statement myStat = con.createStatement();
									String sql3 = "UPDATE sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame SET CardHealthPoints = '" + yAttacke + "' WHERE CardName = '" + enemycardName + "';";
									myStat.executeUpdate(sql3);	
								
									Statement myStat1 = con.createStatement();
									String sql4 = "UPDATE sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame SET CardHealthPoints = '" + eAttacky + "' WHERE CardName = '" + yourcardName + "';";
									myStat1.executeUpdate(sql4);
								}
								
								else {
									JOptionPane.showMessageDialog(null, "Please select a minion card!");
								}
							} 

						}
				}

			} catch(SQLException e) {
				e.printStackTrace();
			}
	}
	
	public void attack2(String yourCard, String enemyCard, String username) {
		setUsername(username);
	
		// Pass index value for your and enemy card, and then use for loop to find the obj of that card and then do yo thing.		
			try {
				Connection con = JDBConnector.getCon();
				

				PreparedStatement ps;
				String sql = "SELECT SenderUsername FROM sql3282320." + getUsername() + "_ReceivedGamedInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
				ps = con.prepareStatement(sql);
				ResultSet myRs = ps.executeQuery();
				
				while(myRs.next()) {
					setOpponentUsername(myRs.getString("SenderUsername"));	
					PreparedStatement ps1;
					String sql1 = "Select * FROM sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame WHERE CardName = '" + yourCard + "'";
					ps1 = con.prepareStatement(sql1);
					ResultSet myRs1 = ps1.executeQuery();
					
					while(myRs1.next()) {
						String yourcardName = myRs1.getString("CardName");
						int yourattackPoints = myRs1.getInt("CardAttackPoints");
						int yourhealthPoints = myRs1.getInt("CardHealthPoints");
						String yourcardType = myRs1.getString("CardType");
						//cardViewer.setLblerb(myRs1.getInt("SpellValueCost"));//fullpassList.get(index1).getSpellValueCost());
						//cardViewer.setLblarb(myRs1.getInt("SpellValueArmor"));//fullpassList.get(index1).getSpellValueArmor());
						
						PreparedStatement ps2;
						String sql2 = "Select * FROM sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame WHERE CardName = '" + enemyCard + "'";
						ps2 = con.prepareStatement(sql2);
						ResultSet myRs2 = ps2.executeQuery();
							while(myRs2.next()) {
								String enemycardName = myRs2.getString("CardName");
								int enemyattackPoints = myRs2.getInt("CardAttackPoints");
								int enemyhealthPoints = myRs2.getInt("CardHealthPoints");
								String enemycardType = myRs2.getString("CardType");
								//cardViewer.setLblerb(myRs1.getInt("SpellValueCost"));//fullpassList.get(index1).getSpellValueCost());
								//cardViewer.setLblarb(myRs1.getInt("SpellValueArmor"));//fullpassList.get(index1).getSpellValueArmor());
								if(yourcardType.equals(enemycardType)) {
									int yAttacke = enemyhealthPoints - yourattackPoints; // Set this as the updated enemy card health points
							
									int eAttacky = yourhealthPoints - enemyattackPoints; // Set this as the updated your card health points
							
									if(yAttacke <= 0) {yAttacke = 0;}
									if(eAttacky <= 0) {eAttacky = 0;}
									Statement myStat = con.createStatement();
									String sql3 = "UPDATE sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame SET CardHealthPoints = '" + yAttacke + "' WHERE CardName = '" + enemycardName +"';";
									myStat.executeUpdate(sql3);	
								
									Statement myStat1 = con.createStatement();
									String sql4 = "UPDATE sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame SET CardHealthPoints = '" + eAttacky + "' WHERE CardName = '" + yourcardName + "';";
									myStat1.executeUpdate(sql4);	
								}
								
								else {
									JOptionPane.showMessageDialog(null, "Please select a minion card!");
								}
							} 

						}
				}

			} catch(SQLException e) {
				e.printStackTrace();
			}
	}

	public String getOpponentUsername() {
		return opponentUsername;
	}

	public void setOpponentUsername(String opponentUsername) {
		this.opponentUsername = opponentUsername;
	}
}