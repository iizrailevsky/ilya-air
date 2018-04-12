package com.iizrailevsky.air;

import java.io.IOException;
import java.util.Properties;

public class Main {

    public static void main(String[] args) throws IOException {
        Properties properties = new Properties();

        properties.load(Main.class.getResourceAsStream("/proto.properties"));

        System.out.println("hello world!!! signed: " + properties.getProperty("salutations", "--unknown--"));
        System.out.println("salve mundi!!! signed: " + properties.getProperty("salutations", "--unknown--"));
    }
}
