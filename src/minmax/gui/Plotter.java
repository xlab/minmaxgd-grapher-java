/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minmax.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.media.opengl.GLJPanel;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

/**
 *
 * @author Kouprianov Maxim <me@kc.vc> @ SE HSE
 */
public class Plotter extends GLJPanel {

    private Point tmpViewboxCenter;
    private Point viewboxCorner;
    private Point gridCenter;
    private Point viewboxCenter;
    private int viewboxW;
    private int viewboxH;
    private Point startDrag = new Point(0, 0);
    private Point currentDrag = new Point(0, 0);
    private int cellSize;
    boolean dragStarted;
    int kX;
    int kY;

    public Plotter() {
        initComponents();
        gridCenter = new Point(gridBounds / 2, gridBounds / 2);
        viewboxCenter = gridCenter.getLocation();
        cellSize = 20;
        viewboxCorner = new Point(0, 0);
        grid = new Grid(gridBounds);
    }
    private Grid grid = new Grid();

    public Grid getGrid() {
        return grid;
    }

    public void setGrid(Grid grid) {
        this.grid = grid;
    }
    private int gridBounds = 1000;

    public int getGridBounds() {
        return gridBounds;
    }

    private int getMinX() {
        return 0 - gridBounds;
    }

    private int getMinY() {
        return 0 - gridBounds;
    }

    private int getMaxX() {
        return 0 + gridBounds;
    }

    private int getMaxY() {
        return 0 + gridBounds;
    }
    private double zoom = 1.0;

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        if (zoom >= 0.4 && zoom <= 2) {
            this.zoom = zoom;
            cellSize = (int) (20.0 * zoom);
        }
    }

    public void setGridBounds(int gridBounds) {
        this.gridBounds = gridBounds;
        gridCenter = new Point(gridBounds / 2, gridBounds / 2);
        repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        addMouseWheelListener(new java.awt.event.MouseWheelListener() {
            public void mouseWheelMoved(java.awt.event.MouseWheelEvent evt) {
                iZoom(evt);
            }
        });
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                Plotter.this.mousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                Plotter.this.mouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                Plotter.this.mouseClicked(evt);
            }
        });
        addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                Plotter.this.mouseDragged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void iZoom(java.awt.event.MouseWheelEvent evt) {//GEN-FIRST:event_iZoom
        setZoom(getZoom() + evt.getWheelRotation() * 0.1);
        repaint();
    }//GEN-LAST:event_iZoom

    private void mousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mousePressed
        dragStarted = true;
        startDrag = evt.getPoint();
        tmpViewboxCenter = viewboxCenter.getLocation();
    }//GEN-LAST:event_mousePressed

    private void mouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseDragged
        currentDrag = evt.getPoint();
        viewboxCorner = currentDrag.getLocation();
        viewboxCenter = new Point(tmpViewboxCenter.x - (currentDrag.x - startDrag.x) / cellSize, tmpViewboxCenter.y - (currentDrag.y - startDrag.y) / cellSize);
        repaint();
    }//GEN-LAST:event_mouseDragged

    private void mouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseClicked
        int x = 0, y = 0;
        for (int i = 0; i < viewboxW * 2; ++i) {
            if (Math.abs(i * cellSize + kX - evt.getX()) <= cellSize / 2) {
                x = (viewboxCenter.x - viewboxW) + i - gridCenter.x;
            }
        }

        for (int i = 0; i < viewboxH * 2; ++i) {
            if (Math.abs(i * cellSize + kY - evt.getY()) <= cellSize / 2) {
                y = (viewboxCenter.y - viewboxH) + i - gridCenter.y;
            }
        }

        placeDot(x, y, Color.red);
    }//GEN-LAST:event_mouseClicked

    private void mouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_mouseReleased
        dragStarted = false;
    }//GEN-LAST:event_mouseReleased

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        final Point p = new Point(0, 0);
        final int w = getSize().width;
        final int h = getSize().height;

        GraphicsConfiguration gc = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().
                getDefaultConfiguration();
        BufferedImage buffer = gc.createCompatibleImage(w, h);
        Graphics2D g2 = (Graphics2D) buffer.getGraphics();
        //Graphics2D g2 = (Graphics2D) g;

        if (dragStarted) {
            kX = (currentDrag.x - startDrag.x) % cellSize;
            kY = (currentDrag.y - startDrag.y) % cellSize;
        }

        viewboxW = w / (2 * cellSize);
        viewboxH = h / (2 * cellSize);

        final boolean shifted_h = (viewboxCenter.y - viewboxH) > gridCenter.y - 3;
        final boolean shifted_w = (viewboxCenter.x + viewboxW) <= gridCenter.x + 1;

        final int testR = 2;

        g2.setColor(Color.decode("#f4f4f4"));
        g2.fillRect(0, 0, w, h);


        //Layer1
        g2.setColor(Color.decode("#dddddd"));
        g2.setStroke(new BasicStroke(1.0f,
                BasicStroke.CAP_BUTT,
                BasicStroke.JOIN_MITER,
                10.0f, new float[]{2.0f}, 0.0f));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);

        int column = 0;
        for (int i = viewboxCenter.x - viewboxW; i <= viewboxCenter.x + viewboxW + 2; ++i) {
            if (i != gridCenter.x) {
                g2.drawLine(column * cellSize + kX, 0, column * cellSize + kX, h);
            }

            ++column;
        }

        int row = 0;
        for (int i = viewboxCenter.y - viewboxH; i <= viewboxCenter.y + viewboxH + 2; ++i) {
            if (i != gridCenter.y) {
                g2.drawLine(0, row * cellSize + kY, w, row * cellSize + kY);
            }

            ++row;
        }

        //Layer2
        g2.setColor(Color.black);
        g2.setStroke(new BasicStroke(1.0f));
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_SPEED);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_SPEED);

        column = 0;
        for (int i = viewboxCenter.x - viewboxW; i <= viewboxCenter.x + viewboxW + 2; ++i) {
            if (i == gridCenter.x) {
                if (!shifted_h) {
                    p.move(column * cellSize + kX - (Math.max((int) (15 * zoom), 15)), (Math.max((int) (5 * zoom), 5)));
                    g2.drawImage(new TeXFormula("\\delta").createBufferedImage(TeXConstants.STYLE_DISPLAY, (Math.max((int) (15 * zoom), 15)), Color.black, Color.decode("#f4f4f4")), p.x, p.y, this);
                }

                g2.drawLine(column * cellSize + kX, (shifted_h ? 0 : Math.max((int) (6 * zoom), 4)), column * cellSize + kX, h);

                row = (shifted_h ? 0 : 2);
                for (int j = viewboxCenter.y - viewboxH; j <= viewboxCenter.y + viewboxH + 2; ++j) {
                    if (j + (shifted_h ? 0 : 2) != gridCenter.y) {
                        //засечки
                        g2.drawLine(column * cellSize + kX - 2, row * cellSize + kY, column * cellSize + kX + 2, row * cellSize + kY);

                        //цифры
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setFont(new Font("Arial", Font.PLAIN, Math.min((int) (8 * zoom), 8)));

                        final int val = -(j - gridCenter.y + (shifted_h ? 0 : 2));
                        g2.drawString("" + val,
                                column * cellSize + kX - (Math.min((int) (8 * zoom), 8) / 2)
                                - (("" + val).length() * (Math.min((int) (8 * zoom), 8)) / 2),
                                row * cellSize + kY + (Math.min((int) (8 * zoom), 8)) / 2);
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_OFF);
                    }
                    ++row;
                }

                if (!shifted_h) {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.fillPolygon(new int[]{column * cellSize + kX - Math.max((int) (2 * zoom), 2), column * cellSize + kX,
                                column * cellSize + kX + Math.max((int) (2 * zoom), 2)},
                            new int[]{Math.max((int) (6 * zoom), 4), 0, Math.max((int) (6 * zoom), 4)}, 3);
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_OFF);
                }
            }

            ++column;
        }

        row = 0;
        for (int i = viewboxCenter.y - viewboxH; i <= viewboxCenter.y + viewboxH + 2; ++i) {
            if (i == gridCenter.y) {
                if (!shifted_w) {
                    p.move(w - (Math.max((int) (15 * zoom), 15)), row * cellSize + kY + 5);
                    g2.drawImage(new TeXFormula("\\gamma").createBufferedImage(TeXConstants.STYLE_DISPLAY, (Math.max((int) (15 * zoom), 15)), Color.black, Color.decode("#f4f4f4")), p.x, p.y, this);
                }


                //ось
                g2.drawLine(0, row * cellSize + kY, w - (shifted_w ? 0 : Math.max((int) (6 * zoom), 4)), row * cellSize + kY);

                //засечки & цифры
                column = 0;
                for (int j = viewboxCenter.x - viewboxW; j < viewboxCenter.x + viewboxW + (shifted_w ? 2 : 0); ++j) {
                    if (j != gridCenter.x) {
                        //засечки
                        g2.drawLine(column * cellSize + kX, row * cellSize + kY - 2, column * cellSize + kX, row * cellSize + kY + 2);

                        //цифры
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_ON);
                        g2.setFont(new Font("Arial", Font.PLAIN, Math.min((int) (8 * zoom), 8)));
                        g2.drawString("" + (j - gridCenter.x), column * cellSize + kX - (Math.min((int) (8 * zoom), 8) / 2), row * cellSize + kY + Math.min((int) (8 * zoom), 8) + 3);
                        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                RenderingHints.VALUE_ANTIALIAS_OFF);
                    }
                    ++column;
                }

                //стрелочка & переменная
                if (!shifted_w) {
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.fillPolygon(new int[]{w - Math.max((int) (6 * zoom), 4), w, w - Math.max((int) (6 * zoom), 4)}, new int[]{row * cellSize + kY - Math.max((int) (2 * zoom), 2), row * cellSize + kY, row * cellSize + kY + Math.max((int) (2 * zoom), 2)}, 3);
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_OFF);
                }
            }
            ++row;
        }


        //Layer 3
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        column = 0;
        for (int i = viewboxCenter.x - viewboxW; i <= viewboxCenter.x + viewboxW + 1; ++i) {
            row = 0;
            for (int j = viewboxCenter.y - viewboxH; j
                    <= viewboxCenter.y + viewboxH + 1; ++j) {
                final int x, y;
                x = i - gridCenter.x;
                y = j - gridCenter.y;
                if (grid.exist(x, y)) {
                    g2.setColor(grid.getPoint(x, y));
                    g2.fillOval(column * cellSize + kX - testR,
                            row * cellSize + kY - testR, testR * 2 + 1, testR * 2 + 1);
                }
                ++row;
            }
            ++column;
        }

        g.drawImage(buffer, 0, 0, this);
        g2.dispose();
    }

    private void placeDot(int x, int y, Color c) {
        grid.setPoint(x, y, c);
        repaint();
    }
}
