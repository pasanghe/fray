package testing;

import java.util.ArrayList;
import static org.junit.Assert.*;
import org.junit.Test;

class FrayTest {

    @Test
    void testPlayerNameGood() {
        String testPlayerUsername = "Name1";
        base.system.Player testPlayer = new base.system.Player(testPlayerUsername);
        boolean result = true;
        boolean compare = testPlayer.getName().equals(testPlayerUsername);

        assertEquals(compare, result);
    }

    @Test
    void testPlayerNameBad() {
        String testPlayerUsername = "Name2";
        base.system.Player testPlayer = new base.system.Player(testPlayerUsername);
        boolean result = false;
        testPlayerUsername = "Test2W";
        boolean compare = testPlayer.getName().equals(testPlayerUsername);

        assertEquals(compare, result);
    }

    @Test
    void testPlayerNameBoundary() {
        String testPlayerUsername = "Name3";
        base.system.Player testPlayer = new base.system.Player(testPlayerUsername);
        boolean result = true;
        boolean compare = testPlayer.getName().equals(testPlayerUsername);

        assertEquals(compare, result);
    }

    @Test
    void testPlayerRaceGood() {
        base.data.RaceList testRace = base.data.RaceList.ELF;
        base.system.Player testPlayer = new base.system.Player(testRace);
        boolean result = true;
        boolean compare = testPlayer.getName().equals(testRace.getName());

        assertEquals(compare, result);
    }

    @Test
    void testPlayerRaceBad() {
        base.data.RaceList testRace = base.data.RaceList.ELF;
        base.system.Player testPlayer = new base.system.Player(base.data.RaceList.HUMAN);
        boolean result = false;
        boolean compare = testPlayer.getName().equals(testRace.getName());

        assertEquals(compare, result);
    }

    @Test
    void testPlayerRaceBoundary() {
        base.data.RaceList testRace = base.data.RaceList.ELF;
        base.system.Player testPlayer = new base.system.Player(testRace);
        boolean result = true;
        boolean compare = testPlayer.getPlayerRace().equals(testRace.getName());

        assertEquals(compare, result);
    }

    @Test
    void testGenerateDeckGood() {
        base.system.Player testPlayer = new base.system.Player(base.data.RaceList.ELF);
        base.system.DeckBuilder builder = new base.system.DeckBuilder();
        builder.generateDeck(testPlayer);
        ArrayList<base.system.Card> playerDeck = testPlayer.getPlayerDeck();
        boolean result = true;
        boolean compare = true;
        for (base.system.Card card : playerDeck) {
            if (card.getType() != base.data.RaceList.ELF.getType()) {
                compare = false;
            }
        }
        assertEquals(compare, result);
    }

    @Test
    void testGenerateDeckBad() {
        base.system.Player testPlayer = new base.system.Player(base.data.RaceList.ELF);
        base.system.DeckBuilder builder = new base.system.DeckBuilder();
        builder.generateDeck(testPlayer);
        ArrayList<base.system.Card> playerDeck = testPlayer.getPlayerDeck();
        boolean result = false;
        boolean compare = true;
        for (base.system.Card card : playerDeck) {
            if (card.getType() != base.data.RaceList.ELF.getType()) {
                compare = false;
            }
        }
        assertEquals(compare, result);
    }

    @Test
    void testGenerateDeckBoundary() {
        base.system.Player testPlayer = new base.system.Player(base.data.RaceList.ELF);
        base.system.DeckBuilder builder = new base.system.DeckBuilder();
        builder.generateDeck(testPlayer);
        ArrayList<base.system.Card> playerDeck = testPlayer.getPlayerDeck();
        boolean result = true;
        boolean compare = true;
        for (base.system.Card card : playerDeck) {
            if (card.getType() != base.data.RaceList.ELF.getType()) {
                compare = false;
            }
        }
        assertEquals(compare, result);
    }

    @Test
    void drawCardGood() {
        ArrayList<base.system.Card> testDeck = new ArrayList<base.system.Card>();
        ArrayList<base.system.Card> testHand = new ArrayList<base.system.Card>();
        base.system.DeckBuilder builder = new base.system.DeckBuilder();
        testDeck.add(new base.system.Card(base.data.RaceMinionList.SWORDSMAN));
        builder.drawCard(testDeck, testHand);
        boolean result = true;
        boolean compare = false;
        if (testHand.get(0).getName().equals(base.data.RaceMinionList.SWORDSMAN.getName())) {
            compare = true;
        }
        assertEquals(compare, result);
    }

    @Test
    void drawCardBad() {
        ArrayList<base.system.Card> testDeck = new ArrayList<base.system.Card>();
        ArrayList<base.system.Card> testHand = new ArrayList<base.system.Card>();
        base.system.DeckBuilder builder = new base.system.DeckBuilder();
        testDeck.add(new base.system.Card(base.data.RaceMinionList.SWORDSMAN));
        builder.drawCard(testDeck, testHand);
        boolean result = false;
        boolean compare = true;
        if (testHand.get(0).getName().equals(base.data.RaceMinionList.CALVARY.getName())) {
            compare = false;
        }
        assertEquals(compare, result);
    }

    @Test
    void drawCardBoundary() {
        ArrayList<base.system.Card> testDeck = new ArrayList<base.system.Card>();
        ArrayList<base.system.Card> testHand = new ArrayList<base.system.Card>();
        base.system.DeckBuilder builder = new base.system.DeckBuilder();
        testDeck.add(new base.system.Card(base.data.RaceMinionList.SWORDSMAN));
        builder.drawCard(testDeck, testHand);
        boolean result = true;
        boolean compare = false;
        if (testHand.get(0).getName().equals(base.data.RaceMinionList.SWORDSMAN.getName())) {
            compare = true;
        }
        assertEquals(compare, result);
    }

    @Test
    void attackGood() {
        base.system.Card testAttacker = new base.system.Card(base.data.RaceMinionList.SWORDSMAN);
        base.system.Card testTarget = new base.system.Card(base.data.RaceMinionList.MANTICORE);
        base.system.Referee testReferee = new base.system.Referee();
        boolean result = true;
        boolean compare = false;
        testReferee.attack(testAttacker, testTarget);
        if (testTarget.getCardLifePoint() == 4) {
            compare = true;
        }
        assertEquals(compare, result);
    }

    @Test
    void attackBad() {
        base.system.Card testAttacker = new base.system.Card(base.data.RaceMinionList.SWORDSMAN);
        base.system.Card testTarget = new base.system.Card(base.data.RaceMinionList.MANTICORE);
        base.system.Referee testReferee = new base.system.Referee();
        boolean result = true;
        boolean compare = true;
        testReferee.attack(testAttacker, testTarget);
        if (testTarget.getCardLifePoint() == 4) {
            compare = false;
        }
        assertEquals(compare, result);
    }

    @Test
    void attackBoundary() {
        base.system.Card testAttacker = new base.system.Card(base.data.RaceMinionList.SWORDSMAN);
        base.system.Card testTarget = new base.system.Card(base.data.RaceMinionList.MANTICORE);
        base.system.Referee testReferee = new base.system.Referee();
        boolean result = true;
        boolean compare = false;
        testReferee.attack(testAttacker, testTarget);
        if (testTarget.getCardLifePoint() == 4) {
            compare = true;
        }
        assertEquals(compare, result);
    }

    @Test
    void declareResultGood() {
        base.system.Player testPlayer = new base.system.Player();
        base.system.Referee testReferee = new base.system.Referee();
        boolean result = false;
        testPlayer.setPlayerHealthPoint(0);
        testReferee.declareResult(testPlayer);
        boolean compare = testPlayer.isAlive();
        assertEquals(compare, result);
    }

    @Test
    void declareResultBad() {
        base.system.Player testPlayer = new base.system.Player();
        base.system.Referee testReferee = new base.system.Referee();
        boolean result = false;
        boolean compare = true;
        testPlayer.setPlayerHealthPoint(10);
        testReferee.declareResult(testPlayer);
        if (testPlayer.isAlive() == true) {
            compare = false;
        }
        assertEquals(compare, result);
    }

    @Test
    void declareResulatBoundary() {
        base.system.Player testPlayer = new base.system.Player();
        base.system.Referee testReferee = new base.system.Referee();
        boolean result = false;
        testPlayer.setPlayerHealthPoint(0);
        testReferee.declareResult(testPlayer);
        boolean compare = testPlayer.isAlive();
        assertEquals(compare, result);
    }
}
