package com.defi.hub.deposit.bank.provider;

import com.defi.common.SimpleResponse;
import com.defi.hub.deposit.bank.Bank;
import com.defi.hub.deposit.bank.BankList;
import com.defi.hub.deposit.bank.BankTransaction;
import com.defi.hub.deposit.bank.service.BankTransactionConstant;
import com.defi.hub.deposit.momo.service.MomoTransactionConstant;
import com.defi.util.json.GsonUtil;
import com.defi.util.log.DebugLogger;
import com.defi.util.network.OkHttpUtil;
import com.defi.util.string.StringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BankCocoPayProvider implements IBankProvider{
    String username;
    String password = "Abc@123";
    String api_key;
    String provider_submit_url;
    String provider_list_bank_url;
    String hub_callback_url;

    Map<String, String> provider_to_hub;
    Map<String, String> hub_to_provider;

    public BankCocoPayProvider(JsonObject json){
        this.username = json.get("username").getAsString();
        this.api_key = json.get("api_key").getAsString();
        this.provider_submit_url = json.get("provider_submit_url").getAsString();
        this.provider_list_bank_url = json.get("provider_list_bank_url").getAsString();
        this.hub_callback_url = json.get("hub_callback_url").getAsString();
        provider_to_hub = new HashMap<>();
        hub_to_provider = new HashMap<>();
        JsonArray array = json.getAsJsonArray("bank_mapping");
        for(int i = 0; i < array.size(); i++){
            JsonObject map = array.get(i).getAsJsonObject();
            String provider_code = map.get("provider_code").getAsString();
            String hub_code = map.get("hub_code").getAsString();
            provider_to_hub.put(provider_code, hub_code);
            hub_to_provider.put(hub_code, provider_code);
        }
    }

    public JsonObject requestPayment(String request_id, String bank_code, long amount, int custom_content){
        String url = new StringBuilder(provider_submit_url)
                .append("?api_key=").append(api_key)
                .append("&request_id=").append(request_id)
                .append("&bank_code=").append(bank_code)
                .append("&amount=").append(amount)
                .append("&url_callback=").append(hub_callback_url)
                .append("&custom_content=").append(custom_content)
                .toString();
        JsonObject response = OkHttpUtil.get(url);
//        JsonObject response = sample(request_id, amount);
        DebugLogger.logger.info("requestPayment url: {} response: {}", url, response);
        return response;
    }

    private JsonObject sample(String request_id, long amount) {
        JsonObject json = new JsonObject();
        json.addProperty("status", 1);
        json.addProperty("bank", "MB Quân đội");
        json.addProperty("account", "08860493690");
        json.addProperty("bank_name", "TRAN XUAN THANG");
        json.addProperty("content", request_id);
        json.addProperty("amount", amount);
        return json;
    }

    @Override
    public JsonObject send(BankTransaction transaction) {
        transaction.provider_bank_code = hub_to_provider.get(transaction.bank_code);
        JsonObject response = requestPayment(transaction.code, transaction.provider_bank_code,
                transaction.request_amount, 0);
        int status = response.get("status").getAsInt();
        if(status == 1){
            transaction.provider = username;
            transaction.hub_callback_url = hub_callback_url;
            transaction.state = BankTransactionConstant.STATE_PROVIDER_CREATED;
            transaction.bank_account = response.get("account").getAsString();
            transaction.bank_owner = response.get("bank_name").getAsString();
            transaction.message = response.get("content").getAsString();
            transaction.request_amount = response.get("amount").getAsLong();
            transaction.provider_transaction_response = response;
            transaction.update_time = System.currentTimeMillis();
            return SimpleResponse.createResponse(0);
        }else{
            transaction.error = BankTransactionConstant.STATE_PROVIDER_CREATED;
            transaction.provider_transaction_response = response;
            transaction.update_time = System.currentTimeMillis();
            return SimpleResponse.createResponse(10);
        }
    }

    @Override
    public boolean verifyCallback(JsonObject json) {
        String request_id = json.get("request_id").getAsString();
        String message = json.get("message").getAsString();
        String amount = json.get("amount").getAsString();
        String status = json.get("status").getAsString();
        String signature = json.get("signature").getAsString();
        String signData = new StringBuilder()
                .append(request_id)
                .append(message)
                .append(amount)
                .append(status)
                .append(api_key)
                .toString();
        String sign = StringUtil.md5(signData);
        if(sign.equals(signature)){
            return true;
        }
        return false;
    }

    @Override
    public void callback(BankTransaction transaction, JsonObject json) {
        String trans_id = json.get("trans_id").getAsString();
        String message = json.get("message").getAsString();
        long amount = json.get("amount").getAsLong();
        int status = json.get("status").getAsInt();
        transaction.real_amount = amount;
        if(!trans_id.equals("")) {
            transaction.provider_transaction_id = trans_id;
        }
        transaction.message = message;
        transaction.state = MomoTransactionConstant.STATE_PROVIDER_CALLBACKED;
        transaction.provider_callback_data = json;
        transaction.create_time = System.currentTimeMillis();
        if(status != 1){
            transaction.error = 1;
        }
    }

    @Override
    public void updateSupportBank(BankList bankList) {
        String url = new StringBuilder(provider_list_bank_url)
                .append("?api_key=").append(api_key)
                .toString();
        JsonArray array = OkHttpUtil.getAsArray(url);
//        DebugLogger.logger.info("updateSupportBank {} {}", url, array);
        if(array != null){
            Set<String> supportBankCode = new HashSet<>();
            JsonArray list = new JsonArray();
            for(int i = 0; i < array.size(); i++){
                JsonObject json = array.get(i).getAsJsonObject();
                String provider_code = json.get("code").getAsString();
                String hub_code = provider_to_hub.get(provider_code);
                if(hub_code != null){
                    Bank bank = bankList.bankMap.get(hub_code);
                    if(bank != null){
                        supportBankCode.add(bank.code);
                        list.add(bank.code);
                    }
                }
            }
            bankList.supportBankCode = supportBankCode;
            bankList.supportCache = SimpleResponse.createResponse(0, list).toString();
        }
    }

    private JsonArray listSample() {
        String s = "[{\"id\":\"1\",\"code\":\"VCB\",\"bank_name\":\"VietcomBank\"},{\"id\":\"2\",\"code\":\"VTB\",\"bank_name\":\"ViettinBank\"},{\"id\":\"3\",\"code\":\"TCB\",\"bank_name\":\"Techcombank\"},{\"id\":\"4\",\"code\":\"VIB\",\"bank_name\":\"VIB\"},{\"id\":\"5\",\"code\":\"BIDV\",\"bank_name\":\"BIDV\"},{\"id\":\"6\",\"code\":\"SHB\",\"bank_name\":\"SHB\"},{\"id\":\"7\",\"code\":\"DAB\",\"bank_name\":\"DongABank\"},{\"id\":\"8\",\"code\":\"ACB\",\"bank_name\":\"ACB\"},{\"id\":\"9\",\"code\":\"TPB\",\"bank_name\":\"TPBank\"},{\"id\":\"10\",\"code\":\"EXB\",\"bank_name\":\"Eximbank\"},{\"id\":\"11\",\"code\":\"SAC\",\"bank_name\":\"Sacombank\"},{\"id\":\"12\",\"code\":\"SCB\",\"bank_name\":\"SaigonBank\"}]";
        return GsonUtil.toJsonArray(s);
    }
}
