package objectClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.swing.ImageIcon;

import fullGame.FrayCardGame;
import jdbc.JDBConnector;
import interfaces.Fray_Race;

public class HumanRace implements Fray_Race{
	
	private String raceName = "Human";
	private String racePassive = "";
	private int raceHealthNumber = 10;
	private ArrayList <FrayCard> frayHumanCardDeck = new ArrayList<FrayCard>();

	@Override
	public void setRacePassive(String racePassive) {
		this.racePassive = racePassive;		
	}

	@Override
	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}
	
	@Override
	public void setHealthNumber(int raceHealthNumber) {
		this.raceHealthNumber = raceHealthNumber;
	}
	
	@Override
	public String toString() {
		return "Race Name: " + raceName + ", Race Passive: " + racePassive;
	}

	@Override
	public ArrayList<FrayCard> addRaceCards() {
		try {
			Connection con = JDBConnector.getCon(); // This will select all the cards with the class Human and import them from the database, 'FrayCardTable'.
			String sql = "SELECT CardName, CardAttackPoints, CardHealthPoints, CardArmorPoints, CardType, EnergyCost, CardState, SpellValueHealth, SpellValueAttack, SpellValueCost, SpellValueArmor FROM sql3282320.FrayCardTable WHERE CardRace = 'Human';";
			PreparedStatement ps = con.prepareStatement(sql);
			ResultSet myRs = ps.executeQuery();
			
			while(myRs.next()) {
				String cardName = myRs.getString("CardName");
				int cardAttackPoints = myRs.getInt("CardAttackPoints");
				int cardHealthPoints = myRs.getInt("CardHealthPoints");
				int cardArmorPoints = myRs.getInt("CardArmorPoints");
				String cardType = myRs.getString("CardType");
				int energyCost = myRs.getInt("EnergyCost");
				String cardState = myRs.getString("CardState");
				int spellValueHealth = myRs.getInt("SpellValueHealth");
				int spellValueAttack = myRs.getInt("SpellValueAttack");
				int spellValueCost = myRs.getInt("SpellValueCost");
				int spellValueArmor = myRs.getInt("SpellValueArmor");
				
				frayHumanCardDeck.add(new FrayCard(null, cardName, cardAttackPoints, cardArmorPoints, cardHealthPoints, "", cardType, energyCost, cardState, spellValueHealth, spellValueAttack, spellValueCost, spellValueArmor));
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return frayHumanCardDeck;
	}

}
