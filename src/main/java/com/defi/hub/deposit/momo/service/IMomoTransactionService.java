package com.defi.hub.deposit.momo.service;

import com.defi.hub.deposit.momo.MomoTransaction;
import com.google.gson.JsonObject;

public interface IMomoTransactionService {
    JsonObject createTransaction(String client, String client_transaction_id, String client_callback_url, int request_amount);
    JsonObject getTransaction(String code);
    JsonObject providerCreated(MomoTransaction transaction);

    JsonObject providerCallbacked(MomoTransaction transaction);

    JsonObject callbackClient(MomoTransaction transaction);

    JsonObject listByState(int stateProviderCallbacked);

    JsonObject providerCreateFailed(MomoTransaction transaction);
}
