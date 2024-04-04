package com.defi.hub.vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.CorsHandler;

import java.util.HashSet;
import java.util.Set;

public class HubVerticle extends AbstractVerticle {
    private HttpServer httpServer;


    @Override
    public void start() {
        Router router = Router.router(vertx);
        crossAccessControl(router);
        router.get(HubVertx.instance().getPath("/test")).handler(this::test);
        HubApi.configAPI(router);
        httpServer = vertx.createHttpServer()
                .requestHandler(router)
                .listen(HubVertx.instance().http_port).result();
    }

    @Override
    public void stop() {
        if(httpServer != null) httpServer.close();
    }
    public void crossAccessControl(Router router) {
        Set<String> allowedHeaders = new HashSet<>();
        allowedHeaders.add("*");
        Set<HttpMethod> allowedMethods = new HashSet<>();
        allowedMethods.add(HttpMethod.GET);
        allowedMethods.add(HttpMethod.POST);
        allowedMethods.add(HttpMethod.OPTIONS);
        router.route().handler(CorsHandler.create()
                .allowedHeaders(allowedHeaders)
                .allowedMethods(allowedMethods)
                .allowCredentials(true));
    }
    private void test(RoutingContext rc) {
        rc.response().end("OK");
    }
}
