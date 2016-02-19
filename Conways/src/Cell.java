import java.awt.Color;
import java.awt.Graphics;

public class Cell {
	private int myX, myY; // x,y position on grid
	private boolean myAlive; // alive (true) or dead (false)
	private int myNeighbors; // count of neighbors with respect to x,y
	private boolean myAliveNextTurn; // Used for state in next iteration
	private Color myColor; // Based on alive/dead rules
	private final Color DEFAULT_ALIVE = Color.GREEN;
	private final Color DEFAULT_DEAD = Color.GRAY;

	
	public Cell(int x, int y) {
		this(x, y, false, Color.GRAY);
	}

	public Cell(int row, int col, boolean alive, Color color) {
		myAlive = alive;
		myColor = color;
		myX = col;
		myY = row;
	}

	public boolean getAlive() {
		return myAlive;
	}

	public int getX() {
		return myX;
	}

	public int getY() {
		return myY;
	}

	public Color getColor() {
		return myColor;
	}

	public void setAlive(boolean alive) {
		if (alive) {
			setAlive(true, DEFAULT_ALIVE);
		} else {
			setAlive(false, DEFAULT_DEAD);
		}
	}

	public void setAlive(boolean alive, Color color) {
		myColor = color;
		myAlive = alive;
	}

	public void setAliveNextTurn(boolean alive) {
		myAliveNextTurn = alive;
	}

	public boolean getAliveNextTurn() {
		return myAliveNextTurn;
	}

	public void setColor(Color color) {
		myColor = color;
	}

	public int getNeighbors() {
		return myNeighbors;
	}

	/**
	 * Calculate the neighbors
	 * @param cell: the array of Cell objects
	 */
	public void calcNeighbors(Cell[][] cell) {	
		myNeighbors = 0; 
		for (int xvalue = this.getX()-1; xvalue <= this.getX()+1; xvalue++){
			for (int yvalue = this.getY()-1; yvalue <= this.getY()+1; yvalue++){
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
	 * TODO
	 * @param cells
	 * @param x
	 * @param y
	 */
	public void wrapNeighbor(Cell[][] cells, int x, int y){
		
	}
	
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