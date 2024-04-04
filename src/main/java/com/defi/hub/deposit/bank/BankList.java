package com.defi.hub.deposit.bank;

import com.defi.util.json.GsonUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class BankList {
    public Map<String, Bank> bankMap;
    public Set<String> supportBankCode;
    public String supportCache;

    public BankList(){
        bankMap = new HashMap<>();
    }
    public void init(String configFile){
        JsonObject config = GsonUtil.getJsonObject(configFile);
        JsonArray array = config.getAsJsonArray("data");
        for(int i = 0; i < array.size(); i++){
            JsonObject json = array.get(i).getAsJsonObject();
            Bank bank = new Bank(json);
            bankMap.put(bank.code, bank);
        }
    }
}
