package testing;

import objectClasses.Player;
import static org.junit.Assert.*;
import org.junit.Test;
class TestingPlayerObjectClass {

	@Test
	void testPlayerStringString() {
		String testPlayerUsername = "Test1";
		String testPlayerRace = "Elf";
		Player testPlayer = new Player(testPlayerUsername, testPlayerRace);
		if((testPlayer.getPlayerID().equals(testPlayerUsername)) || (testPlayer.getPlayerRaceName().equals(testPlayerRace)))
			assertEquals(true, true);
		else
			assertEquals(true, false);
	}

}
