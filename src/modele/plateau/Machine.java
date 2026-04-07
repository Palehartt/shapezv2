package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Machine implements Runnable {
    LinkedList<Item> current;
    Case c;           // case principale
    List<Case> casesOccupées = new ArrayList<>(); // cases supplémentaires
    Direction d = Direction.North;

    public Machine()
    {
        current = new LinkedList<Item>();
    }

    public List<Case> getCasesOccupees() {
        return casesOccupées;
    }

    public Machine(Item _item) {
        this();
        current.add(_item);
    }

    public void setCase(Case _c) {
        c= _c;
    }

    public Item getCurrent() {
        if (current.size() > 0) {
            return current.get(0);
        } else {
            return null;
        }
    }

    public Direction getDirection() {
        return d;
    }

    public void setDirection(Direction d) {
        this.d = d;
    }

    /*public void send() // la machine dépose un item sur sa ou ses sorties
    {
        Case up = c.plateau.getCase(c, Direction.North);
        if (up != null) {
            Machine m = up.getMachine();
            ;
            if (m != null && !current.isEmpty()) {
                Item item = current.getFirst();
                m.current.add(item);
                System.out.println("Debut : " + current.size());
                current.removeFirst();
                System.out.println("Fin : " + current.size());
            }
        }
    }*/

    public void send() {
        Direction sendDir = d;
        if (this instanceof Tapis) {
            sendDir = ((Tapis) this).getOutputDirection();
        }

        Case next = c.plateau.getCase(c, sendDir);
        if (next != null && !current.isEmpty()) {
            Item item = current.getFirst();

            if (next instanceof CaseHub caseHub) {
                // Déposer dans la file de cette case spécifique
                caseHub.itemsRecus.add(item);
                current.remove(item);
            } else {
                Machine m = next.getMachine() != null
                        ? next.getMachine()
                        : next.getMachineEsclave();
                if (m != null) {
                    m.current.add(item);
                    current.remove(item);
                }
            }
        }
    }

    public void rotate() {
        switch (d) {
            case North -> d = Direction.East;
            case South -> d = Direction.West;
            case East -> d = Direction.South;
            case West -> d = Direction.North;
        }
        System.out.println(d);
    }

    public void work() {
        if (current.size() > 0) {
            //((ItemShape) current.get(0)).rotate();

        }
    }; // action de la machine, aucune par défaut

    @Override
    public void run() {
        work();
        send();


    }


    public Case getCase() {
        return c;
    }
}
