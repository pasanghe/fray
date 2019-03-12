package system;

import data.ObjectType;

public class Card implements system.InterfaceCard {

    private InterfaceCard cardInfo;
    private int attackPoint;
    private int lifePoint;
    private int armorPoint;
    private int energyCost;

    public Card(InterfaceCard cardInfo) {
        this.cardInfo = cardInfo;
        this.attackPoint = cardInfo.getCardDamagePoint();
        this.lifePoint = cardInfo.getCardLifePoint();
        this.armorPoint = cardInfo.getCardArmorPoint();
        this.energyCost = cardInfo.getCardCost();
    }

    @Override
    public ObjectType getType() {
        return cardInfo.getType();
    }

    @Override
    public String getName() {
        return cardInfo.getName();
    }

    @Override
    public int getCardDamagePoint() {
        return this.attackPoint;
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

    @Override
    public String toString() {
        return (cardInfo.getName() + " (" + cardInfo.getType() + "| AP: " + getCardDamagePoint() + " LP: " + getCardLifePoint() + " AP: " + getCardArmorPoint());
    }
}
