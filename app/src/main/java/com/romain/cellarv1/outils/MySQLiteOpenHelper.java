package com.romain.cellarv1.outils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MySQLiteOpenHelper extends SQLiteOpenHelper {

    // Propriétés : requête de création de la BDD
    private String creation="create table bottle ("
            + "dateaddnewbottle TEXT PRIMARY KEY," // Que 4 types dispos sur SQLite, pas de format Date
            + "country TEXT,"
            + "region TEXT,"
            + "winecolor TEXT,"
            + "domain TEXT,"
            + "appellation TEXT,"
            + "year INTEGER,"
            + "apogee INTEGER,"
            + "number INTEGER,"
            + "estimate INTEGER,"
            + "image TEXT,"
            + "favorite TEXT,"
            + "wish TEXT,"
            + "random TEXT);";



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
