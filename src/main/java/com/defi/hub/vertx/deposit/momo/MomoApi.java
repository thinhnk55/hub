package com.defi.hub.vertx.deposit.momo;

import com.defi.hub.vertx.HubVertx;
import io.vertx.ext.web.Router;

public class MomoApi {
    public static void configAPI(Router router) {
        privateApi(router);
    }

    private static void privateApi(Router router) {
        router.get(HubVertx.instance().getPath("/momo/create"))
                .handler(MomoRouter::createTransaction);
    }
}
