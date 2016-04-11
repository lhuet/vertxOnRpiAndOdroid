package fr.lhuet.devoxx;

import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import io.vertx.core.Vertx;

/**
 * Created by lhuet on 14/03/16.
 */
public class VertxHelloWorld {

    public static void main(String[] args) {

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

        // Vertx event timer
        Vertx.vertx().setPeriodic(1000, l -> {
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

}
