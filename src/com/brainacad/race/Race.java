package com.brainacad.race;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Main race class.
 *
 * @author Vladimir Bratchikov
 */
public class Race {

    static AtomicLong startRaceTime;

    public static void main(String[] args) {

        final int distance = 500;

        // init base car list
        final ArrayList<Car> cars = new ArrayList<>();
        cars.add(new Car("Aveo", 120));
        cars.add(new Car("Formula 1", 250));
        cars.add(new Car("BMW M5", 200));

        // init CountDownLatch by cars list size
        final CountDownLatch countDownLatch = new CountDownLatch(cars.size());

        // transform cars for race
        final ArrayList<RaceCarRunnable> raceCarRunnables = new ArrayList<>();
        cars.forEach(raceCar -> raceCarRunnables.add(new RaceCarRunnable(raceCar, distance, countDownLatch)));

        // init threads list
        final ArrayList<Thread> threads = new ArrayList<>();
        raceCarRunnables.forEach(raceCar -> threads.add(new Thread(raceCar)));  // Java 8 foreach

        // prepare and start race
        startRace(threads);

        try {
            countDownLatch.await(); // wait for all car finished
            System.out.println("=================== RACE OVER! ======================");
            System.out.println("!!!!!!!!!!!!! WINNER: " + defineWinner(raceCarRunnables).getName() + " !!!!!!!!!!!!!!!");
        } catch (InterruptedException ex) {
            System.err.print(ex);
        }
    }

    /**
     * Find winner car.
     * Serarch by car with min finish time.
     *
     * @param raceCarRunnables race cars list.
     * @return winner race car
     */
    private static RaceCarRunnable defineWinner(final ArrayList<RaceCarRunnable> raceCarRunnables) {
        return raceCarRunnables.stream()
                .min(Comparator.comparingLong(RaceCarRunnable::getFinishTime))
                .orElseThrow(NoSuchElementException::new);
    }

    /**
     * Start race by threads list.
     * Show countdown and run all racing cars.
     *
     * @param cars threads list with racing cars
     */
    private static void startRace(final List<Thread> cars) {
        new Thread(() -> { // Java 8 lambda
            try {
                // countdown to start
                System.out.println("3...");
                Thread.sleep(500);
                System.out.println("2...");
                Thread.sleep(500);
                System.out.println("1...");
                Thread.sleep(500);
                System.out.println("GO!!!");
                Thread.sleep(500);

                // init race start time
                startRaceTime = new AtomicLong(System.currentTimeMillis());

                synchronized (cars) {
                    for (Thread car : cars) {
                        car.start();
                    }
                }
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
        }).start();
    }
}