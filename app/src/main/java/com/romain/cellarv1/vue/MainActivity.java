package com.romain.cellarv1.vue;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocalCellar;
import com.romain.cellarv1.modele.WineBottle;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    // Déclaration de la Custom FAB et de ses caractéristiques
    private FloatingActionButton fabWineMenu, fabRed, fabRose, fabWhite, fabChamp;
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private Boolean isFABWineMenuOpen = false;

    // Déclaration permission map, si on souhaite localiser le téléphone, ce qui n'est pour l'instant pas le cas
    private static final int MAP_REQUEST_PERMISSION = 100;

    private LocationManager locmanager;
    private static final String TAG = MainActivity.class.getSimpleName();

    private MapFragment mapFragment;
    private GoogleMap googleMap;

    // Boutons Zoom In Out Map
    private ImageButton zoomIn, zoomOut;

    // FAB Search
    private FloatingActionButton searchFAB;

    // Popup
    // Popup
    private Dialog popupInfoMap;
    private ImageButton btnInfoCellar, btnInfoAddBottle, btnInfoExit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        init();

        //searchLocation();
        //loadMap();


    }

    private void init() {

        // Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapView);
        mapFragment.getMapAsync(this);

        // PopupInfo
        popupInfoMap = new Dialog(MainActivity.this);
        popupInfoMap.setContentView(R.layout.popup_info_map);
        popupInfoMap.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupInfoMap.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupInfoMap.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        btnInfoExit = (ImageButton) popupInfoMap.findViewById(R.id.btnInfoExit);
        btnInfoCellar = (ImageButton) popupInfoMap.findViewById(R.id.btnInfoCamera);
        btnInfoAddBottle = (ImageButton) popupInfoMap.findViewById(R.id.btnInfoGallery);

        initCurvedNavigationView();
        initFabWineMenu();
        getFabWineMenuValue();
        displayPopupInfo();
        searchFAB();

    }

    private void searchFAB() {
        searchFAB = (FloatingActionButton) findViewById(R.id.searchFAB);
        searchFAB.setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }

    /**
     * Permet de savoir si au moins une bouteille du retour de la liste contient des coordonnées
     * Ce qui permet de savoir si on affiche ou non l'infoCardView
     */
    private void displayPopupInfo() {

        popupInfoMap.show();

        // Récupération de la liste de toutes les bouteilles SI ET SEULEMENT SI une BDD existe
        AccesLocalCellar accesLocalCellar = new AccesLocalCellar(MainActivity.this);

        if(accesLocalCellar.doesDBExists() == true) {
            ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocalCellar.recoverWineBottleList();
            for(int i = 0; i < wineBottleList.size(); i++) {
                if(wineBottleList.get(i).getLattitude() == null && wineBottleList.get(i).getLongitude() == null
                && wineBottleList.get(i).getLattitude() == 0f && wineBottleList.get(i).getLongitude() == 0f) {

                } else if(wineBottleList.get(i).getLattitude() != null && wineBottleList.get(i).getLongitude() != null
                && wineBottleList.get(i).getLattitude() != 0f && wineBottleList.get(i).getLongitude() != 0f) {
                    popupInfoMap.dismiss();
                }
            }
        } else {
        }

        btnInfoExit.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupInfoMap.dismiss();
            }
        });

        btnInfoCellar.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btnInfoAddBottle.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
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
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
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
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
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
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
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
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
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
        curvedBottomNavigationView.setSelectedItemId(R.id.mapMenu);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(new CurvedBottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.cellarMenu:
                        startActivity(new Intent(MainActivity.this, CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        MainActivity.this.finish();
                        return true;
                    case R.id.scanMenu:
                        startActivity(new Intent(MainActivity.this, ScanActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        MainActivity.this.finish();
                        return true;
                    case R.id.likeMenu:
                        startActivity(new Intent(MainActivity.this, LikeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        MainActivity.this.finish();
                        return true;
                    case R.id.userMenu:
                        startActivity(new Intent(MainActivity.this, UserActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        MainActivity.this.finish();
                        return true;
                }
                return false;
            }
        });
    }


    @Override
    public void onMapReady(final GoogleMap googleMap) {

        // Alpha Boutons Zoom In Out Map
        zoomIn = (ImageButton) findViewById(R.id.zoomIn);
        zoomOut = (ImageButton) findViewById(R.id.zoomOut);
        zoomIn.setAlpha(0.3f);
        zoomOut.setAlpha(0.3f);

        // Zoom in out sur boutons de la map
        zoomIn.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            }
        });

        zoomOut.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleMap.animateCamera(CameraUpdateFactory.zoomOut());
            }
        });

        // Personnalisation des options d'affichage
        UiSettings mapSettings = googleMap.getUiSettings();
        mapSettings.setZoomControlsEnabled(false);
        mapSettings.setMyLocationButtonEnabled(false);

        // Personnalisation de la map liée à res/raw/mapstyle_dark.json par défaut
        try {
            boolean success = googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, R.raw.mapstyle_dark));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }

        // Initialisation default position sur Nancy
        LatLng defaultPosition = new LatLng(48.687646, 6.181732); // Coordonnées LatLng de Nancy
        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(defaultPosition, 5);
        googleMap.moveCamera(cameraPosition);
        googleMap.animateCamera(cameraPosition);












        // Nouvelles dimensions du marker
        int height = 100;
        int width = 100;

        // Récupération de la liste de toutes les bouteilles SI ET SEULEMENT SI une BDD existe
        AccesLocalCellar accesLocalCellar = new AccesLocalCellar(MainActivity.this);
        if(accesLocalCellar.doesDBExists() == true) {
            ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocalCellar.recoverWineBottleList();

            for(int i = 0; i < wineBottleList.size(); i++) {
                if(wineBottleList.get(i).getLattitude() != null && wineBottleList.get(i).getLongitude() != null
                && wineBottleList.get(i).getLattitude() != 0f && wineBottleList.get(i).getLongitude() != 0f) {
                    // Positions
                    String nomVin = wineBottleList.get(i).getDomain();
                    Float latitude = wineBottleList.get(i).getLattitude();
                    Float longitude = wineBottleList.get(i).getLongitude();
                    LatLng coordonnees = new LatLng(latitude, longitude);

                    // Markers ronds
                    Tools tools = new Tools();
                    String etiquette = wineBottleList.get(i).getPictureSmall();
                    byte[] decodedByte = Base64.decode(etiquette, 0);
                    Bitmap etiquetteBitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
                    Bitmap roundEtiquette = tools.getRoundBitmap(etiquetteBitmap);
                    Bitmap smallMarker = Bitmap.createScaledBitmap(roundEtiquette, width, height, false);
                    BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

                    // Affichage des markers
                    MarkerOptions markerOptions = new MarkerOptions().position(coordonnees).title(nomVin).icon(smallMarkerIcon);
                    googleMap.addMarker(markerOptions);
                } else {
                }
            }
        }






        Map<String, List<Double>> villes = new HashMap<>();

        List<Double> latlngBordeaux = new ArrayList<>();
        latlngBordeaux.add(44.833333);
        latlngBordeaux.add(-0.566667);

        List<Double> latlngParis = new ArrayList<>();
        latlngParis.add(48.856697);
        latlngParis.add(2.351462);

        List<Double> latlngLyon = new ArrayList<>();
        latlngLyon.add(45.757814);
        latlngLyon.add(4.832011);

        villes.put("Bordeaux", latlngBordeaux);
        villes.put("Paris", latlngParis);
        villes.put("Lyon", latlngLyon);

        Iterator iterator = villes.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry mapentry = (Map.Entry) iterator.next();

            String nomVille = (String) mapentry.getKey();
            List<Double> coordonnees = (List<Double>) mapentry.getValue();
            //Double latitude = coordonnees.get(0);
            //Double longitude = coordonnees.get(1);
            LatLng ville = new LatLng(coordonnees.get(0), coordonnees.get(1));
            MarkerOptions markerOptions = new MarkerOptions().position(ville).title(nomVille);
            googleMap.addMarker(markerOptions);
        }
























    }

        /*
    private void mapRequestPermission() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            new AlertDialog.Builder(MainActivity.this)
                    .setTitle("Permission")
                    .setMessage("Cellar requiert votre permission pour accéder à votre position")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(MainActivity.this, new String[] {
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION}, MAP_REQUEST_PERMISSION);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();
        } else {
            ActivityCompat.requestPermissions(MainActivity.this, new String[] {Manifest.permission.CAMERA}, MAP_REQUEST_PERMISSION);
        }
    }
    private void validationPermissions() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Appel asynchrone
            ActivityCompat.requestPermissions(this, new String[] {
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
            }, MAP_REQUEST_PERMISSION);

            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        //locmanager = (LocationManager) getSystemService(LOCATION_SERVICE);
        //if (locmanager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
        //    locmanager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 0, this);
        //}
        //if(locmanager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER)){
        //    locmanager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, 10000, 0, this);
        //}
        //if(locmanager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
        //    locmanager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 0, this);
        //}
        //loadMap();
    }
    @Override
    protected void onResume() {
        super.onResume();
        validationPermissions();
        //mapRequestPermission();
    }
    // Méthode activée quand une demande d'activation des permissions est proposée
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == MAP_REQUEST_PERMISSION){
            //mapRequestPermission();
            validationPermissions();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(locmanager != null){
            locmanager.removeUpdates(this);
        }
    }
     */

    /*
    public void searchLocation(View view) {
        EditText locationSearch = (EditText) findViewById(R.id.editText);
        String location = locationSearch.getText().toString();
        List<Address> addressList = null;
        //location = "nancy";
        if(location == null || location.equals("")) {
            Toast.makeText(MainActivity.this, "Merci d'entrer une localité", Toast.LENGTH_SHORT).show();
        }
        else if (location != null || !location.equals("")) {
            Geocoder geocoder = new Geocoder(this);
            try {
                addressList = geocoder.getFromLocationName(location, 10);
            } catch (IOException e) {
                e.printStackTrace();
            }
            Address address = addressList.get(0);
            LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title(location));
            map.animateCamera(CameraUpdateFactory.newLatLng(latLng));
            Toast.makeText(MainActivity.this,address.getLatitude() + " " + address.getLongitude(),Toast.LENGTH_LONG).show();
        }
    }
     */


}