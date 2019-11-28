package net.mky.isoRendering;

/*
 * ISOPanel
 * Desc: A Java Panel (JPanel) where isometric objects will be drawn.
 * An Isometric "Camera".
 *
 * Handles drawing and painting of isometric objects in
 * an isometric view.
 */

import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

public class ISOPanel extends JPanel {

	// Test code
	SpriteBank spriteBank;
	ISOSurface surf;
	Screen screen;
	private int width, height;

	boolean canQuake = true;
	
	// Load images
	// image loading is done by SpriteBank

	// Constructor
	public ISOPanel() {

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

	public ISOPanel(SpriteBank sprB, int w, int h, Screen scr) {
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
		return new Dimension(width, height);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		surf.paintScreen(g, screen, true);
		screen.drawRealRect(g);
		screen.drawRelativeRect(g);
	}

	public void moveScreen(int x, int y) {
		screen.move(x, y);
		repaint();
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
	
	// Effect objects
	class QuakeFX implements Runnable {

		final ISOPanel panel;
		final int z;
		
		QuakeFX(ISOPanel panel, int z) {
			this.panel = panel;
			this.z = z;
		}
		
		@Override
		public void run() {
			int count = 0;
			while (count < 6) {
				count++;
				panel.surf.earthquake(z);
				try {
					Thread.sleep(150);
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
			
			panel.canQuake = true;
		}
		
	}
}