//at the add more to this later
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
