package com.romain.cellarv1.outils;

import java.util.regex.Pattern;


public class Validation {

    private String RegExPseudo = "^[^\\s]+,?(\\s[^\\s]+)*$";
    //private String RegExPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private String RegExPassword = "^[^\\s]+,?(\\s[^\\s]+)*$";
    private String RegExMail = "^(.+)@(.+)$";
    private String RegExTextField = "^[^\\s]+,?(\\s[^\\s]+)*$"; // Pour l'instant, même RegEx que Pseudo
    private String RegExNumberField = "^[^\\s]+,?(\\s[^\\s]+)*$"; // Pour l'instant, même RegEx que Pseudo

    // PopupRegistration & PopupConnection
    public boolean isValidPseudo(String target) {
        return Pattern.compile(RegExPseudo).matcher(target).matches();
    }

    public boolean isValidPassword(String target) {
        return Pattern.compile(RegExPassword).matcher(target).matches();

    }

    public boolean isValidMail(String target) {
        return Pattern.compile(RegExMail).matcher(target).matches();
    }

    // AddActivity
    public boolean isValidTextField(String target) {
        return Pattern.compile(RegExTextField).matcher(target).matches();
    }

    public boolean isValidNumberField(String target) {
        return Pattern.compile(RegExNumberField).matcher(target).matches();
    }

}
