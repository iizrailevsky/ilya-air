package com.iizrailevsky.air.objects;

/**
 * Represents a flight
 */
public interface Flight {

    /**
     * Returns flight number
     */
    int getFlightNumber();

    /**
     * Returns flight state
     */
    State getState();

    /**
     * Sets flight state
     */
    void setState(State state);

    /**
     * Returns time flight was registered
     */
    Long getTimeRegistered();

}
