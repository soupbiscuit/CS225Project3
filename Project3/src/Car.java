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

    // These fields track the car's progress during the race
    private int currentStopIndex = 0;      // Which stop the car is currently at
    private double progressToNextStop = 0; // Distance traveled toward next stop
    private double totalTime = 0.0;        // Total time spent racing
    private boolean finished = false;      // Whether the car has finished the race

    // Current position of the car (used for drawing in JavaFX)
    private double x;
    private double y;

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
     * Resets the car back to the beginning of its route.
     * This is used when restarting the race.
     */
    public void reset() {
        currentStopIndex = 0;
        progressToNextStop = 0.0;
        totalTime = 0.0;
        finished = false;

        // Place the car at the starting stop
        if (path != null && !path.isEmpty()) {
            Stop start = path.get(0);
            x = start.getX();
            y = start.getY();
        }
    }

    /**
     * Checks whether the car has completed its entire route.
     * @return true if the car reached the final stop
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Updates the car's position and timing.
     * This is called every frame of the animation.
     *
     * @param deltaTime time passed since last update (in seconds)
     */
    public void update(double deltaTime) {

        // If the car is done or has no valid path, do nothing
        if (finished || path == null || path.size() < 2) {
            return;
        }

        // If there are no more stops to go to, mark as finished
        if (currentStopIndex >= path.size() - 1) {
            finished = true;
            return;
        }

        // Get the current stop and next stop
        Stop current = path.get(currentStopIndex);
        Stop next = path.get(currentStopIndex + 1);

        // Calculate direction vector
        double dx = next.getX() - current.getX();
        double dy = next.getY() - current.getY();

        // Distance between the two stops
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance == 0) return;

        // Move forward based on speed and elapsed time
        progressToNextStop += speed * deltaTime;

        // Add to total race time
        totalTime += deltaTime;

        // If the car reached or passed the next stop
        if (progressToNextStop >= distance) {

            // Snap to exact stop position
            x = next.getX();
            y = next.getY();

            // Move to the next segment
            currentStopIndex++;
            progressToNextStop = 0;

            // If no more stops remain, finish race
            if (currentStopIndex >= path.size() - 1) {
                finished = true;
            }

        } else {
            // Otherwise, interpolate position between stops
            double ratio = progressToNextStop / distance;

            x = current.getX() + ratio * dx;
            y = current.getY() + ratio * dy;
        }
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

    public double getX() { return x; }
    public double getY() { return y; }
    public double getTotalTime() { return totalTime; }

}
