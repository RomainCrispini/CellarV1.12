package com.romain.cellarv1.vue;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocalDbCellar;
import com.romain.cellarv1.outils.BlurBitmap;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.Tools;

import java.util.Objects;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class BottleActivity extends AppCompatActivity {

    // Déclaration de la Custom FAB et de ses caractéristiques
    private FloatingActionButton fabWineMenu, fabRed, fabRose, fabWhite, fabChamp;
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private Boolean isFABWineMenuOpen = false;

    // Déclaration des champs texte et des ImageView
    private TextView dateBottle;
    private EditText countryBottle, regionBottle, domainBottle, appellationBottle, addressBottle;
    private EditText millesimeBottle, apogeeBottle, estimateBottle, numberBottle;
    private ImageView imageBottle, imageWineColor;
    private TextView nbRate;

    // Button Update
    private ImageButton btnUpdateBottle;

    // Button Delete
    private ImageButton btnDeleteBottle;

    // Buttons Appreciation
    private ToggleButton btnFavorite;
    private ToggleButton btnWishlist;

    // PopupRateSeekBar
    private Dialog popupRateSeekBar;
    private CircularSeekBar rateSeekBar;
    private ImageButton btnRateAccept, btnRateDenie;
    private TextView txtRateSeekBar;

    // PopupUpdate et PopupDelete
    private Dialog popupUpdate, popupDelete, popupSuccessUpdate, popupSuccessDelete;
    private TextView nbRatePopup;

    // ImageViewVignoble
    private ImageView imgVignoble;



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

        btnUpdateBottle = (ImageButton) findViewById(R.id.btnUpdateBottle);
        btnDeleteBottle = (ImageButton) findViewById(R.id.btnDeleteBottle);

        FrameLayout menuBis = (FrameLayout) findViewById(R.id.menuBis);
        menuBis.setTranslationY(300f);
        menuBis.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

        popupUpdate = new Dialog(BottleActivity.this);
        popupUpdate.setContentView(R.layout.popup_update_bottle);
        popupUpdate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupUpdate.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        popupDelete = new Dialog(BottleActivity.this);
        popupDelete.setContentView(R.layout.popup_take_out_bottle);
        popupDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupDelete.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        popupSuccessUpdate = new Dialog(BottleActivity.this);
        popupSuccessUpdate.setContentView(R.layout.popup_success_update_bottle);
        popupSuccessUpdate.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupSuccessUpdate.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        popupSuccessDelete = new Dialog(BottleActivity.this);
        popupSuccessDelete.setContentView(R.layout.popup_success_delete_bottle);
        popupSuccessDelete.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupSuccessDelete.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        // PopupRateSeekBar
        popupRateSeekBar = new Dialog(BottleActivity.this);
        popupRateSeekBar.setContentView(R.layout.popup_add_rate);
        popupRateSeekBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupRateSeekBar.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        rateSeekBar = (CircularSeekBar) popupRateSeekBar.findViewById(R.id.rateSeekBar);
        txtRateSeekBar = (TextView) popupRateSeekBar.findViewById(R.id.txtRateSeekBar);
        btnRateAccept = (ImageButton) popupRateSeekBar.findViewById(R.id.btnAccept);
        btnRateDenie = (ImageButton) popupRateSeekBar.findViewById(R.id.btnDenie);
        nbRate = (TextView) findViewById(R.id.nbRate);

        imgVignoble = (ImageView) findViewById(R.id.imgVignoble);



        gestionImageVignoble();
        gestionRoundButtonSeekBar();


    }

    private void gestionImageVignoble() {
        // L'image par défaut est vignoble_alsace
        imgVignoble = (ImageView) findViewById(R.id.imgVignoble);
        imgVignoble.setBackgroundResource(R.drawable.vignoble_alsace);
        // On floute par défaut cette image
        Bitmap bitmap = ((BitmapDrawable) imgVignoble.getBackground()).getBitmap();
        imgVignoble.setImageBitmap(new BlurBitmap().blur(BottleActivity.this, bitmap, 20f));
    }

    private void gestionRoundButtonSeekBar() {

        nbRate.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupRateSeekBar();
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void displayPopupRateSeekBar() {

        String nbRateRecup = nbRate.getText().toString();
        if(!nbRateRecup.equals("*/10")) {
            // On set la progressBar à l'ouverture en retirant le string : "/10" (les 3 derniers caractères)
            String strNbRate = nbRate.getText().toString().substring(0, nbRate.length() - 3);
            int initRate = Integer.parseInt(strNbRate);
            rateSeekBar.setProgress(initRate);
                // On set le texte central de la popup à l'ouverture avec le texte de l'activité suivant sa valeur
                if(initRate == 0) {
                    txtRateSeekBar.setText("*/10");
                    txtRateSeekBar.setTextColor(getResources().getColor(R.color.green_light));
                } else {
                    txtRateSeekBar.setText(nbRate.getText());
                    txtRateSeekBar.setTextColor(getResources().getColor(R.color.green_apple));
                }
        } else {
            txtRateSeekBar.setText("*/10");
            rateSeekBar.setProgress(0);
        }

        popupRateSeekBar.show();

        btnRateAccept.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                nbRate.setText(txtRateSeekBar.getText());
                popupRateSeekBar.dismiss();
            }
        });

        btnRateDenie.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupRateSeekBar.dismiss();
            }
        });

        rateSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                txtRateSeekBar.setText((int)progress + "/10");
                txtRateSeekBar.setTextColor(getResources().getColor(R.color.green_apple));
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

    }

    private void btnUpdateBottle() {
        btnUpdateBottle.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageButton btnAccept = (ImageButton) popupUpdate.findViewById(R.id.btnAccept);
                ImageButton btnDenie = (ImageButton) popupUpdate.findViewById(R.id.btnDenie);

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


                nbRatePopup = (TextView) popupUpdate.findViewById(R.id.nbRatePopup);
                String ratePopup = nbRate.getText().toString();
                nbRatePopup.setText(ratePopup);

                if(btnFavorite.isChecked()) {
                    imageFavorite.setColorFilter(getResources().getColor(R.color.green_apple));
                } else {
                    imageFavorite.setColorFilter(getResources().getColor(R.color.green_light));
                }

                if(btnWishlist.isChecked()) {
                    imageWish.setColorFilter(getResources().getColor(R.color.green_apple));
                } else {
                    imageWish.setColorFilter(getResources().getColor(R.color.green_light));
                }

                region.setText(regionBottle.getText());
                domain.setText(domainBottle.getText());
                appellation.setText(appellationBottle.getText());
                millesime.setText(millesimeBottle.getText());


                popupUpdate.show();

                btnAccept.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        // Conversion de l'id à la récupération de l'Intent
                        String strId = getIntent().getStringExtra("id");
                        Integer intId = Integer.parseInt(strId);

                        String strCountry = countryBottle.getText().toString();
                        String strRegion = regionBottle.getText().toString();
                        String strDomain = domainBottle.getText().toString();
                        String strAppellation = appellationBottle.getText().toString();
                        String strAddress = addressBottle.getText().toString();
                        Integer intMillesime = Integer.parseInt(millesimeBottle.getText().toString());
                        Integer intApogee = Integer.parseInt(apogeeBottle.getText().toString());
                        Integer intNumber = Integer.parseInt(numberBottle.getText().toString());
                        Integer intEstimate = Integer.parseInt(estimateBottle.getText().toString());

                        // On retire toujours les 3 derniers caractères : "/10" et on remplace "*" par "0", si c'est le cas
                        Integer intRate;
                        String strRate = nbRate.getText().toString().trim().substring(0, nbRate.length() - 3);
                        if(strRate.equals("*")) {
                            intRate = 0;
                        } else {
                            intRate = Integer.parseInt(strRate);
                        }

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

                        AccesLocalDbCellar accesLocalDbCellar = new AccesLocalDbCellar(BottleActivity.this);
                        accesLocalDbCellar.updateBottle(intId, strCountry, strRegion, strDomain, strAppellation, strAddress, intMillesime, intApogee, intNumber, intEstimate, intRate, strFavorite, strWish);
                        popupUpdate.dismiss();

                        popupSuccessUpdate.show();
                        // Permet de faire aparaitre le panneau 1 seconde sans interventions
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                if (popupSuccessUpdate.isShowing()) {
                                    popupSuccessUpdate.dismiss();
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

                nbRatePopup = (TextView) popupDelete.findViewById(R.id.nbRatePopup);
                String ratePopup = nbRate.getText().toString();
                nbRatePopup.setText(ratePopup);

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


                if(btnFavorite.isChecked()) {
                    imageFavorite.setColorFilter(getResources().getColor(R.color.green_apple));
                } else {
                    imageFavorite.setColorFilter(getResources().getColor(R.color.green_light));
                }

                if(btnWishlist.isChecked()) {
                    imageWish.setColorFilter(getResources().getColor(R.color.green_apple));
                } else {
                    imageWish.setColorFilter(getResources().getColor(R.color.green_light));
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

                        AccesLocalDbCellar accesLocalDbCellar = new AccesLocalDbCellar(BottleActivity.this);
                        accesLocalDbCellar.takeOutBottle(intId);
                        popupDelete.dismiss();

                        // Basculement vers CellarActivity à l'effacement d'une bouteille dans le BottleActivity
                        Intent intent = new Intent(BottleActivity.this, CellarActivity.class);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        popupSuccessDelete.show();
                        // Permet de faire aparaitre le panneau 2 secondes sans interventions
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
    }

    @SuppressLint("SetTextI18n")
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
        addressBottle = (EditText) findViewById(R.id.addressBottle);
        millesimeBottle = (EditText) findViewById(R.id.millesimeBottle);
        apogeeBottle = (EditText) findViewById(R.id.apogeeBottle);
        estimateBottle = (EditText) findViewById(R.id.estimateBottle);
        numberBottle = (EditText) findViewById(R.id.numberBottle);

        nbRate = (TextView) findViewById(R.id.nbRate);


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
        if(rate.equals("0")) {
            nbRate.setText("*/10");
        } else {
            nbRate.setText(rate + "/10");
        }



        btnFavorite.setText(null);
        btnFavorite.setTextOn(null);
        btnFavorite.setTextOff(null);
        switch(Objects.requireNonNull(getIntent().getStringExtra("favorite"))) {
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
        addressBottle.setText(getIntent().getStringExtra("address"));
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
        curvedBottomNavigationView.setSelectedItemId(R.id.scanMenu);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(new CurvedBottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.mapMenu:
                        startActivity(new Intent(BottleActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        BottleActivity.this.finish();
                        return true;
                    case R.id.cellarMenu:
                        startActivity(new Intent(BottleActivity.this, CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        BottleActivity.this.finish();
                        return true;
                    case R.id.scanMenu:
                        startActivity(new Intent(BottleActivity.this, ScanActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        BottleActivity.this.finish();
                        return true;
                    case R.id.likeMenu:
                        startActivity(new Intent(BottleActivity.this, LikeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        BottleActivity.this.finish();
                        return true;
                    case R.id.userMenu:
                        startActivity(new Intent(BottleActivity.this, UserActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        BottleActivity.this.finish();
                        return true;
                }
                return false;
            }
        });
    }


}
