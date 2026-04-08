package modele.plateau;

import modele.item.*;

import java.util.List;

public class Mixer extends Machine {

    private modele.item.Color pendingColor1 = null;
    private modele.item.Color pendingColor2 = null;

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
        System.out.println("Case Esclave : " + casesOccupées.size());
        Case esclave = casesOccupées.isEmpty() ? null : casesOccupées.get(0);
        System.out.println("ADDING PORT IN PROGRESS : " + ports.size());

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
        System.out.println("ADDING PORT → total: " + ports.size());
    }

    @Override
    public void work() {
        /*System.out.println("TOTAL PORTS: " + ports.size());
        System.out.println("Nb ports entrée: " + getPortsEntree().size());*/
        List<Port> portsEntree = getPortsEntree();

        // Collecter les items depuis les cases d'entrée via les ports
        // Trier les items reçus
        /*while (!current.isEmpty()) {
            Item item = current.getFirst();
            current.removeFirst();

            if (item instanceof ItemColor) {
                modele.item.Color col = ((ItemColor) item).getColor();
                if (pendingColor1 == null) {
                    pendingColor1 = col;
                } else if (pendingColor2 == null) {
                    pendingColor2 = col;
                }
            }
        }*/
        /*if(item1 == null) item1 = collecterEntrees(portsEntree.get(0));;
        if(item2 == null) item2 = collecterEntrees(portsEntree.get(1));;*/

        if (item1 == null) {
            item1 = collecterEntrees(portsEntree.get(0));
            ;
            if (item1 instanceof ItemColor) {
                modele.item.Color col = ((ItemColor) item1).getColor();
                if (pendingColor1 == null) {
                    pendingColor1 = col;
                    System.out.println("Color 1 " + pendingColor1.toString());
                }
            }
        } else if (item2 == null) {
            item2 = collecterEntrees(portsEntree.get(1));
            ;
            if (item2 instanceof ItemColor) {
                modele.item.Color col = ((ItemColor) item2).getColor();
                if (pendingColor2 == null) {
                    pendingColor2 = col;
                    System.out.println("Color 2 " + pendingColor2.toString());
                }
            }
        }
    }

    @Override
    public void send() {
        // Si on a les deux couleurs, on mélange
        if (pendingColor1 != null && pendingColor2 != null) {
            System.out.println("Color 1 " + pendingColor1.toString() + "Color 2 " + pendingColor2.toString());
            modele.item.Color result = melanger(pendingColor1, pendingColor2);
            if (result != null) {
                Case next = c.plateau.getCase(c, d);
                if (next != null && next.getMachine() != null) {
                    next.getMachine().current.add(new ItemColor(result));
                }
                System.out.println("Nouvelle Couleur");
            }
            item1 = null;
            item2 = null;
            pendingColor1 = null;
            pendingColor2 = null;
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

    private modele.item.Color melanger(modele.item.Color c1, modele.item.Color c2) {
        if (c1 == c2) return c1;

        if (match(c1, c2, modele.item.Color.Red,    modele.item.Color.Blue))   return modele.item.Color.Purple;
        if (match(c1, c2, modele.item.Color.Red,    modele.item.Color.Green))  return modele.item.Color.Yellow;
        if (match(c1, c2, modele.item.Color.Blue,   modele.item.Color.Green))  return modele.item.Color.Cyan;
        if (match(c1, c2, modele.item.Color.Red,    modele.item.Color.Yellow)) return modele.item.Color.Red;
        if (match(c1, c2, modele.item.Color.Red,    modele.item.Color.Cyan))   return modele.item.Color.White;
        if (match(c1, c2, modele.item.Color.Green,  modele.item.Color.Purple)) return modele.item.Color.White;
        if (match(c1, c2, modele.item.Color.Blue,   modele.item.Color.Yellow)) return modele.item.Color.White;
        if (match(c1, c2, modele.item.Color.Red,    modele.item.Color.Purple)) return modele.item.Color.Purple;
        if (match(c1, c2, modele.item.Color.Green,  modele.item.Color.Yellow)) return modele.item.Color.Yellow;
        if (match(c1, c2, modele.item.Color.Green,  modele.item.Color.Cyan))   return modele.item.Color.Cyan;
        if (match(c1, c2, modele.item.Color.Blue,   modele.item.Color.Cyan))   return modele.item.Color.Cyan;
        if (match(c1, c2, modele.item.Color.Blue,   modele.item.Color.Purple)) return modele.item.Color.Purple;
        if (match(c1, c2, modele.item.Color.Yellow, modele.item.Color.Cyan))   return modele.item.Color.Green;
        if (match(c1, c2, modele.item.Color.Yellow, modele.item.Color.Purple)) return modele.item.Color.White;
        if (match(c1, c2, modele.item.Color.Cyan,   modele.item.Color.Purple)) return modele.item.Color.Blue;

        if (c1 == modele.item.Color.White || c2 == modele.item.Color.White) return modele.item.Color.White;

        return null;
    }

    private boolean match(modele.item.Color a, modele.item.Color b,
                          modele.item.Color x, modele.item.Color y) {
        return (a == x && b == y) || (a == y && b == x);
    }
}