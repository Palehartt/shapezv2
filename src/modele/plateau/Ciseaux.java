package modele.plateau;

import modele.item.Item;

import java.util.List;

public class Ciseaux extends Machine {
    public static List<Point> getOffsets(Direction d) {
        return switch (d) {
            case North -> List.of(new Point(1, 0));  // s'étend vers l'Est
            case East  -> List.of(new Point(0, 1));  // s'étend vers le Sud
            case South -> List.of(new Point(-1, 0)); // s'étend vers l'Ouest
            case West  -> List.of(new Point(0, -1)); // s'étend vers le Nord
        };
    }

    @Override
    public void work() {
    }

    @Override
    public void send() {
        Case next = c.plateau.getCase(c, Direction.North);
        if (next != null && next.getMachine() != null && !current.isEmpty()) {
            Item item = current.removeFirst();
            next.getMachine().current.add(item);
        }
    }
}
