package fr.lhuet.devoxx;

import com.pi4j.io.gpio.*;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by lhuet on 14/03/16.
 */
public class VertxHelloWorld2  {

    private static Logger log = LoggerFactory.getLogger(VertxHelloWorld2.class);

    public static void main(String[] args) throws PlatformAlreadyAssignedException {

        GpioPinDigitalOutput led = initGpio();

        // Vertx event timer
        Vertx.vertx().setPeriodic(1000, l -> {
            // Blink led every seconds
            led.toggle();
            log.info("Led status : " + led.getState());
        });

    }

    private static GpioPinDigitalOutput initGpio() throws PlatformAlreadyAssignedException {

        // Default platform is Raspberry -> Explicit assign the target platform
        PlatformManager.setPlatform(Platform.ODROID);

        // Get GpioController (with wiringPu Setup)
        GpioController gpio = GpioFactory.getInstance();
        // Configure GPIO 01 as Output
        GpioPinDigitalOutput led = gpio.provisionDigitalOutputPin(OdroidC1Pin.GPIO_01, PinState.LOW);

        // Force GPIO to LOW on shutdown
        led.setShutdownOptions(true, PinState.LOW);

        return led;

    }
}
