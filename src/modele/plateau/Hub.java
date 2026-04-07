package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;
import modele.item.SubShape;

import java.util.ArrayList;
import java.util.List;

public class Hub extends Machine {
    private int compteur = 0;
    private List<SubShape> formesAcceptees = new ArrayList<>();

    public Hub() {
        super();
    }

    public void addFormeAcceptee(SubShape shape) {
        formesAcceptees.add(shape);
    }

    public int getCompteur() {
        return compteur;
    }

    public static List<Point> getOffsets(Direction d) {
        return List.of(
                new Point(1, 0), new Point(2, 0), new Point(3, 0), // ligne 0
                new Point(0, 1), new Point(1, 1), new Point(2, 1), new Point(3, 1), // ligne 1
                new Point(0, 2), new Point(1, 2), new Point(2, 2), new Point(3, 2), // ligne 2
                new Point(0, 3), new Point(1, 3), new Point(2, 3), new Point(3, 3)  // ligne 3
        );
    }

    @Override
    public void work() {
        // Collecter les items de toutes les cases esclaves (CaseHub)
        for (Case esclave : casesOccupées) {
            if (esclave instanceof CaseHub caseHub) {
                while (!caseHub.itemsRecus.isEmpty()) {
                    Item item = caseHub.itemsRecus.removeFirst();
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
                            compteur++;
                            System.out.println("C'est good");
                        }
                    } else {
                        System.out.println("Hub : forme refusée.");
                    }
                }
            }
        }

        // Items reçus sur la case principale
        while (!current.isEmpty()) {
            Item item = current.removeFirst();
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
                    compteur++;
                    System.out.println("C'est good");
                }
            } else {
                System.out.println("Hub : forme refusée.");
            }
        }
        System.out.println("Compteur = " + compteur);
    }

    private boolean estAcceptee(ItemShape shape) {
        if (formesAcceptees.isEmpty()) return true; // accepte tout si aucun filtre

        SubShape[] subs = shape.getSubShapes(ItemShape.Layer.one);
        for (SubShape acceptee : formesAcceptees) {
            boolean match = true;
            for (SubShape sub : subs) {
                if (sub != acceptee && sub != SubShape.None) {
                    match = false;
                    break;
                }
            }
            if (match) return true;
        }
        return false;
    }
}
