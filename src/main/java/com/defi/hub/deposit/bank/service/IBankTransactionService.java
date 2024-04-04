package com.defi.hub.deposit.bank.service;

import com.defi.hub.deposit.bank.Bank;
import com.defi.hub.deposit.bank.BankTransaction;
import com.google.gson.JsonObject;

public interface IBankTransactionService {
    JsonObject createTransaction(String client, String client_transaction_id, Bank bank,
                                 String client_callback_url, long request_amount);
    JsonObject getTransaction(String code);
    JsonObject providerCreated(BankTransaction transaction);

    JsonObject providerCallbacked(BankTransaction transaction);

    JsonObject callbackClient(BankTransaction transaction);

    JsonObject listByState(int state);

    JsonObject providerCreateFailed(BankTransaction transaction);
}
