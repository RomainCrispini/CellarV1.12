package com.romain.cellarv1.vue;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.github.mikephil.charting.data.BarEntry;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.romain.cellarv1.R;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.modele.WineBottle;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.Tools;

import java.io.IOException;
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
    private GoogleMap map;

    // Info Window
    private CardView cardViewInfo;
    private ImageButton btnInfoExit;
    private ImageButton btnCellar;
    private ImageButton btnAddBottle;

    // Boutons Zoom In Out Map
    private ImageButton zoomIn, zoomOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();

        //searchLocation();
        //loadMap();


    }

    /**
     * Commande les boutons zoom de la map
     * @param view
     */
    public void onZoom(View view) {
        if(view.getId() == R.id.zoomIn) {
            map.animateCamera(CameraUpdateFactory.zoomIn());
        }
        if(view.getId() == R.id.zoomOut) {
            map.animateCamera(CameraUpdateFactory.zoomOut());
        }
    }

    private void init() {

        // Map Fragment
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Alpha Boutons Zoom In Out Map
        zoomIn = (ImageButton) findViewById(R.id.zoomIn);
        zoomOut = (ImageButton) findViewById(R.id.zoomOut);
        zoomIn.setAlpha(0.3f);
        zoomOut.setAlpha(0.3f);

        initCurvedNavigationView();
        initFabWineMenu();
        getFabWineMenuValue();
        infoWindow();
    }

    private void infoWindow() {
        cardViewInfo = (CardView) findViewById(R.id.cardViewInfo);
        btnInfoExit = (ImageButton) findViewById(R.id.btnInfoExit);
        btnCellar = (ImageButton) findViewById(R.id.btnCellar);
        btnAddBottle = (ImageButton) findViewById(R.id.btnAddBottle);

        // Animation entrante de la cardView
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(cardViewInfo, "translationY", -400, 0);
        fadeIn.setDuration(500);
        fadeIn.start();

        btnInfoExit.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO IF LATT LONG NON NULL -> NE PAS AFFICHER INFO CARDVIEW
                cardViewInfo.setVisibility(View.GONE);
            }
        });

        btnCellar.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });

        btnAddBottle.setOnClickListener(new ImageButton.OnClickListener() {
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
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(new CurvedBottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.user:
                        //Toast.makeText(UserActivity.this, "USER", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, UserActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.cellar:
                        //Toast.makeText(UserActivity.this, "CELLAR", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.scan:
                        //Toast.makeText(UserActivity.this, "SCAN", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, ScanActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.like:
                        //Toast.makeText(UserActivity.this, "LIKE", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, LikeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                    case R.id.search:
                        //Toast.makeText(UserActivity.this, "SEARCH", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, SearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        //overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        map = googleMap;


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


        /*
        for(int i=0; i<markers_location.length; i++){
            coords_markers.add(new LatLng(
                    lat_markers.get(i).getLatitude,
                    lon_markers.get(i).getLongitude));}
         */




        /*
        AccesLocal accesLocal = new AccesLocal(MainActivity.this);
        ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.recoverWineBottleList();

         */

        Map<String, List<Double>> villes = new HashMap<>();

        List<Double> latlngBordeaux = new ArrayList<Double>();
        latlngBordeaux.add(44.833333);
        latlngBordeaux.add(-0.566667);

        List<Double> latlngParis = new ArrayList<Double>();
        latlngParis.add(48.856697);
        latlngParis.add(2.351462);

        List<Double> latlngLyon = new ArrayList<Double>();
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
            map.addMarker(markerOptions);
        }








        /*

        // Nouvelles dimensions du marker
        int height = 100;
        int width = 100;


        // Test avec la Map villes
        Tools tools = new Tools();
        String etiquette = wineBottleList.get(1).getPictureSmall();
        byte[] decodedByte = Base64.decode(etiquette, 0);
        Bitmap etiquetteBitmap = BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
        Bitmap roundEtiquette = tools.getRoundBitmap(etiquetteBitmap);






        // Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.champ_wine);
        Bitmap smallMarker = Bitmap.createScaledBitmap(roundEtiquette, width, height, false);
        BitmapDescriptor smallMarkerIcon = BitmapDescriptorFactory.fromBitmap(smallMarker);

        // Initialisation default position sur Nancy
        LatLng nancy = new LatLng(48.687646, 6.181732);
        MarkerOptions markerOptions = new MarkerOptions().position(nancy).title(wineBottleList.get(1).getRegion()).icon(smallMarkerIcon);
        map.addMarker(markerOptions);
        LatLng defaultPosition = nancy; // Coordonnées LatLng de Nancy

        CameraUpdate cameraPosition = CameraUpdateFactory.newLatLngZoom(defaultPosition, 5);
        map.moveCamera(cameraPosition);
        map.animateCamera(cameraPosition);

         */







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