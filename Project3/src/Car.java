import java.util.List;

/**
 * The Car class represents a car in the race.
 * Each Car has a name, an engine, tires, a speed,
 * and a path it will follow during the race.
 * @author Danielle Gullage
 */

public class Car {

    private String name;
    private Engine engine;
    private Tires tires;
    private double speed;
    private List<Stop> path;

    /**
     * Class constructor.
     * @param name the car's name.
     * @param engine the car's engine.
     * @param tires the car's tires.
     */
    public Car(String name, Engine engine, Tires tires) {
        this.name = name;
        this.engine = engine;
        this.tires = tires;

        // TODO: How will the car's speed and path be determined?
        this.speed = 0;
        this.path = null;
    }

    /**
     * Sets the car's name.
     * @param name the name the car will have.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the car's engine.
     * @param engine the engine the car will have.
     */
    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    /**
     * Sets the car's tires.
     * @param tires the tires the car will have.
     */
    public void setTires(Tires tires) {
        this.tires = tires;
    }

    /**
     * Sets the car's speed.
     * @param speed the speed the car will have.
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * Sets the car's path.
     * @param path the path the car will have.
     */
    public void setPath(List<Stop> path) {
        this.path = path;
    }

    /**
     * Gets the car's name.
     * @return the car's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the car's engine.
     * @return the car's engine.
     */
    public Engine getEngine() {
        return this.engine;
    }

    /**
     * Gets the car's tires.
     * @return the car's tires.
     */
    public Tires getTires() {
        return this.tires;
    }

    /**
     * Gets the car's speed.
     * @return the car's speed.
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * Gets the car's path.
     * @return the car's path.
     */
    public List<Stop> getPath() {
        return this.path;
    }

}
