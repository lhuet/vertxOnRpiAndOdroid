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

    private I2CLcdDisplay lcd;

    @Override
    public void start() throws Exception {

        // Get LCD Address from config
        int lcdAddress = config().getInteger("lcd.address", 0x27);

        // LCD Display with I2C PCF8574 chip
        lcd = new I2CLcdDisplay(4, 20, 1, lcdAddress, 3, 0, 1, 2, 7, 6, 5, 4);

        // switch on the light
        lcd.setBacklight(false);

        lcd.writeln(0, "Vert.x - Devoxx France", LCDTextAlignment.ALIGN_CENTER);

        vertx.eventBus().consumer("sensors-temp", msg -> {
            // Get the temp from Ds18b20Verticle and display the temp on LCD
            JsonObject message = (JsonObject) msg.body();
            Double sensor1 = message.getDouble("sensor1");
            Double sensor2 = message.getDouble("sensor2");

            lcd.writeln(2, "  Sensor 1 : " + sensor1 + "C", LCDTextAlignment.ALIGN_LEFT);
            lcd.writeln(3, "  Sensor 2 : " + sensor2 + "C", LCDTextAlignment.ALIGN_LEFT);
        });

    }

    @Override
    public void stop() throws Exception {
        // Switch on the light
        lcd.setBacklight(true);
        // Clean de display bedore stopping the verticle
        lcd.clear();
    }
}
