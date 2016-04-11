package fr.lhuet.devoxx;

import com.pi4j.io.gpio.*;
import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by lhuet on 14/03/16.
 */
public class VertxVerticleHelloWorld extends AbstractVerticle {

    private static Logger log = LoggerFactory.getLogger(VertxVerticleHelloWorld.class);

    private GpioPinDigitalOutput led;

    public static void main(String[] args) {
        // Verticle deployment
        Vertx.vertx().deployVerticle(VertxVerticleHelloWorld.class.getName());
    }


    @Override
    public void start() throws Exception {

        log.info("Starting Vertx Verticle ...");

        initGPIO();

        vertx.setPeriodic(1000, l -> {
            // Blink led every seconds
            led.toggle();
            log.info("Led status : " + led.getState());

        });
    }

    @Override
    public void stop() throws Exception {
        log.info("Stopping Vertx Verticle ...");
        GpioController gpio = GpioFactory.getInstance();
        gpio.shutdown();
    }

    private void initGPIO() throws PlatformAlreadyAssignedException {
        // Default platform is Raspberry -> Explicit assign the target platform
        PlatformManager.setPlatform(Platform.ODROID);

        // Configure GPIO 01 as Output
        GpioController gpio = GpioFactory.getInstance();
        led = gpio.provisionDigitalOutputPin(OdroidC1Pin.GPIO_01, PinState.LOW);

        // Force GPIO to LOW on shutdown
        led.setShutdownOptions(true, PinState.LOW);

    }

}
