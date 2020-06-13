package com.romain.cellarv1.modele;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.romain.cellarv1.outils.MySQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;


public class AccesLocalDbCellar {

    // Propriétés
    private String nomBase = "cellar.sqlite";
    private Integer versionBase = 1;
    public MySQLiteOpenHelper accesBD; // Accès à la BDD SQLite
    private SQLiteDatabase bd; // Propriété qui permet de créer des canaux pour lire et/ou écrire dans la BDD

    /**
     * Constructeur, quand on instanciera cette classe (il faudra y envoyer le context)
     * @param context
     */
    public AccesLocalDbCellar(Context context) {
        accesBD = new MySQLiteOpenHelper(context, nomBase, null, versionBase);
    }


    /**
     * Ajout d'une bouteille dans la BDD
     * @param wineBottle
     */
    public Integer add(WineBottle wineBottle) {

        bd = accesBD.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(accesBD.FIELD_COUNTRY, wineBottle.getCountry());
        contentValues.put(accesBD.FIELD_REGION, wineBottle.getRegion());
        contentValues.put(accesBD.FIELD_WINECOLOR, wineBottle.getWineColor());
        contentValues.put(accesBD.FIELD_DOMAIN, wineBottle.getDomain());
        contentValues.put(accesBD.FIELD_APPELLATION, wineBottle.getAppellation());
        contentValues.put(accesBD.FIELD_ADDRESS, wineBottle.getAddress());
        contentValues.put(accesBD.FIELD_YEAR, wineBottle.getYear());
        contentValues.put(accesBD.FIELD_APOGEE, wineBottle.getApogee());
        contentValues.put(accesBD.FIELD_NUMBER, wineBottle.getNumber());
        contentValues.put(accesBD.FIELD_ESTIMATE, wineBottle.getEstimate());
        contentValues.put(accesBD.FIELD_PICTURELARGE, wineBottle.getPictureLarge());
        contentValues.put(accesBD.FIELD_PICTURESMALL, wineBottle.getPictureSmall());
        contentValues.put(accesBD.FIELD_IMAGELARGE, wineBottle.getImageLarge());
        contentValues.put(accesBD.FIELD_IMAGESMALL, wineBottle.getImageSmall());
        contentValues.put(accesBD.FIELD_RATE, wineBottle.getRate());
        contentValues.put(accesBD.FIELD_FAVORITE, wineBottle.getFavorite());
        contentValues.put(accesBD.FIELD_WISH, wineBottle.getWish());
        contentValues.put(accesBD.FIELD_LATTITUDE, wineBottle.getLattitude());
        contentValues.put(accesBD.FIELD_LONGITUDE, wineBottle.getLongitude());
        contentValues.put(accesBD.FIELD_TIMESTAMP, wineBottle.getTimeStamp());
        Long idFromInsert = bd.insert(accesBD.TABLE_BOTTLE, null, contentValues);
        int id = idFromInsert.intValue();
        bd.close();

        return id;
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
        String requete = "select sum(number) from bottle where winecolor = 'Rouge'";
        Cursor cursor = bd.rawQuery(requete, null);
        if(cursor.moveToFirst()) {
            nbRed = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        cursor.close();
        return nbRed;
    }

    public int nbRose() {
        int nbRose = 0;
        bd = accesBD.getReadableDatabase();
        String requete = "select sum(number) from bottle where winecolor = 'Rose'";
        Cursor cursor = bd.rawQuery(requete, null);
        if(cursor.moveToFirst()) {
            nbRose = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        cursor.close();
        bd.close();
        return nbRose;
    }

    public int nbWhite() {
        int nbWhite = 0;
        bd = accesBD.getReadableDatabase();
        String requete = "select sum(number) from bottle where winecolor = 'Blanc'";
        Cursor cursor = bd.rawQuery(requete, null);
        if(cursor.moveToFirst()) {
            nbWhite = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        cursor.close();
        bd.close();
        return nbWhite;
    }

    public int nbChamp() {
        int nbChamp = 0;
        bd = accesBD.getReadableDatabase();
        String requete = "select sum(number) from bottle where winecolor = 'Effervescent'";
        Cursor cursor = bd.rawQuery(requete, null);
        if(cursor.moveToFirst()) {
            nbChamp = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        cursor.close();
        bd.close();
        return nbChamp;
    }

    /**
     * Méthode qui permet de sortir une bouteille suivant son id
     */
    public void takeOutBottle(Integer id) {
        bd = accesBD.getWritableDatabase();
        bd.delete("bottle", "id = ?", new String[] { String.valueOf(id) });

        bd.close();
    }

    /**
     * Méthode qui permet d'ajouter un like à une bouteille suivant son id
     */
    public void addLikeToABottle(String id) {
        bd = accesBD.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("favorite", "1");
        String where = "id = ?";
        String[] whereArgs = new String[] {String.valueOf(id)};
        bd.update("bottle", args, where, whereArgs);

        bd.close();
    }

    /**
     * Méthode qui permet de retirer in like à une bouteille
     */
    public void removeLikeToABottle(String id) {
        bd = accesBD.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("favorite", "0");
        String where = "id = ?";
        String[] whereArgs = new String[] {String.valueOf(id)};
        bd.update("bottle", args, where, whereArgs);

        bd.close();
    }

    /**
     * Méthode qui permet d'upload les infos d'une bouteille
     */
    public void updateBottle(Integer id, String country, String region, String domain, String appellation, String address, int year, int apogee, int number, int estimate, Integer rate, String favorite, String wish) {
        bd = accesBD.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put("country", country);
        args.put("region", region);
        args.put("domain", domain);
        args.put("appellation", appellation);
        args.put("address", address);
        args.put("year", year);
        args.put("apogee", apogee);
        args.put("number", number);
        args.put("estimate", estimate);
        args.put("rate", rate);
        args.put("favorite", favorite);
        args.put("wish", wish);
        bd.update("bottle", args, "id = " + id, null);

        bd.close();
    }

    public Boolean doesDBExists() {
        bd = accesBD.getReadableDatabase();
        Cursor cursor = bd.rawQuery("select * from " + accesBD.TABLE_BOTTLE, null);
        Boolean dbExists;
        if(cursor.moveToFirst()) {
            dbExists = true;
        } else {
            dbExists = false;
        }
        return dbExists;
    }

    /**
     * Récupération de la liste des bouteilles enregistrées dans le cellier
     * @return Liste exhaustive des bouteilles de vin
     */
    public List<WineBottle> recoverWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>(); ////////////////////// Affiche des crochets et des virgules avec sa méthode toString()
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select * from " + accesBD.TABLE_BOTTLE + " order by " + accesBD.FIELD_ID + " desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {

            int id = cursor.getInt(0);
            String country = cursor.getString(1);
            String region = cursor.getString(2);
            String winecolor = cursor.getString(3);
            String domain = cursor.getString(4);
            String appellation = cursor.getString(5);
            String address = cursor.getString(6);
            Integer year = cursor.getInt(7);
            Integer apogee = cursor.getInt(8);
            Integer number = cursor.getInt(9);
            Integer estimate = cursor.getInt(10);
            String picturelarge = cursor.getString(11);
            String picturesmall = cursor.getString(12);
            byte[] imagelarge = cursor.getBlob(13);
            byte[] imagesmall = cursor.getBlob(14);
            Integer rate = cursor.getInt(15);
            String favorite = cursor.getString(16);
            String wish = cursor.getString(17);
            Float latitude = cursor.getFloat(18);
            Float longitude = cursor.getFloat(19);
            String timestamp = cursor.getString(20);
            wineBottle = new WineBottle(id, country, region, winecolor, domain, appellation, address, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, latitude, longitude, timestamp);
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
    public List<WineBottle> recoverWineBottleListNumberGroupByYear() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        WineBottle wineBottle;
        String requete = "select id, year, sum(number), sum(estimate) from bottle group by year";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            Integer id = cursor.getInt(0);
            Integer year = cursor.getInt(1);
            Integer number = cursor.getInt(2);
            Integer estimate = cursor.getInt(3);
            wineBottle = new WineBottle(id, year, number, estimate);
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
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle where favorite = '1' order by id desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    /**
     * Récupération de la liste des bouteilles enregistrées dans le cellier
     * @return Liste des bouteilles de vin notées par ordre décroissant
     */
    public List<WineBottle> recoverRateWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        // On sort de la liste les bouteilles dont la note est 0
        String requete = "select * from bottle where rate <> 0 order by rate desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> recoverWishWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle where wish = '1' order by id desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> recoverSearchWineBottleList(String searchWord) {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle where (country like '%" + searchWord + "%') or (region like '%" + searchWord + "%') or (domain like '%" + searchWord + "%') or (appellation like '%" + searchWord + "%') or (year like '%" + searchWord + "%') or (apogee like '%" + searchWord + "%') order by id desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortMapWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle order by region asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortColorWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle order by winecolor desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortYearWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle order by year asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortApogeeWineBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle order by apogee desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortMapLikeBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle where favorite = '1' order by region asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortColorLikeBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle where favorite = '1' order by winecolor desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortYearLikeBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle where favorite = '1' order by year asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortApogeeLikeBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle where favorite = '1' order by apogee desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortMapWishBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle where wish = '1' order by region asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortColorWishBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle where wish = '1' order by winecolor desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortYearWishBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle where wish = '1' order by year asc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            wineBottleList.add(this.cursorToWineBottle(cursor));
            cursor.moveToNext();
        }
        cursor.close();
        bd.close();
        return wineBottleList;
    }

    public List<WineBottle> sortApogeeWishBottleList() {
        List<WineBottle> wineBottleList = new ArrayList<>();
        bd = accesBD.getReadableDatabase();
        String requete = "select * from bottle where wish = '1' order by apogee desc";
        Cursor cursor = bd.rawQuery(requete, null);
        cursor.moveToFirst();

        cursor.close();
        bd.close();
        return wineBottleList;
    }

    private WineBottle cursorToWineBottle (Cursor cursor){
        WineBottle wineBottle;

        Integer id = cursor.getInt(0);
        String country = cursor.getString(1);
        String region = cursor.getString(2);
        String winecolor = cursor.getString(3);
        String domain = cursor.getString(4);
        String appellation = cursor.getString(5);
        String address = cursor.getString(6);
        Integer year = cursor.getInt(7);
        Integer apogee = cursor.getInt(8);
        Integer number = cursor.getInt(9);
        Integer estimate = cursor.getInt(10);
        String picturelarge = cursor.getString(11);
        String picturesmall = cursor.getString(12);
        byte[] imagelarge = cursor.getBlob(13);
        byte[] imagesmall = cursor.getBlob(14);
        Integer rate = cursor.getInt(15);
        String favorite = cursor.getString(16);
        String wish = cursor.getString(17);
        Float latitude = cursor.getFloat(18);
        Float longitude = cursor.getFloat(19);
        String timestamp = cursor.getString(20);
        wineBottle = new WineBottle(id, country, region, winecolor, domain, appellation, address, year, apogee, number, estimate, picturelarge, picturesmall, imagelarge, imagesmall, rate, favorite, wish, latitude, longitude, timestamp);
        cursor.moveToNext();

        return wineBottle;
    }
}
