package com.brainacad.race;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Thread.sleep;

/**
 * Extends car model.
 * It's race car.
 *
 * @author Vladimir Bratchikov
 */
public class RaceCarRunnable extends Car implements Runnable {

    private static final double METERS_PER_SEC = 1000d / 3600;
    private static final int ONE_SEC_MILLIS = 1000;

    private final CountDownLatch countDownLatch;
    private final int distance;

    private int passed = 0;
    private int currentSpeed;
    private boolean isFinish = false;
    private long finishTime;

    public RaceCarRunnable(Car car, int distance, CountDownLatch countDownLatch) {
        super(car.getName(), car.getMaxSpeed());
        this.distance = distance;
        this.countDownLatch = countDownLatch;
    }

    public long getFinishTime() {
        return finishTime / ONE_SEC_MILLIS;
    }

    @Override
    public void run() {
        try {
            while (!isFinish) { // check if car still drive
                sleep(ONE_SEC_MILLIS);
                finishTime = System.currentTimeMillis() - Race.startRaceTime.get();
                passed += getRandomSpeed() * METERS_PER_SEC; // speed: { m / s }
                if (passed >= distance) { // distance: { m }
                    System.out.println(getName() + " FINISHED!!! Time:" + getFinishTime() );
                    isFinish = true;
                    countDownLatch.countDown(); // decrement thread counter
                } else {
                    System.out.println(this);
                }
            }
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
        }
    }

    @Override
    public String toString() {
        return getName() + " \t\t => " + "speed: " + currentSpeed + " { km/h }; progress: " +
                passed + "/" + distance + "{ m }" + " Time: " + getFinishTime();
    }

    /**
     * Calc random speed in range [min: maxSpeed / 2; max: maxSpeed;]
     * @return random speed in range.
     */
    private int getRandomSpeed() {
        return currentSpeed = ThreadLocalRandom.current().nextInt((getMaxSpeed() / 2), getMaxSpeed());
    }

}