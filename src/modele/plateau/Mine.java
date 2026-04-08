package modele.plateau;

import modele.item.Item;
import modele.item.ItemGisement;
import modele.item.Color;
import modele.item.ItemColor;
import modele.item.ItemShape;
import modele.item.SubShape;

import java.util.Random;

public class Mine extends Machine {
    private int i = 0;

    @Override
    public void work() { // TODO : modifier, suivant le gisement
        Item item = c.getGisement();
        if (item instanceof ItemGisement) {
            if (((ItemGisement) item).getSubShape().equals(SubShape.Carre)) {
                if (new Random().nextInt(4) == 0) {
                    i++;
                    current.add(new ItemShape("CwCwCwCw"));
                }
            } else if (((ItemGisement) item).getSubShape().equals(SubShape.Circle)) {
                if (new Random().nextInt(4) == 0) {
                    i++;
                    current.add(new ItemShape("RwRwRwRw"));
                }
            } else {
                switch (((ItemGisement) item).getColor()) {
                    case Red:
                        if (new Random().nextInt(4) == 0) {
                            i++;
                            current.add(new ItemColor(Color.Red));
                        }
                        break;
                    case Blue:
                        if (new Random().nextInt(4) == 0) {
                            i++;
                            current.add(new ItemColor(Color.Blue));
                        }
                        break;
                    case Green:
                        if (new Random().nextInt(4) == 0) {
                            i++;
                            current.add(new ItemColor(Color.Green));
                        }
                        break;
                    case None:
                }
                //System.out.println("Nouvel item à envoyer");
            }
        }
    }
}
