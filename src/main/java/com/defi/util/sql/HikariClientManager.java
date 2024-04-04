package com.defi.util.sql;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HikariClientManager {
    Map<String, HikariClient> clients = new HashMap<String, HikariClient>();

    HikariClient createHikariClient(String clientName, String jdbcUrl,
                                                         String user, String password,
                                                         String database, String otherConfigFile) throws IOException {
        HikariClient client = new HikariClient(jdbcUrl, user, password, database, otherConfigFile);
        clients.put(clientName, client);
        return client;
    }

    public HikariClient getHikariClient(String clientName) throws IOException {
        HikariClient client = clients.get(clientName);
        return client;
    }
}
