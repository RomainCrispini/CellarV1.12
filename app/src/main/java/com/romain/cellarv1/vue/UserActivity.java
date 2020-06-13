package com.romain.cellarv1.vue;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SwitchCompat;

import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.romain.cellarv1.R;
import com.romain.cellarv1.controleur.UserControle;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.modele.WineBottle;
import com.romain.cellarv1.outils.BlurBitmap;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.PlaceAutoSuggestAdapter;

import java.util.ArrayList;
import java.util.Arrays;


public class UserActivity extends AppCompatActivity {

    // Déclaration des Popup d'accès
    private Dialog popupConnectionRegistration, popupConnection, popupRegistration;
    private EditText txtPseudo, txtPassword, txtMail;
    private ImageView imgValidPseudo, imgValidPassword, imgValidMail;
    private ImageButton btnExit, btnConnection, btnRegistration;

    // Déclaration de la Custom FAB et de ses caractéristiques
    private FloatingActionButton fabWineMenu, fabRed, fabRose, fabWhite, fabChamp;
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private Boolean isFABWineMenuOpen = false;

    // Déclaration du switchDarkMode
    private SwitchCompat switchDarkMode = null;

    private PlacesClient placesClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initPopup();
        init();

        String apikey = getString(R.string.map_key);

        if(!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apikey);
        }

        placesClient = Places.createClient(this);

        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete2);





        final AutoCompleteTextView autoCompleteTextView=findViewById(R.id.autocomplete);
        autoCompleteTextView.setAdapter(new PlaceAutoSuggestAdapter(UserActivity.this,android.R.layout.simple_list_item_1));

        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                final LatLng latLng = place.getLatLng();

                Toast.makeText(UserActivity.this, "lattitude : " + latLng.latitude + "longitude" + latLng.longitude, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });



    }

    private void init() {

        initCurvedNavigationView();
        initFabWineMenu();
        getFabWineMenuValue();
        switchDarkMode();

    }

    private void initPopup() {

        // PopupConnectionRegistration
        popupConnectionRegistration = new Dialog(UserActivity.this);
        popupConnectionRegistration.setContentView(R.layout.popup_connection_registration);
        popupConnectionRegistration.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupConnectionRegistration.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        btnExit = (ImageButton) popupConnectionRegistration.findViewById(R.id.btnExit);
        btnConnection = (ImageButton) popupConnectionRegistration.findViewById(R.id.btnConnection);
        btnRegistration = (ImageButton) popupConnectionRegistration.findViewById(R.id.btnRegistration);

        // PopupRegistration
        popupRegistration = new Dialog(UserActivity.this);
        popupRegistration.setContentView(R.layout.popup_registration);
        popupRegistration.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupRegistration.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        txtPseudo = (EditText) popupRegistration.findViewById(R.id.txtPseudo);
        txtPassword = (EditText) popupRegistration.findViewById(R.id.txtPassword);
        txtMail = (EditText) popupRegistration.findViewById(R.id.txtMail);
        imgValidPseudo = (ImageView) popupRegistration.findViewById(R.id.imgValidPseudo);
        imgValidPassword = (ImageView) popupRegistration.findViewById(R.id.imgValidPassword);
        imgValidMail = (ImageView) popupRegistration.findViewById(R.id.imgValidMail);

        // PopupConnection
        popupConnection = new Dialog(UserActivity.this);
        popupConnection.setContentView(R.layout.popup_connection);
        popupConnection.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupConnection.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        displayPopupConnectionRegistration();
    }

    private void displayPopupConnectionRegistration() {

        popupConnectionRegistration.show();

        btnExit.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                UserActivity.this.finish();
            }
        });


        btnConnection.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupConnectionRegistration.dismiss();
                //displayPopupConnection();
            }
        });

        btnRegistration.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupConnectionRegistration.dismiss();
                displayPopupRegistration();
            }
        });


    }

    private void displayPopupConnection() {

        popupConnection.show();

        /*

        btnExit.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                UserActivity.this.finish();
            }
        });


        btnConnection.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupConnectionRegistration.dismiss();
            }
        });

        btnRegistration.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupConnectionRegistration.dismiss();
            }
        });

         */


    }

    private void displayPopupRegistration() {

        imgValidPseudo.setVisibility(View.INVISIBLE);
        imgValidPassword.setVisibility(View.INVISIBLE);
        imgValidMail.setVisibility(View.INVISIBLE);

        popupRegistration.show();

        final UserControle userControle = new UserControle();

        txtPseudo.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidPseudo.setVisibility(View.VISIBLE);
                if (userControle.isValidPseudo(txtPseudo.getText().toString().trim())) {
                    // is true
                    imgValidPseudo.setColorFilter(getResources().getColor(R.color.green_apple));

                } else if(!userControle.isValidPseudo(txtPseudo.getText().toString().trim())) {
                    // is false
                    imgValidPseudo.setColorFilter(getResources().getColor(R.color.pink));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        txtPassword.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidPassword.setVisibility(View.VISIBLE);
                if (userControle.isValidPassword(txtPassword.getText().toString().trim())) {
                    // is true
                    imgValidPassword.setColorFilter(getResources().getColor(R.color.green_apple));

                } else if(!userControle.isValidPassword(txtPassword.getText().toString().trim())) {
                    // is false
                    imgValidPassword.setColorFilter(getResources().getColor(R.color.pink));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        txtMail.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidMail.setVisibility(View.VISIBLE);
                if (userControle.isValidMail(txtMail.getText().toString().trim())) {
                    // is true
                    imgValidMail.setColorFilter(getResources().getColor(R.color.green_apple));

                } else if(!userControle.isValidMail(txtMail.getText().toString().trim())) {
                    // is false
                    imgValidMail.setColorFilter(getResources().getColor(R.color.pink));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });







        /*

        btnExit.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(UserActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                UserActivity.this.finish();
            }
        });


        btnConnection.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupConnectionRegistration.dismiss();
            }
        });

        btnRegistration.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupConnectionRegistration.dismiss();
            }
        });

         */


    }

    private void switchDarkMode() {
        SwitchCompat switchDarkMode = (SwitchCompat) findViewById(R.id.switchDarkMode);
        switchDarkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setTheme(R.style.AppTheme);
                    Toast.makeText(UserActivity.this, "Switch DARK",Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserActivity.this, "Switch LIGHT",Toast.LENGTH_SHORT).show();
                    setTheme(R.style.AppTheme);
                }
            }
        });
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
                Intent intent = new Intent(UserActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("redWine", "redWine");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB ROUGE",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabRose = (FloatingActionButton) findViewById(R.id.fabRose);
        fabRose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("roseWine", "roseWine");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB ROSE",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabWhite = (FloatingActionButton) findViewById(R.id.fabWhite);
        fabWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("whiteWine", "whiteWine");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                closeFabWineMenu();
                //Toast.makeText(getApplicationContext(), "FAB BLANC",Toast.LENGTH_SHORT).show();
            }
        });

        FloatingActionButton fabChamp = (FloatingActionButton) findViewById(R.id.fabChamp);
        fabChamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this, AddActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.putExtra("champWine", "champWine");
                startActivity(intent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
        curvedBottomNavigationView.setSelectedItemId(R.id.userMenu);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(new CurvedBottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.cellarMenu:
                        startActivity(new Intent(UserActivity.this, CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        UserActivity.this.finish();
                        return true;
                    case R.id.scanMenu:
                        startActivity(new Intent(UserActivity.this, ScanActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        UserActivity.this.finish();
                        return true;
                    case R.id.likeMenu:
                        startActivity(new Intent(UserActivity.this, LikeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        UserActivity.this.finish();
                        return true;
                    case R.id.mapMenu:
                        startActivity(new Intent(UserActivity.this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        UserActivity.this.finish();
                        return true;
                }
                return false;
            }
        });
    }



}
