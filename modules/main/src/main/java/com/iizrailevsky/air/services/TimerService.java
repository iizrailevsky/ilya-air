package com.iizrailevsky.air.services;

import com.iizrailevsky.air.objects.Runway;

/**
 * Represents timer service
 */
public class TimerService implements Runnable {

    // airport runway (one for this use case)
    private Runway runway;
    // timeout value
    private int timeoutInMillis;
    // time
    private long baseTimeInSec = System.currentTimeMillis() / 1000;

    /**
     * Constructor
     * @param runway
     * @param timeout
     */
    public TimerService(Runway runway, int timeout) {
        this.runway = runway;
        this.runway.registerTimer(this);
        this.timeoutInMillis = timeout * 1000;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(this.timeoutInMillis);
                this.runway.update();
            } catch (InterruptedException e) {

            }
        }
    }

    /**
     * Timeout in seconds
     * @return
     */
    public int getTimeoutInSec() {
        return timeoutInMillis / 1000;
    }


    /**
     * Get elapsed time in seconds
     */
    public long elapsedSeconds() {
        return System.currentTimeMillis() / 1000 - baseTimeInSec;
    }
}
