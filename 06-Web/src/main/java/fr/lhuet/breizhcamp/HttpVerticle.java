package fr.lhuet.breizhcamp;

import io.vertx.core.AbstractVerticle;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Created by lhuet on 17/03/16.
 */
public class HttpVerticle extends AbstractVerticle {


    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);

        router.get().path("/temp/:sensorId").handler(this::handlerGetTemp);
        router.put().path("/led").handler(this::handlerPutLed);

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8000, event -> {
                    System.out.println("HTTP Server started on port 8000");
                });

    }

    private void handlerGetTemp(RoutingContext ctx) {

        String sensorId = ctx.request().getParam("sensorId");

        vertx.eventBus().send("sensor-temp", sensorId, ar -> {
           if (ar.succeeded()) {
               ctx.response().end(ar.result().body().toString());
           } else {
               ctx.response().setStatusCode(500).end("Event bus temp error : " + ar.cause().getMessage());
           }
        });

    }

    private void handlerPutLed(RoutingContext ctx) {

        vertx.eventBus().send("toggle-led", "" , ar -> {
            System.out.println("test toggle led");
            if (ar.succeeded()) {
                ctx.response().end(ar.result().body().toString());
            } else {
                ctx.response().setStatusCode(500).end("Event bus temp error : " + ar.cause().getMessage());
            }
        });

    }

}
