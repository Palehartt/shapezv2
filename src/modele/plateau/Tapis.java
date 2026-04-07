package modele.plateau;

import modele.item.Item;

public class Tapis extends Machine {

    public enum TapisType { Droit, VirageGauche, ViragedDroite }

    private TapisType type;

    public Tapis() {
        super();
        this.type = TapisType.Droit;
    }

    public Tapis(Direction dir) {
        super();
        this.d = dir;
        this.type = TapisType.Droit;
    }

    public Tapis(Direction dir, TapisType type) {
        super();
        this.d = dir;
        this.type = type;
    }

    public TapisType getType() {
        return type;
    }

    public Direction getOutputDirection() {
        return switch (type) {
            case Droit -> d; // tout droit, direction inchangée
            case VirageGauche -> switch (d) {
                case North -> Direction.West;
                case West -> Direction.South;
                case South -> Direction.East;
                case East -> Direction.North;
            };
            case ViragedDroite -> switch (d) {
                case North -> Direction.East;
                case East -> Direction.South;
                case South -> Direction.West;
                case West -> Direction.North;
            };
        };
    }
}