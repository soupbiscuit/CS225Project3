/**
 * @author:  Quincy Williams
 */
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
    //sets up the race by creating the stops and cars
    public void setUpRace() {
        //creats list of Stops fort he cars to follow
        List<Stop> stops = new ArrayList<>();
        stops.add(new Stop("Start", 800, 550));
        stops.add(new Stop("A", 200, 400));
        stops.add(new Stop("B", 700, 150));
        stops.add(new Stop("C", 1300, 150));
        stops.add(new Stop("D", 1800, 400));
        
        //list of car names, tire names, and engine names to randomly assign to the cars
        String[] carNames = {"Blue Car", "Brown Car", "Red Car", "Yellow Car"};
        String[] tireNames = {"Soft", "Medium", "Hard", "All-Weather"};
        String[] engineNames = {"V6", "V8", "Electric", "Hybrid"};

        for (String carName : carNames) {
            double topSpeed = 150 + Math.random() * 150; // Random top speed between 150 and 300
            double grip = 0.7 + Math.random() * 0.4; // Random grip between 0.7 and 1.1

            String engineName = engineNames[(int) (Math.random() * engineNames.length)];// Randomly select an engine name
            String tireName = tireNames[(int) (Math.random() * tireNames.length)];// Randomly select a tire name

            Car car = new Car(carName, new Engine(engineName, topSpeed), new Tires(tireName, grip), generateRandomPath(stops));
            car.reset(); 
            cars.add(car);
        }
        startRace();
    }
    //Shuffles the stops other than the starting point
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