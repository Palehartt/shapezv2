package modele.plateau;

public class Port {
    public enum Type { Entree, Sortie }

    private Direction direction;  // côté exposé vers l'extérieur
    private Type type;
    private Case caseSource;      // case de la machine depuis laquelle ce port est exposé

    public Port(Direction direction, Type type, Case caseSource) {
        this.direction = direction;
        this.type = type;
        this.caseSource = caseSource;
    }

    public Direction getDirection() { return direction; }
    public Type getType() { return type; }
    public Case getCaseSource() { return caseSource; }

    // Retourne la case extérieure visée par ce port
    public Case getCaseVoisine() {
        if (caseSource == null || caseSource.plateau == null) return null;
        return caseSource.plateau.getCase(caseSource, direction);
    }
}