package net.mky.isoRendering;

/*
 * ISOSurface
 * Desc: holds a 2D array of ISOColumns
 * consisting of ISOObjects.
 *
 * the surface defines a complete isometric surface with height.
 * essentially a "room".
 */

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Random;

class ISOSurface {

	/** Instance variables */
	ISOColumn[][] colArray;
	final int WIDTH;
	final int HEIGHT;
	SpriteBank spriteBank;

	final int actualWidth;
	final int actualHeight;

	final int Tile_W = 32;
	final int Tile_H = 16;
	final int Tile_HW = Tile_W / 2;
	final int Tile_HH = Tile_H / 2;
	int vOff = 0;
	int hOff = 0;

	/** Constructors */
	ISOSurface(SpriteBank sprB, int w, int h) {
		spriteBank = sprB;
		WIDTH = w;
		HEIGHT = h;
		vOff = 0; // vertical Offset
		hOff = 0;//HEIGHT * Tile_W / 2; // horizontal offset

		actualWidth = -(w * Tile_HW);
		actualHeight = (h * Tile_H);

		// vOff = 0;
		// hOff = 0;

		System.out.println("Creating Columns: [" + WIDTH + "] x [" + HEIGHT
				+ "]");
		colArray = new ISOColumn[WIDTH][HEIGHT];

		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				colArray[i][j] = new ISOColumn(spriteBank, i, j, isoX(i, j),
						isoY(i, j));
			}
		}
	}

	ISOSurface(SpriteBank sprB, int w, int h, boolean b) {
		// loads a default map ( used for testing )
		this(sprB, w, h); // initialize the map
		if (b == true) {
			// Load a block in each column ( for testing )
			addSurface(0);
		}
	}

	/** Methods */
	// Fun/Interactive methods
	public void earthquake(int z) {
		Random gen = new Random();
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				//colArray[i][j].getObj(0).setZ(gen.nextInt(z));
				colArray[i][j].move(gen.nextInt(z) - gen.nextInt(z));
			}
		}
	}
	
	public void moveChaos(int distance) {
		// Move columns blocks in random z directions.
		
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				int size = colArray[i][j].arrayList.size();
				
				if (size < 5) continue;
				
				ISOObject obj = colArray[i][j].getObj(0);
				
				int pz = obj.getZ();
				
				for(int k = 1; k < size; k++) {
					obj = colArray[i][j].getObj(k);
					obj.z = pz;
					pz += obj.getH() + distance;
					
				}
			}
		}
	}
	
	/**
	 * Even the field.
	 * @param z Height to reset to.
	 */
	public void clearToTop(int z) {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				colArray[i][j].clearToTop(z);
			}
		}
	}
	
	public void pile(int z) {
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				colArray[i][j].pile(z);
			}
		}
	}
	
	public void reset() {
		// Reset everything from the columns and add another surface
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				colArray[i][j].clear();
			}
		}
		
		this.addGrass(0);
	}
	
	// Get a specific Column from the surface
	public ISOColumn getColumn(int x, int y) {
		int i = isoToI(x,y);
		int j = isoToJ(x,y);
		
		return colArray[hWrap(i)][vWrap(j)];
	}
	public ISOColumn[] getColumns(Screen scr) {
		
		int sx = scr.x() + scr.width()/2;
		int sy = scr.y() + scr.height()/2;
		int sw = scr.width();
		int sh = scr.height();

		int iStart = isoToI(sx, sy) -1;
		int jStart = isoToJ(sx, sy) -1;

		int dist = 3;
		
		// ArrayList to store our return array
		ArrayList<ISOColumn> array = new ArrayList<ISOColumn>(sw*sh);
		
		for(int i=0; i < dist; i++) {
			for(int j=0; j < dist; j++) {
				array.add(
						colArray[hWrap(iStart+i)][vWrap(jStart+j)]
						);
			}
		}
		
		ISOColumn[] cols = new ISOColumn[array.size()];
		array.toArray(cols);
		return cols;
	}

	// Functionality methods
	public void addSurface(int height) {
		// Adds a block in each column at a specified height ( z value )
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				colArray[i][j].add(new Block(spriteBank, i, j, height));
			}
		}
	}

	public void addGrass(int height) {
		Random gen = new Random(66);
		String[] grassSprites = { "Grass1.png", "Grass2.png", "Grass3.png" };
		// Adds a block in each column at a specified height ( z value )
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				String spr = grassSprites[gen.nextInt(3)];
				// System.out.println("Sprite chosen: " + spr);
				
				// TEST
				if (i == 0 || j == 0 || i == WIDTH-1 || j == HEIGHT-1) {
					Block b = new Block(spriteBank, i, j, height - 25);
					colArray[i][j].add(b);
				}
				else {
					Block grassBlock = new Block(spriteBank, i, j, height - 25, spr);
					colArray[i][j].add(grassBlock);
				}
			}
		}
	}

	public int hWrap(int i) {
		// horizontal wrap - wraps the map horizontally together
		// % = mod = modulo = maths
		// if(i<0) return -(i%WIDTH);
		return Math.abs((i % WIDTH));
	}

	public int vWrap(int j) {
		// vertical wrap
		// if(j<0) return -(j%HEIGHT);
		return Math.abs((j % HEIGHT));
	}

	// Functional methods
	private int isoToI(int x, int y) {
		// converts world isometric coordinates
		// into the j position of the 2D-Array
		return (((y / Tile_HH) + (x / Tile_HW)) / 2);
	}

	private int isoToJ(int x, int y) {
		// converts world isometric coordinates
		// into the j position of the 2D-Array
		return (((y / Tile_HH) - (x / Tile_HW)) / 2);
	}

	private int wrapTW(int w) {
		// wraps the tile width
		w = (w / Tile_W);
		return w * Tile_W;
	}

	private int wrapTH(int h) {
		// wraps the tile width
		h = (h / Tile_H);
		return h * Tile_H;
	}

	// Paint methods
	public void paintAll(Graphics g) {
		// Paint all Columns in (more or less) depth order.
		for (int i = 0; i < WIDTH; i++) {
			for (int j = 0; j < HEIGHT; j++) {
				colArray[i][j].paintAll(g, this.hOff, this.vOff);
			}
		}
	}// paintAll() Method ends

	public void paintBox(Graphics g, int x, int y, int w, int h) {
		// Paint a specific box from the surface
		// shows up as a diamond shape in isometrci view
		for (int i = x; i < w; i++) {
			for (int j = y; j < h; j++) {
				colArray[hWrap(i)][vWrap(j)].paintAll(g);
			}
		}
	}// paintBox() Method ends

	public void paintScreen(Graphics g, Screen scr) {
		// Screen is a class that holds data
		// start position of the screen, and its width and height
		// paint only the isometric tiles within the canvas

		// We need to even out the screen size to fit each isometric tile
		// to avoid small gaps
		int sx = scr.x();
		int sy = scr.y();
		int sw = scr.width();
		int sh = scr.height();

		int iStart = isoToI(sx, sy) - 1;
		int jStart = isoToJ(sx, sy);
		int iMax = isoToI(sx + sw, sy + sh) + 1;
		int jMax = isoToJ(sx, sy + sh) + 2;
		int jMin = isoToJ(sx + sw, sy);

		boolean nBump = false, mBump = false;
		int n = 0, nStart = 0, nBuffer = 1;
		int m = 1, mStart = 0, mBuffer = 0;

		// Fix for negative screen values
		if (sx < 0) {
			m = 3;
		}

		for (int i = iStart; i < iMax; i++) {
			for (int j = jStart - n; j < jStart + m; j++) {
				// paint the column
				colArray[hWrap(i)][vWrap(j)].paintAll(g);

			}
			// adjust m and n to keep us within the screen

			// adjust n
			if (!nBump) {
				// we have not yet reached the lowest j point
				// increment n to go even lower next iteration
				n++;
				// Check if we have reached the lowest j point
				if ((jStart - n) == jMin) {
					nBump = true;
					// System.out.println("Bump N");
				}
			} else {
				// we have reached the deepest j and are now going back
				// start decreasing after the buffer is gone
				if (nBuffer > 0) {
					nBuffer--;
				} else {
					// The buffer is gone, start decreasing n each iteration
					n--;
				}
			}

			// adjust m
			if (!mBump) {
				// we have not yet reached the HIGHEST j point
				// increasee m to go even higher next iteration
				m++;
				// Check if we have reached the highest j point
				if ((jStart + m) == jMax) {
					mBump = true;
					// System.out.println("Bump M");
				}
			} else {
				// we have reached the maximum j point
				// and are now moving back.
				// start decreasing m after the buffer is gone
				if (mBuffer > 0) {
					mBuffer--;
				} else {
					// The Buffer is gone
					// decrease m each iteration
					m--;
				}
			}
		} // for loop ends

	}// paintScreen() Method ends

	
	/**
	 * This is the most signifcant of the paint methods!!
	 * @param g
	 * @param scr A Screen is basically just a Dimension object.
	 * @param relative
	 */
	public void paintScreen(Graphics g, Screen scr, boolean relative) {
		// Screen is a class that holds data:
		// start position of the screen, and its width and height
		// paint only the isometric tiles within the screen
		// RELATIVE to the screen and window position!
		// Only the paint method get influenced by this
		// ( it adds horizontal and vertical offsets )

		if (relative == false) {
			paintScreen(g, scr);
			return;
		}

		// We need to even out the screen size to fit each isometric tile
		// to avoid small gaps
		int sx = scr.x();
		int sy = scr.y();
		int sw = scr.width();
		int sh = scr.height();

		int iStart = isoToI(sx, sy) -4;
		int jStart = isoToJ(sx, sy) -1;
		int iMax = isoToI(sx + sw, sy + sh) + 1;
		int jMax = isoToJ(sx, sy + sh) + 2;
		int jMin = isoToJ(sx + sw, sy) - 1;

		boolean nBump = false, mBump = false;
		int n = 0, nStart = 0, nBuffer = 6;
		int m = 0, mStart = 0, mBuffer = 6;

		// Fix for negative screen values
	

		for (int i = iStart; i < iMax; i++) {
			for (int j = jStart - n; j < jStart + m; j++) {
				// paint the column
				// RELATIVELY
				// colArray[ hWrap(i) ][ vWrap(j) ].paintAll(g, scr);
				colArray[hWrap(i)][vWrap(j)].paintWrap(g, scr, i, j,
						isoX(i, j), isoY(i, j), Tile_W, Tile_H, WIDTH, HEIGHT);
			}
			// adjust m and n to keep us within the screen

			// adjust n
			if (!nBump) {
				// we have not yet reached the lowest j point
				// increment n to go even lower next iteration
				n++;
				// Check if we have reached the lowest j point
				if ((jStart - n) == jMin) {
					nBump = true;
					// System.out.println("Bump N");
				}
			} else {
				// we have reached the deepest j and are now going back
				// start decreasing after the buffer is gone
				if (nBuffer > 0) {
					nBuffer--;
				} else {
					// The buffer is gone, start decreasing n each iteration
					n--;
				}
			}

			// adjust m
			if (!mBump) {
				// we have not yet reached the HIGHEST j point
				// increase m to go even higher next iteration
				m++;
				// Check if we have reached the highest j point
				if ((jStart + m) == jMax) {
					mBump = true;
					// System.out.println("Bump M");
				}
			} else {
				// we have reached the maximum j point
				// and are now moving back.
				// start decreasing m after the buffer is gone
				if (mBuffer > 0) {
					mBuffer--;
				} else {
					// The Buffer is gone
					// decrease m each iteration
					m--;
				}
			}
		} // for loop ends

	}// end of paintScreen(Graphics, Screen, boolean) Method

	// methods to convert position into isometric position
	private int isoX(int x, int y) {
		// convert x into isometric x
		int isoX;
		isoX = (int) ((x * Tile_W / 2) - (y * Tile_W / 2));

		return isoX;
	}

	private int isoY(int x, int y) {
		// convert y into isometric y
		int isoY;
		isoY = (int) ((x * Tile_H / 2) + (y * Tile_H / 2));

		return isoY;
	}

}