package com.defi.common;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class SimpleResponse {
    public static JsonObject createResponse(int error, JsonElement data) {
        JsonObject json = new JsonObject();
        json.addProperty("e", error);
        json.add("d", data);
        return json;
    }

    public static JsonObject createResponse(int error) {
        JsonObject json = new JsonObject();
        json.addProperty("e", error);
        return json;
    }

    public static boolean isSuccess(JsonObject response) {
        return response.get("e").getAsInt() == 0;
    }
}
