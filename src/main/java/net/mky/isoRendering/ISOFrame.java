package net.mky.isoRendering;

/*
 * ISOFrame
 * Desc: sets up the game window
 * where isometric objects will be drawn.
 */

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import java.awt.event.*;

public class ISOFrame extends JFrame implements KeyListener {

	// ISOPanel panel = new ISOPanel(new SpriteBank(), 160, 160, new Screen(58,
	// 128, 160, 100, 20, 20));
	ISOPanel panel;

	public static void main(String[] args) {

		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				createAndShowGUI();
			}
		});
	}

	// constructor
	public ISOFrame(String name) {
		super(name);

		init();
		// panel.addKeyListener(this);
	}

	public static void createAndShowGUI() {
		System.out.println("Created GUI on EDT? "
				+ SwingUtilities.isEventDispatchThread());
		ISOFrame f = new ISOFrame("Isometric Frame");
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setMySize(); // default size

		f.setVisible(true);
		f.focusPanel();

	}

	public ISOFrame getFrame() {
		return this;
	}

	public void focusPanel() {
		panel.requestFocus();
	}

	private void init() {
		panel = new ISOPanel();
		// setMySize();
		add(panel, null);
		// this.getContentPane().
	}

	private void setMySize() {
		setSize(panel.getPreferredSize());
	}

	/** Listen for keys */
	public void keyPressed(KeyEvent e) {
		int a = 1;
		int b = 1;

		System.out.println("Key Press Event!");
		int k = e.getKeyCode();

		if (k == e.VK_LEFT) {
			panel.moveScreen(-a, 0);
		}
		if (k == e.VK_RIGHT) {
			panel.moveScreen(a, 0);
		}
		if (k == e.VK_UP) {
			panel.moveScreen(0, -b);
		}
		if (k == e.VK_DOWN) {
			panel.moveScreen(0, b);
		}
		if (k == e.VK_SPACE) {
			// do an earthquake
			panel.earthquake(8);
		}
	}

	public void keyTyped(KeyEvent e) {
		System.out.println("Key Typed Event!");
		// do nothing
	}

	public void keyReleased(KeyEvent e) {
		System.out.println("Key Released Event!");
		// do nothing
	}
}