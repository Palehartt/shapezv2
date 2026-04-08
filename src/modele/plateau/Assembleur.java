package modele.plateau;

import modele.item.Item;
import modele.item.ItemShape;
import modele.item.SubShape;
import modele.item.Color;

import java.util.List;

public class Assembleur extends Machine {

    private ItemShape pendingShape1 = null;
    private ItemShape pendingShape2 = null;

    public static List<Point> getOffsets(Direction d) {
        return switch (d) {
            case North -> List.of(new Point(1, 0));
            case East  -> List.of(new Point(0, 1));
            case South -> List.of(new Point(-1, 0));
            case West  -> List.of(new Point(0, -1));
        };
    }

    @Override
    public void initPorts() {
        ports.clear();
        Case esclave = casesOccupées.isEmpty() ? null : casesOccupées.get(0);

        switch (d) {
            case North -> {
                ports.add(new Port(Direction.South, Port.Type.Entree, c));
                if (esclave != null)
                    ports.add(new Port(Direction.South, Port.Type.Entree, esclave));
                ports.add(new Port(Direction.North, Port.Type.Sortie, c));
            }
            case East -> {
                ports.add(new Port(Direction.West, Port.Type.Entree, c));
                if (esclave != null)
                    ports.add(new Port(Direction.West, Port.Type.Entree, esclave));
                ports.add(new Port(Direction.East, Port.Type.Sortie, c));
            }
            case South -> {
                ports.add(new Port(Direction.North, Port.Type.Entree, c));
                if (esclave != null)
                    ports.add(new Port(Direction.North, Port.Type.Entree, esclave));
                ports.add(new Port(Direction.South, Port.Type.Sortie, c));
            }
            case West -> {
                ports.add(new Port(Direction.East, Port.Type.Entree, c));
                if (esclave != null)
                    ports.add(new Port(Direction.East, Port.Type.Entree, esclave));
                ports.add(new Port(Direction.West, Port.Type.Sortie, c));
            }
        }
    }

    @Override
    public void work() {
        List<Port> portsEntree = getPortsEntree();

        if (pendingShape1 == null) {
            Item item = collecterEntrees(portsEntree.get(0));
            if (item instanceof ItemShape shape) {
                pendingShape1 = shape;
            }
        }
        if (pendingShape2 == null) {
            Item item = collecterEntrees(portsEntree.get(1));
            if (item instanceof ItemShape shape) {
                pendingShape2 = shape;
            }
        }
    }

    @Override
    public void send() {
        if (pendingShape1 == null || pendingShape2 == null) return;

        ItemShape result = merge(pendingShape1, pendingShape2);
        pendingShape1 = null;
        pendingShape2 = null;

        Case next = c.plateau.getCase(c, d);
        if (next != null) {
            Machine m = next.getMachine() != null ? next.getMachine() : next.getMachineEsclave();
            if (m != null) m.current.add(result);
        }
    }

    private ItemShape merge(ItemShape s1, ItemShape s2) {
        SubShape[] subs1 = s1.getSubShapes(ItemShape.Layer.one);
        SubShape[] subs2 = s2.getSubShapes(ItemShape.Layer.one);
        Color[] cols1 = s1.getColors(ItemShape.Layer.one);
        Color[] cols2 = s2.getColors(ItemShape.Layer.one);

        SubShape[] resultSubs = new SubShape[4];
        Color[] resultCols = new Color[4];

        for (int i = 0; i < 4; i++) {
            if (subs2[i] != SubShape.None && subs2[i] != null) {
                resultSubs[i] = subs2[i];
                resultCols[i] = cols2[i];
            } else {
                resultSubs[i] = subs1[i];
                resultCols[i] = cols1[i];
            }
        }
        return ItemShape.fromArrays(resultSubs, resultCols);
    }
}