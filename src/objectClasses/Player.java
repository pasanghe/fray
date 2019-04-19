/**
 * SYST 17796 Project Winter 2019 Base code.
 * Students can modify and extend to implement their game.
 * Add your name as a modifier and the date!
 */
package objectClasses;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import objectClasses.CardDeck;
import objectClasses.ElfRace;
import objectClasses.HumanRace;

/**
 * A class that models each Player in the game. Players have an identifier, which should be unique.
 * @author dancye, 2018
 */
public class Player//abstract class Player
{
    private String playerID; //the unique ID for this player
    private String playerRaceName; // the unique Race for this player
    public ArrayList <FrayCard> frayCardDeck = new ArrayList<FrayCard>();

    /**
     * A constructor that allows you to set the player's unique ID
     * @param name the unique ID to assign to this player.
     */
    public Player(String name, String raceName) // Used for setting up the race of the player before playing the actual card game
    {
        this.playerID = name;
        
    	if(raceName.equalsIgnoreCase("Elf")) {
			this.playerRaceName = raceName;
			frayCardDeck = CardDeck.generateDeck("Elf");
			
			for(int i = 0; i < frayCardDeck.size(); i++) {
				System.out.println(frayCardDeck.get(i).toString());
			}
		}
		
		else if (raceName.equalsIgnoreCase("Human")) {
			this.playerRaceName = raceName;
			frayCardDeck = CardDeck.generateDeck("Human");
			
			for(int i = 0; i < frayCardDeck.size(); i++) {
				System.out.println(frayCardDeck.get(i).toString());
			}
		}
		
		else {
			JOptionPane.showMessageDialog(null, "Please enter a proper race name! Either 'Elf' or 'Human' will work!");
		}
    }
    
    public ArrayList<FrayCard> getFrayCardDeck() {
		return frayCardDeck;
	}

	public void setFrayCardDeck(ArrayList<FrayCard> frayCardDeck) {
		this.frayCardDeck = frayCardDeck;
	}

	public Player(String name) {// Used for sign up and login
    	playerID = name;
    }

    /**
     * @return the playerID
     */
    public String getPlayerID()
    {
        return playerID;
    }

    /**
     * Ensure that the playerID is unique
     * @param givenID the playerID to set
     */
    public void setPlayerID(String givenID)
    {
        playerID = givenID;
    }
    
	public String getPlayerRaceName() {
		return playerRaceName;
	}

	public void setPlayerRaceName(String playerRaceName) {
		this.playerRaceName = playerRaceName;
	}

    /**
     * The method to be instantiated when you subclass the Player class
     * with your specific type of Player and filled in with logic to play your game.
     */
  //  public abstract void play();

}
