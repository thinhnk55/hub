package com.defi.hub.deposit.bank;

import com.google.gson.JsonObject;

public class Bank {
    public int id;
    public String code;
    public String name;
    public String shortName;
    public String logo;
    public String swift_code;
    public int transferSupported;
    public int lookupSupported;

    public Bank(JsonObject json) {
        this.id = json.get("id").getAsInt();
        this.code = json.get("code").getAsString();
        this.name = json.get("name").getAsString();
        this.shortName = json.get("shortName").getAsString();
        this.logo = json.get("logo").getAsString();
        this.swift_code = json.get("id").getAsString();
        this.transferSupported = json.get("transferSupported").getAsInt();
        this.lookupSupported = json.get("lookupSupported").getAsInt();
    }
}
