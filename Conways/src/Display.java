import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JRadioButton;

// Note that the JComponent is set up to listen for mouse clicks
// and mouse movement.  To achieve this, the MouseListener and
// MousMotionListener interfaces are implemented and there is additional
// code in init() to attach those interfaces to the JComponent.


public class Display extends JComponent implements MouseListener, MouseMotionListener {
	public static final int ROWS = 80;
	public static final int COLS = 100;
	public static Cell[][] cell = new Cell[ROWS][COLS];
	private final int X_GRID_OFFSET = 25; // 25 pixels from left
	private final int Y_GRID_OFFSET = 40; // 40 pixels from top
	private final int CELL_WIDTH = 6;
	private final int CELL_HEIGHT = 6;
	private final int BUTTON_ROW_DEPTH = 550; //how far down the button is
	
	// Note that a final field can be initialized in constructor
	private final int DISPLAY_WIDTH;   
	private final int DISPLAY_HEIGHT;
	private boolean paintloop = false;
	
	/**
	 * All the buttons.
	 */
	private StartButton startStop;
	private WrapButton wrapButton;
	private Killer killButton;
	
	public static boolean wrap = true;

	public Display(int width, int height) {
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = height;
		init();
	}


	public void init() {
		setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT);
		initCells();

		addMouseListener(this);
		addMouseMotionListener(this);
		
		/**
		 * Add the buttons.
		 */
		//Start stop button
		startStop = new StartButton();
		startStop.setBounds(100, BUTTON_ROW_DEPTH, 100, 36);	
		add(startStop);
		startStop.setVisible(true);
		//wrap button
		wrapButton = new WrapButton();
		wrapButton.setBounds(225, BUTTON_ROW_DEPTH, 100, 36);
		add(wrapButton);
		wrapButton.setVisible(true);
		//kill button
		killButton = new Killer();
		killButton.setBounds(350, BUTTON_ROW_DEPTH, 100, 36);
		add(killButton);
		killButton.setVisible(true);
		
		
		repaint();
	}

	
	

	public void paintComponent(Graphics g) {
		final int TIME_BETWEEN_REPLOTS = 200; // change to your liking

		g.setColor(Color.BLACK);
		drawGrid(g);
		drawCells(g);
		drawButtons();

		if (paintloop) {
			try {
				Thread.sleep(TIME_BETWEEN_REPLOTS);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			nextGeneration();
			repaint();
		}
	}

	// cell[80][100]
	public void initCells() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				cell[row][col] = new Cell(row, col);
			}
		}
		cell[1][10].setAlive(true);
		cell[36][22].setAlive(true); // sample use of cell mutator method
		cell[36][23].setAlive(true); // sample use of cell mutator method
		cell[36][24].setAlive(true); // sample use of cell mutator method
	}


	public void togglePaintLoop() {
		paintloop = !paintloop;
	}


	public void setPaintLoop(boolean value) {
		paintloop = value;
	}


	void drawGrid(Graphics g) {
		for (int row = 0; row <= ROWS; row++) {
			g.drawLine(X_GRID_OFFSET,
					Y_GRID_OFFSET + (row * (CELL_HEIGHT + 1)), X_GRID_OFFSET
					+ COLS * (CELL_WIDTH + 1), Y_GRID_OFFSET
					+ (row * (CELL_HEIGHT + 1)));
		}
		for (int col = 0; col <= COLS; col++) {
			g.drawLine(X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET,
					X_GRID_OFFSET + (col * (CELL_WIDTH + 1)), Y_GRID_OFFSET
					+ ROWS * (CELL_HEIGHT + 1));
		}
	}

	
	void drawCells(Graphics g) {
		// Have each cell draw itself
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				// The cell cannot know for certain the offsets nor the height
				// and width; it has been set up to know its own position, so
				// that need not be passed as an argument to the draw method
				cell[row][col].draw(X_GRID_OFFSET, Y_GRID_OFFSET, CELL_WIDTH,
						CELL_HEIGHT, g);
			}
		}
	}


	private void drawButtons() {
		startStop.repaint();
		wrapButton.repaint();
		killButton.repaint();
	}


	private void nextGeneration() {	
		calcAliveNextTurn();
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				cell[row][col].setAlive(cell[row][col].getAliveNextTurn());
			}
		}		
	}

	public void calcAliveNextTurn(){
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				cell[row][col].calcNeighbors(cell);
				if (cell[row][col].getAlive() && cell[row][col].getNeighbors() < 2) 
					cell[row][col].setAliveNextTurn(false);
				if (cell[row][col].getAlive() && (cell[row][col].getNeighbors() == 2 || cell[row][col].getNeighbors() == 3))
					cell[row][col].setAliveNextTurn(true);
				if (cell[row][col].getAlive() && cell[row][col].getNeighbors() > 3) 
					cell[row][col].setAliveNextTurn(false);
				if (!cell[row][col].getAlive() && cell[row][col].getNeighbors() == 3) 
					cell[row][col].setAliveNextTurn(true);
			}
		}
	}
	
	/**
	 * Mouse clicked method.
	 */
	public void mouseClicked(MouseEvent arg0) {
		int xCellGuess = (int) (arg0.getX() - X_GRID_OFFSET)/(CELL_WIDTH + 1);
		int yCellGuess = (int) (arg0.getY() - Y_GRID_OFFSET)/(CELL_HEIGHT + 1 );
		cell[yCellGuess][xCellGuess].setAlive(!(cell[yCellGuess][xCellGuess].getAlive()));
		repaint();
	}


	public void mouseEntered(MouseEvent arg0) {

	}


	public void mouseExited(MouseEvent arg0) {

	}


	public void mousePressed(MouseEvent arg0) {

	}


	public void mouseReleased(MouseEvent arg0) {

	}

	/**
	 * Mouse dragged method.
	 */
	public void mouseDragged(MouseEvent arg0) {
		int xCellGuess = (int) (arg0.getX() - X_GRID_OFFSET)/(CELL_WIDTH + 1);
		int yCellGuess = (int) (arg0.getY() - Y_GRID_OFFSET)/(CELL_HEIGHT + 1 );
		try{
			cell[yCellGuess][xCellGuess].setAlive(!(cell[yCellGuess][xCellGuess].getAlive()));
		}
		catch(ArrayIndexOutOfBoundsException e){
			//Click outside
		}
		repaint();
	}


	public void mouseMoved(MouseEvent arg0) {
		return;
	}
	

	private class StartButton extends JButton implements ActionListener {
		StartButton() {
			super("Start");
			addActionListener(this);
		}

		public void actionPerformed(ActionEvent arg0) {
			// nextGeneration(); // test the start button
			if (this.getText().equals("Start")) {
				togglePaintLoop();
				setText("Stop");
			} else {
				togglePaintLoop();
				setText("Start");
			}
			repaint();
		}
	}
	
	/**
	 * The wrap on/off button
	 */
	private class WrapButton extends JButton implements ActionListener {
		WrapButton() {
			super("Wrap Off");
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent arg0) {
			if (this.getText().equals("Wrap Off")) {
				setText("Wrap On");
			} else {
				setText("Wrap Off");
			}
			repaint();
		}
	}
	
	private class Killer extends JButton implements ActionListener {
		Killer() {
			super("Kill");
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent arg0) {
			for (Cell[] cells : cell){
				for (Cell c : cells){
					c.setAliveNextTurn(false);
					c.setAlive(false);
				}
			}
			repaint();
		}
		
	}
	

}
