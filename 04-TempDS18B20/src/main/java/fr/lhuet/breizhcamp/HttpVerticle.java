package fr.lhuet.breizhcamp;

import io.vertx.core.AbstractVerticle;

/**
 * Created by lhuet on 17/03/16.
 */
public class HttpVerticle extends AbstractVerticle {


    @Override
    public void start() throws Exception {

        vertx.createHttpServer()
                .requestHandler(req -> {
                    vertx.eventBus().send("sensor-temp", "1", ar -> {
                        if (ar.succeeded()) {
                            req.response().end(ar.result().body().toString());
                        }
                    });
                })
                .listen(8000, event -> {
                    System.out.println("HTTP Server started on port 8000");
                });

    }
}
