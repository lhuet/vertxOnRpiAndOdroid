package fr.lhuet.devoxx;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by lhuet on 17/03/16.
 */
public class Ds18b20Verticle extends AbstractVerticle {

    private static Logger logger = LoggerFactory.getLogger(Ds18b20Verticle.class.getName());

    private String w1Sensor1File = "/sys/bus/w1/devices/28-0000062d006a/w1_slave";
    private String w1Sensor2File = "/sys/bus/w1/devices/28-0000062d1425/w1_slave";
    private static final int IDSENSOR1 = 0;
    private static final int IDSENSOR2 = 1;
    private float tempSensor1;
    private float tempSensor2;

    @Override
    public void start(Future<Void> fut) throws Exception {

        // Handler to serve the sensors values on the vertx event loop
        MessageConsumer<String> dhwConsumer = vertx.eventBus().consumer("sensor-temp");
        dhwConsumer.handler(event -> {
            switch (event.body()) {
                case "1":
                    event.reply(this.tempSensor1);
                    break;
                case "2":
                    event.reply(this.tempSensor2);
                    break;
                default:
                    event.reply(0);
            }
        });

        // Read the 2 temp sensors "immediately"
        readTemp(event -> {
            logger.debug("Temp sensor reading finished");
            fut.complete();
        });
        // .. and continue refreshing the 2 temp. sensors every minute
        vertx.setPeriodic(60000, event -> {
            readTemp(event1 -> logger.debug("Temp sensor reading finished"));
        });

    }

    private void readTemp(Handler<AsyncResult> resultHandler) {

        Future<Void> sensor1Fut = Future.future();
        Future<Void> sensor2Fut = Future.future();

        readW1temp(w1Sensor1File, IDSENSOR1, sensor1Fut.completer());
        readW1temp(w1Sensor2File, IDSENSOR2, sensor2Fut.completer());

        CompositeFuture.all(sensor1Fut, sensor2Fut).setHandler(event -> {
            resultHandler.handle(Future.succeededFuture());
        });

    }

    private void readW1temp(String file, int sensor, Handler<AsyncResult<Void>> resultHandler) {
        // w1 file content like :
        //    ce 02 4b 46 7f ff 02 10 0c : crc=0c YES
        //    ce 02 4b 46 7f ff 02 10 0c t=44875
        vertx.fileSystem().readFile(file, (AsyncResult<Buffer> res) -> {
            if (res.failed()) {
                // File read error
                resultHandler.handle(Future.failedFuture(res.cause()));
            } else {
                String content = res.result().toString();
                if (content.contains("YES")) {
                    // CRC ok -> extract Temp in deg C
                    String[] temp = content.split("t=");
                    switch (sensor) {
                        case IDSENSOR1:
                            this.tempSensor1 = Float.valueOf(temp[1]) / 1000;
                            logger.info("Refreshing DHW Temp sensor value : " + this.tempSensor1);
                            break;
                        case IDSENSOR2:
                            this.tempSensor2 = Float.valueOf(temp[1]) / 1000;
                            logger.info("Refreshing Buffer Temp sensor value : " + this.tempSensor2);
                            break;
                        default:
                            logger.error("readW1temp -> Bad sensor switch");
                            resultHandler.handle(Future.failedFuture("readW1temp -> Bad sensor switch"));
                    }
                    resultHandler.handle(Future.succeededFuture());
                } else {
                    resultHandler.handle(Future.failedFuture("(One Wire bus error) Bad CRC on file " + file));
                }
            }
        });
    }
}
