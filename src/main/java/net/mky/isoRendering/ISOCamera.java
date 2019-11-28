package main;

/*
 * ISOCamera
 * Desc: Displays ISOObjects within a "screen"
 */

import java.awt.Dimension;

class ISOCamera {

	private int tileWidth = 34; // 34x25 pixels as default
	private int tileHeight = 25;
	private int x = 0; // x and y position of the camera on the "map"
	private int y = 0;
	private int height;
	private int width;

	public ISOCamera(int height, int width) {
		this.height = height;
		this.width = width;
	}

	public ISOCamera(int height, int width, int tw, int th) {
		this(height, width);
		tileWidth = tw;
		tileHeight = th;
	}
}