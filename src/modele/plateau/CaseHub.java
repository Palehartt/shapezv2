package modele.plateau;

import modele.item.Item;
import java.util.LinkedList;

public class CaseHub extends Case {
    public LinkedList<Item> itemsRecus = new LinkedList<>();

    public CaseHub(Plateau plateau) {
        super(plateau);
    }
}