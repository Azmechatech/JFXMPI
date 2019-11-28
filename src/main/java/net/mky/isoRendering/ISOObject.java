package net.mky.isoRendering;

/*
 * ISOObject
 * Desc: Frame ( parent pbject ) for all other objects
 * represented in an isometric environment.
 */

import java.awt.Image;
import java.awt.Graphics;
import java.lang.Math.*;

public abstract class ISOObject {
	// Abstract class ( cannot be instantiated )
	// Holds all base values/variables/methods
	// For objects represented in an isometric world.

	protected SpriteBank spriteBank;

	private static int objects = 0;
	// amount of ISOObjects that exists altogether.
	// ( +1 is added whenever an ISOObject is created
	// ( a child of ISOObject to be more exact ).
	protected int x; // x position
	protected int y; // y position
	protected int z; // z position ( lowest position (floor) of object )
	protected int h; // height ( z+h = highest position (roof) of object )
	
	protected int internalHOff;
	protected int internalVOff;
	
	// protected int depth;
	/*
	 * Only used as a method ( much much simpler ) Depth = ( x + y + z );
	 * IMPORTANT! for drawing objects to the screen in the correct order.
	 */
	protected Image sprite; // sprite: visual representation of the object
	protected String spriteDest;
	protected String spriteName;
	protected String name; // Name of the object ( f.ex: tree, table2, block16 )
	protected String desc; // Description of the object. ex: "A small flower"

	// Constructor
	public ISOObject(SpriteBank sprB) {
		objects++;
		// adds 1 to the 'objects' instance counter
		// whenever a new subclass of ISOObject is created.
		spriteBank = sprB;
	}

	// Methods
	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public int getH() {
		return h;
	}

	public int getDepth() {
		return (x + y + z);
	}

	public Image getSprite() {
		return sprite;
	}

	public String getSpriteDest() {
		return spriteDest;
	}

	public String getSpriteName() {
		return spriteName;
	}

	public String getName() {
		return name;
	}

	public String getDesc() {
		return desc;
	}

	private int Tile_W = 32;
	private int Tile_H = 16;

	public void setSprite(Image spr) {
		this.sprite = spr;
		reload();
	}
	
	public void reload() {
		if (sprite == null) return;
		
	}
	
	public void setInternalOff() {
		if (sprite == null) return;
		
		internalHOff = sprite.getWidth(null)/2;
		internalVOff = sprite.getHeight(null);
	}
	public void setInternalOff(int h, int v) {
		internalHOff = h;
		internalVOff = v;
	}

	public boolean classExists(String className) {
		return true;
		// Checks if className exists ( if we can use it )
		// Because we need to check if we can access the sprite
		// loading class to load our sprites/images
		/*
		 * try { Class.forName(className); return true; // className exists }
		 * catch (ClassNotFoundException e) {
		 * System.out.println("ERROR: Can't find SpriteBank class"); return
		 * false; // className not found // package.className also works }
		 */
	}

	public boolean loadSprite(String sprName) {
		// Loads the sprite through the SpriteBank class
		// returns true if successful.
		if (classExists("SpriteBank") == false) {
			// Unable to find/access the "SpriteBank" class.
			System.out.println("Can't load sprite, SpriteBank"
					+ " class doesn't exist");
			return false;
		} else {
			// SpriteBank class found
			sprite = spriteBank.getSprite(sprName);
			reload();
			if (sprite == null) {
				// Error occured
				System.out.println("ERROR: Couldn't find sprite + " + sprName);
				return false;
			}
			return true;
		}
	}

	// method to convert position into isometric position
	private int isoX() {
		// convert x into isometric x
		int isoX;
		isoX = (int) ((x * Tile_W / 2) - (y * Tile_W / 2));

		return isoX;
	}

	private int isoY() {
		// convert y into isometric y
		int isoY;
		isoY = (int) ((x * Tile_H / 2) + (y * Tile_H / 2));

		return isoY;
	}

	//
	// paint methods
	//
	public void paint(Graphics g) {
		// Paints the objects corresponding sprite
		if (sprite == null)
			return; // no sprite = do nothing.
		g.drawImage(sprite, isoX() - internalHOff, isoY() - internalVOff - this.z, null);
	}

	public void paintOff(Graphics g, int hOff, int vOff) {
		// paint image with an offset
		if (sprite == null)
			return;
		g.drawImage(sprite, hOff + isoX() - internalHOff, vOff + isoY() - internalVOff - this.z, null);
	}

	public void paintOnScreen(Graphics g, Screen screen) {
		paintOff(g, -screen.x() + screen.hOff(), -screen.y() + screen.vOff());
	}

	public int getObjects() {
		return objects;
	}
}