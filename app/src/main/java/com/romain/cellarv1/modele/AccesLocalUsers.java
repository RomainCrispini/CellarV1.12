package com.romain.cellarv1.modele;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.romain.cellarv1.outils.MySQLiteOpenHelper;

public class AccesLocalUsers {

    // Propriétés
    private String nomBase = "cellar.sqlite";
    private Integer versionBase = 1;
    public MySQLiteOpenHelper accesBD; // Accès à la BDD SQLite
    private SQLiteDatabase bd; // Propriété qui permet de créer des canaux pour lire et/ou écrire dans la BDD

    /**
     * Constructeur, quand on instanciera cette classe (il faudra y envoyer le context)
     * @param context
     */
    public AccesLocalUsers(Context context) {
        accesBD = new MySQLiteOpenHelper(context, nomBase, null, versionBase);
    }

    /**
     * Ajout d'un user dans la BDD
     * @param user
     */
    public Integer addUser(User user) {

        bd = accesBD.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(accesBD.FIELD_PSEUDO, user.getPseudo());
        contentValues.put(accesBD.FIELD_PASSWORD, user.getPassword());
        contentValues.put(accesBD.FIELD_MAIL, user.getMail());
        Long idFromInsert = bd.insert(accesBD.TABLE_USER, null, contentValues);
        int id = idFromInsert.intValue();
        bd.close();

        return id;
    }

    /**
     * Méthode qui permet de récupérer le nombre total de users
     */
    public int nbTotalUsers() {
        int nbTotalUsers = 0;
        bd = accesBD.getReadableDatabase();
        String requete = "select count(pseudo) from " + accesBD.TABLE_USER;
        Cursor cursor = bd.rawQuery(requete, null);
        if(cursor.moveToFirst()) {
            nbTotalUsers = cursor.getInt(0);
        }
        while (cursor.moveToNext());
        return nbTotalUsers;
    }


}
