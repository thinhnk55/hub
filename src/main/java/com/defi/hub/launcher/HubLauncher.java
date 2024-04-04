package com.defi.hub.launcher;

import com.defi.hub.deposit.bank.BankManager;
import com.defi.hub.deposit.momo.MomoManager;
import com.defi.hub.deposit.telco.TelcoManager;
import com.defi.hub.internal.HubClientManager;
import com.defi.hub.vertx.HubVerticle;
import com.defi.hub.vertx.HubVertx;
import com.defi.util.log.DebugLogger;
import com.defi.util.sql.HikariClients;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.ThreadingModel;
import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import org.apache.log4j.xml.DOMConfigurator;

public class HubLauncher {
    public static void main(String[] args) {
        try {
            initConfig();
            initLogic();
            startHttpServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void initLogic() {
        BankManager.instance().init("data/hub/bank.json");
        MomoManager.instance().init("data/hub/momo.json");
        TelcoManager.instance().init("data/hub/telco.json");
        HubClientManager.instance().init("hub_client");
    }


    public static void initConfig() throws Exception {
        DOMConfigurator.configure("config/hub/log/log4j.xml");
        HubVertx.instance().init("config/hub/vertx/http.json");
        HikariClients.instance().init("config/hub/sql/databases.json"
                , "config/hub/sql/hikari.properties");
    }
    public static void startHttpServer() {
        int procs = Runtime.getRuntime().availableProcessors();
        VertxOptions vxOptions = new VertxOptions()
                .setBlockedThreadCheckInterval(30000);
        HubVertx.instance().vertx = Vertx.vertx(vxOptions);
        DeploymentOptions deploymentOptions = new DeploymentOptions();
        deploymentOptions.setThreadingModel(ThreadingModel.WORKER)
                .setWorkerPoolSize(procs * 2);
        HubVertx.instance().vertx.deployVerticle(HubVerticle.class.getName(),
                deploymentOptions.setInstances(procs * 2), event -> {
                    if (event.succeeded()) {
                        DebugLogger.logger.error("Your Vert.x application is started!");
                    } else {
                        DebugLogger.logger.error("Unable to start your application", event.cause());
                    }
                });
    }
}
