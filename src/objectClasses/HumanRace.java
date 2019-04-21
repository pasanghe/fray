package objectClasses;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import jdbc.JDBConnector;
import interfaces.Fray_Race;
/**
 * 
 * @author Gagandeep Ghotra, Sang Heon Park, Zain Razvi, Lee fyyfe
 * This class is a child class that implements the general 
 * Fray_Race interface. The basic abstract methods that 
 * are brought in from the interface are:
 * setRacePassive, setRaceName, setHealthNumber, addRaceCards.
 * Race name: Human
 * Side note, the race cards are loaded from the database.
 */
public class HumanRace implements Fray_Race {
	
	private String raceName = "Human"; // Race Name
	private String racePassive = "";
	private ArrayList <FrayCard> frayHumanCardDeck = new ArrayList<FrayCard>(); // Empty Race Card Deck which will be filled with addRaceCards() method

	@Override
	public void setRacePassive(String racePassive) {
		this.racePassive = racePassive;		
	}

	@Override
	public void setRaceName(String raceName) {
		this.raceName = raceName;
	}
	
	@Override
	public void setHealthNumber(int raceHealthNumber) {}
	
	@Override
	public String toString() {
		return "Race Name: " + raceName + ", Race Passive: " + racePassive;
	}

	@Override
	public ArrayList<FrayCard> addRaceCards() {
		/* This will select all the cards that belong to the Human Race and import them from the database, 'FrayCardTable' into
		 * the frayHumanCardDeck arraylist. This array list will be the one that will be used during the game.
		 */
		try {
			Connection con = JDBConnector.getCon();
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
