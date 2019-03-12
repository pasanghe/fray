/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

/**
 *
 * @author Sang Heon, Park (991517263)
 */
public enum RaceSpellList implements system.InterfaceSpellCard {

    FORGE(ObjectType.SPELL, RaceList.HUMAN, "Forge", 1, 3),
    FIREBALL(ObjectType.SPELL, RaceList.ELF, "Fireball", 3, 2),
    POSINOUS_TREANT(ObjectType.SPELL, RaceList.ELF, "Posinous Treant", 3, 2),
    HYSTERIA(ObjectType.SPELL, RaceList.ELF, "Hysteria", 2, 3),
    OAKENSHIELD(ObjectType.SPELL, RaceList.ELF, "Oakenshield", 3, 4);

    private ObjectType cardType;
    private RaceList cardRace;
    private String cardName;
    private int spellValue;
    private int energyCost;

    RaceSpellList(ObjectType cardType, RaceList cardRace, String cardName, int spellValue, int energyCost) {
        this.cardType = cardType;
        this.cardRace = cardRace;
        this.cardName = cardName;
        this.spellValue = spellValue;
        this.energyCost = energyCost;
    }

    @Override
    public ObjectType getCardType() {
        return this.cardType;
    }

    @Override
    public String getCardName() {
        return this.cardName;
    }

    @Override
    public int getCardSpellValue() {
        return this.spellValue;
    }

    @Override
    public int getCardCost() {
        return this.energyCost;
    }

}
