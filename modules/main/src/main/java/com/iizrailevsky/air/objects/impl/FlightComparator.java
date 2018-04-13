package com.iizrailevsky.air.objects.impl;

import com.iizrailevsky.air.objects.Flight;

import java.util.Comparator;

/**
 * Flight comparator in the queue
 */
public class FlightComparator implements Comparator<Flight> {

    @Override
    public int compare(Flight o1, Flight o2) {
        if (o1.getState().getPriority().equals(o2.getState().getPriority())) {
            return o1.getTimeRegistered().compareTo(o2.getTimeRegistered());
        }
        return o1.getState().getPriority().compareTo(o2.getState().getPriority());
    }
}
