package modele.plateau;

import modele.item.ItemShape;

import java.util.Random;

public class Mine extends Machine {
    private int i = 0;

    @Override
    public void work() { // TODO : modifier, suivant le gisement
        if (i == 0) {
            System.out.println("Nouvelle mine");
            i++;
            current.add(new ItemShape("CrCb--Cb"));
        }
    }
}
