package ObjectClasses;

import java.util.ArrayList;

public class CardDeck {
	
	private static int cardDeckSize = 13;
	public static ArrayList <FrayCard> frayCardDeck = new ArrayList<FrayCard>(cardDeckSize);
	
	public static ArrayList<FrayCard> generateDeck(String raceName) {
		
		// card image (ignore and keep it at null for now), card name, cardAP, cardArmorPoints,
				// cardHP, cardClass, cardType, energyCost, cardState, spellValueHealth, spellValueAttack.
		// The last to properties are for spell card buffs.
		// You can add the classless cards here
		
		frayCardDeck.add(new FrayCard(null, "Manticore3232", 2,  0, 5, ' ', ' ', 0, ' ', 0, 0));
		frayCardDeck.add(new FrayCard(null, "Pixie3232", 2, 0, 2, ' ', ' ', 0, ' ', 0, 0));
		
		if(raceName.equalsIgnoreCase("Human")) {
			HumanRace newHumanRace = new HumanRace();
			frayCardDeck.addAll(newHumanRace.addRaceCards());
		}
		
		else if(raceName.equalsIgnoreCase("Elf")) {
			ElfRace newElfRace = new ElfRace();
			frayCardDeck.addAll(newElfRace.addRaceCards());
		}
		
		return frayCardDeck;
	}
	
}
