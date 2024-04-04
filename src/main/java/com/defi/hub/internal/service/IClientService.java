package com.defi.hub.internal.service;

import com.google.gson.JsonObject;

public interface IClientService {
    JsonObject create(String client);
    JsonObject get(String client);
}
