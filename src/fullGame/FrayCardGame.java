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

public class FrayCardGame{

	private JPanel contentPane;
	private JFrame gameFrame;
	private ArrayList<FrayCard> passList;
	private ArrayList<FrayCard> fullpassList;
	private String arrayOfCardsOnHand[] = new String[5];
	private JButton[] btnCardsInHand = new JButton[5];
	private JButton[] yourFieldCards = new JButton[6];
	private JButton[] enemyFieldCards = new JButton[6];
	private JButton btnEndTurn = new JButton("END TURN");
	private JLabel lblturnstatus = new JLabel("*TurnStatus*"), lblglobaltimer = new JLabel("*GlobalTimer*");
	private Player passPlayer;
	private Timer timerObjUpdatingGame;	
	private String Username;
	private String opponentUsername;
	private int opponentHealth;
	private String opponentRace;
	private int i;
	private int passIndex = 99;
	private int indexe;
	private String currentTurnStatus;
	private int cardsInHandx = 304, cardsInHandy = 623, cardsInHandwidth = 119, cardsInHandheight = 148;
	private int yourFieldCardsHandx = 162, yourFieldCardsHandy = 464, yourFieldCardswidth = 119, yourFieldCardsheight = 148;
	private int enemyFieldCardsHandx = 160, enemyFieldCardsHandy = 159, enemyFieldCardswidth = 119, enemyFieldCardsheight = 148;
	/**
	 * Create the frame.
	 */
	@SuppressWarnings("unchecked")
	public FrayCardGame(Player passPlayer) {
		setPassPlayer(passPlayer);
		setUsername(passPlayer.getPlayerID());
		
		JLabel lblenemyhealth = new JLabel();
		JLabel cardDeckFractionLabel = new JLabel();
		
		passList = (ArrayList<FrayCard>) passPlayer.getFrayCardDeck().clone();
		fullpassList = (ArrayList<FrayCard>) passList.clone();
		
		for(int i = 0; i < arrayOfCardsOnHand.length; i++) {
			if(i == 0) {
				arrayOfCardsOnHand[i] = passList.get(i).getCardName();
				System.out.println("reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee " + passList.get(i));
				System.out.println("Going to remove:" + passList.get(i).getCardName());
				passList.remove(i);
			}
		
			if(i > 0) {
				arrayOfCardsOnHand[i] = passList.get(i-1).getCardName();
				System.out.println("reeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee " + passList.get(i-1));
				System.out.println("Going to remove:" + passList.get(i-1).getCardName());
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
				playerSetFirstTurnStatus();
				getCurrentPlayerTurnStatus();
				getCurrentPlayerTurnStatusPlayer2();
				resetTurnCountDownTimer();
				lblenemyhealth.setText(String.valueOf(getOpponentHealth()));
				cardDeckFractionLabel.setText(passList.size() + "/" + passPlayer.getFrayCardDeck().size());
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
		
		JLabel lblenemyusername = new JLabel();
		lblenemyusername.setText(getOpponentUsername());
		panel.add(lblenemyusername);
		panel.add(lblenemyhealth);
		if(passPlayer.getPlayerRaceName().equalsIgnoreCase("Elf")) {
			JLabel lblenemyraceimage = new JLabel(new ImageIcon(FrayCardGame.class.getResource("../images/ElfGeneral2.jpg")));
			panel.add(lblenemyraceimage);
		}	 
		else if (passPlayer.getPlayerRaceName().equalsIgnoreCase("Human")) {
			JLabel lblenemyraceimage = new JLabel(new ImageIcon(FrayCardGame.class.getResource("../images/HumanGeneral2.png")));
			panel.add(lblenemyraceimage);
		}
		
		cardDeckFractionLabel.setFont(new Font("Tahoma", Font.PLAIN, 20));
		cardDeckFractionLabel.setBounds(160, 651, 122, 94);
		contentPane.add(cardDeckFractionLabel);
		
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
			public void actionPerformed(ActionEvent e) { // Maybe check what state the game is now in and then run the proper methods.
					endTurn();
					endTurnPlayer2();
					
					getCurrentPlayerTurnStatus();
					getCurrentPlayerTurnStatusPlayer2();
			}
		});

		lblturnstatus.setFont(new Font("Tahoma", Font.PLAIN, 17));
		lblturnstatus.setBounds(1022, 723, 144, 39);
		contentPane.add(lblturnstatus);
		
		JLabel lblyourhealth = new JLabel("*YourHealth*");//////////////////////////////////
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
		
		btnCardsInHand[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				setPassIndex(0);
			}
		});
	
		btnCardsInHand[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				setPassIndex(1);
			}
		});
		
		btnCardsInHand[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				setPassIndex(2);
			}
		});
		
		btnCardsInHand[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				setPassIndex(3);
			}
		});
		
		btnCardsInHand[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) { 
				setPassIndex(4);
			}
		});
		
		yourFieldCards[0].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 99) {JOptionPane.showMessageDialog(null, "Please chose a card to put onto the field first!");}
				else {
					Collections.sort(fullpassList, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					FrayCard searchKey = new FrayCard(null, arrayOfCardsOnHand[getPassIndex()], 0,  0, 0, "", "", 0, "", 0, 0, 0, 0);
					
					int index1 = Collections.binarySearch(fullpassList, searchKey, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					JOptionPane.showMessageDialog(null, "Found in PassList: " + fullpassList.get(index1).getCardName());
					System.out.println(getPassIndex());
					yourFieldCards[0].setText(arrayOfCardsOnHand[getPassIndex()]);
					putCardOnToField(getPassIndex(), 1); // CardPosition in arrayList, card field button position
					putCardOnToFieldPlayer2(getPassIndex(), 1);
					
					arrayOfCardsOnHand[getPassIndex()] = "Null";
				}
			}
		});
		
		yourFieldCards[1].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 99) {JOptionPane.showMessageDialog(null, "Please chose a card to put onto the field first!");}
				else {
					
					Collections.sort(fullpassList, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					FrayCard searchKey = new FrayCard(null, arrayOfCardsOnHand[getPassIndex()], 0,  0, 0, "", "", 0, "", 0, 0, 0, 0);
					
					int index1 = Collections.binarySearch(fullpassList, searchKey, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					JOptionPane.showMessageDialog(null, "Found in PassList: " + fullpassList.get(index1).getCardName());
					System.out.println(getPassIndex());
					yourFieldCards[1].setText(arrayOfCardsOnHand[getPassIndex()]);
					putCardOnToField(getPassIndex(), 2); // CardPosition in arrayList, card field button position
					putCardOnToFieldPlayer2(getPassIndex(), 2);
					
					arrayOfCardsOnHand[getPassIndex()] = "Null";
				}
			}
		});
		
		yourFieldCards[2].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 99) {JOptionPane.showMessageDialog(null, "Please chose a card to put onto the field first!");}
				else {
					//yourFieldCard1.setText(passList.get(passIndex).getCardName());
					
					
					Collections.sort(fullpassList, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					FrayCard searchKey = new FrayCard(null, arrayOfCardsOnHand[getPassIndex()], 0,  0, 0, "", "", 0, "", 0, 0, 0, 0);
					
					int index1 = Collections.binarySearch(fullpassList, searchKey, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					JOptionPane.showMessageDialog(null, "Found in PassList: " + fullpassList.get(index1).getCardName());
					System.out.println(getPassIndex());
					yourFieldCards[2].setText(arrayOfCardsOnHand[getPassIndex()]);
					putCardOnToField(getPassIndex(), 3); // CardPosition in arrayList, card field button position
					putCardOnToFieldPlayer2(getPassIndex(), 3);
					
					arrayOfCardsOnHand[getPassIndex()] = "Null";
				}
			}
		});
		
		yourFieldCards[3].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 99) {JOptionPane.showMessageDialog(null, "Please chose a card to put onto the field first!");}
				else {
					//yourFieldCard1.setText(passList.get(passIndex).getCardName());
					
					
					Collections.sort(fullpassList, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					FrayCard searchKey = new FrayCard(null, arrayOfCardsOnHand[getPassIndex()], 0,  0, 0, "", "", 0, "", 0, 0, 0, 0);
					
					int index1 = Collections.binarySearch(fullpassList, searchKey, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					JOptionPane.showMessageDialog(null, "Found in PassList: " + fullpassList.get(index1).getCardName());
					System.out.println(getPassIndex());
					yourFieldCards[3].setText(arrayOfCardsOnHand[getPassIndex()]);
					putCardOnToField(getPassIndex(), 4); // CardPosition in arrayList, card field button position
					putCardOnToFieldPlayer2(getPassIndex(), 4);
					
					arrayOfCardsOnHand[getPassIndex()] = "Null";
				}
			}
		});
		
		yourFieldCards[4].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 99) {JOptionPane.showMessageDialog(null, "Please chose a card to put onto the field first!");}
				else {
					//yourFieldCard1.setText(passList.get(passIndex).getCardName());
					
					
					Collections.sort(fullpassList, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					FrayCard searchKey = new FrayCard(null, arrayOfCardsOnHand[getPassIndex()], 0,  0, 0, "", "", 0, "", 0, 0, 0, 0);
					
					int index1 = Collections.binarySearch(fullpassList, searchKey, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					JOptionPane.showMessageDialog(null, "Found in PassList: " + fullpassList.get(index1).getCardName());
					System.out.println(getPassIndex());
					yourFieldCards[4].setText(arrayOfCardsOnHand[getPassIndex()]);
					putCardOnToField(getPassIndex(), 5); // CardPosition in arrayList, card field button position
					putCardOnToFieldPlayer2(getPassIndex(), 5);
					
					arrayOfCardsOnHand[getPassIndex()] = "Null";
				}
			}
		});
		
		yourFieldCards[5].addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(passIndex == 99) {JOptionPane.showMessageDialog(null, "Please chose a card to put onto the field first!");}
				else {
					//yourFieldCard1.setText(passList.get(passIndex).getCardName());
					
					
					Collections.sort(fullpassList, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					FrayCard searchKey = new FrayCard(null, arrayOfCardsOnHand[getPassIndex()], 0,  0, 0, "", "", 0, "", 0, 0, 0, 0);
					
					int index1 = Collections.binarySearch(fullpassList, searchKey, new Comparator<FrayCard>() {
						public int compare(FrayCard c1, FrayCard c2) {
							return c1.getCardName().compareToIgnoreCase(c2.getCardName());
						}
					});
					
					JOptionPane.showMessageDialog(null, "Found in PassList: " + fullpassList.get(index1).getCardName());
					System.out.println(getPassIndex());
					yourFieldCards[5].setText(arrayOfCardsOnHand[getPassIndex()]);
					putCardOnToField(getPassIndex(), 6); // CardPosition in arrayList, card field button position
					putCardOnToFieldPlayer2(getPassIndex(), 6);
					
					arrayOfCardsOnHand[getPassIndex()] = "Null";
				}
			}
		});
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
						System.out.println(getCurrentTurnStatus());
						
						if(getCurrentTurnStatus().equalsIgnoreCase("Turn")) {
							lblturnstatus.setText("Your Turn!");
							System.out.println("YourUsername + EnemyUsername and its your turn" + getCurrentTurnStatus());
//							enemyFieldCard1.setEnabled(false);
//							enemyFieldCard2.setEnabled(false);
//							enemyFieldCard3.setEnabled(false);
//							enemyFieldCard4.setEnabled(false);
//							enemyFieldCard5.setEnabled(false);
//							enemyFieldCard6.setEnabled(false);
							yourFieldCards[0].setEnabled(true);
							yourFieldCards[1].setEnabled(true);
							yourFieldCards[2].setEnabled(true);
							yourFieldCards[3].setEnabled(true);
							yourFieldCards[4].setEnabled(true);
							yourFieldCards[5].setEnabled(true);
							btnEndTurn.setEnabled(true); // Your end turn button
							//countdownTimer.schedule(new TimerTask() {int second = 60; @Override public void run() {lblglobaltimer.setText(String.valueOf(second--));}},0, 1000);	
							//endTurn();////////////////////////////////////////////////////////////
							turnCountdownTimer();
						}
						
						else if(getCurrentTurnStatus().equalsIgnoreCase("NotTurn")) {
							lblturnstatus.setText("Enemy's Turn!");
							System.out.println("YourUsername + EnemyUsername and its not your turn" + getCurrentTurnStatus());
							yourFieldCards[0].setEnabled(false);
							yourFieldCards[1].setEnabled(false);
							yourFieldCards[2].setEnabled(false);
							yourFieldCards[3].setEnabled(false);
							yourFieldCards[4].setEnabled(false);
							yourFieldCards[5].setEnabled(false);
							btnEndTurn.setEnabled(false); // Your end turn button
							//endTurn();////////////////////////////////////////////////////////////
							//countdownTimer.schedule(new TimerTask() {int second = 60; @Override public void run() {lblglobaltimer.setText(String.valueOf(second--));}},0, 1000);
							turnCountdownTimer();
						}
					}
					} catch (java.sql.SQLSyntaxErrorException e) {
						e.printStackTrace();
					}

			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(getCurrentTurnStatus() + "//");
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
						System.out.println(getCurrentTurnStatus());

						if(getCurrentTurnStatus().equalsIgnoreCase("Turn")) {
							lblturnstatus.setText("Your Turn!");
							System.out.println("EnemyUsername + YourUsername and its your Turn" + getCurrentTurnStatus());
//							enemyFieldCard1.setEnabled(false);
//							enemyFieldCard2.setEnabled(false);
//							enemyFieldCard3.setEnabled(false);
//							enemyFieldCard4.setEnabled(false);
//							enemyFieldCard5.setEnabled(false);
//							enemyFieldCard6.setEnabled(false);
							yourFieldCards[0].setEnabled(true);
							yourFieldCards[1].setEnabled(true);
							yourFieldCards[2].setEnabled(true);
							yourFieldCards[3].setEnabled(true);
							yourFieldCards[4].setEnabled(true);
							yourFieldCards[5].setEnabled(true);
							btnEndTurn.setEnabled(true); // Enemy's End turn Button
							//endTurnPlayer2();////////////////////////////////////////////////////////////
							//countdownTimer.schedule(new TimerTask() {int second = 60; @Override public void run() {lblglobaltimer.setText(String.valueOf(second--));}},0, 1000);
							turnCountdownTimerPlayer2();
						}
		
						else if(getCurrentTurnStatus().equalsIgnoreCase("NotTurn")) {
							lblturnstatus.setText("Enemy's Turn!");
							System.out.println("EnemyUsername + YourUsername and its not your Turn" + getCurrentTurnStatus());
							yourFieldCards[0].setEnabled(false);
							yourFieldCards[1].setEnabled(false);
							yourFieldCards[2].setEnabled(false);
							yourFieldCards[3].setEnabled(false);
							yourFieldCards[4].setEnabled(false);
							yourFieldCards[5].setEnabled(false);
							btnEndTurn.setEnabled(false); // Enemy's End turn Button
							//endTurnPlayer2();////////////////////////////////////////////////////////////
							//countdownTimer.schedule(new TimerTask() {int second = 60; @Override public void run() {lblglobaltimer.setText(String.valueOf(second--));}},0, 1000);
							turnCountdownTimerPlayer2();
						}
					}
			}
		} catch(SQLException e) {
			e.printStackTrace();
		}
		System.out.println(getCurrentTurnStatus() + "////");
	}
	
	void turnCountdownTimer() {
		try {
				Connection con = JDBConnector.getCon();
				PreparedStatement ps;
				String sql = "SELECT SentToPlayerUsername FROM sql3282320." + getUsername() + "_SentGameInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
				ps = con.prepareStatement(sql);
				ResultSet myRs = ps.executeQuery();
				
				while(myRs.next()) {
					setOpponentUsername(myRs.getString("SentToPlayerUsername"));
					PreparedStatement ps1;
					String sql1 = "SELECT TurnTimer FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE CardPositionInArrayList = '0' AND PlayerName = '" + getUsername() + "';";
					ps1 = con.prepareStatement(sql1);
					ResultSet myRs1 = ps1.executeQuery();
					while(myRs1.next()) {
						int timeValue = myRs1.getInt("TurnTimer");
						lblglobaltimer.setText(String.valueOf(timeValue));
						timeValue = timeValue - 1; //FrayGame.lblglobaltimer.setText(String.valueOf(second--));
					
						Statement myStat = con.createStatement();
						//UPDATE sql3282320.UserDataTable SET InGame = 'No' WHERE Username = '" + opponentUsername + "';
						String sql2 = "UPDATE sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame SET TurnTimer = '" + timeValue + "' WHERE CardPositionInArrayList = '0' AND PlayerName = '" + getUsername() + "';";
						myStat.executeUpdate(sql2);	
						
						PreparedStatement ps2;
						String sql3 = "SELECT TurnTimer FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE CardPositionInArrayList = '0' AND PlayerName = '" + getUsername() + "';";
						ps2 = con.prepareStatement(sql3);
						ResultSet myRs2 = ps2.executeQuery();
						while(myRs2.next()) {
							int timeValue1 = myRs2.getInt("TurnTimer");
							System.out.println(timeValue1);
							lblglobaltimer.setText(String.valueOf(timeValue1));
							if(timeValue1 == 0) {endTurn();System.out.println("Timer ENDED1");}
						}	
					}
				}
		} catch (SQLException e) {e.printStackTrace();}	
	}
	
	void turnCountdownTimerPlayer2() {	
		try {
			Connection con = JDBConnector.getCon();
			PreparedStatement ps;
			String sql = "SELECT SenderUsername FROM sql3282320." + getUsername() + "_ReceivedGamedInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
			while(myRs.next()) {
				setOpponentUsername(myRs.getString("SenderUsername"));
				PreparedStatement ps1;
				String sql1 = "SELECT TurnTimer FROM sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame WHERE CardPositionInArrayList = '0' AND PlayerName = '" + getOpponentUsername() + "';";
				ps1 = con.prepareStatement(sql1);
				ResultSet myRs1 = ps1.executeQuery();
				while(myRs1.next()) {
					int timeValue = myRs1.getInt("TurnTimer");
					System.out.println(timeValue);
					lblglobaltimer.setText(String.valueOf(timeValue));
					if(timeValue == 0) {endTurnPlayer2();System.out.println("Timer ENDED2");}
				}		
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	void resetTurnCountDownTimer() {
		try {// Player 1
			Connection con = JDBConnector.getCon();
			
			PreparedStatement ps;
			String sql = "SELECT SentToPlayerUsername FROM sql3282320." + getUsername() + "_SentGameInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
			ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
			while(myRs.next()) {
				setOpponentUsername(myRs.getString("SentToPlayerUsername"));
				
				PreparedStatement ps1;
				String sql1 = "SELECT TurnTimer FROM sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame WHERE CardPositionInArrayList = '0' AND PlayerName = '" + getUsername() + "';";
				ps1 = con.prepareStatement(sql1);
				ResultSet myRs1 = ps1.executeQuery();
				
				while(myRs1.next()) {
					int turnTimer = myRs1.getInt("TurnTimer");
					
					if (turnTimer <= 0) {
						Statement myStat = con.createStatement();
						String sql2 = "UPDATE sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame SET TurnTimer = '60' WHERE PlayerName = '" + getUsername() + "' AND CardPositionInArrayList = '0';";
						myStat.executeUpdate(sql2);
						System.out.println("Timer RESET1");
					}
				}
			}
			
		} catch (java.sql.SQLSyntaxErrorException e) {// Player 2
	
			try {
				e.printStackTrace();
				Connection con = JDBConnector.getCon();
				PreparedStatement ps3;
				String sql3 = "SELECT SenderUsername FROM sql3282320." + getUsername() + "ReceivedGamedInvitesTable WHERE SecondGameInviteStatus = 'Accepted'";
				ps3 = con.prepareStatement(sql3);
				ResultSet myRs2 = ps3.executeQuery();
				
				while(myRs2.next()) {
					setOpponentUsername(myRs2.getString("SenderUsername"));
					
					PreparedStatement ps4;
					String sql4 = "SELECT TurnTimer FROM sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame WHERE CardPositionInArrayList = '0' AND PlayerName = '" + getOpponentUsername() + "';";
					ps4 = con.prepareStatement(sql4);
					ResultSet myRs3 = ps4.executeQuery();
					
					while(myRs3.next()) {
						int turnTimer = myRs3.getInt("TurnTimer");
						
						if(turnTimer <= 0) {
							Statement myStat2 = con.createStatement();
							String sql5 = "UPDATE sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame SET TurnTimer = '60' WHERE PlayerName = '" + getUsername() + "' AND CardPositionInArrayList = '0';";
							myStat2.executeUpdate(sql5);
							System.out.println("Timer RESET2");
						}
					}
					
				}
				
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		} catch (SQLException e) {
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
						System.out.println(random);
					
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
															System.out.print("Opponent1");
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
															System.out.print("Opponent2");
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
					System.out.println("End Turn Username" + opponentUsername);
					
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
							
							drawCardAtTheEndOfTurn();
						}
						
						else if (turnStatus.equals("NotTurn")) {
							Statement myStat = con.createStatement();
							String sql1 = "UPDATE sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame SET PlayerTurnStatus = 'Turn' WHERE PlayerName = '" + getUsername() + "' AND CardPositionInArrayList = '0';";
							myStat.executeUpdate(sql1);
				
							Statement myStat2 = con.createStatement();
							String sql2 = "UPDATE sql3282320." + getUsername() + "_VS_" + getOpponentUsername() + "_FrayCardGame SET PlayerTurnStatus = 'NotTurn' WHERE PlayerName = '" + getOpponentUsername() + "' AND CardPositionInArrayList = '10';";
							myStat2.executeUpdate(sql2);
							
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
							
							drawCardAtTheEndOfTurn();
						}
						
						else if (turnStatus.equals("NotTurn")) {
							Statement myStat = con.createStatement();
							String sql1 = "UPDATE sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame SET PlayerTurnStatus = 'NotTurn' WHERE PlayerName = '" + getOpponentUsername() + "' AND CardPositionInArrayList = '0';";
							myStat.executeUpdate(sql1);
				
							Statement myStat2 = con.createStatement();
							String sql2 = "UPDATE sql3282320." + getOpponentUsername() + "_VS_" + getUsername() + "_FrayCardGame SET PlayerTurnStatus = 'Turn' WHERE PlayerName = '" + getUsername() + "' AND CardPositionInArrayList = '10';";
							myStat2.executeUpdate(sql2);
							
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
				for(int g = 0; g < 5; g++) {
	///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					if(arrayOfCardsOnHand[g].equals("Null")) {
						indexe = g;
						//JOptionPane.showMessageDialog(null, indexe);
					} 
				}	
				// Look in arrayOfCardsOnHand and see if the cards are the same as the ones in the list, than dont set.
			
				arrayOfCardsOnHand[indexe] = passList.get(indexe).getCardName();
				JOptionPane.showMessageDialog(null,passList.get(indexe).getCardName());
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
				
				System.out.println("PUTCARDONFIELD OPPONENT USERNAME: " + opponentUsername);
				
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
					
					JOptionPane.showMessageDialog(null, "Found in PassList: " + fullpassList.get(index1).getCardName());
					
					setOpponentUsername(myRs.getString("SentToPlayerUsername"));
					System.out.println("PUTCARDONFIELD OPPONENT USERNAME: " + opponentUsername);
					Statement myStat = con.createStatement();
					JOptionPane.showMessageDialog(null, fullpassList.get(index1).toString());
					String sql1 = "UPDATE sql3282320." + getUsername() + "_VS_" + opponentUsername + "_FrayCardGame SET " +
					"CardImage = '" + String.valueOf(fullpassList.get(index1).getImage()) + "', CardName = '" + String.valueOf(fullpassList.get(index1).getCardName()) 
					+ "', CardAttackPoints = '" + fullpassList.get(index1).getCardAttackPoints() + "', CardHealthPoints = '" + fullpassList.get(index1).getCardHealthPoints()
					+ "', CardArmorPoints = '" + fullpassList.get(index1).getCardArmorPoints() + "', CardClass = '" + fullpassList.get(index1).getCardClass() 
					+ "', CardType = '" + String.valueOf(fullpassList.get(index1).getCardType()) + "', EnergyCost = '" + fullpassList.get(index1).getEnergyCost()
					+ "', CardState = '" + String.valueOf(fullpassList.get(index1).getCardState()) + "', CardSpellValueHealth = '" + fullpassList.get(index1).getSpellValueHealth()
					+ "', CardSpellValueAttack = '" + fullpassList.get(index1).getSpellValueAttack() + "'" + "WHERE CardPositionInArrayList = '" + index + "' AND PlayerName = '" + getUsername() + "';";
					myStat.executeUpdate(sql1);
					
				//} catch (SQLException e) {}
					//JOptionPane.showMessageDialog(null, "Removed: " + fullpassList.get(index1));//////////////////
					//passList.remove(index1);/////////////////////////////////////////////////////////////////
					
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
						JOptionPane.showMessageDialog(null, "Removed: " + passList.get(index2));
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
						for (int i = 0; i < btnCardsInHand.length; i++) {
							System.out.println("Status of cards in hand: " + btnCardsInHand[i].getText());///////////////////
							
						}
					}
						btnCardsInHand[cardPosition].setText("Null");
						System.out.println("CARDPOSITION:" + cardPosition);
						btnCardsInHand[cardPosition].setVisible(false);// Reset visible of any cards in this btn array.
						for (int i = 0; i < btnCardsInHand.length; i++) {
							System.out.println("Status of cards in hand: " + btnCardsInHand[i].getText());///////////////////
						
						}
					} catch(java.lang.ArrayIndexOutOfBoundsException e) {
						btnCardsInHand[cardPosition].setText("Null");
						System.out.println("CARDPOSITION:" + cardPosition);
						btnCardsInHand[cardPosition].setVisible(false);// Reset visible of any cards in this btn array.
						for (int i = 0; i < btnCardsInHand.length; i++) {
							System.out.println("Status of cards in hand: " + btnCardsInHand[i].getText());///////////////////
						
						}
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
					JOptionPane.showMessageDialog(null, "THIS IS PLAYER 2");
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
					
					//JOptionPane.showMessageDialog(null, "Found in PassList: " + passList.get(index1).getCardName());
					System.out.println("PUTCARDONFIELD OPPONENT USERNAME: " + opponentUsername);
					Statement myStat = con.createStatement();
					JOptionPane.showMessageDialog(null, fullpassList.get(index1).toString());
					String sql1 = "UPDATE sql3282320." + opponentUsername + "_VS_" + getUsername() + "_FrayCardGame SET " +
							"CardImage = '" + String.valueOf(fullpassList.get(index1).getImage()) + "', CardName = '" + String.valueOf(fullpassList.get(index1).getCardName()) 
							+ "', CardAttackPoints = '" + fullpassList.get(index1).getCardAttackPoints() + "', CardHealthPoints = '" + fullpassList.get(index1).getCardHealthPoints()
							+ "', CardArmorPoints = '" + fullpassList.get(index1).getCardArmorPoints() + "', CardClass = '" + fullpassList.get(index1).getCardClass() 
							+ "', CardType = '" + String.valueOf(fullpassList.get(index1).getCardType()) + "', EnergyCost = '" + fullpassList.get(index1).getEnergyCost()
							+ "', CardState = '" + String.valueOf(fullpassList.get(index1).getCardState()) + "', CardSpellValueHealth = '" + fullpassList.get(index1).getSpellValueHealth()
							+ "', CardSpellValueAttack = '" + fullpassList.get(index1).getSpellValueAttack() + "'" + "WHERE CardPositionInArrayList = '" + index + "' AND PlayerName = '" + getUsername() + "';";
							myStat.executeUpdate(sql1);				
						try {
							//JOptionPane.showMessageDialog(null, "Removed: " + passList.get(index1));//////////////////////////////////////
							try {
								passList.remove(index1);//////////////////////////////////////////////////////////////////////////
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
								for (int i = 0; i < btnCardsInHand.length; i++) {
									System.out.println("Status of cards in hand: " + btnCardsInHand[i].getText());///////////////////
									
								}
							}
							btnCardsInHand[cardPosition].setText("Null");
							btnCardsInHand[cardPosition].setVisible(false);// Reset visible of any cards in this btn array.
							for (int i = 0; i < btnCardsInHand.length; i++) {
								System.out.println("Status of cards in hand: " + btnCardsInHand[i].getText());///////////////////
								
							}
						} catch(java.lang.ArrayIndexOutOfBoundsException e) {
							JOptionPane.showMessageDialog(null, "Removed");
							btnCardsInHand[cardPosition].setText("Null");
							btnCardsInHand[cardPosition].setVisible(false);// Reset visible of any cards in this btn array.
							for (int i = 0; i < btnCardsInHand.length; i++) {
								System.out.println("Status of cards in hand: " + btnCardsInHand[i].getText());///////////////////
								
							}
							continue;
						}
							//for (int i = 0; i < btnCardsInHand.length; i++) {
							//	System.out.println("Status of cards in deck: " + passList.get(i));//////////////////////////
							//}
							//importCardsIntoYourHands(pass); 
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
}
