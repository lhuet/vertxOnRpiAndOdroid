package fr.lhuet.devoxx;

import com.pi4j.io.gpio.*;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by lhuet on 14/03/16.
 */
public class ButtonVerticle extends AbstractVerticle {

    private static Logger log = LoggerFactory.getLogger(ButtonVerticle.class);

    private GpioController gpio;
    private GpioPinDigitalInput myButton;
    private int buttonState;

    @Override
    public void start() throws Exception {

        gpio = GpioFactory.getInstance();

        myButton = gpio.provisionDigitalInputPin(OdroidC1Pin.GPIO_04, PinPullResistance.PULL_UP);
        log.info("Button configured ...");
        buttonState = myButton.getState().getValue();

        vertx.setPeriodic(100, l -> {
            if (buttonState != myButton.getState().getValue()) {
                log.info("Button state changed -> " + myButton.getState());
                vertx.eventBus().send("myButton", buttonState);
                buttonState = myButton.getState().getValue();
            }
        });

    }


    @Override
    public void stop() throws Exception {
        System.out.println("Stop ButtonVerticle ...");
        gpio.shutdown();
    }
}
