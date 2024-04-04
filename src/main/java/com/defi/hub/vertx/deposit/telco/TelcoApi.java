package com.defi.hub.vertx.deposit.telco;

import com.defi.hub.vertx.HubVertx;
import io.vertx.ext.web.Router;

public class TelcoApi {
    public static void configAPI(Router router) {
        privateApi(router);
    }

    private static void privateApi(Router router) {
        router.get(HubVertx.instance().getPath("/telco/create"))
                .handler(TelcoRouter::createTransaction);
    }
}
