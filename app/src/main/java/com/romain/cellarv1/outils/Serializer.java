package com.romain.cellarv1.outils;

import android.content.Context;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;


public class Serializer {
    /**
     * Serialisation d'un objet, mémorisation d'un objet dans un fichier binaire
     *
     * @param filename
     * @param object
     * @param context
     */
    public static void serialize(String filename, Object object, Context context) {
        try {
            FileOutputStream file = context.openFileOutput(filename, Context.MODE_PRIVATE);
            ObjectOutputStream objectOutputStream;
            try {
                objectOutputStream = new ObjectOutputStream(file);
                objectOutputStream.writeObject(object);
                objectOutputStream.flush();
                objectOutputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Récupération de l'objet qui a été sérialisé
     *  @param filename
     * @param context
     * @return
     */
    public static Object deSerialize(String filename, Context context) {
        try {
            FileInputStream file = context.openFileInput(filename);
            ObjectInputStream objectInputStream;
            try {
                objectInputStream = new ObjectInputStream(file);
                try {
                    Object object = objectInputStream.readObject();
                    objectInputStream.close();
                    return object;
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (StreamCorruptedException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {e.printStackTrace();}
        return null;
    }
}
