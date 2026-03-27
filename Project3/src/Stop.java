/**
 * The Stop class represents a stop in the race.
 * Each Stop has a name and (x, y) coordinates.
 * @author Danielle Gullage
 */

public class Stop {

    private String stopName;
    private int x;
    private int y;

    /**
     * Class constructor.
     * @param stopName the stop's name.
     * @param x the stop's x coordinate.
     * @param y the stop's y coordinate.
     */
    public Stop(String stopName, int x, int y) {
        this.stopName = stopName;
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the stop's name.
     * @param stopName the name the stop will have.
     */
    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    /**
     * Sets the stop's x coordinate.
     * @param x the x coordinate the stop will have.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the stop's y coordinate.
     * @param y the y coordinate the stop will have.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * Gets the stop's name.
     * @return the stop's name.
     */
    public String getStopName() {
        return this.stopName;
    }

    /**
     * Gets the stop's x coordinate.
     * @return the stop's x coordinate.
     */
    public int getX() {
        return this.x;
    }

    /**
     * Gets the stop's y coordinate.
     * @return the stop's y coordinate.
     */
    public int getY() {
        return this.y;
    }

    /**
     * Gets the distance between this stop and
     * a given stop using their (x, y) coordinates.
     * @param nextStop the stop that will be compared with.
     * @return the distance between the two stops.
     */
    public double distanceToStop(Stop nextStop) {
        int x1 = this.x;
        int y1 = this.y;
        int x2 = nextStop.getX();
        int y2 = nextStop.getY();

        // Use the distance formula to get the distance between 2 points
        return Math.hypot(x1 - x2, y1 - y2);
    }

}
