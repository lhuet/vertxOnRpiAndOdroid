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

        vertx.deployVerticle(Ds18b20Verticle.class.getName());
        vertx.deployVerticle(LedVerticle.class.getName());
        vertx.deployVerticle(HttpVerticle.class.getName());

    }

    private void initHardware() {
        // PI4J Init
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
        }
    }
}
