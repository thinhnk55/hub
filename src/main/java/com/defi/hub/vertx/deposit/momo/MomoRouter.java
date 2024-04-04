package com.defi.hub.vertx.deposit.momo;

import com.defi.common.SimpleResponse;
import com.defi.hub.deposit.momo.MomoManager;
import com.defi.hub.internal.HubClient;
import com.defi.hub.internal.HubClientManager;
import com.defi.util.string.StringUtil;
import com.google.gson.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class MomoRouter {
    public static void createTransaction(RoutingContext rc) {
        try {
            String client_name = rc.request().getParam("client");
            HubClient client = HubClientManager.instance().getClient(client_name);
            if (client == null) {
                rc.response().end(SimpleResponse.createResponse(2).toString());
                return;
            }
            String access_key = rc.request().getParam("access_key");
            if (!client.access_key.equalsIgnoreCase(access_key)) {
                rc.response().end(SimpleResponse.createResponse(2).toString());
                return;
            }
            String client_transaction_id = rc.request().getParam("request_id");
            String client_callback_url = rc.request().getParam("callback_url");
            int request_amount = Integer.parseInt(rc.request().getParam("amount"));
            String sign = rc.request().getParam("sign");
            String signData = StringUtil.md5(new StringBuilder(client_transaction_id)
                    .append(request_amount)
                    .append(client_callback_url)
                    .append(client.secret_key)
                    .toString());
            if (!sign.equals(signData)) {
                rc.response().end(SimpleResponse.createResponse(2).toString());
                return;
            }
            JsonObject response = MomoManager.instance().createTransaction(
                    client_name, client_transaction_id, client_callback_url, request_amount
            );
            if (SimpleResponse.isSuccess(response)) {
                rc.response().end(response.toString());
            } else {
                rc.response().end(response.toString());
            }
        } catch (Exception e) {
            rc.response().end(SimpleResponse.createResponse(1).toString());
            e.printStackTrace();
        }
    }
}
