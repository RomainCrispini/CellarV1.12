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
import android.view.animation.OvershootInterpolator;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import androidx.appcompat.widget.SwitchCompat;

import android.widget.EditText;
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
import com.romain.cellarv1.controleur.UserControle;
import com.romain.cellarv1.modele.AccesLocalDbUsers;
import com.romain.cellarv1.modele.User;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.PlaceAutoSuggestAdapter;

import java.util.Arrays;


public class UserActivity extends AppCompatActivity {

    // Déclaration de PopupRegistration et de ses éléments
    private ImageButton btnRegistration;
    private Dialog popupRegistration;
    private Dialog popupSuccess;
    private Dialog popupRegistrationDenie;
    private EditText txtPseudo, txtPassword, txtMail;
    private ImageView imgValidPseudo, imgValidPassword, imgValidMail;
    private ImageButton btnAccept, btnDenie;

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

        initPopupRegistration();
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
        popupRegistrationDenie.setContentView(R.layout.popup_registration_denie);
        popupRegistrationDenie.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupRegistrationDenie.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

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
        btnAccept = (ImageButton) popupRegistration.findViewById(R.id.btnAccept);
        btnDenie = (ImageButton) popupRegistration.findViewById(R.id.btnDenie);

        btnRegistration = (ImageButton) findViewById(R.id.btnRegistration);

        btnRegistration.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayPopupRegistration();
            }
        });

    }

    private void displayPopupRegistration() {

        imgValidPseudo.setVisibility(View.INVISIBLE);
        imgValidPassword.setVisibility(View.INVISIBLE);
        imgValidMail.setVisibility(View.INVISIBLE);

        // Validation des champs texte
        final boolean[] pseudoOK = {false};
        final boolean[] passwordOK = {false};
        final boolean[] mailOK = {false};

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
                    pseudoOK[0] = true;

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
                    passwordOK[0] = true;

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
                    mailOK[0] = true;

                } else if(!userControle.isValidMail(txtMail.getText().toString().trim())) {
                    // is false
                    imgValidMail.setColorFilter(getResources().getColor(R.color.pink));
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });

        // Gestion des buttons Accept & Denie
        btnAccept.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(pseudoOK[0] == true && passwordOK[0] == true && mailOK[0] == true) {
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


        btnDenie.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupRegistration.dismiss();

            }
        });

    }

    private void addUser() {

        txtPseudo = (EditText) popupRegistration.findViewById(R.id.txtPseudo);
        txtPassword = (EditText) popupRegistration.findViewById(R.id.txtPassword);
        txtMail = (EditText) popupRegistration.findViewById(R.id.txtMail);

        String pseudo = "";
        String password = "";
        String mail = "";
        String avatarLarge = "";
        String avatarSmall = "";

        // Update des champs texte
        try {
            pseudo = txtPseudo.getText().toString();
            password = txtPassword.getText().toString();
            mail = txtMail.getText().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }


        User user = new User(null, pseudo, password, mail, avatarLarge, avatarSmall);
        AccesLocalDbUsers accesLocalDbUsers = new AccesLocalDbUsers(UserActivity.this);
        user.setId(accesLocalDbUsers.addUser(user));

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
