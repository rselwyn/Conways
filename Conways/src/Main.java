import javax.swing.JFrame;

public class Main {

	public static void main(String[] args) {
		final int DISPLAY_WIDTH = 800; //the display width
		final int DISPLAY_HEIGHT = 700; //the display height
		JFrame f = new JFrame(); //create a new JFrame
		f.setSize(DISPLAY_WIDTH, DISPLAY_HEIGHT); //set the size to the DISPAY constants
		Display display = new Display(DISPLAY_WIDTH, DISPLAY_HEIGHT); //instantiate a new Display
		f.setLayout(null); //set the layout to null
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //set the default close operation
		f.setTitle("Conway's Game of Life"); //set the title
		f.add(display); //add the display to the frame
		f.setVisible(true); // set the frame to visible
		
	}
}