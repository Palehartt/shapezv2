package vuecontroleur;

import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;


import modele.item.*;
import modele.item.Color;
import modele.jeu.Jeu;
import modele.plateau.*;


/** Cette classe a deux fonctions :
 *  (1) Vue : proposer une représentation graphique de l'application (cases graphiques, etc.)
 *  (2) Controleur : écouter les évènements clavier et déclencher le traitement adapté sur le modèle
 *
 */
public class VueControleur extends JFrame implements Observer {
    private Plateau plateau; // référence sur une classe de modèle : permet d'accéder aux données du modèle pour le rafraichissement, permet de communiquer les actions clavier (ou souris)
    private Jeu jeu;
    private final int sizeX; // taille de la grille affichée
    private final int sizeY;
    private static final int pxCase = 82; // nombre de pixel par case
    // icones affichées dans la grille
    private Image icoRouge;
    private Image icoTapisHaut;
    private Image icoTapisDroite;
    private Image icoTapisGauche;
    private Image icoPoubelle;
    private Image icoMine;
    private Image icoLivraison;

    private Image icoGisementCarre;
    private Image icoGisementCercle;

    private Image icoRed;
    private Image icoBlue;
    private Image icoGreen;
    private Image icoYellow;
    private Image icoPurple;


    private ObjetChoisi objetChoisi;
    private Direction directionObjetChoisi;

    private JComponent grilleIP;

    private boolean mousePressed = false; // permet de mémoriser l'état de la souris

    private ImagePanel[][] tabIP; // cases graphique (au moment du rafraichissement, chaque case va être associée à une icône background et front, suivant ce qui est présent dans le modèle)


    public VueControleur(Jeu _jeu) {
        jeu = _jeu;
        plateau = jeu.getPlateau();
        sizeX = plateau.SIZE_X;
        sizeY = plateau.SIZE_Y;

        chargerLesIcones();
        placerLesComposantsGraphiques();

        plateau.addObserver(this);

        mettreAJourAffichage();

    }


    private void chargerLesIcones() {

        icoRouge = new ImageIcon("./data/sprites/colors/blue.png").getImage();
        icoTapisHaut = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();
        icoTapisGauche = new ImageIcon("./data/sprites/buildings/belt_left.png").getImage();
        icoTapisDroite = new ImageIcon("./data/sprites/buildings/belt_right.png").getImage();
        icoGisementCarre = new ImageIcon("./data/sprites/shapes/Carre.png").getImage();
        icoGisementCercle  = new ImageIcon("./data/sprites/shapes/Cercle.png").getImage();

        icoPoubelle = new ImageIcon("./data/sprites/buildings/trash.png").getImage();
        icoMine = new ImageIcon("./data/sprites/buildings/miner.png").getImage();

        icoRed = new ImageIcon("./data/sprites/colors/red.png").getImage();
        icoBlue = new ImageIcon("./data/sprites/colors/blue.png").getImage();
        icoGreen = new ImageIcon("./data/sprites/colors/green.png").getImage();
        icoPurple = new ImageIcon("./data/sprites/colors/purple.png").getImage();
        icoYellow = new ImageIcon("./data/sprites/colors/yellow.png").getImage();

        icoLivraison = new ImageIcon("./data/sprites/buildings/goal_acceptor.png").getImage();

    }



    private void placerLesComposantsGraphiques() {
        setTitle("ShapeCraft");
        setResizable(true);
        setSize(sizeX * pxCase, sizeX * pxCase);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // permet de terminer l'application à la fermeture de la fenêtre

        grilleIP = new JPanel(new GridLayout(sizeY, sizeX)); // grilleJLabels va contenir les cases graphiques et les positionner sous la forme d'une grille

        InputMap im = grilleIP.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = grilleIP.getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), "left");
        am.put("left", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                directionObjetChoisi = Direction.West;

            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), "right");
        am.put("right", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                directionObjetChoisi = Direction.East;
//                mettreAJourAffichage();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0), "up");
        am.put("up", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                directionObjetChoisi = Direction.North;
//                mettreAJourAffichage();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0), "down");
        am.put("down", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                directionObjetChoisi = Direction.South;
//                mettreAJourAffichage();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), "tapis");
        am.put("tapis", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                objetChoisi = ObjetChoisi.Tapis;
//                mettreAJourAffichage();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "mine");
        am.put("mine", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                objetChoisi = ObjetChoisi.Mine;
//                mettreAJourAffichage();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), "poubelle");
        am.put("poubelle", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                objetChoisi = ObjetChoisi.Poubelle;
//                mettreAJourAffichage();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "couteau");
        am.put("couteau", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                objetChoisi = ObjetChoisi.Couteau;
//                mettreAJourAffichage();
            }
        });

        tabIP = new ImagePanel[sizeX][sizeY];


        for (int y = 0; y < sizeY; y++) {
            for (int x = 0; x < sizeX; x++) {
                ImagePanel iP = new ImagePanel();

                tabIP[x][y] = iP; // on conserve les cases graphiques dans tabJLabel pour avoir un accès pratique à celles-ci (voir mettreAJourAffichage() )

                final int xx = x; // permet de compiler la classe anonyme ci-dessous
                final int yy = y;
                // écouteur de clics
                iP.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        mousePressed = false;
                        jeu.press(xx, yy, objetChoisi, directionObjetChoisi);
                        System.out.println(xx + "-" + yy);
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (mousePressed) {
                            jeu.slide(xx, yy);
                        }
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        mousePressed = true;
                        jeu.press(xx, yy, objetChoisi, directionObjetChoisi);
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        mousePressed = false;

                    }
                });


                grilleIP.add(iP);
            }
        }
        add(grilleIP);
    }

   private void mettreAJourAffichage() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                tabIP[x][y].setBackground((Image) null);
                tabIP[x][y].setFront(null);
                tabIP[x][y].setShape(null);

                Case c = plateau.getCases()[x][y];
                Machine m = c.getMachine();
                Item g = c.getGisement();

                if (g != null) {
                    if (g instanceof ItemGisement) {
                        if  (((ItemGisement) g).getSubShape().equals(SubShape.Carre)) {
                            tabIP[x][y].setBackground(icoGisementCarre);
                        } else if (((ItemGisement) g).getSubShape().equals(SubShape.Circle)) {
                            tabIP[x][y].setBackground(icoGisementCercle);
                        } else if (((ItemGisement) g).getColor().equals(Color.Red)) {
                            tabIP[x][y].setBackground(icoRed);
                        }
                    }
                }

                if (m != null) {
                    if (m instanceof Tapis tapis) {
                        Direction d = tapis.getDirection();
                        double angle = switch (d) {
                            case North -> 0;
                            case East  -> 90;
                            case South -> 180;
                            case West  -> 270;
                        };

                        Image baseSprite = switch (tapis.getType()) {
                            case Droit         -> icoTapisHaut;
                            case VirageGauche  -> icoTapisGauche;
                            case ViragedDroite -> icoTapisDroite;
                        };

                        tabIP[x][y].setBackground(tabIP[x][y].rotateImage(baseSprite, angle));

                    } else if (m instanceof Poubelle) {
                        tabIP[x][y].setBackground(icoPoubelle);

                    } else if (m instanceof Mine) {
                        Direction d = m.getDirection();
                        double angle = switch (d) {
                            case North -> 0;
                            case East  -> 90;
                            case South -> 180;
                            case West  -> 270;
                        };
                        tabIP[x][y].setBackground(tabIP[x][y].rotateImage(icoMine, angle));
                    } else if  (m instanceof Livraison) {
                        Direction d = m.getDirection();
                        double angle = switch (d) {
                            case North -> 0;
                            case East  -> 90;
                            case South -> 180;
                            case West  -> 270;
                        };
                        tabIP[x][y].setBackground(tabIP[x][y].rotateImage(icoLivraison, angle));
                    }

                    Item current = m.getCurrent();

                    if (current instanceof ItemShape) {
                        tabIP[x][y].setShape((ItemShape) current);
                    }
                    if (current instanceof ItemColor) {
                        // TODO : placer l'icône couleur appropriée
                    }
                }
            }
        }
        grilleIP.repaint();
    }

    @Override
    public void update(Observable o, Object arg) {

        SwingUtilities.invokeLater(new Runnable() {
                    @Override
                    public void run() {
                        mettreAJourAffichage();
                    }
                }); 

    }
}
