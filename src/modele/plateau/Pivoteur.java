package modele.plateau;

import modele.item.*;

public class Pivoteur extends Machine {

    public enum Sens { Horaire, AntiHoraire }

    private Sens sens;

    /*public Pivoteur(Direction output, Sens sens) {
        super();
        this.d = output;
        this.sens = sens;
    }*/

    @Override
    public void work() {
        if (current.isEmpty()) return;
        ((ItemShape) current.getFirst()).rotate();
    }
}