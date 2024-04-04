package com;

import com.defi.hub.deposit.momo.MomoManager;
import com.defi.hub.internal.HubClientManager;
import com.defi.hub.vertx.HubVertx;
import com.defi.util.log.DebugLogger;
import com.defi.util.network.OkHttpUtil;
import com.defi.util.sql.HikariClients;
import com.defi.util.string.StringUtil;
import com.google.gson.JsonObject;
import org.apache.log4j.xml.DOMConfigurator;

public class HubTest {
    public static void main(String[] args) {
        try {
            initConfig();
            bankCallbackTest();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void bankCallbackTest() {
        String api_key = "zgaxytcobujri5l78qdpf3k691m2es4v";
        String request_id = "XTJJFJ";
        int status = 1;
        long amount = 10000;
        String message = request_id;
        String signData = new StringBuilder()
                .append(request_id)
                .append(message)
                .append(amount)
                .append(status)
                .append(api_key)
                .toString();
        String signature = StringUtil.md5(signData);

        String url = new StringBuilder("http://127.0.0.1:8080/hb/cocobay/bank")
                .append("?request_id=").append(request_id)
                .append("&status=").append(status)
                .append("&amount=").append(amount)
                .append("&message=").append(message)
                .append("&signature=").append(signature)
                .toString();
        JsonObject response = OkHttpUtil.get(url);
        DebugLogger.logger.info("{}", response);
    }

    private static void momoCallbackTest() {
        String api_key = "zgaxytcobujri5l78qdpf3k691m2es4v";
        String request_id = "";
        int status = 1;
        long amount = 10000;
        String message = request_id;
        String signData = new StringBuilder()
                .append(request_id)
                .append(message)
                .append(amount)
                .append(status)
                .append(api_key)
                .toString();
        String signature = StringUtil.md5(signData);

        String url = new StringBuilder("http://127.0.0.1:8080/hb/cocobay/momo")
                .append("?request_id=").append(request_id)
                .append("&status=").append(status)
                .append("&amount=").append(amount)
                .append("&message=").append(message)
                .append("&signature=").append(signature)
                .toString();
        JsonObject response = OkHttpUtil.get(url);
        DebugLogger.logger.info("{}", response);
    }

    private static void createClient() {
        HubClientManager.instance().init("hub_client");
        JsonObject response = HubClientManager.instance().clientService.create("x1");
        DebugLogger.logger.info("{}", response);
    }

    private static void initLogic() {
        MomoManager.instance().init("data/hub/momo.json");
        HubClientManager.instance().init("hub_client");
    }


    public static void initConfig() throws Exception {
        DOMConfigurator.configure("config/hub/log/log4j.xml");
        HubVertx.instance().init("config/hub/vertx/http.json");
        HikariClients.instance().init("config/hub/sql/databases.json"
                , "config/hub/sql/hikari.properties");
    }
}
