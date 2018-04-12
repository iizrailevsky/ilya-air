package com.iizrailevsky.air.objects.impl;

import com.iizrailevsky.air.objects.Flight;
import com.iizrailevsky.air.objects.State;

public class FlightImpl implements Flight{

    // flight number
    private int flightNumber;
    // flight state
    private State state;

    /**
     * Constructor
     * @param flightNumber
     * @param state
     */
    public FlightImpl(int flightNumber, State state) {
        this.flightNumber = flightNumber;
        this.state = state;
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
    public String toString() {
        return "" + getFlightNumber();
    }
}
