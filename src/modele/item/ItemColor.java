package modele.item;

public class ItemColor extends Item {
    Color color;

    public void transform(Color add) { // faire varier la couleur suivant la couleur ajoutée

    }

    public Color getColor() { return color; }
    public ItemColor() {
    }
    public ItemColor(Color color) {
        this.color = color;
    }
}
