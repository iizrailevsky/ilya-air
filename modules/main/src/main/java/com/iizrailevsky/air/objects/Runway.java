package com.iizrailevsky.air.objects;

/**
 * Represents airport runway
 */
public interface Runway {

    /**
     * Sends flight for takeoff
     * @param flight Flight
     */
    void sendForTakeoff(Flight flight);

    /**
     * Sends flight for landing
     * @param flight Flight
     */
    void sendForLanding(Flight flight);

    /**
     * Update runway of passed time period
     */
    void update();

    /**
     * Lists current status of in-flight landing/takeoff,
     * waiting for landing/takeoff, and successfully landing/takeoff
     */
    void printStatus();

}
