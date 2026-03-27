/**
 * The Tires class represents the tires of a car.
 * Tires have a name and a grip.
 * @author Danielle Gullage
 */

public class Tires {

    private String name;
    private double grip;

    /**
     * Class constructor.
     * @param name the tires' name.
     * @param grip the tires' grip.
     */
    public Tires(String name, double grip) {
        this.name = name;
        this.grip = grip;
    }

    /**
     * Sets the name of the tires.
     * @param name the name the tires will have.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the grip of the tires.
     * @param grip the grip the tires will have.
     */
    public void setGrip(double grip) {
        this.grip = grip;
    }

    /**
     * Gets the name of the tires.
     * @return the name of the tires.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the grip of the tires.
     * @return the grip of the tires.
     */
    public double getGrip() {
        return this.grip;
    }

}
