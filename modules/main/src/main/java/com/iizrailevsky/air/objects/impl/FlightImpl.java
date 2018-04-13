package com.iizrailevsky.air.objects.impl;

import com.iizrailevsky.air.objects.Flight;
import com.iizrailevsky.air.objects.State;

public class FlightImpl implements Flight{

    // flight number
    private int flightNumber;
    // flight state
    private State state;
    // time of flight registration
    private long timeRegistered;

    /**
     * Constructor
     * @param flightNumber
     */
    public FlightImpl(int flightNumber) {
        this.flightNumber = flightNumber;
        this.timeRegistered = System.currentTimeMillis();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getFlightNumber() {
        return flightNumber;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public State getState() {
        return state;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setState(State state) {
        this.state = state;
    }

    @Override
    public Long getTimeRegistered() {
        return timeRegistered;
    }

    @Override
    public String toString() {
        return "" + getFlightNumber();
    }
}
