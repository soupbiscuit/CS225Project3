import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Alert;
import java.util.ArrayList;
import java.util.List;

public class TrackView extends Application {
    private RaceManager raceManager = new RaceManager();
    private List<ImageView> carViews = new ArrayList<>();
    @Override
    public void start(Stage game)  {
        Pane root = new Pane();
        root.setStyle("-fx-background-color: #C2B280;");

        raceManager.setUpRace();
        // Load car images and create ImageViews for each car
        ImageView blueCar = new ImageView(new Image(getClass().getResourceAsStream("/Resources/bluecar-2.png")));
        ImageView brownCar = new ImageView(new Image(getClass().getResourceAsStream("/Resources/brown-1.png")));
        ImageView redCar = new ImageView(new Image(getClass().getResourceAsStream("/Resources/decapred-1.png")));
        ImageView yellowCar = new ImageView(new Image(getClass().getResourceAsStream("/Resources/yellow1.png")));
        carViews.add(blueCar);
        carViews.add(brownCar);
        carViews.add(redCar);
        carViews.add(yellowCar);

        blueCar.setFitWidth(20);
        blueCar.setFitHeight(10);
        brownCar.setFitWidth(20);
        brownCar.setFitHeight(10);
        redCar.setFitWidth(20);
        redCar.setFitHeight(10);
        yellowCar.setFitWidth(20);
        yellowCar.setFitHeight(10);

        blueCar.setLayoutX(660);
        blueCar.setLayoutY(550);

        brownCar.setLayoutX(740);
        brownCar.setLayoutY(550);

        redCar.setLayoutX(820);
        redCar.setLayoutY(550);

        yellowCar.setLayoutX(900);
        yellowCar.setLayoutY(550);

        
        // Create markers for the stops
        Group markerA = makeFlag("A", 200, 400);
        Group markerB = makeFlag("B", 700, 150);
        Group markerC = makeFlag("C", 1300, 150);
        Group markerD = makeFlag("D", 1800, 400);

        root.getChildren().addAll(blueCar, brownCar, redCar, yellowCar, markerA, markerB, markerC, markerD);

        Scene scene = new Scene(root, 2000, 800);
        game.setScene(scene);
        game.setTitle("Track View");
        game.show();
        AnimationTimer timer = new AnimationTimer() {
            private long lastTime = 0;

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

                if (raceManager.isRaceFinished()) {
                    showResults();
                    stop();
                }
            }
        };

        timer.start();
    }

    public Group makeFlag(String letter, double x, double y) {
    // Pole
    Line pole = new Line(0, 0, 0, 40);
    pole.setStroke(Color.BLACK);
    pole.setStrokeWidth(2);

    // Flag shape
    Polygon flagShape = new Polygon(0,0, 25,8, 0,16);
    flagShape.setFill(Color.RED);
    flagShape.setStroke(Color.DARKRED);

    // Letter on flag
    Text mark = new Text(letter);
    mark.setFont(Font.font("Arial", FontWeight.BOLD, 8));
    mark.setFill(Color.WHITE);
    mark.setX(4);
    mark.setY(11);

    // Group everything together
    Group flag = new Group(pole, flagShape, mark);
    flag.setLayoutX(x);
    flag.setLayoutY(y);

    return flag;
}

    /**
     * Updates all car images so they match the model positions.
     */
    public void update() {
        List<Car> cars = raceManager.getCars();

        for (int i = 0; i < cars.size(); i++) {
            Car car = cars.get(i);
            ImageView view = carViews.get(i);

            view.setLayoutX(car.getX());
            view.setLayoutY(car.getY());
        }
    }

    /**
     * Shows race results in a popup dialog.
     */
    public void showResults() {
        StringBuilder message = new StringBuilder();

        for (RaceResult r : raceManager.getResults()) {
            Car car = r.getCar();
            message.append(car.getName())
                    .append("\nEngine: ").append(car.getEngine().getName())
                    .append(" (Top Speed: ").append(String.format("%.2f", car.getEngine().getTopSpeed())).append(")")
                    .append("\nTires: ").append(car.getTires().getName())
                    .append(" (Grip: ").append(String.format("%.2f", car.getTires().getGrip())).append(")")
                    .append(" time: ")
                    .append(String.format("%.2f", r.getTotalTime()))
                    .append("\n");
        }

        if (raceManager.getWinner() != null) {
            message.append("\nWinner: ")
                    .append(raceManager.getWinner().getName());
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Race Results");
        alert.setHeaderText("Race Finished");
        alert.setContentText(message.toString());
        alert.show();
    }
    
}
