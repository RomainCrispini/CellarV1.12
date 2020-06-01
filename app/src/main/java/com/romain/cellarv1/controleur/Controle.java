package com.romain.cellarv1.controleur;

import android.content.Context;
import android.widget.Toast;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.modele.WineBottle;
import com.romain.cellarv1.outils.Serializer;

import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;



public class Controle {

    // Pour générer une instance de cette classe
    // Static car accessible par la classe et non pas par une instance de la classe
    private static Controle instance = null;

    private static WineBottle wineBottle;
    //private static String serializableFile = "saveWineBottle";
    private static AccesLocal accesLocal; // Permet d'accéder à la classe AccesLocal
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
            accesLocal = new AccesLocal(context);
            //wineBottle = accesLocal.getLastWineBottle();
        }
        return Controle.instance;
    }


    /**
     *  @param country
     * @param region
     * @param wineColor
     * @param domain
     * @param appellation
     * @param year
     * @param apogee
     * @param number
     * @param estimate
     * @param pictureLarge
     * @param pictureSmall
     * @param favorite
     * @param wish
     */
    public void createWineBottle(Integer id, String country, String region, String wineColor, String domain, String appellation, Integer year, Integer apogee, Integer number, Integer estimate, String pictureLarge, String pictureSmall, byte[] imageLarge, byte[] imageSmall, Float rate, String favorite, String wish, Float lattitude, Float longitude, String timeStamp, Context context) {
        wineBottle = new WineBottle(id, country, region, wineColor, domain, appellation, year, apogee, number, estimate, pictureLarge, pictureSmall, imageLarge, imageSmall, rate, favorite, wish, lattitude, longitude, timeStamp);
        accesLocal.add(wineBottle);
    }
}
