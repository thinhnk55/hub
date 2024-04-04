package com.defi.util.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DebugLogger {
    public static final Logger logger = LoggerFactory.getLogger("debug");
    public static Logger getLogger(String loggerName){
        return LoggerFactory.getLogger(loggerName);
    }
}
