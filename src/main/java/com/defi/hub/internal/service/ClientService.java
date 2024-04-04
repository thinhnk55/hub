package com.defi.hub.internal.service;

import com.defi.common.SimpleResponse;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.HikariClients;
import com.defi.util.sql.SQLJavaBridge;
import com.defi.util.string.StringUtil;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.exception.ExceptionUtils;

public class ClientService implements IClientService{
    String table;
    SQLJavaBridge bridge;
    public ClientService(String table, SQLJavaBridge bridge){
        this.table = table;
        this.bridge = bridge;
    }
    @Override
    public JsonObject create(String client) {
        try{
            JsonObject response = get(client);
            if(SimpleResponse.isSuccess(response)){
                return response;
            }
            long create_time = System.currentTimeMillis();
            String secret_key = StringUtil.randomStringUpperCaseCharacter(12);
            String access_key = StringUtil.randomStringUpperCaseCharacter(12);
            String query = new StringBuilder("INSERT INTO ")
                    .append(table)
                    .append(" (client, secret_key, access_key, create_time) VALUE (?,?,?,?)")
                    .toString();
            bridge.update(query, client, secret_key, access_key, create_time);
            JsonObject json = new JsonObject();
            json.addProperty("client", client);
            json.addProperty("secret_key", secret_key);
            json.addProperty("access_key", access_key);
            json.addProperty("create_time", create_time);
            return SimpleResponse.createResponse(0, json);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject get(String client) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = new StringBuilder("SELECT * FROM ")
                    .append(table)
                    .append(" WHERE client = ?")
                    .toString();
            JsonObject json = bridge.queryOne(query, client);
            if(json == null){
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0, json);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }
}
