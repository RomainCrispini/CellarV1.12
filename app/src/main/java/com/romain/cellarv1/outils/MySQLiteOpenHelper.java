package com.romain.cellarv1.outils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    public final String FIELD_ID = "id";
    public final String FIELD_COUNTRY = "country";
    public final String FIELD_REGION = "region";
    public final String FIELD_WINECOLOR = "winecolor";
    public final String FIELD_DOMAIN = "domain";
    public final String FIELD_APPELLATION = "appellation";
    public final String FIELD_YEAR = "year";
    public final String FIELD_APOGEE = "apogee";
    public final String FIELD_NUMBER = "number";
    public final String FIELD_ESTIMATE = "estimate";
    public final String FIELD_PICTURELARGE = "picturelarge";
    public final String FIELD_PICTURESMALL = "picturesmall";
    public final String FIELD_IMAGELARGE = "imagelarge";
    public final String FIELD_IMAGESMALL = "imagesmall";
    public final String FIELD_RATE = "rate";
    public final String FIELD_FAVORITE = "favorite";
    public final String FIELD_WISH = "wish";
    public final String FIELD_LATTITUDE = "lattitude";
    public final String FIELD_LONGITUDE = "longitude";
    public final String FIELD_TIMESTAMP = "timestamp";

    public final String TABLE_BOTTLE = "bottle";

    // Propriétés : requête de création de la BDD
    private String creation="create table " + TABLE_BOTTLE + " (" +
            FIELD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"  +// Que 4 types dispos sur SQLite, pas de format Date
            FIELD_COUNTRY + " TEXT," +
            FIELD_REGION + " TEXT," +
            FIELD_WINECOLOR + " TEXT," +
            FIELD_DOMAIN + " TEXT," +
            FIELD_APPELLATION + " TEXT," +
            FIELD_YEAR + " INTEGER," +
            FIELD_APOGEE + " INTEGER," +
            FIELD_NUMBER + " INTEGER," +
            FIELD_ESTIMATE + " INTEGER," +
            FIELD_PICTURELARGE + " TEXT," +
            FIELD_PICTURESMALL + " TEXT," +
            FIELD_IMAGELARGE + " BLOB," +
            FIELD_IMAGESMALL + " BLOB," +
            FIELD_RATE + " REAL," +
            FIELD_FAVORITE + " TEXT," +
            FIELD_WISH + " TEXT," +
            FIELD_LATTITUDE + " REAL," +
            FIELD_LONGITUDE + " REAL," +
            FIELD_TIMESTAMP + " TEXT);";



    /**
     * Constructeur
     * @param context
     * @param name
     * @param factory
     * @param version
     */
    public MySQLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    /**
     * Méthode qui ne s'exécute que si changement de BDD
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(creation);
    }

    /**
     * Méthode qui ne s'exécute que si changement de version
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + "bottle");
        onCreate(db);

    }
}
