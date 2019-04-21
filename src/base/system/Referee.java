package base.system;

import base.data.ObjectType;
import java.util.ArrayList;

public class Referee {

    public void declareResult(Player player) {
        if (player.getHealthPoint() == 0) {
            player.setAlive(false);
        } else {
            player.setAlive(true);
        }
    }

    public void attack(Card attacker, Card target) {
        if (target.getType() == ObjectType.MINION) {
            target.setCardLifePoint(target.getCardLifePoint() - attacker.getCardDamagePoint());
        }
    }

    public void destroyCard(ArrayList<Card> list) {
        for (Card card : list) {
            if (card.getCardLifePoint() <= 0) {
                list.remove(card);
            }
        }
    }
}
