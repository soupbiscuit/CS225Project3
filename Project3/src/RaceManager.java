import java.util.ArrayList;
import java.util.List;
import java.util.Collections;


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

    public void setUpRace() {
        List<Stop> stops = new ArrayList<>();
        stops.add(new Stop("Start", 800, 550));
        stops.add(new Stop("A", 200, 400));
        stops.add(new Stop("B", 700, 150));
        stops.add(new Stop("C", 1300, 150));
        stops.add(new Stop("D", 1800, 400));
        

        Car blueCar = new Car("Blue Car", new Engine("V8",200), new Tires("Sport",0.9), generateRandomPath(stops));
        Car brownCar = new Car("Brown Car", new Engine("V6",180), new Tires("Standard",0.8), generateRandomPath(stops));
        Car redCar = new Car("Red Car", new Engine("Electric",210), new Tires("Racing",0.95), generateRandomPath(stops));
        Car yellowCar = new Car("Yellow Car", new Engine("V12",250), new Tires("Off-road",1), generateRandomPath(stops));

        for (Car car : List.of(blueCar, brownCar, redCar, yellowCar)) {
            car.reset();
            cars.add(car);
        }

        startRace();
    }

    public List<Stop> generateRandomPath(List<Stop> stops) {
    // Keep the first stop (starting position) fixed
    Stop start = stops.get(0);
    
    // Shuffle the rest
    List<Stop> middleStops = new ArrayList<>(stops.subList(1, stops.size()));
    Collections.shuffle(middleStops);
    
    // Build the full path
    List<Stop> path = new ArrayList<>();
    path.add(start);
    path.addAll(middleStops);
    
    return path;
    }
}