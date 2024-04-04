package com.defi.hub.vertx.deposit.cocopay;

import com.defi.hub.vertx.HubVertx;
import io.vertx.ext.web.Router;

public class CocoPayApi {
    public static void configAPI(Router router) {
        privateApi(router);
    }
    private static void privateApi(Router router) {
        router.get(HubVertx.instance().getPath("/cocobay"))
                .handler(CocoPayRouter::momoCallback);
        router.get(HubVertx.instance().getPath("/cocobay/momo"))
                .handler(CocoPayRouter::momoCallback);
        router.get(HubVertx.instance().getPath("/cocobay/bank"))
                .handler(CocoPayRouter::bankCallback);
    }
}
