package com.defi.hub.deposit.telco;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class TelcoTransaction {
    public long id;
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
    public String card_type;
    public String card_seri;
    public String card_code;
    public int state;
    public int error;
    public String hub_callback_url;
    public JsonObject provider_callback_data;
    public long create_time;
    public long update_time;
    public long expired_time;

    public TelcoTransaction(JsonObject data) {
        this.id = data.get("id").getAsLong();
        this.client = data.get("client").getAsString();
        this.client_transaction_id = data.get("client_transaction_id").getAsString();
        this.client_callback_url = data.get("client_callback_url").getAsString();
        this.request_amount = data.get("request_amount").getAsLong();
        this.real_amount = data.get("real_amount").getAsLong();
        this.client_callback_count = data.get("client_callback_count").getAsInt();
        this.client_callback_response = data.getAsJsonObject("client_callback_response");
        this.provider = data.get("provider").getAsString();
        this.provider_transaction_response = data.getAsJsonObject("provider_transaction_response");
        this.provider_transaction_id = data.get("provider_transaction_id").isJsonNull()? null : data.get("provider_transaction_id").getAsString();
        this.card_type = data.get("card_type").getAsString();
        this.card_seri = data.get("card_seri").getAsString();
        this.card_code = data.get("card_code").getAsString();
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
        json.addProperty("request_id", client_transaction_id);
        json.addProperty("expired_time", expired_time);
        return json;
    }
}
