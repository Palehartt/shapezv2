package modele.item;

public class ItemGisement extends Item {
    private Color color;
    private SubShape subShape;

    public ItemGisement(SubShape subShape) {
        this.subShape = subShape;
        this.color = Color.None;
    }

    public ItemGisement(Color color) {
        this.color = color;
        this.subShape = SubShape.None;
    }

    public SubShape getSubShape() {
        return subShape;
    }

    public Color getColor() {
        return color;
    }

    public void setSubShape(SubShape subShape) {
        this.subShape = subShape;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
