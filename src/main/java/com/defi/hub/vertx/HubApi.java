package com.defi.hub.vertx;

import com.defi.hub.vertx.deposit.bank.BankApi;
import com.defi.hub.vertx.deposit.cocopay.CocoPayApi;
import com.defi.hub.vertx.deposit.momo.MomoApi;
import io.vertx.ext.web.Router;

public class HubApi {
    public static void configAPI(Router router) {
        MomoApi.configAPI(router);
        BankApi.configAPI(router);
        CocoPayApi.configAPI(router);
    }
}
