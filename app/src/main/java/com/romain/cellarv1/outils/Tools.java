package com.romain.cellarv1.outils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;

import androidx.recyclerview.widget.RecyclerView;

import com.romain.cellarv1.R;

import java.io.ByteArrayOutputStream;


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

}
