package com.defi.util.string;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class StringUtil {
    public static String sha256(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(src.getBytes());
            byte[] byteData = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte byteDatum : byteData) {
                sb.append(Integer.toString((byteDatum & 0xff) + 0x100, 16).substring(1));
            }
            return sb.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public static String sha512(String src) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-512");
            byte[] hash = digest.digest(src.getBytes(StandardCharsets.UTF_8));
            StringBuilder hashPassword = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hashPassword.append('0');
                hashPassword.append(hex);
            }
            return hashPassword.toString();
        } catch (Exception exception) {
            return null;
        }
    }
    public static String md5(String src) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(src.getBytes());
            byte[] digest = md.digest();
            StringBuilder hexString = new StringBuilder();
            for (byte b : digest) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception exception) {
            return null;
        }
    }

    private static final Random random = new Random();
    private static final String digits = "0123456789";
    private static final String digits_lower_case_characters = "0123456789abcdefghijklmnopqrstuvwxyz";
    private static final String lower_case_characters = "abcdefghijklmnopqrstuvwxyz";
    private static final String upper_case_characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String lower_upper_case_characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String digits_lower_upper_case_characters = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    private static String randomString(String source, int length){
        char[] buf = new char[length];
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(source.length());
            buf[i] = source.charAt(index);
        }
        return new String(buf);
    }

    public static String randomStringDigitsLowerCaseCharacter(int length) {
        return randomString(digits_lower_case_characters, length);
    }
    public static String randomStringUpperCaseCharacter(int length) {
        return randomString(upper_case_characters, length);
    }
    public static String randomStringLowerCaseCharacter(int length) {
        return randomString(lower_case_characters, length);
    }
    public static String randomStringLowerAndUpperCaseCharacter(int length) {
        return randomString(lower_upper_case_characters, length);
    }
    public static String randomStringDigitLowerAndUpperCaseCharacter(int length) {
        return randomString(digits_lower_upper_case_characters, length);
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean endWithDigit(String query) {
        char end = query.charAt(query.length()-1);
        for(int i = 0; i < digits.length(); i++){
            char c = digits.charAt(i);
            if(c == end){
                return true;
            }
        }
        return false;
    }

    public static String formatVND(long amount) {
        Locale vietnameseLocale = new Locale("vi", "VN");
        NumberFormat format = NumberFormat.getCurrencyInstance(vietnameseLocale);
        format.setCurrency(Currency.getInstance(vietnameseLocale));
        String formattedAmount = format.format(amount);
        return formattedAmount;
    }

    public static Date convertToDate(String format, String data, Locale locale) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, locale);
        try {
            Date parsedDate = dateFormat.parse(data);
            return parsedDate;
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
    public static Long parseLong(String s) {
        try{
            return Long.parseLong(s);
        }catch (Exception e){
            return null;
        }
    }
}
