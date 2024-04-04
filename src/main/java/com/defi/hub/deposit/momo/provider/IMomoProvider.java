package com.defi.hub.deposit.momo.provider;

import com.defi.hub.deposit.momo.MomoTransaction;
import com.google.gson.JsonObject;

public interface IMomoProvider {
    JsonObject send(MomoTransaction transaction);

    boolean verifyCallback(JsonObject json);

    void callback(MomoTransaction transaction, JsonObject json);
}
