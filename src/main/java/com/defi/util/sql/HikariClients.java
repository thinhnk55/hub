package com.defi.util.sql;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.defi.util.file.FileUtil;
import com.defi.util.json.GsonUtil;
import com.defi.util.log.DebugLogger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HikariClients {
    private static HikariClients ins = null;

    private HikariClients() {

    }

    public static HikariClients instance() {
        if (ins == null) {
            ins = new HikariClients();
        }
        return ins;
    }

    Map<String, HikariClient> clients = new HashMap<String, HikariClient>();
    Map<String, SQLJavaBridge> bridges = new HashMap<String, SQLJavaBridge>();
    HikariClient defaultClient;
    SQLJavaBridge defaultBridge;

    public void init(String databases, String otherConfigFile) throws IOException {
        DebugLogger.logger.info("Hikari Client init ...");
        String config = FileUtil.getString(databases);
        JsonArray array = GsonUtil.toJsonArray(config);
        for (int i = 0; i < array.size(); i++) {
            JsonObject json = array.get(i).getAsJsonObject();
            String clientName = json.get("name").getAsString();
            String jdbcUrl = json.get("jdbcUrl").getAsString();
            String user = json.get("dataSource.user").getAsString();
            String password = json.get("dataSource.password").getAsString();
            String database = json.get("dataSource.database").getAsString();
            HikariClient sqlClient = createHikariClient(clientName, jdbcUrl, user, password, database, otherConfigFile);
            SQLJavaBridge bridge = new SQLJavaBridge(sqlClient);
            clients.put(clientName, sqlClient);
            bridges.put(clientName, bridge);
            if (i == 0) {
                this.defaultClient = sqlClient;
                this.defaultBridge = bridge;
            }
        }
    }

    HikariClient createHikariClient(String clientName, String jdbcUrl,
                                                         String user, String password,
                                                         String database, String otherConfigFile) throws IOException {
        HikariClient sqlClient = new HikariClient(jdbcUrl, user, password, database, otherConfigFile);
        return sqlClient;
    }

    public HikariClient getHikariClient(String clientName) {
        HikariClient client = clients.get(clientName);
        return client;
    }

    public SQLJavaBridge getSQLJavaBridge(String clientName) {
        SQLJavaBridge bridge = bridges.get(clientName);
        return bridge;
    }

    public HikariClient defaultHikariClient() {
        return defaultClient;
    }

    public SQLJavaBridge defaulSQLJavaBridge() {
        return defaultBridge;
    }
}
