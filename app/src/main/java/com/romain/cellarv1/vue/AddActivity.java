package com.romain.cellarv1.vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.location.Address;
import android.location.Geocoder;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.Size;
import android.util.SparseArray;
import android.view.MenuItem;
import android.view.Surface;
import android.view.TextureView;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AddressComponents;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.romain.cellarv1.R;
import com.romain.cellarv1.controleur.Controle;
import com.romain.cellarv1.modele.AccesLocalCellar;
import com.romain.cellarv1.modele.WineBottle;
import com.romain.cellarv1.outils.BlurBitmap;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.ProgressBarAnimation;
import com.romain.cellarv1.outils.Tools;
import com.romain.cellarv1.outils.Validation;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import me.tankery.lib.circularseekbar.CircularSeekBar;


public class AddActivity extends AppCompatActivity {

    /**
     * Propriétés
     */

    private OvershootInterpolator interpolator = new OvershootInterpolator();

    // Appareil photo et gallery
    private String photoPath = null;
    private static final int GALLERY_READ_REQUEST_PERMISSION = 100;
    private static final int CAMERA_REQUEST_PERMISSION = 101;
    // private static final int GALLERY_WRITE_REQUEST_PERMISSION = 102;
    private static final int CAMERA_REQUEST_CODE = 103;
    private static final int GALLERY_REQUEST_CODE = 104;

    private ImageView scanImageView;
    private LinearLayout layapp;

    // Gallery
    private ImageButton btnGallery;

    // Liste Pays
    private ArrayList<String> countryList = new ArrayList<>();

    // ProgessBar
    private ProgressBar progressBar;

    // Champs texte
    private AutoCompleteTextView txtCountry, txtRegion;
    private AutoCompleteTextView txtAddress;
    private EditText txtDomain, txtAppellation;
    private EditText nbYear, nbApogee, nbNumber, nbEstimate;
    private TextView nbRate;

    // Boutons WineColor
    private ImageButton btnRed, btnRose, btnWhite, btnChamp;

    // Bouton add
    private FloatingActionButton btnAdd;

    // Boutons SeekBar
    private ImageButton btnRoundMillesime, btnRoundApogee, btnRoundNumber, btnRoundEstimate;

    // PopupInfoLabelBottle
    private Dialog popupInfoLabelBottle;
    private ImageButton btnInfoCamera, btnInfoGallery, btnInfoExit;

    // PopupCircularSeekBar
    private Dialog popupMillesimeSeekBar, popupApogeeSeekBar, popupNumberSeekBar, popupEstimateSeekBar;
    private CircularSeekBar millesimeSeekBar, apogeeSeekBar, numberSeekBar, estimateSeekBar;
    private TextView txtMillesimeSeekBar, txtApogeeSeekBar, txtNumberSeekBar, txtEstimateSeekBar;
    private ImageButton btnMillesimeAccept, btnApogeeAccept, btnNumberAccept, btnEstimateAccept;
    private ImageButton btnMillesimeDenie, btnApogeeDenie, btnNumberDenie, btnEstimateDenie;

    // Déclaration du contrôleur
    private Controle controle;

    // PopupRateSeekBar
    private Dialog popupRateSeekBar;
    private CircularSeekBar rateSeekBar;
    private ImageButton btnRateAccept, btnRateDenie;
    private TextView txtRateSeekBar;

    // Popups Add et Success
    private Dialog popupAdd, popupSuccess;
    private ImageView imageFavorite, imageWish;
    private TextView nbRatePopup;

    // Buttons Appreciations
    private ToggleButton btnFavorite;
    private ToggleButton btnWishlist;

    // Image Vignoble & WineColor
    private ImageView imgVignoble;
    private ImageView imgWineColor;

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

    // Images des indicateurs de validation
    private ImageView imgValidCountry, imgValidRegion, imgValidDomain, imgValidAppellation, imgValidAddress;

    private PlacesClient placesClient;

    // TextureView et gestion de l'appareil photo
    private ImageButton btnPhoto;
    private ImageButton btnCancelPhoto;
    private TextureView textureView;
    private static final SparseArray ORIENTATIONS = new SparseArray();

    private String cameraId;
    CameraDevice cameraDevice;
    CameraCaptureSession cameraCaptureSession;
    CaptureRequest captureRequest;
    CaptureRequest.Builder captureRequestBuilder;

    private Size imageDimensions;
    private ImageReader imageReader;
    private File file;
    Handler mBackgroundHandler;
    HandlerThread mBackgroundThread;

    // Fragment AutoComplete
    Fragment autocompleteFrag;

    // On déclare une ArrayList qui va permettre de stocker LatLng de l'autocompleteAddress
    private ArrayList<Double> latlngAddress = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        init();


        textureView = (TextureView) findViewById(R.id.textureView);
        btnPhoto = (ImageButton) findViewById(R.id.btnPhoto);
        textureView.setSurfaceTextureListener(textureListener);

        btnPhoto.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePicture();
            }
        });

    }

    /**
     * Méthode qui initialise les liens avec les objets graphiques, et appelle toutes les méthodes
     */
    private void init() {

        CurvedBottomNavigationView curvedBottomNavigationView = findViewById(R.id.curvedBottomNavigationView);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(customBottomNavListener);
        txtCountry = (AutoCompleteTextView) findViewById(R.id.textCountry);
        txtRegion = (AutoCompleteTextView) findViewById(R.id.textRegion);
        btnRed = (ImageButton) findViewById(R.id.redWineButton);
        btnRose = (ImageButton) findViewById(R.id.roseWineButton);
        btnWhite = (ImageButton) findViewById(R.id.whiteWineButton);
        btnChamp = (ImageButton) findViewById(R.id.champWineButton);
        txtDomain = (EditText) findViewById(R.id.textDomain);
        txtAppellation = (EditText) findViewById(R.id.textAppellation);
        txtAddress = (AutoCompleteTextView) findViewById(R.id.textAddress);
        nbYear = (EditText) findViewById(R.id.nbYear);
        nbApogee = (EditText) findViewById(R.id.nbApogee);
        nbNumber = (EditText) findViewById(R.id.nbNumber);
        nbEstimate = (EditText) findViewById(R.id.nbEstimate);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnPhoto = (ImageButton) findViewById(R.id.btnPhoto);
        this.controle = Controle.getInstance(this); // Création d'une instance de type Controle

        btnGallery = (ImageButton) findViewById(R.id.btnGallery);
        scanImageView = (ImageView) findViewById(R.id.scanImageView);

        // On laisse le bouton btnCancelPhoto inopérant et invisible au chargement
        btnCancelPhoto = (ImageButton) findViewById(R.id.btnCancelPhoto);
        btnCancelPhoto.setVisibility(View.GONE);

        // PopupAdd & PopupSuccess
        popupAdd = new Dialog(AddActivity.this);
        popupAdd.setContentView(R.layout.popup_add_bottle);
        popupAdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupAdd.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        popupSuccess = new Dialog(AddActivity.this);
        popupSuccess.setContentView(R.layout.popup_success_add_bottle);
        popupSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupSuccess.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        // PopupInfo
        popupInfoLabelBottle = new Dialog(AddActivity.this);
        popupInfoLabelBottle.setContentView(R.layout.popup_info_label_bottle);
        popupInfoLabelBottle.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupInfoLabelBottle.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupInfoLabelBottle.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        btnInfoExit = (ImageButton) popupInfoLabelBottle.findViewById(R.id.btnInfoExit);
        btnInfoCamera = (ImageButton) popupInfoLabelBottle.findViewById(R.id.btnInfoCamera);
        btnInfoGallery = (ImageButton) popupInfoLabelBottle.findViewById(R.id.btnInfoGallery);

        // PopupMillesimeSeekBar
        popupMillesimeSeekBar = new Dialog(AddActivity.this);
        popupMillesimeSeekBar.setContentView(R.layout.popup_add_millesime);
        popupMillesimeSeekBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupMillesimeSeekBar.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupMillesimeSeekBar.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        millesimeSeekBar = (CircularSeekBar) popupMillesimeSeekBar.findViewById(R.id.millesimeSeekBar);
        txtMillesimeSeekBar = (TextView) popupMillesimeSeekBar.findViewById(R.id.txtMillesimeSeekBar);
        btnMillesimeAccept = (ImageButton) popupMillesimeSeekBar.findViewById(R.id.btnAccept);
        btnMillesimeDenie = (ImageButton) popupMillesimeSeekBar.findViewById(R.id.btnDenie);

        // PopupApogeeSeekBar
        popupApogeeSeekBar = new Dialog(AddActivity.this);
        popupApogeeSeekBar.setContentView(R.layout.popup_add_apogee);
        popupApogeeSeekBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupApogeeSeekBar.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupApogeeSeekBar.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        apogeeSeekBar = (CircularSeekBar) popupApogeeSeekBar.findViewById(R.id.apogeeSeekBar);
        txtApogeeSeekBar = (TextView) popupApogeeSeekBar.findViewById(R.id.txtApogeeSeekBar);
        btnApogeeAccept = (ImageButton) popupApogeeSeekBar.findViewById(R.id.btnAccept);
        btnApogeeDenie = (ImageButton) popupApogeeSeekBar.findViewById(R.id.btnDenie);

        // PopupNumberSeekBar
        popupNumberSeekBar = new Dialog(AddActivity.this);
        popupNumberSeekBar.setContentView(R.layout.popup_add_number);
        popupNumberSeekBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupNumberSeekBar.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupNumberSeekBar.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        numberSeekBar = (CircularSeekBar) popupNumberSeekBar.findViewById(R.id.numberSeekBar);
        txtNumberSeekBar = (TextView) popupNumberSeekBar.findViewById(R.id.txtNumberSeekBar);
        btnNumberAccept = (ImageButton) popupNumberSeekBar.findViewById(R.id.btnAccept);
        btnNumberDenie = (ImageButton) popupNumberSeekBar.findViewById(R.id.btnDenie);

        // PopupEstimateSeekBar
        popupEstimateSeekBar = new Dialog(AddActivity.this);
        popupEstimateSeekBar.setContentView(R.layout.popup_add_estimate);
        popupEstimateSeekBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupEstimateSeekBar.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        popupEstimateSeekBar.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        estimateSeekBar = (CircularSeekBar) popupEstimateSeekBar.findViewById(R.id.estimateSeekBar);
        txtEstimateSeekBar = (TextView) popupEstimateSeekBar.findViewById(R.id.txtEstimateSeekBar);
        btnEstimateAccept = (ImageButton) popupEstimateSeekBar.findViewById(R.id.btnAccept);
        btnEstimateDenie = (ImageButton) popupEstimateSeekBar.findViewById(R.id.btnDenie);

        // PopupRateSeekBar
        popupRateSeekBar = new Dialog(AddActivity.this);
        popupRateSeekBar.setContentView(R.layout.popup_add_rate);
        popupRateSeekBar.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupRateSeekBar.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        rateSeekBar = (CircularSeekBar) popupRateSeekBar.findViewById(R.id.rateSeekBar);
        txtRateSeekBar = (TextView) popupRateSeekBar.findViewById(R.id.txtRateSeekBar);
        btnRateAccept = (ImageButton) popupRateSeekBar.findViewById(R.id.btnAccept);
        btnRateDenie = (ImageButton) popupRateSeekBar.findViewById(R.id.btnDenie);
        nbRate = (TextView) findViewById(R.id.nbRate);

        // Toggle Buttons
        btnFavorite = (ToggleButton) findViewById(R.id.btnFavorite);
        btnFavorite.setText(null);
        btnFavorite.setTextOn(null);
        btnFavorite.setTextOff(null);
        btnWishlist = (ToggleButton) findViewById(R.id.btnWishlist);
        btnWishlist.setText(null);
        btnWishlist.setTextOn(null);
        btnWishlist.setTextOff(null);

        imgWineColor = (ImageView) findViewById(R.id.imgWineColor);

        // ImageView des indicateurs de validation
        imgValidCountry = (ImageView) findViewById(R.id.imgValidCountry);
        imgValidRegion = (ImageView) findViewById(R.id.imgValidRegion);
        imgValidDomain = (ImageView) findViewById(R.id.imgValidDomain);
        imgValidAppellation = (ImageView) findViewById(R.id.imgValidAppellation);
        imgValidAddress = (ImageView) findViewById(R.id.imgValidAddress);

        // Instanciation et animation du menuBis coulissant
        FrameLayout menuBis = (FrameLayout) findViewById(R.id.menuBis);
        menuBis.setTranslationY(300f);
        menuBis.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

        // PopupInfo
        displayPopupInfo();

        // Gestion des RoundButtons et ouverture PopupSeekBar
        gestionRoundButtonsSeekBar();

        addWineBottle();
        //recoverWineBottle();
        recoverFABWineColor();
        recoverJsonCountries();
        getRegionsList();
        gestionProgressBarValidation();

        gestionImageVignoble();
        gestionWineColorSelector();

        autocompleteAddress();




    }




    private ArrayList<Double> autocompleteAddress() {

        String apikey = getResources().getString(R.string.map_key);

        if(!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), apikey);
        }

        placesClient = Places.createClient(this);




        final AutocompleteSupportFragment autocompleteSupportFragment =
                (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocompleteAddress);

        // On passe le fragment INVISIBLE mais CLIQUABLE
        autocompleteSupportFragment.getView().setAlpha(0f);

        assert autocompleteSupportFragment != null;
        autocompleteSupportFragment.setPlaceFields(Arrays.asList(Place.Field.ADDRESS, Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS_COMPONENTS));

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {


                Toast.makeText(AddActivity.this, "lattitude : " + place.getLatLng().latitude + "longitude" + place.getLatLng().longitude, Toast.LENGTH_SHORT).show();


                double latitude = place.getLatLng().latitude;
                double longitude = place.getLatLng().longitude;
                latlngAddress.add(latitude);
                latlngAddress.add(longitude);


                // TODO PROBLEME !!!!!!!!!!!!!!
                /*
                try {
                    getPlaceInfo(place.getLatLng().latitude,place.getLatLng().longitude);
                } catch (IOException e) {

                   e.printStackTrace();
                }


*/

                String address = place.getName();
                txtAddress.setText(address);


                Geocoder mGeocoder = new Geocoder(AddActivity.this, Locale.getDefault());
                try {

                    List<Address> addresses = null;
                    addresses = mGeocoder.getFromLocation(latitude, longitude, 1);
                    if (addresses.get(0).getCountryName() != null) {
                        String country = addresses.get(0).getCountryName();
                        txtCountry.setText(country);
                        String region = addresses.get(0).getAdminArea();
                        txtRegion.setText(region);

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }














            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });

        return latlngAddress;

    }

    /*
    private void getPlaceInfo(double lat, double lon) throws IOException {

        Geocoder mGeocoder = new Geocoder(AddActivity.this);
        List<Address> addresses = mGeocoder.getFromLocation(lat, lon, 1);

        if (addresses.get(0).getCountryName() != null) {
            String country = addresses.get(0).getCountryName();
            txtCountry.setText(country);
            //Log.d("COUNTRY",country);
        }

        if (addresses.get(0).getAdminArea() != null) {
            String region = addresses.get(0).getAdminArea();
            txtRegion.setText(region);
            //Log.d("REGION",region);
        }

        if (addresses.get(0).getAdminArea() != null) {
            String address = addresses.get(0).getThoroughfare();
            txtAddress.setText(address);
            //Log.d("ADDRESS",address);
        }

    }

     */


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 101) {
            if(grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(getApplicationContext(), "Désolé, pas de permission", Toast.LENGTH_SHORT).show();
                // finish();
            }
        }
    }

    TextureView.SurfaceTextureListener textureListener = new TextureView.SurfaceTextureListener() {
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surface) {

        }
    };

    private final CameraDevice.StateCallback stateCallBack = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            cameraDevice = camera;
            try {
                createCameraPreview();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            cameraDevice.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            cameraDevice.close();
            cameraDevice = null;
        }
    };

    private void createCameraPreview() throws CameraAccessException {
        SurfaceTexture texture = textureView.getSurfaceTexture();
        texture.setDefaultBufferSize(imageDimensions.getWidth(), imageDimensions.getHeight());
        Surface surface = new Surface(texture);
        captureRequestBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        captureRequestBuilder.addTarget(surface);
        cameraDevice.createCaptureSession(Arrays.asList(surface), new CameraCaptureSession.StateCallback() {
            @Override
            public void onConfigured(CameraCaptureSession session) {
                if(cameraDevice == null) {
                    return;
                }
                cameraCaptureSession = session;
                try {
                    updatePreview();
                } catch (CameraAccessException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onConfigureFailed(CameraCaptureSession session) {
                Toast.makeText(getApplicationContext(), "Configuration changed", Toast.LENGTH_SHORT).show();
            }
        }, null);
    }

    private void updatePreview() throws CameraAccessException {
        if(cameraDevice == null) {
            return;
        }

        captureRequestBuilder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
        cameraCaptureSession.setRepeatingRequest(captureRequestBuilder.build(), null, mBackgroundHandler);
    }

    private void openCamera() throws CameraAccessException {
        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        cameraId = manager.getCameraIdList()[0];
        CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);
        StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        imageDimensions = map.getOutputSizes(SurfaceTexture.class)[0];

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(AddActivity.this, new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 101);
            return;
        }
        manager.openCamera(cameraId, stateCallBack, null);

    }

    private void cancelPhoto() {
        btnCancelPhoto.setAlpha(0.4f);
        btnCancelPhoto.setVisibility(View.VISIBLE);
        btnCancelPhoto.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanImageView.setImageResource(0);
                btnCancelPhoto.setVisibility(View.GONE);
            }
        });
    }

    private void takePicture() {
        Bitmap bitmap = textureView.getBitmap();
        scanImageView.setImageBitmap(bitmap);
        cancelPhoto();
    }

    @Override
    protected void onResume() {
        super.onResume();

        startBackgroundThread();

        if(textureView.isAvailable()) {
            try {
                openCamera();
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        } else {
            textureView.setSurfaceTextureListener(textureListener);
        }
    }

    private void startBackgroundThread() {
        mBackgroundThread = new HandlerThread("Camera Background");
        mBackgroundThread.start();
        mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
    }

    @Override
    protected void onPause() {
        try {
            stopBackgroundThread();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        super.onPause();
    }

    protected void stopBackgroundThread() throws InterruptedException {
        mBackgroundThread.quitSafely();
        mBackgroundThread.join();
        mBackgroundThread = null;
        mBackgroundHandler = null;
    }

    private void gestionImageVignoble() {

        // L'image par défaut est vignoble_alsace
        imgVignoble = (ImageView) findViewById(R.id.imgVignoble);
        imgVignoble.setBackgroundResource(R.drawable.vignoble_alsace);
        // On floute par défaut cette image
        Bitmap bitmap = ((BitmapDrawable) imgVignoble.getBackground()).getBitmap();
        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 20f));

        txtRegion.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                Bitmap bitmap = ((BitmapDrawable) imgVignoble.getBackground()).getBitmap();

                // On switch avec toutes les régions et on retire le flou si une région est sélectionnée
                switch (txtRegion.getText().toString().trim()) {
                    case "Alsace":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_alsace);
                        // On retire le flou de l'image
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Beaujolais":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_beaujolais);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Bourgogne":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_bourgogne);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Bretagne":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_bretagne);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Champagne":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_champagne);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Charentes":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_charentes);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Corse":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_corse);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Ile-de-France":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_ile_france);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Jura":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_jura);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Languedoc-Roussillon":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_languedoc);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Lorraine":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_lorraine);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Lyonnais":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_loyonnais);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Nord-Pas-De-Calais":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_npc);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Normandie":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_normandie);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Picardie":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_picardie);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Provence":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_provence);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Savoie-Bugey":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_savoie);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Sud-Ouest":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_sud_ouest);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Tahiti":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_tahiti);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Val de Loire":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_val_loire);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Vallée du Rhône":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_vallee_rhone);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    default:
                        // On remet l'image vignoble_alsace dans tout les autres cas
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_alsace);
                        // On remet le flou sur l'image dans tout les autres cas
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 20f));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

                Bitmap bitmap = ((BitmapDrawable) imgVignoble.getBackground()).getBitmap();

                // On switch avec toutes les régions et on retire le flou si une région est sélectionnée
                switch (txtRegion.getText().toString().trim()) {
                    case "Alsace":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_alsace);
                        // On retire le flou de l'image
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Beaujolais":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_beaujolais);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Bourgogne":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_bourgogne);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Bretagne":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_bretagne);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Champagne":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_champagne);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Charentes":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_charentes);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Corse":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_corse);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Ile-de-France":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_ile_france);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Jura":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_jura);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Languedoc-Roussillon":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_languedoc);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Lorraine":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_lorraine);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Lyonnais":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_loyonnais);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Nord-Pas-De-Calais":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_npc);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Normandie":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_normandie);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Picardie":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_picardie);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Provence":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_provence);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Savoie-Bugey":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_savoie);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Sud-Ouest":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_sud_ouest);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Tahiti":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_tahiti);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Val de Loire":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_val_loire);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    case "Vallée du Rhône":
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_vallee_rhone);
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 0f));
                        break;
                    default:
                        // On remet l'image vignoble_alsace dans tout les autres cas
                        imgVignoble.setBackgroundResource(R.drawable.vignoble_alsace);
                        // On remet le flou sur l'image dans tout les autres cas
                        imgVignoble.setImageBitmap(new BlurBitmap().blur(AddActivity.this, bitmap, 20f));
                }

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

        });


    }

    private void gestionRoundButtonsSeekBar() {
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

    @SuppressLint("SetTextI18n")
    private void displayPopupRateSeekBar() {

        if (nbRate.getText().toString().trim().equals("*/10")) {
            txtRateSeekBar.setText("*/10");
            txtRateSeekBar.setTextColor(getResources().getColor(R.color.green_light));
            //Sinon, on récupère la valeur de nbRate, qu'on réaffiche
        } else {
            txtRateSeekBar.setText(nbRate.getText().toString().trim());
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
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(CircularSeekBar circularSeekBar, float progress, boolean fromUser) {
                txtRateSeekBar.setText((int) progress + "/10");
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

    private void displayPopupMillesimeSeekBar() {

        popupMillesimeSeekBar.show();

        btnMillesimeAccept.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                nbYear.setText(String.valueOf(txtMillesimeSeekBar.getText()));
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
                nbApogee.setText(String.valueOf(txtApogeeSeekBar.getText()));
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
                nbNumber.setText(String.valueOf(txtNumberSeekBar.getText()));
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
                nbEstimate.setText(nbEstimateRaw.substring(0, nbEstimateRaw.length() - 2));
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

    private void displayPopupInfo() {

        popupInfoLabelBottle.show();

        // Récupération de la liste de toutes les bouteilles SI ET SEULEMENT SI une BDD existe
        AccesLocalCellar accesLocalCellar = new AccesLocalCellar(AddActivity.this);

        if (accesLocalCellar.doesDBExists() == true) {
            ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocalCellar.recoverWineBottleList();
            for (int i = 0; i < wineBottleList.size(); i++) {
                if (wineBottleList.get(i).getPictureSmall().equals("")) {
                } else if (!wineBottleList.get(i).getPictureSmall().equals("")) {
                    popupInfoLabelBottle.dismiss();
                }
            }
        } else {
        }

        btnInfoExit.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupInfoLabelBottle.dismiss();
            }
        });

    }

    // Au retour de la sélection d'une image (après appel de startactivityforresult)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView scanImageView = (ImageView) findViewById(R.id.scanImageView);
        // Vérification du bon code de retour et l'état du retour ok
        if (requestCode == GALLERY_REQUEST_CODE && resultCode == RESULT_OK && data != null) { // On vérifie si une image existe et est récupérée
            // Accès à l'image à partir de data
            Uri selectedImage = data.getData();
            // Mémorisation du path précis
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            // Curseur d'accès au chemin de l'image
            Cursor cursor = this.getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            // Position sur la première ligne (normalement une seule)
            cursor.moveToFirst();
            // Récupération du chemin précis de l'image
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgPath = cursor.getString(columnIndex);
            cursor.close();
            // Récupération de l'image
            Bitmap imageGallery = BitmapFactory.decodeFile(imgPath);
            // Redimentionner l'image, en fait, on ne le fait pas
            imageGallery = changeSizeBitmap(imageGallery, 2f);
            // Affichage de l'image
            scanImageView.setImageBitmap(imageGallery);
            // On fait apparaitre la possibilité d'effacer la photo
            cancelPhoto();
        } else {
        }

    }

    public void accesGallery(View view) {
        // Permission pour accès Gallery
        if (ActivityCompat.checkSelfPermission(AddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Accès à la gallery du tel
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        } else {
            galleryRequestPermission();
        }
    }

    private Bitmap changeSizeBitmap(Bitmap bitmap, float proportion) {
        // Métrique qui permet de récupérer les dimensions de l'écran
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // Taille de l'écran (et affecter les proportions)
        float screenHeight = metrics.heightPixels * proportion;
        float screenWidth = metrics.widthPixels * proportion;
        // Taille de l'image
        float bitmapHeight = bitmap.getHeight();
        float bitmapWidth = bitmap.getWidth();
        // Calcul du ratio entre taille image et écran
        float ratioHeight = screenHeight / bitmapHeight;
        float ratioWidth = screenWidth / bitmapWidth;
        // Récupération du plus petit ratio
        float ratio = Math.min(ratioHeight, ratioWidth);
        // Redimentionnement de l'image par rapport au ratio
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmapWidth * ratio), (int) (bitmapHeight * ratio), true);
        // envoie la nouvelle image
        return bitmap;
    }

    private void galleryRequestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(AddActivity.this)
                    .setTitle("Permission")
                    .setMessage("Cellar requiert votre permission pour accéder à votre galerie d'images")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_READ_REQUEST_PERMISSION);
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
            ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_READ_REQUEST_PERMISSION);
        }
    }

    private void cameraRequestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(AddActivity.this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(AddActivity.this)
                    .setTitle("Permission")
                    .setMessage("Cellar requiert votre permission pour accéder à votre appareil photo")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_PERMISSION);
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
            ActivityCompat.requestPermissions(AddActivity.this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_PERMISSION);
        }
    }

    /**
     * Test si le scanImageView contient un Bitmap
     */
    private boolean doesImageLabel() {

        boolean test;
        BitmapDrawable drawable = (BitmapDrawable) scanImageView.getDrawable();
        if (drawable == null) {
            test = false;
        } else {
            test = true;
        }
        return test;
    }

    private String getPictureLarge() {

        String pictureLarge = "";
        Tools tools = new Tools();

        if (doesImageLabel()) {
            BitmapDrawable drawable = (BitmapDrawable) scanImageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            Bitmap bitmap500 = tools.getResizedBitmap500px(bitmap);

            // Enregistrement de bitmap500 dans pictureLarge
            ByteArrayOutputStream stream500 = new ByteArrayOutputStream();
            bitmap500.compress(Bitmap.CompressFormat.PNG, 0, stream500);
            byte[] b500 = stream500.toByteArray();
            String image500 = Base64.encodeToString(b500, Base64.DEFAULT);
            pictureLarge = image500;
            try {
                stream500.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!doesImageLabel()) {
            pictureLarge = "";
        }

        return pictureLarge;
    }

    private String getPictureSmall() {

        String pictureSmall = "";
        Tools tools = new Tools();

        if (doesImageLabel()) {
            BitmapDrawable drawable = (BitmapDrawable) scanImageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            Bitmap bitmap100 = tools.getResizedBitmap100px(bitmap);

            // Enregistrement de bitmap100 dans pictureSmall
            //ByteArrayOutputStream stream100 = new ByteArrayOutputStream(bitmap100.getWidth() * bitmap.getHeight());
            ByteArrayOutputStream stream100 = new ByteArrayOutputStream();
            bitmap100.compress(Bitmap.CompressFormat.PNG, 0, stream100);
            byte[] b100 = stream100.toByteArray();
            String image100 = Base64.encodeToString(b100, Base64.DEFAULT);
            pictureSmall = image100;
            try {
                stream100.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!doesImageLabel()) {
            pictureSmall = "";
        }

        return pictureSmall;
    }

        /*
    public void askCameraPermissions(View view) {
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.CAMERA}, CAMERA_PERM_CODE);
        } else {
            openCamera();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == CAMERA_PERM_CODE) {
            if(grantResults.length < 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCamera();
            } else {
                Toast.makeText(this, "L'accès à la caméra est requis", Toast.LENGTH_LONG).show();
            }

        }
    }
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView scanImageView = (ImageView) findViewById(R.id.scanImageView); // DOIT ETRE DECLAREE ICI !!!!!!!!!!!!!!!!!!!!!!
        if(requestCode == CAMERA_REQUEST_CODE) {
            Bitmap image = (Bitmap) data.getExtras().get("data");
            scanImageView.setImageBitmap(image);
        }
    }
     */


    /**
     * Ajout d'une nouvelle bouteille et de toutes ses caractéristiques
     */
    private void addWineBottle() {
        ((FloatingActionButton) findViewById(R.id.btnAdd)).setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView imageFavorite = (ImageView) popupAdd.findViewById(R.id.imageFavorite);
                ImageView imageWish = (ImageView) popupAdd.findViewById(R.id.imageWish);

                ImageButton btnAccept = (ImageButton) popupAdd.findViewById(R.id.btnAccept);
                ImageButton btnDenie = (ImageButton) popupAdd.findViewById(R.id.btnDenie);
                TextView number = (TextView) popupAdd.findViewById(R.id.number);
                ImageView imageWineColor = (ImageView) popupAdd.findViewById(R.id.imageWineColor);
                ImageView imageBottle = (ImageView) popupAdd.findViewById(R.id.imageBottle);
                TextView region = (TextView) popupAdd.findViewById(R.id.region);
                TextView domain = (TextView) popupAdd.findViewById(R.id.domain);
                TextView appellation = (TextView) popupAdd.findViewById(R.id.appellation);
                TextView millesime = (TextView) popupAdd.findViewById(R.id.millesime);


                // Récupération de l'étiquette
                if (scanImageView.getDrawable() != null) {
                    //Bitmap bitmapEtiquette = ((BitmapDrawable) scanImageView.getDrawable()).getBitmap();
                    Bitmap bitmapEtiquette = textureView.getBitmap();
                    imageBottle.setImageBitmap(bitmapEtiquette);
                } else {
                }


                // Récupération de la couleur
                if (btnRed.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.red_wine_listview);
                } else if (btnRose.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.rose_wine_listview);
                } else if (btnWhite.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.white_wine_listview);
                } else if (btnChamp.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.champ_wine_listview);
                }

                // Récupération des appréciations
                if (btnFavorite.isChecked()) {
                    imageFavorite.setColorFilter(getResources().getColor(R.color.green_apple));
                } else {
                    imageFavorite.setColorFilter(getResources().getColor(R.color.green_light));
                }

                if (btnWishlist.isChecked()) {
                    imageWish.setColorFilter(getResources().getColor(R.color.green_apple));
                } else {
                    imageWish.setColorFilter(getResources().getColor(R.color.green_light));
                }

                // On gère la couleur de la note sur la popupUpdate suivant sa valeur
                nbRatePopup = (TextView) popupAdd.findViewById(R.id.nbRatePopup);
                String ratePopup = nbRate.getText().toString();
                if (ratePopup.trim().equals("*/10")) {
                    nbRatePopup.setText("*/10");
                    nbRatePopup.setTextColor(getResources().getColor(R.color.green_middle_light));
                } else {
                    nbRatePopup.setText(ratePopup);
                    nbRatePopup.setTextColor(getResources().getColor(R.color.green_apple));
                }

                // Récupération des champs texte
                number.setText(nbNumber.getText());
                region.setText(txtRegion.getText());
                domain.setText(txtDomain.getText());
                appellation.setText(txtAppellation.getText());
                millesime.setText(nbYear.getText());

                // Affichage de la popup
                popupAdd.show();


                btnAccept.setOnClickListener(new Button.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*
                        if(txtCountry.getText().toString().equals("")) {
                            txtRegion.setError("tt");
                            Drawable warning = getResources().getDrawable(R.drawable.ic_add_black_24dp);
                            //add an error icon to your drawable files
                            warning.setBounds(0, 0, warning.getIntrinsicWidth(), warning.getIntrinsicHeight());

                            txtRegion.setCompoundDrawables(null,null, warning,null);
                            //txtRegion.setErrorColor(Color.BLUE);
                            btnAdd.setBackgroundColor(Color.RED);
                        }

                         */

                        String country = "";
                        String region = "";
                        String domain = "";
                        String appellation = "";
                        String address = "";
                        String wineColor = "";
                        int year = 0;
                        int apogee = 0;
                        int number = 0;
                        int estimate = 0;
                        byte[] imageLarge = null;
                        byte[] imageSmall = null;
                        int rate;
                        String favorite = "0";
                        String wish = "0";
                        double latitude = 0;
                        double longitude = 0;

                        // Update des Latitude et Longitude SI elles ne sont pas nulles
                        try {
                            latitude = autocompleteAddress().get(0);
                            longitude = autocompleteAddress().get(1);
                        } catch (Exception e) {
                            e.printStackTrace();
                            //Toast.makeText(AddActivity.this, "PROBLEME", Toast.LENGTH_SHORT).show();
                        }


                        // Update de la date
                        Long timeStampLong = System.currentTimeMillis(); // Résultat en millisecondes
                        String timeStamp = timeStampLong.toString();

                        // Update de la note
                        if (nbRate.getText().toString().trim().equals("*/10")) {
                            rate = 0;
                        } else {
                            // On retire les 3 derniers caractères : "/10"
                            rate = Integer.parseInt(nbRate.getText().toString().trim().substring(0, nbRate.length() - 3));
                        }

                        // Update de la couleur, des appréciations et des champs texte
                        try {
                            if (btnRed.getAlpha() == 1f) {
                                wineColor = "Rouge";
                            } else if (btnRose.getAlpha() == 1f) {
                                wineColor = "Rose";
                            } else if (btnWhite.getAlpha() == 1f) {
                                wineColor = "Blanc";
                            } else if (btnChamp.getAlpha() == 1f) {
                                wineColor = "Effervescent";
                            }

                            if (btnFavorite.isChecked()) {
                                favorite = "1";
                            } else {
                                favorite = "0";
                            }

                            if (btnWishlist.isChecked()) {
                                wish = "1";
                            } else {
                                wish = "0";
                            }

                            country = txtCountry.getText().toString();
                            region = txtRegion.getText().toString();
                            domain = txtDomain.getText().toString();
                            appellation = txtAppellation.getText().toString();
                            address = txtAddress.getText().toString();
                            year = Integer.parseInt(nbYear.getText().toString());
                            apogee = Integer.parseInt(nbApogee.getText().toString());
                            number = Integer.parseInt(nbNumber.getText().toString());
                            estimate = Integer.parseInt(nbEstimate.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Update des pictureLarge et pictureSmall suivant si une image a été utilisée
                        String pictureLarge = getPictureLarge();
                        String pictureSmall = getPictureSmall();

                        WineBottle wineBottle = new WineBottle(null, country, region, wineColor, domain, appellation, address, year, apogee, number, estimate, pictureLarge, pictureSmall, imageLarge, imageSmall, rate, favorite, wish, latitude, longitude, timeStamp);
                        AccesLocalCellar accesLocalCellar = new AccesLocalCellar(AddActivity.this);
                        wineBottle.setId(accesLocalCellar.add(wineBottle));

                        popupAdd.dismiss();

                        // Affiche le logo de validation 1 seconde
                        popupSuccess.show();
                        // Permet de faire aparaitre le panneau de validation 1 seconde
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
                        popupAdd.dismiss();
                    }
                });

            }
        });

    }

    /**
     * Chargement et récupération des infos du fichier JSon pour le nom des pays
     */
    public void getJsonCountries() {

        String json;
        try {
            //Load File
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
    }

    private void recoverJsonCountries() {
        getJsonCountries();
        AutoCompleteTextView textCountries = (AutoCompleteTextView) findViewById(R.id.textCountry);
        // On change la couleur de fond de la liste déroulante
        textCountries.setDropDownBackgroundDrawable(new ColorDrawable(AddActivity.this.getResources().getColor(R.color.green_very_dark)));

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_autocomplete, countryList);
        textCountries.setAdapter(adapter);
    }

    private void getRegionsList() {
        List<String> regionsList = Arrays.asList("Alsace", "Beaujolais", "Bordelais", "Bourgogne",
                "Bretagne", "Champagne", "Charentes", "Corse", "Ile-de-France", "Jura",
                "Languedoc-Roussillon", "Lorraine", "Lyonnais", "Nord-Pas-De-Calais", "Normandie",
                "Picardie", "Provence", "Savoie-Bugey", "Sud-Ouest", "Tahiti", "Val de Loire", "Vallée du Rhône");
        AutoCompleteTextView textRegions = (AutoCompleteTextView) findViewById(R.id.textRegion);
        // On change la couleur de fond de la liste déroulante
        textRegions.setDropDownBackgroundDrawable(new ColorDrawable(AddActivity.this.getResources().getColor(R.color.green_very_dark)));
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_autocomplete, regionsList);
        textRegions.setAdapter(adapter);
    }

    private void recoverFABWineColor() {

        Intent intent = getIntent();
        if (intent != null) {
            String str = "";
            if (intent.hasExtra("redWine")) {
                btnRed.setAlpha(1f);
                btnRose.setAlpha(0.3f);
                btnWhite.setAlpha(0.3f);
                btnChamp.setAlpha(0.3f);
                imgWineColor.setImageDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.red_wine_listview));
            } else if (intent.hasExtra("roseWine")) {
                btnRed.setAlpha(0.3f);
                btnRose.setAlpha(1f);
                btnWhite.setAlpha(0.3f);
                btnChamp.setAlpha(0.3f);
                imgWineColor.setImageDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.rose_wine_listview));
            } else if (intent.hasExtra("whiteWine")) {
                btnRed.setAlpha(0.3f);
                btnRose.setAlpha(0.3f);
                btnWhite.setAlpha(1f);
                btnChamp.setAlpha(0.3f);
                imgWineColor.setImageDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.white_wine_listview));
            } else {
                btnRed.setAlpha(0.3f);
                btnRose.setAlpha(0.3f);
                btnWhite.setAlpha(0.3f);
                btnChamp.setAlpha(1f);
                imgWineColor.setImageDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.champ_wine_listview));
            }

        }
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
                imgWineColor.setImageDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.red_wine_listview));
            }
        });

        btnRose.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRed.setAlpha(0.3f);
                btnRose.setAlpha(1f);
                btnWhite.setAlpha(0.3f);
                btnChamp.setAlpha(0.3f);
                imgWineColor.setImageDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.rose_wine_listview));
            }
        });

        btnWhite.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRed.setAlpha(0.3f);
                btnRose.setAlpha(0.3f);
                btnWhite.setAlpha(1f);
                btnChamp.setAlpha(0.3f);
                imgWineColor.setImageDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.white_wine_listview));
            }
        });

        btnChamp.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnRed.setAlpha(0.3f);
                btnRose.setAlpha(0.3f);
                btnWhite.setAlpha(0.3f);
                btnChamp.setAlpha(1f);
                imgWineColor.setImageDrawable(ContextCompat.getDrawable(AddActivity.this, R.drawable.champ_wine_listview));
            }
        });

    }

    /**
     * Méthode qui gère la progressBar et les validations de champs
     */
    private void gestionProgressBarValidation() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(900);
        progressBar.setProgress(0);

        // Animation de la progressBar
        final ProgressBarAnimation progressBarAnimation = new ProgressBarAnimation(progressBar, 1000);

        // Instanciation des méthodes de validation
        final Validation validation = new Validation();

        txtCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        check = false;
                    }
                } else {
                    if (progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                    }
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidCountry.setVisibility(View.VISIBLE);
                if (validation.isValidTextField(txtCountry.getText().toString().trim())) {
                    // is true
                    imgValidCountry.setColorFilter(getResources().getColor(R.color.green_apple));
                    countryOK = true;

                } else if (!validation.isValidTextField(txtCountry.getText().toString().trim())) {
                    // is false
                    imgValidCountry.setColorFilter(getResources().getColor(R.color.pink));
                    countryOK = false;
                }
                gestionAddButtonColor();
            }
        });

        txtRegion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        check = false;
                    }
                } else {
                    if (progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                    }
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidRegion.setVisibility(View.VISIBLE);
                if (validation.isValidTextField(txtRegion.getText().toString().trim())) {
                    // is true
                    imgValidRegion.setColorFilter(getResources().getColor(R.color.green_apple));
                    regionOK = true;

                } else if (!validation.isValidTextField(txtRegion.getText().toString().trim())) {
                    // is false
                    imgValidRegion.setColorFilter(getResources().getColor(R.color.pink));
                    regionOK = false;
                }
                gestionAddButtonColor();
            }
        });

        nbYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        check = false;
                    }
                } else {
                    if (progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                    }
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validation.isValidNumberField(nbYear.getText().toString().trim())) {
                    millesimeOK = true;
                } else if (!validation.isValidNumberField(nbYear.getText().toString().trim())) {
                    millesimeOK = false;
                }
                gestionAddButtonColor();
            }
        });

        nbApogee.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        check = false;
                    }
                } else {
                    if (progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                    }
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validation.isValidNumberField(nbApogee.getText().toString().trim())) {
                    apogeeOK = true;

                } else if (!validation.isValidNumberField(nbApogee.getText().toString().trim())) {
                    apogeeOK = false;
                }
                gestionAddButtonColor();
            }
        });

        txtDomain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        check = false;
                    }
                } else {
                    if (progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                    }
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidDomain.setVisibility(View.VISIBLE);
                if (validation.isValidTextField(txtDomain.getText().toString().trim())) {
                    // is true
                    imgValidDomain.setColorFilter(getResources().getColor(R.color.green_apple));
                    domainOK = true;

                } else if (!validation.isValidTextField(txtDomain.getText().toString().trim())) {
                    // is false
                    imgValidDomain.setColorFilter(getResources().getColor(R.color.pink));
                    domainOK = false;
                }
                gestionAddButtonColor();
            }
        });

        txtAppellation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        check = false;
                    }
                } else {
                    if (progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                    }
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidAppellation.setVisibility(View.VISIBLE);
                if (validation.isValidTextField(txtAppellation.getText().toString().trim())) {
                    // is true
                    imgValidAppellation.setColorFilter(getResources().getColor(R.color.green_apple));
                    appellationOK = true;

                } else if (!validation.isValidTextField(txtAppellation.getText().toString().trim())) {
                    // is false
                    imgValidAppellation.setColorFilter(getResources().getColor(R.color.pink));
                    appellationOK = false;
                }
                gestionAddButtonColor();
            }
        });

        txtAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        check = false;
                    }
                } else {
                    if (progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                    }
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                imgValidAddress.setVisibility(View.VISIBLE);
                if (validation.isValidTextField(txtAddress.getText().toString().trim())) {
                    // is true
                    imgValidAddress.setColorFilter(getResources().getColor(R.color.green_apple));
                    addressOK = true;

                } else if (!validation.isValidTextField(txtAddress.getText().toString().trim())) {
                    // is false
                    imgValidAddress.setColorFilter(getResources().getColor(R.color.pink));
                    addressOK = false;
                }
                gestionAddButtonColor();
            }
        });

        nbNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        check = false;
                    }
                } else {
                    if (progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                    }
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validation.isValidNumberField(nbNumber.getText().toString().trim())) {
                    numberOK = true;

                } else if (!validation.isValidNumberField(nbNumber.getText().toString().trim())) {
                    numberOK = false;
                }
                gestionAddButtonColor();
            }

        });

        nbEstimate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    if (check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        check = false;
                    }
                } else {
                    if (progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                    }
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validation.isValidNumberField(nbEstimate.getText().toString().trim())) {
                    estimateOK = true;

                } else if (!validation.isValidNumberField(nbEstimate.getText().toString().trim())) {
                    estimateOK = false;
                }
                gestionAddButtonColor();
            }
        });

    }

    private void gestionAddButtonColor() {

        if(countryOK == true && regionOK == true && millesimeOK == true && apogeeOK == true && domainOK == true && appellationOK == true && addressOK == true && numberOK == true && estimateOK == true) {
            btnAdd.setColorFilter(getResources().getColor(R.color.green_apple));
        }else {
            btnAdd.setColorFilter(getResources().getColor(R.color.green_third_light));
        }


    }

    private CurvedBottomNavigationView.OnNavigationItemSelectedListener customBottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.mapMenu:
                            startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            AddActivity.this.finish();
                            return true;
                        case R.id.cellarMenu:
                            startActivity(new Intent(getApplicationContext(), CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            AddActivity.this.finish();
                            return true;
                        case R.id.scanMenu:
                            //startActivity(new Intent(getApplicationContext(), ScanActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.likeMenu:
                            startActivity(new Intent(getApplicationContext(), LikeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            AddActivity.this.finish();
                            return true;
                        case R.id.userMenu:
                            startActivity(new Intent(getApplicationContext(), UserActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            AddActivity.this.finish();
                            return true;
                    }
                    return false;
                }
            };


}

