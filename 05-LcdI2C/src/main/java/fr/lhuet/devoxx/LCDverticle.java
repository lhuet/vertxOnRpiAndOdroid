package fr.lhuet.devoxx;

import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.LCDTextAlignment;
import com.pi4j.component.lcd.impl.I2CLcdDisplay;
import io.vertx.core.AbstractVerticle;

/**
 * Created by lhuet on 17/03/16.
 */
public class LCDverticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        I2CLcdDisplay lcd = new I2CLcdDisplay(4, 20, 1, 0x27, 3, 0, 1, 2, 7, 6, 5, 4);

        lcd.writeln(1, "Demo Devoxx France", LCDTextAlignment.ALIGN_CENTER);

        vertx.setPeriodic(1000, l-> {
            lcd.setBacklight(lcd.isBacklight());
        });
    }
}
