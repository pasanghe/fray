/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package base.data;

/**
 *
 * @author tuturu
 */
public enum ObjectType {
    RACE("Race"), MINION("Minion"), SPELL("Spell");

    private String type;

    ObjectType(String type) {
        this.type = type;
    }
}
