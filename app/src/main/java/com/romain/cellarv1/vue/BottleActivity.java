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
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocalCellar;
import com.romain.cellarv1.outils.BlurBitmap;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.ProgressBarAnimation;
import com.romain.cellarv1.outils.Tools;
import com.romain.cellarv1.outils.Validation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import me.tankery.lib.circularseekbar.CircularSeekBar;

public class BottleActivity extends AppCompatActivity {

    // Déclaration de la Custom FAB et de ses caractéristiques
    private FloatingActionButton fabWineMenu, fabRed, fabRose, fabWhite, fabChamp;
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private Boolean isFABWineMenuOpen = false;

    // Déclaration des champs texte et des ImageView
    private TextView dateBottle;
    private AutoCompleteTextView countryBottle, regionBottle;
    private EditText domainBottle, appellationBottle, addressBottle;
    private EditText millesimeBottle, apogeeBottle, estimateBottle, numberBottle;
    private ImageView imageBottle;
    private TextView nbRate;

    // Boutons SeekBar
    private ImageButton btnRoundMillesime, btnRoundApogee, btnRoundNumber, btnRoundEstimate;

    // PopupCircularSeekBar
    private Dialog popupMillesimeSeekBar, popupApogeeSeekBar, popupNumberSeekBar, popupEstimateSeekBar;
    private CircularSeekBar millesimeSeekBar, apogeeSeekBar, numberSeekBar, estimateSeekBar;
    private TextView txtMillesimeSeekBar, txtApogeeSeekBar, txtNumberSeekBar, txtEstimateSeekBar;
    private ImageButton btnMillesimeAccept, btnApogeeAccept, btnNumberAccept, btnEstimateAccept;
    private ImageButton btnMillesimeDenie, btnApogeeDenie, btnNumberDenie, btnEstimateDenie;

    // Buttons WineColor
    private ImageButton btnRed, btnRose, btnWhite, btnChamp;

    // Liste Pays
    private ArrayList<String> countryList = new ArrayList<>();

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

    // Images des indicateurs de validation
    private ImageView imgValidCountry, imgValidRegion, imgValidDomain, imgValidAppellation, imgValidAddress;

    // Pour check la validation des champs
    boolean countryOK = false;
    boolean regionOK = false;
    boolean millesimeOK = false;
    boolean apogeeOK = false;
    boolean domainOK = false;
    boolean appellationOK = false;
    boolean addressOK = false;
    boolean numberOK = false;
    boolean estimateOK = false;

    // ImageViewVignoble & WineColor
    private ImageView imgVignoble;
    private ImageView imageWineColor;



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

        // Animation entrante du menuBis
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

        // PopupMillesimeSeekBar
        popupMillesimeSeekBar = new Dialog(BottleActivity.this);
        popupMillesimeSeekBar.setContentView(R.layout.popup_add_millesime);
        popupMillesimeSeekBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupMillesimeSeekBar.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupMillesimeSeekBar.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        millesimeSeekBar = (CircularSeekBar) popupMillesimeSeekBar.findViewById(R.id.millesimeSeekBar);
        txtMillesimeSeekBar = (TextView) popupMillesimeSeekBar.findViewById(R.id.txtMillesimeSeekBar);
        btnMillesimeAccept = (ImageButton) popupMillesimeSeekBar.findViewById(R.id.btnAccept);
        btnMillesimeDenie = (ImageButton) popupMillesimeSeekBar.findViewById(R.id.btnDenie);

        // PopupApogeeSeekBar
        popupApogeeSeekBar = new Dialog(BottleActivity.this);
        popupApogeeSeekBar.setContentView(R.layout.popup_add_apogee);
        popupApogeeSeekBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupApogeeSeekBar.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupApogeeSeekBar.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        apogeeSeekBar = (CircularSeekBar) popupApogeeSeekBar.findViewById(R.id.apogeeSeekBar);
        txtApogeeSeekBar = (TextView) popupApogeeSeekBar.findViewById(R.id.txtApogeeSeekBar);
        btnApogeeAccept = (ImageButton) popupApogeeSeekBar.findViewById(R.id.btnAccept);
        btnApogeeDenie = (ImageButton) popupApogeeSeekBar.findViewById(R.id.btnDenie);

        // PopupNumberSeekBar
        popupNumberSeekBar = new Dialog(BottleActivity.this);
        popupNumberSeekBar.setContentView(R.layout.popup_add_number);
        popupNumberSeekBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupNumberSeekBar.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupNumberSeekBar.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        numberSeekBar = (CircularSeekBar) popupNumberSeekBar.findViewById(R.id.numberSeekBar);
        txtNumberSeekBar = (TextView) popupNumberSeekBar.findViewById(R.id.txtNumberSeekBar);
        btnNumberAccept = (ImageButton) popupNumberSeekBar.findViewById(R.id.btnAccept);
        btnNumberDenie = (ImageButton) popupNumberSeekBar.findViewById(R.id.btnDenie);

        // PopupEstimateSeekBar
        popupEstimateSeekBar = new Dialog(BottleActivity.this);
        popupEstimateSeekBar.setContentView(R.layout.popup_add_estimate);
        popupEstimateSeekBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupEstimateSeekBar.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupEstimateSeekBar.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        estimateSeekBar = (CircularSeekBar) popupEstimateSeekBar.findViewById(R.id.estimateSeekBar);
        txtEstimateSeekBar = (TextView) popupEstimateSeekBar.findViewById(R.id.txtEstimateSeekBar);
        btnEstimateAccept = (ImageButton) popupEstimateSeekBar.findViewById(R.id.btnAccept);
        btnEstimateDenie = (ImageButton) popupEstimateSeekBar.findViewById(R.id.btnDenie);

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

        // Buttons WineColor
        btnRed = (ImageButton) findViewById(R.id.redWineButton);
        btnRose = (ImageButton) findViewById(R.id.roseWineButton);
        btnWhite = (ImageButton) findViewById(R.id.whiteWineButton);
        btnChamp = (ImageButton) findViewById(R.id.champWineButton);

        // ImageView des indicateurs de validation
        imgValidCountry = (ImageView) findViewById(R.id.imgValidCountry);
        imgValidRegion = (ImageView) findViewById(R.id.imgValidRegion);
        imgValidDomain = (ImageView) findViewById(R.id.imgValidDomain);
        imgValidAppellation = (ImageView) findViewById(R.id.imgValidAppellation);
        imgValidAddress = (ImageView) findViewById(R.id.imgValidAddress);

        imgVignoble = (ImageView) findViewById(R.id.imgVignoble);

        gestionWineColorSelector();

        getRegionsList();
        getJsonCountries();
        recoverJsonCountries();

        gestionValidation();
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

    /**
     * Gestion de l'affichage de la couleur du vin
     */
    public void gestionWineColorSelector() {

        btnRed.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRed.setAlpha(1f);
                btnRose.setAlpha(0.3f);
                btnWhite.setAlpha(0.3f);
                btnChamp.setAlpha(0.3f);
                imageWineColor.setImageDrawable(ContextCompat.getDrawable(BottleActivity.this, R.drawable.red_wine_listview));
            }
        });

        btnRose.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRed.setAlpha(0.3f);
                btnRose.setAlpha(1f);
                btnWhite.setAlpha(0.3f);
                btnChamp.setAlpha(0.3f);
                imageWineColor.setImageDrawable(ContextCompat.getDrawable(BottleActivity.this, R.drawable.rose_wine_listview));
            }
        });

        btnWhite.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRed.setAlpha(0.3f);
                btnRose.setAlpha(0.3f);
                btnWhite.setAlpha(1f);
                btnChamp.setAlpha(0.3f);
                imageWineColor.setImageDrawable(ContextCompat.getDrawable(BottleActivity.this, R.drawable.white_wine_listview));
            }
        });

        btnChamp.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRed.setAlpha(0.3f);
                btnRose.setAlpha(0.3f);
                btnWhite.setAlpha(0.3f);
                btnChamp.setAlpha(1f);
                imageWineColor.setImageDrawable(ContextCompat.getDrawable(BottleActivity.this, R.drawable.champ_wine_listview));
            }
        });

    }

    /**
     * Chargement et récupération des infos du fichier JSon pour le nom des pays
     */
    public void getJsonCountries() {

        String json;
        try {
            // Load File
            InputStream is = getResources().openRawResource(R.raw.countries);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                countryList.add(jsonObject.getString("name"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Toast.makeText(getApplicationContext(),countrylist.toString(),Toast.LENGTH_LONG).show();
    }

    private void recoverJsonCountries() {
        getJsonCountries();
        countryBottle = (AutoCompleteTextView) findViewById(R.id.countryBottle);
        // On change la couleur de fond de la liste déroulante
        countryBottle.setDropDownBackgroundDrawable(new ColorDrawable(BottleActivity.this.getResources().getColor(R.color.green_very_dark)));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_autocomplete, countryList);
        countryBottle.setAdapter(adapter);
    }

    private void getRegionsList() {
        List<String> regionsList = Arrays.asList("Alsace", "Beaujolais", "Bordelais", "Bourgogne",
                "Bretagne", "Champagne", "Charentes", "Corse", "Ile-de-France", "Jura",
                "Languedoc-Roussillon", "Lorraine", "Lyonnais", "Nord-Pas-De-Calais", "Normandie",
                "Picardie", "Provence", "Savoie-Bugey", "Sud-Ouest", "Tahiti", "Val de Loire", "Vallée du Rhône");
        regionBottle = (AutoCompleteTextView) findViewById(R.id.regionBottle);
        // On change la couleur de fond de la liste déroulante
        regionBottle.setDropDownBackgroundDrawable(new ColorDrawable(BottleActivity.this.getResources().getColor(R.color.green_very_dark)));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_autocomplete, regionsList);
        regionBottle.setAdapter(adapter);
    }

    private void gestionRoundButtonSeekBar() {

        nbRate.setOnClickListener(new TextView.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupRateSeekBar();
            }
        });

        btnRoundMillesime = (ImageButton) findViewById(R.id.btnRoundMillesime);
        btnRoundMillesime.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupMillesimeSeekBar();
            }
        });

        btnRoundApogee = (ImageButton) findViewById(R.id.btnRoundApogee);
        btnRoundApogee.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupApogeeSeekBar();
            }
        });

        btnRoundNumber = (ImageButton) findViewById(R.id.btnRoundNumber);
        btnRoundNumber.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupNumberSeekBar();
            }
        });

        btnRoundEstimate = (ImageButton) findViewById(R.id.btnRoundEstimate);
        btnRoundEstimate.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupEstimateSeekBar();
            }
        });
    }

    private void displayPopupMillesimeSeekBar() {

        popupMillesimeSeekBar.show();

        btnMillesimeAccept.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                millesimeBottle.setText(String.valueOf(txtMillesimeSeekBar.getText()));
                popupMillesimeSeekBar.dismiss();
            }
        });

        btnMillesimeDenie.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupMillesimeSeekBar.dismiss();
            }
        });

        // Récupère la date actuelle au format yyyy
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String dateString = formatter.format(new Date());
        final Integer dateInt = Integer.parseInt(dateString);

        millesimeSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                // Permet de naviguer de l'année actuelle à -70 ans
                Integer millesime = dateInt - (int) progress;
                txtMillesimeSeekBar.setText(String.valueOf(millesime));
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

    }

    private void displayPopupApogeeSeekBar() {

        popupApogeeSeekBar.show();

        btnApogeeAccept.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                apogeeBottle.setText(String.valueOf(txtApogeeSeekBar.getText()));
                popupApogeeSeekBar.dismiss();
            }
        });

        btnApogeeDenie.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupApogeeSeekBar.dismiss();
            }
        });

        // Récupère la date actuelle au format yyyy
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
        String dateString = formatter.format(new Date());
        final Integer dateInt = Integer.parseInt(dateString);

        apogeeSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                // Permet de naviguer de l'année actuelle à +70 ans
                Integer millesime = dateInt + (int) progress;
                txtApogeeSeekBar.setText(String.valueOf(millesime));
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

    }

    private void displayPopupNumberSeekBar() {

        popupNumberSeekBar.show();

        btnNumberAccept.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                numberBottle.setText(String.valueOf(txtNumberSeekBar.getText()));
                popupNumberSeekBar.dismiss();
            }
        });

        btnNumberDenie.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupNumberSeekBar.dismiss();
            }
        });

        numberSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                // Permet d'ajouter jusqu'à 66 bouteilles
                txtNumberSeekBar.setText(String.valueOf((int) progress));
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

            }
        });

    }

    private void displayPopupEstimateSeekBar() {

        popupEstimateSeekBar.show();

        btnEstimateAccept.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Permet de retirer les deux derniers caractères : " €" pour ne set qu'un nombre
                String nbEstimateRaw = String.valueOf(txtEstimateSeekBar.getText());
                estimateBottle.setText(nbEstimateRaw.substring(0, nbEstimateRaw.length() - 2));
                popupEstimateSeekBar.dismiss();
            }
        });

        btnEstimateDenie.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupEstimateSeekBar.dismiss();
            }
        });

        estimateSeekBar.setOnSeekBarChangeListener(new CircularSeekBar.OnCircularSeekBarChangeListener() {
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                // Permet d'estimer jusqu'à 150€
                txtEstimateSeekBar.setText(String.valueOf((int) progress + " €"));
            }

            @Override
            public void onStopTrackingTouch(CircularSeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(CircularSeekBar seekBar) {

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

                // On gère la couleur de nbRate suivant la valeur de la note
                if (txtRateSeekBar.getText().toString().trim().equals("*/10")) {
                    nbRate.setTextColor(getResources().getColor(R.color.green_middle_light));
                    nbRate.setText("*/10");
                } else if(txtRateSeekBar.getText().toString().trim().equals("0/10")) {
                    nbRate.setTextColor(getResources().getColor(R.color.green_middle_light));
                    nbRate.setText("*/10");
                } else {
                    nbRate.setTextColor(getResources().getColor(R.color.green_apple));
                    nbRate.setText(txtRateSeekBar.getText().toString().trim());
                }

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

    /**
     * Méthode qui gère la progressBar et les validations de champs
     */
    private void gestionValidation() {

        // Initialisation des indicateurs de validation à INVISIBLE
        imgValidCountry.setVisibility(View.INVISIBLE);
        imgValidRegion.setVisibility(View.INVISIBLE);
        imgValidDomain.setVisibility(View.INVISIBLE);
        imgValidAppellation.setVisibility(View.INVISIBLE);
        imgValidAddress.setVisibility(View.INVISIBLE);

        // Instanciation des méthodes de validation
        final Validation validation = new Validation();

        countryBottle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidCountry.setVisibility(View.VISIBLE);
                if (validation.isValidTextField(countryBottle.getText().toString().trim())) {
                    // is true
                    imgValidCountry.setColorFilter(getResources().getColor(R.color.green_apple));
                    countryOK = true;

                } else if (!validation.isValidTextField(countryBottle.getText().toString().trim())) {
                    // is false
                    imgValidCountry.setColorFilter(getResources().getColor(R.color.pink));
                    countryOK = false;
                }
                gestionUpdateButtonColor();
            }
        });

        regionBottle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidRegion.setVisibility(View.VISIBLE);
                if (validation.isValidTextField(regionBottle.getText().toString().trim())) {
                    // is true
                    imgValidRegion.setColorFilter(getResources().getColor(R.color.green_apple));
                    regionOK = true;

                } else if (!validation.isValidTextField(regionBottle.getText().toString().trim())) {
                    // is false
                    imgValidRegion.setColorFilter(getResources().getColor(R.color.pink));
                    regionOK = false;
                }
                gestionUpdateButtonColor();
            }
        });

        millesimeBottle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validation.isValidNumberField(millesimeBottle.getText().toString().trim())) {
                    millesimeOK = true;
                } else if (!validation.isValidNumberField(millesimeBottle.getText().toString().trim())) {
                    millesimeOK = false;
                }
                gestionUpdateButtonColor();
            }
        });

        apogeeBottle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validation.isValidNumberField(apogeeBottle.getText().toString().trim())) {
                    apogeeOK = true;

                } else if (!validation.isValidNumberField(apogeeBottle.getText().toString().trim())) {
                    apogeeOK = false;
                }
                gestionUpdateButtonColor();
            }
        });

        domainBottle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidDomain.setVisibility(View.VISIBLE);
                if (validation.isValidTextField(domainBottle.getText().toString().trim())) {
                    // is true
                    imgValidDomain.setColorFilter(getResources().getColor(R.color.green_apple));
                    domainOK = true;

                } else if (!validation.isValidTextField(domainBottle.getText().toString().trim())) {
                    // is false
                    imgValidDomain.setColorFilter(getResources().getColor(R.color.pink));
                    domainOK = false;
                }
                gestionUpdateButtonColor();
            }
        });

        appellationBottle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidAppellation.setVisibility(View.VISIBLE);
                if (validation.isValidTextField(appellationBottle.getText().toString().trim())) {
                    // is true
                    imgValidAppellation.setColorFilter(getResources().getColor(R.color.green_apple));
                    appellationOK = true;

                } else if (!validation.isValidTextField(appellationBottle.getText().toString().trim())) {
                    // is false
                    imgValidAppellation.setColorFilter(getResources().getColor(R.color.pink));
                    appellationOK = false;
                }
                gestionUpdateButtonColor();
            }
        });

        addressBottle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidAddress.setVisibility(View.VISIBLE);
                if (validation.isValidTextField(addressBottle.getText().toString().trim())) {
                    // is true
                    imgValidAddress.setColorFilter(getResources().getColor(R.color.green_apple));
                    addressOK = true;

                } else if (!validation.isValidTextField(addressBottle.getText().toString().trim())) {
                    // is false
                    imgValidAddress.setColorFilter(getResources().getColor(R.color.pink));
                    addressOK = false;
                }
                gestionUpdateButtonColor();
            }
        });

        numberBottle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validation.isValidNumberField(numberBottle.getText().toString().trim())) {
                    numberOK = true;

                } else if (!validation.isValidNumberField(numberBottle.getText().toString().trim())) {
                    numberOK = false;
                }
                gestionUpdateButtonColor();
            }

        });

        estimateBottle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validation.isValidNumberField(estimateBottle.getText().toString().trim())) {
                    estimateOK = true;

                } else if (!validation.isValidNumberField(estimateBottle.getText().toString().trim())) {
                    estimateOK = false;
                }
                gestionUpdateButtonColor();
            }
        });

    }

    private void gestionUpdateButtonColor() {

        // TODO POUR L'INSTANT, ON NE GERE PAS L'APPARENCE DU BOUTON UPDATE
        /*
        if(countryOK == true && regionOK == true && millesimeOK == true && apogeeOK == true && domainOK == true && appellationOK == true && addressOK == true && numberOK == true && estimateOK == true) {
            btnUpdateBottle.setColorFilter(getResources().getColor(R.color.green_apple));
        }else {
            btnUpdateBottle.setColorFilter(getResources().getColor(R.color.pink));
        }
        */


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

                // Récupération de la couleur du vin avec l'alpha des boutons btnWineColor
                if(btnRed.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.red_wine_listview);
                } else if(btnRose.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.rose_wine_listview);
                } else if(btnWhite.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.white_wine_listview);
                } else if(btnChamp.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.champ_wine_listview);
                }


                String image = getIntent().getStringExtra("imageBottleSmall");
                Tools tools = new Tools();
                imageBottle.setImageBitmap(tools.stringToBitmap(image));


                // On gère la couleur de la note sur la popupUpdate suivant sa valeur
                nbRatePopup = (TextView) popupUpdate.findViewById(R.id.nbRatePopup);
                String ratePopup = nbRate.getText().toString();
                if (ratePopup.trim().equals("*/10")) {
                    nbRatePopup.setText("*/10");
                    nbRatePopup.setTextColor(getResources().getColor(R.color.green_middle_light));
                } else {
                    nbRatePopup.setText(ratePopup);
                    nbRatePopup.setTextColor(getResources().getColor(R.color.green_apple));
                }


                if(btnFavorite.isChecked()) {
                    imageFavorite.setColorFilter(getResources().getColor(R.color.green_apple));
                } else {
                    imageFavorite.setColorFilter(getResources().getColor(R.color.green_middle_light));
                }

                if(btnWishlist.isChecked()) {
                    imageWish.setColorFilter(getResources().getColor(R.color.green_apple));
                } else {
                    imageWish.setColorFilter(getResources().getColor(R.color.green_middle_light));
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

                        // On set la couleur du vin suivant l'alpha du bouton
                        String strWineColor = "";
                        if(btnRed.getAlpha() == 1f) {
                            strWineColor = "Rouge";
                        } else if(btnRose.getAlpha() == 1f) {
                            strWineColor = "Rose";
                        } else if(btnWhite.getAlpha() == 1f) {
                            strWineColor = "Blanc";
                        } else if(btnChamp.getAlpha() == 1f) {
                            strWineColor = "Effervescent";
                        }

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

                        AccesLocalCellar accesLocalCellar = new AccesLocalCellar(BottleActivity.this);
                        accesLocalCellar.updateBottle(intId, strCountry, strRegion, strWineColor, strDomain, strAppellation, strAddress, intMillesime, intApogee, intNumber, intEstimate, intRate, strFavorite, strWish);
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

                // On gère la couleur de la note sur la popupDelete suivant sa valeur
                nbRatePopup = (TextView) popupDelete.findViewById(R.id.nbRatePopup);
                String ratePopup = nbRate.getText().toString();
                if (ratePopup.trim().equals("*/10")) {
                    nbRatePopup.setText("*/10");
                    nbRatePopup.setTextColor(getResources().getColor(R.color.green_middle_light));
                } else {
                    nbRatePopup.setText(ratePopup);
                    nbRatePopup.setTextColor(getResources().getColor(R.color.green_apple));
                }

                // Récupération de la couleur du vin avec l'alpha des boutons btnWineColor
                if(btnRed.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.red_wine_listview);
                } else if(btnRose.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.rose_wine_listview);
                } else if(btnWhite.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.white_wine_listview);
                } else if(btnChamp.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.champ_wine_listview);
                }

                String image = getIntent().getStringExtra("imageBottleSmall");
                Tools tools = new Tools();
                imageBottle.setImageBitmap(tools.stringToBitmap(image));


                if(btnFavorite.isChecked()) {
                    imageFavorite.setColorFilter(getResources().getColor(R.color.green_apple));
                } else {
                    imageFavorite.setColorFilter(getResources().getColor(R.color.green_middle_light));
                }

                if(btnWishlist.isChecked()) {
                    imageWish.setColorFilter(getResources().getColor(R.color.green_apple));
                } else {
                    imageWish.setColorFilter(getResources().getColor(R.color.green_middle_light));
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

                        AccesLocalCellar accesLocalCellar = new AccesLocalCellar(BottleActivity.this);
                        accesLocalCellar.takeOutBottle(intId);
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

        countryBottle = (AutoCompleteTextView) findViewById(R.id.countryBottle);
        regionBottle = (AutoCompleteTextView) findViewById(R.id.regionBottle);
        domainBottle = (EditText) findViewById(R.id.domainBottle);
        appellationBottle = (EditText) findViewById(R.id.appellationBottle);
        addressBottle = (EditText) findViewById(R.id.addressBottle);
        millesimeBottle = (EditText) findViewById(R.id.millesimeBottle);
        apogeeBottle = (EditText) findViewById(R.id.apogeeBottle);
        estimateBottle = (EditText) findViewById(R.id.estimateBottle);
        numberBottle = (EditText) findViewById(R.id.numberBottle);

        nbRate = (TextView) findViewById(R.id.nbRate);

        btnRed = (ImageButton) findViewById(R.id.redWineButton);
        btnRose = (ImageButton) findViewById(R.id.roseWineButton);
        btnWhite = (ImageButton) findViewById(R.id.whiteWineButton);
        btnChamp = (ImageButton) findViewById(R.id.champWineButton);

        switch(getIntent().getStringExtra("wineColor").trim()) {
            case "Rouge" :
                btnRed.setAlpha(1f);
                btnRose.setAlpha(0.3f);
                btnWhite.setAlpha(0.3f);
                btnChamp.setAlpha(0.3f);
                imageWineColor.setImageResource(R.drawable.red_wine_listview);
                break;
            case "Rose" :
                btnRed.setAlpha(0.3f);
                btnRose.setAlpha(1f);
                btnWhite.setAlpha(0.3f);
                btnChamp.setAlpha(0.3f);
                imageWineColor.setImageResource(R.drawable.rose_wine_listview);
                break;
            case "Blanc" :
                btnRed.setAlpha(0.3f);
                btnRose.setAlpha(0.3f);
                btnWhite.setAlpha(1f);
                btnChamp.setAlpha(0.3f);
                imageWineColor.setImageResource(R.drawable.white_wine_listview);
                break;
            case "Effervescent" :
                btnRed.setAlpha(0.3f);
                btnRose.setAlpha(0.3f);
                btnWhite.setAlpha(0.3f);
                btnChamp.setAlpha(1f);
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
            nbRate.setTextColor(getResources().getColor(R.color.green_middle_light));
        } else {
            nbRate.setText(rate + "/10");
            nbRate.setTextColor(getResources().getColor(R.color.green_apple));
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
        switch(Objects.requireNonNull(getIntent().getStringExtra("wish"))) {
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
