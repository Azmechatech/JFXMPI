package net.mky.isoRendering;

public class ISOFloor extends Block {

	public ISOFloor(SpriteBank sprB, int x, int y, int z, String sprName) {
		super(sprB, x, y, z, sprName);
		this.h = 1;
		setInternalOff(16, 16);
	}
	
}
