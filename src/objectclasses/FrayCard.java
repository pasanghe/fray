package objectclasses;

import javax.swing.ImageIcon;

public class FrayCard{
	
	private ImageIcon image;
	private String cardName;
	private int cardAttackPoints;
	private int cardHealthPoints;
	private int cardArmorPoints;
	private char cardClass;
	private char cardType;
	private int energyCost;
	private char cardState;
	
	public FrayCard(ImageIcon image, String cardName, int cardAttackPoints, int cardArmorPoints, int cardHealthPoints, char cardClass, char cardType, int energyCost, char cardState) {
		this.image = image;
		this.cardName = cardName;
		this.cardAttackPoints = cardAttackPoints;
		this.cardHealthPoints = cardHealthPoints;
		this.cardClass = cardClass;
		this.cardType = cardType;
		this.energyCost = energyCost;
		this.cardState = cardState;
		this.cardArmorPoints = cardArmorPoints;
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

	public char getCardClass() {
		return cardClass;
	}

	public void setCardClass(char cardClass) {
		this.cardClass = cardClass;
	}

	public char getCardType() {
		return cardType;
	}

	public void setCardType(char cardType) {
		this.cardType = cardType;
	}

	public int getEnergyCost() {
		return energyCost;
	}

	public void setEnergyCost(int energyCost) {
		this.energyCost = energyCost;
	}

	public char getCardState() {
		return cardState;
	}

	public void setCardState(char cardState) {
		this.cardState = cardState;
	}

	public int getCardArmorPoints() {
		return cardArmorPoints;
	}

	public void setCardArmorPoints(int cardArmorPoints) {
		this.cardArmorPoints = cardArmorPoints;
	}

	@Override
	public String toString() {
		return "FrayCard [image=" + image + ", cardName=" + cardName + ", cardAttackPoints=" + cardAttackPoints
				+ ", cardHealthPoints=" + cardHealthPoints + ", cardClass=" + cardClass + ", cardType=" + cardType
				+ ", energyCost=" + energyCost + ", cardState=" + cardState + "]";
	}	
}
