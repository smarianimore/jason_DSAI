package world;

import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;
import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import javax.swing.border.*;

/**
 * Simple Vacuum cleaning environment
 *
 * @author Jomi
 *
 */
public class VCWorld extends Environment {

    /** world model */
    private boolean[][] dirty =
      { { true, true },    // all dirty
        { true, true }
      };

    private int vcx = 0; // the vacuum cleaner location
    private int vcy = 0;

    private Object modelLock = new Object();
    private final Icon robotIcon = new RoombaIcon();
    private final Icon dirtIcon = new DirtPileIcon();

    /** general delegations */
    private HouseGUI gui = new HouseGUI();
    private Logger   logger = Logger.getLogger("env."+VCWorld.class.getName());
    private Random   r = new Random();

    /** constant terms used for perception */
    private static final Literal lPos1  = ASSyntax.createLiteral("pos", ASSyntax.createNumber(1));
    private static final Literal lPos2  = ASSyntax.createLiteral("pos", ASSyntax.createNumber(2));
    private static final Literal lPos3  = ASSyntax.createLiteral("pos", ASSyntax.createNumber(3));
    private static final Literal lPos4  = ASSyntax.createLiteral("pos", ASSyntax.createNumber(4));
    private static final Literal lDirty = ASSyntax.createLiteral("dirty");
    private static final Literal lClean = ASSyntax.createLiteral("clean");

    public VCWorld() {
        createPercept();

        // create a thread to add dirty
        new Thread() {
            public void run() {
                try {
                    while (isRunning()) {
                        // add random dirty
                        if (r.nextInt(100) < 20) {
                            dirty[r.nextInt(2)][r.nextInt(2)] = true;
                            gui.paint();
                            createPercept();
                        }
                        Thread.sleep(1000);
                    }
                } catch (Exception e) {}
            }
        } .start();
    }

    /** create the agents perceptions based on the world model */
    private void createPercept() {
        // remove previous perception
        clearPercepts();

        if (vcx == 0 && vcy == 0) {
            addPercept(lPos1);
        } else if (vcx == 1 && vcy == 0) {
            addPercept(lPos2);
        } else if (vcx == 0 && vcy == 1) {
            addPercept(lPos3);
        } else if (vcx == 1 && vcy == 1) {
            addPercept(lPos4);
        }

        if (dirty[vcx][vcy]) {
            addPercept(lDirty);
        } else {
            addPercept(lClean);
        }
    }

    @Override
    public boolean executeAction(String ag, Structure action) {
        logger.info("doing "+action);

        try {
            Thread.sleep(500);   // slow down the execution
        }  catch (Exception e) {}

        synchronized (modelLock) {
            // Change the world model based on action
            if (action.getFunctor().equals("suck")) {
                if (dirty[vcx][vcy]) {
                    dirty[vcx][vcy] = false;
                } else {
                    logger.info("suck in a clean location!");
                    Toolkit.getDefaultToolkit().beep();
                }
            } else if (action.getFunctor().equals("left")) {
                if (vcx > 0) {
                    vcx--;
                }
            } else if (action.getFunctor().equals("right")) {
                if (vcx < 1) {
                    vcx++;
                }
            } else if (action.getFunctor().equals("up")) {
                if (vcy > 0) {
                    vcy--;
                }
            } else if (action.getFunctor().equals("down")) {
                if (vcy < 1) {
                    vcy++;
                }
            } else {
                logger.info("The action "+action+" is not implemented!");
                return false;
            }
        }

        createPercept(); // update agents perception for the new world state
        gui.paint();
        return true;
    }

    @Override
    public void stop() {
        super.stop();
        gui.setVisible(false);
    }


    /* a simple GUI */
    class HouseGUI extends JFrame {
        JLabel[][] labels;

        HouseGUI() {
            super("Domestic Robot");
            labels = new JLabel[dirty.length][dirty.length];
            getContentPane().setLayout(new GridLayout(labels.length, labels.length));
            for (int j = 0; j < labels.length; j++) {
                for (int i = 0; i < labels.length; i++) {
                    labels[i][j] = new JLabel();
                    labels[i][j].setPreferredSize(new Dimension(180,180));
                    labels[i][j].setHorizontalAlignment(JLabel.CENTER);
                    labels[i][j].setBorder(new EtchedBorder());
                    getContentPane().add(labels[i][j]);
                }
            }
            pack();
            setVisible(true);
            paint();
        }

        void paint() {
            synchronized (modelLock) { // do not allow changes in the robot location while painting
                for (int i = 0; i < labels.length; i++) {
                    for (int j = 0; j < labels.length; j++) {
                        boolean hasRobot = vcx == i && vcy == j;
                        boolean hasDirt = dirty[i][j];
                        labels[i][j].setText("");
                        labels[i][j].setIcon(new CellIcon(
                                hasRobot ? robotIcon : null,
                                hasDirt ? dirtIcon : null));
                        labels[i][j].setToolTipText(hasRobot && hasDirt
                                ? "Roomba cleaning robot and dirt"
                                : hasRobot ? "Roomba cleaning robot" : hasDirt ? "Dirt" : null);
                    }
                }
            }
        }
    }

    private static class RoombaIcon implements Icon {
        private static final int SIZE = 88;

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.translate(x, y);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(new Color(0, 0, 0, 35));
            g.fill(new Ellipse2D.Double(12, 15, 66, 66));

            g.setColor(new Color(35, 39, 43));
            g.fill(new Ellipse2D.Double(8, 8, 72, 72));
            g.setPaint(new GradientPaint(22, 16, new Color(225, 228, 230),
                    65, 72, new Color(132, 139, 145)));
            g.fill(new Ellipse2D.Double(13, 13, 62, 62));

            g.setColor(new Color(65, 70, 74));
            g.setStroke(new BasicStroke(3f));
            g.draw(new Ellipse2D.Double(10, 10, 68, 68));

            g.setColor(new Color(94, 101, 106));
            g.fill(new Ellipse2D.Double(31, 27, 27, 27));
            g.setColor(new Color(205, 208, 210));
            g.fill(new Ellipse2D.Double(35, 31, 19, 19));
            g.setColor(new Color(70, 76, 81));
            g.fill(new Ellipse2D.Double(40, 36, 9, 9));

            g.setColor(new Color(194, 48, 43));
            g.fill(new RoundRectangle2D.Double(37, 18, 15, 5, 2, 2));

            g.setColor(new Color(50, 54, 58));
            g.setStroke(new BasicStroke(2f));
            g.draw(new Arc2D.Double(17, 17, 54, 54, 205, 130, Arc2D.OPEN));
            g.dispose();
        }
    }

    private static class DirtPileIcon implements Icon {
        private static final int SIZE = 88;

        @Override
        public int getIconWidth() {
            return SIZE;
        }

        @Override
        public int getIconHeight() {
            return SIZE;
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            Graphics2D g = (Graphics2D) graphics.create();
            g.translate(x, y);
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g.setColor(new Color(0, 0, 0, 30));
            g.fill(new Ellipse2D.Double(12, 65, 66, 10));

            Path2D.Double pile = new Path2D.Double();
            pile.moveTo(12, 66);
            pile.curveTo(14, 56, 23, 53, 28, 48);
            pile.curveTo(31, 38, 39, 34, 44, 40);
            pile.curveTo(51, 31, 61, 39, 62, 47);
            pile.curveTo(72, 49, 77, 57, 77, 66);
            pile.closePath();
            g.setPaint(new GradientPaint(30, 38, new Color(139, 91, 48),
                    50, 67, new Color(75, 45, 27)));
            g.fill(pile);

            g.setColor(new Color(97, 58, 34));
            g.setStroke(new BasicStroke(2f));
            g.draw(pile);

            g.setColor(new Color(163, 113, 67));
            g.setStroke(new BasicStroke(2.5f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            g.draw(new QuadCurve2D.Double(27, 58, 38, 49, 49, 58));
            g.draw(new QuadCurve2D.Double(48, 61, 59, 49, 69, 58));

            g.setColor(new Color(61, 39, 27));
            g.fill(new Ellipse2D.Double(31, 61, 3, 2));
            g.fill(new Ellipse2D.Double(55, 54, 3, 2));
            g.fill(new Ellipse2D.Double(42, 45, 3, 2));
            g.dispose();
        }
    }

    private static class CellIcon implements Icon {
        private final Icon topIcon;
        private final Icon bottomIcon;

        CellIcon(Icon topIcon, Icon bottomIcon) {
            this.topIcon = topIcon;
            this.bottomIcon = bottomIcon;
        }

        @Override
        public int getIconWidth() {
            return Math.max(width(topIcon), width(bottomIcon));
        }

        @Override
        public int getIconHeight() {
            return height(topIcon) + height(bottomIcon);
        }

        @Override
        public void paintIcon(Component component, Graphics graphics, int x, int y) {
            if (topIcon != null) {
                topIcon.paintIcon(component, graphics,
                        x + (getIconWidth() - topIcon.getIconWidth()) / 2, y);
            }
            if (bottomIcon != null) {
                bottomIcon.paintIcon(component, graphics,
                        x + (getIconWidth() - bottomIcon.getIconWidth()) / 2,
                        y + height(topIcon));
            }
        }

        private static int width(Icon icon) {
            return icon == null ? 0 : icon.getIconWidth();
        }

        private static int height(Icon icon) {
            return icon == null ? 0 : icon.getIconHeight();
        }
    }
}
