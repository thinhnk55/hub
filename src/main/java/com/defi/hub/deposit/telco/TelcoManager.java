package com.defi.hub.deposit.telco;

import com.defi.common.SimpleResponse;
import com.defi.hub.deposit.telco.provider.ITelcoProvider;
import com.defi.hub.deposit.telco.provider.TelcoCocoPayProvider;
import com.defi.hub.deposit.telco.service.ITelcoTransactionService;
import com.defi.hub.deposit.telco.service.TelcoTransactionService;
import com.defi.util.json.GsonUtil;
import com.defi.util.sql.HikariClients;
import com.google.gson.JsonObject;

import static com.defi.hub.deposit.telco.service.TelcoTransactionConstant.STATE_PROVIDER_CREATED;
public class TelcoManager {
    private static TelcoManager ins = null;
    public static TelcoManager instance() {
        if (ins == null) {
            ins = new TelcoManager();
        }
        return ins;
    }
    private TelcoManager(){

    }
    public void init(String configFile){
        JsonObject config = GsonUtil.getJsonObject(configFile);
        String table = config.get("table_transaction").getAsString();
        transactionService = new TelcoTransactionService(table, HikariClients.instance().defaulSQLJavaBridge());
        JsonObject cocobayConfig = config.getAsJsonObject("provider").getAsJsonObject("cocopay");
        provider = new TelcoCocoPayProvider(cocobayConfig);
        worker = new TelcoTransactionWorker(transactionService);
        worker.run();
    }
    public ITelcoTransactionService transactionService;
    ITelcoProvider provider;
    TelcoTransactionWorker worker;

    public JsonObject createTransaction(String client_name, String client_transaction_id,
                                        String client_callback_url, String card_type,
                                        String card_seri, String card_code,
                                        long request_amount) {
        JsonObject response = transactionService.createTransaction(client_name, client_transaction_id,
                client_callback_url, card_type,
                card_seri, card_code,
                request_amount);
        if(SimpleResponse.isSuccess(response)){
            JsonObject json = response.getAsJsonObject("d");
            TelcoTransaction transaction = new TelcoTransaction(json);
            response = sendToProvider(transaction);
            if(SimpleResponse.isSuccess(response)){
                response = transactionService.providerCreated(transaction);
                if(SimpleResponse.isSuccess(response)){
                    return SimpleResponse.createResponse(0, transaction.toCreateSuccessJson());
                }else{
                    return SimpleResponse.createResponse(12);
                }
            }else{
                transactionService.providerCreateFailed(transaction);
                return SimpleResponse.createResponse(11);
            }
        }else{
            return SimpleResponse.createResponse(10);
        }
    }

    private JsonObject sendToProvider(TelcoTransaction transaction) {
        return provider.send(transaction);
    }

    public boolean verifyCallback(String providerName, JsonObject json) {
        return provider.verifyCallback(json);
    }

    public JsonObject callback(long id, String providerName, JsonObject json) {
        JsonObject response = transactionService.getTransaction(id);
        if(SimpleResponse.isSuccess(response)){
            JsonObject data = response.getAsJsonObject("d");
            TelcoTransaction transaction = new TelcoTransaction(data);
            if(transaction.state == STATE_PROVIDER_CREATED) {
                provider.callback(transaction, json);
                response = transactionService.providerCallbacked(transaction);
                if (SimpleResponse.isSuccess(response)) {
                    worker.clientCallback(transaction);
                    return SimpleResponse.createResponse(0);
                } else {
                    return SimpleResponse.createResponse(11);
                }
            }else{
                return SimpleResponse.createResponse(12);
            }
        }else{
            return SimpleResponse.createResponse(10);
        }
    }
}
