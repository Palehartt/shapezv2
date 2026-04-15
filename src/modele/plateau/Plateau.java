/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modele.plateau;



import modele.item.Color;
import modele.item.Item;
import modele.item.SubShape;

import java.util.HashMap;
import java.util.List;
import java.util.Observable;


public class Plateau extends Observable implements Runnable {

    public static final int SIZE_X = 25;
    public static final int SIZE_Y = 14;


    private HashMap<Case, Point> map = new HashMap<Case, Point>(); // permet de récupérer la position d'une case à partir de sa référence
    private Case[][] grilleCases = new Case[SIZE_X][SIZE_Y]; // permet de récupérer une case à partir de ses coordonnées

    public Plateau() {
        initPlateauVide();
    }

    public int[] getSize(){
        return new int[]{SIZE_X, SIZE_Y};
    }

    public Case[][] getCases() {
        return grilleCases;
    }

    public Case getCase(Case source, Direction d) {
        
        Point p = map.get(source);
        return caseALaPosition(new Point(p.x+d.dx, p.y+d.dy));


    }

    private void initPlateauVide() {

        for (int x = 0; x < SIZE_X; x++) {
            for (int y = 0; y < SIZE_Y; y++) {
                grilleCases[x][y] = new Case(this);
                map.put(grilleCases[x][y], new Point(x, y));
            }

        }

    }

    public void setMachine(int x, int y, Machine m) {
        if (m == null) {
            Machine old = grilleCases[x][y].getMachine();
            if (old != null) {
                for (Case esclave : old.getCasesOccupees()) {
                    esclave.setMachineEsclave(null);
                }
            }
            grilleCases[x][y].setMachine(null);
        } else {
            grilleCases[x][y].setMachine(m);
        }
        setChanged();
        notifyObservers();
    }

    public void setMachine(int x, int y, Machine m, List<Point> offsets) {
        grilleCases[x][y].setMachine(m);

        for (Point offset : offsets) {
            Case esclave;

            // Si c'est un Hub, chaque case esclave a sa propre file
            if (m instanceof Hub) {
                esclave = new CaseHub(this);
                grilleCases[x + offset.x][y + offset.y] = esclave;
                map.put(esclave, new Point(x + offset.x, y + offset.y));
            } else {
                esclave = grilleCases[x + offset.x][y + offset.y];
            }

            esclave.setMachineEsclave(m);
            m.getCasesOccupees().add(esclave);
        }

        setChanged();
        notifyObservers();
    }

    public Point getPosition(Case c) {
        return map.get(c);
    }

    public void setGisement(int x, int y, SubShape shape) {
        grilleCases[x][y].setGisement(shape);
        setChanged();
        notifyObservers();
    }

    public void setGisement(int x, int y, Color color) {
        grilleCases[x][y].setGisement(color);
        setChanged();
        notifyObservers();
    }

    public void rotateMachine(int x, int y) {
        Machine m = grilleCases[x][y].getMachine();
        if (m != null) {
            m.rotate();
            refresh();
        }
    }


    /**
     * Indique si p est contenu dans la grille
     */
    private boolean contenuDansGrille(Point p) {
        return p.x >= 0 && p.x < SIZE_X && p.y >= 0 && p.y < SIZE_Y;
    }

    private Case caseALaPosition(Point p) {
        Case retour = null;

        if (contenuDansGrille(p)) {
            retour = grilleCases[p.x][p.y];
        }
        return retour;
    }

    public void refresh() {
        setChanged();
        notifyObservers();
    }

    // Dans Plateau.java
    public void removeMachine(int x, int y) {
        Machine m = grilleCases[x][y].getMachine();
        if (m != null) {
            for (Case esclave : m.getCasesOccupees()) {
                esclave.setMachineEsclave(null);
                Point pos = map.get(esclave);
                if (esclave instanceof CaseHub && pos != null) {
                    Case newCase = new Case(this);
                    grilleCases[pos.x][pos.y] = newCase;
                    map.put(newCase, pos);
                    map.remove(esclave);
                }
            }
            m.getCasesOccupees().clear();
        }
        grilleCases[x][y].setMachine(null);
        setChanged();
        notifyObservers();
    }


    // Plateau.run()
    @Override
    public void run() {
        // Snapshot : mémoriser les items actuels de chaque machine
        HashMap<Machine, Item> snapshot = new HashMap<>();
        for (int x = 0; x < SIZE_X; x++)
            for (int y = 0; y < SIZE_Y; y++) {
                Machine m = grilleCases[x][y].getMachine();
                if (m != null) snapshot.put(m, m.getCurrent());
            }

        // Work sur tout le monde
        for (int x = 0; x < SIZE_X; x++)
            for (int y = 0; y < SIZE_Y; y++) {
                Machine m = grilleCases[x][y].getMachine();
                if (m != null) m.work();
            }

        // Send uniquement si la machine avait un item AU DÉBUT du tick
        for (int x = 0; x < SIZE_X; x++)
            for (int y = 0; y < SIZE_Y; y++) {
                Machine m = grilleCases[x][y].getMachine();
                if (m != null && snapshot.get(m) != null) m.send();
            }

        setChanged();
        notifyObservers();
    }
}