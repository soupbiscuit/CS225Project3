/**
 * The Engine class represents the engine of a car.
 * Each Engine has a name and a top speed.
 * @author Danielle Gullage
 */

public class Engine {

    private String name;
    private double topSpeed;

    /**
     * Class constructor.
     * @param name the engine's name.
     * @param topSpeed the engine's top speed.
     */
    public Engine(String name, double topSpeed) {
        this.name = name;
        this.topSpeed = topSpeed;
    }

    /**
     * Sets the engine's name.
     * @param name the name the engine will have.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the engine's top speed.
     * @param topSpeed the top speed the engine will have.
     */
    public void setTopSpeed(double topSpeed) {
        this.topSpeed = topSpeed;
    }

    /**
     * Gets the engine's name.
     * @return the engine's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the engine's top speed.
     * @return the engine's top speed.
     */
    public double getTopSpeed() {
        return this.topSpeed;
    }

}
