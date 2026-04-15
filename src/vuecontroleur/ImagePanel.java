package vuecontroleur;

import modele.item.ItemShape;
import modele.item.SubShape;
import modele.item.ItemColor;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {
    private Image imgBackground;
    private Image imgFront;
    private ItemShape shape;
    private ItemShape gissement;
    private modele.item.Color color;

    private int spriteOffsetX = 0;
    private int spriteOffsetY = 0;
    private int spriteTotalW = 1;
    private int spriteTotalH = 1;
    private boolean isHub = false;
    private int hubOffsetX, hubOffsetY, hubTotalW, hubTotalH;
    private String hubNiveau;
    private int hubCompteur;
    private ItemShape hubForme;

    public void setHubInfo(int offX, int offY, int totalW, int totalH,
                           String niveau, ItemShape forme, int compteur) {
        isHub = true;
        hubOffsetX  = offX;
        hubOffsetY  = offY;
        hubTotalW   = totalW;
        hubTotalH   = totalH;
        hubNiveau   = niveau;
        hubForme    = forme;
        hubCompteur = compteur;
    }

    public void resetHubInfo() {
        isHub = false;
    }

    public ImagePanel() {
        setOpaque(true);
    }

    public void setShape(ItemShape _shape) {
        shape = _shape;
    }

    public void setBackground(Image img) {
        imgBackground = img;
        spriteOffsetX = 0;
        spriteOffsetY = 0;
        spriteTotalW = 1;
        spriteTotalH = 1;
    }

    public void setGissement(ItemShape _gissement) {
        gissement = _gissement;
        color = null;
    }

    public void setGissement(modele.item.Color _color) {
        gissement = null;
        color = _color; }

    public void resetGissement() {
        gissement = null;
        color = null;
    }

    public void setItemColor(modele.item.Color _color) {
        color = _color;
    }

    public void resetItemColor() {
        color = null;
    }

    public Image rotateImage(Image img, double angleDegrees) {
        int w = img.getWidth(null);
        int h = img.getHeight(null);
        if (w < 0 || h < 0) return img;

        // Pour une rotation à 90° ou 270°, les dimensions s'inversent
        boolean swap = (Math.abs(angleDegrees) % 180 != 0);
        int newW = swap ? h : w;
        int newH = swap ? w : h;

        BufferedImage rotated = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = rotated.createGraphics();

        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Translation pour recentrer après rotation
        g2d.translate(newW / 2.0, newH / 2.0);
        g2d.rotate(Math.toRadians(angleDegrees));
        g2d.drawImage(img, -w / 2, -h / 2, null);

        g2d.dispose();
        return rotated;
    }

    public void setBackground(Image img, int offsetX, int offsetY, int totalW, int totalH) {
        imgBackground = img;
        spriteOffsetX = offsetX;
        spriteOffsetY = offsetY;
        spriteTotalW = totalW;
        spriteTotalH = totalH;
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

        if(gissement != null) {
            g.setColor(new Color(165, 165, 176, 100)); // plus opaque pour être visible
            g.fillRect(xBack, yBack, widthBack, heigthBack);
        }

        if(color != null) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            switch(color) {
                case Red    -> g.setColor(new Color(255, 80,  80, 100));
                case Blue   -> g.setColor(new Color(80,  80,  255, 100));
                case Green  -> g.setColor(new java.awt.Color(80,  200, 80, 100));
                case Yellow -> g.setColor(new java.awt.Color(255, 230, 50, 100));
                case Purple -> g.setColor(new java.awt.Color(180, 80,  255, 100));
                default     -> g.setColor(new java.awt.Color(200, 200, 200, 100));
            }
            g2d.fillRect(xBack, yBack, widthBack, heigthBack);
            //g.fillRect(xBack, yBack, widthBack, heigthBack);
        }

        if (imgBackground != null) {
            int srcX = (imgBackground.getWidth(null) * spriteOffsetX) / spriteTotalW;
            int srcY = (imgBackground.getHeight(null) * spriteOffsetY) / spriteTotalH;
            int srcW = imgBackground.getWidth(null) / spriteTotalW;
            int srcH = imgBackground.getHeight(null) / spriteTotalH;

            g.drawImage(imgBackground,
                    xBack, yBack, xBack + widthBack, yBack + heigthBack,  // destination
                    srcX, srcY, srcX + srcW, srcY + srcH,                 // source
                    this);
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
                            case Green:
                                g.setColor(Color.GREEN);
                                break;
                            case Blue:
                                g.setColor(Color.BLUE);
                                break;
                            case Yellow:
                                g.setColor(Color.YELLOW);
                                break;
                            case Purple:
                                g.setColor(new Color(128, 0, 128));
                                break;
                            case Cyan:
                                g.setColor(Color.CYAN);
                                break;
                            case White:
                                g.setColor(Color.WHITE);
                                break;
                            case None:
                                g.setColor(Color.BLACK);
                                break;
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
            if (isHub) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                int hubPixelW = getWidth()  * hubTotalW;
                int hubPixelH = getHeight() * hubTotalH;
                int hubOriginX = -hubOffsetX * getWidth();
                int hubOriginY = -hubOffsetY * getHeight();
                int cx = hubOriginX + hubPixelW / 2;
                int cy = hubOriginY + hubPixelH / 2;

                // --- Niveau ---
                g2d.setFont(new Font("Arial", Font.BOLD, 20));
                g2d.setColor(java.awt.Color.BLACK);
                FontMetrics fm = g2d.getFontMetrics();
                String niveauStr = hubNiveau;
                g2d.drawString(niveauStr, cx - fm.stringWidth(niveauStr)/2 + 1, cy/2 - 10);
                g2d.setColor(java.awt.Color.WHITE);
                g2d.drawString(niveauStr, cx - fm.stringWidth(niveauStr)/2 + 1, cy/2 - 12);

                // --- Compteur ---
                g2d.setFont(new Font("Arial", Font.PLAIN, 20));
                fm = g2d.getFontMetrics();
                String compteurStr = "x" + hubCompteur;
                g2d.setColor(java.awt.Color.BLACK);
                g2d.drawString(compteurStr, cx - fm.stringWidth(compteurStr)/2 + 1, cy + 86);
                g2d.setColor(java.awt.Color.WHITE);
                g2d.drawString(compteurStr, cx - fm.stringWidth(compteurStr)/2 + 1, cy + 85);

                // --- Forme cible (SubShapes) centrée dans le hub ---
                if (hubForme != null) {
                    SubShape[] tabS = hubForme.getSubShapes(ItemShape.Layer.one);
                    modele.item.Color[] tabC = hubForme.getColors(ItemShape.Layer.one);

                    // Taille de la forme affichée (ajustez selon vos goûts)
                    int formeSize = Math.min(hubPixelW, hubPixelH) / 2;
                    int halfCell  = formeSize / 2;

                    // Coin haut-gauche de la zone forme, centrée dans le hub
                    int fx = cx - formeSize / 2;
                    int fy = cy - formeSize / 2;

                    for (int i = 0; i < 4; i++) {
                        SubShape ss = tabS[i];
                        if (ss == SubShape.None) continue;

                        // Même mapping que dans le paintComponent existant :
                        // i=0 → haut-gauche, i=1 → bas-gauche, i=2 → bas-droite, i=3 → haut-droite
                        int cellX = fx + (halfCell) * ((i >> 1) ^ 1);
                        int cellY = fy + (halfCell) * ((i & 1) ^ ((i >> 1) & 1));
                        int w = halfCell;
                        int h = halfCell;

                        // Couleur
                        if (tabC[i] != null) {
                            switch (tabC[i]) {
                                case Red    -> g2d.setColor(java.awt.Color.RED);
                                case Blue   -> g2d.setColor(java.awt.Color.BLUE);
                                case Green  -> g2d.setColor(java.awt.Color.GREEN);
                                case Yellow -> g2d.setColor(java.awt.Color.YELLOW);
                                case Purple -> g2d.setColor(new java.awt.Color(232, 2, 232));
                                case Cyan   -> g2d.setColor(java.awt.Color.CYAN);
                                case White  -> g2d.setColor(java.awt.Color.WHITE);
                                default     -> g2d.setColor(java.awt.Color.LIGHT_GRAY);
                            }
                        } else {
                            g2d.setColor(java.awt.Color.LIGHT_GRAY);
                        }

                        switch (ss) {
                            case Carre ->
                                    g2d.fillRect(cellX, cellY, w, h);
                            case Circle ->
                                    g2d.fillOval(cellX, cellY, w, h);
                            case QuartCircleTopRight ->
                                    g2d.fillArc(cellX - w, cellY, w * 2, h * 2, 0, 90);
                            case QuartCircleTopLeft ->
                                    g2d.fillArc(cellX, cellY, w * 2, h * 2, 90, 90);
                            case QuartCircleBottomLeft ->
                                    g2d.fillArc(cellX, cellY - h, w * 2, h * 2, 180, 90);
                            case QuartCircleBottomRight ->
                                    g2d.fillArc(cellX - w, cellY - h, w * 2, h * 2, 270, 90);
                        }
                    }
                }
            }
        }



    }





