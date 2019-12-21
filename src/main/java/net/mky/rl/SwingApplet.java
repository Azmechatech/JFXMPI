package net.mky.rl;

//<APPLET CODE = "SwingApplet.class" WIDTH = 700 HEIGHT = 400 ></applet>

// load for early releases
//import com.sun.java.swing.*;

import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

public class SwingApplet extends JPanel  implements ActionListener,Runnable{
	static final int BW=300, BH=300, BX=8, BY=8, NUM_WALLS=20,
		SAMP_W = 100, SAMP_H = 100;
	static final int DEF_EPOCHS = 50000;
	static final long DELAY=500;
	static int MAXX=400, MAXY=400;
	
	CatAndMouseGame game;
	CatAndMouseWorld trainWorld, playWorld; // seperate world from playing world
	RLController rlc;
	RLearner rl;
		
	JTabbedPane tabbedPane;
	Container instructions, playPanel, trainPanel, worldPanel;
	
	// world setting components
	JTextField rows, cols, obst;
	sampleWorlds samples;
	boolean[][] selectedWalls;
	ButtonGroup worldSelGroup;
	boolean sampleWorld=true, designWorld=false;
	
	// instructions components
	JLabel instructLabel, usageLabel;
	final String INSTRUCT_MESSAGE = "<html><p>This applet demonstrates how reinforcement <p>learning can be used to train an agent to play <p>a simple game.  In this case the game is Cat and <p>Mouse- the mouse tries to get to the cheese <p>and back to it's hole, the cat tries to catch the mouse.",
		USAGE_MESSAGE = "<html><p>You can train the agent by selecting the Train tab.  At <p>any time you can select the Play tab to see how <p>well the agent is performing!  Of course, the more <p>training, the better the chance the mouse <p>has of surviving :)";

	// train panel components
	public static final String START="S", CONT_CHECK="C";
	final String SETTINGS_TEXT = "These settings adjust some of the internal workings of the reinforcement learning algorithm.",
		SETTINGS_TEXT2 = "Please see the web pages for more details on what the parameters do.";
	JTextField alpha, gamma, epsilon, epochs, penalty, reward;
	JButton startTraining, stopTraining;
	JRadioButton softmax, greedy, sarsa, qlearn;
	JProgressBar progress;
	JLabel learnEpochsDone;	
	
	// play panel components
	JButton startbutt, stopbutt, pausebutt;
	boardPanel bp;
	public int mousescore=0, catscore =0;
	JLabel catscorelabel, mousescorelabel;
	final String MS_TEXT = "Mouse Score:", CS_TEXT = "Cat Score:";
	JSlider speed, smoothSlider;
	Image catImg, mouseImg;
	chartPanel graphPanel;
	JLabel winPerc;
			
	boardObject cat, mouse, cheese, back, hole, wall;

        
        
        public static void main(String... args) throws IOException{
            SwingApplet sa=new SwingApplet();
            sa.init();
            JFrame frame = new JFrame("JFrame Example");  
            frame.add(sa);  
            frame.setSize(200, 300);  
            frame.setLocationRelativeTo(null);  
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
            frame.setVisible(true);  
        }
	
	public void init() throws IOException {
		// load images
		//catImg = getImage(, "cat.gif");
		//mouseImg = getImage(ClassLoader.getSystemResource("mouse.gif"));
		//Image wallImg = getImage(getCodeBase(), "wall.gif");
		//Image cheeseImg = getImage(getCodeBase(), "cheese.gif");
		Image floorImg =  ImageIO.read(this.getClass().getResourceAsStream(("/rl/floor.gif")));//getImage(getCodeBase(), "floor.gif");
		
		 catImg = ImageIO.read(this.getClass().getResourceAsStream(("/rl/cat.gif")));
		 mouseImg = ImageIO.read(this.getClass().getResourceAsStream(("/rl/mouse2.gif")));
		Image wallImg = ImageIO.read(this.getClass().getResourceAsStream(("/rl/wall.gif")));
		Image cheeseImg = ImageIO.read(this.getClass().getResourceAsStream(("/rl/cheese.gif")));

		// set up board objects
		cat = new boardObject(catImg);
		mouse = new boardObject(mouseImg);
		cheese = new boardObject(cheeseImg);
		back = new boardObject(floorImg);
		hole = new boardObject(Color.orange);
		wall = new boardObject(wallImg);
		
		// setup content panes
		tabbedPane = new JTabbedPane();
		
		//instructions = makeInstructions();
		worldPanel = makeWorldPanel();
		playPanel = makePlayPanel();
		trainPanel = makeTrainPanel();
		
		tabbedPane.addTab("World", worldPanel);
		tabbedPane.addTab("Play", playPanel);
		//tabbedPane.addTab("Instructions", instructions);
		tabbedPane.addTab("Train", trainPanel);
		tabbedPane.setSelectedIndex(0);

		// disable panes until world created
		tabbedPane.setEnabledAt(1,false);
		tabbedPane.setEnabledAt(2,false);
		
		// set up controls
		//setContentPane(new JPanel());
		//getContentPane().add(tabbedPane);
		
		add(tabbedPane);
	}

	public void worldInit(int xdim, int ydim, int numwalls) { 
		trainWorld = new CatAndMouseWorld(xdim, ydim,numwalls);
		gameInit(xdim,ydim);
	}
	public void worldInit(boolean[][] givenWalls) {
		int xdim = givenWalls.length, ydim = givenWalls[0].length;
		trainWorld = new CatAndMouseWorld(xdim, ydim,givenWalls);
		gameInit(xdim,ydim);		
	}
	private void gameInit(int xdim, int ydim) {
		// disable this pane
		tabbedPane.setEnabledAt(0,false);
		
		playWorld = new CatAndMouseWorld(xdim, ydim,trainWorld.walls);

		bp.setDimensions(xdim, ydim);
		
		rlc = new RLController(this, trainWorld, DELAY);
		rl = rlc.learner;
		rlc.start();
		
		game = new CatAndMouseGame(this, DELAY, playWorld, rl.getPolicy());
		game.start();

		// set text fields on panels
		penalty.setText(Integer.toString(trainWorld.deathPenalty));
		reward.setText(Integer.toString(trainWorld.cheeseReward));
		alpha.setText(Double.toString(rl.getAlpha()));
		gamma.setText(Double.toString(rl.getGamma()));
		epsilon.setText(Double.toString(rl.getEpsilon()));

		// enable other panes
		tabbedPane.setEnabledAt(1,true);
		tabbedPane.setEnabledAt(2,true);
		
		// switch active pane
		tabbedPane.setSelectedIndex(1);

		// set first position on board
		updateBoard();
	}
	
	// this method is triggered by SwingUtilities.invokeLater in other threads
	public void run() { updateBoard(); }
	
	/************ general functions ****************/
	public void updateBoard() {
		// update score panels
		mousescorelabel.setText(MS_TEXT+" "+Integer.toString(mousescore));
		catscorelabel.setText(CS_TEXT+" "+Integer.toString(catscore));
		if (game.newInfo) {
			updateScore();
			game.newInfo = false;
		}
		
		// update progress info
		progress.setValue(rlc.epochsdone);
		learnEpochsDone.setText(Integer.toString(rlc.totaldone));
		if (rlc.newInfo) endTraining();
		
		// update game board
		bp.clearBoard();

		// draw walls
		boolean[][] w = game.getWalls(); 
		for (int i=0; i<w.length; i++) {
			for (int j=0; j<w[0].length; j++) {
				if (w[i][j]) bp.setSquare(wall, i, j);
			}
		}

		// draw objects (cat over mouse over cheese)
		bp.setSquare(cheese, game.getCheese());
		bp.setSquare(mouse, game.getMouse());
		bp.setSquare(cat, game.getCat());
		//bp.setSquare(hole, game.getHole());
					
		// display text representation
		//System.out.println(bp);
		bp.repaint();
	}

	void doTraining() {
		// begin training
		int episodes = Integer.parseInt(epochs.getText());
		double aval = Double.parseDouble(alpha.getText());
		double gval = Double.parseDouble(gamma.getText());
		double eval = Double.parseDouble(epsilon.getText());
		int cval = Integer.parseInt(reward.getText());
		int dval = Integer.parseInt(penalty.getText());
				
		rl.setAlpha(aval);
		rl.setGamma(gval);
		rl.setEpsilon(eval);
				
		// disable controls
		startTraining.setEnabled(false);
		epochs.setEnabled(false);
		reward.setEnabled(false);
		penalty.setEnabled(false);
		alpha.setEnabled(false);
		gamma.setEnabled(false);
		epsilon.setEnabled(false);
		softmax.setEnabled(false);
		greedy.setEnabled(false);
		sarsa.setEnabled(false);
		qlearn.setEnabled(false);
				
		// fix progress bar
		progress.setMinimum(0);
		progress.setMaximum(episodes);
		progress.setValue(0);
				
		// enable stop button
		stopTraining.setEnabled(true);
				
		// start training
		trainWorld.cheeseReward = cval;
		trainWorld.deathPenalty = dval;
		rlc.setEpisodes(episodes);
	}

	void endTraining() {
		// stop training
		rlc.stopLearner();
		
		// enable buttons
		startTraining.setEnabled(true);
		epochs.setEnabled(true);
		reward.setEnabled(true);
		penalty.setEnabled(true);
		alpha.setEnabled(true);
		gamma.setEnabled(true);
		epsilon.setEnabled(true);
		softmax.setEnabled(true);
		greedy.setEnabled(true);
		sarsa.setEnabled(true);
		qlearn.setEnabled(true);

		// disable stop button
		stopTraining.setEnabled(false);
	}

	void updateScore() {
		double newScore = Math.round(1000*((double)mousescore)/(catscore + mousescore))/10;
		winPerc.setText(Double.toString(newScore)+"%");
		graphPanel.updateScores();
		graphPanel.repaint();
		game.gameActive = true;		
	}	
	/************ general functions ****************/

	/********** Methods to construct panels *************/
	Container makeWorldPanel() {
		JPanel worldPane = new JPanel();
		worldPane.setLayout(new BorderLayout());

		worldSelGroup = new ButtonGroup();
		
		worldPane.add(chooseWorld(), BorderLayout.CENTER);
		//worldPane.add(customWorld(), BorderLayout.EAST);
		JButton startbutt = new JButton("Click here to start!");
		startbutt.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// selected world type, choose action
				if (sampleWorld) {
					worldInit(selectedWalls);
				} else if (designWorld) {
					// custom designed world
				
				} else {
					// random world
					worldInit(Integer.parseInt(cols.getText()),
						Integer.parseInt(rows.getText()),
						Integer.parseInt(obst.getText()));
				}
			}
		});
		worldPane.add(startbutt, BorderLayout.SOUTH);
		return worldPane;
	}
	
	Container customWorld() {
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		
		JRadioButton random = new JRadioButton("Random World");
		random.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// enable obstacles field
				obst.setEnabled(true);
				designWorld=false;
				sampleWorld=false;
			}
		});
		worldSelGroup.add(random);
		pane.add(random, BorderLayout.NORTH);
		
		/*JRadioButton custom = new JRadioButton("Custom Design");
		custom.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				// enable obstacles field
				obst.setEnabled(false);
				designWorld=true;
				sampleWorld=false;
			}
		});*/
		
		// add controls to set dimensions
		JPanel labelpane = new JPanel();
		labelpane.setLayout(new GridLayout(0,2));

		//worldSelGroup.add(custom);
		
		//JPanel controls = new JPanel();
		//controls.setLayout(new GridLayout(0,1));
		rows = new JTextField(Integer.toString(BY), 20);
		cols = new JTextField(Integer.toString(BX), 20);
		obst = new JTextField(Integer.toString(NUM_WALLS), 20);
		
		labelpane.add(new JLabel("Rows:",JLabel.RIGHT));
		labelpane.add(rows);
		labelpane.add(new JLabel("Columns:",JLabel.RIGHT));
		labelpane.add(cols);
		labelpane.add(new JLabel("Obstacles:",JLabel.RIGHT));
		labelpane.add(obst);
		
		//labelpane.setBorder(BorderFactory.createTitledBorder("Custom World"));
		//labelpane.add(random);
		//labelpane.add(custom);

		pane.add(labelpane, BorderLayout.CENTER);
		//pane.add(controls, BorderLayout.EAST);
		//pane.add(labelpane);
		//pane.add(controls);
		
		return pane;	
	}
	
	Container chooseWorld() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(0,3));

		// grab each sample
		samples = new sampleWorlds();
		for (int i=0; i<samples.numSamples(); i++) {
			JPanel thisPanel = new JPanel();
			thisPanel.setLayout(new BorderLayout());
			
			// set first as selected
			if (i==0) selectedWalls = samples.getWalls(i);
			
			JRadioButton b = new JRadioButton(samples.getTitle(i), (i==0));
			b.setHorizontalAlignment(SwingConstants.LEFT);
			b.setActionCommand(Integer.toString(i));
			b.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e) {
					int index = Integer.parseInt(e.getActionCommand());
					selectedWalls = samples.getWalls(index);
					sampleWorld=true;
				}
			});
			
			boolean[][] w = samples.getWalls(i);
			
			// create boardPanel object for this world
			boardPanel pic = new boardPanel(back, w.length, w[0].length,SAMP_W, SAMP_H);
			// add walls to panel
			for (int x=0; x<w.length; x++)
				for (int y=0; y<w.length; y++)
					if (w[x][y]) pic.setSquare(wall, x, y);
			
			worldSelGroup.add(b); // add to button group
			pic.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			
			thisPanel.add(pic, BorderLayout.CENTER);
			thisPanel.add(b, BorderLayout.NORTH); // add to pane
			thisPanel.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
			
			pane.add(thisPanel);
		}

		// add random world option
		pane.add(customWorld());
		pane.setBorder(BorderFactory.createTitledBorder("Choose World"));
		return pane;
	}
	
	Container makeInstructions() {
		JPanel pane = new JPanel();
		pane.setLayout(new GridLayout(2,1));
		
		instructLabel = new JLabel(INSTRUCT_MESSAGE);
		usageLabel = new JLabel(USAGE_MESSAGE);
		
		pane.add(instructLabel);
		pane.add(usageLabel);
		return pane;
	}
	
	// makes the board panel and the controls to start and stop the game etc
	Container makePlayPanel() {
		JPanel pane = new JPanel();		
		//pane.setLayout(new BoxLayout(pane, BoxLayout.X_AXIS));

		// make drawable area
		pane.add(makeBoardPanel());
		
		// add buttons
		pane.add(makeButtonPane());
		

		//pane.setBackground(new Color(255,255,204));
		pane.setBorder(BorderFactory.createMatteBorder(1,1,2,2,Color.black));
				
		return pane;
	}

	Container makeTrainPanel() {
		JPanel trainPane = new JPanel();
		//trainPane.setLayout(new BoxLayout(trainPane, BoxLayout.X_AXIS));
		
		trainPane.add(makeSettingPanel());
		trainPane.add(makeParamPanel());

		return trainPane;
	}
	
	// a,g,e parameters for reinforcement learner
	Container makeParamPanel() {
		JPanel parampane = new JPanel();
		parampane.setLayout(new BorderLayout(1,2));
		
		
		JPanel labelpane = new JPanel();
		labelpane.setLayout(new GridLayout(0,1));
		labelpane.add(new JLabel("Death Penalty:", JLabel.RIGHT));
		labelpane.add(new JLabel("Cheese Reward:", JLabel.RIGHT));
		labelpane.add(new JLabel("Alpha:", JLabel.RIGHT));
		labelpane.add(new JLabel("Gamma:", JLabel.RIGHT));
		labelpane.add(new JLabel("Epsilon:", JLabel.RIGHT));
		labelpane.add(new JLabel("Action Selection Method:", JLabel.RIGHT));
		labelpane.add(new JLabel("Learning Method:", JLabel.RIGHT));
		labelpane.add(new JLabel("Epochs to train for:",JLabel.RIGHT));
		labelpane.add(new JLabel("Progress:",JLabel.RIGHT));
		labelpane.add(new JLabel("Epochs done:",JLabel.RIGHT));

		JPanel controlspane = new JPanel();
		controlspane.setLayout(new GridLayout(0,1));
		penalty = new JTextField(20);
		reward = new JTextField(20);
		alpha = new JTextField(20);
		gamma = new JTextField(20);
		epsilon = new JTextField(20);
		controlspane.add(penalty);
		controlspane.add(reward);
		controlspane.add(alpha);
		controlspane.add(gamma);
		controlspane.add(epsilon);

		JPanel actionButtons = new JPanel();
		actionButtons.setLayout(new GridLayout(1,0));
		softmax = new JRadioButton("Softmax");
		greedy = new JRadioButton("Greedy",true);
		ButtonGroup actionButts = new ButtonGroup();
		actionButts.add(softmax);
		actionButts.add(greedy);
		actionButtons.add(softmax);
		actionButtons.add(greedy);
		
		JPanel learnButtons = new JPanel();
		learnButtons.setLayout(new GridLayout(1,0));
		sarsa = new JRadioButton("SARSA");
		qlearn = new JRadioButton("Q-Learning",true);
		ButtonGroup learnButts = new ButtonGroup();
		learnButts.add(sarsa);
		learnButts.add(qlearn);
		learnButtons.add(sarsa);
		learnButtons.add(qlearn);
		epochs = new JTextField(Integer.toString(DEF_EPOCHS));
		progress = new JProgressBar();
		learnEpochsDone = new JLabel("0",JLabel.LEFT);
		 
		controlspane.add(actionButtons);
		controlspane.add(learnButtons);
		controlspane.add(epochs);
		controlspane.add(progress);
		controlspane.add(learnEpochsDone);

		parampane.add(labelpane, BorderLayout.CENTER);
		parampane.add(controlspane, BorderLayout.EAST);
		
		parampane.setBorder(BorderFactory.createTitledBorder("Parameters"));
		
		
		return parampane;
	}
	
	// number of epochs, other settings?, instructions?
	Container makeSettingPanel() {
		JPanel setPane = new JPanel();
		setPane.setLayout(new GridLayout(0,1));
		setPane.add(new JLabel(SETTINGS_TEXT));
		setPane.add(new JLabel(SETTINGS_TEXT2));

		JPanel controls = new JPanel();
		//controls.setLayout(new BoxLayout(controls, BoxLayout.X_AXIS));
		
		startTraining = new JButton("Begin Training");
		startTraining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doTraining();
			}
		});
		stopTraining = new JButton("Stop");
		stopTraining.setEnabled(false);
		stopTraining.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				endTraining();
			}
		});
		JButton clearPolicy = new JButton("Undo Training");
		clearPolicy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				game.setPolicy(rlc.resetLearner());
			}
		});
		controls.add(startTraining);
		controls.add(stopTraining);
		controls.add(clearPolicy);
		
		setPane.add(controls);
		return setPane;
	}

	Container makeBoardPanel() {
		JPanel boardPane = new JPanel();
		boardPane.setLayout(new BoxLayout(boardPane, BoxLayout.Y_AXIS));
		
		bp = new boardPanel(back, BW, BH);
		
		boardPane.add(bp);
		
		// speed control
		speed = new JSlider(0,10,5);
		speed.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				double ratio = 1 - ((double)speed.getValue())/10;
				game.delay = (long)(ratio*DELAY*2);
			}
		});
		speed.setMajorTickSpacing(2);
        speed.setPaintTicks(true);

        //Create the label table.
        Hashtable labelTable = new Hashtable();
        labelTable.put(new Integer( 0 ), new JLabel("Slow") );
        labelTable.put(new Integer( 30 ), new JLabel("Fast") );
        speed.setLabelTable(labelTable);
        speed.setPaintLabels(true);
		boardPane.add(speed);

        //boardPane.setBorder(BorderFactory.createTitledBorder("Game"));

		return boardPane;
	}
	
	Container makeButtonPane() {
		JPanel buttpane = new JPanel();
		buttpane.setLayout(new BoxLayout(buttpane, BoxLayout.Y_AXIS));
		
		// graph of scores
		buttpane.add(chartPane());
		
		// scores
		buttpane.add(scorePanel());
		
		// buttons
		buttpane.add(playControlPanel());
		
		return buttpane;
	}

	Container playControlPanel() {
		JPanel playPanel = new JPanel();
		playPanel.setLayout(new GridLayout(0,2));
		
		startbutt = new JButton("Start");
		startbutt.setActionCommand(START);
		startbutt.addActionListener(this);
		
		JCheckBox continuous = new JCheckBox("Continuous", true);
		continuous.setActionCommand(CONT_CHECK);
		continuous.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.DESELECTED)
					game.single=true;
				else game.single = false;
			}
		});

		// add controls to select greedy or rl mouse
		//JPanel learnButtons = new JPanel();
		//learnButtons.setLayout(new GridLayout(1,0));
		JRadioButton greedy = new JRadioButton("Greedy Mouse");
		JRadioButton smart = new JRadioButton("Smart Mouse",true);
		greedy.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// set game to use greedy mouse
				game.mousetype = game.GREEDY;
			}
		});
		smart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// set game to use smart mouse
				game.mousetype = game.SMART;
			}
		});
		ButtonGroup mouseButts = new ButtonGroup();
		mouseButts.add(greedy);
		mouseButts.add(smart);

		// add to grid (l-r t-b)
		playPanel.add(startbutt);
		playPanel.add(smart);
		playPanel.add(continuous);
		playPanel.add(greedy);

		playPanel.setBorder(BorderFactory.createTitledBorder("Game Controls"));
		return playPanel;
	}
	
	Container scorePanel() {
		JPanel scorePane = new JPanel();
		//scorePane.setLayout(new BoxLayout(scorePane, BoxLayout.Y_AXIS));
		scorePane.setLayout(new GridLayout(0,2));
		
		// score labels
		ImageIcon cat = new ImageIcon(catImg);
		ImageIcon mouse = new ImageIcon(mouseImg);
		mousescorelabel = new JLabel(MS_TEXT, mouse, JLabel.RIGHT);
		catscorelabel = new JLabel(CS_TEXT, cat, JLabel.RIGHT);

		// reset scores
		//JPanel hbox = new JPanel();
		//hbox.setLayout(new BoxLayout(hbox, BoxLayout.X_AXIS));
		JButton reset = new JButton("Reset Scores");
		reset.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				mousescore = 0;
				catscore = 0;
				updateBoard();
			}			
		});
		winPerc = new JLabel("", JLabel.RIGHT); // winning percentage label
		//hbox.add(reset);
		//hbox.add(winPerc);
		
		//scorePane.add(hbox);

		scorePane.add(mousescorelabel);
		scorePane.add(winPerc);
		scorePane.add(catscorelabel);
		scorePane.add(reset);
		
		scorePane.setBorder(BorderFactory.createTitledBorder("Scores"));
		return scorePane;
	}
	
	Container chartPane() {
		JPanel ch=new JPanel();
		ch.setLayout(new BorderLayout());
		
		graphPanel = new chartPanel(this);
		graphPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		
		// smoothing control
		smoothSlider = new JSlider(JSlider.HORIZONTAL, 0,99,50);
		smoothSlider.addChangeListener(new ChangeListener(){
			public void stateChanged(ChangeEvent e) {
				double ratio = ((double)smoothSlider.getValue())/100;
				graphPanel.setSmoothing(ratio);
				graphPanel.repaint();
			}
		});
		smoothSlider.setMajorTickSpacing(20);
        smoothSlider.setPaintTicks(true);

        //Create the label table.
        Hashtable labelTable = new Hashtable();
        labelTable.put(new Integer( 0 ), new JLabel("Coarse") );
        labelTable.put(new Integer( 99 ), new JLabel("Smooth") );
        smoothSlider.setLabelTable(labelTable);
        smoothSlider.setPaintLabels(true);

		ch.add(graphPanel, BorderLayout.CENTER);
		ch.add(smoothSlider, BorderLayout.SOUTH);
		
		//ch.add(scorePanel(), BorderLayout.SOUTH);
		
		ch.setBorder(BorderFactory.createTitledBorder("Performance"));
		
		return ch;
	}
	
	/********** Methods to construct panels *************/

	/********** Action handling methods ****************/
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals(START)) {
			game.gameOn = true;
		} else if (e.getActionCommand().equals("Draw")) {
			System.out.println("draw test");
			updateBoard();
		}
	}
	/********** Action handling methods ****************/
}

class chartPanel extends JPanel {
	Vector history;
	SwingApplet a;
	final int POINTW=1, POINTH=1, PREFX = 200, PREFY = 100;
	double smoothing = 0.5;
	
	final Color bg=Color.white, fg=Color.blue;
	int MAXSIZE;
	int lastm=0, lastc=0;
	
	
	public chartPanel(SwingApplet a) {
		this.a = a;
		history = new Vector();
	}
	
	public void updateScores() {
		int m = a.mousescore, c = a.catscore;
		int dm = m-lastm, dc = c-lastc;
		lastm=m; lastc=c;
		double score;
		if ((m+c)==0) score = 0;
		else score = ((double)dm) / (dm+dc);
		addScore(score);	
	}
	
	public void paintComponent(Graphics g) {
		MAXSIZE=getWidth()*2;
		
		// draw panel
		g.setColor(bg);
		g.fillRect(0,0,getWidth(),getHeight());
		g.setColor(fg);
		
		double previous=0, thisval, newval;
		for (int x=0; x<history.size(); x++) {
			// draw this point
			
			// smooth with previous values
			thisval = 1 - ((Double)history.elementAt(x)).doubleValue();
			//if (x != startpoint)
			newval = smoothing * previous + (1 - smoothing) * thisval;
			if ((newval >= 0) && (newval <= 1)) previous = newval;
			else System.err.println("Invalid new value: "+newval);
			int yval = (int) (newval * getHeight());
			int xval = x-(history.size() - getWidth());
			//System.out.println("index="+x+" thisval="+thisval+"newval="+newval+" xval="+xval+" yval="+yval+" previous="+previous);
			g.drawOval(xval,yval,POINTW,POINTH);
		}
		
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(PREFX, PREFY);
	}
	
	public void setSmoothing(double s) { smoothing = s; }
	
	void addScore(double s) {
		if (!((s >= 0) && (s<=1))) {
			System.err.println("Graph: rejecting value"+s);
			return;
		}
		
		history.addElement(new Double(s));

		// prune if list too big
		if (history.size() >= MAXSIZE) history.remove(0);

		//System.out.println("Size:"+history.size()+" maxsize:"+MAXSIZE);

		/*
			System.out.println("History being pruned."+Thread.currentThread().getName());
			Vector nVec = new Vector();
			for (int i=(MAXSIZE/3); i<history.size(); i++) {
				nVec.addElement(history.elementAt(i));
			}
			history = nVec;
			System.out.println("Pruning Finished.");
		}*/
	}
}

