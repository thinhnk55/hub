package com.defi.hub.vertx.deposit.cocopay;

import com.defi.common.SimpleResponse;
import com.defi.hub.deposit.bank.BankManager;
import com.defi.hub.deposit.momo.MomoManager;
import com.defi.util.log.DebugLogger;
import com.google.gson.JsonObject;
import io.vertx.ext.web.RoutingContext;

public class CocoPayRouter {
    public static void momoCallback(RoutingContext rc) {
        try {
            String uri = rc.request().uri();
            DebugLogger.logger.error(uri);
            String request_id = rc.request().getParam("request_id");
            String trans_id = rc.request().getParam("trans_id", "");
            String status = rc.request().getParam("status");
            String amount = rc.request().getParam("amount");
            String message = rc.request().getParam("message");
            String signature = rc.request().getParam("signature");
            JsonObject json = new JsonObject();
            json.addProperty("uri", uri);
            json.addProperty("request_id", request_id);
            json.addProperty("trans_id", trans_id);
            json.addProperty("status", status);
            json.addProperty("amount", amount);
            json.addProperty("message", message);
            json.addProperty("signature", signature);
            String provider = "cocobay";
            boolean verify = MomoManager.instance().verifyCallback(provider, json);
            if (!verify) {
                rc.response().setStatusCode(401).end(SimpleResponse.createResponse(2).toString());
            }
            JsonObject response = MomoManager.instance().callback(request_id, "provider", json);
            if(SimpleResponse.isSuccess(response)) {
                rc.response().end(response.toString());
            }else{
                rc.response().setStatusCode(500).end(response.toString());
            }
        } catch (Exception e) {
            rc.response().setStatusCode(500).end(SimpleResponse.createResponse(1).toString());
            e.printStackTrace();
        }
    }

    public static void bankCallback(RoutingContext rc) {
        try {
            String uri = rc.request().uri();
            DebugLogger.logger.error(uri);
            String request_id = rc.request().getParam("request_id");
            String trans_id = rc.request().getParam("trans_id", "");
            String bank = rc.request().getParam("bank");
            String status = rc.request().getParam("status");
            String amount = rc.request().getParam("amount");
            String message = rc.request().getParam("message");
            String signature = rc.request().getParam("signature");
            JsonObject json = new JsonObject();
            json.addProperty("uri", uri);
            json.addProperty("request_id", request_id);
            json.addProperty("trans_id", trans_id);
            json.addProperty("bank", bank);
            json.addProperty("status", status);
            json.addProperty("amount", amount);
            json.addProperty("message", message);
            json.addProperty("signature", signature);
            String provider = "cocobay";
            boolean verify = BankManager.instance().verifyCallback(provider, json);
            if (!verify) {
                rc.response().setStatusCode(401).end(SimpleResponse.createResponse(2).toString());
            }
            JsonObject response = BankManager.instance().callback(request_id, "provider", json);
            if(SimpleResponse.isSuccess(response)) {
                rc.response().end(response.toString());
            }else{
                rc.response().setStatusCode(500).end(response.toString());
            }
        } catch (Exception e) {
            rc.response().setStatusCode(500).end(SimpleResponse.createResponse(1).toString());
            e.printStackTrace();
        }
    }
}
