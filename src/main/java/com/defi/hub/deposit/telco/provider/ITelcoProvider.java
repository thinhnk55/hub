package com.defi.hub.deposit.telco.provider;

import com.defi.hub.deposit.telco.TelcoTransaction;
import com.google.gson.JsonObject;

public interface ITelcoProvider {
    JsonObject send(TelcoTransaction transaction);

    boolean verifyCallback(JsonObject json);

    void callback(TelcoTransaction transaction, JsonObject json);
}
