package com.romain.cellarv1.outils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.recyclerview.widget.RecyclerView;

import com.romain.cellarv1.R;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Tools {

    public String bitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 70, stream);
        byte[] bute_arr = stream.toByteArray();
        return Base64.encodeToString(bute_arr, 0);
    }

    public Bitmap stringToBitmap(String picture) {
        Bitmap bitmap = null;
        try {
            byte[] decodeString = Base64.decode(picture, Base64.DEFAULT);
            bitmap = BitmapFactory.decodeByteArray(decodeString, 0, decodeString.length);
        } catch(Exception e) {
            Log.i("Bug", "texte non convertible");
        }
        return bitmap;
    }

    public Bitmap ByteToBitmap(byte[] byteArray) {
        ByteArrayInputStream arrayInputStream = new ByteArrayInputStream(byteArray);
        Bitmap bitmap = BitmapFactory.decodeStream(arrayInputStream);
        return bitmap;
    }

    /**
     * Permet de récupérer la date formattée
     * @param timeStamp
     * @return
     */
    public String timeStampToStringDate(String timeStamp) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        String dateString = formatter.format(new Date(Long.parseLong(timeStamp)));
        return dateString;
    }

    /**
     * Permet de redimensionner une image à 70px de large
     * @param bm
     * @return
     */
    public Bitmap getResizedBitmap100px(Bitmap bm) {

        float aspectRatio = bm.getWidth() /
                (float) bm.getHeight();
        int width = 70;
        int height = Math.round(width / aspectRatio);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, width, height, false);
        return resizedBitmap;
    }

    /**
     * Permet de redimensionner une image à 1000px de large
     * @param bm
     * @return
     */
    public Bitmap getResizedBitmap500px(Bitmap bm) {

        float aspectRatio = bm.getWidth() /
                (float) bm.getHeight();
        int width = 500;
        int height = Math.round(width / aspectRatio);

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bm, width, height, false);
        return resizedBitmap;
    }





}
