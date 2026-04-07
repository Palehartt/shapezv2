/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;

import modele.item.*;

public class Case {


    protected Plateau plateau;
    protected Machine machine;
    protected Machine machineEsclave; // référence vers une machine qui déborde ici
    protected Item gisement;

    public void setMachine(Machine m) {
        machine = m;
        if (m != null) {
            m.setCase(this);
        }
    }

    public Machine getMachine() {
        return machine;
    }

    public Machine getMachineEsclave() {
        return machineEsclave;
    }

    public boolean isEsclave() {
        return machine == null && machineEsclave != null;
    }

    public void setMachineEsclave(Machine m) {
        machineEsclave = m;
    }

    public Case(Plateau _plateau) {

        plateau = _plateau;
    }

    public void setGisement(SubShape shape) {
        gisement = new ItemGisement(shape);
    }

    public void setGisement(Color color) {
        gisement = new ItemGisement(color);
    }

    public Item getGisement() {
        return gisement;
    }
}