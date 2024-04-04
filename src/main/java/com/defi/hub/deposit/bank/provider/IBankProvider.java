package com.defi.hub.deposit.bank.provider;

import com.defi.hub.deposit.bank.BankList;
import com.defi.hub.deposit.bank.BankTransaction;
import com.google.gson.JsonObject;

public interface IBankProvider {
    JsonObject send(BankTransaction transaction);

    boolean verifyCallback(JsonObject json);

    void callback(BankTransaction transaction, JsonObject json);

    void updateSupportBank(BankList bankList);
}
