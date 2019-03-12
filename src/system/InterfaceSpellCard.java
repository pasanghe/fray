/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

import data.ObjectType;

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
