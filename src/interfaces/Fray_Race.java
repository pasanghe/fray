/**
 * 
 */
package interfaces;

import java.util.ArrayList;

import objectClasses.FrayCard;

/**
 * @author Gagandeep Ghotra, Sang Heon Park, Zain Razvi, Lee fyyfe
 * Having Fray_Race as an abstract interface means that we can
 * quickly and easily add/create new races into the FrayGame.
 */
public interface Fray_Race {
	
	void setRacePassive(String racePassive); // Setting specific race passive
	
	void setRaceName(String raceName); // Setting specific race name
	
	void setHealthNumber(int raceHealth); // Setting race health
	
	ArrayList<FrayCard> addRaceCards(); // Create a method that will import race specific cards from the database
	
	@Override
	String toString(); // To string method for testing during development
}
