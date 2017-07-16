package ITC313_Assignment1.ITC313_Assignment1;

import java.util.ArrayList;

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
 * The Class ArcheryTarget. Displays an archery target in a small window.
 */
public class ArcheryTarget extends Application {

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

		primaryStage.setTitle("Archery Target");

		// define the canvas size
		int width = 400;
		int height = 250;

		final Group root = new Group();
		
		// define the circles
		final int startRadius = 90;
		final int radiusDiff = 20;
		final int numOfCircles = 5;

		// create them, set their position, radius and colour
		int radius = startRadius;
		ArrayList<Circle> circles = new ArrayList<Circle>();
		for (int i = 1; i <= numOfCircles; i++) {
			Circle circle = new Circle(width / 2, height / 2, radius);
			Color color = Color.WHITE;
			switch (i) {
			case 1:
				color = Color.WHITE;
				break;
			case 2:
				color = Color.BLACK;
				break;
			case 3:
				color = Color.CYAN;
				break;
			case 4:
				color = Color.RED;
				break;
			case 5:
				color = Color.GOLD;
				break;
			}
			circle.setFill(color);
			circles.add(circle);
			radius -= radiusDiff;
		}
		root.getChildren().addAll(circles);

		// and add them to the scene, also setting background colour
		final Scene scene = new Scene(root, width, height, Color.DARKSEAGREEN);

		// draw the scene to the stage
		primaryStage.setScene(scene);
		primaryStage.show();
	}
}
