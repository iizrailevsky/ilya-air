package com.iizrailevsky.air.objects.impl;

import com.iizrailevsky.air.objects.Flight;
import com.iizrailevsky.air.objects.Runway;
import com.iizrailevsky.air.objects.State;
import com.iizrailevsky.air.services.TimerService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

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

        appendToStatusFromQ(flights, sb, "Inflight for takeoff: ", State.INFLIGHT_TAKEOFF);

        appendToStatusFromQ(flights, sb, "Waiting for takeoff: ", State.WAITING_TAKEOFF);

        appendToStatusFromQ(flights, sb, "Inflight for landing: ", State.INFLIGHT_LANDING);

        appendToStatusFromQ(flights, sb, "Waiting for landing: ", State.WAITING_LANDING);

        appendToStatusFromCompleted(sb, "Successful takeoffs: ", State.SUCCESSFUL_TAKEOFF);

        appendToStatusFromCompleted(sb, "Successful landings: ", State.SUCCESSFUL_LANDING);
        System.out.print(sb);
    }

    /**
     * Appends status from q
     * @param flights
     * @param sb
     * @param s
     * @param state
     */
    private void appendToStatusFromQ(Flight[] flights, StringBuilder sb, String s, State state) {
        sb.append(s);
        int count = 0;
        for (Flight f1 : flights) {
            if (f1.getState().equals(state)) {
                if (count > 0) {
                    sb.append(", ");
                }
                sb.append(f1);
                count++;
            }
        }
        if (count == 0) {
            sb.append("none");
        }
        sb.append("\n");
    }

    /**
     * Appends status from completed list
     * @param sb
     * @param s
     * @param state
     */
    private void appendToStatusFromCompleted(StringBuilder sb, String s, State state) {
        sb.append(s);
        int count = 0;
        for (Flight f1 : completedList) {
            if (f1.getState().equals(state)) {
                if (count > 0) {
                    sb.append(" ");
                }
                sb.append(f1);
                count++;
            }
        }
        if (count == 0) {
            sb.append("none");
        }
        sb.append("\n");
    }

    @Override
    public void registerTimer(TimerService timerService) {
        this.timerService = timerService;
    }

    PriorityBlockingQueue<Flight> getQ() { return this.q; }
}
