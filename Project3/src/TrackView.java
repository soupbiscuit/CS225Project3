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

public class TrackView extends Application {
    @Override
    public void start(Stage game)  {
        Pane root = new Pane();

        ImageView blueCar = new ImageView(new Image(getClass().getResourceAsStream("/Resources/bluecar-2.png")));
        ImageView brownCar = new ImageView(new Image(getClass().getResourceAsStream("/Resources/brown-1.png")));
        ImageView redCar = new ImageView(new Image(getClass().getResourceAsStream("/Resources/decapred-1.png")));
        ImageView yellowCar = new ImageView(new Image(getClass().getResourceAsStream("/Resources/yellow1.png")));

        blueCar.setFitWidth(20);
        blueCar.setFitHeight(10);
        brownCar.setFitWidth(20);
        brownCar.setFitHeight(10);
        redCar.setFitWidth(20);
        redCar.setFitHeight(10);
        yellowCar.setFitWidth(20);
        yellowCar.setFitHeight(10);

        blueCar.setLayoutX(100);
        blueCar.setLayoutY(50);
        brownCar.setLayoutX(100);
        brownCar.setLayoutY(80);
        redCar.setLayoutX(100);
        redCar.setLayoutY(110);
        yellowCar.setLayoutX(100);
        yellowCar.setLayoutY(140);

        Group markerA = makeFlag("A", 500, 50);
        Group markerB = makeFlag("B", 1000, 50);
        Group markerC = makeFlag("C", 1500, 50);
        Group markerD = makeFlag("D", 1800, 50);

        root.getChildren().addAll(blueCar, brownCar, redCar, yellowCar, markerA, markerB, markerC, markerD);

        Scene scene = new Scene(root, 2000, 800);
        game.setScene(scene);
        game.setTitle("Track View");
        game.show();
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
    
}
