package com.iizrailevsky.air.services;

import com.iizrailevsky.air.objects.Flight;
import com.iizrailevsky.air.objects.Runway;
import com.iizrailevsky.air.objects.impl.FlightImpl;
import com.iizrailevsky.air.objects.impl.RunwayImpl;

import java.awt.*;
import java.util.Scanner;

/**
 * Represents an airport runway service which processes landing and taking off flights
 */
public class RunwayService implements Runnable {

    // airport runway (one for this use case)
    private Runway runway;
    // input scanner
    private Scanner scanner;
    // input error message
    private static final String error = "Please enter: takeoff <flight id>, land <flight id>, status, or exit";

    /**
     * Constructor
     * @param runway
     * @param scanner
     */
    public RunwayService(Runway runway, Scanner scanner) {
        this.runway = runway;
        this.scanner = scanner;
    }

    @Override
    public void run() {
        String command = null;

        // read commands
        while (true) {
            command = scanner.nextLine();
            String[] tokens = command.split(" ");
            if (tokens.length == 0) {
                System.out.println(error);
            } else if (tokens.length == 1) {
                if (tokens[0].equalsIgnoreCase("status")) {
                    this.runway.printStatus();
                } else if (tokens[0].equalsIgnoreCase("exit")) {
                    System.exit(0);
                } else {
                    System.err.println(error);
                }
            } else if (tokens.length == 2) {
                try {
                    if (tokens[0].equalsIgnoreCase("takeoff")) {
                        Flight flight = new FlightImpl(Integer.parseInt(tokens[1]));
                        this.runway.sendForTakeoff(flight);
                    } else if (tokens[0].equalsIgnoreCase("land")) {
                        Flight flight = new FlightImpl(Integer.parseInt(tokens[1]));
                        this.runway.sendForLanding(flight);
                    } else {
                        System.out.println(error);
                    }
                } catch (NumberFormatException e) {
                    System.out.println(error);
                }
            }
        }

    }
}
