package vuecontroleur;

import java.awt.*;
import java.awt.Color;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;
import javax.swing.*;


import modele.item.*;
import modele.jeu.Jeu;
import modele.plateau.*;
import modele.plateau.Point;



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
    private static final int pxCase = 60; // nombre de pixel par case
    // icones affichées dans la grille
    private Image icoRouge;
    private Image icoTapisHaut;
    private Image icoTapisDroite;
    private Image icoTapisGauche;
    private Image icoPoubelle;
    private Image icoMine;
    private Image icoLivraison;
    private Image icoCiseaux;
    private Image icoHub;
    private Image icoPeintre;
    private Image icoMixer;
    private Image icoPivoteur;
    private Image icoAssembleur;

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

        objetChoisi = ObjetChoisi.Mine;
        directionObjetChoisi = Direction.North;

        placerLesComposantsGraphiques();

        plateau.addObserver(this);

        mettreAJourAffichage();

    }


    private void chargerLesIcones() {

        icoRouge = new ImageIcon("./data/sprites/colors/blue.png").getImage();
        icoTapisHaut = new ImageIcon("./data/sprites/buildings/belt_top.png").getImage();
        icoTapisGauche = new ImageIcon("./data/sprites/buildings/belt_left.png").getImage();
        icoTapisDroite = new ImageIcon("./data/sprites/buildings/belt_right.png").getImage();

        icoPoubelle = new ImageIcon("./data/sprites/buildings/trash.png").getImage();
        icoMine = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        icoCiseaux = new ImageIcon("./data/sprites/buildings/cutter.png").getImage();
        icoMine = new ImageIcon("./data/sprites/buildings/miner.png").getImage();
        icoHub = new ImageIcon("./data/sprites/buildings/hub.png").getImage();
        icoPivoteur =  new ImageIcon("./data/sprites/buildings/rotater.png").getImage();
        icoPeintre = new ImageIcon("./data/sprites/buildings/painter.png").getImage();
        icoMixer = new ImageIcon("./data/sprites/buildings/mixer.png").getImage();
        icoAssembleur = new ImageIcon("./data/sprites/buildings/stacker.png").getImage();

        icoRed = new ImageIcon("./data/sprites/colors/red.png").getImage();
        icoBlue = new ImageIcon("./data/sprites/colors/blue.png").getImage();
        icoGreen = new ImageIcon("./data/sprites/colors/green.png").getImage();
        icoPurple = new ImageIcon("./data/sprites/colors/purple.png").getImage();
        icoYellow = new ImageIcon("./data/sprites/colors/yellow.png").getImage();

        icoLivraison = new ImageIcon("./data/sprites/buildings/goal_acceptor.png").getImage();

    }



    private void placerLesComposantsGraphiques() {
        setTitle("ShapeCraft");
        setLayout(new BorderLayout());
        setResizable(true);
        setSize(sizeX * pxCase, sizeX * pxCase + 100);
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

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_I, 0), "mixer");
        am.put("mixer", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                objetChoisi = ObjetChoisi.Mixer;
//                mettreAJourAffichage();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_K, 0), "peintre");
        am.put("peintre", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                objetChoisi = ObjetChoisi.Peintre;
//                mettreAJourAffichage();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_V, 0), "pivoteur");
        am.put("pivoteur", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                objetChoisi = ObjetChoisi.Pivoteur;
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

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), "cutter");
        am.put("cutter", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                objetChoisi = ObjetChoisi.Ciseaux;
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

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_H, 0), "hub");
        am.put("hub", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                objetChoisi = ObjetChoisi.Hub;
//                mettreAJourAffichage();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "assembleur");
        am.put("assembleur", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                objetChoisi = ObjetChoisi.Assembleur;
//                mettreAJourAffichage();
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, 0), "accelerate");
        am.put("accelerate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jeu.setTick(200);
//               mettreAJourAffichage();
            }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT_PARENTHESIS, 0), "deccelerate");
        am.put("deccelerate", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                jeu.setTick(1000);
//               mettreAJourAffichage();
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
                        if (SwingUtilities.isRightMouseButton(e)) {
                            jeu.supprimer(xx, yy);
                        } else if (plateau.getCases()[xx][yy].getMachine() == null && plateau.getCases()[xx][yy].getMachineEsclave() == null) {
                            jeu.press(xx, yy, objetChoisi, directionObjetChoisi);
                        }
                        System.out.println(xx + "-" + yy);
                    }

                    @Override
                    public void mousePressed(MouseEvent e) {
                        mousePressed = true;
                        if (SwingUtilities.isRightMouseButton(e)) {
                            jeu.supprimer(xx, yy);
                        } else if (plateau.getCases()[xx][yy].getMachine() == null && plateau.getCases()[xx][yy].getMachineEsclave() == null) {
                            jeu.press(xx, yy, objetChoisi, directionObjetChoisi);
                        }
                    }

                    @Override
                    public void mouseEntered(MouseEvent e) {
                        if (mousePressed) {
                            if (SwingUtilities.isRightMouseButton(e)) {
                                jeu.supprimer(xx, yy);
                            } else if (plateau.getCases()[xx][yy].getMachine() == null && plateau.getCases()[xx][yy].getMachineEsclave() == null) {
                                jeu.slide(xx, yy);
                            }
                        }
                    }

                    @Override
                    public void mouseReleased(MouseEvent e) {
                        mousePressed = false;

                    }
                });

                grilleIP.add(iP);
            }
        }
        add(grilleIP, BorderLayout.CENTER);
        add(creerMenuMachines(), BorderLayout.SOUTH);
   }

    private JPanel creerMenuMachines() {
        JPanel menu = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        menu.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY));

        // Boutons machines
        String[][] machines = {
                {"Mine",      "Mine"},
                {"Tapis",     "Tapis"},
                {"Ciseaux",   "Ciseaux"},
                {"Poubelle",  "Poubelle"},
                {"Pivoteur", "Pivoteur"},
                {"Peintre", "Peintre"},
                {"Assembleur", "Assembleur"},
                {"Mixer", "Mixer"},
        };

        ButtonGroup groupMachines = new ButtonGroup();
        for (String[] entry : machines) {
            JToggleButton btn = new JToggleButton(entry[0]);
            btn.setFocusable(false);
            groupMachines.add(btn);
            btn.addActionListener(e -> {
                switch (entry[1]) {
                    case "Mine"      -> objetChoisi = ObjetChoisi.Mine;
                    case "Tapis"     -> objetChoisi = ObjetChoisi.Tapis;
                    case "Ciseaux"   -> objetChoisi = ObjetChoisi.Ciseaux;
                    case "Poubelle"  -> objetChoisi = ObjetChoisi.Poubelle;
                    case "Livraison" -> objetChoisi = ObjetChoisi.Livraison;
                    case "Peintre"     -> objetChoisi = ObjetChoisi.Peintre;
                    case "Assembleur" -> objetChoisi = ObjetChoisi.Assembleur;
                    case "Mixer"    -> objetChoisi = ObjetChoisi.Mixer;
                    case "Pivoteur"  -> objetChoisi = ObjetChoisi.Pivoteur;
                }
            });
            menu.add(btn);
        }

        // Séparateur
        menu.add(Box.createHorizontalStrut(16));
        menu.add(new JSeparator(SwingConstants.VERTICAL));
        menu.add(Box.createHorizontalStrut(8));

        // Boutons direction
        menu.add(new JLabel("Direction :"));
        ButtonGroup groupDir = new ButtonGroup();
        java.util.Map<String, Direction> dirs = new java.util.LinkedHashMap<>();
        dirs.put("↑ N", Direction.North);
        dirs.put("→ E", Direction.East);
        dirs.put("↓ S", Direction.South);
        dirs.put("← O", Direction.West);

        for (java.util.Map.Entry<String, Direction> entry : dirs.entrySet()) {
            JToggleButton btn = new JToggleButton(entry.getKey());
            btn.setFocusable(false);
            if (entry.getValue() == Direction.North) btn.setSelected(true);
            groupDir.add(btn);
            btn.addActionListener(e -> directionObjetChoisi = entry.getValue());
            menu.add(btn);
        }

        return menu;
    }

   private void mettreAJourAffichage() {
        for (int x = 0; x < sizeX; x++) {
            for (int y = 0; y < sizeY; y++) {

                tabIP[x][y].setBackground((Image) null);
                tabIP[x][y].setFront(null);
                tabIP[x][y].setShape(null);
                tabIP[x][y].resetGissement();
                tabIP[x][y].resetHubInfo();

                Case c = plateau.getCases()[x][y];
                Machine m = c.getMachine();

                if (m == null && c.getMachineEsclave() != null) {
                    m = c.getMachineEsclave();
                }
                Item g = c.getGisement();

                if (g != null) {
                    if (g instanceof ItemGisement) {
                        if(((ItemGisement) g).getItemShape() != null) {
                            tabIP[x][y].setShape(((ItemGisement) g).getItemShape());
                            tabIP[x][y].setGissement(((ItemGisement) g).getItemShape());
                        }
                        else {
                            switch (((ItemGisement) g).getColor()) {
                                case Red:
                                    tabIP[x][y].setBackground(icoRed);
                                    break;
                                case Blue:
                                    tabIP[x][y].setBackground(icoBlue);
                                    break;
                                case Green:
                                    tabIP[x][y].setBackground(icoGreen);
                                    break;
                                case Yellow:
                                    tabIP[x][y].setBackground(icoYellow);
                                    break;
                                case Purple:
                                    tabIP[x][y].setBackground(icoPurple);
                                    break;
                                case None:
                            }
                            tabIP[x][y].setGissement(((ItemGisement) g).getColor());
                        }

                    }
                }

                if (m != null) {
                    if (m instanceof Tapis tapis) {
                        Direction d = tapis.getDirection();
                        double angle = switch (d) {
                            case North -> 0;
                            case East -> 90;
                            case South -> 180;
                            case West -> 270;
                        };

                        Image baseSprite = switch (tapis.getType()) {
                            case Droit -> icoTapisHaut;
                            case VirageGauche -> icoTapisGauche;
                            case ViragedDroite -> icoTapisDroite;
                        };

                        tabIP[x][y].setBackground(tabIP[x][y].rotateImage(baseSprite, angle));

                    } else if (m instanceof Poubelle) {
                        tabIP[x][y].setBackground(icoPoubelle);

                    } else if (m instanceof Mine) {
                        Direction d = m.getDirection();
                        double angle = switch (d) {
                            case North -> 0;
                            case East -> 90;
                            case South -> 180;
                            case West -> 270;
                        };
                        tabIP[x][y].setBackground(tabIP[x][y].rotateImage(icoMine, angle));
                    } else if (m instanceof Livraison) {
                        Direction d = m.getDirection();
                        double angle = switch (d) {
                            case North -> 0;
                            case East -> 90;
                            case South -> 180;
                            case West -> 270;
                        };
                        tabIP[x][y].setBackground(tabIP[x][y].rotateImage(icoLivraison, angle));
                    } else if (m instanceof Ciseaux ciseaux) {
                        Point posMain = plateau.getPosition(ciseaux.getCase());
                        int offsetX = x - posMain.x;
                        int offsetY = y - posMain.y;

                        int totalW = 0;
                        int totalH = 0;

                        switch (ciseaux.getDirection()) {
                            case North, South -> { totalW = 2; totalH = 1; }
                            case East,  West  -> { totalW = 1; totalH = 2; }
                        }

                        double angle = switch (ciseaux.getDirection()) {
                            case North -> 0;
                            case East -> 90;
                            case South -> 180;
                            case West -> 270;
                        };

                        Image spriteRotate = tabIP[x][y].rotateImage(icoCiseaux, angle);

                        //System.out.println("OffsetX: " + offsetX +  " OffsetY: " + offsetY + " TotalW: " + totalW + " TotalH: " + totalH);
                        switch (ciseaux.getDirection()) {
                            case North -> tabIP[x][y].setBackground(spriteRotate, offsetX, offsetY, totalW, totalH);
                            case South -> tabIP[x][y].setBackground(spriteRotate, offsetX+1, offsetY, totalW, totalH);
                            case East  -> tabIP[x][y].setBackground(spriteRotate, offsetX, offsetY, totalW, totalH);
                            case West  -> tabIP[x][y].setBackground(spriteRotate, offsetX, offsetY+1, totalW, totalH);
                        }
                    } else if (m instanceof Assembleur ass) {
                        Point posMain = plateau.getPosition(ass.getCase());
                        int offsetX = x - posMain.x;
                        int offsetY = y - posMain.y;

                        int totalW = 0;
                        int totalH = 0;

                        switch (ass.getDirection()) {
                            case North, South -> { totalW = 2; totalH = 1; }
                            case East,  West  -> { totalW = 1; totalH = 2; }
                        }

                        double angle = switch (ass.getDirection()) {
                            case North -> 0;
                            case East -> 90;
                            case South -> 180;
                            case West -> 270;
                        };

                        Image spriteRotate = tabIP[x][y].rotateImage(icoAssembleur, angle);

                        //System.out.println("OffsetX: " + offsetX +  " OffsetY: " + offsetY + " TotalW: " + totalW + " TotalH: " + totalH);
                        switch (ass.getDirection()) {
                            case North -> tabIP[x][y].setBackground(spriteRotate, offsetX, offsetY, totalW, totalH);
                            case South -> tabIP[x][y].setBackground(spriteRotate, offsetX+1, offsetY, totalW, totalH);
                            case East  -> tabIP[x][y].setBackground(spriteRotate, offsetX, offsetY, totalW, totalH);
                            case West  -> tabIP[x][y].setBackground(spriteRotate, offsetX, offsetY+1, totalW, totalH);
                        }
                    } else if (m instanceof Hub gm) {
                        if(gm.getCompteur()>=5) {
                            gm.setCompteur(0);
                            jeu.setNiveau(jeu.getNiveau()+1);
                            gm.changeLevel(jeu.getNiveau());
                        }
                        Point posMain = plateau.getPosition(gm.getCase());
                        int offsetX = x - posMain.x;
                        int offsetY = y - posMain.y;

                        tabIP[x][y].setBackground(icoHub, offsetX, offsetY, 4, 4);

                        tabIP[x][y].setHubInfo(offsetX, offsetY, 4, 4,
                                "Niveau " + (1 + jeu.getNiveau()),
                                gm.getFormeCible(),
                                gm.getCompteur()
                        );
                    } else if (m instanceof Mixer mixer) {
                        Point posMain = plateau.getPosition(mixer.getCase());
                        int offsetX = x - posMain.x;
                        int offsetY = y - posMain.y;

                        int totalW = 0;
                        int totalH = 0;

                        switch (mixer.getDirection()) {
                            case North, South -> { totalW = 2; totalH = 1; }
                            case East,  West  -> { totalW = 1; totalH = 2; }
                        }

                        double angle = switch (mixer.getDirection()) {
                            case North -> 0;
                            case East -> 90;
                            case South -> 180;
                            case West -> 270;
                        };

                        Image spriteRotate = tabIP[x][y].rotateImage(icoMixer, angle);

                        //System.out.println("OffsetX: " + offsetX +  " OffsetY: " + offsetY + " TotalW: " + totalW + " TotalH: " + totalH);
                        switch (mixer.getDirection()) {
                            case North -> tabIP[x][y].setBackground(spriteRotate, offsetX, offsetY, totalW, totalH);
                            case South -> tabIP[x][y].setBackground(spriteRotate, offsetX+1, offsetY, totalW, totalH);
                            case East  -> tabIP[x][y].setBackground(spriteRotate, offsetX, offsetY, totalW, totalH);
                            case West  -> tabIP[x][y].setBackground(spriteRotate, offsetX, offsetY+1, totalW, totalH);
                        }
                    } else if (m instanceof Pivoteur pivoteur) {
                        Direction d = pivoteur.getDirection();
                        double angle = switch (d) {
                            case North -> 0;
                            case East -> 90;
                            case South -> 180;
                            case West -> 270;
                        };

                        tabIP[x][y].setBackground((tabIP[x][y].rotateImage(icoPivoteur, angle)));
                    } else if (m instanceof Peintre peintre) {
                        Point posMain = plateau.getPosition(peintre.getCase());
                        int offsetX = x - posMain.x;
                        int offsetY = y - posMain.y;

                        int totalW = 0;
                        int totalH = 0;

                        switch (peintre.getDirection()) {
                            case North, South -> {
                                totalW = 2;
                                totalH = 1;
                            }
                            case East, West -> {
                                totalW = 1;
                                totalH = 2;
                            }
                        }

                        double angle = switch (peintre.getDirection()) {
                            case North -> 0;
                            case East -> 90;
                            case South -> 180;
                            case West -> 270;
                        };

                        Image spriteRotate = tabIP[x][y].rotateImage(icoPeintre, angle);

                        //System.out.println("OffsetX: " + offsetX +  " OffsetY: " + offsetY + " TotalW: " + totalW + " TotalH: " + totalH);
                        switch (peintre.getDirection()) {
                            case North -> tabIP[x][y].setBackground(spriteRotate, offsetX, offsetY, totalW, totalH);
                            case South -> tabIP[x][y].setBackground(spriteRotate, offsetX + 1, offsetY, totalW, totalH);
                            case East -> tabIP[x][y].setBackground(spriteRotate, offsetX, offsetY, totalW, totalH);
                            case West -> tabIP[x][y].setBackground(spriteRotate, offsetX, offsetY + 1, totalW, totalH);
                        }
                    }

                    Item current = m.getCurrent();

                    if(m != null || c.getMachineEsclave() != null){
                        if(!(m instanceof Tapis)) current = null;
                    }

                    if (current instanceof ItemShape) {
                        tabIP[x][y].setShape((ItemShape) current);
                    }
                    if (current instanceof ItemColor) {
                        switch (((ItemColor) current).getColor()) {
                            case Red:
                                tabIP[x][y].setFront(icoRed);
                                break;
                            case Blue:
                                tabIP[x][y].setFront(icoBlue);
                                break;
                            case Green:
                                tabIP[x][y].setFront(icoGreen);
                                break;
                            case Yellow:
                                tabIP[x][y].setFront(icoYellow);
                                break;
                            case Purple:
                                tabIP[x][y].setFront(icoPurple);
                                break;
                            case None:
                        }
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


