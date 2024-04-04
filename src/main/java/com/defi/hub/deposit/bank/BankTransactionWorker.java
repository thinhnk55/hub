package com.defi.hub.deposit.bank;

import com.defi.common.SimpleResponse;
import com.defi.hub.deposit.bank.provider.IBankProvider;
import com.defi.hub.deposit.bank.service.IBankTransactionService;
import com.defi.hub.internal.HubClient;
import com.defi.hub.internal.HubClientManager;
import com.defi.util.network.OkHttpUtil;
import com.defi.util.string.StringUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.*;

import static com.defi.hub.deposit.bank.service.BankTransactionConstant.*;


public class BankTransactionWorker {
    BlockingQueue<BankTransaction> clientCallbackQueue;
    IBankTransactionService transactionService;
    BankList bankList;
    IBankProvider provider;

    public BankTransactionWorker(IBankTransactionService transactionService,
                                 BankList bankList, IBankProvider provider) {
        this.transactionService = transactionService;
        this.bankList = bankList;
        this.provider = provider;
        clientCallbackQueue = new LinkedBlockingDeque<>();
        JsonObject response = transactionService.listByState(STATE_PROVIDER_CALLBACKED);
        if(SimpleResponse.isSuccess(response)){
            JsonArray array = response.getAsJsonArray("d");
            for(int i = 0; i < array.size(); i++){
                JsonObject json = array.get(i).getAsJsonObject();
                BankTransaction transaction = new BankTransaction(json);
                clientCallbackQueue.add(transaction);
            }
        }
    }

    public void run() {
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
        executor.scheduleAtFixedRate(() -> {
            loop();
        }, 1, 3, TimeUnit.MINUTES);
    }

    private void loop() {
        List<BankTransaction> list = new LinkedList<>();
        while (clientCallbackQueue.size() > 0){
            BankTransaction transaction = clientCallbackQueue.poll();
            list.add(transaction);
        }
        if(list.size() > 0){
            clientCallback(list);
        }
        updateSupportBank();
    }

    private void updateSupportBank() {
        provider.updateSupportBank(bankList);
    }

    private void clientCallback(List<BankTransaction> list) {
        for(int i = 0; i < list.size(); i++){
            BankTransaction transaction = list.get(i);
            clientCallback(transaction);
        }
    }

    public void clientCallback(BankTransaction transaction) {
        HubClient client = HubClientManager.instance().getClient(transaction.client);
        String signData = new StringBuilder()
                .append(transaction.client_transaction_id)
                .append(transaction.error)
                .append(transaction.real_amount)
                .append(client.secret_key)
                .toString();
        String signature = StringUtil.md5(signData);
        String url = new StringBuilder()
                .append(transaction.client_callback_url)
                .append("?request_id=").append(transaction.client_transaction_id)
                .append("&error=").append(transaction.error)
                .append("&amount=").append(transaction.real_amount)
                .append("&signature=").append(signature)
                .toString();
        JsonObject response = OkHttpUtil.get(url);
        if(response != null) {
            transaction.client_callback_response = response;
        }
        transaction.client_callback_count++;
        if(response != null && SimpleResponse.isSuccess(response)){
            transaction.state = STATE_CLIENT_CALLBACKED_SUCCESS;
            transactionService.callbackClient(transaction);
        }else {
            if(transaction.client_callback_count >= 10){
                transaction.state = STATE_CLIENT_CALLBACKED_CANCEL;
                transactionService.callbackClient(transaction);
            }else{
                transactionService.callbackClient(transaction);
                clientCallbackQueue.add(transaction);
            }
        }
    }
}
