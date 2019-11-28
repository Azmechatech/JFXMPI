package net.mky.isoRendering;

/*
 * ISORoom
 * Desc: Essentially a data type.
 * has a full list of all ISOObjects in a specific room/world.
 * The objects themselves have information about their depth
 * and position etc.
 */

import java.util.*;
import java.util.Comparator;
import java.util.Collection;
import java.util.Collections;
import java.util.Collections.*;
import java.util.Iterator;
import java.awt.Graphics;

public class ISORoom {

	ArrayList<ISOObject> arrayList;
	SpriteBank spriteBank;

	// Constructor
	public ISORoom(SpriteBank sprB) {
		arrayList = new ArrayList<ISOObject>();
		spriteBank = sprB;
	}

	// Inner class
	class DepthComparator implements Comparator<ISOObject> {
		// A Comparator class to compare objects depths
		public int compare(ISOObject obj1, ISOObject obj2) {
			// compare depths
			if (obj1.getDepth() == obj2.getDepth())
				return 0;
			if (obj1.getDepth() < obj2.getDepth())
				return -1;
			else
				return 1;
		}
	}

	// Methods
	private void add(ArrayList<ISOObject> arrayList, ISOObject obj) {
		if (arrayList.size() <= 0) {
			this.arrayList.add(obj);
		}
		// adds an object to the array list at its correct position
		// ( depends on getDepth() )
		Comparator<ISOObject> c = new DepthComparator();
		int index;
		index = Collections.binarySearch(arrayList, obj, c);
		if (index < 0)
			index = -(index + 1);
		this.arrayList.add(index, obj);
	}

	public void loadFlat(int x, int y) {
		for (int i = 0; i < x; i++) {
			for (int j = 0; j < y; j++) {
				Block b = new Block(spriteBank, i, j);
				add(arrayList, b);
				// The add method adds the block sorted to the
				// arrayList depending on depth.
			}
		}
	}

	public void loadDefault() {
		// Creates a default 10x10 block map
		for (int i = 0; i < 10; i++) {
			for (int j = 0; j < 10; j++) {
				Block b = new Block(spriteBank, i, j);
				add(arrayList, b);
				// arrayList.add(b.getDepth(), b); // place them in order by
				// depth!
				// So that we don't draw object that are behind other objects
				// infront... :) - very important!
			}
		}
	}

	public void loadFromTxt() {
		// TODO
	}

	// Object handling methods
	public void paintAll(Graphics g) {
		// Invokes all objects paint() method.
		// in correct order.
		Iterator<ISOObject> itr = arrayList.iterator();

		while (itr.hasNext()) {
			// bottoms up - biggest depth painted last
			// = they will be painted ontop of everything else,
			// which is what we want
			ISOObject objRef = (ISOObject) itr.next();
			objRef.paint(g);
		}
	}

}