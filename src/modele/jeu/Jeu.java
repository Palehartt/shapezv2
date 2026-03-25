package modele.jeu;

import modele.plateau.*;
import modele.item.ItemShape;

public class Jeu extends Thread{
    private Plateau plateau;




    public Jeu() {
        plateau = new Plateau();
        environnement(plateau);
        start();

    }

    public void environnement(Plateau plateau) {
        // génération de 5 mines
        plateau.setMachine(5, 10, new Mine());
        plateau.setMachine(5, 5, new Mine());
        plateau.setMachine(13, 15, new Mine());
        plateau.setMachine(1, 12, new Mine());
        plateau.setMachine(1, 12, new Mine());
        plateau.setMachine(1, 9, new Livraison());
        // plateau.setMachine(1, 12, new Livraison());
    }

    public Plateau getPlateau() {
        return plateau;
    }


    public void press(int x, int y) {

        plateau.setMachine(x, y, new Tapis());
    }

    public void slide(int x, int y) {
        plateau.setMachine(x, y, new Tapis());
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
