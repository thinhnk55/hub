package com.defi.hub.internal;

import com.defi.common.SimpleResponse;
import com.defi.hub.internal.service.ClientService;
import com.defi.hub.internal.service.IClientService;
import com.defi.util.sql.HikariClients;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class HubClientManager {
    private static HubClientManager ins = null;
    public static HubClientManager instance() {
        if (ins == null) {
            ins = new HubClientManager();
        }
        return ins;
    }
    private HubClientManager(){

    }
    public void init(String table){
        hubClients = new HashMap<>();
        clientService = new ClientService(table, HikariClients.instance().defaulSQLJavaBridge());
    }
    public IClientService clientService;
    Map<String, HubClient> hubClients;

    public HubClient getClient(String client){
        HubClient hubClient = hubClients.get(client);
        if(hubClient == null){
            return getClientFromDB(client);
        }
        return hubClient;
    }

    private HubClient getClientFromDB(String client) {
        JsonObject response = clientService.get(client);
        HubClient hubClient = null;
        if(SimpleResponse.isSuccess(response)){
            JsonObject json = response.getAsJsonObject("d");
            hubClient = new HubClient(json);
            hubClients.put(client, hubClient);
        }
        return hubClient;
    }
}
