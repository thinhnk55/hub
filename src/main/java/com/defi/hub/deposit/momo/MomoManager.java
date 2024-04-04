package com.defi.hub.deposit.momo;

import com.defi.common.SimpleResponse;
import com.defi.hub.deposit.momo.provider.MomoCocoPayProvider;
import com.defi.hub.deposit.momo.provider.IMomoProvider;
import com.defi.hub.deposit.momo.service.IMomoTransactionService;
import com.defi.hub.deposit.momo.service.MomoTransactionService;
import com.defi.util.json.GsonUtil;
import com.defi.util.sql.HikariClients;
import com.google.gson.JsonObject;

import static com.defi.hub.deposit.momo.service.MomoTransactionConstant.STATE_PROVIDER_CREATED;

public class MomoManager {
    private static MomoManager ins = null;
    public static MomoManager instance() {
        if (ins == null) {
            ins = new MomoManager();
        }
        return ins;
    }
    private MomoManager(){

    }
    public void init(String configFile){
        JsonObject config = GsonUtil.getJsonObject(configFile);
        String table = config.get("table_transaction").getAsString();
        transactionService = new MomoTransactionService(table, HikariClients.instance().defaulSQLJavaBridge());
        JsonObject cocobayConfig = config.getAsJsonObject("provider").getAsJsonObject("cocopay");
        provider = new MomoCocoPayProvider(cocobayConfig);
        worker = new MomoTransactionWorker(transactionService);
        worker.run();
    }
    public IMomoTransactionService transactionService;
    IMomoProvider provider;
    MomoTransactionWorker worker;

    public JsonObject createTransaction(String client_name, String client_transaction_id,
                                        String client_callback_url, long request_amount) {
        JsonObject response = transactionService.createTransaction(client_name, client_transaction_id, client_callback_url, request_amount);
        if(SimpleResponse.isSuccess(response)){
            JsonObject json = response.getAsJsonObject("d");
            MomoTransaction transaction = new MomoTransaction(json);
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

    private JsonObject sendToProvider(MomoTransaction transaction) {
        return provider.send(transaction);
    }

    public boolean verifyCallback(String providerName, JsonObject json) {
        return provider.verifyCallback(json);
    }

    public JsonObject callback(String code, String providerName, JsonObject json) {
        JsonObject response = transactionService.getTransaction(code);
        if(SimpleResponse.isSuccess(response)){
            JsonObject data = response.getAsJsonObject("d");
            MomoTransaction transaction = new MomoTransaction(data);
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
