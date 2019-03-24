package Game;

import java.util.TimerTask;

public class UpdateCheck extends TimerTask{

	@Override
	public void run() {
		FrayPlayerSelectScreen.jDU.setRowCount(0);
		FrayPlayerSelectScreen.displayUsersOut();
		FrayPlayerSelectScreen.jDIGI.setRowCount(0);
		FrayPlayerSelectScreen.displaySentGameInvitesOut();
		FrayPlayerSelectScreen.jDRGI.setRowCount(0);
		FrayPlayerSelectScreen.displayReceivedGameInvitesOut();
		FrayPlayerSelectScreen.sentGameInviteAccepted();
		FrayPlayerSelectScreen.sentSecondGameInviteAccepted();
	}
}
