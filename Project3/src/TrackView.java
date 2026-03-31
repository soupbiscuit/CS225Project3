import javafx.application.Application;
import javafx.animation.AnimationTimer;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * The TrackView class is responsible for displaying the race using JavaFX.
 *
 * It creates the graphical interface, including the race track, car images,
 * stop markers, and control buttons (Start and Restart).
 *
 * TrackView interacts with the RaceManager to retrieve and update the state
 * of the race, including car positions and race results.
 *
 * It also handles the animation loop that continuously updates the display
 * as the race progresses, and presents the final results to the user when
 * the race is complete.
 *
 * Quincy Williams + Jasper Carr
 */

public class TrackView extends Application {

    // Manages the race logic, cars, winner, and results
    private RaceManager raceManager = new RaceManager();

    // Stores the JavaFX image nodes that visually represent each car
    private List<ImageView> carViews = new ArrayList<>();

    // Buttons for user control
    private Button startButton = new Button("Start");
    private Button restartButton = new Button("Restart");

    // Track whether results were already shown for the current race
    // This prevents multiple popups from appearing
    private boolean resultsShown = false;

    @Override
    public void start(Stage game) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #C2B280;");

        // Set up the model side of the race:
        // create cars, assign paths, and reset each car to its starting stop
        raceManager.setUpRace();

        // Create the Start/Restart button bar
        HBox buttonBar = new HBox(10);
        buttonBar.setLayoutX(20);
        buttonBar.setLayoutY(20);
        buttonBar.getChildren().addAll(startButton, restartButton);

        // Load car images and create one ImageView for each racing car
        ImageView blueCar = new ImageView(
                new Image(getClass().getResourceAsStream("/Resources/bluecar-2.png"))
        );
        ImageView brownCar = new ImageView(
                new Image(getClass().getResourceAsStream("/Resources/brown-1.png"))
        );
        ImageView redCar = new ImageView(
                new Image(getClass().getResourceAsStream("/Resources/decapred-1.png"))
        );
        ImageView yellowCar = new ImageView(
                new Image(getClass().getResourceAsStream("/Resources/yellow1.png"))
        );

        // Store the views in the same order as the cars managed by RaceManager
        carViews.add(blueCar);
        carViews.add(brownCar);
        carViews.add(redCar);
        carViews.add(yellowCar);

        // Set a consistent size for each car image
        blueCar.setFitWidth(20);
        blueCar.setFitHeight(10);

        brownCar.setFitWidth(20);
        brownCar.setFitHeight(10);

        redCar.setFitWidth(20);
        redCar.setFitHeight(10);

        yellowCar.setFitWidth(20);
        yellowCar.setFitHeight(10);

        // Temporary positions before the first update() call
        // The first update() will move these cars to their actual model positions
        blueCar.setLayoutX(660);
        blueCar.setLayoutY(550);

        brownCar.setLayoutX(740);
        brownCar.setLayoutY(550);

        redCar.setLayoutX(820);
        redCar.setLayoutY(550);

        yellowCar.setLayoutX(900);
        yellowCar.setLayoutY(550);

        // Create the stop markers shown on the track
        // These match the stop coordinates used in RaceManager
        Group markerA = makeFlag("A", 200, 400);
        Group markerB = makeFlag("B", 700, 150);
        Group markerC = makeFlag("C", 1300, 150);
        Group markerD = makeFlag("D", 1800, 400);

        // Add everything to the root pane
        root.getChildren().addAll(
                blueCar, brownCar, redCar, yellowCar,
                markerA, markerB, markerC, markerD,
                buttonBar
        );

        Scene scene = new Scene(root, 2000, 800);
        game.setScene(scene);
        game.setTitle("Track View");
        game.show();

        // Immediately place the car images at their model positions
        // so they do not appear at the temporary hardcoded positions
        update();

        // Main animation loop
        // This repeatedly updates the race and redraws the cars
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

            @Override
            public void handle(long now) {
                // Skip timing calculation on the first frame
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                // Convert nanoseconds to seconds
                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                // Update the model, then update the visuals
                raceManager.updateRace(deltaTime);
                update();

                // When race ends, show results once and stop the timer
                if (raceManager.isRaceFinished() && !resultsShown) {
                    resultsShown = true;
                    showResults();
                    stop();
                }
            }
        };

        // Start button behavior
        startButton.setOnAction(e -> {
            // Disable Start so the user does not start the same race multiple times
            startButton.setDisable(true);

            // Begin the race logic and animation loop
            raceManager.startRace();
            timer.start();
        });

        // Restart button behavior
        restartButton.setOnAction(e -> {
            // Stop current animation loop
            timer.stop();

            // Reset and rebuild the race
            raceManager.resetRace();
            raceManager.setUpRace();

            // Allow the user to start the new race manually
            startButton.setDisable(false);

            // Allow results popup for the next race
            resultsShown = false;

            // Reposition cars to their new starting points
            update();
        });
    }

    /**
     * Creates a flag marker showing a stop label.
     *
     * @param letter the stop label, such as A, B, C, or D
     * @param x x-position of the flag on the screen
     * @param y y-position of the flag on the screen
     * @return a grouped JavaFX node representing the flag
     */
    public Group makeFlag(String letter, double x, double y) {
        // Pole for the flag
        Line pole = new Line(0, 0, 0, 40);
        pole.setStroke(Color.BLACK);
        pole.setStrokeWidth(2);

        // Triangular flag shape
        Polygon flagShape = new Polygon(0, 0, 25, 8, 0, 16);
        flagShape.setFill(Color.RED);
        flagShape.setStroke(Color.DARKRED);

        // Label shown on the flag
        Text mark = new Text(letter);
        mark.setFont(Font.font("Arial", FontWeight.BOLD, 8));
        mark.setFill(Color.WHITE);
        mark.setX(4);
        mark.setY(11);

        // Group the pole, flag, and label into one object
        Group flag = new Group(pole, flagShape, mark);
        flag.setLayoutX(x);
        flag.setLayoutY(y);

        return flag;
    }

    /**
     * Updates the on-screen position of every car image
     * so it matches the corresponding Car model object.
     */
    public void update() {
        List<Car> cars = raceManager.getCars();

        // Move each car image to the position stored in the model
        for (int i = 0; i < cars.size() && i < carViews.size(); i++) {
            Car car = cars.get(i);
            ImageView view = carViews.get(i);

            view.setLayoutX(car.getX());

            // Add a small vertical offset so cars are easier to see
            // when they are close together on the track
            view.setLayoutY(car.getY() + (i * 18));
        }
    }

    /**
     * Displays the race results in a popup dialog.
     * Shows each car's name, engine, tires, and total time,
     * then shows the winner at the bottom.
     */
    public void showResults() {
        StringBuilder message = new StringBuilder();

        for (RaceResult r : raceManager.getResults()) {
            Car car = r.getCar();

            message.append(car.getName())
                    .append("\nEngine: ")
                    .append(car.getEngine().getName())
                    .append(" (Top Speed: ")
                    .append(String.format("%.2f", car.getEngine().getTopSpeed()))
                    .append(")")
                    .append("\nTires: ")
                    .append(car.getTires().getName())
                    .append(" (Grip: ")
                    .append(String.format("%.2f", car.getTires().getGrip()))
                    .append(")")
                    .append("\nTime: ")
                    .append(String.format("%.2f", r.getTotalTime()))
                    .append("\n\n");
        }

        if (raceManager.getWinner() != null) {
            message.append("Winner: ")
                    .append(raceManager.getWinner().getName());
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Race Results");
        alert.setHeaderText("Race Finished");
        alert.setContentText(message.toString());
        alert.show();
    }
}