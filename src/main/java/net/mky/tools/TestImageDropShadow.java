///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package net.mky.tools;
//
//import java.awt.BorderLayout;
//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.EventQueue;
//import java.awt.Graphics;
//import java.awt.image.BufferedImage;
//import java.io.IOException;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//import javax.imageio.ImageIO;
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//import javax.swing.UIManager;
//import javax.swing.UnsupportedLookAndFeelException;
//
//public class TestImageDropShadow {
//
//    public static void main(String[] args) {
//        new TestImageDropShadow();
//    }
//
//    public TestImageDropShadow() {
//        EventQueue.invokeLater(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
//                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
//                }
//
//                JFrame frame = new JFrame("Testing");
//                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//                frame.setLayout(new BorderLayout());
//                frame.add(new ImagePane());
//                frame.pack();
//                frame.setLocationRelativeTo(null);
//                frame.setVisible(true);
//            }
//        });
//    }
//
//    public class ImagePane extends JPanel {
//
//        private BufferedImage background;
//
//        public ImagePane() {
//            try {
//                BufferedImage master = ImageIO.read(getClass().getResource("/Scaled.png"));
//                background = applyShadow(master, 5, Color.BLACK, 0.5f);
//            } catch (IOException ex) {
//                Logger.getLogger(TestImageDropShadow.class.getName()).log(Level.SEVERE, null, ex);
//            }
//        }
//
//        @Override
//        public Dimension getPreferredSize() {
//            return background == null ? super.getPreferredSize() : new Dimension(background.getWidth(), background.getHeight());
//        }
//
//        @Override
//        protected void paintComponent(Graphics g) {
//            super.paintComponent(g);
//            if (background != null) {
//                int x = (getWidth() - background.getWidth()) / 2;
//                int y = (getHeight() - background.getHeight()) / 2;
//                g.drawImage(background, x, y, this);
//            }
//        }
//
//    }
//
//    public static void applyQualityRenderingHints(Graphics2D g2d) {
//        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
//        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
//        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
//        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
//        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
//    }
//
//    public static BufferedImage createCompatibleImage(int width, int height) {
//        return createCompatibleImage(width, height, Transparency.TRANSLUCENT);
//    }
//
//    public static BufferedImage createCompatibleImage(int width, int height, int transparency) {
//        BufferedImage image = getGraphicsConfiguration().createCompatibleImage(width, height, transparency);
//        image.coerceData(true);
//        return image;
//    }
//
//    public static BufferedImage createCompatibleImage(BufferedImage image) {
//        return createCompatibleImage(image, image.getWidth(), image.getHeight());
//    }
//
//    public static BufferedImage createCompatibleImage(BufferedImage image,
//            int width, int height) {
//        return getGraphicsConfiguration().createCompatibleImage(width, height, image.getTransparency());
//    }
//
//    public static GraphicsConfiguration getGraphicsConfiguration() {
//        return GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
//    }
//
//    public static BufferedImage generateMask(BufferedImage imgSource, Color color, float alpha) {
//        int imgWidth = imgSource.getWidth();
//        int imgHeight = imgSource.getHeight();
//
//        BufferedImage imgBlur = createCompatibleImage(imgWidth, imgHeight);
//        Graphics2D g2 = imgBlur.createGraphics();
//        applyQualityRenderingHints(g2);
//
//        g2.drawImage(imgSource, 0, 0, null);
//        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
//        g2.setColor(color);
//
//        g2.fillRect(0, 0, imgSource.getWidth(), imgSource.getHeight());
//        g2.dispose();
//
//        return imgBlur;
//    }
//
//    public static BufferedImage generateBlur(BufferedImage imgSource, int size, Color color, float alpha) {
//        GaussianFilter filter = new GaussianFilter(size);
//
//        int imgWidth = imgSource.getWidth();
//        int imgHeight = imgSource.getHeight();
//
//        BufferedImage imgBlur = createCompatibleImage(imgWidth, imgHeight);
//        Graphics2D g2 = imgBlur.createGraphics();
//        applyQualityRenderingHints(g2);
//
//        g2.drawImage(imgSource, 0, 0, null);
//        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN, alpha));
//        g2.setColor(color);
//
//        g2.fillRect(0, 0, imgSource.getWidth(), imgSource.getHeight());
//        g2.dispose();
//
//        imgBlur = filter.filter(imgBlur, null);
//
//        return imgBlur;
//    }
//
//    public static BufferedImage applyShadow(BufferedImage imgSource, int size, Color color, float alpha) {
//        BufferedImage result = createCompatibleImage(imgSource, imgSource.getWidth() + (size * 2), imgSource.getHeight() + (size * 2));
//        Graphics2D g2d = result.createGraphics();
//        g2d.drawImage(generateShadow(imgSource, size, color, alpha), size, size, null);
//        g2d.drawImage(imgSource, 0, 0, null);
//        g2d.dispose();
//
//        return result;
//    }
//
//    public static BufferedImage generateShadow(BufferedImage imgSource, int size, Color color, float alpha) {
//        int imgWidth = imgSource.getWidth() + (size * 2);
//        int imgHeight = imgSource.getHeight() + (size * 2);
//
//        BufferedImage imgMask = createCompatibleImage(imgWidth, imgHeight);
//        Graphics2D g2 = imgMask.createGraphics();
//        applyQualityRenderingHints(g2);
//
//        int x = Math.round((imgWidth - imgSource.getWidth()) / 2f);
//        int y = Math.round((imgHeight - imgSource.getHeight()) / 2f);
//        g2.drawImage(imgSource, x, y, null);
//        g2.dispose();
//
//        // ---- Blur here ---
//
//        BufferedImage imgGlow = generateBlur(imgMask, (size * 2), color, alpha);
//
//        return imgGlow;
//    }
//
//    public static Image applyMask(BufferedImage sourceImage, BufferedImage maskImage) {
//        return applyMask(sourceImage, maskImage, AlphaComposite.DST_IN);
//    }
//
//    public static BufferedImage applyMask(BufferedImage sourceImage, BufferedImage maskImage, int method) {
//        BufferedImage maskedImage = null;
//        if (sourceImage != null) {
//
//            int width = maskImage.getWidth(null);
//            int height = maskImage.getHeight(null);
//
//            maskedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
//            Graphics2D mg = maskedImage.createGraphics();
//
//            int x = (width - sourceImage.getWidth(null)) / 2;
//            int y = (height - sourceImage.getHeight(null)) / 2;
//
//            mg.drawImage(sourceImage, x, y, null);
//            mg.setComposite(AlphaComposite.getInstance(method));
//
//            mg.drawImage(maskImage, 0, 0, null);
//
//            mg.dispose();
//        }
//        return maskedImage;
//    }
//}