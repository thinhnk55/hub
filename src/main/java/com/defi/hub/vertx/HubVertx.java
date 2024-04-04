package com.defi.hub.vertx;

import com.defi.util.json.GsonUtil;
import com.google.gson.JsonObject;
import io.vertx.core.Vertx;

public class HubVertx {
    public static Vertx vertx;
    private static HubVertx ins = null;
    public static HubVertx instance() {
        if (ins == null) {
            ins = new HubVertx();
        }
        return ins;
    }
    private HubVertx(){

    }

    public void init(String configFile) {
        JsonObject config = GsonUtil.getJsonObject(configFile);
        this.name = config.get("name").getAsString();
        this.url_prefix = config.get("url_prefix").getAsString();
        this.http_port= config.get("http_port").getAsInt();

    }

    public String name;
    public int http_port;
    public String url_prefix;

    public String getPath(String path) {
        String fullPath = new StringBuilder(url_prefix).append(path).toString();
        return fullPath;
    }
}
