package com.defi.hub.vertx.deposit.bank;

import com.defi.hub.vertx.HubVertx;
import io.vertx.ext.web.Router;

public class BankApi {
    public static void configAPI(Router router) {
        privateApi(router);
    }

    private static void privateApi(Router router) {
        router.get(HubVertx.instance().getPath("/bank/support"))
                .handler(BankRouter::listSupportBank);
        router.get(HubVertx.instance().getPath("/bank/create"))
                .handler(BankRouter::createTransaction);
    }
}
