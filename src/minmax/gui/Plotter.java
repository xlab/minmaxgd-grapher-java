/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package minmax.gui;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.media.opengl.GLJPanel;
import minmax.Settings;
import minmax.model.Piece;
import minmax.model.Surface;
import org.scilab.forge.jlatexmath.TeXConstants;
import org.scilab.forge.jlatexmath.TeXFormula;

/**
 *
 * @author Kouprianov Maxim <me@kc.vc> @ SE HSE
 */
public class Plotter extends GLJPanel {

    private Point tmpViewboxCenter;
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
    private Surface surface;

    public Plotter() {
        initComponents();

        gridCenter = new Point(Settings.defaultDimension / 2, Settings.defaultDimension / 2);
        viewboxCenter = gridCenter.getLocation();
        cellSize = 20;
        surface = new Surface();
    }

    public Surface getSurface() {
        return surface;
    }

    public void setSurface(Surface surface) {
        this.surface = surface;
    }
    private double zoom = 1.0;

    private double getZoom() {
        return zoom;
    }
    private String xLabel = "X";

    public String getXLabel() {
        return xLabel;
    }

    public void setXLabel(String xLabel) {
        this.xLabel = xLabel;
    }
    private String yLabel = "Y";

    public String getYLabel() {
        return yLabel;
    }

    public void setYLabel(String yLabel) {
        this.yLabel = yLabel;
    }

    private void setZoom(double zoom) {
        if (zoom >= 0.4 && zoom <= 2) {
            this.zoom = zoom;
            cellSize = (int) (20.0 * zoom);
        }
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

        final int shift = 4;

        final int lowX = viewboxCenter.x - viewboxW;
        final int hiX = viewboxCenter.x + viewboxW;
        final int lowY = viewboxCenter.y - viewboxH;
        final int hiY = viewboxCenter.y + viewboxH;

        final boolean shifted_h = lowY > gridCenter.y - 3;
        final boolean shifted_w = hiX <= gridCenter.x + 1;

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

        int column = -shift;
        for (int i = lowX - shift; i <= hiX + shift; ++i) {
            if (i != gridCenter.x) {
                g2.drawLine(column * cellSize + kX, 0, column * cellSize + kX, h);
            }

            ++column;
        }

        int row = -shift;
        for (int i = lowY - shift; i <= hiY + shift; ++i) {
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
        for (int i = lowX; i <= hiX; ++i) {
            if (i == gridCenter.x) {
                if (!shifted_h) {
                    p.move(column * cellSize + kX - (Math.max((int) (15 * zoom), 15)), (Math.max((int) (5 * zoom), 5)));
                    g2.drawImage(drawFormula(getYLabel()), p.x, p.y, this);
                }

                g2.drawLine(column * cellSize + kX, (shifted_h ? 0 : Math.max((int) (6 * zoom), 4)), column * cellSize + kX, h);

                row = (shifted_h ? 0 : 2);
                for (int j = lowY; j <= hiY; ++j) {
                    if (j + (shifted_h ? 0 : 2) != gridCenter.y) {
                        //засечки
                        g2.drawLine(column * cellSize + kX - 2, row * cellSize + kY, column * cellSize + kX + 2, row * cellSize + kY);

                        if (zoom >= 0.8) {
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
        for (int i = lowY; i <= hiY; ++i) {
            if (i == gridCenter.y) {
                if (!shifted_w) {
                    p.move(w - (Math.max((int) (15 * zoom), 15)), row * cellSize + kY + 10);
                    g2.drawImage(drawFormula(getXLabel()), p.x, p.y, this);
                }


                //ось
                g2.drawLine(0, row * cellSize + kY, w - (shifted_w ? 0 : Math.max((int) (6 * zoom), 4)), row * cellSize + kY);

                //засечки & цифры
                column = 0;
                for (int j = lowX; j <= hiX; ++j) {
                    if (j != gridCenter.x) {
                        //засечки
                        g2.drawLine(column * cellSize + kX, row * cellSize + kY - 2, column * cellSize + kX, row * cellSize + kY + 2);

                        if (zoom >= 0.8) {
                            //цифры
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                            g2.setFont(new Font("Arial", Font.PLAIN, Math.min((int) (8 * zoom), 8)));
                            g2.drawString("" + (j - gridCenter.x), column * cellSize + kX - (Math.min((int) (8 * zoom), 8) / 2), row * cellSize + kY + Math.min((int) (8 * zoom), 8) + 3);
                            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                                    RenderingHints.VALUE_ANTIALIAS_OFF);
                        }
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

        //Slope layers
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        for (int layer = 0; layer < surface.getLayersCount(); ++layer) {
            column = -shift;
            for (int i = lowX - shift; i <= hiX + shift; ++i) {
                row = -shift;
                for (int j = lowY - shift; j <= hiY + shift; ++j) {
                    final int x, y;
                    x = i - gridCenter.x + Settings.defaultDimension / 2;
                    y = j - gridCenter.y + Settings.defaultDimension / 2;
                    Piece piece = surface.getPiece(layer, x, y + 1);
                    if (piece != null) {
                        g2.setColor(piece.getColor());

                        final int x1 = (column) * cellSize + kX;
                        final int y1 = (row + 1) * cellSize + kY;
                        final int x2 = (column) * cellSize + kX - cellSize;
                        final int y2 = (row + 1) * cellSize + kY - cellSize;

                        if (piece.getType() != Piece.Type.REGULAR) {
                            g2.drawImage(drawPiece(piece), null, column * cellSize + kX, row * cellSize + kY + 1);

                            if (piece.getType() == Piece.Type.VERTEX) {
                                final int R = 2;
                                g2.fillOval(x1 - R, y1 - R, R * 2 + 1, R * 2 + 1);
                            }
                        } else {
                            g2.setComposite(ImageHelpers.makeComposite(0.3f));
                            switch (layer % 2) {
                                case 0:
                                    g2.drawLine(x1, y2, x2, y1);
                                    break;
                                case 1:
                                    g2.drawLine(x1, y1, x2, y2);
                                    break;
                            }
                            g2.fillRect(x1 - cellSize, y1 - cellSize, cellSize, cellSize);
                            g2.setComposite(AlphaComposite.SrcOver);
                        }
                    }
                    ++row;
                }
                ++column;
            }
        }

        g.drawImage(buffer, 0, 0, this);
        g2.dispose();
    }

    private void placeDot(int x, int y, Color c) {
        //grid.setPoint(x, y, c);
        repaint();
    }

    private BufferedImage drawFormula(String formula) {
        BufferedImage image = (BufferedImage) new TeXFormula(formula).createBufferedImage(TeXConstants.STYLE_DISPLAY, (Math.max((int) (15 * zoom), 15)), Color.black, Color.decode("#ffffff"));
        BufferedImage opaque = ImageHelpers.makeColorTransparent(image, Color.decode("#ffffff"));

        return opaque;
    }

    private BufferedImage drawPiece(Piece p) {
        int size = cellSize;
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        ((Graphics2D) image.getGraphics()).setBackground(Color.yellow);
        BufferedImage target = ImageHelpers.makeColorTransparent(image, Color.yellow);
        Graphics2D g2d = (Graphics2D) target.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_OFF);
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
                RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);

        g2d.setComposite(ImageHelpers.makeComposite(0.8f));
        g2d.setColor(p.getColor());
        switch (p.getType()) {
            case VERTEX:
                g2d.setStroke(new BasicStroke(4.0f));
                g2d.drawLine(0, size, size, size);
                g2d.setStroke(new BasicStroke(1.0f));
                g2d.fillOval(0, size, 5, 5);
                break;
            case LEFT:
                g2d.setStroke(new BasicStroke(3.0f));
                g2d.drawLine(0, 0, 0, size);
                g2d.setStroke(new BasicStroke(1.0f));
                break;
            case BOTTOM:
                g2d.setStroke(new BasicStroke(4.0f));
                g2d.drawLine(0, size, size, size);
                g2d.setStroke(new BasicStroke(1.0f));
                break;
        }

        return target;
    }
}
