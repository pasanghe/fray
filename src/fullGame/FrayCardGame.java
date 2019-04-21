package fullGame;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import fullGame.PlayerSelectScreen;
import jdbc.JDBConnector;
import objectClasses.FrayCard;
import objectClasses.Player;
/**
 * @author Gagandeep Ghotra, Sang Heon Park, Zain Razvi, Lee Fyyfe
 * This class displays out a JFrame that lets the users play the actual 
 * card game. This class is large, it handles what ever goes on in/during the actual game
 * except the cards affect. The majority of the class is different code for 
 * accessing the database through player 1 or player 2.
 * Keep in mind, even though the methods or code have the same purpose, they both 
 * are meant for connecting to the same database table and doing a task
 * using different 'perspectives'.
 * Example: attack1 is you vs enemy. 
 * Attack1 method/code lets player 1 connect to the db. 
 * attack2 is enemy vs you.
 * Attack2 method/code lets player 2 connect to the db. 
 * All methods in this class have a single responsibility,
 * to play the core game. 
 */
public class FrayCardGame{

	private JPanel contentPane;
	private JFrame gameFrame;
	private ArrayList<FrayCard> passList; // Is modified when playing the game depending on the user
	private ArrayList<FrayCard> fullpassList; // Stores the whole race deck depending on the user's race.
	private String arrayOfCardsOnHand[] = new String[5]; // Array of strings that will hold the names of the cards in the user's hand.
	private JButton[] btnCardsInHand = new JButton[5]; // Array of JButtons that will hold the cards in the user's hand
	private JButton[] yourFieldCards = new JButton[6]; // Array of JButtons that will hold the cards in the user's field
	private JButton[] enemyFieldCards = new JButton[6]; // Array of JButtons that will hold the cards in the enemy's field
	private JButton btnEndTurn = new JButton("END TURN"); // End Turn JButton
	private JLabel lblturnstatus = new JLabel("*TurnStatus*"); // Current Turn Status lbl
	private JButton lblenemyraceimage = new JButton(); // Enemy Race Image
	private JLabel lblmanaValue = new JLabel(); // User's mana value
	private Player passPlayer; // Player object passed from PlayerSelectScreen
	private Timer timerObjUpdatingGame;	// Timer object that will be used to repeat methods that will keep the game updated
	private String Username; // String to hold user's username
	private String opponentUsername; // String to hold enemy's username
	private int opponentHealth; // Int to hold enemy's health
	private String opponentRace; // String to hold enemy's race name
	private int i;
	private int passIndex = 99; // Used to determine what card (jbutton) is click and is used in sending the card to the database
	private int indexe; // Used in the same way as passIndex
	private String currentTurnStatus; // String to hold current turn status
	private int cardsInHandx = 304, cardsInHandy = 623, cardsInHandwidth = 119, cardsInHandheight = 148; // x, y, width, height values used in generating JButtons
	private int yourFieldCardsHandx = 162, yourFieldCardsHandy = 464, yourFieldCardswidth = 119, yourFieldCardsheight = 148; // x, y, width, height values used in generating JButtons
	private int enemyFieldCardsHandx = 160, enemyFieldCardsHandy = 159, enemyFieldCardswidth = 119, enemyFieldCardsheight = 148;// x, y, width, height values used in generating JButtons
	private int manaValue, yourHealth; // Int to hold user's mana value and healt
	private JLabel lblyourhealth = new JLabel(); // Label that will have the user's health int fed to it
	private InGameCardViewer cardViewer; // InGameCardViewer object that will be used to display out a specfic card's information
	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unchecked")
	public FrayCardGame(Player passPlayer) throws java.sql.SQLSyntaxErrorException {
		setPassPlayer(passPlayer);
		setUsername(passPlayer.getPlayerID());
		setManaValue(1);
		
		JLabel lblenemyhealth = new JLabel();
		JLabel cardDeckFractionLabel = new JLabel();
		
		cardViewer = new InGameCardViewer(getUsername());
		cardViewer.setVisible(true);
		
		passList = (ArrayList<FrayCard>) passPlayer.getFrayCardDeck().clone();
		fullpassList = (ArrayList<FrayCard>) passList.clone();
		Collections.shuffle(passList);
		
		for(int i = 0; i < arrayOfCardsOnHand.length; i++) {
			if(i == 0) {
				arrayOfCardsOnHand[i] = passList.get(i).getCardName();
				passList.remove(i);
			}
		
			if(i > 0) {
				arrayOfCardsOnHand[i] = passList.get(i-1).getCardName();
				passList.remove(i-1);
			}
		}
		
		timerObjUpdatingGame = new Timer(true);
		timerObjUpdatingGame.schedule(new TimerTask() {

			@Override
			public void run() {
				inGameStatusCheck();
				inGameStatusCheckPlayer2();
				setOpponentData();
				setOpponentData2();
				setYourData();
				setYourData2();
				playerSetFirstTurnStatus();
				getCurrentPlayerTurnStatus();
				getCurrentPlayerTurnStatusPlayer2();
				lblenemyhealth.setText(String.valueOf(getOpponentHealth()));
				cardDeckFractionLabel.setText(passList.size() + "/" + passPlayer.getFrayCardDeck().size());
				lblyourhealth.setText(getYourHealth() + "Health");
				removeCardIfDead();
				removeCardIfDead2();
				updateEnemyFieldPlayer1();
				updateEnemyFieldPlayer2();
			}
			
		}, 0, 1000);
		setGameFrame(new JFrame());
		getGameFrame().setBounds(100, 100, 1200, 800);
		getGameFrame().setResizable(false);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(null);
		getGameFrame().setContentPane(contentPane);
		
		getGameFrame().setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		getGameFrame().addWindowListener(new WindowAdapter(){ // Closes system, we only need to close this window.
			@Override
			public void windowClosing(WindowEvent we) {
			int value = JOptionPane.showConfirmDialog(null, "Are you sure you want to exit?", "", JOptionPane.YES_NO_OPTION);
			
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
					cardViewer.dispose();
					getGameFrame().setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
					timerObjUpdatingGame.cancel();
					timerObjUpdatingGame.purge();
					getGameFrame().dispose();
				} catch (SQLException e1) {
					e1.printStackTrace(); // Make the error display out nicer
				}
     			
			}
			
			else {/*Do Nothing*/}
				}
			});
		
		JPanel panel = new JPanel();
		panel.setBounds(0, 0, 1194, 148);
		contentPane.add(panel);
		
		JButton btnClearButtonSelection = new JButton("Clear Button Selection");
		btnClearButtonSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				passIndex = 8;
			}
		});
		
		btnClearButtonSelection.setBounds(1022, 629, 162, 23);
		contentPane.add(btnClearButtonSelection);
		panel.add(lblenemyhealth);
		if(passPlayer.getPlayerRaceName().equalsIgnoreCase("Elf")) {
			lblenemyraceimage.setIcon(new ImageIcon(FrayCardGame.class.getResource("../images/ElfGeneral2.jpg")));
		}	 
		else if (passPlayer.getPlayerRaceName().equalsIgnoreCase("Human")) {
			lblenemyraceimage.setIcon(new ImageIcon(FrayCardGame.class.getResource("../images/HumanGeneral2.png")));
		}
		panel.add(lblenemyraceimage);
		lblenemyraceimage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//Call CardsAffect Class here
			}
		});
		
		cardDeckFractionLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cardDeckFractionLabel.setBounds(142, 629, 122, 94);
		contentPane.add(cardDeckFractionLabel);
		
		lblmanaValue.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblmanaValue.setBounds(275, 629, 122, 94);
		lblmanaValue.setText(getManaValue() + "/" + 10);
		contentPane.add(lblmanaValue);
		
		for(int j = 0; j < btnCardsInHand.length; j++) {//Your Hand Cards
			btnCardsInHand[j] = new JButton();
			btnCardsInHand[j].setText(String.valueOf(arrayOfCardsOnHand[j]));
			btnCardsInHand[j].setHorizontalTextPosition(JButton.CENTER);
			btnCardsInHand[j].setVerticalTextPosition(JButton.CENTER);
			btnCardsInHand[j].setBounds(cardsInHandx, cardsInHandy, cardsInHandwidth, cardsInHandheight);
			cardsInHandx = cardsInHandx + 129;
			contentPane.add(btnCardsInHand[j]);
		}
		
		for(i = 0; i < yourFieldCards.length; i++) { // Your Field Cards
			yourFieldCards[i] = new JButton();
			yourFieldCards[i].setText("Card " + (i + 1));
			yourFieldCards[i].setHorizontalTextPosition(JButton.CENTER);
			yourFieldCards[i].setVerticalTextPosition(JButton.CENTER);
			yourFieldCards[i].setBounds(yourFieldCardsHandx, yourFieldCardsHandy, yourFieldCardswidth, yourFieldCardsheight);
			yourFieldCardsHandx = yourFieldCardsHandx + 152;
			contentPane.add(yourFieldCards[i]);
		}
		
		for(int e = 0; e < enemyFieldCards.length; e++) {
			enemyFieldCards[e] = new JButton();
			enemyFieldCards[e].setText("Card " + (i + 1));
			enemyFieldCards[e].setHorizontalTextPosition(JButton.CENTER);
			enemyFieldCards[e].setVerticalTextPosition(JButton.CENTER);
			enemyFieldCards[e].setBounds(enemyFieldCardsHandx, enemyFieldCardsHandy, enemyFieldCardswidth, enemyFieldCardsheight);
			enemyFieldCardsHandx = enemyFieldCardsHandx + 152;
			contentPane.add(enemyFieldCards[e]);
		}
		
		btnEndTurn.setBounds(1022, 651, 162, 72);
		contentPane.add(btnEndTurn);
		btnEndTurn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					endTurn();
					endTurnPlayer2();
					
					getCurrentPlayerTurnStatus();
					getCurrentPlayerTurnStatusPlayer2();
			}
		});

		lblturnstatus.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblturnstatus.setBounds(1022, 723, 144, 39);
		contentPane.add(lblturnstatus);
		
		lblyourhealth.setBounds(142, 738, 164, 14);
		contentPane.add(lblyourhealth);
		
		JLabel lblusername = new JLabel(getUsername());
		lblusername.setForeground(Color.WHITE);
		lblusername.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblusername.setBounds(20, 735, 102, 14);
		contentPane.add(lblusername);
		
		JLabel CharacterPotrait = new JLabel(new ImageIcon(FrayCardGame.class.getResource("../images/HumanGeneral2.png")));
		CharacterPotrait.setBounds(0, 623, 134, 148);
		contentPane.add(CharacterPotrait);
		
		JLabel yourTableBackground = new JLabel(new ImageIcon(FrayCardGame.class.getResource("../images/CardTable.png")));
		yourTableBackground.setBounds(0, 623, 1194, 148);
		contentPane.add(yourTableBackground);
		
		JLabel grassTexture = new JLabel(new ImageIcon(FrayCardGame.class.getResource("../images/PlayingField2.jpg")));
		grassTexture.setBounds(0, 0, 1194, 771);
		contentPane.add(grassTexture);
		
		enemyFieldCards[0].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 8) {setText(enemyFieldCards[0].getText());setText2(enemyFieldCards[0].getText());}
			}
			
		});
		
		enemyFieldCards[1].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 8) {setText(enemyFieldCards[1].getText());setText2(enemyFieldCards[1].getText());}
			}
			
		});
		enemyFieldCards[2].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 8) {setText(enemyFieldCards[2].getText());setText2(enemyFieldCards[2].getText());}
			}
			
		});
		enemyFieldCards[3].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 8) {setText(enemyFieldCards[3].getText());setText2(enemyFieldCards[3].getText());}
			}
			
		});
		enemyFieldCards[4].addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 8) {setText(enemyFieldCards[4].getText());setText2(enemyFieldCards[4].getText());}
			}
			
		});
		
		btnCardsInHand[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				setPassIndex(0);
				setTextInHandCards(btnCardsInHand[0].getText());
			}
		});
	
		btnCardsInHand[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				setPassIndex(1);
				setTextInHandCards(btnCardsInHand[1].getText());
			}
		});
		
		btnCardsInHand[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				setPassIndex(2);
				setTextInHandCards(btnCardsInHand[2].getText());
			}
		});
		
		btnCardsInHand[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				setPassIndex(3);
				setTextInHandCards(btnCardsInHand[3].getText());
			}
		});
		
		btnCardsInHand[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				setPassIndex(4);
				setTextInHandCards(btnCardsInHand[4].getText());
			}
		});
		
		yourFieldCards[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 99) {JOptionPane.showMessageDialog(null, "Please chose a card to put onto the field first!");}
				else if (passIndex == 8) {
					setText(yourFieldCards[0].getText());
					setText2(yourFieldCards[0].getText());
				}
				else {
			
					int index1 = searchForCard(arrayOfCardsOnHand[getPassIndex()]);

					int cardEnergyCost = fullpassList.get(index1).getEnergyCost();
					int calculation = getManaValue() - cardEnergyCost;
					if(calculation >= 0 ) {
						setManaValue(calculation);
						lblmanaValue.setText(getManaValue() + "/" + 10);
						yourFieldCards[0].setText(arrayOfCardsOnHand[getPassIndex()]);
						putCardOnToField(getPassIndex(), 1); // CardPosition in arrayList, card field button position
						putCardOnToFieldPlayer2(getPassIndex(), 1);
					
						arrayOfCardsOnHand[getPassIndex()] = "Null";
						passIndex = 8;
					}
					
					else {
						JOptionPane.showMessageDialog(null, "You will have no mana!");
					}
					
				}
			}
		});
		
		yourFieldCards[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 99) {JOptionPane.showMessageDialog(null, "Please chose a card to put onto the field first!");}
				else if (passIndex == 8) {
					setText(yourFieldCards[1].getText());
					setText2(yourFieldCards[1].getText());
				}
				else {
					
					int index1 = searchForCard(arrayOfCardsOnHand[getPassIndex()]);
					
					
					int cardEnergyCost = fullpassList.get(index1).getEnergyCost();
					int calculation = getManaValue() - cardEnergyCost;
					if(calculation >= 0 ) {
						setManaValue(calculation);
						lblmanaValue.setText(getManaValue() + "/" + 10);
						yourFieldCards[1].setText(arrayOfCardsOnHand[getPassIndex()]);
						putCardOnToField(getPassIndex(), 2); // CardPosition in arrayList, card field button position
						putCardOnToFieldPlayer2(getPassIndex(), 2);
						
						arrayOfCardsOnHand[getPassIndex()] = "Null";
						passIndex = 8;
					}
					
					else {
						JOptionPane.showMessageDialog(null, "You will have no mana!");
					}
				}
			}
		});
		
		yourFieldCards[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 99) {JOptionPane.showMessageDialog(null, "Please chose a card to put onto the field first!");}
				else if (passIndex == 8) {
					setText(yourFieldCards[2].getText());
					setText2(yourFieldCards[2].getText());
				}
				else {
					int index1 = searchForCard(arrayOfCardsOnHand[getPassIndex()]);
					
					int cardEnergyCost = fullpassList.get(index1).getEnergyCost();
					int calculation = getManaValue() - cardEnergyCost;
					if(calculation >= 0 ) {
						setManaValue(calculation);
						lblmanaValue.setText(getManaValue() + "/" + 10);
						yourFieldCards[2].setText(arrayOfCardsOnHand[getPassIndex()]);
						putCardOnToField(getPassIndex(), 3); // CardPosition in arrayList, card field button position
						putCardOnToFieldPlayer2(getPassIndex(), 3);
						
						arrayOfCardsOnHand[getPassIndex()] = "Null";
						passIndex = 8;
					}
					
					else {
						JOptionPane.showMessageDialog(null, "You will have no mana!");
					}
				}
			}
		});
		
		yourFieldCards[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 99) {JOptionPane.showMessageDialog(null, "Please chose a card to put onto the field first!");}
				else if (passIndex == 8) {
					setText(yourFieldCards[3].getText());
					setText2(yourFieldCards[3].getText());
				}
				else {
					int index1 = searchForCard(arrayOfCardsOnHand[getPassIndex()]);
					
					int cardEnergyCost = fullpassList.get(index1).getEnergyCost();
					int calculation = getManaValue() - cardEnergyCost;
					if(calculation >= 0 ) {
						setManaValue(calculation);
						lblmanaValue.setText(getManaValue() + "/" + 10);
						yourFieldCards[3].setText(arrayOfCardsOnHand[getPassIndex()]);
						putCardOnToField(getPassIndex(), 4); // CardPosition in arrayList, card field button position
						putCardOnToFieldPlayer2(getPassIndex(), 4);
					
						arrayOfCardsOnHand[getPassIndex()] = "Null";
						passIndex = 8;
					}
					
					else {
						JOptionPane.showMessageDialog(null, "You will have no mana!");
					}
				}
			}
		});
		
		yourFieldCards[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 99) {JOptionPane.showMessageDialog(null, "Please chose a card to put onto the field first!");}
				else if (passIndex == 8) {
					setText(yourFieldCards[4].getText());
					setText2(yourFieldCards[4].getText());
				}
				else {
					int index1 = searchForCard(arrayOfCardsOnHand[getPassIndex()]);
					
					int cardEnergyCost = fullpassList.get(index1).getEnergyCost();
					int calculation = getManaValue() - cardEnergyCost;
					if(calculation >= 0 ) {
						setManaValue(calculation);
						lblmanaValue.setText(getManaValue() + "/" + 10);
						yourFieldCards[4].setText(arrayOfCardsOnHand[getPassIndex()]);
						putCardOnToField(getPassIndex(), 5); // CardPosition in arrayList, card field button position
						putCardOnToFieldPlayer2(getPassIndex(), 5);
					
						arrayOfCardsOnHand[getPassIndex()] = "Null";
						passIndex = 8;
					}
					
					else {
						JOptionPane.showMessageDialog(null, "You will have no mana!");
					}
				}
			}
		});
		
		yourFieldCards[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 99) {JOptionPane.showMessageDialog(null, "Please chose a card to put onto the field first!");}
				else if (passIndex == 8) {
					setText(yourFieldCards[5].getText());
					setText2(yourFieldCards[5].getText());
				}
				else {
					int index1 = searchForCard(arrayOfCardsOnHand[getPassIndex()]);
					
					int cardEnergyCost = fullpassList.get(index1).getEnergyCost();
					int calculation = getManaValue() - cardEnergyCost;
					if(calculation >= 0 ) {
						setManaValue(calculation);
						lblmanaValue.setText(getManaValue() + "/" + 10);
						yourFieldCards[5].setText(arrayOfCardsOnHand[getPassIndex()]);
						putCardOnToField(getPassIndex(), 6); // CardPosition in arrayList, card field button position
						putCardOnToFieldPlayer2(getPassIndex(), 6);
					
						arrayOfCardsOnHand[getPassIndex()] = "Null";
						passIndex = 8;
					}
					
					else {
						JOptionPane.showMessageDialog(null, "You will have no mana!");
					}
				}
			}
		});
	}
	
	void removeCardIfDead() {
		try {
			Connection con = JDBConnector.getCon();
			
			PreparedStatement ps;
			String sql = "SELECT SentToPlayerUsername FROM sql3282320." + getUsername() + "_SentGameInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
			while(myRs.next()) {
				setOpponentUsername(myRs.getString("SentToPlayerUsername"));
				PreparedStatement ps1;
				String sql1 = "SELECT CardHealthPoints, CardPositionInArrayList FROM sql3282320." + getUsername() + "_VS_"  + getOpponentUsername() + "_FrayCardGame WHERE CardPositionInArrayList >= '1' AND PlayerName = '" + getUsername() + "'";
				ps1 = con.prepareStatement(sql1);
				ResultSet myRs1 = ps1.executeQuery();
				
				while(myRs1.next()) {
					int cardHealth = myRs1.getInt("CardHealthPoints");
					int cardPositionInArrayList = myRs1.getInt("CardPositionInArrayList");
					
					if(cardHealth <= 0) {
						yourFieldCards[cardPositionInArrayList-1].setText("Card " + cardPositionInArrayList);//////////////////////////////////////////////////////////////////////
						
						PreparedStatement ps66;
						String sql100 = "DELETE FROM sql3282320." + getUsername()  + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE PlayerName = '" + getUsername() + "' AND CardPositionInArrayList = '"  + cardPositionInArrayList + "';";
						ps66 = con.prepareStatement(sql100);
						ps66.execute();
						
						String sql2 = "INSERT INTO sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame(PlayerName,CardPositionInArrayList) VALUES(?,?);";
						PreparedStatement ps2 = con.prepareStatement(sql2);
						ps2.setString(1, getUsername());
						ps2.setString(2, String.valueOf(cardPositionInArrayList));
						ps2.execute();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void removeCardIfDead2() {
		try {
			Connection con = JDBConnector.getCon();
			
			PreparedStatement ps;
			String sql = "SELECT SenderUsername FROM sql3282320." + getUsername() + "_ReceivedGamedInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
			while(myRs.next()) {
				setOpponentUsername(myRs.getString("SenderUsername"));	
				PreparedStatement ps1;
				String sql1 = "SELECT CardHealthPoints, CardPositionInArrayList FROM sql3282320." + getOpponentUsername() + "_VS_"  + getUsername() + "_FrayCardGame WHERE CardPositionInArrayList >= '1' AND PlayerName = '" + getUsername() + "'";
				ps1 = con.prepareStatement(sql1);
				ResultSet myRs1 = ps1.executeQuery();
				
				while(myRs1.next()) {
					int cardHealth = myRs1.getInt("CardHealthPoints");
					int cardPositionInArrayList = myRs1.getInt("CardPositionInArrayList");
					
					if(cardHealth <= 0) {
						yourFieldCards[cardPositionInArrayList-1].setText("Card " + cardPositionInArrayList);//////////////////////////////////////////////////////////////////////
						
						PreparedStatement ps66;
						String sql100 = "DELETE FROM sql3282320." + getOpponentUsername()  + "_VS_" + getUsername() + "_FrayCardGame WHERE PlayerName = '" + getUsername() + "' AND CardPositionInArrayList = '"  + cardPositionInArrayList + "';";
						ps66 = con.prepareStatement(sql100);
						ps66.execute();
						
						String sql2 = "INSERT INTO sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame(PlayerName,CardPositionInArrayList) VALUES(?,?);";
						PreparedStatement ps2 = con.prepareStatement(sql2);
						ps2.setString(1, getUsername());
						ps2.setString(2, String.valueOf(cardPositionInArrayList));
						ps2.execute();
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void setText(String name) {
			try {
				Connection con = JDBConnector.getCon();
				
				PreparedStatement ps;
				String sql = "SELECT SentToPlayerUsername FROM sql3282320." + getUsername() + "_SentGameInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
				ps = con.prepareStatement(sql);
				ResultSet myRs = ps.executeQuery();
				
				while(myRs.next()) {
					setOpponentUsername(myRs.getString("SentToPlayerUsername"));
					PreparedStatement ps1;
					String sql1 = "Select * FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE CardName = '" + name + "'";
					ps1 = con.prepareStatement(sql1);
					ResultSet myRs1 = ps1.executeQuery();
					
					while(myRs1.next()) {
						cardViewer.setLblcn(myRs1.getString("CardName"));
						cardViewer.setLblap(myRs1.getInt("CardAttackPoints"));
						cardViewer.setLblhp(myRs1.getInt("CardHealthPoints"));
						cardViewer.setLblct(myRs1.getString("CardType"));
						cardViewer.setLblec(myRs1.getInt("EnergyCost"));
						cardViewer.setLblhb(myRs1.getInt("CardSpellValueHealth"));
						cardViewer.setLblapb(myRs1.getInt("CardSpellValueAttack"));
					}
				}
			
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	
	void setTextInHandCards(String name) {
		Collections.sort(fullpassList, new Comparator<FrayCard>() {
			public int compare(FrayCard c1, FrayCard c2) {
				return c1.getCardName().compareToIgnoreCase(c2.getCardName());
			}
		});
	
		FrayCard searchKey = new FrayCard(null, name, 0,  0, 0, "", "", 0, "", 0, 0, 0, 0);
	
		int index1 = Collections.binarySearch(fullpassList, searchKey, new Comparator<FrayCard>() {
			public int compare(FrayCard c1, FrayCard c2) {
				return c1.getCardName().compareToIgnoreCase(c2.getCardName());
			}
		});
	
		cardViewer.setLblcn(fullpassList.get(index1).getCardName());
		cardViewer.setLblap(fullpassList.get(index1).getCardAttackPoints());
		cardViewer.setLblhp(fullpassList.get(index1).getCardHealthPoints());
		cardViewer.setLblct(fullpassList.get(index1).getCardType());
		cardViewer.setLblec(fullpassList.get(index1).getEnergyCost());
		cardViewer.setLblhb(fullpassList.get(index1).getSpellValueHealth());
		cardViewer.setLblapb(fullpassList.get(index1).getSpellValueAttack());
		cardViewer.setLblerb(fullpassList.get(index1).getSpellValueCost());
		cardViewer.setLblarb(fullpassList.get(index1).getSpellValueArmor());
	}
	
	void setText2(String name) {
			try {
				Connection con = JDBConnector.getCon();
		
				PreparedStatement ps;
				String sql = "SELECT SenderUsername FROM sql3282320." + getUsername() + "_ReceivedGamedInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
				ps = con.prepareStatement(sql);
				ResultSet myRs = ps.executeQuery();
		
				while(myRs.next()) {
					setOpponentUsername(myRs.getString("SenderUsername"));	
					PreparedStatement ps1;
					String sql1 = "Select * FROM sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame WHERE CardName = '" + name + "'";
					ps1 = con.prepareStatement(sql1);
					ResultSet myRs1 = ps1.executeQuery();
			
					while(myRs1.next()) {
						cardViewer.setLblcn(myRs1.getString("CardName"));
						cardViewer.setLblap(myRs1.getInt("CardAttackPoints"));
						cardViewer.setLblhp(myRs1.getInt("CardHealthPoints"));
						cardViewer.setLblct(myRs1.getString("CardType"));
						cardViewer.setLblec(myRs1.getInt("EnergyCost"));
						cardViewer.setLblhb(myRs1.getInt("CardSpellValueHealth"));
						cardViewer.setLblapb(myRs1.getInt("CardSpellValueAttack"));
					}
				}
			} catch (SQLException e2) {e2.printStackTrace();}
		}
	
	int searchForCard(String name) {
		Collections.sort(fullpassList, new Comparator<FrayCard>() {
			public int compare(FrayCard c1, FrayCard c2) {
				return c1.getCardName().compareToIgnoreCase(c2.getCardName());
			}
		});
		
		FrayCard searchKey = new FrayCard(null, name, 0,  0, 0, "", "", 0, "", 0, 0, 0, 0);
		
		int index1 = Collections.binarySearch(fullpassList, searchKey, new Comparator<FrayCard>() {
			public int compare(FrayCard c1, FrayCard c2) {
				return c1.getCardName().compareToIgnoreCase(c2.getCardName());
			}
		});
		
		return index1;
	}
	
	void updateEnemyFieldPlayer1() {// 1 to 2
		try {
			Connection con = JDBConnector.getCon();
			
			PreparedStatement ps;
			String sql = "SELECT SentToPlayerUsername FROM sql3282320." + getUsername() + "_SentGameInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
			while(myRs.next()) {
				setOpponentUsername(myRs.getString("SentToPlayerUsername"));
				PreparedStatement ps1;
				String sql1 = "SELECT CardName, CardPositionInArrayList FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE PlayerName = '" + getOpponentUsername() +"' AND CardPositionInArrayList BETWEEN '1' AND '6'";
				ps1 = con.prepareStatement(sql1);
				ResultSet myRs1 = ps1.executeQuery();
				while(myRs1.next()) {
					int cardPositionInArrayList = myRs1.getInt("CardPositionInArrayList");
						enemyFieldCards[cardPositionInArrayList-1].setText(myRs1.getString("CardName"));
				}
			}
		} catch (SQLException e) {e.printStackTrace();}
	}
	void updateEnemyFieldPlayer2() {/*2 to 1*/
		try {
			
			Connection con = JDBConnector.getCon();
			
			PreparedStatement ps;
			String sql = "SELECT SenderUsername FROM sql3282320." + getUsername() + "_ReceivedGamedInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
			while(myRs.next()) {
				setOpponentUsername(myRs.getString("SenderUsername"));	
				PreparedStatement ps1;
				String sql1 = "SELECT CardName, CardPositionInArrayList FROM sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame WHERE PlayerName = '" + getOpponentUsername() +"' AND CardPositionInArrayList BETWEEN '1' AND '6'";
				ps1 = con.prepareStatement(sql1);
				ResultSet myRs1 = ps1.executeQuery();
				while(myRs1.next()) {
					int cardPositionInArrayList = myRs1.getInt("CardPositionInArrayList");
						enemyFieldCards[cardPositionInArrayList-1].setText(myRs1.getString("CardName"));
				}
			}
		} catch(SQLException e) {e.printStackTrace();}
	}
	
	void getCurrentPlayerTurnStatus() {
		
		try {
			
			Connection con = JDBConnector.getCon();
			
			PreparedStatement ps;
			String sql = "SELECT SentToPlayerUsername FROM sql3282320." + getUsername() + "_SentGameInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
			while(myRs.next()) {
				setOpponentUsername(myRs.getString("SentToPlayerUsername"));
				try {
					PreparedStatement ps1;
					String sql1 = "SELECT PlayerTurnStatus FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE PlayerName = '" + getUsername() + "' AND CardPositionInArrayList = '0';";
					ps1 = con.prepareStatement(sql1);
					ResultSet myRs1 = ps1.executeQuery();
					while(myRs1.next()) {
						setCurrentTurnStatus(myRs1.getString("PlayerTurnStatus"));
						lblenemyraceimage.setText(getOpponentUsername());
						lblenemyraceimage.setFont(new Font("Arial", Font.PLAIN, 14));
						lblenemyraceimage.setForeground(Color.WHITE);
						lblenemyraceimage.setHorizontalTextPosition(JButton.CENTER);
						lblenemyraceimage.setVerticalTextPosition(JButton.CENTER);
					
						if(getCurrentTurnStatus().equalsIgnoreCase("Turn")) {
							lblturnstatus.setText("Your Turn!");
							yourFieldCards[0].setEnabled(true);
							yourFieldCards[1].setEnabled(true);
							yourFieldCards[2].setEnabled(true);
							yourFieldCards[3].setEnabled(true);
							yourFieldCards[4].setEnabled(true);
							yourFieldCards[5].setEnabled(true);
							btnEndTurn.setEnabled(true); // Your end turn button
						}
						
						else if(getCurrentTurnStatus().equalsIgnoreCase("NotTurn")) {
							lblturnstatus.setText("Enemy's Turn!");
					
							yourFieldCards[0].setEnabled(false);
							yourFieldCards[1].setEnabled(false);
							yourFieldCards[2].setEnabled(false);
							yourFieldCards[3].setEnabled(false);
							yourFieldCards[4].setEnabled(false);
							yourFieldCards[5].setEnabled(false);
							btnEndTurn.setEnabled(false); // Your end turn button
						}
					}
					} catch (java.sql.SQLSyntaxErrorException e) {
						e.printStackTrace();
					}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	void getCurrentPlayerTurnStatusPlayer2() {
		
		try {
			
			Connection con = JDBConnector.getCon();
			
			PreparedStatement ps;
			String sql = "SELECT SenderUsername FROM sql3282320." + getUsername() + "_ReceivedGamedInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
			while(myRs.next()) {
				setOpponentUsername(myRs.getString("SenderUsername"));	
				PreparedStatement ps11;
				String sql11 = "SELECT PlayerTurnStatus FROM sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame WHERE PlayerName = '" + getUsername() + "' AND CardPositionInArrayList = '10';";
				ps11 = con.prepareStatement(sql11);
				ResultSet myRs11 = ps11.executeQuery();
					while(myRs11.next()) {
						setCurrentTurnStatus(myRs11.getString("PlayerTurnStatus"));
						lblenemyraceimage.setText(getOpponentUsername());
						lblenemyraceimage.setFont(new Font("Arial", Font.PLAIN, 14));
						lblenemyraceimage.setForeground(Color.WHITE);
						lblenemyraceimage.setHorizontalTextPosition(JButton.CENTER);
						lblenemyraceimage.setVerticalTextPosition(JButton.CENTER);

						if(getCurrentTurnStatus().equalsIgnoreCase("Turn")) {
							lblturnstatus.setText("Your Turn!");
							yourFieldCards[0].setEnabled(true);
							yourFieldCards[1].setEnabled(true);
							yourFieldCards[2].setEnabled(true);
							yourFieldCards[3].setEnabled(true);
							yourFieldCards[4].setEnabled(true);
							yourFieldCards[5].setEnabled(true);
							btnEndTurn.setEnabled(true); // Enemy's End turn Button
						}
		
						else if(getCurrentTurnStatus().equalsIgnoreCase("NotTurn")) {
							lblturnstatus.setText("Enemy's Turn!");
							yourFieldCards[0].setEnabled(false);
							yourFieldCards[1].setEnabled(false);
							yourFieldCards[2].setEnabled(false);
							yourFieldCards[3].setEnabled(false);
							yourFieldCards[4].setEnabled(false);
							yourFieldCards[5].setEnabled(false);
							btnEndTurn.setEnabled(false); // Enemy's End turn Button
						}
					}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
	}

	void inGameStatusCheck() {
		try {
			Connection con = JDBConnector.getCon();
			PreparedStatement ps;
			String sql = "SELECT SentToPlayerUsername FROM sql3282320." + getUsername() + "_SentGameInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
		
			while(myRs.next()) {
				setOpponentUsername(myRs.getString("SentToPlayerUsername"));
				inGameStatusCheckSQL();
			}
	
		} catch(SQLException e) {e.printStackTrace();}
	}
	 
	void inGameStatusCheckPlayer2() {
		try {
			Connection con = JDBConnector.getCon();	
			PreparedStatement ps;
			String sql = "SELECT SenderUsername FROM sql3282320." + getUsername() + "_ReceivedGamedInvitesTable WHERE SecondGameInviteStatus = 'Accepted';";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
			while(myRs.next()) {
				setOpponentUsername(myRs.getString("SenderUsername"));
				inGameStatusCheckSQL();
			}
		} catch(SQLException e) {e.printStackTrace();}
	}
	
	 void setOpponentData() {
		 try {
			 Connection con = JDBConnector.getCon();
			 PreparedStatement ps;
			 String sql = "SELECT CardHealthPoints FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE PlayerName = '" + getOpponentUsername() +"' AND CardPositionInArrayList = '10';";
			 ps = con.prepareStatement(sql);
			 ResultSet myRs = ps.executeQuery();
			 
			 while(myRs.next()) {
				 setOpponentHealth(myRs.getInt("CardHealthPoints"));
				 if(getOpponentHealth() <= 0) {
					JOptionPane.showMessageDialog(null, "!VICTORY!");
					getGameFrame().dispose();
					timerObjUpdatingGame.cancel();
					PlayerSelectScreen newFrayGame = new PlayerSelectScreen(getUsername());
					newFrayGame.getPassFrame().setVisible(true);
				 }
			 }	
		 } catch (SQLSyntaxErrorException e){	
		 } catch (SQLException e) {
			e.printStackTrace();
		}
	 }
	 
	 void setOpponentData2() {
		 try {
			 Connection con = JDBConnector.getCon();
			 PreparedStatement ps;
			 String sql = "SELECT CardHealthPoints FROM sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame WHERE PlayerName = '" + getOpponentUsername() +"'  AND CardPositionInArrayList = '0';";
			 ps = con.prepareStatement(sql);
			 ResultSet myRs = ps.executeQuery();
			 
			 while(myRs.next()) {
				 setOpponentHealth(myRs.getInt("CardHealthPoints"));
				 if(getOpponentHealth() <= 0) {
						JOptionPane.showMessageDialog(null, "!VICTORY!"); 
						getGameFrame().dispose();
						timerObjUpdatingGame.cancel();
						PlayerSelectScreen newFrayGame = new PlayerSelectScreen(getUsername());
						newFrayGame.getPassFrame().setVisible(true);
				}
			 }
		} catch (SQLSyntaxErrorException e){	
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	 }
	 
	 void setYourData() {
		 try {
			 Connection con = JDBConnector.getCon();
			 PreparedStatement ps;
			 String sql = "SELECT CardHealthPoints FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE PlayerName = '" + getUsername() +"' AND CardPositionInArrayList = '0';";
			 ps = con.prepareStatement(sql);
			 ResultSet myRs = ps.executeQuery();
			 
			 while(myRs.next()) {
				 setYourHealth(myRs.getInt("CardHealthPoints"));
			 }	
		 } catch (SQLSyntaxErrorException e){	
		 } catch (SQLException e) {
			e.printStackTrace();
		}
	 }
	 
	 void setYourData2() {
		 try {
			 Connection con = JDBConnector.getCon();
			 PreparedStatement ps;
			 String sql = "SELECT CardHealthPoints FROM sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame WHERE PlayerName = '" + getUsername() +"'  AND CardPositionInArrayList = '10';";
			 ps = con.prepareStatement(sql);
			 ResultSet myRs = ps.executeQuery();
			 
			 while(myRs.next()) {
				 setYourHealth(myRs.getInt("CardHealthPoints"));
			 }
		} catch (SQLSyntaxErrorException e){	
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
	 }
	 
	 void playerSetFirstTurnStatus() {
			try {
				Connection con = JDBConnector.getCon();
				
				PreparedStatement ps;
				String sql = "SELECT SentToPlayerUsername FROM sql3282320." + getUsername() + "_SentGameInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
				ps = con.prepareStatement(sql);
				ResultSet myRs = ps.executeQuery();
				
				while(myRs.next()) {
						setOpponentUsername(myRs.getString("SentToPlayerUsername"));
					
						int random = (int) (Math.random() + 1);
					
						// This will work based on how the table player names are set.
						// 1 = Player one
						// 2 = Player two
						
							PreparedStatement ps1;
							String sql5 = "SELECT PlayerTurnStatus FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE PlayerName = '" + getOpponentUsername() + "';";
							ps1 = con.prepareStatement(sql5);
							ResultSet myRs1 = ps1.executeQuery();
						
							while(myRs1.next()) {
								PreparedStatement ps3;
								String sql7 = "SELECT PlayerTurnStatus FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE PlayerName = '" + getUsername() + "';";
								ps3 = con.prepareStatement(sql7);
								ResultSet myRs3 = ps3.executeQuery();
							
								while(myRs3.next()) {
									String turnStatusPlayer1 = myRs3.getString("PlayerTurnStatus");
									if(turnStatusPlayer1.equalsIgnoreCase("FIRSTTURN")) {
										PreparedStatement ps2;
										String sql6 = "SELECT PlayerTurnStatus FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE PlayerName = '" + getOpponentUsername() + "'";
										ps2 = con.prepareStatement(sql6);
										ResultSet myRs2 = ps2.executeQuery();
										
										while(myRs2.next()) {
											String turnStatusPlayer2 = myRs2.getString("PlayerTurnStatus");
											if(turnStatusPlayer2.equalsIgnoreCase("FIRSTTURN")) {
												try {
													if(random == 1) {
														try {
															Statement myStat = con.createStatement();
															String sql1 = "UPDATE sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame SET PlayerTurnStatus = 'Turn' WHERE PlayerName = '" + getUsername() + "';";
															myStat.executeUpdate(sql1);
															
															Statement myStat1 = con.createStatement();
															String sql11 = "UPDATE sql3282320." + getUsername()+ "_VS_" + getOpponentUsername() + "_FrayCardGame SET PlayerTurnStatus = 'NotTurn' WHERE PlayerName = '" + getOpponentUsername() + "';";
															myStat1.executeUpdate(sql11);
														} catch (java.sql.SQLSyntaxErrorException e1) {
															e1.printStackTrace();
														
															Statement myStat = con.createStatement();
															String sql2 = "UPDATE sql3282320." + getOpponentUsername()  + "_VS_" + getUsername() + "_FrayCardGame SET PlayerTurnStatus = 'NotTurn' WHERE PlayerName = '" + getOpponentUsername()  + "';";
															myStat.executeUpdate(sql2);
															
															Statement myStat1 = con.createStatement();
															String sql21 = "UPDATE sql3282320." + getOpponentUsername()  + "_VS_" + getUsername() + "_FrayCardGame SET PlayerTurnStatus = 'Turn' WHERE PlayerName = '" + getUsername() + "';";
															myStat1.executeUpdate(sql21);
														}
													}
							
													if(random == 2) {
														try {
															Statement myStat = con.createStatement();
															String sql3 = "UPDATE sql3282320." + getUsername() + "_VS_" + getOpponentUsername()  + "_FrayCardGame SET PlayerTurnStatus = 'Turn' WHERE PlayerName = '" + getOpponentUsername()  + "';";
															myStat.executeUpdate(sql3);
															
															Statement myStat1 = con.createStatement();
															String sql31 = "UPDATE sql3282320." + getUsername() + "_VS_" + getOpponentUsername()  + "_FrayCardGame SET PlayerTurnStatus = 'NotTurn' WHERE PlayerName = '" + getUsername()  + "';";
															myStat1.executeUpdate(sql31);
														} catch (java.sql.SQLSyntaxErrorException e1) {
															e1.printStackTrace();
															
															Statement myStat = con.createStatement();
															String sql4 = "UPDATE sql3282320." + getOpponentUsername()  + "_VS_" + getUsername() + "_FrayCardGame SET PlayerTurnStatus = 'NotTurn' WHERE PlayerName = '" + getUsername() + "';";
															myStat.executeUpdate(sql4);
															
															Statement myStat1 = con.createStatement();
															String sql41 = "UPDATE sql3282320." + getOpponentUsername()  + "_VS_" + getUsername() + "_FrayCardGame SET PlayerTurnStatus = 'Turn' WHERE PlayerName = '" + getOpponentUsername() + "';";
															myStat1.executeUpdate(sql41);
														}
													}
												} catch (SQLException e1) {
													e1.printStackTrace();
												}
											}
										}
									}
								}
							}
						}
				} catch(SQLException e) {e.printStackTrace();}			
			}
	 
	 void endTurn() {
			try {
				
				Connection con = JDBConnector.getCon();

				PreparedStatement ps;
				String sql = "SELECT SentToPlayerUsername FROM sql3282320." + getUsername() + "_SentGameInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
				ps = con.prepareStatement(sql);
				ResultSet myRs = ps.executeQuery();
				
				
				while(myRs.next()) {
					setOpponentUsername(myRs.getString("SentToPlayerUsername"));
		
					
					PreparedStatement ps1;
					String sql11 = "SELECT PlayerTurnStatus FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE CardPositionInArrayList = '0' AND PlayerName = '" + getUsername() + "';"; 
					ps1 = con.prepareStatement(sql11);
					ResultSet myRs1 = ps1.executeQuery();
					
					while(myRs1.next()) {
						String turnStatus = myRs1.getString("PlayerTurnStatus");
						
						if (turnStatus.equals("Turn")) {
							Statement myStat = con.createStatement();
							String sql1 = "UPDATE sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame SET PlayerTurnStatus = 'NotTurn' WHERE PlayerName = '" + getUsername() + "' AND CardPositionInArrayList = '0';";
							myStat.executeUpdate(sql1);
				
							Statement myStat2 = con.createStatement();
							String sql2 = "UPDATE sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame SET PlayerTurnStatus = 'Turn' WHERE PlayerName = '" + getOpponentUsername() + "' AND CardPositionInArrayList = '10';";
							myStat2.executeUpdate(sql2);
							setManaValue(getManaValue() + 1);
							lblmanaValue.setText(getManaValue() + "/" + 10);
							drawCardAtTheEndOfTurn();
						}
						
						else if (turnStatus.equals("NotTurn")) {
							Statement myStat = con.createStatement();
							String sql1 = "UPDATE sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame SET PlayerTurnStatus = 'Turn' WHERE PlayerName = '" + getUsername() + "' AND CardPositionInArrayList = '0';";
							myStat.executeUpdate(sql1);
				
							Statement myStat2 = con.createStatement();
							String sql2 = "UPDATE sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame SET PlayerTurnStatus = 'NotTurn' WHERE PlayerName = '" + getOpponentUsername() + "' AND CardPositionInArrayList = '10';";
							myStat2.executeUpdate(sql2);
							setManaValue(getManaValue() + 1);
							lblmanaValue.setText(getManaValue() + "/" + 10);
							
							drawCardAtTheEndOfTurn();
						}
					}
					
					
				}
				} catch (SQLException e) {
					e.printStackTrace();
			}
		}
		
		void endTurnPlayer2() {
			try {
				
				Connection con = JDBConnector.getCon();

				PreparedStatement ps;
				String sql = "SELECT SenderUsername FROM sql3282320." + getUsername() + "_ReceivedGamedInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
				ps = con.prepareStatement(sql);
				ResultSet myRs = ps.executeQuery();
				
				while(myRs.next()) {
					setOpponentUsername(myRs.getString("SenderUsername"));
					
					PreparedStatement ps1;
					String sql11 = "SELECT PlayerTurnStatus FROM sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame WHERE CardPositionInArrayList = '10' AND PlayerName = '" + getUsername() + "';";
					ps1 = con.prepareStatement(sql11);
					ResultSet myRs1 = ps1.executeQuery();
					
					while(myRs1.next()) {
						String turnStatus = myRs1.getString("PlayerTurnStatus");
						
						if (turnStatus.equals("Turn")) {
							Statement myStat = con.createStatement();
							String sql1 = "UPDATE sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame SET PlayerTurnStatus = 'Turn' WHERE PlayerName = '" + getOpponentUsername() + "' AND CardPositionInArrayList = '0';";
							myStat.executeUpdate(sql1);
				
							Statement myStat2 = con.createStatement();
							String sql2 = "UPDATE sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame SET PlayerTurnStatus = 'NotTurn' WHERE PlayerName = '" + getUsername() + "' AND CardPositionInArrayList = '10';";
							myStat2.executeUpdate(sql2);
							setManaValue(getManaValue() + 1);
							lblmanaValue.setText(getManaValue() + "/" + 10);
							
							drawCardAtTheEndOfTurn();
						}
						
						else if (turnStatus.equals("NotTurn")) {
							Statement myStat = con.createStatement();
							String sql1 = "UPDATE sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame SET PlayerTurnStatus = 'NotTurn' WHERE PlayerName = '" + getOpponentUsername() + "' AND CardPositionInArrayList = '0';";
							myStat.executeUpdate(sql1);
				
							Statement myStat2 = con.createStatement();
							String sql2 = "UPDATE sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame SET PlayerTurnStatus = 'Turn' WHERE PlayerName = '" + getUsername() + "' AND CardPositionInArrayList = '10';";
							myStat2.executeUpdate(sql2);
							setManaValue(getManaValue() + 1);
							lblmanaValue.setText(getManaValue() + "/" + 10);
							
							drawCardAtTheEndOfTurn();
						}
					}
					
				}
				} catch (SQLException e) {
					e.printStackTrace();
			}
		}
		
		void drawCardAtTheEndOfTurn() {	
			/* 
			 * Find first null card and than add a new card from passList in that same position.
			 * What I was thinking was to find the same card and than delete.
			 */
			try {
				Collections.shuffle(passList);
				for(int g = 0; g < 5; g++) {
					if(arrayOfCardsOnHand[g].equals("Null")) {
						indexe = g;
					} 
				}
			
				arrayOfCardsOnHand[indexe] = passList.get(indexe).getCardName();
				btnCardsInHand[indexe].setText(passList.get(indexe).getCardName());
				btnCardsInHand[indexe].setIcon(passList.get(indexe).getImage());
				for(int i = 0; i < btnCardsInHand.length; i++) {
					btnCardsInHand[i].setVisible(true);
				}
			}catch(java.lang.IndexOutOfBoundsException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, "No cards left");
			}
		}
	 
	 void inGameStatusCheckSQL() {
			try {
				Connection con = JDBConnector.getCon();
				PreparedStatement ps2;
				String sql2 = "SELECT OnlineStatus FROM sql3282320.UserDataTable WHERE Username = '" + getOpponentUsername() + "';";
				
				ps2 = con.prepareStatement(sql2);
				ResultSet myRs2 = ps2.executeQuery();
				
				while(myRs2.next()) {
						String opponentOnlineStatus = myRs2.getString("OnlineStatus");
					
						if(opponentOnlineStatus.equalsIgnoreCase("Offline")) {
								Statement myStat = con.createStatement();
								String sql3 = "UPDATE sql3282320.UserDataTable SET InGame = 'No' WHERE Username = '" + getUsername() + "';";
								myStat.executeUpdate(sql3);
					
								PreparedStatement ps3;
								String sql4 = "SELECT UserXP FROM sql3282320.UserDataTable WHERE Username = '" + getUsername() + "';";
								ps3 = con.prepareStatement(sql4);
								ResultSet myRs3 = ps3.executeQuery();
						
								while(myRs3.next()) {
									int userXP = myRs3.getInt("UserXP");
									userXP = userXP + 10;
								
									if(userXP < 0) {
										userXP = 0;
									}
						
									Statement myStat1 = con.createStatement();
									String sql5 = "UPDATE sql3282320.UserDataTable SET UserXP = '" + userXP + "' WHERE Username = '" + getUsername() + "';";
									myStat1.executeUpdate(sql5);
								}
						
								Statement myStat2 = con.createStatement();
								String sql6 = "UPDATE sql3282320.UserDataTable SET InGame = 'No' WHERE Username = '" + getOpponentUsername() + "';";
								myStat2.executeUpdate(sql6);
						
								PreparedStatement ps4;
								String sql7 = "SELECT UserXP FROM sql3282320.UserDataTable WHERE Username = '" + getOpponentUsername() + "';";
								ps4 = con.prepareStatement(sql7);
								ResultSet myRs4 = ps4.executeQuery();
						
								while(myRs4.next()) {
									int userXP = myRs4.getInt("UserXP");
									userXP = userXP - 10;
									
									if(userXP < 0) {
										userXP = 0;
									}
							
									Statement myStat3 = con.createStatement();
									String sql8 = "UPDATE sql3282320.UserDataTable SET UserXP = '" + userXP + "' WHERE Username = '" + getOpponentUsername() + "';";
									myStat3.executeUpdate(sql8);
								}
							
								try {
									PreparedStatement ps5;
									String sql9 = "DROP TABLE sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame;";
									ps5 = con.prepareStatement(sql9);
									ps5.execute();
								} catch (java.sql.SQLSyntaxErrorException e) {
									PreparedStatement ps55;
									String sql99 = "DROP TABLE sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame;";
									ps55 = con.prepareStatement(sql99);
									ps55.execute();
								}
							
								PreparedStatement ps6;
								String sql10 = "DELETE FROM sql3282320." + getOpponentUsername() + "_ReceivedGamedInvitesTable WHERE SenderUsername = '" + getUsername() + "';";
								ps6 = con.prepareStatement(sql10);
								ps6.execute();
								
								PreparedStatement ps66;
								String sql100 = "DELETE FROM sql3282320." + getUsername() + "_ReceivedGamedInvitesTable WHERE SenderUsername = '" + getOpponentUsername() + "';";
								ps66 = con.prepareStatement(sql100);
								ps66.execute();
						
								PreparedStatement ps7;
								String sql11 = "DELETE FROM sql3282320." + getUsername() + "_SentGameInvitesTable WHERE SentToPlayerUsername = '" + getOpponentUsername() + "';";
								ps7 = con.prepareStatement(sql11);
								ps7.execute();
								
								PreparedStatement ps77;
								String sql111 = "DELETE FROM sql3282320." + getOpponentUsername() + "_SentGameInvitesTable WHERE SentToPlayerUsername = '" + getUsername() + "';";
								ps77 = con.prepareStatement(sql111);
								ps77.execute();
					
								JOptionPane.showMessageDialog(null, "The Game Between " + getOpponentUsername() + " and " + getUsername() + " has ended! YOU WIN!"); // Add who won.
								timerObjUpdatingGame.cancel();
								getGameFrame().dispose();
								PlayerSelectScreen returnBackToPlayerSelectScreen = new PlayerSelectScreen(getUsername());
								returnBackToPlayerSelectScreen.getPassFrame().setVisible(true);
						}
				 }
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			
		}
	 
	 void putCardOnToField(int cardPosition, int index) {
			try {
				Connection con = JDBConnector.getCon();
				PreparedStatement ps;
				String sql = "SELECT SentToPlayerUsername FROM sql3282320." + getUsername() + "_SentGameInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
				ps = con.prepareStatement(sql);
				ResultSet myRs = ps.executeQuery();
				
				
				while(myRs.next()) {
				//try {
					Collections.sort(fullpassList, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					FrayCard searchKey = new FrayCard(null, arrayOfCardsOnHand[cardPosition], 0,  0, 0, "", "", 0, "", 0, 0, 0, 0);
					
					int index1 = Collections.binarySearch(fullpassList, searchKey, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					
						setOpponentUsername(myRs.getString("SentToPlayerUsername"));

						Statement myStat = con.createStatement();
						String sql1 = "UPDATE sql3282320." + getUsername() + "_VS_" + opponentUsername + "_FrayCardGame SET " +
						"CardImage = '" + String.valueOf(fullpassList.get(index1).getImage()) + "', CardName = '" + String.valueOf(fullpassList.get(index1).getCardName()) 
						+ "', CardAttackPoints = '" + fullpassList.get(index1).getCardAttackPoints() + "', CardHealthPoints = '" + fullpassList.get(index1).getCardHealthPoints()
						+ "', CardArmorPoints = '" + fullpassList.get(index1).getCardArmorPoints() + "', CardClass = '" + fullpassList.get(index1).getCardClass() 
						+ "', CardType = '" + String.valueOf(fullpassList.get(index1).getCardType()) + "', EnergyCost = '" + fullpassList.get(index1).getEnergyCost()
						+ "', CardState = '" + String.valueOf(fullpassList.get(index1).getCardState()) + "', CardSpellValueHealth = '" + fullpassList.get(index1).getSpellValueHealth()
						+ "', CardSpellValueAttack = '" + fullpassList.get(index1).getSpellValueAttack() + "'" + "WHERE CardPositionInArrayList = '" + index + "' AND PlayerName = '" + getUsername() + "';";
						myStat.executeUpdate(sql1);
						
						Collections.sort(passList, new Comparator<FrayCard>() {
							public int compare(FrayCard c1, FrayCard c2) {
								return c1.getCardName().compareToIgnoreCase(c2.getCardName());
							}
						});
						
						FrayCard searchKey1 = new FrayCard(null, fullpassList.get(index1).getCardName(), 0,  0, 0, "", "", 0, "", 0, 0, 0, 0);
						
						int index2 = Collections.binarySearch(passList, searchKey1, new Comparator<FrayCard>() {
							public int compare(FrayCard c1, FrayCard c2) {
								return c1.getCardName().compareToIgnoreCase(c2.getCardName());
							}
						});
						
						try {
							try {
								passList.remove(index2);
							} catch (java.lang.IndexOutOfBoundsException e) {
							Collections.sort(passList, new Comparator<FrayCard>() {
								public int compare(FrayCard c1, FrayCard c2) {
									return c1.getCardName().compareToIgnoreCase(c2.getCardName());
								}
							});
							FrayCard searchKey11 = new FrayCard(null, fullpassList.get(index1).getCardName(), 0,  0, 0, "", "", 0, "", 0, 0, 0, 0);
							
							int index11 = Collections.binarySearch(passList, searchKey11, new Comparator<FrayCard>() {
								public int compare(FrayCard c1, FrayCard c2) {
									return c1.getCardName().compareToIgnoreCase(c2.getCardName());
								}
							});
							passList.remove(index11);
							btnCardsInHand[cardPosition].setText("Null");
							btnCardsInHand[cardPosition].setVisible(false);// Reset visible of any cards in this btn array.
							Collections.shuffle(passList);
						}
							btnCardsInHand[cardPosition].setText("Null");
				
							btnCardsInHand[cardPosition].setVisible(false);// Reset visible of any cards in this btn array.
							
							Collections.shuffle(passList);
						} catch(java.lang.ArrayIndexOutOfBoundsException e) {
							btnCardsInHand[cardPosition].setText("Null");
							btnCardsInHand[cardPosition].setVisible(false);// Reset visible of any cards in this btn array.

							Collections.shuffle(passList);
							continue;
						}
					}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	 
	 void putCardOnToFieldPlayer2(int cardPosition, int index) {
			
			try {
				Connection con = JDBConnector.getCon();
				PreparedStatement ps3;
				String sql3 = "SELECT SenderUsername FROM sql3282320." + getUsername() + "_ReceivedGamedInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
				ps3 = con.prepareStatement(sql3);
				ResultSet myRs2 = ps3.executeQuery();
			
				while(myRs2.next()) {
					setOpponentUsername(myRs2.getString("SenderUsername"));
					
					Collections.sort(fullpassList, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					FrayCard searchKey = new FrayCard(null, arrayOfCardsOnHand[cardPosition], 0,  0, 0, "", "", 0, "", 0, 0, 0, 0);
					
					int index1 = Collections.binarySearch(fullpassList, searchKey, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					Statement myStat = con.createStatement();
			
					String sql1 = "UPDATE sql3282320." + opponentUsername + "_VS_" + getUsername() + "_FrayCardGame SET " +
							"CardImage = '" + String.valueOf(fullpassList.get(index1).getImage()) + "', CardName = '" + String.valueOf(fullpassList.get(index1).getCardName()) 
							+ "', CardAttackPoints = '" + fullpassList.get(index1).getCardAttackPoints() + "', CardHealthPoints = '" + fullpassList.get(index1).getCardHealthPoints()
							+ "', CardArmorPoints = '" + fullpassList.get(index1).getCardArmorPoints() + "', CardClass = '" + fullpassList.get(index1).getCardClass() 
							+ "', CardType = '" + String.valueOf(fullpassList.get(index1).getCardType()) + "', EnergyCost = '" + fullpassList.get(index1).getEnergyCost()
							+ "', CardState = '" + String.valueOf(fullpassList.get(index1).getCardState()) + "', CardSpellValueHealth = '" + fullpassList.get(index1).getSpellValueHealth()
							+ "', CardSpellValueAttack = '" + fullpassList.get(index1).getSpellValueAttack() + "'" + "WHERE CardPositionInArrayList = '" + index + "' AND PlayerName = '" + getUsername() + "';";
							myStat.executeUpdate(sql1);				
						try {
							try {
								passList.remove(index1);
							} catch (java.lang.IndexOutOfBoundsException e) {
								Collections.sort(passList, new Comparator<FrayCard>() {
									public int compare(FrayCard c1, FrayCard c2) {
										return c1.getCardName().compareToIgnoreCase(c2.getCardName());
									}
								});
								FrayCard searchKey1 = new FrayCard(null, fullpassList.get(index1).getCardName(), 0,  0, 0, "", "", 0, "", 0, 0, 0, 0);
								
								int index11 = Collections.binarySearch(passList, searchKey1, new Comparator<FrayCard>() {
									public int compare(FrayCard c1, FrayCard c2) {
										return c1.getCardName().compareToIgnoreCase(c2.getCardName());
									}
								});
								passList.remove(index11);
								btnCardsInHand[cardPosition].setText("Null");
								btnCardsInHand[cardPosition].setVisible(false);// Reset visible of any cards in this btn array.
								
								Collections.shuffle(passList);
							}
							btnCardsInHand[cardPosition].setText("Null");
							btnCardsInHand[cardPosition].setVisible(false);// Reset visible of any cards in this btn array.

							Collections.shuffle(passList);
						} catch(java.lang.ArrayIndexOutOfBoundsException e) {
					
							btnCardsInHand[cardPosition].setText("Null");
							btnCardsInHand[cardPosition].setVisible(false);// Reset visible of any cards in this btn array.
							Collections.shuffle(passList);
							continue;
						}

					}
			
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}

	public Player getPassPlayer() {
		return passPlayer;
	}

	public void setPassPlayer(Player passPlayer) {
		this.passPlayer = passPlayer;
	}

	public JFrame getGameFrame() {
		return gameFrame;
	}

	public void setGameFrame(JFrame gameFrame) {
		this.gameFrame = gameFrame;
	}

	public String getOpponentUsername() {
		return opponentUsername;
	}

	public void setOpponentUsername(String opponentUsername) {
		this.opponentUsername = opponentUsername;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}

	public String getOpponentRace() {
		return opponentRace;
	}

	public void setOpponentRace(String opponentRace) {
		this.opponentRace = opponentRace;
	}

	public int getOpponentHealth() {
		return opponentHealth;
	}

	public void setOpponentHealth(int opponentHealth) {
		this.opponentHealth = opponentHealth;
	}
	 public int getPassIndex() {
		return passIndex;
	}

	public void setPassIndex(int passIndex) {
		this.passIndex = passIndex;
	}

	public int getIndexe() {
		return indexe;
	}

	public void setIndexe(int indexe) {
		this.indexe = indexe;
	}

	public String getCurrentTurnStatus() {
		return currentTurnStatus;
	}

	public void setCurrentTurnStatus(String currentTurnStatus) {
		this.currentTurnStatus = currentTurnStatus;
	}

	public int getManaValue() {
		return manaValue;
	}

	public void setManaValue(int manaValue) {
		this.manaValue = manaValue;
	}

	public int getYourHealth() {
		return yourHealth;
	}

	public void setYourHealth(int yourHealth) {
		this.yourHealth = yourHealth;
	}
}
