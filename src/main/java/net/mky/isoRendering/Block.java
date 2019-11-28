package net.mky.isoRendering;

/*
 * Block
 * Desc; The general Block class
 * functions as walls, ground, blocks, obstacles
 *
 * Basically anything you can stand on.
 */

public class Block extends ISOObject {

	String spriteName = "spr_defaultBlock.png"; // Default block graphics

	// Constructors
	public Block(SpriteBank sprB, int x, int y) {
		super(sprB);
		this.x = x;
		this.y = y;
		z = 0; // Default z value
		h = 8; // Default height

		// Load sprite
		loadSprite(spriteName);
		setInternalOff(16, 25);
	}

	public Block(SpriteBank sprB, int x, int y, int z) {
		this(sprB, x, y);
		this.z = z;
	}

	public Block(SpriteBank sprB, int x, int y, int z, String sprName) {
		this(sprB, x, y, z);
		this.spriteName = sprName;
		loadSprite(spriteName);
	}

	public Block(SpriteBank sprB, int x, int y, int z, int h) {
		this(sprB, x, y, z);
		this.h = h;
	}

	public Block(SpriteBank sprB, int x, int y, int z, int h, String sprName) {
		this(sprB, x, y, z, h);
		this.spriteName = sprName;
		loadSprite(spriteName);
	}

	/** Methods */
	// Interactive Methods
	public void setZ(int z) {
		this.z = z;
	}

}