package com.defi.hub.internal;

import com.google.gson.JsonObject;

public class HubClient {
    public String client;
    public String secret_key;
    public String access_key;
    public long create_time;

    public HubClient(JsonObject json) {
        this.client = json.get("client").getAsString();
        this.secret_key = json.get("secret_key").getAsString();
        this.access_key = json.get("access_key").getAsString();
        this.create_time = json.get("create_time").getAsLong();
    }
}
