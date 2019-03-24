package Game;

import java.util.TimerTask;

public class UpdateCheckerInGame extends TimerTask {

	@Override
	public void run() {
		FrayGame.inGameStatusCheck();
		FrayGame.inGameStatusCheckPlayer2();
	}

}
