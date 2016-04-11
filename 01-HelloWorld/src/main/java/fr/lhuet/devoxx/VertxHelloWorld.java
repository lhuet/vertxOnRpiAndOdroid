package fr.lhuet.devoxx;

import com.pi4j.platform.Platform;
import com.pi4j.platform.PlatformAlreadyAssignedException;
import com.pi4j.platform.PlatformManager;
import com.pi4j.wiringpi.Gpio;
import com.pi4j.wiringpi.GpioUtil;
import io.vertx.core.Vertx;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by lhuet on 14/03/16.
 */
public class VertxHelloWorld {

    private static Logger log = LoggerFactory.getLogger(VertxHelloWorld.class);

    public static void main(String[] args) throws PlatformAlreadyAssignedException {

        // Default platform is Raspberry -> Explicit assign the target platform
        // TODO : Use PI4J_PLATFORM env variable ??
        PlatformManager.setPlatform(Platform.ODROID);

        // PI4J Init
        if (Gpio.wiringPiSetup() == -1) {
            log.error(" ==>> GPIO SETUP FAILED");
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
                log.info("Switch off ...");
                Gpio.digitalWrite(1, Gpio.LOW);
            }
            else {
                log.info("Switch on ...");
                Gpio.digitalWrite(1, Gpio.HIGH);
            }
        });

    }

}
