package fr.lhuet.breizhcamp;

import com.pi4j.io.gpio.*;
import com.pi4j.io.gpio.event.GpioPinDigitalStateChangeEvent;
import com.pi4j.io.gpio.event.GpioPinListenerDigital;
import com.pi4j.wiringpi.*;
import io.vertx.core.AbstractVerticle;

/**
 * Created by lhuet on 14/03/16.
 */
public class ButtonVerticle extends AbstractVerticle {

    private GpioController gpio;
    private GpioPinDigitalInput myButton;
    private int buttonState;

    @Override
    public void start() throws Exception {

        gpio = GpioFactory.getInstance();

        myButton = gpio.provisionDigitalInputPin(RaspiPin.GPIO_04, PinPullResistance.PULL_UP);
        System.out.println("Button configured ...");
        buttonState = myButton.getState().getValue();

        vertx.setPeriodic(200, l -> {
            if (buttonState != myButton.getState().getValue()) {
                vertx.eventBus().send("myButton", buttonState);
                buttonState = myButton.getState().getValue();
            }
        });


    }

}
