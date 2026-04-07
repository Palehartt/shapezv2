package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;

import java.util.List;

public class Ciseaux extends Machine {

    public static List<Point> getOffsets(Direction d) {
        return switch (d) {
            case North -> List.of(new Point(1, 0));
            case East  -> List.of(new Point(0, 1));
            case South -> List.of(new Point(-1, 0));
            case West  -> List.of(new Point(0, -1));
        };
    }

    @Override
    public void work() {
        if (current.isEmpty()) return;
        Item item = current.getFirst();
        if (!(item instanceof ItemShape shape)) return;

        ItemShape[] halves = shape.cut(d);
        current.removeFirst();
        current.add(halves[0]);
        current.add(halves[1]);
    }

    @Override
    public void send() {
        if (current.isEmpty()) return;

        if (current.size() >= 1) {
            Direction droite = switch (d) {
                case North -> Direction.East;
                case East -> Direction.South;
                case South  -> Direction.West;
                case West  -> Direction.North;
            };
            Case preNext = c.plateau.getCase(c, droite);
            Case next = preNext.plateau.getCase(preNext, d);
            if (next != null && next.getMachine() != null) {
                next.getMachine().current.add(current.removeFirst());
            }
        }

        if (current.size() >= 1) {
            Case next = c.plateau.getCase(c, d);
            if (next != null && next.getMachine() != null) {
                next.getMachine().current.add(current.removeFirst());
            }
        }
    }
}