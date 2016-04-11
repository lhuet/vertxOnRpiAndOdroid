package fr.lhuet.breizhcamp;

import com.pi4j.io.gpio.*;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import io.vertx.core.Vertx;

/**
 * Created by lhuet on 14/03/16.
 */
public class VertxHelloWorld2 {

    public static void main(String[] args) {

        GpioController gpio = GpioFactory.getInstance();

        GpioPinDigitalOutput pin = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01, PinState.LOW);
        pin.setShutdownOptions(true, PinState.LOW);

        // Vertx event timer
        Vertx.vertx().setPeriodic(1000, l -> {
            // Blink led every seconds
            pin.toggle();
            System.out.println("GPIO 01 Value : " + pin.getState().getValue());
        });

    }

}
