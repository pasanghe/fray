/**
 * 
 */
package interfaces;

import java.util.ArrayList;

import ObjectClasses.FrayCard;

/**
 * @author gagan
 *
 */
public interface Fray_Race {
	
	void setRacePassive(String racePassive);
	
	void setRaceName(String raceName);
	
	void setHealthNumber(int raceHealth);
	
	ArrayList<FrayCard> addRaceCards();
	
	@Override
	String toString();
}
