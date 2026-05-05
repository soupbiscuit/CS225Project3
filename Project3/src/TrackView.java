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
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * The TrackView class is responsible for displaying the race using JavaFX.
 * It creates the graphical interface, including the race track, car images,
 * stop markers, and control buttons (Start and Restart).
 * TrackView interacts with the RaceManager to retrieve and update the state
 * of the race, including car positions and race results.
 * It also handles the animation loop that continuously updates the display
 * as the race progresses, and presents the final results to the user when
 * the race is complete.
 * Quincy Williams + Jasper Carr
 */
public class TrackView extends Application {

    // Scene size constants.
    private static final int SCENE_WIDTH = 1200;
    private static final int SCENE_HEIGHT = 700;

    // Track coordinate constants. These should match the coordinates used in RaceManager.
    private static final int A_X = 200;
    private static final int A_Y = 350;
    private static final int B_X = 500;
    private static final int B_Y = 150;
    private static final int C_X = 850;
    private static final int C_Y = 150;
    private static final int D_X = 1050;
    private static final int D_Y = 350;

    // Manages the race logic, cars, winner, and results.
    private RaceManager raceManager = new RaceManager();

    // Stores the JavaFX image nodes that visually represent each car.
    private List<ImageView> carViews = new ArrayList<>();

    // Buttons for user control.
    private Button startButton = new Button("Start");
    private Button restartButton = new Button("Restart");

    // Status text tells the user what is happening.
    private Text statusText = new Text("Press Start to begin the race");

    // Track whether results were already shown for the current race.
    // This prevents multiple result popups from appearing.
    private boolean resultsShown = false;

    // Stores the previous animation frame time.
    // Resetting this prevents cars from jumping after restarting.
    private long lastTime = 0;

    @Override
    public void start(Stage game) {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #C2B280;");

        // Set up the model side of the race:
        // create cars, assign paths, and reset each car to its starting stop.
        raceManager.setUpRace();

        // Create the Start/Restart button bar.
        HBox buttonBar = new HBox(10);
        buttonBar.setLayoutX(20);
        buttonBar.setLayoutY(20);
        buttonBar.getChildren().addAll(startButton, restartButton);

        // Create status text near the top-left corner.
        statusText.setX(20);
        statusText.setY(80);
        statusText.setFont(Font.font("Arial", FontWeight.BOLD, 18));
        statusText.setFill(Color.BLACK);

        // Load car images and create one ImageView for each racing car.
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

        // Store the views in the same order as the cars managed by RaceManager.
        carViews.add(blueCar);
        carViews.add(brownCar);
        carViews.add(redCar);
        carViews.add(yellowCar);

        // Set a consistent size for each car image.
        blueCar.setFitWidth(28);
        blueCar.setFitHeight(16);

        brownCar.setFitWidth(28);
        brownCar.setFitHeight(16);

        redCar.setFitWidth(28);
        redCar.setFitHeight(16);

        yellowCar.setFitWidth(28);
        yellowCar.setFitHeight(16);

        // Temporary positions before the first update() call.
        // The first update() moves these cars to their actual model positions.
        blueCar.setLayoutX(660);
        blueCar.setLayoutY(550);
        brownCar.setLayoutX(740);
        brownCar.setLayoutY(550);
        redCar.setLayoutX(820);
        redCar.setLayoutY(550);
        yellowCar.setLayoutX(900);
        yellowCar.setLayoutY(550);

        // Create the stop markers shown on the track.
        // These match the stop coordinates used in RaceManager.
        Group markerA = makeFlag("A", A_X, A_Y);
        Group markerB = makeFlag("B", B_X, B_Y);
        Group markerC = makeFlag("C", C_X, C_Y);
        Group markerD = makeFlag("D", D_X, D_Y);

        // Draw the road/track before cars and flags so it appears in the background.
        drawGrassPerimeter(root);
        drawInfield(root);
        drawTrack(root);

        // Add everything to the root pane.
        // Track is already added by drawTrack(...), so cars and labels appear on top.
        root.getChildren().addAll(
                blueCar, brownCar, redCar, yellowCar,
                markerA, markerB, markerC, markerD,
                buttonBar,
                statusText
        );

        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        game.setScene(scene);
        game.setTitle("Car Racing Game");
        game.setResizable(false);
        game.show();

        // Immediately place the car images at their model positions
        // so they do not appear at the temporary hardcoded positions.
        update();

        // Main animation loop.
        // This repeatedly updates the race and redraws the cars.
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (lastTime == 0) {
                    lastTime = now;
                    return;
                }

                double deltaTime = (now - lastTime) / 1_000_000_000.0;
                lastTime = now;

                raceManager.updateRace(deltaTime);
                update();

                if (raceManager.isRaceFinished() && !resultsShown) {
                    resultsShown = true;
                    statusText.setText("Race finished! View the results.");
                    showResults();
                    stop();
                }
            }
        };

        // Start button behavior.
        startButton.setOnAction(e -> {
            startButton.setDisable(true);
            statusText.setText("Race running...");
            resultsShown = false;
            lastTime = 0;

            raceManager.startRace();
            timer.start();
        });

        // Restart button behavior.
        restartButton.setOnAction(e -> {
            // Stop the current animation loop.
            timer.stop();

            // setUpRace() already clears old cars/results and creates a new race.
            raceManager.setUpRace();

            // Reset animation state.
            lastTime = 0;
            resultsShown = false;

            // Allow the user to start the new race manually.
            startButton.setDisable(false);
            statusText.setText("New race ready. Press Start.");

            // Reposition cars to their new starting points.
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
        // Pole for the flag.
        Line pole = new Line(0, 0, 0, 50);
        pole.setStroke(Color.BLACK);
        pole.setStrokeWidth(3);

        // Larger triangular flag shape for better visibility.
        Polygon flagShape = new Polygon(0, 0, 40, 12, 0, 24);
        flagShape.setFill(Color.RED);
        flagShape.setStroke(Color.DARKRED);

        // Label shown on the flag.
        Text mark = new Text(letter);
        mark.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        mark.setFill(Color.WHITE);
        mark.setX(8);
        mark.setY(17);

        // Group the pole, flag, and label into one object.
        Group flag = new Group(pole, flagShape, mark);
        flag.setLayoutX(x);
        flag.setLayoutY(y);

        return flag;
    }

    /**
     * Creates a wide dark road between two points.
     */
    public Line makeRoad(double x1, double y1, double x2, double y2) {
        Line road = new Line(x1, y1, x2, y2);
        road.setStroke(Color.DIMGRAY);
        road.setStrokeWidth(60);
        road.setStrokeLineCap(StrokeLineCap.ROUND);
        return road;
    }

    /**
     * Creates a dashed center line for the road.
     */
    public Line makeCenterLine(double x1, double y1, double x2, double y2) {
        Line line = new Line(x1, y1, x2, y2);
        line.setStroke(Color.WHITE);
        line.setStrokeWidth(3);
        line.getStrokeDashArray().addAll(20.0, 20.0);
        return line;
    }

    /**
     * Draws the track roads between the race stops.
     */
    public void drawTrack(Pane root) {
        // Outer loop roads.
        root.getChildren().addAll(
                makeRoad(A_X, A_Y, B_X, B_Y),   // A to B
                makeRoad(B_X, B_Y, C_X, C_Y),   // B to C
                makeRoad(C_X, C_Y, D_X, D_Y),   // C to D
                makeRoad(D_X, D_Y, A_X, A_Y)    // D to A
        );

        // Dashed center lines.
        root.getChildren().addAll(
                makeCenterLine(A_X, A_Y, B_X, B_Y),
                makeCenterLine(B_X, B_Y, C_X, C_Y),
                makeCenterLine(C_X, C_Y, D_X, D_Y),
                makeCenterLine(D_X, D_Y, A_X, A_Y)
        );
    }

    /**
     * Draws a fully filled green infield inside the track loop.
     */
    public void drawInfield(Pane root) {
        Polygon infield = new Polygon(
                A_X, A_Y,
                B_X, B_Y,
                C_X, C_Y,
                D_X, D_Y
        );

        infield.setFill(Color.web("#7CFC8A"));
        infield.setStroke(Color.SEAGREEN);
        infield.setStrokeWidth(3);

        root.getChildren().add(infield);
    }

    /**
     * Draws a larger green perimeter band around the track (grass near the road).
     */
    public void drawGrassPerimeter(Pane root) {
        int offsetX = 180; // make sides wider
        int offsetY = 100; // vertical spacing

        Polygon grass = new Polygon(
                A_X - offsetX, A_Y + offsetY,
                B_X - offsetX, B_Y - offsetY,
                C_X + offsetX, C_Y - offsetY,
                D_X + offsetX, D_Y + offsetY
        );

        grass.setFill(Color.web("#7CFC8A"));
        grass.setStroke(null);

        root.getChildren().add(grass);
    }

    /**
     * Updates the on-screen position of every car image
     * so it matches the corresponding Car model object.
     */
    public void update() {
        List<Car> cars = raceManager.getCars();

        for (int i = 0; i < cars.size() && i < carViews.size(); i++) {
            Car car = cars.get(i);
            ImageView view = carViews.get(i);

            // Center the image on the car's actual model position.
            double centeredX = car.getX() - view.getFitWidth() / 2;
            double centeredY = car.getY() - view.getFitHeight() / 2;

            // Small lane offset keeps cars from completely overlapping.
            // The offset is kept small so cars still appear on the road.
            double laneOffset = (i - 1.5) * 6;

            view.setLayoutX(centeredX);
            view.setLayoutY(centeredY + laneOffset);

            // Rotate car to match movement direction
            double angle = car.getDirectionAngle();

            // Base rotation (car already faces right by default)
            view.setRotate(angle);

            // Flip vertically if moving left (prevents upside-down cars)
            if (angle > 90 || angle < -90) {
                view.setScaleY(-1);
            } else {
                view.setScaleY(1);
            }
        }
    }

    /**
     * Displays the race results in a popup dialog.
     * Shows each car's name, engine, tires, total time, and path,
     * then shows the winner at the bottom.
     */
    public void showResults() {
        StringBuilder message = new StringBuilder();

        for (RaceResult r : raceManager.getResults()) {
            Car car = r.getCar();

            message.append(car.getName())
                    .append(r.isWinner() ? " (WINNER)" : "")
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
                    .append(" seconds")
                    .append("\nPath: ")
                    .append(car.getPathString())
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
