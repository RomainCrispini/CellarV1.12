package com.romain.cellarv1.controleur;

import androidx.appcompat.app.AppCompatActivity;
import java.util.regex.Pattern;


public class UserControle extends AppCompatActivity {

    private String RegExPseudo = "^[^\\s]+,?(\\s[^\\s]+)*$";
    //private String RegExPassword = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$";
    private String RegExPassword = "^[^\\s]+,?(\\s[^\\s]+)*$";
    private String RegExMail = "^(.+)@(.+)$";


    public boolean isValidPseudo(String target) {
        return Pattern.compile(RegExPseudo).matcher(target).matches();
    }

    public boolean isValidPassword(String target) {
        return Pattern.compile(RegExPassword).matcher(target).matches();

    }

    public boolean isValidMail(String target) {
        return Pattern.compile(RegExMail).matcher(target).matches();
    }

}
