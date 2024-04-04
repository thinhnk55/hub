package com.defi.hub.deposit.telco.provider;

import com.defi.common.SimpleResponse;
import com.defi.hub.deposit.telco.TelcoTransaction;
import com.defi.util.log.DebugLogger;
import com.defi.util.network.OkHttpUtil;
import com.defi.util.string.StringUtil;
import com.google.gson.JsonObject;

import static com.defi.hub.deposit.telco.service.TelcoTransactionConstant.*;

public class TelcoCocoPayProvider implements ITelcoProvider{
    String username;
    String password = "Abc@123";
    String api_key;
    String provider_submit_url;
    String hub_callback_url;

    public TelcoCocoPayProvider(JsonObject json){
        this.username = json.get("username").getAsString();
        this.api_key = json.get("api_key").getAsString();
        this.provider_submit_url = json.get("provider_submit_url").getAsString();
        this.hub_callback_url = json.get("hub_callback_url").getAsString();
    }

    public JsonObject requestPayment(long request_id, String card_type,
                                     String card_seri, String card_code, long request_amount){
        card_type = cardTypeHubToProvider(card_type);
        String signature = StringUtil.md5(
          new StringBuilder()
                  .append(api_key).append(request_amount)
                  .append(card_code).append(card_seri)
                  .toString()
        );
        String url = new StringBuilder(provider_submit_url)
                .append("?api_key=").append(api_key)
                .append("&request_id=").append(request_id)
                .append("&card_type=").append(card_type)
                .append("&card_seri=").append(card_seri)
                .append("&card_code=").append(card_code)
                .append("&card_amount=").append(request_amount)
                .append("&signature=").append(signature)
                .toString();
        JsonObject response = OkHttpUtil.get(url);
        DebugLogger.logger.info("url: {}\nresponse: {}", url, response);
        return response;
    }
    @Override
    public JsonObject send(TelcoTransaction transaction) {
        JsonObject response = requestPayment(transaction.id, transaction.card_type, transaction.card_seri,
                transaction.card_code, transaction.request_amount);
        int status = response.get("status").getAsInt();
        if(status == 0){
            transaction.provider = username;
            transaction.hub_callback_url = hub_callback_url;
            transaction.state = STATE_PROVIDER_CREATED;
            transaction.request_amount = response.get("amount").getAsLong();
            transaction.provider_transaction_id = response.get("trans_code").getAsString();
            transaction.provider_transaction_response = response;
            transaction.update_time = System.currentTimeMillis();
            return SimpleResponse.createResponse(0);
        }else{
            transaction.provider = username;
            transaction.hub_callback_url = hub_callback_url;
            transaction.error = ERROR_TRANSACTION_FAILED;
            transaction.provider_transaction_response = response;
            transaction.update_time = System.currentTimeMillis();
            return SimpleResponse.createResponse(10);
        }
    }

    public String cardTypeHubToProvider(String card_type){
        if(card_type.equals(TELCO_VIETTEL)){
            return "VT";
        }
        if(card_type.equals(TELCO_VINAPHONE)){
            return "Vina";
        }
        if(card_type.equals(TELCO_MOBIFONE)){
            return "Mobi";
        }
        return "VNM";
    }
    public String cardTypeProviderToHub(String card_type){
        if(card_type.equals("VT")){
            return TELCO_VIETTEL;
        }
        if(card_type.equals("Vina")){
            return TELCO_VINAPHONE;
        }
        if(card_type.equals("Mobi")){
            return TELCO_MOBIFONE;
        }
        return "VNM";
    }

    @Override
    public boolean verifyCallback(JsonObject json) {
        String request_id = json.get("request_id").getAsString();
        String card_code = json.get("card_code").getAsString();
        String signature = json.get("signature").getAsString();
        String signData = new StringBuilder()
                .append(api_key)
                .append(request_id)
                .append(card_code)
                .toString();
        String sign = StringUtil.md5(signData);
        if(sign.equals(signature)){
            return true;
        }
        return false;
    }

    @Override
    public void callback(TelcoTransaction transaction, JsonObject json) {
        long real_amount = json.get("real_amount").getAsLong();
        int status = json.get("status").getAsInt();
        transaction.real_amount = real_amount;
        transaction.state = STATE_PROVIDER_CALLBACKED;
        transaction.provider_callback_data = json;
        transaction.create_time = System.currentTimeMillis();
        if(status != 0){
            transaction.error = ERROR_TRANSACTION_FAILED;
        }
    }
}
