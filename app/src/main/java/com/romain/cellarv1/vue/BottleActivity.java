package com.romain.cellarv1.vue;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.modele.WineBottle;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.Tools;

public class BottleActivity extends AppCompatActivity {

    // Déclaration de la Custom FAB et de ses caractéristiques
    private FloatingActionButton fabWineMenu, fabRed, fabRose, fabWhite, fabChamp;
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private Boolean isFABWineMenuOpen = false;

    // Déclaration des champs texte et des ImageView
    private TextView dateBottle;
    private EditText countryBottle, regionBottle, domainBottle, appellationBottle;
    private EditText millesimeBottle, apogeeBottle, estimateBottle, numberBottle;
    private ImageView imageBottle, imageWineColor;

    // RatingBar
    RatingBar ratingBar;

    // Button Update
    private Button btnUpdateBottle;

    // Button Delete
    private Button btnDeleteBottle;

    // Buttons MenuBis
    private ToggleButton btnFavorite;
    private ToggleButton btnWishlist;
    private ImageButton btnBackMap1;
    private ImageButton btnBackMap2;

    // Déclaration des PopupUpdate et PopupDelete
    private Dialog popupUpdate, popupDelete, popupSuccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottle);
        init();
        btnUpdateBottle();
        btnDeleteBottle();


    }

    private void init() {

        initCurvedNavigationView();
        initFabWineMenu();
        getFabWineMenuValue();
        initWineBottle();

        btnUpdateBottle = (Button) findViewById(R.id.btnUpdateBottle);
        btnDeleteBottle = (Button) findViewById(R.id.btnDeleteBottle);

        // Je n'ai pas trouvé d'autres moyens pour rendre toute la surface clickable
        btnBackMap1 = (ImageButton) findViewById(R.id.btnBackMap1);
        btnBackMap2 = (ImageButton) findViewById(R.id.btnBackMap2);
        btnBackMap1.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        btnBackMap2.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        FrameLayout menuBis = (FrameLayout) findViewById(R.id.menuBis);
        menuBis.setTranslationY(300f);
        menuBis.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

        popupUpdate = new Dialog(this);
        popupUpdate.setContentView(R.layout.popup_update_bottle);
        popupUpdate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupUpdate.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        popupDelete = new Dialog(this);
        popupDelete.setContentView(R.layout.popup_take_out_bottle);
        popupDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupDelete.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        popupSuccess = new Dialog(this);
        popupSuccess.setContentView(R.layout.popup_success_update_bottle);
        popupSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupSuccess.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

    }

    private void btnUpdateBottle() {
        btnUpdateBottle.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btnAccept = (Button) popupUpdate.findViewById(R.id.btnAccept);
                Button btnDenie = (Button) popupUpdate.findViewById(R.id.btnDenie);

                ImageView imageFavorite = (ImageView) popupUpdate.findViewById(R.id.imageFavorite);
                ImageView imageWish = (ImageView) popupUpdate.findViewById(R.id.imageWish);

                ImageView imageWineColor = (ImageView) popupUpdate.findViewById(R.id.imageWineColor);
                ImageView imageBottle = (ImageView) popupUpdate.findViewById(R.id.imageBottle);
                TextView region = (TextView) popupUpdate.findViewById(R.id.region);
                TextView domain = (TextView) popupUpdate.findViewById(R.id.domain);
                TextView appellation = (TextView) popupUpdate.findViewById(R.id.appellation);
                TextView millesime = (TextView) popupUpdate.findViewById(R.id.millesime);

                switch(getIntent().getStringExtra("wineColor").trim()) {
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

                String image = getIntent().getStringExtra("imageBottleSmall");
                Tools tools = new Tools();
                imageBottle.setImageBitmap(tools.stringToBitmap(image));

                final RatingBar ratingBarPopUp = (RatingBar) popupUpdate.findViewById(R.id.ratingBarPopUp);
                Float ratePopUp = ratingBar.getRating();
                ratingBarPopUp.setRating(ratePopUp);

                if(btnFavorite.isChecked()) {
                    imageFavorite.setVisibility(View.VISIBLE);
                } else {
                    imageFavorite.setVisibility(View.INVISIBLE);
                }

                if(btnWishlist.isChecked()) {
                    imageWish.setVisibility(View.VISIBLE);
                } else {
                    imageWish.setVisibility(View.INVISIBLE);
                }

                region.setText(regionBottle.getText());
                domain.setText(domainBottle.getText());
                appellation.setText(appellationBottle.getText());
                millesime.setText(millesimeBottle.getText());


                popupUpdate.show();

                btnAccept.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Conversion de l'id à la récupération de l'intent
                        String strId = getIntent().getStringExtra("id");
                        Integer intId = Integer.parseInt(strId);

                        String strCountry = countryBottle.getText().toString();
                        String strRegion = regionBottle.getText().toString();
                        String strDomain = domainBottle.getText().toString();
                        String strAppellation = appellationBottle.getText().toString();
                        Integer intMillesime = Integer.parseInt(millesimeBottle.getText().toString());
                        Integer intApogee = Integer.parseInt(apogeeBottle.getText().toString());
                        Integer intNumber = Integer.parseInt(numberBottle.getText().toString());
                        Integer intEstimate = Integer.parseInt(estimateBottle.getText().toString());

                        Float floatRate = ratingBar.getRating();

                        String strFavorite;
                        if(btnFavorite.isChecked()) {
                            strFavorite = "1";
                        } else {
                            strFavorite = "0";
                        }

                        String strWish;
                        if(btnWishlist.isChecked()) {
                            strWish = "1";
                        } else {
                            strWish = "0";
                        }

                        AccesLocal accesLocal = new AccesLocal(BottleActivity.this);
                        accesLocal.updateBottle(intId, strCountry, strRegion, strDomain, strAppellation, intMillesime, intApogee, intNumber, intEstimate, floatRate, strFavorite, strWish);
                        popupUpdate.dismiss();

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
                        popupUpdate.dismiss();
                    }
                });
            }
        });
    }

    private void btnDeleteBottle() {
        btnDeleteBottle.setOnClickListener(new Button.OnClickListener() {
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

                switch(getIntent().getStringExtra("wineColor").trim()) {
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

                String image = getIntent().getStringExtra("imageBottleSmall");
                Tools tools = new Tools();
                imageBottle.setImageBitmap(tools.stringToBitmap(image));

                RatingBar ratingBarPopUp = (RatingBar) popupDelete.findViewById(R.id.ratingBarPopUp);
                Float ratePopUp = ratingBar.getRating();
                ratingBarPopUp.setRating(ratePopUp);

                if(btnFavorite.isChecked()) {
                    imageFavorite.setVisibility(View.VISIBLE);
                } else {
                    imageFavorite.setVisibility(View.INVISIBLE);
                }

                if(btnWishlist.isChecked()) {
                    imageWish.setVisibility(View.VISIBLE);
                } else {
                    imageWish.setVisibility(View.INVISIBLE);
                }

                region.setText(regionBottle.getText());
                domain.setText(domainBottle.getText());
                appellation.setText(appellationBottle.getText());
                millesime.setText(millesimeBottle.getText());

                // Affichage du décompte du nombre de bouteilles restantes
                Integer intNumber = Integer.parseInt(numberBottle.getText().toString()) - 1;
                number.setText(intNumber.toString());

                popupDelete.show();

                btnAccept.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Conversion de l'id à la récupération de l'intent
                        String strId = getIntent().getStringExtra("id");
                        Integer intId = Integer.parseInt(strId);

                        AccesLocal accesLocal = new AccesLocal(BottleActivity.this);
                        accesLocal.takeOutBottle(intId);
                        popupDelete.dismiss();

                        // Basculement vers CellarActivity à l'effacement d'une bouteille dans le BottleActivity
                        Intent intent = new Intent(BottleActivity.this, CellarActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

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
    }

    private void initWineBottle() {
        btnFavorite = (ToggleButton) findViewById(R.id.btnFavorite);
        btnWishlist = (ToggleButton) findViewById(R.id.btnWishlist);

        imageBottle = (ImageView) findViewById(R.id.imageBottle);
        imageWineColor = (ImageView) findViewById(R.id.imageWineColor);

        dateBottle = (TextView) findViewById(R.id.date);

        countryBottle = (EditText) findViewById(R.id.countryBottle);
        regionBottle = (EditText) findViewById(R.id.regionBottle);
        domainBottle = (EditText) findViewById(R.id.domainBottle);
        appellationBottle = (EditText) findViewById(R.id.appellationBottle);
        millesimeBottle = (EditText) findViewById(R.id.millesimeBottle);
        apogeeBottle = (EditText) findViewById(R.id.apogeeBottle);
        estimateBottle = (EditText) findViewById(R.id.estimateBottle);
        numberBottle = (EditText) findViewById(R.id.numberBottle);

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);

        switch(getIntent().getStringExtra("wineColor").trim()) {
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


        // On cherche l'image la moins compressée
        String image = getIntent().getStringExtra("imageBottleLarge");
        //Tools tools = new Tools();
        //imageBottle.setImageBitmap(tools.stringToBitmap(image));
        byte[] decodedByte = Base64.decode(image, 0);
        //return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        //byte[] image = currentItem.getImage();
        //ByteArrayOutputStream stream = new ByteArrayOutputStream(currentItem.getImageLarge());
        imageBottle.setImageBitmap(BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length));




        String rate = getIntent().getStringExtra("rate");
        Float floatRate = Float.valueOf(rate);
        ratingBar.setRating(floatRate);

        btnFavorite.setText(null);
        btnFavorite.setTextOn(null);
        btnFavorite.setTextOff(null);
        switch(getIntent().getStringExtra("favorite")) {
            case "0" :
                btnFavorite.setChecked(false);
                break;
            case "1" :
                btnFavorite.setChecked(true);
                break;
        }

        btnWishlist.setText(null);
        btnWishlist.setTextOn(null);
        btnWishlist.setTextOff(null);
        switch(getIntent().getStringExtra("wish")) {
            case "0" :
                btnWishlist.setChecked(false);
                break;
            case "1" :
                btnWishlist.setChecked(true);
                break;
        }

        dateBottle.setText(getIntent().getStringExtra("date"));

        countryBottle.setText(getIntent().getStringExtra("country"));
        regionBottle.setText(getIntent().getStringExtra("region"));
        domainBottle.setText(getIntent().getStringExtra("domain"));
        appellationBottle.setText(getIntent().getStringExtra("appellation"));
        millesimeBottle.setText(getIntent().getStringExtra("millesime"));
        apogeeBottle.setText(getIntent().getStringExtra("apogee"));
        estimateBottle.setText(getIntent().getStringExtra("estimate"));
        numberBottle.setText(getIntent().getStringExtra("number"));
    }

    private void initFabWineMenu() {
        fabWineMenu = findViewById(R.id.fabWineMenu);
        fabRed = findViewById(R.id.fabRed);
        fabRose = findViewById(R.id.fabRose);
        fabWhite = findViewById(R.id.fabWhite);
        fabChamp = findViewById(R.id.fabChamp);

        fabWineMenu.setAlpha(1f);
        fabRed.setAlpha(0f);
        fabRose.setAlpha(0f);
        fabWhite.setAlpha(0f);
        fabChamp.setAlpha(0f);

        fabRed.setTranslationX(0f);
        fabRed.setTranslationY(0f);
        fabRose.setTranslationX(0f);
        fabRose.setTranslationY(0f);
        fabWhite.setTranslationX(0f);
        fabWhite.setTranslationY(0f);
        fabChamp.setTranslationX(0f);
        fabChamp.setTranslationY(0f);
    }

    private void getFabWineMenuValue() {
        FloatingActionButton fabWineMenu = (FloatingActionButton) findViewById(R.id.fabWineMenu);
        fabWineMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isFABWineMenuOpen) {
                    closeFabWineMenu();
                } else {
                    openFabWineMenu();
                }
                //Toast.makeText(getApplicationContext(), "FAB WINE MENU",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabRed = (FloatingActionButton) findViewById(R.id.fabRed);
        fabRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BottleActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("redWine", "redWine");
                startActivity(intent);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB ROUGE",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabRose = (FloatingActionButton) findViewById(R.id.fabRose);
        fabRose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BottleActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("roseWine", "roseWine");
                startActivity(intent);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB ROSE",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabWhite = (FloatingActionButton) findViewById(R.id.fabWhite);
        fabWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BottleActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("whiteWine", "whiteWine");
                startActivity(intent);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB BLANC",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabChamp = (FloatingActionButton) findViewById(R.id.fabChamp);
        fabChamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BottleActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("champWine", "champWine");
                startActivity(intent);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB CHAMP",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void openFabWineMenu() {
        isFABWineMenuOpen = !isFABWineMenuOpen;

        fabWineMenu.animate().setInterpolator(interpolator).rotation(135f).setDuration(300).start();

        fabRed.animate().translationX(-250f).translationY(-180f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabRose.animate().translationX(-90f).translationY(-240f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabWhite.animate().translationX(90f).translationY(-240f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
        fabChamp.animate().translationX(250f).translationY(-180f).alpha(1f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void closeFabWineMenu() {
        isFABWineMenuOpen = !isFABWineMenuOpen;

        fabWineMenu.animate().setInterpolator(interpolator).rotation(0f).setDuration(300).start();

        fabRed.animate().translationX(0f).translationY(0f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabRose.animate().translationX(0f).translationY(0f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabWhite.animate().translationX(0f).translationY(0f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
        fabChamp.animate().translationX(0f).translationY(0f).alpha(0f).setInterpolator(interpolator).setDuration(300).start();
    }

    private void initCurvedNavigationView() {
        CurvedBottomNavigationView curvedBottomNavigationView = findViewById(R.id.curvedBottomNavigationView);
        curvedBottomNavigationView.setSelectedItemId(R.id.scan);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(new CurvedBottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.cellar:
                        //Toast.makeText(UserActivity.this, "USER", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BottleActivity.this, CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.user:
                        //Toast.makeText(UserActivity.this, "USER", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BottleActivity.this, UserActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.scan:
                        //Toast.makeText(UserActivity.this, "SCAN", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BottleActivity.this, ScanActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.like:
                        //Toast.makeText(UserActivity.this, "SEARCH", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BottleActivity.this, LikeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.search:
                        //Toast.makeText(UserActivity.this, "SEARCH", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BottleActivity.this, SearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }


}
