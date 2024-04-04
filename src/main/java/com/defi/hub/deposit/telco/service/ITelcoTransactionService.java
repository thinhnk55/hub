package com.defi.hub.deposit.telco.service;

import com.defi.hub.deposit.telco.TelcoTransaction;
import com.google.gson.JsonObject;

public interface ITelcoTransactionService {
    JsonObject createTransaction(String client, String client_transaction_id,
                                 String client_callback_url, String card_type,
                                 String card_seri, String card_code,
                                 long request_amount);
    JsonObject getTransaction(long id);
    JsonObject providerCreated(TelcoTransaction transaction);

    JsonObject providerCallbacked(TelcoTransaction transaction);

    JsonObject callbackClient(TelcoTransaction transaction);

    JsonObject listByState(int state);

    JsonObject providerCreateFailed(TelcoTransaction transaction);
}
