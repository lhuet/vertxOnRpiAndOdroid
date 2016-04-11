package fr.lhuet.devoxx;

import com.pi4j.io.gpio.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by lhuet on 14/03/16.
 */
public class LedVerticle extends AbstractVerticle {

    private Logger log = LoggerFactory.getLogger(LedVerticle.class);

    private GpioController gpio;
    private GpioPinDigitalOutput myLed;

    @Override
    public void start() throws Exception {

        gpio = GpioFactory.getInstance();
        myLed = gpio.provisionDigitalOutputPin(OdroidC1Pin.GPIO_01);
        // Force GPIO to LOW on shutdown
        myLed.setShutdownOptions(true, PinState.LOW);
        log.info("Led configured ...");
        myLed.low();

        vertx.eventBus().consumer("myButton", event -> {
            myLed.toggle();
            log.info("Led toggle -> " + myLed.getState());
        });

    }


    @Override
    public void stop() throws Exception {
        System.out.println("Stop LedVerticle ...");
        gpio.shutdown();
    }
}
