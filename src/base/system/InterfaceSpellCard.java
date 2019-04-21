package base.system;

import base.data.ObjectType;

/**
 *
 * @author Sang Heon, Park (991517263)
 */
public interface InterfaceSpellCard {

    public ObjectType getCardType();

    public String getCardName();

    public int getCardCost();

    public int getCardSpellValue();
}
