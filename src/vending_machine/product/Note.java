/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vending_machine.product;

/**
 *
 * @author phats
 */
public class Note {
    String denomination;

    public Note(String denomination, int value) {
        this.denomination = denomination;
        this.value = value;
    }

    public Note() {
    }
    int value;

    public String getDenomination() {
        return denomination;
    }

    public int getValue() {
        return value;
    }

    public void setDenomination(String denomination) {
        this.denomination = denomination;
    }

    public void setValue(int value) {
        this.value = value;
    }
    
}
