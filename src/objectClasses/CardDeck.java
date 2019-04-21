package objectClasses;

import java.util.ArrayList;

public class CardDeck {
	
	public static ArrayList <FrayCard> frayCardDeck = new ArrayList<FrayCard>();
	
	public static ArrayList<FrayCard> getFrayCardDeck() {
		return frayCardDeck;
	}

	public static void setFrayCardDeck(ArrayList<FrayCard> frayCardDeck) {
		CardDeck.frayCardDeck = frayCardDeck;
	}

	public static ArrayList<FrayCard> generateDeck(String raceName) {
		
		// card image (ignore and keep it at null for now), card name, cardAP, cardArmorPoints,
				// cardHP, cardClass, cardType, energyCost, cardState, spellValueHealth, spellValueAttack.
		// The last to properties are for spell card buffs.

		if(raceName.equalsIgnoreCase("Human")) {
			HumanRace newHumanRace = new HumanRace();
			frayCardDeck.addAll(newHumanRace.addRaceCards());
			for(int i = 0; i < frayCardDeck.size(); i++) {frayCardDeck.get(i).toString();}
		}
		
		else if(raceName.equalsIgnoreCase("Elf")) {
			ElfRace newElfRace = new ElfRace();
			frayCardDeck.addAll(newElfRace.addRaceCards());
			for(int i = 0; i < frayCardDeck.size(); i++) {frayCardDeck.get(i).toString();}
		}
		
		return frayCardDeck;
	}
	
}
