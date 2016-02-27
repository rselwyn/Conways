import java.awt.Color;
import java.awt.Graphics;

public class Cell {
	private int myX, myY; // x,y position on grid
	private boolean myAlive; // alive (true) or dead (false)
	private int myNeighbors; // count of neighbors with respect to x,y
	private boolean myAliveNextTurn; // Used for state in next iteration
	private Color myColor; // Based on alive/dead rules
	public Color DEFAULT_ALIVE = Color.GREEN;
	public Color DEFAULT_DEAD = Color.GRAY;

	
	public Cell(int x, int y) {
		this(x, y, false, Color.GRAY);
	}

	public Cell(int row, int col, boolean alive, Color color) {
		myAlive = alive;
		myColor = color;
		myX = col;
		myY = row;
	}

	/**
	 * Gets if this cell is alive.
	 * @return the alive state
	 */
	public boolean getAlive() {
		return myAlive;
	}
	
	/**
	 * Get the x position.
	 * @return the x position.
	 */
	public int getX() {
		return myX;
	}
	
	/**
	 * Get the y position.
	 * @return the y position
	 */
	public int getY() {
		return myY;
	}

	/**
	 * Returns the current color
	 * @return the color
	 */
	public Color getColor() {
		return myColor;
	}
	
	/**
	 * Sets alive to the parameter
	 * @param alive
	 */
	public void setAlive(boolean alive) {
		if (alive) {
			setAlive(true, DEFAULT_ALIVE);
		} else {
			setAlive(false, DEFAULT_DEAD);
		}
	}
	
	/**
	 * Overloaded method
	 * Sets alive and color
	 * @param alive: alive or not
	 * @param color: the color
	 */
	public void setAlive(boolean alive, Color color) {
		myColor = color;
		myAlive = alive;
	}

	/**
	 * Sets the alive next turn.
	 * @param alive: the boolean state to set
	 */
	public void setAliveNextTurn(boolean alive) {
		myAliveNextTurn = alive;
	}

	/**
	 * Gets the alive next turn
	 * @return the boolean on whether or not this cell should or shouldn't be
	 * alive next turn.
	 */
	public boolean getAliveNextTurn() {
		return myAliveNextTurn;
	}
	
	/**
	 * Set the color.
	 * @param color
	 */
	public void setColor(Color color) {
		myColor = color;
	}

	/**
	 * Get the neighbor instance variable
	 * @return the neighbors
	 */
	public int getNeighbors() {
		return myNeighbors;
	}

	/**
	 * Calculate the neighbors of a cell.  This works
	 * by iterating over the cells next to it.  It also functions
	 * without using a try/catch statement by catching out of bounds cells (that can be wrapped).
	 *   
	 * @param cell: the array of Cell objects
	 */
	public void calcNeighbors(Cell[][] cell) {	
		myNeighbors = 0; 
		for (int xvalue = this.getX()-1; xvalue <= this.getX()+1; xvalue++){
			for (int yvalue = this.getY()-1; yvalue <= this.getY()+1; yvalue++){
				
				//if out of bounds
				if (xvalue >= Display.COLS || yvalue >= Display.ROWS || xvalue < 0 || yvalue < 0){
					if (Display.wrap) wrapNeighbor(cell,xvalue,yvalue);	 
				}
				else{
					if (cell[yvalue][xvalue].getAlive()) myNeighbors++;
				}
			}
		}
		//there will be an extra neighbor if we are alive
		if (this.getAlive()) myNeighbors--;
	}
	
	/**
	 * If the calcneighbors method sees that the neighbor is off the grid (and wrapping is on),
	 * this method will be called.  
	 * @param cells: the cell array
	 * @param x: the x position of the cell neighbor (not wrapped)
	 * @param y: the y position of the cell neighbor (not wrapped)
	 */
	public void wrapNeighbor(Cell[][] cells, int x, int y){
		if (x == Display.COLS){
			x=0;
		}
		if (y == Display.ROWS){
			y=0;
		}
		if (x==-1){
			x = Display.COLS-1;
		}
		if (y==-1){
			y = Display.ROWS-1;
		}
		if (cells[y][x].getAlive()) myNeighbors++;
	}
	
	/**
	 * Draw a cell on the grid.
	 * 
	 * The Offsets:
	 * @param x_offset
	 * @param y_offset
	 * @param width
	 * @param height
	 * @param g
	 */
	public void draw(int x_offset, int y_offset, int width, int height, Graphics g) {
		// I leave this understanding to the reader
		int xleft = x_offset + 1 + (myX * (width + 1));
		int xright = x_offset + width + (myX * (width + 1));
		int ytop = y_offset + 1 + (myY * (height + 1));
		int ybottom = y_offset + height + (myY * (height + 1));
		Color temp = g.getColor();

		g.setColor(myColor);
		g.fillRect(xleft, ytop, width, height);
	}
}