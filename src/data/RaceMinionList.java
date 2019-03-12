/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import system.Card;
import data.RaceList;

/**
 *
 * @author Sang Heon, Park (991517263)
 */
public enum RaceMinionList implements system.InterfaceCard {

    SWORDSMAN(ObjectType.MINION, RaceList.HUMAN, "Swordsman", 1, 2, 0, 1),
    BOMBARDIER(ObjectType.MINION, RaceList.HUMAN, "Bombardier", 1, 1, 0, 1),
    CALVARY(ObjectType.MINION, RaceList.HUMAN, "Calvary", 2, 1, 0, 2),
    MANTICORE(ObjectType.MINION, RaceList.ELF, "Manticore", 2, 5, 0, 5),
    PIXIE(ObjectType.MINION, RaceList.ELF, "Pixie", 2, 2, 0, 1);

    private ObjectType cardType;
    private RaceList cardRace;
    private String name;
    private int damagePoint;
    private int lifePoint;
    private int armorPoint;
    private int energyCost;

    RaceMinionList(ObjectType cardType, RaceList cardRace, String name, int damagePoint, int lifePoint, int armorPoint, int energyCost) {
        this.cardType = cardType;
        this.cardRace = cardRace;
        this.name = name;
        this.damagePoint = damagePoint;
        this.lifePoint = lifePoint;
        this.armorPoint = armorPoint;
        this.energyCost = energyCost;
    }

    @Override
    public ObjectType getType() {
        return this.cardType;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getCardDamagePoint() {
        return this.damagePoint;
    }

    @Override
    public int getCardLifePoint() {
        return this.lifePoint;
    }

    @Override
    public int getCardArmorPoint() {
        return this.armorPoint;
    }

    @Override
    public int getCardCost() {
        return this.energyCost;
    }

}
