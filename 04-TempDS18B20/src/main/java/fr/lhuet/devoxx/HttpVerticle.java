package fr.lhuet.devoxx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;

/**
 * Created by lhuet on 17/03/16.
 */
public class HttpVerticle extends AbstractVerticle {

    private Logger log = LoggerFactory.getLogger(HttpVerticle.class);

    @Override
    public void start() throws Exception {

        vertx.createHttpServer()
             .requestHandler(req -> {
                 String id = req.getParam("id");
                 log.info("HTTP Request / sensor " + id);
                 vertx.eventBus().send("sensor-temp", id, ar -> {
                     if (ar.succeeded()) {
                         req.response().end(ar.result().body().toString());
                     }
                     else {
                         req.response().setStatusCode(500).end(ar.cause().getMessage());
                     }
                 });
             })
             .listen(8000, event -> {
                 log.info("HTTP Server started on port 8000");
             });
    }
}
