package net.mky.isoRendering;

/*
 * Screen class
 * holds x,y position of the TOP LEFT corner of the screen
 * as well as the screens width and height
 */

import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

class Screen {
	private int x;
	private int y;
	private int width;
	private int height;
	private int hOff = 32;
	private int vOff = 32;

	// constructors
	public Screen(int x, int y, int w, int h) {
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		// hOff = 0;
		// vOff = 0; // default hOff and vOff
	}

	public Screen(int x, int y, int w, int h, int hoff, int voff) {
		this(x, y, w, h);
		hOff = hoff;
		vOff = voff;
	}

	// methods
	public void move(int x, int y) {
		// System.out.println("Moving screen");
		this.x += x;
		this.y += y;
	}

	public Dimension getPreferredSize() {
		return new Dimension(width + hOff * 4, height + vOff * 4);
	}

	// get methods
	public int x() {
		return x;
	}

	public int y() {
		return y;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public int hOff() {
		return hOff;
	}

	public int vOff() {
		return vOff;
	}

	// paint methods
	public void drawRealRect(Graphics g) {
		// draw the real positoin of the screen
		Color c = g.getColor();
		g.setColor(Color.blue);
		g.drawRect(x, y, width, height);
		g.setColor(c);
	}

	public void drawRelativeRect(Graphics g) {
		// draw the relative position. I.e, where the screen is painted
		// on the monitor/frame/canvas
		g.drawRect(hOff, vOff, width, height);
	}
}