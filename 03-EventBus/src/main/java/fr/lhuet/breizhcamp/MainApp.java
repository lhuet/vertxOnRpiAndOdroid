package fr.lhuet.breizhcamp;

import com.pi4j.wiringpi.Gpio;
import io.vertx.core.AbstractVerticle;

/**
 * Created by lhuet on 14/03/16.
 */
public class MainApp extends AbstractVerticle {


    @Override
    public void start() throws Exception {

        initHardware();

        vertx.deployVerticle(LedVerticle.class.getName());
        vertx.deployVerticle(ButtonVerticle.class.getName());

    }

    private void initHardware() {
        // PI4J Init
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
        }
    }
}
