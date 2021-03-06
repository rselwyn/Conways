import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;


public class Display extends JComponent implements MouseListener, MouseMotionListener {
	
	/**
	 * Constants.
	 */
	public static final int ROWS = 80; //number of cell rows
	public static final int COLS = 100; //number of cell cols
	public static Cell[][] cell = new Cell[ROWS][COLS]; //2 dimensional array of cells with previous constants as sizr
	private final int X_GRID_OFFSET = 25; //the offset from the left
	private final int Y_GRID_OFFSET = 40; //the offset from the top
	private final int CELL_WIDTH = 6; //the cell width
	private final int CELL_HEIGHT = 6; //the cell height
	private final int BUTTON_ROW_DEPTH = 600; //how far down the button is
	private final int DISPLAY_WIDTH;   //the width of the entire display screen
	private final int DISPLAY_HEIGHT; //the height of the entire display screen
	private final int BUTTON_WIDTH = 100; //the button width
	private final int BUTTON_HEIGHT = 36; //the button height
	/**
	 * End Constants. 
	 */
	
	/**
	 * All the buttons.
	 */
	private StartButton startStop; //the start/stop button
	private WrapButton wrapButton; //the wrap button
	private Killer killButton; //the clear button
	private UpdatingLabel pop; //the population label
	private UpdatingLabel difference; //the difference label
	private ColorPicker cp; //the color picker
	private PauseButton pause; //the pause button
	private CloseButton closer; //the close button
	private StepButton step; //the stop button
	/**
	 * End Buttons.
	 */

	/**
	 * Variables
	 */
	private boolean paintloop = false; //the paint loop status
	public static boolean wrap = false; //whether or not to wrap
	public ArrayList<Integer> population = new ArrayList<Integer>(); //the arraylist containing the population.  Every update cycle, the current population is added
	/**
	 * End Variables.
	 */
	
	/**
	 * Constructor for the display.
	 * @param width: the display width
	 * @param height: the display height
	 */
	public Display(int width, int height) {
		DISPLAY_WIDTH = width;
		DISPLAY_HEIGHT = height;
		init();
	}

	/**
	 * Initialize the cells and buttons.
	 * 
	 * For each Button/Label:
	 * 	- Initialize the instance variable
	 *  - set the bounds
	 *  - add it to the frame
	 *  - make it visible
	 */
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
		startStop.setBounds(100, BUTTON_ROW_DEPTH, BUTTON_WIDTH, BUTTON_HEIGHT);	
		add(startStop);
		startStop.setVisible(true);
		//wrap button
		wrapButton = new WrapButton();
		wrapButton.setBounds(225, BUTTON_ROW_DEPTH, BUTTON_WIDTH, BUTTON_HEIGHT);
		add(wrapButton);
		wrapButton.setVisible(true);
		//kill button
		killButton = new Killer();
		killButton.setBounds(350, BUTTON_ROW_DEPTH, BUTTON_WIDTH, BUTTON_HEIGHT);
		add(killButton);
		killButton.setVisible(true);
		//population label
		pop = new Population("Population");
		pop.setBounds(475, BUTTON_ROW_DEPTH, BUTTON_WIDTH, BUTTON_HEIGHT);
		this.population.add(0); //we have to add the first index in
		this.population.add(0); //we have to add the second index in so to not throw a diff error
		add(pop);
		//Difference label
		difference = new DifferenceBetweenGeneration("Difference");
		difference.setBounds(600, BUTTON_ROW_DEPTH, BUTTON_WIDTH, BUTTON_HEIGHT);
		add(difference);
		//color picker
		cp = new ColorPicker();
		cp.setBounds(100, BUTTON_ROW_DEPTH+40, BUTTON_WIDTH, BUTTON_HEIGHT);
		add(cp);
		//pause button
		pause = new PauseButton();
		pause.setBounds(225, BUTTON_ROW_DEPTH+40, BUTTON_WIDTH, BUTTON_HEIGHT);
		add(pause);
		//close button
		closer = new CloseButton();
		closer.setBounds(350, BUTTON_ROW_DEPTH+40, BUTTON_WIDTH, BUTTON_HEIGHT);
		add(closer);
		//step to the next generation
		step = new StepButton();
		step.setBounds(475, BUTTON_ROW_DEPTH+40, BUTTON_WIDTH, BUTTON_HEIGHT);
		add(step);
		//repaint at the end
		repaint();
	}

	
	/**
	 * Main paint loop
	 */
	public void paintComponent(Graphics g) {
		final int TIME_BETWEEN_REPLOTS = 100; // change to your liking
		//set the color
		g.setColor(Color.BLACK);
		//draw the grid
		drawGrid(g);
		//draw the cells
		drawCells(g);
		//draw the buttons
		drawButtons();
		
		//update
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

	/**
	 * Initialize the cells at the start.
	 */
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

	/**
	 * Toggles the paint loop
	 */
	public void togglePaintLoop() {
		paintloop = !paintloop;
	}


	public void setPaintLoop(boolean value) {
		paintloop = value;
	}

	/**
	 * Draw the grid (lines).
	 * @param g: the graphics object
	 */
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

	/**
	 * Calls the update or repaint method in the buttons.
	 */
	private void drawButtons() {
		startStop.repaint();
		wrapButton.repaint();
		killButton.repaint();
		pop.update(this.population.get(this.population.size() - 1)); //update the population with the arraylist's last value
		
		/**
		 * Update the difference with the last value of the population subtracted by the second to last value
		 */
		difference.update(this.population.get(this.population.size() - 1) - this.population.get(this.population.size() - 2)); 
		cp.update();
		pause.repaint();
		closer.repaint();
		repaint();
		step.repaint();
	}

	/**
	 * Sets the cells alive depending on the calculateAliveNextTurn
	 * method.  It iterates through the cells, and turns them on or off.
	 * It also counts the population. 
	 */
	private void nextGeneration() {	
		int setpop = 0;
		calcAliveNextTurn();
		//iterate over the cells
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				//set the alive to alive next turn
				cell[row][col].setAlive(cell[row][col].getAliveNextTurn());
				if (cell[row][col].getAliveNextTurn()){
					setpop++; //increase the population
				}
			}
		}	
		addPopulationMember(setpop); //add the population value to the arraylist
	}
	
	/**
	 * Calculate whether or not the cell will be alive 
	 * next turn using the rules.
	 */
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
		try{
			cell[yCellGuess][xCellGuess].setAlive(!(cell[yCellGuess][xCellGuess].getAlive()));
		}catch(Exception e){} //swallow the error if they click outside of the grid
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

	}
	
	/**
	 * Starts and stops the paint loop.
	 */
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
	 * The wrap on/off button.  When it is clicked, 
	 * it changes the static boolean in the display class which 
	 * is accessed by the cells to change wrapping.
	 */
	private class WrapButton extends JButton implements ActionListener {

		WrapButton() {
			super("Wrap Off");
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent arg0) {
			if (this.getText().equals("Wrap Off")) {
				setText("Wrap On");
				Display.wrap = true;
			} else {
				setText("Wrap Off");
				Display.wrap = false;
			}
			repaint();
		}
	}
	
	/**
	 * Clear button.
	 * 
	 * Iterates over all of the cells to turn them off.  It also 
	 * fixes the start stop button after killing the cells.
	 * 
	 */
	private class Killer extends JButton implements ActionListener {
		
		Killer() {
			super("Clear");
			addActionListener(this);
		}
		
		public void actionPerformed(ActionEvent arg0) {
			for (Cell[] cells : cell){
				for (Cell c : cells){
					c.setAliveNextTurn(false);
					c.setAlive(false);
					repaint();
				}
			}
			repaint();
			if (startStop.getText().equals("Stop")){
				startStop.actionPerformed(arg0);
			}
			
		}
	}
	
	/**
	 * Add to the population arraylist
	 * @param pop
	 */
	public void addPopulationMember(int pop){
		this.population.add(pop);
	}
	
	/**
	 * Abstract class for an updating label.
	 */
	private abstract class UpdatingLabel extends JLabel{
		protected String title;
		
		public UpdatingLabel(String title) {
			this.title = title;
		}
		
		/**
		 * Using generics because the fields could be different. 
		 * @param param
		 */
		public abstract <E> void update(E param);
	}
	
	/**
	 * Population size label.
	 */
	private class Population extends UpdatingLabel{
		public Population(String title) {
			super(title);
			// TODO Auto-generated constructor stub
		}
		
		@Override
		public <E> void update(E param) {
			this.setText(this.title + ": " + param);
		}
	}
	
	/**
	 * The difference label.
	 */
	private class DifferenceBetweenGeneration extends UpdatingLabel{

		public DifferenceBetweenGeneration(String title) {
			super(title);
			// TODO Auto-generated constructor stub
		}
		
		/**
		 * Use generic parameter so that it can take in multiple data types.
		 */
		@Override
		public <E> void update(E param) {
			this.setText(this.title +": " + param);
		}
		
		
	}
	
	/**
	 * The color picker.
	 */
	private class ColorPicker extends JComboBox{
		@SuppressWarnings("unchecked")
		ColorPicker(){
			//add all of the colors to the color picker
			this.addItem("Green");
			this.addItem("Red");
			this.addItem("Black");
			this.addItem("Blue");
			this.addItem("Orange");
			this.addItem("Yellow");
		}
		
		/**
		 * Looks for the color and then changes the color to that color
		 */
		private void update(){
			if (this.getSelectedItem().toString().equals("Red")){
				changeColor(Color.RED);
			}
			if (this.getSelectedItem().toString().equals("Green")){
				changeColor(Color.GREEN);
			}
			if (this.getSelectedItem().toString().equals("Black")){
				changeColor(Color.BLACK);
			}
			if (this.getSelectedItem().toString().equals("Blue")){
				changeColor(Color.BLUE);
			}
			if (this.getSelectedItem().toString().equals("Orange")){
				changeColor(Color.ORANGE);
			}
			if (this.getSelectedItem().toString().equals("Yellow")){
				changeColor(Color.YELLOW);
			}
		}
		
		/**
		 * Change all of the cell colors.
		 * @param c
		 */
		private void changeColor(Color c){
			for (Cell[] ce : cell){
				for (Cell b : ce){
					b.DEFAULT_ALIVE = c;
				}
			}
		}
	}
	
	/**
	 * Pauses the game.  It also resets the start/stop button.
	 */
	private class PauseButton extends JButton implements ActionListener{
		
		PauseButton(){
			super("Pause");
			addActionListener(this);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			if (startStop.getText().equals("Stop")) {
				startStop.actionPerformed(e);
			} 
		}	
	}
	
	/**
	 * Closes the program using
	 * System.exit()
	 */
	private class CloseButton extends JButton implements ActionListener{
		
		CloseButton(){
			super("Quit");
			addActionListener(this);
		}
		
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
			repaint();
		}	
	}
	
	/**
	 * The step buttons.
	 * When you click the button, it runs the nextGeneration
	 * loop.
	 */
	private class StepButton extends JButton implements ActionListener{
		StepButton(){
			super("Step");
			addActionListener(this);
		}
		@Override
		public void actionPerformed(ActionEvent e) {
			/**
			 * Run the regular nextGeneration method
			 */
			nextGeneration();
			repaint();
		}
		
		
	}
	
}
