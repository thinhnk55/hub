package com.defi.util.time;

public class TimeUtil {
    public static long getTimestampSecond(){
        return System.currentTimeMillis()/1000;
    }
    public static long getTimestampMilisecond(){
        return System.currentTimeMillis();
    }
    public static long startDayTimeStamp(){
        long now = System.currentTimeMillis();
        return getStartDay(now);
    }
    public static long getStartDay(long now) {
        long timestamp = now - (now % 86400000);
        return timestamp;
    }

    public static String convertToTimeStringHourMinuteSecond(long milliseconds){
        long second = milliseconds / 1000;
        long minute = second / 60;
        long hour = minute / 60;
        second = second % 60;
        minute = minute % 60;
        hour = hour % 24;
        if(hour > 0) {
            return String.format("%02dh:%02dm:%02ds", hour, minute, second);
        }else if(minute > 0){
            return String.format("%02dm:%02ds", minute, second);
        }else {
            return String.format("%02ds", second);
        }
    }


}
