package fr.lhuet.devoxx;

import com.pi4j.component.temperature.TemperatureSensor;
import com.pi4j.io.w1.W1Master;
import com.pi4j.temperature.TemperatureScale;
import io.vertx.core.*;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lhuet on 17/03/16.
 */
public class Ds18b20Verticle extends AbstractVerticle {

    private static Logger log = LoggerFactory.getLogger(Ds18b20Verticle.class);

    private Map<Integer, Double> sensors = new HashMap();
    private TemperatureSensor sensor1;
    private TemperatureSensor sensor2;

    @Override
    public void start(Future<Void> fut) throws Exception {

        // Sensors
        sensors.put(1, null);
        sensors.put(2, null);

        // W1 Bus init
        W1Master w1Master = new W1Master();

        // Get DS18B20 Temp device objects
        for (TemperatureSensor device : w1Master.getDevices(TemperatureSensor.class)) {
            if (device.getName().contains("28-0000062d006a")) sensor1 = device;
            if (device.getName().contains("28-0000062d1425")) sensor2 = device;
        }

        // Read the 2 temp sensors "immediately"
        readTemp(event -> {
            log.info("Temp sensor reading finished");
            fut.complete();
        });
        // .. and continue refreshing the 2 temp. sensors every minute
        vertx.setPeriodic(2000, event -> {
            readTemp(event1 -> {
                log.info("Temp sensor reading finished");
                // JSON message construction
                JsonObject msg = new JsonObject();
                msg.put("sensor1", sensors.get(1));
                msg.put("sensor2", sensors.get(2));
                // Message publishing
                vertx.eventBus().publish("sensors-temp", msg);
            });
        });

    }

    private void readTemp(Handler<AsyncResult> resultHandler) {

        Future<Void> sensor1Fut = Future.future();
        Future<Void> sensor2Fut = Future.future();

        vertx.executeBlocking(future -> {
            Double temp = sensor1.getTemperature(TemperatureScale.CELSIUS);
            future.complete(temp);
        }, res -> {
            sensors.put(1, (Double) res.result());
            sensor1Fut.complete();
        });

        vertx.executeBlocking( fut -> {
            Double temp = sensor2.getTemperature(TemperatureScale.CELSIUS);
            fut.complete(temp);
        }, res -> {
            sensors.put(2, (Double) res.result());
            sensor2Fut.complete();
        });

        // Resolve readTemp Future only when values from the 2 sensors are obtained
        CompositeFuture.all(sensor1Fut, sensor2Fut).setHandler(event -> {
            resultHandler.handle(Future.succeededFuture());
        });
    }

}
