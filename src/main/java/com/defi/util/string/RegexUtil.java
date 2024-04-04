package com.defi.util.string;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtil {
    public static String regexIgnoreCase(String word){
        return new StringBuilder("(?i)").append(word).toString();
    }

    public static Set<String> wordSearchIgnoreCase(String text, String wordToFind){
        Pattern pattern = Pattern.compile("\\b" + Pattern.quote(wordToFind) + "\\b", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);
        Set<String> result = new HashSet<>();
        while (matcher.find()) {
            String match = matcher.group();
            result.add(match);
        }
        return result;
    }

    public static boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        }
        String regex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
        return email.matches(regex);
    }
    public static boolean isValidPhoneNumber(String phone) {
        if (phone == null) {
            return false;
        }
        String regex = "^0[0-9]{9}$";
        return phone.matches(regex);
    }

    public static boolean isValid(String regex, String nickname) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nickname);
        return matcher.matches();
    }
}
