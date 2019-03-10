/**
 * @author gagan
 * //card interface -> abstract minion / spell -> class swordsman, rifleman, bombardier ... etc
 * //also have to figure out how deck class will be
 */

package interfaces;

import javax.swing.ImageIcon;


public interface Card {
	
	void setImage(ImageIcon image);
	
	void setCardName(String cardName);
	
	void setCardHealthPoints(int cardHealthPoints);
	
	void setCardAttackPoints(int cardAttackPoints);
	
	void setCardArmorPoints(int cardArmorPoints);
	
	void setCardClass(char cardClass);
	
	void setCardType(char cardType);
	
	void setEnergyCost(int energyCost);
	
	void setCardState(char cardState);
	
	@Override
	String toString();
}