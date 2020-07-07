package com.romain.cellarv1.controleur;

import android.content.Context;

import com.romain.cellarv1.modele.AccesLocalCellar;
import com.romain.cellarv1.modele.WineBottle;

import java.util.ArrayList;


public class Controle {

    // Pour générer une instance de cette classe
    // Static car accessible par la classe et non pas par une instance de la classe
    private static Controle instance = null;

    private static WineBottle wineBottle;
    //private static String serializableFile = "saveWineBottle";
    private static AccesLocalCellar accesLocalCellar; // Permet d'accéder à la classe AccesLocal
    private static ArrayList<WineBottle> wineBottleList;

    /**
     * On créé un constructeur privé de manière à ne pas pouvoir en créer de nouveaux avec new
     */
    private Controle() {
        super();
    }

    /**
     * Pattern Singleton qui permet de ne créer qu'une seule instance de cette classe et retourne un objet de type Controle
     * @return instance
     */
    // Pattern Singleton qui permet de ne créer qu'une seule instance de cette classe et retourne un objet de type Controle
    public static final Controle getInstance(Context context) {
        if(Controle.instance == null) {
            Controle.instance = new Controle();
            //recoverSerialize(context);
            accesLocalCellar = new AccesLocalCellar(context);
            //wineBottle = accesLocal.getLastWineBottle();
        }
        return Controle.instance;
    }


    /**
     * @param id
     * @param country
     * @param region
     * @param wineColor
     * @param domain
     * @param appellation
     * @param address
     * @param year
     * @param apogee
     * @param number
     * @param estimate
     * @param pictureLarge
     * @param pictureSmall
     * @param imageLarge
     * @param imageSmall
     * @param rate
     * @param favorite
     * @param wish
     * @param latitude
     * @param longitude
     * @param timeStamp
     * @param context
     */
    public void createWineBottle(Integer id, String country, String region, String wineColor, String domain, String appellation, String address, Integer year, Integer apogee, Integer number, Integer estimate, String pictureLarge, String pictureSmall, byte[] imageLarge, byte[] imageSmall, Integer rate, String favorite, String wish, Double latitude, Double longitude, String timeStamp, Context context) {
        wineBottle = new WineBottle(id, country, region, wineColor, domain, appellation, address, year, apogee, number, estimate, pictureLarge, pictureSmall, imageLarge, imageSmall, rate, favorite, wish, latitude, longitude, timeStamp);
        accesLocalCellar.add(wineBottle);
    }
}
