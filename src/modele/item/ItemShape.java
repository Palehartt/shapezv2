package modele.item;

import modele.plateau.Direction;

public class ItemShape extends Item {
    private SubShape[] tabSubShapes;
    private Color[] tabColors;
    public enum Layer {one, two, three};

    public void applyColor(Color c) {
        for (int i = 0; i < tabColors.length; i++) {
            if (tabColors[i] != null) {
                tabColors[i] = c;
            }
        }
    }

    public SubShape[] getSubShapes(Layer l) {
        switch(l) {
            case one : return new SubShape[] {tabSubShapes[0], tabSubShapes[1], tabSubShapes[2], tabSubShapes[3]};

            // TODO two & three
            default:
                throw new IllegalStateException("Unexpected value: " + l);
        }
    }

    public Color[] getColors(Layer l) {
        switch(l) {
            case one : return new Color[] {tabColors[0], tabColors[1], tabColors[2], tabColors[3]};
            // TODO two & three
            default:
                throw new IllegalStateException("Unexpected value: " + l);
        }
    }

    /**;
     * Initialisation des formes par chaîne de caractères
     * @param str : codage : (sous forme + couleur ) * (haut-droit, bas-droit, bas-gauche, haut-gauche) * 3 Layers
     *            str.length multiple de 4
     */
    public ItemShape(String str) {

        tabSubShapes = new SubShape[4];
        tabColors = new Color[4];

        for (int i = 0; i < 4; i++) { // fait uniquement pour la première couche
            switch (str.charAt(i*2)) {
                case 'C' : tabSubShapes[i] = SubShape.Carre;
                    break;
                case 'R' :
                    switch (i) {
                        case 0:
                            tabSubShapes[i] = SubShape.QuartCircleTopRight;
                            break;
                        case 1:
                            tabSubShapes[i] = SubShape.QuartCircleBottomRight;
                            break;
                        case 2:
                            tabSubShapes[i] = SubShape.QuartCircleBottomLeft;
                            break;
                        case 3:
                            tabSubShapes[i] = SubShape.QuartCircleTopLeft;
                            break;
                        default:
                    }
                    break;
                case '-' : tabSubShapes[i] = SubShape.None;break;
                default:
                    throw new IllegalStateException("Unexpected value: " + str.charAt(i));
            }

            switch (str.charAt((i*2 + 1))) {
                case 'r' : tabColors[i] = Color.Red; break;
                case 'g':  tabColors[i] = Color.Green; break;
                case 'b':  tabColors[i] = Color.Blue; break;
                case 'y':  tabColors[i] = Color.Yellow; break;
                case 'p':  tabColors[i] = Color.Purple; break;
                case 'c':  tabColors[i] = Color.Cyan; break;
                case 'w' : tabColors[i] = Color.White; break;
                case '-' : tabColors[i] = null; break;
                default:
                    throw new IllegalStateException("Unexpected value: " + str.charAt((i + 1)*2));
            }

        }
    }

    public static ItemShape fromArrays(SubShape[] shapes, Color[] colors) {
        return new ItemShape(shapes, colors);
    }

    private ItemShape(SubShape[] shapes, Color[] colors) {
        this.tabSubShapes = shapes;
        this.tabColors = colors;
    }

    public ItemShape[] cut(Direction d) {
        ItemShape half1, half2;

        switch (d) {
            case North -> {
                // Coupe verticale : droite (0,1) et gauche (2,3)
                half1 = new ItemShape(
                        new SubShape[]{tabSubShapes[0], tabSubShapes[1], SubShape.None, SubShape.None},
                        new Color[]   {tabColors[0],    tabColors[1],    null,          null}
                );
                half2 = new ItemShape(
                        new SubShape[]{SubShape.None, SubShape.None, tabSubShapes[2], tabSubShapes[3]},
                        new Color[]   {null,          null,          tabColors[2],    tabColors[3]}
                );
            }
            case South -> {
                // Coupe verticale : droite (0,1) et gauche (2,3)
                half2 = new ItemShape(
                        new SubShape[]{tabSubShapes[0], tabSubShapes[1], SubShape.None, SubShape.None},
                        new Color[]   {tabColors[0],    tabColors[1],    null,          null}
                );
                half1 = new ItemShape(
                        new SubShape[]{SubShape.None, SubShape.None, tabSubShapes[2], tabSubShapes[3]},
                        new Color[]   {null,          null,          tabColors[2],    tabColors[3]}
                );
            }
            case West -> {
                // Coupe horizontale : haut (0,3) et bas (1,2)
                half1 = new ItemShape(
                        new SubShape[]{tabSubShapes[0], SubShape.None, SubShape.None, tabSubShapes[3]},
                        new Color[]   {tabColors[0],    null,          null,          tabColors[3]}
                );
                half2 = new ItemShape(
                        new SubShape[]{SubShape.None, tabSubShapes[1], tabSubShapes[2], SubShape.None},
                        new Color[]   {null,          tabColors[1],    tabColors[2],    null}
                );
            }
            case East -> {
                // Coupe horizontale : haut (0,3) et bas (1,2)
                half2 = new ItemShape(
                        new SubShape[]{tabSubShapes[0], SubShape.None, SubShape.None, tabSubShapes[3]},
                        new Color[]   {tabColors[0],    null,          null,          tabColors[3]}
                );
                half1 = new ItemShape(
                        new SubShape[]{SubShape.None, tabSubShapes[1], tabSubShapes[2], SubShape.None},
                        new Color[]   {null,          tabColors[1],    tabColors[2],    null}
                );
            }
            default -> throw new IllegalStateException("Direction inconnue : " + d);
        }
        return new ItemShape[]{half1, half2};
    }


    // TODO : écrire l'ensemble des fonctions de transformation souhaitées, définir les paramètres éventuels (sens, axe, etc.)
    public void rotate() {

        SubShape[] bufferSubShapes = new SubShape[4];
        bufferSubShapes[0] = tabSubShapes[3];
        bufferSubShapes [1] = tabSubShapes[0];
        bufferSubShapes [2] = tabSubShapes[1];
        bufferSubShapes [3] = tabSubShapes[2];

        Color[] bufferColors = new Color[4];
        bufferColors[0] = tabColors[3];
        bufferColors [1] = tabColors[0];
        bufferColors [2] = tabColors[1];
        bufferColors [3] = tabColors[2];

        tabSubShapes = bufferSubShapes;
        tabColors = bufferColors;


    }

    public void rotateAntiHoraire() {
        SubShape[] bufferSubShapes = new SubShape[4];
        bufferSubShapes[0] = tabSubShapes[1];
        bufferSubShapes[1] = tabSubShapes[2];
        bufferSubShapes[2] = tabSubShapes[3];
        bufferSubShapes[3] = tabSubShapes[0];

        Color[] bufferColors = new Color[4];
        bufferColors[0] = tabColors[1];
        bufferColors[1] = tabColors[2];
        bufferColors[2] = tabColors[3];
        bufferColors[3] = tabColors[0];

        tabSubShapes = bufferSubShapes;
        tabColors = bufferColors;
    }

    public void stack(ItemShape ShapeSup) { // ShapeSup est empilé sur this

    }

    public ItemShape Cut() { // this et l'objet retourné correpondent au deux sorties
        return null;
    }

    public void Color(Color c) {

    }

}
