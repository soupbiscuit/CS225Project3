/**
 * The RaceResult class represents the outcome of a single car in a race.
 * Each RaceResult object stores the car associated with the result,
 * the total time taken to complete the race, and whether the car is
 * the winner.
 * This class is used by the RaceManager to store and organize results
 * for all cars, and by the TrackView to display those results to the user.
 * Jonathan Joseph + Jasper Carr
 */

public class RaceResult {

    private Car car;        // The car this result belongs to
    private double totalTime;
    private boolean winner; // Whether this car is the winner

    /**
     * Constructor for RaceResult
     */
    public RaceResult(Car car, double totalTime, boolean winner) {
        this.car = car;
        this.totalTime = totalTime;
        this.winner = winner;
    }

    /**
     * Returns the car associated with this result
     */
    public Car getCar() {
        return car;
    }

    /**
     * Returns total time taken by the car
     */
    public double getTotalTime() {
        return totalTime;
    }

    /**
     * Returns whether this car is the winner
     */
    public boolean isWinner() {
        return winner;
    }

    /**
     * Allows setting winner status later
     */
    public void setWinner(boolean winner) {
        this.winner = winner;
    }
}
