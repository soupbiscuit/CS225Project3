import java.util.ArrayList;
import java.util.List;

public class RaceManager {

    private List<Car> cars = new ArrayList<>();
    private List<RaceResult> results = new ArrayList<>();

    private boolean raceRunning = false;
    private boolean raceFinished = false;
    private Car winner;

    public List<Car> getCars() {
        return cars;
    }

    public List<RaceResult> getResults() {
        return results;
    }

    public boolean isRaceFinished() {
        return raceFinished;
    }

    public Car getWinner() {
        return winner;
    }

    public void startRace() {
        raceRunning = true;
    }

    /**
     * Updates the race by updating each unfinished car.
     */
    public void updateRace(double deltaTime) {
        if (!raceRunning || raceFinished) {
            return;
        }

        for (Car car : cars) {
            if (!car.isFinished()) {
                double baseSpeed = car.getEngine().getTopSpeed();
                double grip = car.getTires().getGrip();

                // Random variation to make the race feel less predictable
                double variation = 0.8 + Math.random() * 0.4;
                car.setSpeed(baseSpeed * variation * grip);

                car.update(deltaTime);
            }
        }

        boolean allFinished = true;
        for (Car car : cars) {
            if (!car.isFinished()) {
                allFinished = false;
                break;
            }
        }

        if (allFinished) {
            raceFinished = true;
            raceRunning = false;

            Car winningCar = determineWinner();
            winner = winningCar;

            results.clear();

            for (Car car : cars) {
                boolean isWinner = (car == winningCar);

                results.add(new RaceResult(
                        car,
                        car.getTotalTime(),
                        isWinner
                ));
            }
        }
    }

    /**
     * Finds the car with the smallest completion time.
     */
    public Car determineWinner() {
        Car best = null;
        double bestTime = Double.MAX_VALUE;

        for (Car car : cars) {
            if (car.isFinished() && car.getTotalTime() < bestTime) {
                bestTime = car.getTotalTime();
                best = car;
            }
        }

        return best;
    }

    /**
     * Resets the race so it can be run again.
     */
    public void resetRace() {
        raceRunning = false;
        raceFinished = false;
        winner = null;
        results.clear();

        for (Car car : cars) {
            car.reset();
        }
    }
}