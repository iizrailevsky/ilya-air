package com.iizrailevsky.air.objects.impl;

import com.iizrailevsky.air.objects.Flight;
import com.iizrailevsky.air.objects.Runway;
import com.iizrailevsky.air.objects.State;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RunwayImpl implements Runway {

    // priority queue for the flight
    private PriorityBlockingQueue<Flight> q;
    // list of completed flights for landings or takeoffs
    private List<Flight> completedList;

    public RunwayImpl() {
        this.q = new PriorityBlockingQueue<>(10, new Comparator<Flight>() {
            @Override
            public int compare(Flight o1, Flight o2) {
                return o1.getState().getPriority().compareTo(o2.getState().getPriority());
            }
        });

        this.completedList = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendForTakeoff(Flight flight) {
        // set flight for takeoff
        flight.setState(State.WAITING_TAKEOFF);
        this.q.offer(flight, 1, TimeUnit.SECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendForLanding(Flight flight) {
        // set flight for landing
        flight.setState(State.WAITING_LANDING);
        this.q.offer(flight, 1, TimeUnit.SECONDS);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update() {
        // time period has passed
        // process the queue
        try {
            Flight head = q.peek();
            switch (head.getState()) {
                case WAITING_LANDING:
                    head.setState(State.INFLIGHT_LANDING);
                    break;

                case WAITING_TAKEOFF:
                    head.setState(State.INFLIGHT_TAKEOFF);
                    break;

                case INFLIGHT_LANDING:
                    head.setState(State.SUCCESSFUL_LANDING);
                    q.poll(1, TimeUnit.SECONDS);
                    completedList.add(head);
                    break;

                case INFLIGHT_TAKEOFF:
                    head.setState(State.SUCCESSFUL_TAKEOFF);
                    q.poll(1, TimeUnit.SECONDS);
                    completedList.add(head);
                    break;

                case SUCCESSFUL_LANDING:
                case SUCCESSFUL_TAKEOFF:
                    q.poll(1, TimeUnit.SECONDS);
                    completedList.add(head);
                    break;

                default:
                    break;
            }

        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } finally {

        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printStatus() {

    }
}
