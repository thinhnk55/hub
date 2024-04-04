package com.defi.hub.deposit.momo.provider;

import com.defi.common.SimpleResponse;
import com.defi.hub.deposit.momo.MomoTransaction;
import com.defi.hub.deposit.momo.service.MomoTransactionConstant;
import com.defi.util.log.DebugLogger;
import com.defi.util.network.OkHttpUtil;
import com.defi.util.string.StringUtil;
import com.google.gson.JsonObject;

public class MomoCocoPayProvider implements IMomoProvider {
    String username;
    String password = "Abc@123";
    String api_key;
    String provider_submit_url;
    String hub_callback_url;

    public MomoCocoPayProvider(JsonObject json){
        this.username = json.get("username").getAsString();
        this.api_key = json.get("api_key").getAsString();
        this.provider_submit_url = json.get("provider_submit_url").getAsString();
        this.hub_callback_url = json.get("hub_callback_url").getAsString();
    }

    public JsonObject requestPayment(String request_id, long amount, int custom_content){
        String url = new StringBuilder(provider_submit_url)
                .append("?api_key=").append(api_key)
                .append("&request_id=").append(request_id)
                .append("&amount=").append(amount)
                .append("&url_callback=").append(hub_callback_url)
                .append("&custom_content=").append(custom_content)
                .toString();
        JsonObject response = OkHttpUtil.get(url);
//        JsonObject response = sample(request_id, amount);
        DebugLogger.logger.info("url: {}\nresponse: {}", url, response);
        return response;
    }

    private JsonObject sample(String request_id, long amount) {
        JsonObject json = new JsonObject();
        json.addProperty("status", 1);
        json.addProperty("phone", "08860493690");
        json.addProperty("name", "TRANXUANTHANG");
        json.addProperty("content", request_id);
        json.addProperty("amount", amount);
        return json;
    }

    @Override
    public JsonObject send(MomoTransaction transaction) {
        JsonObject response = requestPayment(transaction.code, transaction.request_amount, 0);
        int status = response.get("status").getAsInt();
        if(status == 1){
            transaction.provider = username;
            transaction.hub_callback_url = hub_callback_url;
            transaction.state = MomoTransactionConstant.STATE_PROVIDER_CREATED;
            transaction.phone = response.get("phone").getAsString();
            transaction.name = response.get("name").getAsString();
            transaction.message = response.get("content").getAsString();
            transaction.request_amount = response.get("amount").getAsLong();
            transaction.provider_transaction_response = response;
            transaction.update_time = System.currentTimeMillis();
            return SimpleResponse.createResponse(0);
        }else{
            transaction.provider = username;
            transaction.hub_callback_url = hub_callback_url;
            transaction.error = MomoTransactionConstant.ERROR_TRANSACTION_FAILED;
            transaction.provider_transaction_response = response;
            transaction.update_time = System.currentTimeMillis();
            return SimpleResponse.createResponse(10);
        }
    }

    @Override
    public boolean verifyCallback(JsonObject json) {
        String request_id = json.get("request_id").getAsString();
        String message = json.get("message").getAsString();
        String amount = json.get("amount").getAsString();
        String status = json.get("status").getAsString();
        String signature = json.get("signature").getAsString();
        String signData = new StringBuilder()
                .append(request_id)
                .append(message)
                .append(amount)
                .append(status)
                .append(api_key)
                .toString();
        String sign = StringUtil.md5(signData);
        if(sign.equals(signature)){
            return true;
        }
        return false;
    }

    @Override
    public void callback(MomoTransaction transaction, JsonObject json) {
        String trans_id = json.get("trans_id").getAsString();
        String message = json.get("message").getAsString();
        long amount = json.get("amount").getAsLong();
        int status = json.get("status").getAsInt();
        transaction.real_amount = amount;
        transaction.message = message;
        transaction.state = MomoTransactionConstant.STATE_PROVIDER_CALLBACKED;
        transaction.provider_callback_data = json;
        if(!trans_id.equals("")) {
            transaction.provider_transaction_id = trans_id;
        }
        transaction.create_time = System.currentTimeMillis();
        if(status == -1){
            transaction.error = MomoTransactionConstant.ERROR_TRANSACTION_FAILED;
        }
    }
}
