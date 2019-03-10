/**
 * 
 */
package cards.Human;

import javax.swing.ImageIcon;

import objectclasses.MinionCard;

/**
 * @author gagan
 *
 */
public class Bombardier extends MinionCard {
	
	private ImageIcon image;
	private String cardName = "Bomderdier";
	private int cardAttackPoints = 1;
	private int cardHealthPoints = 1;
	private int cardArmorPoints = 0;
	private char cardClass = 'H'; // H = Human
	private char cardType = 'R'; // R = Regular
	private int energyCost = 1;
	private char cardState;
	
	// The user cannot set the card class and the card energy cost.
	
	@Override
	public void setImage(ImageIcon image) {
		this.image = image;
	}

	@Override
	public void setCardName(String cardName) {
		this.cardName = cardName;
	}

	@Override
	public void setCardHealthPoints(int cardHealthPoints) {
		this.cardHealthPoints = cardHealthPoints;
	}

	@Override
	public void setCardAttackPoints(int cardAttackPoints) {
		this.cardAttackPoints = cardAttackPoints;
	}

	@Override
	public void setCardArmorPoints(int cardArmorPoints) {
		this.cardArmorPoints = cardArmorPoints;
	}

	@Override
	public void setCardType(char cardType) {
		this.cardType = cardType;
	}

	@Override
	public void setCardState(char cardState) {
		this.cardState = cardState;
	}

	public ImageIcon getImage() {
		return image;
	}

	public String getCardName() {
		return cardName;
	}

	public int getCardAttackPoints() {
		return cardAttackPoints;
	}

	public int getCardHealthPoints() {
		return cardHealthPoints;
	}

	public int getCardArmorPoints() {
		return cardArmorPoints;
	}

	public char getCardType() {
		return cardType;
	}

	public char getCardState() {
		return cardState;
	}

	@Override
	public String toString() {
		return "Bombardier [image=" + image + ", cardName=" + cardName + ", cardAttackPoints=" + cardAttackPoints
				+ ", cardHealthPoints=" + cardHealthPoints + ", cardArmorPoints=" + cardArmorPoints + ", cardClass="
				+ cardClass + ", cardType=" + cardType + ", energyCost=" + energyCost + ", cardState=" + cardState
				+ "]";
	}

}