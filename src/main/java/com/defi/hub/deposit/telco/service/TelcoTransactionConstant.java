package com.defi.hub.deposit.telco.service;

public class TelcoTransactionConstant {
    public static final int STATE_CLIENT_NEW                                  = 0;
    public static final int STATE_PROVIDER_CREATED                            = 1;
    public static final int STATE_PROVIDER_CALLBACKED                         = 2;
    public static final int STATE_CLIENT_CALLBACKED_SUCCESS                   = 3;
    public static final int STATE_CLIENT_CALLBACKED_CANCEL                    = 4;

    public static final int ERROR_TRANSACTION_FAILED                          = 1;
    public static int EXPIRED_TIME_PERIOD                                     = 6*3600*1000;

    public static String TELCO_VIETTEL = "VTT";
    public static String TELCO_VINAPHONE = "VNP";
    public static String TELCO_MOBIFONE = "VMS";
    public static String TELCO_VIETNAM_MOBILE = "VNM";
}
