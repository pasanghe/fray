package base.system;

import java.util.ArrayList;

public class Player implements base.system.InterfaceRace {

    private base.system.InterfaceRace raceData;
    private String playerName;
    private String playerRace;
    private ArrayList<Card> playerHand;
    private ArrayList<Card> playerDeck;
    private int playerHealthPoint;
    private boolean alive;

    public Player() {
    }

    public Player(String name) {
        this.playerName = name;
    }

    public Player(InterfaceRace playerRace) {
        this.raceData = playerRace;
        this.playerRace = raceData.getName();
        this.playerHealthPoint = raceData.getHealthPoint();
        this.alive = true;
    }

    public Player(String name, InterfaceRace playerRace) {
        this.raceData = playerRace;
        this.playerName = playerName;
        this.playerRace = raceData.getName();
        this.playerHealthPoint = raceData.getHealthPoint();
        this.alive = true;
    }

    public String getName() {
        return this.playerName;
    }

    public String getPlayerRace() {
        return playerRace;
    }

    public void setPlayerRace(String playerRace) {
        this.playerRace = playerRace;
    }

    public void setPlayerHand(ArrayList<Card> playerHand) {
        this.playerHand = playerHand;
    }

    public ArrayList<Card> getPlayerHand() {
        return this.playerHand;
    }

    public void setPlayerDeck(ArrayList<Card> playerDeck) {
        this.playerDeck = playerDeck;
    }

    public ArrayList<Card> getPlayerDeck() {
        return this.playerDeck;
    }

    public void setPlayerHealthPoint(int playerHealthPoint) {
        this.playerHealthPoint = playerHealthPoint;
    }

    public boolean isAlive() {
        return alive;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    @Override
    public base.data.ObjectType getType() {
        return this.raceData.getType();
    }

    @Override
    public int getHealthPoint() {
        return this.playerHealthPoint;
    }

    @Override
    public String toString() {
        return ("Player Name: " + getName() + " | Race: " + getPlayerRace() + " | HP: " + getHealthPoint());
    }
}
