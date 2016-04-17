package fr.lhuet.devoxx;

import com.pi4j.component.lcd.LCD;
import com.pi4j.component.lcd.LCDTextAlignment;
import com.pi4j.component.lcd.impl.I2CLcdDisplay;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.json.JsonObject;

/**
 * Created by lhuet on 17/03/16.
 */
public class LCDverticle extends AbstractVerticle {

    @Override
    public void start() throws Exception {

        I2CLcdDisplay lcd = new I2CLcdDisplay(4, 20, 1, 0x27, 3, 0, 1, 2, 7, 6, 5, 4);

        lcd.writeln(0, "Demo Devoxx France", LCDTextAlignment.ALIGN_CENTER);

        vertx.eventBus().consumer("sensors-temp", msg -> {
            JsonObject message = (JsonObject) msg.body();
            Double sensor1 = message.getDouble("sensor1");
            Double sensor2 = message.getDouble("sensor2");

            lcd.writeln(2, "  Sensor 1 : " + sensor1 + "C", LCDTextAlignment.ALIGN_LEFT);
            lcd.writeln(2, "  Sensor 2 : " + sensor1 + "C", LCDTextAlignment.ALIGN_LEFT);
        });

    }

}
