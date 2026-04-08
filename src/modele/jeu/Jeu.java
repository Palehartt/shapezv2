package modele.jeu;

import modele.item.Color;
import modele.item.SubShape;
import modele.plateau.*;
import modele.item.ItemShape;

import java.util.List;

public class Jeu extends Thread{
    private Plateau plateau;
    private int niveau = 0;

    private int lastX = -1; //Detection du déplacement de la souris lors du Drag and Drop
    private int lastY = -1;


    public Jeu() {
        plateau = new Plateau();
        environnement(plateau);
        start();

    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public void environnement(Plateau plateau) {
        // génération de 5 gisements
        int emplacement_color_x = 13;
        int emplacement_color_y = 13;

        int emplacement_color_xx = 10;
        int emplacement_color_yy = 10;

        int emplacement_shape_x = 0;
        int emplacement_shape_y = 13;

        for(int i=emplacement_shape_x; i<emplacement_shape_x+3; i++)
            for(int j=emplacement_shape_y; j<emplacement_shape_y+3; j++)
                plateau.setGisement(i, j, SubShape.Carre);

        for(int i=emplacement_color_x; i<emplacement_color_x+3; i++)
            for(int j=emplacement_color_y; j<emplacement_color_y+3; j++)
                plateau.setGisement(i, j, Color.Red);

        for(int i=emplacement_color_xx; i<emplacement_color_xx+3; i++)
            for(int j=emplacement_color_yy; j<emplacement_color_yy+3; j++)
                plateau.setGisement(i, j, Color.Blue);

        plateau.setMachine(4, 4, new Hub(0), Hub.getOffsets(Direction.North));
    }

    public Plateau getPlateau() {
        return plateau;
    }

    public void supprimer(int x, int y) {
        Case c = plateau.getCases()[x][y];
        Machine m = c.getMachine();

        if (m == null) return;           // rien à supprimer
        if (m instanceof Livraison) return; // on protège le hub

        plateau.removeMachine(x, y);
    }


    public void press(int x, int y, ObjetChoisi objetChoisi, Direction d) {
        lastX = x;
        lastY = y;

        Machine m = plateau.getCases()[x][y].getMachine();

        if (m == null) {
            switch (objetChoisi) {
                case Livraison:
                    m = new Livraison();
                    m.setDirection(d);
                    plateau.setMachine(x, y, m);
                    break;
                case Poubelle:
                    m = new Poubelle();
                    m.setDirection(d);
                    plateau.setMachine(x, y, m);
                    break;
                case Tapis:
                    m = new Tapis();
                    m.setDirection(d);
                    plateau.setMachine(x, y, m);
                    break;
                case Mine:
                    m = new Mine();
                    m.setDirection(d);
                    plateau.setMachine(x, y, m);
                    break;
                case Ciseaux :
                    m = new Ciseaux();
                    m.setDirection(d);
                    plateau.setMachine(x, y, m, Ciseaux.getOffsets(d));
                    break;
                case Hub:
                    m = new Hub(0);
                    m.setDirection(d);
                    plateau.setMachine(x, y, m, Hub.getOffsets(d));
                    break;
                case Mixer:
                    m = new Mixer();
                    m.setDirection(d);
                    plateau.setMachine(x, y, m, Mixer.getOffsets(d));
                    m.initPorts();
                    break;
                case Pivoteur:
                    m = new Pivoteur();
                    m.setDirection(d);
                    plateau.setMachine(x, y, m);
                    break;
                default:
                    plateau.setMachine(x, y, null);
            }
        } else if (m instanceof Tapis) {
            plateau.rotateMachine(x, y);
        }
    }

    public void slide(int x, int y) {
        if (lastX == -1 || lastY == -1) return;
        if (x == lastX && y == lastY) return;

        int dx = x - lastX;
        int dy = y - lastY;

        Direction newDir;
        if (Math.abs(dx) > Math.abs(dy)) {
            newDir = dx > 0 ? Direction.East : Direction.West;
        } else {
            newDir = dy > 0 ? Direction.South : Direction.North;
        }

        // Détecter si la case précédente était déjà un tapis avec une direction différente
        Machine prev = plateau.getCases()[lastX][lastY].getMachine();
        if (prev instanceof Tapis) {
            Direction lastDir = prev.getDirection();

            Tapis.TapisType type;
            if (lastDir == newDir) {
                type = Tapis.TapisType.Droit;
            } else {
                // Déterminer si c'est un virage gauche ou droite
                // Virage gauche : on tourne dans le sens anti-horaire
                if ((lastDir == Direction.North && newDir == Direction.West)
                        || (lastDir == Direction.West  && newDir == Direction.South)
                        || (lastDir == Direction.South && newDir == Direction.East)
                        || (lastDir == Direction.East  && newDir == Direction.North)) {
                    type = Tapis.TapisType.VirageGauche;
                } else {
                    type = Tapis.TapisType.ViragedDroite;
                }
            }
            plateau.setMachine(lastX, lastY, new Tapis(lastDir, type));
        } else {
            plateau.setMachine(lastX, lastY, new Tapis(newDir));
        }

        plateau.setMachine(x, y, new Tapis(newDir));

        lastX = x;
        lastY = y;
    }


    public void run() {
        jouerPartie();
    }

    public void jouerPartie() {

        while(true) {
            try {
                plateau.run();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }

    }


}
