package com.brainacad.race;

/**
 * Simple car POJO model
 *
 * @author Vladimir Bratchikov
 */
public class Car {

    private String name;
    private int maxSpeed; // max speed in km/h

    public Car(String name, int maxSpeed) {
        this.name = name;
        this.maxSpeed = maxSpeed;
    }

    public String getName() {
        return name;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }
}
