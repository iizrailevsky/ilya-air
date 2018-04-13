package com.iizrailevsky.air.objects.impl;

import com.iizrailevsky.air.objects.Flight;
import com.iizrailevsky.air.objects.Runway;
import com.iizrailevsky.air.services.RunwayService;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.*;

public class RunwayImplTest {
    private RunwayImpl runway;

    @Before
    public void setup() {
        this.runway = new RunwayImpl();
        RunwayService runwayService = new RunwayService(this.runway, new Scanner(System.in));
    }

    @Test
    public void testTakeoff() {
        this.runway.sendForTakeoff(new FlightImpl(10));
        this.runway.sendForTakeoff(new FlightImpl(11));
        this.runway.sendForTakeoff(new FlightImpl(12));
        assertEquals(this.runway.getQ().size(), 3);
    }

    @Test
    public void testLanding() {
        this.runway.sendForLanding(new FlightImpl(15));
        this.runway.sendForLanding(new FlightImpl(16));
        assertEquals(this.runway.getQ().size(), 2);
    }

    @Test
    public void testTakeoffLanding() throws InterruptedException {
        this.runway.sendForTakeoff(new FlightImpl(10));
        Thread.sleep(10);
        this.runway.sendForTakeoff(new FlightImpl(11));
        Thread.sleep(10);
        this.runway.sendForTakeoff(new FlightImpl(12));
        Thread.sleep(10);
        this.runway.sendForLanding(new FlightImpl(15));
        Thread.sleep(10);
        this.runway.sendForLanding(new FlightImpl(16));
        assertEquals(this.runway.getQ().size(), 5);

        List<Flight> flightList = new ArrayList<>();
        this.runway.getQ().drainTo(flightList);
        assertEquals(15, flightList.get(0).getFlightNumber());
        assertEquals(16, flightList.get(1).getFlightNumber());
        assertEquals(10, flightList.get(2).getFlightNumber());
        assertEquals(11, flightList.get(3).getFlightNumber());
        assertEquals(12, flightList.get(4).getFlightNumber());
    }

    @After
    public void tearDown() {
        this.runway = null;
    }

}
