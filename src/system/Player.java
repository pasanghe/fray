package system;

import java.util.ArrayList;

public class Player implements system.InterfaceRace {

    private String playerName;
    private system.InterfaceRace playerRace;
    private ArrayList<system.Card> playerHand;
    private int playerHealthPoint;

    public Player(String name, system.InterfaceRace playerRace) {
        this.playerName = playerName;
        this.playerRace = playerRace;
        playerHand = new ArrayList<>();
        this.playerHealthPoint = playerRace.getHealthPoint();
    }

    public String getName() {
        return this.playerName;
    }

    @Override
    public data.ObjectType getType() {
        return playerRace.getType();
    }

    @Override
    public int getHealthPoint() {
        return this.playerHealthPoint;
    }

    @Override
    public String toString() {
        return ("Player Name: " + getName() + " | Race: " + playerRace.getType() + " | HP: " + getHealthPoint());
    }
}
