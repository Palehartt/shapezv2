package modele.plateau;

import modele.item.*;

import java.util.List;

public class Peintre extends Machine {

    private modele.item.ItemShape pendingShape = null;
    private modele.item.Color pendingColor = null;

    private modele.item.Item item1 = null;
    private modele.item.Item item2 = null;

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
                ports.add(new Port(Direction.West, Port.Type.Entree, c));
                if (esclave != null)
                    ports.add(new Port(Direction.North, Port.Type.Entree, esclave));
                ports.add(new Port(Direction.East, Port.Type.Sortie, esclave));
            }
            case East -> {
                ports.add(new Port(Direction.North, Port.Type.Entree, c));
                if (esclave != null)
                    ports.add(new Port(Direction.East, Port.Type.Entree, esclave));
                ports.add(new Port(Direction.South, Port.Type.Sortie, esclave));
            }
            case South -> {
                ports.add(new Port(Direction.East, Port.Type.Entree, c));
                if (esclave != null)
                    ports.add(new Port(Direction.South, Port.Type.Entree, esclave));
                ports.add(new Port(Direction.West, Port.Type.Sortie, esclave));
            }
            case West -> {
                ports.add(new Port(Direction.South, Port.Type.Entree, c));
                if (esclave != null)
                    ports.add(new Port(Direction.West, Port.Type.Entree, esclave));
                ports.add(new Port(Direction.North, Port.Type.Sortie, esclave));
            }
        }
        System.out.println("ADDING PORT → total: " + ports.size());
    }

    @Override
    public void work() {
        /*System.out.println("TOTAL PORTS: " + ports.size());
        System.out.println("Nb ports entrée: " + getPortsEntree().size());*/
        List<Port> portsEntree = getPortsEntree();

        if (item1 == null) {
            item1 = collecterEntrees(portsEntree.get(0));
            ;
            if (item1 instanceof ItemShape) {
                if (pendingShape == null) {
                    pendingShape = ((ItemShape) item1);
                    System.out.println("Item " + pendingShape.toString());
                }
            }
        }
        if (item2 == null) {
            item2 = collecterEntrees(portsEntree.get(1));
            ;
            if (item2 instanceof ItemColor) {
                modele.item.Color col = ((ItemColor) item2).getColor();
                if (pendingColor == null) {
                    pendingColor = col;
                    System.out.println("Color " + pendingColor.toString());
                }
            }
        }
    }

    @Override
    public void send() {
        // Si on a les deux couleurs, on mélange
        if (pendingShape != null && pendingColor != null) {
            System.out.println("Color 1 " + pendingColor.toString() + "Forme " + pendingShape.toString());
            pendingShape.applyColor(pendingColor);
            for (Port port : getPortsSortie()) {
                Case next = port.getCaseVoisine();
                if (next != null) {
                    deposerDans(next, pendingShape);
                    break; // un seul output
                }
            }
            current.clear();
            current.add(pendingShape);
            item1 = null;
            item2 = null;
            pendingShape = null;
            pendingColor = null;
        }
    }

    /*@Override
    public void send() {
        if (current.isEmpty()) return;

        // Sortie unique vers le bas (direction d)
        Case next = c.plateau.getCase(c, d);
        if (next != null && next.getMachine() != null) {
            next.getMachine().current.add(current.removeFirst());
        }
    }*/
}