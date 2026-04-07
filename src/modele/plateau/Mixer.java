package modele.plateau;

import modele.item.*;

import java.util.List;

public class Mixer extends Machine {

    private modele.item.Color pendingColor1 = null;
    private modele.item.Color pendingColor2 = null;

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
        // Lire depuis la case principale (entrée 1)
        if (!current.isEmpty() && current.getFirst() instanceof ItemColor) {
            modele.item.Color c = ((ItemColor) current.removeFirst()).getColor();
            if (pendingColor1 == null) {
                pendingColor1 = c;
                System.out.println("Premier" + pendingColor1.toString());
            }
        }

        // Lire depuis la case adjacente (entrée 2, sur le côté droit)
        Direction droite = switch (d) {
            case North -> Direction.East;
            case East  -> Direction.South;
            case South -> Direction.West;
            case West  -> Direction.North;
        };

        Case caseEntree2 = c.plateau.getCase(c, droite);

        if (caseEntree2 != null && caseEntree2.getMachineEsclave() != null) {
            Machine m = caseEntree2.getMachineEsclave();
            if (!m.current.isEmpty() && m.current.getFirst() instanceof ItemColor) {
                System.out.println("Entrée sur la case esclave");
                modele.item.Color col = ((ItemColor) m.current.removeFirst()).getColor();
                if (pendingColor2 == null) {
                    pendingColor2 = col;
                    System.out.println("Deuxième " + pendingColor2.toString());
                }
            }
        }

        // Si on a les deux couleurs, on mélange
        if (pendingColor1 != null && pendingColor2 != null) {
            //System.out.println(pendingColor1.toString() + " " + pendingColor2.toString());
            modele.item.Color result = melanger(pendingColor1, pendingColor2);
            if (result != null) {
                current.add(new ItemColor(result));
            }
            pendingColor1 = null;
            pendingColor2 = null;
        }
    }

    @Override
    public void send() {
        if (current.isEmpty()) return;

        // Sortie unique vers le bas (direction d)
        Case next = c.plateau.getCase(c, d);
        if (next != null && next.getMachine() != null) {
            next.getMachine().current.add(current.removeFirst());
        }
    }

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