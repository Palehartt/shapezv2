package modele.plateau;

import modele.item.Item;
import modele.item.ItemColor;
import modele.item.ItemShape;
import modele.item.SubShape;

import java.util.Random;

public class Livraison extends Machine {
    @Override
    public void work() {

    }

    @Override
    public void send() {
        Case up = c.plateau.getCase(c, Direction.North);
        Case down = c.plateau.getCase(c, Direction.South);
        Case right = c.plateau.getCase(c, Direction.East);
        Case left = c.plateau.getCase(c, Direction.West);
        if (!current.isEmpty()) {
            Item item = current.getFirst();
            if (item instanceof ItemShape shape) {
                System.out.println("On a des formes");
                // La variable 'shape' est déjà castée et prête à l'usage ici
                SubShape[] sub = shape.getSubShapes(ItemShape.Layer.one);
                System.out.println(sub[0]);
                System.out.println(sub[1]);
                System.out.println(sub[2]);
                System.out.println(sub[3]);
                if(sub[0] == SubShape.Carre && sub[1] == SubShape.Carre &&  sub[2] == SubShape.Carre && sub[3] == SubShape.Carre) {
                    current.remove(item);
                    System.out.println("C'est good");
                }
            } else {
                System.out.println("On a pas de forme");
            }
        }
    }

}
