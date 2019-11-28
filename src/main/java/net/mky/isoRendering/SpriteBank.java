package net.mky.isoRendering;

/*
 * SpriteBank class
 * Desc: Handles the loading of images/sprites.
 * Other objects use this class to access/load their images
 * so that no image is loaded twice and that all images are handled
 * neatly and seperately.
 * (This class was a singleton with static methods - now it's neither)
 */

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.RGBImageFilter;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

class SpriteBank {

	// echo ( used for testing )
	private boolean echo = false;
	// HashMap - used to store the images uniquely as to prevent
	// loading the same image twice.
	private HashMap<String, BufferedImage> // HashMap<K,V>
	hashMap = new HashMap<String, BufferedImage>();
	// //
	private String path = "res/"; // default path

	// c'tors
	public SpriteBank() {
	}

	public SpriteBank(String newPath) {
		// If the path to the graphics folder need to be changed.
		path = newPath;
	}

	public SpriteBank(boolean b) {
		this.echo = b;
	}

	public SpriteBank(String newPath, boolean b) {
		this(newPath);
		this.echo = b;
	}

	private synchronized BufferedImage loadSprite(String sprName) {
		// Attempts to load sprite sprName
		BufferedImage spr = null;

		URL url = this.getClass().getClassLoader().getResource(path + sprName);

		Image img = new ImageIcon(url).getImage();

		spr = imageToBufferedImage(img);

		return spr;
		// AppletStarter.getBufferedImage(sprName);

		/*
		 * InputStream is = new BufferedInputStream(
		 * getClass().getResourceAsStream(sprName) );
		 * 
		 * Image img = null; try { img = ImageIO.read(is); } catch (Exception e)
		 * { e.printStackTrace(); }
		 * 
		 * spr = imageToBufferedImage(img);
		 * 
		 * return spr;
		 */
	}

	private boolean sprExists(String sprName) {
		// checks if a sprite has already been added/loaded
		// into the hash map
		boolean b = hashMap.containsKey(sprName);
		return b;
	}

	public synchronized BufferedImage getSprite(String sprName) {
		// used by other objects to load/get their image
		// that represents their sprite name.
		if (sprExists(sprName)) {
			// Sprite already loaded.
			return hashMap.get(sprName);
		} else {
			// Sprite hasn't been loaded yet.
			if (echo)
				System.out.print("Loading new sprite... ");
			BufferedImage spr;
			spr = loadSprite(sprName);
			if (spr == null) {
				// An error occured, couldn't load sprite.
				if (echo)
					System.out.println(" Failed.");
				return null;
			} // else move on
			/*
			 * transform the loaded sprite into an image with the top left pixel
			 * color as transparent
			 */
			if (echo)
				System.out.println("Success.");
			spr = imageToBufferedImage(makeColorTransparent(spr));

			// save the loaded sprite (and now transparent) into the hashmap
			hashMap.put(sprName, spr);
			// and then return the sprite
			return spr;
		}
	}

	// Transforms a BufferedImage with a transparent color
	// into an Image ( use imagetoBufferedImage(Image imgage) method
	// to turn it back into a BufferedImage.
	private Image makeColorTransparent(BufferedImage img) {
		final Color c = new Color(img.getRGB(0, 0)); /*
													 * The color to turn
													 * transparent is in the top
													 * left corner of the image
													 */
		ImageFilter filter = new RGBImageFilter() {
			// the color to turn into transparent (opaque)
			int markerRGB = c.getRGB() | 0xFF000000;

			public final int filterRGB(int x, int y, int rgb) {
				if ((rgb | 0xFF000000) == markerRGB) {
					// Mark the alpha bits as zero - transparent
					return 0x00FFFFFF & rgb;
				} else {
					// do nothing
					return rgb;
				}
			}
		};

		ImageProducer ip = new FilteredImageSource(img.getSource(), filter);
		return Toolkit.getDefaultToolkit().createImage(ip);
	}

	// Turn an Image into a BufferedImage
	private BufferedImage imageToBufferedImage(Image image) {
		BufferedImage bufferedImage = new BufferedImage(image.getWidth(null),
				image.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2 = bufferedImage.createGraphics();
		g2.drawImage(image, 0, 0, null);
		g2.dispose();
		return bufferedImage;
		/*
		 * Source code
		 * http://stackoverflow.com/questions/665406/how-to-make-a-color
		 * -transparent-in-a-bufferedimage-and-save-as-png
		 */
	}

	public synchronized int size() {
		// return the number of images loaded into memory
		return hashMap.size();
	}

	public String getPath() {
		return path;
	}
}