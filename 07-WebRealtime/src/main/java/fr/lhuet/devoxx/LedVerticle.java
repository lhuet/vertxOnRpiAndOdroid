package fr.lhuet.devoxx;

import com.pi4j.io.gpio.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by lhuet on 14/03/16.
 */
public class LedVerticle extends AbstractVerticle {

    private Logger log = LoggerFactory.getLogger(LedVerticle.class);

    private GpioController gpio;

    @Override
    public void start() throws Exception {

        gpio = GpioFactory.getInstance();
        GpioPinDigitalOutput myLed = gpio.provisionDigitalOutputPin(OdroidC1Pin.GPIO_01);

        vertx.eventBus().consumer("toggle-led", event -> {
            log.info("Led toggle -> " + myLed.getState());
            myLed.toggle();
            event.reply(myLed.getState().isHigh());
        });
    }

    @Override
    public void stop() throws Exception {
        System.out.println("Stop LedVerticle ...");
        gpio.shutdown();
    }
}
