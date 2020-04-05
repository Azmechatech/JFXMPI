package net.mky.image;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.DataBufferInt;
import java.awt.image.Kernel;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.JComponent;

// TODO merge shadow mask + blur in a single loop

public class DropShadowPanel extends JComponent {
    public static String KEY_BLUR_QUALITY = "blur_quality";
    public static String VALUE_BLUR_QUALITY_FAST = "fast";
    public static String VALUE_BLUR_QUALITY_HIGH = "high";
    
    protected BufferedImage shadow = null;
    protected BufferedImage original = null;

    protected float angle = 30;
    protected int distance = 5;

    protected int shadowSize = 4;
    protected float shadowOpacity = 0.5f;
    protected Color shadowColor = new Color(0x000000);

    // cached values for fast painting
    protected int distance_x = 0;
    protected int distance_y = 0;
    
    protected HashMap<Object, Object> hints;

    protected DropShadowPanel() {
        computeShadowPosition();
        hints = new HashMap<Object, Object>();
        hints.put(KEY_BLUR_QUALITY, VALUE_BLUR_QUALITY_FAST);
    }
    
    public DropShadowPanel(String imageName) {
        this();
        setSubject(imageName);
    }
    
    public DropShadowPanel(URL imageUrl) {
        this();
        setSubject(imageUrl);
    }
    
    public DropShadowPanel(File imageFile) {
        this();
        setSubject(imageFile);
    }
    
    public DropShadowPanel(BufferedImage image) {
        this();
        setSubject(image);
    }

    // TODO use an enum?
    public void setRenderingHint(Object hint, Object value) {
        hints.put(hint, value);
    }
    
    @Override
    public Dimension getPreferredSize() {
        Dimension size;
        if (original == null) {
            size = new Dimension(50, 50);
        } else {
            size = new Dimension(original.getWidth() + (distance + shadowSize) * 2,
                                 original.getHeight() + (distance + shadowSize) * 2);
        }
        return size;
    }

    public void setSubject(String imageName) {
        URL imageUrl = DropShadowPanel.class.getResource(imageName);
        setSubject(imageUrl);
    }

    public void setSubject(URL imageUrl) {
        if (imageUrl != null) {
            try {
                BufferedImage subject = ImageIO.read(imageUrl);
                setSubject(subject);
            } catch (IOException e) {
                this.original = null;
                this.original = null;
            }
        } else {
            this.original = null;
            this.original = null;
        }
    }

    public void setSubject(File imageFile) {
        if (imageFile != null) {
            try {
                BufferedImage subject = ImageIO.read(imageFile);
                setSubject(subject);
            } catch (IOException e) {
                this.original = null;
                this.original = null;
            }
        } else {
            this.original = null;
            this.original = null;
        }
    }

    public void setSubject(BufferedImage subject) {
        if (subject != null) {
            this.original = subject;
            refreshShadow();
        } else {
            this.original = null;
            this.original = null;
        }
    }

    public float getAngle() {
        return angle;
    }

    public void setAngle(float angle) {
        this.angle = angle;
        computeShadowPosition();
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
        computeShadowPosition();
    }

    public Color getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(Color shadowColor) {
        if (shadowColor != null) {
            this.shadowColor = shadowColor;
            refreshShadow();
        }
    }

    public float getShadowOpacity() {
        return shadowOpacity;
    }

    public void setShadowOpacity(float shadowOpacity) {
        this.shadowOpacity = shadowOpacity;
        refreshShadow();
    }

    public int getShadowSize() {
        return shadowSize;
    }

    public void setShadowSize(int shadowSize) {
        this.shadowSize = shadowSize;
        refreshShadow();
    }

    public void refreshShadow() {
        if (original != null) {
            shadow = createDropShadow(original);
        }
    }

    private void computeShadowPosition() {
        double angleRadians = Math.toRadians(angle);
        distance_x = (int) (Math.cos(angleRadians) * distance);
        distance_y = (int) (Math.sin(angleRadians) * distance);
    }

    private BufferedImage prepareImage(BufferedImage image) {
        BufferedImage subject = new BufferedImage(image.getWidth() + shadowSize * 2,
                                                  image.getHeight() + shadowSize * 2,
                                                  BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = subject.createGraphics();
        g2.drawImage(image, null, shadowSize, shadowSize);
        g2.dispose();

        return subject;
    }

    public BufferedImage createDropShadow(BufferedImage image) {
        BufferedImage subject = prepareImage(image);

        if (hints.get(KEY_BLUR_QUALITY) == VALUE_BLUR_QUALITY_HIGH) {
            BufferedImage shadow = new BufferedImage(subject.getWidth(),
                                                     subject.getHeight(),
                                                     BufferedImage.TYPE_INT_ARGB);
            BufferedImage shadowMask = createShadowMask(subject);
            getLinearBlurOp(shadowSize).filter(shadowMask, shadow);
            return shadow;
        }

        applyShadow(subject);
        return subject;
    }

    private void applyShadow(BufferedImage image) {
        int dstWidth = image.getWidth();
        int dstHeight = image.getHeight();

        int left = (shadowSize - 1) >> 1;
        int right = shadowSize - left;
        int xStart = left;
        int xStop = dstWidth - right;
        int yStart = left;
        int yStop = dstHeight - right;

        int shadowRgb = shadowColor.getRGB() & 0x00FFFFFF;

        int[] aHistory = new int[shadowSize];
        int historyIdx = 0;

        int aSum;

        int[] dataBuffer = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int lastPixelOffset = right * dstWidth;
        float sumDivider = shadowOpacity / shadowSize;

        // horizontal pass

        for (int y = 0, bufferOffset = 0; y < dstHeight; y++, bufferOffset = y * dstWidth) {
            aSum = 0;
            historyIdx = 0;
            for (int x = 0; x < shadowSize; x++, bufferOffset++) {
                int a = dataBuffer[bufferOffset] >>> 24;
                aHistory[x] = a;
                aSum += a;
            }

            bufferOffset -= right;

            for (int x = xStart; x < xStop; x++, bufferOffset++) {
                int a = (int) (aSum * sumDivider);
                dataBuffer[bufferOffset] = a << 24 | shadowRgb;

                // substract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];

                // get the lastest pixel
                a = dataBuffer[bufferOffset + right] >>> 24;
                aHistory[historyIdx] = a;
                aSum += a;

                if (++historyIdx >= shadowSize) {
                    historyIdx -= shadowSize;
                }
            }
        }

        // vertical pass
        for (int x = 0, bufferOffset = 0; x < dstWidth; x++, bufferOffset = x) {
            aSum = 0;
            historyIdx = 0;
            for (int y = 0; y < shadowSize; y++, bufferOffset += dstWidth) {
                int a = dataBuffer[bufferOffset] >>> 24;
                aHistory[y] = a;
                aSum += a;
            }

            bufferOffset -= lastPixelOffset;

            for (int y = yStart; y < yStop; y++, bufferOffset += dstWidth) {
                int a = (int) (aSum * sumDivider);
                dataBuffer[bufferOffset] = a << 24 | shadowRgb;

                // substract the oldest pixel from the sum
                aSum -= aHistory[historyIdx];

                // get the lastest pixel
                a = dataBuffer[bufferOffset + lastPixelOffset] >>> 24;
                aHistory[historyIdx] = a;
                aSum += a;

                if (++historyIdx >= shadowSize) {
                    historyIdx -= shadowSize;
                }
            }
        }
    }

    private BufferedImage createShadowMask(BufferedImage image) {
        BufferedImage mask = new BufferedImage(image.getWidth(),
                                               image.getHeight(),
                                               BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = mask.createGraphics();
        g2d.drawImage(image, 0, 0, null);
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_IN,
                                                    shadowOpacity));
        g2d.setColor(shadowColor);
        g2d.fillRect(0, 0, image.getWidth(), image.getHeight());
        g2d.dispose();

        return mask;
    }

    private ConvolveOp getLinearBlurOp(int size) {
        float[] data = new float[size * size];
        float value = 1.0f / (float) (size * size);
        for (int i = 0; i < data.length; i++) {
            data[i] = value;
        }
        return new ConvolveOp(new Kernel(size, size, data));
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (shadow != null) {
            int x = (getWidth() - shadow.getWidth()) / 2;
            int y = (getHeight() - shadow.getHeight()) / 2;
            g.drawImage(shadow, x + distance_x, y + distance_y, null);
        }

        if (original != null) {
            int x = (getWidth() - original.getWidth()) / 2;
            int y = (getHeight() - original.getHeight()) / 2;
            g.drawImage(original, x, y, null);
        }
    }
}
