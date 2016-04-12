package fr.lhuet.devoxx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.StaticHandler;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

/**
 * Created by lhuet on 17/03/16.
 */
public class HttpVerticle extends AbstractVerticle {

    private static Logger log = LoggerFactory.getLogger(HttpVerticle.class);

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);

        router.get().path("/temp/:sensorId").handler(this::handlerGetTemp);
        router.put().path("/led").handler(this::handlerPutLed);

        // Allow outbound traffic to the vtoons addresses
        BridgeOptions options = new BridgeOptions()
                .addInboundPermitted(new PermittedOptions().setAddress("sensor-temp"))
                // all outbound messages are permitted
                .addOutboundPermitted(new PermittedOptions());

        router.route("/eventbus/*").handler(SockJSHandler.create(vertx).bridge(options));

        router.route().handler(StaticHandler.create());

        vertx.createHttpServer()
                .requestHandler(router::accept)
                .listen(8000, event -> {
                    log.info("HTTP Server started on port 8000");
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
