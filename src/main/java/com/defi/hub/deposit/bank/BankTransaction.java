package com.defi.hub.deposit.bank;

import com.google.gson.JsonObject;

public class BankTransaction  {
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
    public String bank_transaction_id;
    public String provider_bank_code;
    public String bank_code;
    public String bank_short_name;
    public String bank_full_name;
    public String bank_owner;
    public String bank_account;
    public String message;
    public int state;
    public int error;
    public String hub_callback_url;
    public JsonObject provider_callback_data;
    public long expired_time;
    public long create_time;
    public long update_time;


    public BankTransaction(JsonObject data) {
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
        this.bank_transaction_id = data.get("bank_transaction_id").getAsString();
        this.provider_bank_code = data.get("provider_bank_code").getAsString();
        this.bank_code = data.get("bank_code").getAsString();
        this.bank_short_name = data.get("bank_short_name").getAsString();
        this.bank_full_name = data.get("bank_full_name").getAsString();
        this.bank_owner = data.get("bank_owner").getAsString();
        this.bank_account = data.get("bank_account").getAsString();
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
        json.addProperty("bank_code", bank_code);
        json.addProperty("bank_owner", bank_owner);
        json.addProperty("bank_account", bank_account);
        json.addProperty("message", message);
        json.addProperty("expired_time", expired_time);
        return json;
    }
}
