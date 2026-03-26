package vuecontroleur;

import modele.item.ItemShape;
import modele.item.SubShape;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private Image imgBackground;
    private Image imgFront;
    private ItemShape shape;


    public void setShape(ItemShape _shape) {
        shape = _shape;
    }

    public void setBackground(Image _imgBackground) {
        imgBackground = _imgBackground;
    }

    public Image rotateImage(Image img, double angleDegrees) {
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        if (w < 0 || h < 0) return img; // safeguard si image non chargée

        BufferedImage rotated = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();

        // activer l'interpolation pour lisser la rotation
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // rotation autour du centre
        g2d.rotate(Math.toRadians(angleDegrees), w / 2.0, h / 2.0);

        // dessiner l'image
        g2d.drawImage(img, 0, 0, null);

        g2d.dispose();
        return rotated;
    }

    public void setFront(Image _imgFront) {
        imgFront = _imgFront;
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        final int bordure= 1;
        final int xBack = bordure;
        final int yBack = bordure;
        final int widthBack = getWidth() - bordure*2;
        final int heigthBack = getHeight() - bordure*2;

        final int subPartWidth = widthBack / 4;
        final int subPartHeigth = heigthBack / 4;

        final int xFront = bordure + subPartWidth;
        final int yFront = bordure + subPartHeigth;
        final int widthFront = subPartWidth*2;
        final int heigthFront = subPartHeigth*2;


        // cadre
        g.drawRoundRect(bordure, bordure, widthBack, heigthBack, bordure, bordure);


        if (imgBackground != null) {

            g.drawImage(imgBackground, xBack, yBack, widthBack, heigthBack, this);
        }

        if (imgFront != null) {
            g.drawImage(imgFront, xFront, yFront, widthFront, heigthFront, this);
        }


        if (shape != null) {

            // TODO autres layers
            SubShape[] tabS = shape.getSubShapes(ItemShape.Layer.one);
            modele.item.Color[] tabC = shape.getColors(ItemShape.Layer.one);

            for (int i = 0; i < 4; i++) {

                    SubShape ss = tabS[i];

                    if (ss != SubShape.None) {

                        switch (tabC[i]) {
                            case modele.item.Color.Red:
                                g.setColor(Color.RED);
                                break;
                            case modele.item.Color.White:
                                g.setColor(Color.WHITE);
                                break;
                            // TODO autres couleurs
                        }

                        int x = xFront + (widthFront / 2) * ((i >> 1) ^ 1);
                        int y = yFront + (heigthFront / 2) * ((i & 1) ^ ((i >> 1) & 1));
                        int w = widthFront / 2;
                        int h = heigthFront / 2;
                        switch (ss) {
                            case SubShape.Carre:
                                g.fillRect(x, y, widthFront / 2, heigthFront / 2);
                                break;

                            case SubShape.Circle:
                                g.fillOval(x, y, widthFront / 2, heigthFront / 2);
                                break;

                            case SubShape.QuartCircleTopRight:
                                // Cercle décalé à gauche : seul le quart droit-haut est visible
                                g.fillArc(x - w, y, w * 2, h * 2, 0, 90);
                                break;

                            case SubShape.QuartCircleTopLeft:
                                // Cercle décalé à droite : seul le quart gauche-haut est visible
                                g.fillArc(x, y, w * 2, h * 2, 90, 90);
                                break;

                            case SubShape.QuartCircleBottomLeft:
                                // Cercle décalé en haut à droite
                                g.fillArc(x, y - h, w * 2, h * 2, 180, 90);
                                break;

                            case SubShape.QuartCircleBottomRight:
                                // Cercle décalé en haut à gauche
                                g.fillArc(x - w, y - h, w * 2, h * 2, 270, 90);
                                break;
                        }
                    }
                }
            }

        }



    }





