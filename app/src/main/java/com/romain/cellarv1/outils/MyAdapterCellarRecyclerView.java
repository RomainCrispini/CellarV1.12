package com.romain.cellarv1.outils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.modele.WineBottle;
import com.romain.cellarv1.vue.BottleActivity;
import java.util.ArrayList;


public class MyAdapterCellarRecyclerView extends RecyclerView.Adapter<MyAdapterCellarRecyclerView.CellarViewHolder> {

    private ArrayList<WineBottle> wineBottleArrayList;
    private Context mContext;
    private Dialog popupDelete, popupSuccessDelete;

    // Constructeur
    public MyAdapterCellarRecyclerView(Context mContext, ArrayList<WineBottle> arrayList) {

        wineBottleArrayList = arrayList;
        this.mContext = mContext;
        popupDelete = new Dialog(mContext);
        popupSuccessDelete = new Dialog(mContext);
    }

    public static class CellarViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageWineColor;
        public ImageView image;
        public TextView region, appellation, domain, year, apogee, number;

        public CardView cardView;
        public CardView pastilleImageBottle;

        public ImageView imageBottle;

        public CardView rateCardView;
        public TextView rateListView;

        public final ToggleButton favorite;
        public final ImageButton delete;

        public CurvedBottomNavigationView curvedBottomNavigationView;


        @SuppressLint("ResourceType")
        public CellarViewHolder(@NonNull View itemView) {
            super(itemView);
            imageWineColor = itemView.findViewById(R.id.imageWineColorListView);
            image = itemView.findViewById(R.id.imageBottleListView);
            region = itemView.findViewById(R.id.regionListView);
            appellation = itemView.findViewById(R.id.appellationListView);
            domain = itemView.findViewById(R.id.domainListView);
            year = itemView.findViewById(R.id.yearListView);
            apogee = itemView.findViewById(R.id.apogeeListView);
            number = itemView.findViewById(R.id.numberListView);

            cardView = itemView.findViewById(R.id.cardView);

            pastilleImageBottle = itemView.findViewById(R.id.pastilleImageBottle);

            imageBottle = itemView.findViewById(R.id.imageBottle);

            // Rate CardView
            rateCardView = itemView.findViewById(R.id.rateCardView);
            rateListView = itemView.findViewById(R.id.txtRateListView);

            favorite = itemView.findViewById(R.id.favorite);
            delete = itemView.findViewById(R.id.delete);


            //CurvedBottomNavigationView curvedBottomNavigationView = itemView.findViewById(R.id.curvedBottomNavigationView);

        }
    }

    @NonNull
    @Override
    public CellarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_cellar_custom_list_view, viewGroup, false);
        //CellarViewHolder cellarViewHolder = new CellarViewHolder(v);

        popupDelete.setContentView(R.layout.popup_take_out_bottle);
        popupSuccessDelete.setContentView(R.layout.popup_success_delete_bottle);

        return new CellarViewHolder(v);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final CellarViewHolder holder, final int position) {

        final WineBottle wineBottle = wineBottleArrayList.get(position);


        holder.cardView.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, BottleActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                Tools tools = new Tools();
                String date = tools.timeStampToStringDate(wineBottle.getTimeStamp());
                intent.putExtra("date", date);

                intent.putExtra("wineColor", wineBottle.getWineColor());
                intent.putExtra("imageBottleLarge", wineBottle.getPictureLarge());
                intent.putExtra("imageBottleSmall", wineBottle.getPictureSmall());
                intent.putExtra("country", wineBottle.getCountry());
                intent.putExtra("region", wineBottle.getRegion());
                intent.putExtra("domain", wineBottle.getDomain());
                intent.putExtra("appellation", wineBottle.getAppellation());
                intent.putExtra("address", wineBottle.getAddress());
                intent.putExtra("millesime", wineBottle.getYear().toString());
                intent.putExtra("apogee", wineBottle.getApogee().toString());
                intent.putExtra("estimate", wineBottle.getEstimate().toString());
                intent.putExtra("number", wineBottle.getNumber().toString());
                intent.putExtra("rate", wineBottle.getRate().toString());
                intent.putExtra("favorite", wineBottle.getFavorite());
                intent.putExtra("wish", wineBottle.getWish());

                intent.putExtra("id", wineBottle.getId().toString());


                /*
                // Tente de faire une transition visuelle vers BottleActivity (fiche vin)
                ImageView imageBottle = cellarViewHolder.imageBottle;
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(imageBottle, "imageTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, pairs);
                mContext.startActivity(intent, options.toBundle());
                 */

                mContext.startActivity(intent);
                // Pas sûr de cette méthode de transition
                ((Activity) v.getContext()).overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });


        holder.favorite.setText(null);
        holder.favorite.setTextOn(null);
        holder.favorite.setTextOff(null);
        holder.favorite.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pour set 1 dans la propriété favorite d'une bottle si elle n'est pas déjà set
                Integer valueId = wineBottle.getId();
                AccesLocal accesLocal = new AccesLocal(mContext);

                if (wineBottle.getFavorite().matches("0")) {
                    accesLocal.addLikeToABottle(String.valueOf(valueId));
                } else if(wineBottle.getFavorite().matches("1")) {
                    accesLocal.removeLikeToABottle(String.valueOf(valueId));

                }
            }
        });


        holder.delete.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageButton btnAccept = (ImageButton) popupDelete.findViewById(R.id.btnAccept);
                ImageButton btnDenie = (ImageButton) popupDelete.findViewById(R.id.btnDenie);

                ImageView imageFavorite = (ImageView) popupDelete.findViewById(R.id.imageFavorite);
                ImageView imageWish = (ImageView) popupDelete.findViewById(R.id.imageWish);

                ImageView imageWineColor = (ImageView) popupDelete.findViewById(R.id.imageWineColor);
                ImageView imageBottle = (ImageView) popupDelete.findViewById(R.id.imageBottle);
                TextView region = (TextView) popupDelete.findViewById(R.id.region);
                TextView domain = (TextView) popupDelete.findViewById(R.id.domain);
                TextView appellation = (TextView) popupDelete.findViewById(R.id.appellation);
                TextView millesime = (TextView) popupDelete.findViewById(R.id.millesime);
                TextView number = (TextView) popupDelete.findViewById(R.id.number);

                popupDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupDelete.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                popupSuccessDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupSuccessDelete.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                Tools tools = new Tools();
                imageBottle.setImageBitmap(tools.stringToBitmap(wineBottle.getPictureSmall()));

                switch(wineBottle.getWineColor().trim()) {
                    case "Rouge" :
                        imageWineColor.setImageResource(R.drawable.red_wine_listview);
                        break;
                    case "Rose" :
                        imageWineColor.setImageResource(R.drawable.rose_wine_listview);
                        break;
                    case "Blanc" :
                        imageWineColor.setImageResource(R.drawable.white_wine_listview);
                        break;
                    case "Effervescent" :
                        imageWineColor.setImageResource(R.drawable.champ_wine_listview);
                        break;
                }

                if(wineBottle.getFavorite().matches("1")) {
                    imageFavorite.setVisibility(View.VISIBLE);
                } else {
                    imageFavorite.setVisibility(View.INVISIBLE);
                }

                if(wineBottle.getWish().matches("1")) {
                    imageWish.setVisibility(View.VISIBLE);
                } else {
                    imageWish.setVisibility(View.INVISIBLE);
                }

                // On retire 1 au nombre de bouteilles
                String bottleNumber = String.valueOf(wineBottle.getNumber() - 1);
                number.setText(bottleNumber);

                region.setText(wineBottle.getRegion());
                domain.setText(wineBottle.getDomain());
                appellation.setText(wineBottle.getAppellation());
                millesime.setText((wineBottle.getYear()).toString());

                popupDelete.show();


                btnAccept.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Integer valueId = wineBottle.getId();
                        AccesLocal accesLocal = new AccesLocal(mContext);
                        accesLocal.takeOutBottle(valueId);
                        popupDelete.dismiss();

                        removeCardViewAt(holder.getAdapterPosition());

                        popupSuccessDelete.show();
                        // Permet de faire aparaitre le panneau 1 seconde sans intervention
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (popupSuccessDelete.isShowing()) {
                                    popupSuccessDelete.dismiss();
                                }
                            }
                        }, 1000);

                    }
                });

                btnDenie.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupDelete.dismiss();
                    }
                });

            }
        });

        // On applique l'animation sur la pastille de la bouteille
        holder.pastilleImageBottle.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_image_cardview));

        // On applique l'animation sur la CardView
        holder.cardView.setAnimation(AnimationUtils.loadAnimation(mContext, R.anim.slide_animation_cardview));


        // On set les infos dans le cardview layout
        WineBottle currentItem = wineBottleArrayList.get(position);








        /*

        //Tools tools = new Tools();
        ByteArrayInputStream stream = new ByteArrayInputStream(currentItem.getImageLarge(), 0, currentItem.getImageLarge().length);
        Bitmap bitmap = BitmapFactory.decodeStream(stream);
        holder.image.setImageBitmap(bitmap);
        String test = currentItem.getImageLarge().toString();
        Toast.makeText(mContext, test, Toast.LENGTH_SHORT).show();

         */



        /*
        byte[] byt = currentItem.getPictureSmall();
        Bitmap bmp = BitmapFactory.decodeByteArray(byt, 0, byt.length);
        holder.image.setImageBitmap(bmp);

         */





        // On cherche l'image la plus compressée
        byte[] decodedByte = Base64.decode(currentItem.getPictureSmall(), 0);
        //ByteArrayOutputStream stream = new ByteArrayOutputStream(currentItem.getImageLarge());
        holder.image.setImageBitmap(BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length));

        holder.region.setText(currentItem.getRegion());
        holder.appellation.setText(currentItem.getAppellation());
        holder.domain.setText(currentItem.getDomain());
        holder.year.setText(currentItem.getYear().toString());
        holder.apogee.setText(currentItem.getApogee().toString());
        holder.number.setText(currentItem.getNumber().toString());

        // On set la couleur du vin sous la pastille de l'image de l'étiquette
        switch(currentItem.getWineColor().trim()) {
            case "Rouge" :
                holder.imageWineColor.setImageResource(R.drawable.red_wine_listview);
                break;
            case "Rose" :
                holder.imageWineColor.setImageResource(R.drawable.rose_wine_listview);
                break;
            case "Blanc" :
                holder.imageWineColor.setImageResource(R.drawable.white_wine_listview);
                break;
            case "Effervescent" :
                holder.imageWineColor.setImageResource(R.drawable.champ_wine_listview);
                break;
        }

        // Si la note != 0, on set la note sur la cardView devenue visible
        if(currentItem.getRate() == 0) {
            holder.rateCardView.setVisibility(View.GONE);
        } else {
            holder.rateCardView.setVisibility(View.VISIBLE);
            holder.rateListView.setText(currentItem.getRate().toString() + "/10");
        }

        // On set la CardView d'un coeur coloré si la bouteille est favorite = 1, rien si favorite = 0
        switch(currentItem.getFavorite()) {
            case "0" :
                holder.favorite.setChecked(false);
                break;
            case "1" :
                holder.favorite.setChecked(true);
                break;
        }

    }

    private void removeCardViewAt(int position) {
        wineBottleArrayList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, wineBottleArrayList.size());
    }

    @Override
    public int getItemCount() {
        return this.wineBottleArrayList.size();
    }

}
