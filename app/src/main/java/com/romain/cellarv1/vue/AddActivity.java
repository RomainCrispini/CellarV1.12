package com.romain.cellarv1.vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.content.Intent;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.Transformation;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.romain.cellarv1.R;
import com.romain.cellarv1.controleur.Controle;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.modele.WineBottle;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.ProgressBarAnimation;
import com.romain.cellarv1.outils.Tools;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class AddActivity extends AppCompatActivity {

    /**
     * Propriétés
     */

    // Appareil photo et gallery
    private String photoPath = null;
    private static final int GALLERY_READ_REQUEST_PERMISSION = 100;
    private static final int CAMERA_REQUEST_PERMISSION = 101;
    // private static final int GALLERY_WRITE_REQUEST_PERMISSION = 102;
    private static final int CAMERA_REQUEST_CODE = 103;
    private static final int GALLERY_REQUEST_CODE = 104;

    private ImageView scanImageView;
    private ImageButton btnPhoto;
    private LinearLayout layapp;

    // Gallery
    private ImageButton btnGallery;

    // Liste Pays
    private ArrayList<String> countryList = new ArrayList<>();

    // ProgessBar
    private ProgressBar progressBar;
    private boolean check = true;

    // Champs texte
    private AutoCompleteTextView txtCountry, txtRegion;
    private EditText txtDomain, txtAppellation;
    private EditText nbYear, nbApogee, nbNumber, nbEstimate;
    private ImageButton btnRed, btnRose, btnWhite, btnChamp;

    // Bouton add
    private FloatingActionButton btnAdd;

    // Popup
    private Dialog popupInfoLabelBottle;
    private ImageButton btnInfoCamera, btnInfoGallery, btnInfoExit;

    // Déclaration du contrôleur
    private Controle controle;

    // Déclaration de la popup
    private Dialog popupAdd, popupSuccess;

    // Buttons MenuBis + Interpolator
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private ToggleButton btnFavorite;
    private ToggleButton btnWishlist;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        init();


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
        nbYear = (EditText) findViewById(R.id.nbYear);
        nbApogee = (EditText) findViewById(R.id.nbApogee);
        nbNumber = (EditText) findViewById(R.id.nbNumber);
        nbEstimate = (EditText) findViewById(R.id.nbEstimate);
        btnAdd = (FloatingActionButton) findViewById(R.id.btnAdd);
        btnPhoto = (ImageButton) findViewById(R.id.btnPhoto);
        this.controle = Controle.getInstance(this); // Création d'une instance de type Controle

        btnGallery = (ImageButton) findViewById(R.id.btnGallery);
        scanImageView = (ImageView) findViewById(R.id.scanImageView);

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

        // Toggle Buttons
        btnFavorite = (ToggleButton) findViewById(R.id.btnFavorite);
        btnFavorite.setText(null);
        btnFavorite.setTextOn(null);
        btnFavorite.setTextOff(null);
        btnWishlist = (ToggleButton) findViewById(R.id.btnWishlist);
        btnWishlist.setText(null);
        btnWishlist.setTextOn(null);
        btnWishlist.setTextOff(null);

        // Instanciation et animation du menuBis coulissant
        FrameLayout menuBis = (FrameLayout) findViewById(R.id.menuBis);
        menuBis.setTranslationY(300f);
        menuBis.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

        displayPopupInfo();
        addWineBottle();
        //recoverWineBottle();
        recoverFABWineColor();
        recoverJsonCountries();
        getRegionsList();
        progressBar();

    }

    private void displayPopupInfo() {

        popupInfoLabelBottle.show();

        // Récupération de la liste de toutes les bouteilles SI ET SEULEMENT SI une BDD existe
        AccesLocal accesLocal = new AccesLocal(AddActivity.this);

        if(accesLocal.doesDBExists() == true) {
            ArrayList<WineBottle> wineBottleList = (ArrayList<WineBottle>) accesLocal.recoverWineBottleList();
            for(int i = 0; i < wineBottleList.size(); i++) {
                if(wineBottleList.get(i).getPictureSmall().equals("")) {
                } else if(!wineBottleList.get(i).getPictureSmall().equals("")) {
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

        btnInfoCamera.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO ACCES A L'APPAREIL PHOTO

            }
        });

        btnInfoGallery.setOnClickListener(new ImageButton.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO ACCES A LA GALERIE IMAGES

            }
        });


    }

    // Au retour de la sélection d'une image (après appel de startactivityforresult)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        ImageView scanImageView = (ImageView) findViewById(R.id.scanImageView); // DOIT ETRE DECLAREE ICI !!!!!!!!!!!!!!!!!!!!!!
        // Vérification du bon code de retour et l'état du retour ok
        switch (requestCode) {
            case CAMERA_REQUEST_CODE :
                // Récupération de l'image
                Bitmap imageCamera = BitmapFactory.decodeFile(photoPath);
                // Afficher l'image
                scanImageView.setImageBitmap(imageCamera);
                break;
            case GALLERY_REQUEST_CODE : // Vérifie si une image est récupérée
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
                // Redimentionner l'image
                imageGallery = changeSizeBitmap(imageGallery, 0.8f);
                // Affichage de l'image
                scanImageView.setImageBitmap(imageGallery);
                break;
        }

    }

    public void accesGallery(View view) {
        // Permission pour accès Gallery
        if(ActivityCompat.checkSelfPermission(AddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            // Accès à la gallery du tel
            Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
        } else {
            galleryRequestPermission();
        }
    }

    public void takePicture(View view) {
        if(ActivityCompat.checkSelfPermission(AddActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Crée un intent pour ouvrir une fenêtre pour prendre la photo
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Test pour contrôler que l'intent peut être géré
            if(intent.resolveActivity(getPackageManager()) != null) {
                // Créer un nom de fichier unique
                String time = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                File photoDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
                try {
                    File photoFile = File.createTempFile("photo" + time, ".jpg", photoDir);
                    // Enregistrer le chemin complet
                    photoPath = photoFile.getAbsolutePath();
                    // Créer l'URI
                    Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);
                    // Transfert uri vers l'intent pour enregistrement photo dans fichier temporaire
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    // Ouvrir l'activity par rapport à l'intent
                    startActivityForResult(intent, CAMERA_REQUEST_CODE);
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            cameraRequestPermission();
        }
    }

    private Bitmap changeSizeBitmap(Bitmap bitmap, float proportion) {
        // Métrique qui permet de récupérer les dimensions de l'écran
        DisplayMetrics metrics = new DisplayMetrics();
        this.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        // Taille de l'écran (et affecter les proportions)
        float screenHeight = metrics.heightPixels*proportion;
        float screenWidth = metrics.widthPixels*proportion;
        // Taille de l'image
        float bitmapHeight = bitmap.getHeight();
        float bitmapWidth = bitmap.getWidth();
        // Calcul du ratio entre taille image et écran
        float ratioHeight = screenHeight/bitmapHeight;
        float ratioWidth = screenWidth/bitmapWidth;
        // Récupération du plus petit ratio
        float ratio = Math.min(ratioHeight, ratioWidth);
        // Redimentionnement de l'image par rapport au ratio
        bitmap = Bitmap.createScaledBitmap(bitmap, (int) (bitmapWidth*ratio), (int) (bitmapHeight*ratio), true);
        // envoie la nouvelle image
        return bitmap;
    }

    private void galleryRequestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(AddActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            new AlertDialog.Builder(AddActivity.this)
                    .setTitle("Permission")
                    .setMessage("Cellar requiert votre permission pour accéder à votre galerie d'images")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_READ_REQUEST_PERMISSION);
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
            ActivityCompat.requestPermissions(AddActivity.this, new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, GALLERY_READ_REQUEST_PERMISSION);
        }
    }

    private void cameraRequestPermission() {
        if(ActivityCompat.shouldShowRequestPermissionRationale(AddActivity.this, Manifest.permission.CAMERA)) {
            new AlertDialog.Builder(AddActivity.this)
                    .setTitle("Permission")
                    .setMessage("Cellar requiert votre permission pour accéder à votre appareil photo")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AddActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_PERMISSION);
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
            ActivityCompat.requestPermissions(AddActivity.this, new String[] {Manifest.permission.CAMERA}, CAMERA_REQUEST_PERMISSION);
        }
    }

    /**
     * Test si le scanImageView contient un Bitmap
     */
    private boolean doesImageLabel() {

        boolean test;
        BitmapDrawable drawable = (BitmapDrawable) scanImageView.getDrawable();
        if(drawable == null) {
            test = false;
        } else {
            test = true;
        }
        return test;
    }

    private String getPictureLarge() {

        String pictureSmall = "";
        Tools tools = new Tools();

        if(doesImageLabel()) {
            BitmapDrawable drawable = (BitmapDrawable) scanImageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            Bitmap bitmap500 = tools.getResizedBitmap500px(bitmap);

            // Enregistrement de bitmap1000 dans pictureLarge
            ByteArrayOutputStream stream500 = new ByteArrayOutputStream();
            bitmap500.compress(Bitmap.CompressFormat.PNG, 0, stream500);
            byte[] b1000 = stream500.toByteArray();
            String image500 = Base64.encodeToString(b1000, Base64.DEFAULT);
            pictureSmall = image500;
            try {
                stream500.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!doesImageLabel()){
            pictureSmall = "";
        }

        return pictureSmall;
    }

    private String getPictureSmall() {

        String pictureLarge = "";
        Tools tools = new Tools();

        if(doesImageLabel()) {
            BitmapDrawable drawable = (BitmapDrawable) scanImageView.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            Bitmap bitmap100 = tools.getResizedBitmap100px(bitmap);

            // Enregistrement de bitmap100 dans pictureSmall
            //ByteArrayOutputStream stream100 = new ByteArrayOutputStream(bitmap100.getWidth() * bitmap.getHeight());
            ByteArrayOutputStream stream100 = new ByteArrayOutputStream();
            bitmap100.compress(Bitmap.CompressFormat.PNG, 0, stream100);
            byte[] b100 = stream100.toByteArray();
            String image100 = Base64.encodeToString(b100, Base64.DEFAULT);
            pictureLarge = image100;
            try {
                stream100.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (!doesImageLabel()){
            pictureLarge = "";
        }

        return pictureLarge;
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
     * Ajout d'une nouvelle bouteille
     */
    private void addWineBottle() {
        ((FloatingActionButton) findViewById(R.id.btnAdd)).setOnClickListener(new FloatingActionButton.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button btnAccept = (Button) popupAdd.findViewById(R.id.btnAccept);
                Button btnDenie = (Button) popupAdd.findViewById(R.id.btnDenie);
                TextView number = (TextView) popupAdd.findViewById(R.id.number);
                ImageView imageWineColor = (ImageView) popupAdd.findViewById(R.id.imageWineColor);
                ImageView imageBottle = (ImageView) popupAdd.findViewById(R.id.imageBottle);
                TextView region = (TextView) popupAdd.findViewById(R.id.region);
                TextView domain = (TextView) popupAdd.findViewById(R.id.domain);
                TextView appellation = (TextView) popupAdd.findViewById(R.id.appellation);
                TextView millesime = (TextView) popupAdd.findViewById(R.id.millesime);

                if(scanImageView.getDrawable() != null) {
                    Bitmap bitmapEtiquette = ((BitmapDrawable) scanImageView.getDrawable()).getBitmap();
                    imageBottle.setImageBitmap(bitmapEtiquette);
                } else {
                }


                if(btnRed.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.red_wine_listview);
                } else if(btnRose.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.rose_wine_listview);
                } else if(btnWhite.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.white_wine_listview);
                } else if(btnChamp.getAlpha() == 1f) {
                    imageWineColor.setImageResource(R.drawable.champ_wine_listview);
                }

                number.setText(nbNumber.getText());

                region.setText(txtRegion.getText());
                domain.setText(txtDomain.getText());
                appellation.setText(txtAppellation.getText());
                millesime.setText(nbYear.getText());

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
                        String wineColor = "";
                        int year = 0;
                        int apogee = 0;
                        int number = 0;
                        int estimate = 0;
                        byte[] imageLarge = null;
                        byte[] imageSmall = null;
                        Float rate = 0f;
                        String favorite = "0";
                        String wish = "0";
                        Float lattitude = 0f;
                        Float longitude = 0f;

                        // Initialisation du timeStamp
                        Long timeStampLong = System.currentTimeMillis(); // Résultat en millisecondes
                        String timeStamp = timeStampLong.toString();

                        // Récupération des données saisies
                        try {
                            if(btnRed.getAlpha() == 1f) {
                                wineColor = "Rouge";
                            } else if(btnRose.getAlpha() == 1f) {
                                wineColor = "Rose";
                            } else if(btnWhite.getAlpha() == 1f) {
                                wineColor = "Blanc";
                            } else if(btnChamp.getAlpha() == 1f) {
                                wineColor = "Effervescent";
                            }

                            if(btnFavorite.isChecked()) {
                                favorite = "1";
                            } else {
                                favorite = "0";
                            }

                            if(btnWishlist.isChecked()) {
                                wish = "1";
                            } else {
                                wish = "0";
                            }

                            country = txtCountry.getText().toString();
                            region = txtRegion.getText().toString();
                            domain = txtDomain.getText().toString();
                            appellation = txtAppellation.getText().toString();
                            year = Integer.parseInt(nbYear.getText().toString());
                            apogee = Integer.parseInt(nbApogee.getText().toString());
                            number = Integer.parseInt(nbNumber.getText().toString());
                            estimate = Integer.parseInt(nbEstimate.getText().toString());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        // Récupération des pictureLarge et pictureSmall suivant si une image a été utilisé
                        String pictureLarge = getPictureLarge();
                        String pictureSmall = getPictureSmall();

                        WineBottle wineBottle = new WineBottle(null, country, region, wineColor, domain, appellation, year, apogee, number, estimate, pictureLarge, pictureSmall, imageLarge, imageSmall, rate, favorite, wish, lattitude, longitude, timeStamp);
                        AccesLocal accesLocal = new AccesLocal(AddActivity.this);
                        wineBottle.setId(accesLocal.add(wineBottle));

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
        //Toast.makeText(getApplicationContext(),countrylist.toString(),Toast.LENGTH_LONG).show();
    }

    private void recoverJsonCountries() {
        getJsonCountries();
        AutoCompleteTextView textCountries = (AutoCompleteTextView) findViewById(R.id.textCountry);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, countryList);
        textCountries.setAdapter(adapter);
    }

    private void getRegionsList() {
        List<String> regionsList = Arrays.asList("Alsace", "Beaujolais", "Bordelais", "Bourgogne",
                "Bretagne", "Champagne", "Charentes", "Corse", "Ile-de-France", "Jura",
                "Languedoc-Roussillon", "Lorraine", "Lyonnais", "Nord-Pas-De-Calais", "Normandie",
                "Picardie", "Provence", "Savoie-Bugey", "Sud-Ouest", "Tahiti", "Val de Loire", "Vallée du Rhône");
        AutoCompleteTextView textRegions = (AutoCompleteTextView) findViewById(R.id.textRegion);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, regionsList);
        textRegions.setAdapter(adapter);
    }

    private void recoverFABWineColor() {
        ImageButton redWineButton = (ImageButton) findViewById(R.id.redWineButton);
        ImageButton roseWineButton = (ImageButton) findViewById(R.id.roseWineButton);
        ImageButton whiteWineButton = (ImageButton) findViewById(R.id.whiteWineButton);
        ImageButton champWineButton = (ImageButton) findViewById(R.id.champWineButton);
        Intent intent = getIntent();
        if (intent != null) {
            String str = "";
            if (intent.hasExtra("redWine")) {
                redWineButton.setAlpha(1f);
                roseWineButton.setAlpha(0.3f);
                whiteWineButton.setAlpha(0.3f);
                champWineButton.setAlpha(0.3f);
            } else if (intent.hasExtra("roseWine")) {
                redWineButton.setAlpha(0.3f);
                roseWineButton.setAlpha(1f);
                whiteWineButton.setAlpha(0.3f);
                champWineButton.setAlpha(0.3f);
            } else if (intent.hasExtra("whiteWine")) {
                redWineButton.setAlpha(0.3f);
                roseWineButton.setAlpha(0.3f);
                whiteWineButton.setAlpha(1f);
                champWineButton.setAlpha(0.3f);
            } else {
                redWineButton.setAlpha(0.3f);
                roseWineButton.setAlpha(0.3f);
                whiteWineButton.setAlpha(0.3f);
                champWineButton.setAlpha(1f);
            }

        }
    }

    public void wineColorSelector(View view) {
        ImageButton redWineButton = (ImageButton) findViewById(R.id.redWineButton);
        ImageButton roseWineButton = (ImageButton) findViewById(R.id.roseWineButton);
        ImageButton whiteWineButton = (ImageButton) findViewById(R.id.whiteWineButton);
        ImageButton champWineButton = (ImageButton) findViewById(R.id.champWineButton);
        int id = view.getId();
        switch (id) {
            case R.id.redWineButton:
                redWineButton.setAlpha(1f);
                roseWineButton.setAlpha(0.3f);
                whiteWineButton.setAlpha(0.3f);
                champWineButton.setAlpha(0.3f);
                break;
            case R.id.roseWineButton:
                redWineButton.setAlpha(0.3f);
                roseWineButton.setAlpha(1f);
                whiteWineButton.setAlpha(0.3f);
                champWineButton.setAlpha(0.3f);
                break;
            case R.id.whiteWineButton:
                redWineButton.setAlpha(0.3f);
                roseWineButton.setAlpha(0.3f);
                whiteWineButton.setAlpha(1f);
                champWineButton.setAlpha(0.3f);
                break;
            case R.id.champWineButton:
                redWineButton.setAlpha(0.3f);
                roseWineButton.setAlpha(0.3f);
                whiteWineButton.setAlpha(0.3f);
                champWineButton.setAlpha(1f);
                break;
        }
    }

    /**
     * Méthode qui gère la progressBar de AddActivity
     */
    private void progressBar() {

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setMax(700);
        progressBar.setProgress(0);
        final ProgressBarAnimation progressBarAnimation = new ProgressBarAnimation(progressBar, 1000);

        txtCountry = (AutoCompleteTextView) findViewById(R.id.textCountry);
        txtCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }


            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        if(progressBar.getProgress() == 700) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 700) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                } else {
                    if(progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                        check = true;
                    } else {
                        check = true;
                    }
                }
            }



            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtRegion = (AutoCompleteTextView) findViewById(R.id.textRegion);
        txtRegion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        if(progressBar.getProgress() == 700) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 700) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                } else {
                    if(progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                        check = true;
                    } else {
                        check = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtDomain = (EditText) findViewById(R.id.textDomain);
        txtDomain.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        if(progressBar.getProgress() == 700) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 700) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                } else {
                    if(progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                        check = true;
                    } else {
                        check = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtAppellation = (EditText) findViewById(R.id.textAppellation);
        txtAppellation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        if(progressBar.getProgress() == 700) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 700) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                } else {
                    if(progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                        check = true;
                    } else {
                        check = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nbYear = (EditText) findViewById(R.id.nbYear);
        nbYear.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        if(progressBar.getProgress() == 700) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 700) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                } else {
                    if(progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                        check = true;
                    } else {
                        check = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nbNumber = (EditText) findViewById(R.id.nbNumber);
        nbNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        if(progressBar.getProgress() == 700) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 700) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                } else {
                    if(progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                        check = true;
                    } else {
                        check = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        nbEstimate = (EditText) findViewById(R.id.nbEstimate);
        nbEstimate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() + 100);
                        if(progressBar.getProgress() == 700) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 700) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                } else {
                    if(progressBar.getProgress() > 0) {
                        progressBarAnimation.setProgressOnly(progressBar.getProgress() - 100);
                        check = true;
                    } else {
                        check = true;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    private CurvedBottomNavigationView.OnNavigationItemSelectedListener customBottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.mapMenu:
                            startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.cellarMenu:
                            startActivity(new Intent(getApplicationContext(), CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.scanMenu:
                            //startActivity(new Intent(getApplicationContext(), ScanActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.likeMenu:
                            startActivity(new Intent(getApplicationContext(), LikeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.userMenu:
                            startActivity(new Intent(getApplicationContext(), UserActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            //overridePendingTransition(0, 0);
                            return true;
                    }
                    return false;
                }
            };




}

