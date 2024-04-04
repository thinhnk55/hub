package com.defi.hub.deposit.bank;

import com.defi.common.SimpleResponse;
import com.defi.hub.deposit.bank.provider.BankCocoPayProvider;
import com.defi.hub.deposit.bank.provider.IBankProvider;
import com.defi.hub.deposit.bank.service.BankTransactionService;
import com.defi.hub.deposit.bank.service.IBankTransactionService;
import com.defi.util.json.GsonUtil;
import com.defi.util.sql.HikariClients;
import com.google.gson.JsonObject;

import static com.defi.hub.deposit.momo.service.MomoTransactionConstant.STATE_PROVIDER_CREATED;

public class BankManager {
    private static BankManager ins = null;
    public static BankManager instance() {
        if (ins == null) {
            ins = new BankManager();
        }
        return ins;
    }
    private BankManager(){

    }
    public void init(String configFile){
        JsonObject config = GsonUtil.getJsonObject(configFile);
        String table = config.get("table_transaction").getAsString();
        bankList = new BankList();
        String bank_list_file = config.get("bank_list_file").getAsString();
        bankList.init(bank_list_file);
        transactionService = new BankTransactionService(table, HikariClients.instance().defaulSQLJavaBridge());
        JsonObject cocobayConfig = config.getAsJsonObject("provider").getAsJsonObject("cocopay");
        provider = new BankCocoPayProvider(cocobayConfig);
        worker = new BankTransactionWorker(transactionService, bankList, provider);
        provider.updateSupportBank(bankList);
        worker.run();
    }
    public IBankTransactionService transactionService;
    IBankProvider provider;
    BankTransactionWorker worker;
    public BankList bankList;

    public JsonObject createTransaction(String client_name, String client_transaction_id, String bank_code,
                                        String client_callback_url, int request_amount) {
        if(!bankList.supportBankCode.contains(bank_code)){
            return SimpleResponse.createResponse(13);
        }
        Bank bank = bankList.bankMap.get(bank_code);
        JsonObject response = transactionService.createTransaction(client_name, client_transaction_id,
                bank, client_callback_url, request_amount);
        if(SimpleResponse.isSuccess(response)){
            JsonObject json = response.getAsJsonObject("d");
            BankTransaction transaction = new BankTransaction(json);
            response = sendToProvider(transaction);
            if(SimpleResponse.isSuccess(response)){
                response = transactionService.providerCreated(transaction);
                if(SimpleResponse.isSuccess(response)){
                    return SimpleResponse.createResponse(0, transaction.toCreateSuccessJson());
                }else{
                    return SimpleResponse.createResponse(12);
                }
            }else{
                return SimpleResponse.createResponse(11);
            }
        }else{
            return SimpleResponse.createResponse(10);
        }
    }

    private JsonObject sendToProvider(BankTransaction transaction) {
        return provider.send(transaction);
    }

    public boolean verifyCallback(String providerName, JsonObject json) {
        return provider.verifyCallback(json);
    }

    public JsonObject callback(String code, String providerName, JsonObject json) {
        JsonObject response = transactionService.getTransaction(code);
        if(SimpleResponse.isSuccess(response)){
            JsonObject data = response.getAsJsonObject("d");
            BankTransaction transaction = new BankTransaction(data);
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
