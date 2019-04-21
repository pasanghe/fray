package objectClasses;

import javax.swing.ImageIcon;

/**
 * @author Gagandeep Ghotra, Sang Heon Park, Zain Razvi, Lee fyyfe
 * This is an object class that will be used as a template for cards
 * that will be brought into the game from the database.
 * Fray's cards are a bit different that regular cards games.
 * Fray's cards have a name, attack points, health points, class,
 * type, energy cost, state, health, attack, cost, and armor buffs.
 */
public class FrayCard {
	
	private ImageIcon image; // Image of the card (Will be added into a later version)
	private String cardName; // Card Name
	private int cardAttackPoints; // Card Attack Points
	private int cardHealthPoints; // Card Health Points
	private int cardArmorPoints; // Card Armor Points
	private String cardClass; // Card Class
	private String cardType; // Card Type (Minions, Spell, Legendary) - Will be used to differentiate what type of card it is in game.
	private int energyCost; // Energy Cost of the card when placed
	private String cardState; // State of the card when placed onto the field
	private int spellValueHealth; // Health buffs given by spell cards
	private int spellValueAttack; // Attack buffs given by spell cards
	private int spellValueCost; // Cost buffs given by spell cards
	private int spellValueArmor; // Armor buffs given by spell cards
	
	public FrayCard(ImageIcon image, String cardName, int cardAttackPoints, int cardArmorPoints, int cardHealthPoints, String cardClass, String cardType, int energyCost, String cardState, int spellValueHealth, int spellValueAttack, int spellValueCost, int spellValueArmor) {
		this.image = image;
		this.cardName = cardName;
		this.cardAttackPoints = cardAttackPoints;
		this.cardHealthPoints = cardHealthPoints;
		this.cardClass = cardClass;
		this.cardType = cardType;
		this.energyCost = energyCost;
		this.cardState = cardState;
		this.cardArmorPoints = cardArmorPoints;
		this.spellValueHealth = spellValueHealth;
		this.spellValueAttack = spellValueAttack;
		this.spellValueCost = spellValueCost;
		this.spellValueArmor = spellValueArmor;
	}
    
    public ImageIcon getImage() {
		return image;
	}

	public void setImage(ImageIcon image) {
		this.image = image;
	}

	public String getCardName() {
		return cardName;
	}

	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	public int getCardAttackPoints() {
		return cardAttackPoints;
	}

	public void setCardAttackPoints(int cardAttackPoints) {
		this.cardAttackPoints = cardAttackPoints;
	}

	public int getCardHealthPoints() {
		return cardHealthPoints;
	}

	public void setCardHealthPoints(int cardHealthPoints) {
		this.cardHealthPoints = cardHealthPoints;
	}

	public String getCardClass() {
		return cardClass;
	}

	public void setCardClass(String cardClass) {
		this.cardClass = cardClass;
	}

	public String getCardType() {
		return cardType;
	}

	public void setCardType(String cardType) {
		this.cardType = cardType;
	}

	public int getEnergyCost() {
		return energyCost;
	}

	public void setEnergyCost(int energyCost) {
		this.energyCost = energyCost;
	}

	public String getCardState() {
		return cardState;
	}

	public void setCardState(String cardState) {
		this.cardState = cardState;
	}

	public int getCardArmorPoints() {
		return cardArmorPoints;
	}

	public void setCardArmorPoints(int cardArmorPoints) {
		this.cardArmorPoints = cardArmorPoints;
	}

	public int getSpellValueHealth() {
		return spellValueHealth;
	}

	public void setSpellValueHealth(int spellValueHealth) {
		this.spellValueHealth = spellValueHealth;
	}

	public int getSpellValueAttack() {
		return spellValueAttack;
	}

	public void setSpellValueAttack(int spellValueAttack) {
		this.spellValueAttack = spellValueAttack;
	}

	@Override
	public String toString() {
		return "FrayCard [image=" + image + ", cardName=" + cardName + ", cardAttackPoints=" + cardAttackPoints
				+ ", cardHealthPoints=" + cardHealthPoints + ", cardArmorPoints=" + cardArmorPoints + ", cardClass="
				+ cardClass + ", cardType=" + cardType + ", energyCost=" + energyCost + ", cardState=" + cardState
				+ ", spellValueHealth=" + spellValueHealth + ", spellValueAttack=" + spellValueAttack + "]";
	}

	public int getSpellValueCost() {
		return spellValueCost;
	}

	public void setSpellValueCost(int spellValueCost) {
		this.spellValueCost = spellValueCost;
	}

	public int getSpellValueArmor() {
		return spellValueArmor;
	}

	public void setSpellValueArmor(int spellValueArmor) {
		this.spellValueArmor = spellValueArmor;
	}

}
