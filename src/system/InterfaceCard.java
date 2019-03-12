/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package system;

/**
 *
 * @author Sang Heon, Park (991517263)
 */
public interface InterfaceCard extends system.FrayObject {

    public int getCardCost();

    public int getCardDamagePoint();

    public int getCardLifePoint();

    public int getCardArmorPoint();
}
