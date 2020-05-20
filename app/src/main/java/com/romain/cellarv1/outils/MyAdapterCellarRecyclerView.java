package com.romain.cellarv1.outils;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
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
    private Dialog popupDelete, popupSuccess;

    // Constructeur
    public MyAdapterCellarRecyclerView(Context mContext, ArrayList<WineBottle> arrayList) {

        wineBottleArrayList = arrayList;
        this.mContext = mContext;
        popupDelete = new Dialog(mContext);
        popupSuccess = new Dialog(mContext);
    }

    public static class CellarViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageWineColor;
        public ImageView image;
        public TextView region, appellation, domain, year, apogee, number;

        public CardView cardView;
        public CardView pastilleImageBottle;

        public ImageView imageBottle;

        public RatingBar ratingBarCardView;

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

            ratingBarCardView = itemView.findViewById(R.id.ratingBarCardView);

            favorite = itemView.findViewById(R.id.favorite);
            delete = itemView.findViewById(R.id.delete);


            CurvedBottomNavigationView curvedBottomNavigationView = itemView.findViewById(R.id.curvedBottomNavigationView);

        }
    }

    @NonNull
    @Override
    public CellarViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {

        View v = LayoutInflater.from(mContext).inflate(R.layout.activity_cellar_custom_list_view, viewGroup, false);
        CellarViewHolder cellarViewHolder = new CellarViewHolder(v);

        popupDelete.setContentView(R.layout.popup_take_out_bottle);
        popupSuccess.setContentView(R.layout.popup_success_update_bottle);

        return new CellarViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull final CellarViewHolder holder, final int position) {

        final WineBottle wineBottle = wineBottleArrayList.get(position);


        holder.cardView.setOnClickListener(new CardView.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO PROBLEME DE DATE
                /*
                Date date = wineBottle.getDateAddNewBottle();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
                String strDate = dateFormat.format(date);

                 */

                Intent intent = new Intent(mContext, BottleActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);

                intent.putExtra("wineColor", wineBottle.getWineColor());
                intent.putExtra("imageBottle", wineBottle.getImage());
                intent.putExtra("country", wineBottle.getCountry());
                intent.putExtra("region", wineBottle.getRegion());
                intent.putExtra("domain", wineBottle.getDomain());
                intent.putExtra("appellation", wineBottle.getAppellation());
                intent.putExtra("millesime", wineBottle.getYear().toString());
                intent.putExtra("apogee", wineBottle.getApogee().toString());
                intent.putExtra("estimate", wineBottle.getEstimate().toString());
                intent.putExtra("number", wineBottle.getNumber().toString());
                intent.putExtra("rate", wineBottle.getRate().toString());
                intent.putExtra("favorite", wineBottle.getFavorite());
                intent.putExtra("wish", wineBottle.getWish());

                intent.putExtra("random", wineBottle.getRandom());


                /*
                // Tente de faire une transition visuelle vers BottleActivity (fiche vin)
                ImageView imageBottle = cellarViewHolder.imageBottle;
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(imageBottle, "imageTransition");
                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation((Activity) mContext, pairs);
                mContext.startActivity(intent, options.toBundle());
                 */

                mContext.startActivity(intent);
            }
        });


        holder.favorite.setText(null);
        holder.favorite.setTextOn(null);
        holder.favorite.setTextOff(null);
        holder.favorite.setOnClickListener(new ToggleButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Pour set 1 dans la propriété favorite d'une bottle si elle n'est pas déjà set
                String valueRandom = wineBottle.getRandom();
                AccesLocal accesLocal = new AccesLocal(mContext);




                if (wineBottle.getFavorite().matches("0")) {
                    accesLocal.addLikeToABottle(valueRandom);

                    /*
                    // COMMENT INSTANCIER BOTTOM NAV VIEW DANS LE RECYCLER VIEW ???????????????????????????????????????????????
                    CellarActivity cellarActivity = new CellarActivity();
                    CurvedBottomNavigationView curvedBottomNavigationView = new CurvedBottomNavigationView(cellarActivity);
                    curvedBottomNavigationView.getOrCreateBadge(R.id.like).setBackgroundColor(Color.RED);

                     */

                } else if(wineBottle.getFavorite().matches("1")) {
                    accesLocal.removeLikeToABottle(valueRandom);

                }



                /*
                ArrayList<WineBottle> wineBottleArrayList = (ArrayList<WineBottle>) accesLocal.recoverWineBottleList();
                MyAdapterCellarRecyclerView myAdapterCellarRecyclerView = new MyAdapterCellarRecyclerView(mContext, wineBottleArrayList);
                myAdapterCellarRecyclerView.notifyDataSetChanged();


                mRecyclerView.setHasFixedSize(true);


                //mLayoutManager = new LinearLayoutManager(getContext());
                //mRecyclerView.setLayoutManager(mLayoutManager);

                mAdapter = new MyAdapterCellarRecyclerView(getContext(), wineBottleArrayList);

                mRecyclerView.setAdapter(mAdapter);

                 */





            }
        });


        holder.delete.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btnAccept = (Button) popupDelete.findViewById(R.id.btnAccept);
                Button btnDenie = (Button) popupDelete.findViewById(R.id.btnDenie);

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
                popupSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                popupSuccess.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

                Tools tools = new Tools();
                imageBottle.setImageBitmap(tools.stringToBitmap(wineBottle.getImage()));
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
                        String valueRandom = wineBottle.getRandom();
                        AccesLocal accesLocal = new AccesLocal(mContext);
                        accesLocal.takeOutBottle(valueRandom);
                        popupDelete.dismiss();

                        popupSuccess.show();
                        // Permet de faire aparaitre le panneau 2 secondes sans interventions
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (popupSuccess.isShowing()) {
                                    popupSuccess.dismiss();
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

        Tools tools = new Tools();
        holder.image.setImageBitmap(tools.stringToBitmap(currentItem.getImage()));

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

        // On applique la note sur la cardView
        holder.ratingBarCardView.setRating(currentItem.getRate());

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

    @Override
    public int getItemCount() {
        return this.wineBottleArrayList.size();
    }

}
