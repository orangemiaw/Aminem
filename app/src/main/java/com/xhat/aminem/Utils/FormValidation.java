package com.xhat.aminem.Utils;

import java.util.regex.Pattern;

public class FormValidation {

    public static final String NPM_PATTERN = "^[0-9]{2}([.][0-9]{2}([.][0-9]{4}))?$";
    public static final String PASSWORD_PATTERN = "^[0-9]{5}$";

    public static boolean isValidNPM(String NPM){
        boolean isValid = Pattern.matches(NPM_PATTERN, NPM);
        return isValid;
    }

    public static boolean isValidPassword(String password){
//        boolean isValid = Pattern.matches(PASSWORD_PATTERN, password);
//        return isValid;
        if(password.length() < 5) {
            return false;
        }

        return true;
    }
}
