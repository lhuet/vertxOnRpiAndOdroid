package fr.lhuet.devoxx;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioFactory;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.RaspiPin;
import io.vertx.core.AbstractVerticle;

/**
 * Created by lhuet on 14/03/16.
 */
public class LedVerticle extends AbstractVerticle {

    private GpioController gpio;
    private GpioPinDigitalOutput myLed;

    @Override
    public void start() throws Exception {

        gpio = GpioFactory.getInstance();
        myLed = gpio.provisionDigitalOutputPin(RaspiPin.GPIO_01);

        vertx.eventBus().consumer("toggle-led", event -> {
            myLed.toggle();
            event.reply(myLed.getState().isHigh());
        });


    }
}
