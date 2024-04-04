package com.defi.hub.deposit.telco.service;

import com.defi.common.SimpleResponse;
import com.defi.hub.deposit.telco.TelcoTransaction;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.HikariClients;
import com.defi.util.sql.SQLJavaBridge;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.exception.ExceptionUtils;

import static com.defi.hub.deposit.telco.service.TelcoTransactionConstant.STATE_CLIENT_NEW;

public class TelcoTransactionService implements ITelcoTransactionService{
    String table;
    SQLJavaBridge bridge;
    public TelcoTransactionService(String table, SQLJavaBridge bridge){
        this.table = table;
        this.bridge = bridge;
    }
    @Override
    public JsonObject createTransaction(String client, String client_transaction_id,
                                        String client_callback_url, String card_type,
                                        String card_seri, String card_code, long request_amount) {
        try{
            long create_time = System.currentTimeMillis();
            long expired_time = create_time + TelcoTransactionConstant.EXPIRED_TIME_PERIOD;
            JsonObject json = new JsonObject();
            json.addProperty("client", client);
            json.addProperty("client_transaction_id", client_transaction_id);
            json.addProperty("client_callback_url", client_callback_url);
            json.addProperty("card_type", card_type);
            json.addProperty("card_seri", card_seri);
            json.addProperty("card_code", card_code);
            json.addProperty("state", STATE_CLIENT_NEW);
            json.addProperty("request_amount", request_amount);
            json.addProperty("create_time", create_time);
            json.addProperty("expired_time", expired_time);
            bridge.insertObjectToDB(table, json);
            long id = json.get("id").getAsLong();
            return getTransaction(id);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject getTransaction(long id) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = new StringBuilder("SELECT * FROM ")
                    .append(table)
                    .append(" WHERE id = ?")
                    .toString();
            JsonObject json = bridge.queryOne(query, id);
            if(json == null) {
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0, json);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject providerCreated(TelcoTransaction transaction) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            transaction.update_time = System.currentTimeMillis();
            String query = new StringBuilder("UPDATE ")
                    .append(table)
                    .append(" SET provider = ?, provider_transaction_id = ?, provider_transaction_response = ?, state = ?, error = ?, hub_callback_url = ?, update_time = ?")
                    .append(" WHERE id = ?")
                    .toString();
            int x = bridge.update(query, transaction.provider,
                    transaction.provider_transaction_id,
                    transaction.provider_transaction_response,
                    transaction.state, transaction.error,
                    transaction.hub_callback_url,
                    transaction.update_time, transaction.id);
            if(x == 0) {
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject providerCallbacked(TelcoTransaction transaction) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            transaction.update_time = System.currentTimeMillis();
            String query = new StringBuilder("UPDATE ")
                    .append(table)
                    .append(" SET real_amount = ?, provider_callback_data = ?, state = ?, error = ?, update_time = ?")
                    .append(" WHERE id = ?")
                    .toString();
            int x = bridge.update(query, transaction.real_amount,
                    transaction.provider_callback_data,
                    transaction.state,
                    transaction.error,
                    transaction.update_time, transaction.id);
            if(x == 0) {
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject callbackClient(TelcoTransaction transaction) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            transaction.update_time = System.currentTimeMillis();
            String query = new StringBuilder("UPDATE ")
                    .append(table)
                    .append(" SET client_callback_count = ?, client_callback_response = ?, state = ?, update_time = ?")
                    .append(" WHERE id = ?")
                    .toString();
            int x = bridge.update(query, transaction.client_callback_count,
                    transaction.client_callback_response, transaction.state,
                    transaction.update_time, transaction.id);
            if(x == 0) {
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject listByState(int state) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = new StringBuilder("SELECT * FROM ")
                    .append(table)
                    .append(" WHERE state = ?")
                    .toString();
            JsonArray array = bridge.query(query, state);
            return SimpleResponse.createResponse(0, array);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    @Override
    public JsonObject providerCreateFailed(TelcoTransaction transaction) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            transaction.update_time = System.currentTimeMillis();
            String query = new StringBuilder("UPDATE ")
                    .append(table)
                    .append(" SET provider = ?, provider_transaction_response = ?, state = ?, error = ?, update_time = ?")
                    .append(" WHERE id = ?")
                    .toString();
            int x = bridge.update(query, transaction.provider,
                    transaction.provider_transaction_response,
                    transaction.state, transaction.error,
                    transaction.update_time, transaction.id);
            if(x == 0) {
                return SimpleResponse.createResponse(10);
            }
            return SimpleResponse.createResponse(0);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }
}
