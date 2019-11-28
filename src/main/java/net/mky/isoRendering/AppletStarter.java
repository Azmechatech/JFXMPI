package net.mky.isoRendering;

import java.applet.Applet;
import java.awt.Event;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Random;

import javax.sound.midi.MidiSystem;
import javax.sound.midi.Sequence;
import javax.sound.midi.Sequencer;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * 
 * @author Ada
 *
 */


public class AppletStarter extends Applet implements Runnable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final boolean debugging = true;
	
	
	String helpText = "	Controls\n" +
			"Arrow Keys	- Move Screen\n" +
			"Ctrl	- Speed increase\n" +
			"Space	- Earthquake\n" +
			"V	- Chaos\n\n" +
			"1	- Add Light Green Tile\n" +
			"2	- Add Medium Green Tile\n" +
			"3	- Add Dark Green Tile\n\n" +
			"Q	- Raise Tile\n" +
			"A	- Lower Tile\n\n" +
			"W	- Add Pink Floor\n" +
			"S	- Add Blue Block\n\n" +
			"E	- Raise Platform\n" +
			"D	- Lower Platform\n\n" +
			"Z	- Align World\n" +
			"X	- Flatten World\n\n" +
			"M	- Toggle Music on/off\n" +
			"T	- Reset World\n" +
			"F1	- Controls (this page)";
	String[] textArray;

	private long framePressTime = 0L;
	
	boolean showHelp = false;
	
	JFrame helpFrame = null;
	
	boolean toggleMusic = false; // Music is off by default.
	long toggleMusicTime = 0L;
	
	Sequencer sequencer;
	
	// Random number generator
	Random random = new Random();
	
	int move_speed = 2; // Default move speed.

	boolean[] keys = new boolean[1000];
	boolean screen_action = false;
	boolean screen_action2 = false;
	
	Thread thread = null;
	
	private ISOCanvas canvas = null;
	
	public void init() {

		// Play midi music
		try {
	        // From file
	        //Sequence sequence = MidiSystem.getSequence(new File("midiaudiofile"));
	    
	        // From URL
	        //sequence = MidiSystem.getSequence(new URL("http://hostname/midiaudiofile"));
	    
			URL url = this.getClass().getClassLoader().getResource("music/music1.mid");
			
			Sequence seq = MidiSystem.getSequence(url);
			
	        // Create a sequencer for the sequence
	        sequencer = MidiSystem.getSequencer();
	        sequencer.open();
	        //sequencer.setSequence(seq);
	        
	        // Loop
	        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
	        
	        // Start playing
	        if (toggleMusic)
	        	sequencer.start();
	        
		}
		catch (Exception e) {
			//e.printStackTrace();
			System.out.println("Failed to play music.");
			// Continue without music.
		}
		
		setIgnoreRepaint(true);
		
		canvas = new ISOCanvas();;
		canvas.addKeyListener(new Input());
		
		this.add(canvas);
		canvas.requestFocus();
	}

	public void start() {
		if (debugging && thread != null) thread.interrupt();
		// define a new thread
		thread = new Thread(this);
		
		// start this thread
		thread.start();
	}

	@SuppressWarnings("unused")
	public void stop() {
		sequencer.stop();
		if (debugging) return;
		try {
			Thread.sleep(100000);
		} catch (InterruptedException e) {
			thread.notify();
		}
	}

	public void destroy() {
	}

	@Override
	public void update(Graphics g) {
		// Override update to ignore clearing the screen.
		paint(g);
	}
	
	public void paint(Graphics g) {
		g.drawString("Press F1 for Controls.", canvas.getX(), 
				canvas.getY() + canvas.getHeight() + 
				this.getFontMetrics(this.getFont()).getHeight());
	}
	
	private void render() {
		canvas.repaint();
		repaint();
	}
	
	public void run() {
		
		while (true) {
			// Update Logic
			updateLogic();

			// Render
			render();
			
			// sleep
			try {
				// Stop thread for a while
				Thread.sleep(33);
			} catch (InterruptedException e) {
				// do nothing
			}
			
		}
	}

	// Event handling methods
	// Mouse events
	public boolean mouseDown(Event e, int x, int y) {
		return true;
	}

	// Keyboard events
	public boolean keyDown(Event e, int key) {
		return true;
	}

	private void updateLogic() {

		// check keyboard events
		updateInput();

		updateScreen();
	}

	/**
	 * Handles the keyboard input.
	 */
	private void inputAddBlock(String sprName) {
		ISOColumn col = canvas.surf.getColumn(
				canvas.screen.x() + canvas.screen.width()/2, 
				canvas.screen.y() + canvas.screen.height()/2);
		col.addBlock(sprName);
	}
	
	private void updateInput() {

		if (canvas == null)
			return;

		int speed = move_speed;
		int move_x = 0, move_y = 0;

		if (keys[KeyEvent.VK_DOWN])
			move_y = 1;
		if (keys[KeyEvent.VK_UP])
			move_y = -1;
		if (keys[KeyEvent.VK_LEFT])
			move_x = -1;
		if (keys[KeyEvent.VK_RIGHT])
			move_x = 1;

		if (keys[KeyEvent.VK_CONTROL])
			speed *= 2;
		
		// Toggle music on off
		if (keys[KeyEvent.VK_M]) {
			if ( (toggleMusicTime + 1000) < System.currentTimeMillis()) {
				if (toggleMusic) {
					sequencer.stop();
					toggleMusic = false;
				} else {
					sequencer.start();
					toggleMusic = true;
				}
				
				toggleMusicTime = System.currentTimeMillis();
			}
		}
		
		if (keys[KeyEvent.VK_F1]) {
			if ((framePressTime + 3000) > System.currentTimeMillis() ) return;
			framePressTime = System.currentTimeMillis();
			if (helpFrame == null) {
				helpFrame = new JFrame("Help");
				helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
				
				helpFrame.addWindowListener(new WindowAdapter() {
					public void windowClosing(WindowEvent e) {
						helpFrame = null;
						keys[KeyEvent.VK_F1] = false;
					}
				});
				
				JTextArea text = new JTextArea(helpText);
				helpFrame.add(text);
				
				helpFrame.pack();
				helpFrame.setVisible(true);
			}
		}
		
		// Move the middle column up/down
		if (keys[KeyEvent.VK_Q]) {
			ISOColumn col = canvas.surf.getColumn(
					canvas.screen.x() + canvas.screen.width()/2, 
					canvas.screen.y() + canvas.screen.height()/2);
			col.move(1);
		}
		if (keys[KeyEvent.VK_A]) {
			ISOColumn col = canvas.surf.getColumn(
					canvas.screen.x() + canvas.screen.width()/2, 
					canvas.screen.y() + canvas.screen.height()/2);
			col.move(-1);
		}
		
		// Move a platform of columns up/down
		if (keys[KeyEvent.VK_E]) {
			ISOColumn[] cols = canvas.surf.getColumns(
					canvas.screen);
			for (ISOColumn c : cols) {
				c.move(1);
			}
		}
		if (keys[KeyEvent.VK_D]) {
			ISOColumn[] cols = canvas.surf.getColumns(
					canvas.screen);
			for (ISOColumn c : cols)
				c.move(-1);
		}
		
		// Reset the levels
		if (keys[KeyEvent.VK_X]) {
			canvas.surf.clearToTop(-25);
		}
		// pile all columns together.
		if (keys[KeyEvent.VK_Z]) {
			canvas.surf.pile(-25);
		}
		if (keys[KeyEvent.VK_T]) {
			canvas.surf.reset();
		}
		
		if (keys[KeyEvent.VK_V]) {
			// Chaos
			screen_action2 = true;
		}
		
		// Add different grass colours
		if (keys[KeyEvent.VK_1])
			inputAddBlock("Grass1.png");
		if (keys[KeyEvent.VK_2])
			inputAddBlock("Grass2.png");
		if (keys[KeyEvent.VK_3])
			inputAddBlock("Grass3.png");
		
		// Add new blocks to the center of the screen.
		if (keys[KeyEvent.VK_W]) { // Add default ( blue ) block
			ISOColumn col = canvas.surf.getColumn(
					canvas.screen.x() + canvas.screen.width()/2, 
					canvas.screen.y() + canvas.screen.height()/2);
			col.addFloor("isoTile_pink.png");
		}
		if (keys[KeyEvent.VK_S]) { // Add Pink block
			ISOColumn col = canvas.surf.getColumn(
					canvas.screen.x() + canvas.screen.width()/2, 
					canvas.screen.y() + canvas.screen.height()/2);
			col.addBlock();
		}
		
		canvas.moveScreen(move_x * speed * 2, move_y * speed);

		if (keys[KeyEvent.VK_SPACE]) {
			screen_action = true;
		}
	}

	public void updateScreen() {
		if (screen_action) {
			screen_action = false;
			canvas.earthquake(2);
		} else if (screen_action2) {
			screen_action2 = false;
			canvas.chaos(4);
		}
	}
	
	/**
	 * Input
	 * @author Ada
	 *
	 */
	class Input implements KeyListener {
		@Override
		public void keyPressed(KeyEvent e) {
			int key = e.getKeyCode();
			keys[key] = true;
		}

		@Override
		public void keyReleased(KeyEvent e) {
			int key = e.getKeyCode();
			keys[key] = false;
		}

		@Override
		public void keyTyped(KeyEvent e) {
		}
	}
	
}
