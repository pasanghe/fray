package base.system;

import java.util.ArrayList;
import base.data.*;
import base.system.Player;
import base.system.Card;

public class DeckBuilder {

    private ArrayList frayDeck = new ArrayList();

    public DeckBuilder() {
    }

    public void generateDeck(Player player) {
        if (player.getType() == ObjectType.RACE) {
            frayDeck.add(new Card(RaceMinionList.SWORDSMAN));
            frayDeck.add(new Card(RaceMinionList.BOMBARDIER));
            frayDeck.add(new Card(RaceMinionList.CALVARY));
        } else {
            frayDeck.add(new Card(RaceMinionList.MANTICORE));
            frayDeck.add(new Card(RaceMinionList.PIXIE));
        }
        player.setPlayerDeck(frayDeck);
    }

    public void drawCard(ArrayList<Card> frayDeck, ArrayList<Card> hand) {
        hand.add(frayDeck.get(0));
    }
}
