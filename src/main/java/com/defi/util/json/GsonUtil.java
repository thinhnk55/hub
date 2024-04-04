package com.defi.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.defi.util.file.FileUtil;

import java.util.HashSet;
import java.util.Set;

public class GsonUtil {
    public static Gson gson = new GsonBuilder()
            .disableHtmlEscaping()
            .create();
    public static Gson beautyGson = new GsonBuilder()
            .disableHtmlEscaping()
            .setPrettyPrinting()
            .create();
    public static JsonObject getJsonObject(String filePath) {
        String data = FileUtil.getString(filePath);
        return GsonUtil.toJsonObject(data);
    }

    public static JsonArray getJsonArray(String filePath){
        String data = FileUtil.getString(filePath);
        return GsonUtil.toJsonArray(data);
    }

    public static JsonObject toJsonObject(String data) {
        try {
            return gson.fromJson(data, JsonObject.class);
        }catch (Exception e){
            return null;
        }
    }

    public static JsonArray toJsonArray(String data) {
        try {
            return gson.fromJson(data, JsonArray.class);
        }catch (Exception e){
            return null;
        }
    }
    public static Set<String> toSet(JsonArray array) {
        Set<String> set = new HashSet<>();
        for(int i = 0; i < array.size(); i++){
            set.add(array.get(i).toString());
        }
        return set;
    }
}
