package com.defi.hub.deposit.momo.service;

import com.defi.common.SimpleResponse;
import com.defi.hub.deposit.momo.MomoTransaction;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.HikariClients;
import com.defi.util.sql.SQLJavaBridge;
import com.defi.util.string.StringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.exception.ExceptionUtils;

import static com.defi.hub.deposit.momo.service.MomoTransactionConstant.EXPIRED_TIME_PERIOD;

public class MomoTransactionService implements IMomoTransactionService{
    String table;
    SQLJavaBridge bridge;
    public MomoTransactionService(String table, SQLJavaBridge bridge){
        this.table = table;
        this.bridge = bridge;
    }

    @Override
    public JsonObject createTransaction(String client, String client_transaction_id,
                                        String client_callback_url, long request_amount) {
        try{
            String code = generateCode();
            try{
                long create_time = System.currentTimeMillis();
                long expired_time = create_time + EXPIRED_TIME_PERIOD;
                String query = new StringBuilder("INSERT INTO ")
                        .append(table)
                        .append(" (code, client, client_transaction_id, client_callback_url, request_amount, provider_transaction_id, momo_transaction_id, state, create_time, expired_time) VALUE (?,?,?,?,?,?,?,?,?,?)")
                        .toString();
                int x = bridge.update(query, code, client, client_transaction_id,
                        client_callback_url, request_amount, code, code, MomoTransactionConstant.STATE_CLIENT_NEW, create_time, expired_time);
                if(x == 0){
                    return SimpleResponse.createResponse(10);
                }
                return getTransaction(code);
            }catch (Exception e){
                DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
                return SimpleResponse.createResponse(1);
            }
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return SimpleResponse.createResponse(1);
        }
    }

    private String generateCode() {
        try{
            do{
                String code = StringUtil.randomStringUpperCaseCharacter(6);
                boolean isExist = isExist(code);
                if(!isExist){
                    return code;
                }
            }while (true);
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return null;
        }
    }

    private boolean isExist(String code) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = new StringBuilder("SELECT * FROM ")
                    .append(table)
                    .append(" WHERE code = ?")
                    .toString();
            boolean result = bridge.queryExist(query, code);
            return result;
        }catch (Exception e){
            DebugLogger.logger.error(ExceptionUtils.getStackTrace(e));
            return false;
        }
    }

    @Override
    public JsonObject getTransaction(String code) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            String query = new StringBuilder("SELECT * FROM ")
                    .append(table)
                    .append(" WHERE code = ?")
                    .toString();
            JsonObject json = bridge.queryOne(query, code);
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
    public JsonObject providerCreated(MomoTransaction transaction) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            transaction.update_time = System.currentTimeMillis();
            String query = new StringBuilder("UPDATE ")
                    .append(table)
                    .append(" SET provider = ?, provider_transaction_response = ?, name = ?, phone = ?, message = ?, state = ?, hub_callback_url = ?, update_time = ?")
                    .append(" WHERE code = ?")
                    .toString();
            int x = bridge.update(query, transaction.provider, transaction.provider_transaction_response, transaction.name, transaction.phone,
                    transaction.message, transaction.state, transaction.hub_callback_url, transaction.update_time, transaction.code);
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
    public JsonObject providerCallbacked(MomoTransaction transaction) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            transaction.update_time = System.currentTimeMillis();
            String query = new StringBuilder("UPDATE ")
                    .append(table)
                    .append(" SET real_amount = ?, message = ?, provider_transaction_id = ?, provider_callback_data = ?, state = ?, update_time = ?")
                    .append(" WHERE code = ?")
                    .toString();
            int x = bridge.update(query, transaction.real_amount,
                    transaction.message,
                    transaction.provider_transaction_id,
                    transaction.provider_callback_data,
                    transaction.state,
                    transaction.update_time, transaction.code);
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
    public JsonObject providerCreateFailed(MomoTransaction transaction) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            transaction.update_time = System.currentTimeMillis();
            String query = new StringBuilder("UPDATE ")
                    .append(table)
                    .append(" SET provider = ?, provider_callback_data = ?, state = ?, error = ?, update_time = ?")
                    .append(" WHERE code = ?")
                    .toString();
            int x = bridge.update(query, transaction.provider, transaction.provider_callback_data,
                    transaction.state, transaction.error,
                    transaction.update_time, transaction.code);
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
    public JsonObject callbackClient(MomoTransaction transaction) {
        try{
            SQLJavaBridge bridge = HikariClients.instance().defaulSQLJavaBridge();
            transaction.update_time = System.currentTimeMillis();
            String query = new StringBuilder("UPDATE ")
                    .append(table)
                    .append(" SET client_callback_count = ?, client_callback_response = ?, state = ?, update_time = ?")
                    .append(" WHERE code = ?")
                    .toString();
            int x = bridge.update(query, transaction.client_callback_count,
                    transaction.client_callback_response, transaction.state,
                    transaction.update_time, transaction.code);
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
}
