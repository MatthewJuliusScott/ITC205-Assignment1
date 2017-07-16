package ITC313_Assignment1.ITC313_Assignment1;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * The Class TrafficLightSimulation. Draws a Traffic Light Simulation, which the
 * user can change the colour of using a radio button select.
 */
public class TrafficLightSimulation extends Application {

	/**
	 * The main method.
	 *
	 * @param args
	 *            the arguments
	 */
	public static void main(String[] args) {
		launch(args);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Traffic Light Simulation");

		// define the canvas size
		int width = 400;
		int height = 250;

		// define the draw height the circles start at, and the space between them
		int radius = 20;
		int drawHeight = 50;
		int heightDiff = radius * 2 + 10;

		// define the border rectangle
		final Rectangle rect = new Rectangle(width / 2 - radius - 10, drawHeight - radius - 10, radius * 2 + 20, drawHeight + (heightDiff * 2) + 10);
		rect.setFill(Color.WHITE);
		rect.setStroke(Color.BLACK);
		rect.setStrokeWidth(2);

		// create the circles
		final Circle topCircle = new Circle(width / 2, drawHeight, radius);
		topCircle.setFill(Color.WHITE);
		topCircle.setStrokeWidth(2);
		topCircle.setStroke(Color.BLACK);
		drawHeight += heightDiff;

		final Circle midCircle = new Circle(width / 2, drawHeight, radius);
		midCircle.setFill(Color.WHITE);
		midCircle.setStrokeWidth(2);
		midCircle.setStroke(Color.BLACK);
		drawHeight += heightDiff;

		final Circle botCircle = new Circle(width / 2, drawHeight, radius);
		botCircle.setFill(Color.WHITE);
		botCircle.setStrokeWidth(2);
		botCircle.setStroke(Color.BLACK);
				
		// create the radio buttons
		RadioButton topButton = new RadioButton("Red");
		topButton.setUserData("Red");
        RadioButton midButton = new RadioButton("Yellow");
        midButton.setUserData("Yellow");
        RadioButton botButton = new RadioButton("Green");
        botButton.setUserData("Green");
        
        // Group them so only one can be toggled at a time
        final ToggleGroup radioGroup = new ToggleGroup();
        
        topButton.setToggleGroup(radioGroup);
        midButton.setToggleGroup(radioGroup);
        botButton.setToggleGroup(radioGroup);
        
        // define what behaviour toggling buttons has
        radioGroup.selectedToggleProperty().addListener(new ChangeListener<Toggle>(){
            public void changed(ObservableValue<? extends Toggle> ov, Toggle old_toggle, Toggle new_toggle) {

                 if (radioGroup.getSelectedToggle() != null) {
                	 String selectedColour = radioGroup.getSelectedToggle().getUserData().toString();
                	 if (selectedColour.equals("Red")) {
                		 topCircle.setFill(Color.RED);
                		 midCircle.setFill(Color.WHITE);
                		 botCircle.setFill(Color.WHITE);
                	 } else if (selectedColour.equals("Yellow")) {
                		 topCircle.setFill(Color.WHITE);
                		 midCircle.setFill(Color.YELLOW);
                		 botCircle.setFill(Color.WHITE);
                	 } else if (selectedColour.equals("Green")) {
                		 topCircle.setFill(Color.WHITE);
                		 midCircle.setFill(Color.WHITE);
                		 botCircle.setFill(Color.GREEN);
                	 }
                 }
             } 
        });
        
        // Place them in a horizontal box group
        HBox hbox = new HBox(topButton, midButton, botButton);
        hbox.setAlignment(Pos.BASELINE_CENTER);
        
        // group them together
        final GridPane grid = new GridPane();
        grid.setAlignment(Pos.BASELINE_CENTER);
        grid.getChildren().add(hbox);
     	final Group root = new Group(rect, topCircle, midCircle, botCircle, grid);

		// and add them to the scene, also setting background colour
		final Scene scene = new Scene(root, width, height, Color.WHITE);

		// draw the scene to the stage
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
