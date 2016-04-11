package fr.lhuet.devoxx;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;

/**
 * Created by lhuet on 14/03/16.
 */
public class VertxVerticleHelloWorld extends AbstractVerticle {

    public static void main(String[] args) {

        // Verticle deployment
        Vertx.vertx().deployVerticle(VertxVerticleHelloWorld.class.getName());
    }

    @Override
    public void start() throws Exception {

        System.out.println("Starting Vertx Verticle ...");

        initGPIO();

        vertx.setPeriodic(1000, l -> {
            // Blink led every seconds
            if (Gpio.digitalRead(1) != Gpio.LOW) {
                System.out.println("Switch off ...");
                Gpio.digitalWrite(1, Gpio.LOW);
            }
            else {
                System.out.println("Switch on ...");
                Gpio.digitalWrite(1, Gpio.HIGH);
            }
        });

    }

    @Override
    public void stop() throws Exception {
        System.out.println("Stopping Vertx Verticle ...");

        System.out.println("Switch off ...");
        Gpio.digitalWrite(1, Gpio.LOW);

    }

    private void initGPIO() {
        // PI4J Init
        if (Gpio.wiringPiSetup() == -1) {
            System.out.println(" ==>> GPIO SETUP FAILED");
            return;
        }

        // GPIO 1 init as Output
        GpioUtil.export(1, GpioUtil.DIRECTION_OUT);
        Gpio.pinMode (1, Gpio.OUTPUT) ;
        // Force low state for GPIO 1
        Gpio.digitalWrite(1, Gpio.LOW);
    }

}
