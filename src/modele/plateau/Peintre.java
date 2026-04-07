package modele.plateau;

import modele.item.*;

public class Peintre extends Machine {

    private Direction inputDir;  // direction d'entrée de la forme
    private Direction colorDir;  // direction d'entrée de la couleur

    public Peintre(Direction output, Direction inputDir, Direction colorDir) {
        super();
        this.d = output;
        this.inputDir = inputDir;
        this.colorDir = colorDir;
    }

    private ItemShape pendingShape = null;
    private modele.item.Color pendingColor = null;

    @Override
    public void send() {
        while (!current.isEmpty()) {
            Item item = current.getFirst();
            current.removeFirst();

            if (item instanceof ItemShape && pendingShape == null) {
                pendingShape = (ItemShape) item;
            } else if (item instanceof ItemColor && pendingColor == null) {
                pendingColor = ((ItemColor) item).getColor();
            }
        }

        // Si on a les deux, on peint et on envoie
        if (pendingShape != null && pendingColor != null) {
            pendingShape.applyColor(pendingColor);
            current.add(pendingShape);
            pendingShape = null;
            pendingColor = null;
            send();
        }
    }
}