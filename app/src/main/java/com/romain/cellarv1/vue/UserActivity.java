package com.romain.cellarv1.vue;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SwitchCompat;

import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

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
import com.romain.cellarv1.outils.Validation;
import com.romain.cellarv1.modele.AccesLocalUsers;
import com.romain.cellarv1.modele.User;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.PlaceAutoSuggestAdapter;

import java.util.Arrays;


public class UserActivity extends AppCompatActivity {

    // Déclaration de PopupRegistration, popupConnection et de ses éléments
    private Dialog popupRegistration, popupConnection;
    private Dialog popupSuccess;
    private Dialog popupRegistrationDenie;
    private EditText txtPseudoRegistration, txtPasswordRegistration, txtMailRegistration;
    private EditText txtPseudoConnection, txtPasswordConnection, txtMailConnection;
    private ImageView imgValidPseudoRegistration, imgValidPasswordRegistration, imgValidMailRegistration;
    private ImageView imgValidPseudoConnection, imgValidPasswordConnection, imgValidMailConnection;
    private ImageButton btnRegistrationAccept, btnRegistrationDenie, btnConnectionAccept, btnConnectionDenie;

    // MenuBis
    private ImageButton btnUserRegistration, btnUserConnection;
    private FrameLayout menuBis;

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
        init();

        // TODO IL NE PEUT Y AVOIR QU'UN SEUL COMPTE ENREGISTRE




        String apikey = getResources().getString(R.string.map_key);

        if(!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apikey);
        }

        placesClient = Places.createClient(this);

        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete2);

        assert autocompleteSupportFragment != null;
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {

                Toast.makeText(UserActivity.this, "lattitude : " + place.getLatLng().latitude + "longitude" + place.getLatLng().longitude + "Adresse : " + place.getAddress(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });




    }

    private void init() {

        // Animation entrante du menuBis
        FrameLayout menuBis = (FrameLayout) findViewById(R.id.menuBis);
        menuBis.setTranslationY(300f);
        menuBis.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

        initPopupRegistration();
        initPopupConnection();
        initCurvedNavigationView();
        initFabWineMenu();
        getFabWineMenuValue();
        switchDarkMode();

    }

    private void initPopupRegistration() {

        popupSuccess = new Dialog(UserActivity.this);
        popupSuccess.setContentView(R.layout.popup_success_add_bottle);
        popupSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupSuccess.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        popupRegistrationDenie = new Dialog(UserActivity.this);
        popupRegistrationDenie.setContentView(R.layout.popup_user_registration_denie);
        popupRegistrationDenie.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupRegistrationDenie.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        popupRegistration = new Dialog(UserActivity.this);
        popupRegistration.setContentView(R.layout.popup_user_registration);
        popupRegistration.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupRegistration.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        txtPseudoRegistration = (EditText) popupRegistration.findViewById(R.id.txtPseudo);
        txtPasswordRegistration = (EditText) popupRegistration.findViewById(R.id.txtPassword);
        txtMailRegistration = (EditText) popupRegistration.findViewById(R.id.txtMail);
        imgValidPseudoRegistration = (ImageView) popupRegistration.findViewById(R.id.imgValidPseudo);
        imgValidPasswordRegistration = (ImageView) popupRegistration.findViewById(R.id.imgValidPassword);
        imgValidMailRegistration = (ImageView) popupRegistration.findViewById(R.id.imgValidMail);
        btnRegistrationAccept = (ImageButton) popupRegistration.findViewById(R.id.btnAccept);
        btnRegistrationDenie = (ImageButton) popupRegistration.findViewById(R.id.btnDenie);

        btnUserRegistration = (ImageButton) findViewById(R.id.btnUserRegistration);

        btnUserRegistration.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupRegistration();
            }
        });

    }

    private void displayPopupRegistration() {

        imgValidPseudoRegistration.setVisibility(View.INVISIBLE);
        imgValidPasswordRegistration.setVisibility(View.INVISIBLE);
        imgValidMailRegistration.setVisibility(View.INVISIBLE);

        // Pour check la validation des champs
        final boolean[] pseudoRegistrationOK = {false};
        final boolean[] passwordRegistrationOK = {false};
        final boolean[] mailRegistrationOK = {false};

        popupRegistration.show();

        final Validation validation = new Validation();

        txtPseudoRegistration.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidPseudoRegistration.setVisibility(View.VISIBLE);
                if (validation.isValidPseudo(txtPseudoRegistration.getText().toString().trim())) {
                    // is true
                    imgValidPseudoRegistration.setColorFilter(getResources().getColor(R.color.green_apple));
                    pseudoRegistrationOK[0] = true;

                } else if(!validation.isValidPseudo(txtPseudoRegistration.getText().toString().trim())) {
                    // is false
                    imgValidPseudoRegistration.setColorFilter(getResources().getColor(R.color.pink));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        txtPasswordRegistration.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidPasswordRegistration.setVisibility(View.VISIBLE);
                if (validation.isValidPassword(txtPasswordRegistration.getText().toString().trim())) {
                    // is true
                    imgValidPasswordRegistration.setColorFilter(getResources().getColor(R.color.green_apple));
                    passwordRegistrationOK[0] = true;

                } else if(!validation.isValidPassword(txtPasswordRegistration.getText().toString().trim())) {
                    // is false
                    imgValidPasswordRegistration.setColorFilter(getResources().getColor(R.color.pink));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        txtMailRegistration.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidMailRegistration.setVisibility(View.VISIBLE);
                if (validation.isValidMail(txtMailRegistration.getText().toString().trim())) {
                    // is true
                    imgValidMailRegistration.setColorFilter(getResources().getColor(R.color.green_apple));
                    mailRegistrationOK[0] = true;

                } else if(!validation.isValidMail(txtMailRegistration.getText().toString().trim())) {
                    // is false
                    imgValidMailRegistration.setColorFilter(getResources().getColor(R.color.pink));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        // Gestion des buttons Accept & Denie
        btnRegistrationAccept.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pseudoRegistrationOK[0] == true && passwordRegistrationOK[0] == true && mailRegistrationOK[0] == true) {
                    addUser();
                    popupRegistration.dismiss();
                    popupSuccess.show();
                    // Permet de faire aparaitre le panneau 1 seconde sans interventions
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (popupSuccess.isShowing()) {
                                popupSuccess.dismiss();
                            }
                        }
                    }, 1000);
                } else {
                    popupRegistrationDenie.show();
                    // Permet de faire aparaitre le panneau 1 seconde sans interventions
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (popupRegistrationDenie.isShowing()) {
                                popupRegistrationDenie.dismiss();
                            }
                        }
                    }, 1000);

                }
            }
        });


        btnRegistrationDenie.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupRegistration.dismiss();

            }
        });

    }

    private void addUser() {

        txtPseudoRegistration = (EditText) popupRegistration.findViewById(R.id.txtPseudo);
        txtPasswordRegistration = (EditText) popupRegistration.findViewById(R.id.txtPassword);
        txtMailRegistration = (EditText) popupRegistration.findViewById(R.id.txtMail);

        String pseudo = "";
        String password = "";
        String mail = "";
        String avatarLarge = "";
        String avatarSmall = "";

        // Update des champs texte
        try {
            pseudo = txtPseudoRegistration.getText().toString();
            password = txtPasswordRegistration.getText().toString();
            mail = txtMailRegistration.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        User user = new User(null, pseudo, password, mail, avatarLarge, avatarSmall);
        AccesLocalUsers accesLocalUsers = new AccesLocalUsers(UserActivity.this);
        user.setId(accesLocalUsers.addUser(user));

    }

    private void initPopupConnection() {

        popupConnection = new Dialog(UserActivity.this);
        popupConnection.setContentView(R.layout.popup_user_connection);
        popupConnection.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupConnection.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        txtPseudoConnection = (EditText) popupConnection.findViewById(R.id.txtPseudo);
        txtPasswordConnection = (EditText) popupConnection.findViewById(R.id.txtPassword);
        txtMailConnection = (EditText) popupConnection.findViewById(R.id.txtMail);
        imgValidPseudoConnection = (ImageView) popupConnection.findViewById(R.id.imgValidPseudo);
        imgValidPasswordConnection = (ImageView) popupConnection.findViewById(R.id.imgValidPassword);
        imgValidMailConnection = (ImageView) popupConnection.findViewById(R.id.imgValidMail);
        btnConnectionAccept = (ImageButton) popupConnection.findViewById(R.id.btnAccept);
        btnConnectionDenie = (ImageButton) popupConnection.findViewById(R.id.btnDenie);

        btnUserConnection = (ImageButton) findViewById(R.id.btnUserConnection);

        btnUserConnection.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupConnection();
            }
        });

    }

    private void displayPopupConnection() {

        imgValidPseudoConnection.setVisibility(View.INVISIBLE);
        imgValidPasswordConnection.setVisibility(View.INVISIBLE);
        imgValidMailConnection.setVisibility(View.INVISIBLE);

        // Validation des champs texte
        final boolean[] pseudoConnectionOK = {false};
        final boolean[] passwordConnectionOK = {false};
        final boolean[] mailConnectionOK = {false};

        popupConnection.show();

        final Validation validation = new Validation();

        txtPseudoConnection.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidPseudoConnection.setVisibility(View.VISIBLE);
                if (validation.isValidPseudo(txtPseudoConnection.getText().toString().trim())) {
                    // is true
                    imgValidPseudoConnection.setColorFilter(getResources().getColor(R.color.green_apple));
                    pseudoConnectionOK[0] = true;

                } else if(!validation.isValidPseudo(txtPseudoConnection.getText().toString().trim())) {
                    // is false
                    imgValidPseudoConnection.setColorFilter(getResources().getColor(R.color.pink));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        txtPasswordConnection.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidPasswordConnection.setVisibility(View.VISIBLE);
                if (validation.isValidPassword(txtPasswordConnection.getText().toString().trim())) {
                    // is true
                    imgValidPasswordConnection.setColorFilter(getResources().getColor(R.color.green_apple));
                    passwordConnectionOK[0] = true;

                } else if(!validation.isValidPassword(txtPasswordConnection.getText().toString().trim())) {
                    // is false
                    imgValidPasswordConnection.setColorFilter(getResources().getColor(R.color.pink));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        txtMailConnection.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidMailConnection.setVisibility(View.VISIBLE);
                if (validation.isValidMail(txtMailConnection.getText().toString().trim())) {
                    // is true
                    imgValidMailConnection.setColorFilter(getResources().getColor(R.color.green_apple));
                    mailConnectionOK[0] = true;

                } else if(!validation.isValidMail(txtMailConnection.getText().toString().trim())) {
                    // is false
                    imgValidMailConnection.setColorFilter(getResources().getColor(R.color.pink));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        // Gestion des buttons Accept & Denie
        btnConnectionAccept.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pseudoConnectionOK[0] == true && passwordConnectionOK[0] == true && mailConnectionOK[0] == true) {
                    addUser();
                    popupConnection.dismiss();
                    popupSuccess.show();
                    // Permet de faire aparaitre le panneau 1 seconde sans interventions
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (popupSuccess.isShowing()) {
                                popupSuccess.dismiss();
                            }
                        }
                    }, 1000);
                } else {
                    popupRegistrationDenie.show();
                    // Permet de faire aparaitre le panneau 1 seconde sans interventions
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (popupRegistrationDenie.isShowing()) {
                                popupRegistrationDenie.dismiss();
                            }
                        }
                    }, 1000);

                }
            }
        });


        btnConnectionDenie.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupConnection.dismiss();

            }
        });

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
