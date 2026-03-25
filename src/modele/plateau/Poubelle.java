package modele.plateau;

import modele.item.Item;

public class Poubelle extends Machine {
    @Override
    public void send() {
        if (!current.isEmpty()) {
            Item item = current.getFirst();
            current.remove(item);
        }
    }
}
