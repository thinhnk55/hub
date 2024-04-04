package com.defi.hub.deposit.momo;

import com.google.gson.JsonObject;

public class MomoTransaction {
    public String code;
    public String client;
    public String client_transaction_id;
    public String client_callback_url;
    public long request_amount;
    public long real_amount;
    public int client_callback_count;
    public JsonObject client_callback_response;
    public String provider;
    public JsonObject provider_transaction_response;
    public String provider_transaction_id;
    public String momo_transaction_id;
    public String name;
    public String phone;
    public String message;
    public int state;
    public int error;
    public String hub_callback_url;
    public JsonObject provider_callback_data;
    public long expired_time;
    public long create_time;
    public long update_time;

    public MomoTransaction(String clientName, String clientTransactionId, String clientCallbackUrl, int requestAmount) {
        this.client = clientName;
        this.client_transaction_id = clientTransactionId;
        this.client_callback_url = clientCallbackUrl;
        this.request_amount = requestAmount;
    }

    public MomoTransaction(JsonObject data) {
        this.code = data.get("code").getAsString();
        this.client = data.get("client").getAsString();
        this.client_transaction_id = data.get("client_transaction_id").getAsString();
        this.client_callback_url = data.get("client_callback_url").getAsString();
        this.request_amount = data.get("request_amount").getAsLong();
        this.real_amount = data.get("real_amount").getAsLong();
        this.client_callback_count = data.get("client_callback_count").getAsInt();
        this.client_callback_response = data.getAsJsonObject("client_callback_response");
        this.provider = data.get("provider").getAsString();
        this.provider_transaction_response = data.getAsJsonObject("provider_transaction_response");
        this.provider_transaction_id = data.get("provider_transaction_id").getAsString();
        this.momo_transaction_id = data.get("momo_transaction_id").getAsString();
        this.name = data.get("name").getAsString();
        this.phone = data.get("phone").getAsString();
        this.message = data.get("message").getAsString();
        this.state = data.get("state").getAsInt();
        this.error = data.get("error").getAsInt();
        this.hub_callback_url = data.get("hub_callback_url").getAsString();
        this.provider_callback_data = data.getAsJsonObject("provider_callback_data");
        this.expired_time = data.get("expired_time").getAsLong();
        this.create_time = data.get("create_time").getAsLong();
        this.update_time = data.get("update_time").getAsLong();
    }

    public JsonObject toCreateSuccessJson() {
        JsonObject json = new JsonObject();
        json.addProperty("name", name);
        json.addProperty("phone", phone);
        json.addProperty("message", message);
        json.addProperty("expired_time", expired_time);
        return json;
    }

    public void callback(JsonObject json) {

    }
}
