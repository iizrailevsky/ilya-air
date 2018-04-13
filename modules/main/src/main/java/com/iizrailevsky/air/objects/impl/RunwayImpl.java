package com.iizrailevsky.air.objects.impl;

import com.iizrailevsky.air.objects.Flight;
import com.iizrailevsky.air.objects.Runway;
import com.iizrailevsky.air.objects.State;
import com.iizrailevsky.air.services.TimerService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

public class RunwayImpl implements Runway {

    // timer service
    private TimerService timerService;
    // priority queue for the flight
    private PriorityBlockingQueue<Flight> q;
    // list of completed flights for landings or takeoffs
    private List<Flight> completedList;

    public RunwayImpl() {
        this.q = new PriorityBlockingQueue<>(10, new FlightComparator());

        this.completedList = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendForTakeoff(Flight flight) {
        // set flight for takeoff
        flight.setState(State.WAITING_TAKEOFF);
        this.q.add(flight);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendForLanding(Flight flight) {
        // set flight for landing
        flight.setState(State.WAITING_LANDING);
        this.q.add(flight);
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
            if (head != null) {
                switch (head.getState()) {
                    case WAITING_LANDING:
                        head.setState(State.INFLIGHT_LANDING);
                        break;

                    case WAITING_TAKEOFF:
                        head.setState(State.INFLIGHT_TAKEOFF);
                        break;

                    case INFLIGHT_LANDING:
                        head.setState(State.SUCCESSFUL_LANDING);
                        q.take();
                        completedList.add(head);
                        advanceWaitingFlight();
                        break;

                    case INFLIGHT_TAKEOFF:
                        head.setState(State.SUCCESSFUL_TAKEOFF);
                        q.take();
                        completedList.add(head);
                        advanceWaitingFlight();
                        break;

                    case SUCCESSFUL_LANDING:
                    case SUCCESSFUL_TAKEOFF:
                        q.take();
                        completedList.add(head);
                        break;

                    default:
                        break;
                }
            }

        } catch (InterruptedException e) {
            System.err.println(e.getMessage());
        } finally {

        }
    }

    /**
     * Advances awaiting flight for inflight landing or takeoff
     */
    private void advanceWaitingFlight() throws InterruptedException {
        Flight head = q.peek();
        if (head != null) {
            if (head.getState().equals(State.WAITING_LANDING)) {
                head.setState(State.INFLIGHT_LANDING);
            } else if (head.getState().equals(State.WAITING_TAKEOFF)) {
                head.setState(State.INFLIGHT_TAKEOFF);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printStatus() {
        // get all the elements of the q
        Flight[] flights = this.q.toArray(new Flight[0]);
        Arrays.sort(flights, new FlightComparator());

        StringBuilder sb = new StringBuilder(200);

        sb.append("Timeout: ").append(this.timerService.getTimeoutInSec()).append("\n");
        sb.append("Time: ").append(timerService.elapsedSeconds()).append(" sec").append("\n");

        sb.append("Inflight for takeoff: ");
        int inForTakeoffCount = 0;
        for (Flight f1: flights) {
            if (f1.getState().equals(State.INFLIGHT_TAKEOFF)) {
                if (inForTakeoffCount > 0) {
                    sb.append(" ");
                }
                sb.append(f1);
                inForTakeoffCount++;
            }
        }
        if (inForTakeoffCount == 0) {
            sb.append("none");
        }
        sb.append("\n");

        sb.append("Waiting for takeoff: ");
        int waitForTakeoffCount = 0;
        for (Flight f1: flights) {
            if (f1.getState().equals(State.WAITING_TAKEOFF)) {
                if (waitForTakeoffCount > 0) {
                    sb.append(" ");
                }
                sb.append(f1);
                waitForTakeoffCount++;
            }
        }
        if (waitForTakeoffCount == 0) {
            sb.append("none");
        }
        sb.append("\n");

        sb.append("Inflight for landing: ");
        int inForLandingCount = 0;
        for (Flight f1: flights) {
            if (f1.getState().equals(State.INFLIGHT_LANDING)) {
                if (inForLandingCount > 0) {
                    sb.append(" ");
                }
                sb.append(f1);
                inForLandingCount++;
            }
        }
        if (inForLandingCount == 0) {
            sb.append("none");
        }
        sb.append("\n");

        sb.append("Waiting for landing: ");
        int waitForLandCount = 0;
        for (Flight f1: flights) {
            if (f1.getState().equals(State.WAITING_LANDING)) {
                if (waitForLandCount > 0) {
                    sb.append(" ");
                }
                sb.append(f1);
                waitForLandCount++;
            }
        }
        if (waitForLandCount == 0) {
            sb.append("none");
        }
        sb.append("\n");

        sb.append("Successful takeoffs: ");
        int successTakeoffsCount = 0;
        for (Flight f1: completedList) {
            if (f1.getState().equals(State.SUCCESSFUL_TAKEOFF)) {
                if (successTakeoffsCount > 0) {
                    sb.append(" ");
                }
                sb.append(f1);
                successTakeoffsCount++;
            }
        }
        if (successTakeoffsCount == 0) {
            sb.append("none");
        }
        sb.append("\n");

        sb.append("Successful landings: ");
        int successLandCount = 0;
        for (Flight f1: completedList) {
            if (f1.getState().equals(State.SUCCESSFUL_LANDING)) {
                if (successLandCount > 0) {
                    sb.append(" ");
                }
                sb.append(f1);
                successLandCount++;
            }
        }
        if (successLandCount == 0) {
            sb.append("none");
        }
        sb.append("\n");
        System.out.print(sb);
    }

    @Override
    public void registerTimer(TimerService timerService) {
        this.timerService = timerService;
    }

    PriorityBlockingQueue<Flight> getQ() { return this.q; }
}
