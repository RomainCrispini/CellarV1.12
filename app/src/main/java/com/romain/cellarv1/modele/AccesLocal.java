package com.romain.cellarv1.modele;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.romain.cellarv1.outils.MySQLiteOpenHelper;

import java.sql.Blob;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class AccesLocal {

    // Propriétés
    private String nomBase = "cellar.sqlite";
    private Integer versionBase = 1;
    private MySQLiteOpenHelper accesBD; // Accès à la BDD SQLite
    private SQLiteDatabase bd; // Propriété qui permet de créer des canaux pour lire et/ou écrire dans la BDD

    /**
     * Constructeur, quand on instanciera cette classe (il faudra y envoyer le context)
     * @param context
     */
    public AccesLocal(Context context) {
        accesBD = new MySQLiteOpenHelper(context, nomBase, null, versionBase);
    }


    /**
     * Ajout d'une bouteille dans la BDD
     * @param wineBottle
     */
    public void add(WineBottle wineBottle) {
        bd = accesBD.getWritableDatabase();
        String requete = "insert into bottle (dateaddnewbottle, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random) values ";
        requete += "(\""+ wineBottle.getDateAddNewBottle() +"\", \""+wineBottle.getCountry()+"\", \""+wineBottle.getRegion()+"\", \""+wineBottle.getWineColor()+" \", \""+wineBottle.getDomain()+"\", \""+wineBottle.getAppellation()+"\", "+wineBottle.getYear()+", "+wineBottle.getApogee()+", "+wineBottle.getNumber()+", "+wineBottle.getEstimate()+", \""+wineBottle.getPictureLarge()+"\", \""+wineBottle.getPictureSmall()+"\", \""+wineBottle.getImageLarge()+"\", \""+ wineBottle.getImageSmall() +"\", "+wineBottle.getRate()+", \""+wineBottle.getFavorite()+"\", \""+wineBottle.getWish()+"\", "+wineBottle.getLattitude()+", "+wineBottle.getLongitude()+", \""+wineBottle.getTimeStamp()+"\", \""+wineBottle.getRandom()+"\")";
        bd.execSQL(requete);
        bd.close();
    }

    /**
     * Méthode qui permet de récupérer le nombre total de bouteilles
     */
    public int nbTotal() {
        int nbTotal = 0;
        bd = accesBD.getReadableDatabase();
        String requete = "select sum(number) from bottle";
        Cursor cursor = bd.rawQuery(requete, null);
        if(cursor.moveToFirst()) {
            nbTotal = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return nbTotal;
    }

    /**
     * Méthode qui permet de récupérer le montant total des bouteilles
     */
    public int nbTotalEstimate() {
        int nbTotalEstimte = 0;
        bd = accesBD.getReadableDatabase();
        String requete = "select sum(estimate) from bottle";
        Cursor cursor = bd.rawQuery(requete, null);
        if(cursor.moveToFirst()) {
            nbTotalEstimte = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return nbTotalEstimte;
    }

    /**
     * 4 méthodes qui permettent de récupérer le nombre de bouteilles par couleur
     */
    public int nbRed() {
        int nbRed = 0;
        bd = accesBD.getReadableDatabase();
        String requete = "select sum(number) from bottle where winecolor = 'Rouge '";
        Cursor cursor = bd.rawQuery(requete, null);
        if(cursor.moveToFirst()) {
            nbRed = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return nbRed;
    }

    public int nbRose() {
        int nbRose = 0;
        bd = accesBD.getReadableDatabase();
        String requete = "select sum(number) from bottle where winecolor = 'Rose '";
        Cursor cursor = bd.rawQuery(requete, null);
        if(cursor.moveToFirst()) {
            nbRose = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return nbRose;
    }

    public int nbWhite() {
        int nbWhite = 0;
        bd = accesBD.getReadableDatabase();
        String requete = "select sum(number) from bottle where winecolor = 'Blanc '";
        Cursor cursor = bd.rawQuery(requete, null);
        if(cursor.moveToFirst()) {
            nbWhite = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return nbWhite;
    }

    public int nbChamp() {
        int nbChamp = 0;
        bd = accesBD.getReadableDatabase();
        String requete = "select sum(number) from bottle where winecolor = 'Effervescent '";
        Cursor cursor = bd.rawQuery(requete, null);
        if(cursor.moveToFirst()) {
            nbChamp = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return nbChamp;
    }

    /**
     * Méthode qui permet de sortir une bouteille
     */
    public void takeOutBottle(String random) {
        bd = accesBD.getWritableDatabase();
        bd.delete("bottle", "random = ?", new String[] { random });
        bd.close();
    }

    /**
     * Méthode qui permet d'ajouter un like à une bouteille
     */
    public void addLikeToABottle(String random) {
        bd = accesBD.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("favorite", "1");
        String where = "random=?";
        String[] whereArgs = new String[] {String.valueOf(random)};
        bd.update("bottle", args, where, whereArgs);

        //bd.execSQL(requete);
        bd.close();
    }

    /**
     * Méthode qui permet de retirer in like à une bouteille
     */
    public void removeLikeToABottle(String random) {
        bd = accesBD.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("favorite", "0");
        String where = "random=?";
        String[] whereArgs = new String[] {String.valueOf(random)};
        bd.update("bottle", args, where, whereArgs);

        //bd.execSQL(requete);
        bd.close();
    }

    /**
     * Méthode qui permet d'upload les infos d'une bouteille
     */
    public void updateBottle(String random, String country, String region, String domain, String appellation, int year, int apogee, int number, int estimate, Float rate, String favorite, String wish) {
        bd = accesBD.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("country", country);
        args.put("region", region);
        args.put("domain", domain);
        args.put("appellation", appellation);
        args.put("year", year);
        args.put("apogee", apogee);
        args.put("number", number);
        args.put("estimate", estimate);
        args.put("rate", rate);
        args.put("favorite", favorite);
        args.put("wish", wish);

        String where = "random=?";
        bd.update("bottle", args, "random=" + random, null);

    }

    /**
     * Récupération de la liste des bouteilles enregistrées dans le cellier
     * @return Liste exhaustive des bouteilles de vin
     */
    public List<WineBottle> recoverWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle order by dateaddnewbottle desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    /**
     * Récupération de la liste des bouteilles enregistrées dans le cellier
     * @return Liste exhaustive des bouteilles de vin group by year avec somme du nombre et de l'estimation
     */
    public List<WineBottle> recoverWineBottleListOrderByYear() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select year, sum(number), sum(estimate), random from bottle group by year";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Integer year = cursor.getInt(0);
            Integer number = cursor.getInt(1);
            Integer estimate = cursor.getInt(2);
            String random = cursor.getString(3);
            wineBottle = new WineBottle(year, number, estimate, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    /**
     * Récupération de la liste des bouteilles enregistrées dans le cellier
     * @return Liste exhaustive des bouteilles de vin dont favorite = 1
     */
    public List<WineBottle> recoverLikeWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle where favorite = '1'";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    /**
     * Récupération de la liste des bouteilles enregistrées dans le cellier
     * @return Liste exhaustive des bouteilles de vin par ordre décroissant de note
     */
    public List<WineBottle> recoverRateWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle order by rate desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> recoverWishWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle where wish = '1'";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> recoverSearchWineBottleList(String searchWord) {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle where (country like '%" + searchWord + "%') or (region like '%" + searchWord + "%') or (domain like '%" + searchWord + "%') or (appellation like '%" + searchWord + "%') or (year like '%" + searchWord + "%') or (apogee like '%" + searchWord + "%')";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortMapWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle order by region asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortColorWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle order by winecolor desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortYearWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle order by year asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortApogeeWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle order by apogee desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortMapLikeBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle where favorite = '1' order by region asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortColorLikeBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle where favorite = '1' order by winecolor desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortYearLikeBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle where favorite = '1' order by year asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortApogeeLikeBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle where favorite = '1' order by apogee desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortMapWishBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle where wish = '1' order by region asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortColorWishBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle where wish = '1' order by winecolor desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortYearWishBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle where wish = '1' order by year asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortApogeeWishBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from bottle where wish = '1' order by apogee desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
            wineBottleList.add(wineBottle);
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    /**
     * Récupération de la dernière bouteille de la BDD
     * @return wineBottle
     */
    public WineBottle getLastWineBottle() {
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle = null;
        String requete = "select * from bottle";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToLast();
        if(!cursor.isAfterLast()) {
            Date date = new Date();
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            Integer year = cursor.getInt(6);
            Integer apogee = cursor.getInt(7);
            Integer number = cursor.getInt(8);
            Integer estimate = cursor.getInt(9);
            String picturelarge = cursor.getString(10);
            String picturesmall = cursor.getString(11);
            byte[] imagelarge = cursor.getBlob(12);
            byte[] imagesmall = cursor.getBlob(13);
            Float rate = cursor.getFloat(14);
            String favorite = cursor.getString(15);
            String wish = cursor.getString(16);
            Float lattitude = cursor.getFloat(17);
            Float longitude = cursor.getFloat(18);
            String timestamp = cursor.getString(19);
            String random = cursor.getString(20);
            wineBottle = new WineBottle(date, country, region, winecolor, domain, appellation, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, lattitude, longitude, timestamp, random);
        }
        cursor.close();
        bd.close();
        return wineBottle;
    }
}
