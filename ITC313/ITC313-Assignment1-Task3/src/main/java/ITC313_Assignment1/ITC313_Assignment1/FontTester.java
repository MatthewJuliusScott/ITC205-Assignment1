package ITC313_Assignment1.ITC313_Assignment1;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 * The Class FontTester. Draws a 2D archery target.
 */
public class FontTester extends Application {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}
	
	/* (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
    public void start(Stage primaryStage) throws Exception {
		
		primaryStage.setTitle("Archery Target");
		
		// define the canvas size
		int width = 400;
		int height = 250;
		
		// define the largest circle radius, and the difference in each circle's radius
		int radius = 90;
		int radiusDiff = 20;
		
		// create the circles, set their colour and decrement the radius of the next circle
        final Circle whiteCircle = new Circle(width/2, height/2, radius);
        whiteCircle.setFill(Color.WHITE);
        radius -= radiusDiff;
        
        final Circle blackCircle = new Circle(width/2, height/2, radius);
        blackCircle.setFill(Color.BLACK);
        radius -= radiusDiff;
        
        final Circle cyanCircle = new Circle(width/2, height/2, radius);
        cyanCircle.setFill(Color.CYAN);
        radius -= radiusDiff;
        
        final Circle redCircle = new Circle(width/2, height/2, radius);
        redCircle.setFill(Color.RED);
        radius -= radiusDiff;
        
        final Circle goldCircle = new Circle(width/2, height/2, radius);
        goldCircle.setFill(Color.GOLD);
        
        // group them together
        final Group root = new Group(whiteCircle, blackCircle, cyanCircle, redCircle, goldCircle);
        
        // and add them to the scene, also setting background colour
        final Scene scene = new Scene(root, width, height, Color.DARKSEAGREEN);
        
        // draw the scene to the stage
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
