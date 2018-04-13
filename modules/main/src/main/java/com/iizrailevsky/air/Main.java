package com.iizrailevsky.air;

import com.iizrailevsky.air.objects.Runway;
import com.iizrailevsky.air.objects.impl.RunwayImpl;
import com.iizrailevsky.air.services.RunwayService;
import com.iizrailevsky.air.services.TimerService;

import java.io.IOException;
import java.util.Properties;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();
        properties.load(Main.class.getResourceAsStream("/proto.properties"));

        Runway runway = new RunwayImpl();
        Scanner in = new Scanner(System.in);
        (new Thread(new TimerService(runway, Integer.parseInt(properties.getProperty("timeout"), 10)))).start();
        (new Thread(new RunwayService(runway, in))).start();
    }
}
