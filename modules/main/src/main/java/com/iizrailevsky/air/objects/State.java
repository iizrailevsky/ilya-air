package com.iizrailevsky.air.objects;

/**
 * Flight states
 */
public enum State {
    INFLIGHT_TAKEOFF(1),
    INFLIGHT_LANDING(1),
    WAITING_TAKEOFF(3),
    WAITING_LANDING(2),
    SUCCESSFUL_TAKEOFF(0),
    SUCCESSFUL_LANDING(0);

    private final int priority;

    State(int priority) {
        this.priority = priority;
    }

    public Integer getPriority() {
        return priority;
    }
}
