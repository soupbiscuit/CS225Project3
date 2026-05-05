import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

/**
 * The RaceManager class is responsible for controlling the entire race simulation.
 * It manages the collection of Car objects, updates their positions over time,
 * applies randomized speed variations, and determines when the race has finished.
 * This class also identifies the winning car and generates RaceResult objects
 * that summarize the performance of each car after the race completes.
 * Additionally, RaceManager supports restarting the race by resetting all cars
 * and generating new loop-based paths for each simulation.
 * Jasper Carr + Quincy Williams
 */
public class RaceManager {

    // Track coordinate constants. These should match the coordinates used in TrackView.
    private static final int A_X = 200;
    private static final int A_Y = 350;
    private static final int B_X = 500;
    private static final int B_Y = 150;
    private static final int C_X = 850;
    private static final int C_Y = 150;
    private static final int D_X = 1050;
    private static final int D_Y = 350;

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

    public boolean isRaceRunning() {
        return raceRunning;
    }

    public Car getWinner() {
        return winner;
    }

    /**
     * Starts the race.
     * The animation timer in TrackView will call updateRace(...) while this is true.
     */
    public void startRace() {
        raceRunning = true;
        raceFinished = false;
    }

    /**
     * Stops the race without rebuilding the cars.
     * This is useful when the race is finished or restarted.
     */
    public void stopRace() {
        raceRunning = false;
    }

    /**
     * Updates the race by updating each unfinished car.
     *
     * @param deltaTime the time passed since the previous frame, in seconds
     */
    public void updateRace(double deltaTime) {
        if (!raceRunning || raceFinished) {
            return;
        }

        for (Car car : cars) {
            if (!car.isFinished()) {
                double baseSpeed = car.getEngine().getTopSpeed();
                double grip = car.getTires().getGrip();

                // Random variation makes each race slightly unpredictable.
                double variation = 0.8 + Math.random() * 0.4;

                // The car's speed is based on its engine, tires, and random variation.
                car.setSpeed(baseSpeed * variation * grip);

                // Move the car along its path.
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
     *
     * @return the car that completed the race fastest
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
     * Resets the current cars to their starting positions.
     * This does not create new cars or new paths.
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

    /**
     * Sets up a new race by creating the stops, cars, engines, tires, and paths.
     * Each car starts at a different stop but follows the same loop direction.
     */
    public void setUpRace() {
        cars.clear();
        results.clear();
        winner = null;
        raceFinished = false;
        raceRunning = false;

        // These stops define the loop track.
        List<Stop> stops = new ArrayList<>();
        stops.add(new Stop("A", A_X, A_Y));
        stops.add(new Stop("B", B_X, B_Y));
        stops.add(new Stop("C", C_X, C_Y));
        stops.add(new Stop("D", D_X, D_Y));

        String[] carNames = {"Blue Car", "Brown Car", "Red Car", "Yellow Car"};
        String[] tireNames = {"Soft", "Medium", "Hard", "All-Weather"};
        String[] engineNames = {"V6", "V8", "Electric", "Hybrid"};

        // Shuffle once so each car receives a different starting stop.
        List<Stop> shuffledStarts = new ArrayList<>(stops);
        Collections.shuffle(shuffledStarts);

        for (int i = 0; i < carNames.length; i++) {
            double topSpeed = 150 + Math.random() * 150;
            double grip = 0.7 + Math.random() * 0.4;

            String engineName = engineNames[(int) (Math.random() * engineNames.length)];
            String tireName = tireNames[(int) (Math.random() * tireNames.length)];

            Stop startStop = shuffledStarts.get(i % shuffledStarts.size());

            // All cars stay on the loop track, but each starts at a different stop.
            List<Stop> path = generateLoopPath(stops, startStop);

            Car car = new Car(
                    carNames[i],
                    new Engine(engineName, topSpeed),
                    new Tires(tireName, grip),
                    path
            );

            car.reset();
            cars.add(car);
        }
    }

    /**
     * Generates a path that starts at a given stop and follows the loop direction.
     * Example: A -> B -> C -> D, or C -> D -> A -> B.
     *
     * @param stops the ordered loop stops
     * @param startStop the stop where the car starts
     * @return a loop path beginning at startStop
     */
    public List<Stop> generateLoopPath(List<Stop> stops, Stop startStop) {
        List<Stop> path = new ArrayList<>();

        int startIndex = stops.indexOf(startStop);

        for (int i = 0; i < stops.size(); i++) {
            int index = (startIndex + i) % stops.size();
            path.add(stops.get(index));
        }

        return path;
    }
}
