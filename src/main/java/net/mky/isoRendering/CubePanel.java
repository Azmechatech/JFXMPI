/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.mky.isoRendering;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class CubePanel extends JPanel{
    private static final int D_W = 400;
    private static final int D_H = 300;

    Cube cube;
    public CubePanel() {
        cube = new Cube(75, 5, 50, 15);
        InputMap im = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        im.put(KeyStroke.getKeyStroke("RIGHT"), "shiftRight");
        getActionMap().put("shiftRight", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                cube.shiftRight();
                repaint();
            }
        });
        im.put(KeyStroke.getKeyStroke("LEFT"), "shiftLeft");
        getActionMap().put("shiftLeft", new AbstractAction(){
            public void actionPerformed(ActionEvent e) {
                cube.shiftLeft();
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        cube.drawCube(g);
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(D_W, D_H);
    }

    public class Cube {
        private static final int SHIFT_INC = 5;
        int x, y, size, shift;
        Point[] cubeOnePoints;
        Point[] cubeTwoPoints;
        public Cube(int x, int y, int size, int shift) {
            this.x = x;
            this.y = y;
            this.size = size;
            this.shift = shift;
            cubeOnePoints = getCubeOnePoints();
            cubeTwoPoints = getCubeTwoPoints();
        }

        private Point[] getCubeOnePoints() {
            Point[] points = new Point[4];
            points[0] = new Point(x, y);
            points[1] = new Point(x + size, y);
            points[2] = new Point(x + size, y + size);
            points[3] = new Point(x, y + size);
            return points;
        }

        private Point[] getCubeTwoPoints() {
            int newX = x + shift;
            int newY = y + shift;
            Point[] points = new Point[4];
            points[0] = new Point(newX, newY);
            points[1] = new Point(newX + size, newY);
            points[2] = new Point(newX + size, newY + size);
            points[3] = new Point(newX, newY + size);
            return points;
        }

        public void shiftLeft() {
            x -= SHIFT_INC;
            for (Point p : cubeOnePoints) {
                p.x -= SHIFT_INC;
            }
            for (Point p : cubeTwoPoints) {
                p.x -= SHIFT_INC;
            }
        }

        public void shiftRight() {
            x += SHIFT_INC;
            for (Point p : cubeOnePoints) {
                p.x += SHIFT_INC;
            }
            for (Point p : cubeTwoPoints) {
                p.x += SHIFT_INC;
            }
        }

        public void drawCube(Graphics g) {
            g.drawRect(x, y, size, size);
            g.drawRect(x + shift, y + shift, size, size);
            // draw connecting lines
            for (int i = 0; i < 4; i++) {
                g.drawLine(cubeOnePoints[i].x, cubeOnePoints[i].y, 
                        cubeTwoPoints[i].x, cubeTwoPoints[i].y);
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JFrame frame = new JFrame();
                frame.add(new CubePanel());
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.pack();
                frame.setLocationRelativeTo(null);
                frame.setVisible(true);
            }
        });
    }
}