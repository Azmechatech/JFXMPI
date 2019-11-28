package net.mky.isoRendering;

/*
 * ISOPanel
 * Desc: A Java Panel (JPanel) where isometric objects will be drawn.
 * An Isometric "Camera".
 *
 * Handles drawing and painting of isometric objects in
 * an isometric view.
 */

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Random;

public class ISOCanvas extends Canvas {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	// Test code
	SpriteBank spriteBank;
	ISOSurface surf;
	Screen screen;
	private int width, height;

	private boolean canQuake = true;
	private boolean canChaos = true;
	
	// Load images
	// Loading images is handled by SpriteBank class
	// Test end

	// Double buffering
	Image dbImage = null;
	Graphics dbg;
	
	// Constructor
	public ISOCanvas() {

		SpriteBank spriteBank = new SpriteBank();
		
		this.surf = new ISOSurface(spriteBank, 40, 40);
		this.surf.addGrass(0);

		int i = 40*32*20-80;
		int j = 83*16*20+20;
		int k = 160;
		int m = 80;
		width = k;
		height = m;
		this.screen = new Screen(i, j, k, m, 0, 0);
		
		// default values
		// this(new SpriteBank(), 40, 40, new Screen(10, 10, 160, 80, 32, 32));
	}

	public ISOCanvas(SpriteBank sprB, int w, int h, Screen scr) {
		super();
		width = w;
		height = h;
		// initialize spriteBank
		spriteBank = sprB;
		// open a room
		surf = new ISOSurface(spriteBank, width, height);
		surf.addGrass(0);
		screen = scr;

		// int x = 100, y = 200, w = 160, h = 80;
		// screen = new Screen(x, y, w, h, 32, 32);
	}

	public ISOSurface getSurface() {
		return surf;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Dimension getPreferredSize() {
		return new Dimension(width+1, height+1);
	}
	
	@Override
	public void paint(Graphics g) {
		surf.paintScreen(g, screen, true);
		screen.drawRealRect(g);
		screen.drawRelativeRect(g);
	}

	@Override
	public void update(Graphics g) {
		if (dbImage == null) {
			dbImage = createImage(this.width, this.height);
			dbg = dbImage.getGraphics();
		}
		
		// Clear the screen.
		dbg.setColor(getBackground());
		dbg.fillRect(0, 0, this.width, this.height);
		
		// Paint
		dbg.setColor(getForeground());
		paint(dbg);
		
		g.drawImage(dbImage, 0, 0, this);
	}
	
	
	
	
	
	public void moveScreen(int x, int y) {
		screen.move(x, y);
		//repaint();
	}

	public void earthquake(int z) {
		if (!canQuake) return;
		canQuake = false;
		
		System.out.println("Earthquake!!");
		//surf.earthquake(z);
		//repaint();
		
		Thread t = new Thread( new QuakeFX(this, z) );
		t.start();
	}
	
	public void chaos(int speed) {
		if (!canChaos) return;
		canChaos = false;
		
		System.out.println("Chaos!!!");
		
		Thread t = new Thread( new ChaosFX(this, speed));
		t.start();
	}
	
	//
	// Effect objects
	//
	
	/**
	 * Chaos Effect object
	 * @author Ada
	 *
	 */
	class ChaosFX implements Runnable {

		final ISOCanvas canvas;
		final int speed;
		
		ChaosFX(ISOCanvas canvas, int speed) {
			this.canvas = canvas;
			this.speed = speed;
		}
		
		@Override
		public void run() {
			int count = 0;
			while (count < speed) {
				canvas.surf.moveChaos(count);
				count ++;
				try {
					Thread.sleep(66);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			canvas.canChaos = true;
		}		
	}
	
	// earthquake effect
	class QuakeFX implements Runnable {

		final ISOCanvas canvas;
		final int z;
		
		QuakeFX(ISOCanvas canvas, int z) {
			this.canvas = canvas;
			this.z = z;
		}
		
		@Override
		public void run() {
			int count = 0;
			while (count < 6) {
				count++;
				canvas.surf.earthquake(z);
				try {
					Thread.sleep(150);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			canvas.canQuake = true;
		}		
	}
	
}