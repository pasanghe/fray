package objectClasses;

import javax.swing.ImageIcon;

public class FrayCard{
	
	private ImageIcon image;
	private String cardName;
	private int cardAttackPoints;
	private int cardHealthPoints;
	private int cardArmorPoints;
	private String cardClass;
	private String cardType;
	private int energyCost;
	private String cardState;
	private int spellValueHealth;
	private int spellValueAttack;
	private int spellValueCost;
	private int spellValueArmor;
	
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
