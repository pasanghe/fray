package base.system;

import base.data.ObjectType;

public class Card implements base.system.InterfaceCard {

    private InterfaceCard cardInfo;
    private ObjectType cardType;
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

    public void setType(ObjectType type) {
        this.cardType = type;
    }

    public void setCardInfo(InterfaceCard cardInfo) {
        this.cardInfo = cardInfo;
    }

    public void setCardType(ObjectType cardType) {
        this.cardType = cardType;
    }

    public void setCardAttackPoint(int attackPoint) {
        this.attackPoint = attackPoint;
    }

    public void setCardLifePoint(int lifePoint) {
        this.lifePoint = lifePoint;
    }

    public void setCardArmorPoint(int armorPoint) {
        this.armorPoint = armorPoint;
    }

    public void setCardEnergyCost(int energyCost) {
        this.energyCost = energyCost;
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
    public ObjectType getType() {
        return this.cardType;
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
