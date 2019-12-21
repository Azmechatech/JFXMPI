package net.mky.rl;

import java.awt.*;
import java.awt.image.*;

public class boardObject {
	static final int O_IMG = 0, O_COL = 1;
	Color objColour;
	Image objImage;
	int style;
	int xcoord, ycoord;
	
	public boardObject(Color c) {
		this.objColour = c;
		this.style = O_COL;
	}

	public boardObject(Image i) {
		this.objImage = i;
		this.style = O_IMG;
	}
	
	public void setCoords(Dimension d) { xcoord = d.width; ycoord = d.height; }
	public void setCoords(int x, int y) { xcoord = x; ycoord = y; }

	public void drawObject(Graphics g, int w, int h, ImageObserver i) { drawObject(g,xcoord,ycoord,w,h,i); }
	public void drawObject(Graphics g, int w, int h) { drawObject(g,xcoord,ycoord,w,h,null); }
	public void drawObject(Graphics g, int x, int y, int w, int h) { drawObject(g,x,y,w,h,null); }
	public void drawObject(Graphics g, int x, int y, int w, int h, ImageObserver i) {
		if (style == O_IMG) {
			// paint image
			g.drawImage(objImage, x*w, y*h, w, h, i);
		} else {
			// paint square
			g.setColor(objColour);
			g.fillRect(x*w,y*h,w,h);
		}
	}
	
	public String toString() {return objColour.toString();}
}
