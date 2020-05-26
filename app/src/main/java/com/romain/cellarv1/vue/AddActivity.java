package com.romain.cellarv1.vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import android.Manifest;
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
import android.widget.ToggleButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.romain.cellarv1.R;
import com.romain.cellarv1.controleur.Controle;
import com.romain.cellarv1.modele.AccesLocal;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.Tools;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Random;


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

    // Liste pays
    private ArrayList<String> countrylist = new ArrayList<>();

    // ProgessBar
    private ProgressBar progressBar;
    //private Handler handler = new Handler();

    // Champs texte
    private AutoCompleteTextView txtCountry;
    private EditText txtRegion, txtDomain, txtAppellation;
    private EditText nbYear, nbApogee, nbNumber, nbEstimate;
    private ImageButton btnRed, btnRose, btnWhite, btnChamp;

    // Bouton add
    private FloatingActionButton btnAdd;

    // Déclaration du contrôleur
    private Controle controle;

    // Déclaration de la popup
    private Dialog popupAdd, popupSuccess;

    // Buttons MenuBis + Interpolator
    private OvershootInterpolator interpolator = new OvershootInterpolator();
    private ToggleButton btnFavorite;
    private ToggleButton btnWishlist;
    private ImageView btnBackMap1;
    private ImageView btnBackMap2;



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
        txtRegion = (EditText) findViewById(R.id.textRegion);
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

        // Popup
        popupAdd = new Dialog(this);
        popupAdd.setContentView(R.layout.popup_add_bottle);
        popupAdd.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupAdd.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        popupSuccess = new Dialog(this);
        popupSuccess.setContentView(R.layout.popup_success_add_bottle);
        popupSuccess.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupSuccess.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        // Toggle Buttons
        btnFavorite = (ToggleButton) findViewById(R.id.btnFavorite);
        btnFavorite.setText(null);
        btnFavorite.setTextOn(null);
        btnFavorite.setTextOff(null);
        btnWishlist = (ToggleButton) findViewById(R.id.btnWishlist);
        btnWishlist.setText(null);
        btnWishlist.setTextOn(null);
        btnWishlist.setTextOff(null);

        // Je n'ai pas trouvé d'autres moyens pour rendre toute la surface clickable
        btnBackMap1 = (ImageView) findViewById(R.id.btnBackMap1);
        btnBackMap2 = (ImageView) findViewById(R.id.btnBackMap2);
        btnBackMap1.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });
        btnBackMap2.setOnClickListener(new LinearLayout.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
            }
        });

        // Instanciation et animation du menuBis coulissant
        FrameLayout menuBis = (FrameLayout) findViewById(R.id.menuBis);
        menuBis.setTranslationY(300f);
        menuBis.animate().translationY(0f).setInterpolator(interpolator).setDuration(1500).start();

        addWineBottle();
        //recoverWineBottle();
        recoverFABWineColor();
        recoverJsonCountries();
        progressBar();

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
                    imageBottle.setImageResource(R.drawable.none_listview);
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
                        String pictureLarge = "";
                        String pictureSmall = "";
                        byte[] imageLarge = null;
                        byte[] imageSmall = null;
                        Float rate = 0f;
                        String favorite = "0";
                        String wish = "0";
                        Float lattitude = 0f;
                        Float longitude = 0f;
                        String timeStamp = "";
                        String random = "";

                        Tools tool = new Tools();

                        Random randomize = new Random();

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

                            random = String.valueOf(randomize.nextInt(10000000));

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





                        // Pour les tests de compression
                        //Bitmap bitmap1 = BitmapFactory.decodeResource(AddActivity.this.getResources(), R.drawable.test_image);
                        //Bitmap bitmap2 = BitmapFactory.decodeResource(AddActivity.this.getResources(), R.drawable.champ_wine);
                        Bitmap bitmap = ((BitmapDrawable) scanImageView.getDrawable()).getBitmap();

                        Tools tools = new Tools();
                        Bitmap bitmap100 = tools.getResizedBitmap100px(bitmap);
                        Bitmap bitmap1000 = tools.getResizedBitmap1000px(bitmap);

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

                        // Enregistrement de bitmap1000 dans pictureLarge
                        ByteArrayOutputStream stream1000 = new ByteArrayOutputStream();
                        bitmap1000.compress(Bitmap.CompressFormat.PNG, 0, stream1000);
                        byte[] b1000 = stream1000.toByteArray();
                        String image1000 = Base64.encodeToString(b1000, Base64.DEFAULT);
                        pictureLarge = image1000;
                        try {
                            stream1000.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }












                        /*
                        ByteArrayOutputStream stream = new ByteArrayOutputStream(bitmap.getWidth() * bitmap.getHeight());
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        imageLarge = stream.toByteArray();

                         */



                        /*
                        try {
                            Bitmap bitmap = ((BitmapDrawable) scanImageView.getDrawable()).getBitmap();
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
                            imageLarge = stream.toByteArray();
                            //image = (tool.bitmapToString(bitmap));
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                         */

                        afficheResult(country, region, wineColor, domain, appellation, year, apogee, number, estimate, pictureLarge, pictureSmall, imageLarge, imageSmall, rate, favorite, wish, lattitude, longitude, timeStamp, random);

                        popupAdd.dismiss();

                        // Affiche le logo de validation 1 seconde
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

    private void afficheResult(String country, String region, String wineColor, String domain, String appellation, Integer year, Integer apogee, Integer number, Integer estimate, String pictureLarge, String pictureSmall, byte[] imageLarge, byte[] imageSmall, Float rate, String favorite, String wish, Float lattitude, Float longitude, String timeStamp, String random) {
        this.controle.createWineBottle(country, region, wineColor, domain, appellation, year, apogee, number, estimate, pictureLarge, pictureSmall, imageLarge, imageSmall, rate, favorite, wish, lattitude, longitude, timeStamp, random,  AddActivity.this);
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
                countrylist.add(jsonObject.getString("name"));
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, countrylist);
        textCountries.setAdapter(adapter);
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
        //holder.progressBar.isIndeterminate(false);
        progressBar.setMax(7);
        progressBar.setProgress(0);

        txtCountry = (AutoCompleteTextView) findViewById(R.id.textCountry);
        txtCountry.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBar.incrementProgressBy(1);
                        //progressBar.setProgress(progressBar.getProgress() + 1);
                        if(progressBar.getProgress() == 7) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 7) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                }
                else {
                    progressBar.incrementProgressBy(-1);
                    //progressBar.setProgress(progressBar.getProgress() - 1);
                    //progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#282828"), android.graphics.PorterDuff.Mode.SRC_IN);
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        txtRegion = (EditText) findViewById(R.id.textRegion);
        txtRegion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            boolean check = true;
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() > 0) {
                    if(check) {
                        progressBar.incrementProgressBy(1);
                        if(progressBar.getProgress() == 7) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 7) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                }
                else {
                    progressBar.setProgress(progressBar.getProgress() - 1);
                    check = true;
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
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        if(progressBar.getProgress() == 7) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 7) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                }
                else {
                    progressBar.setProgress(progressBar.getProgress() - 1);
                    check = true;
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
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        if(progressBar.getProgress() == 7) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 7) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                }
                else {
                    progressBar.setProgress(progressBar.getProgress() - 1);
                    check = true;
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
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        if(progressBar.getProgress() == 7) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 7) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                }
                else {
                    progressBar.setProgress(progressBar.getProgress() - 1);
                    check = true;
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
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        if(progressBar.getProgress() == 7) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 7) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                }
                else {
                    progressBar.setProgress(progressBar.getProgress() - 1);
                    check = true;
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
                        progressBar.setProgress(progressBar.getProgress() + 1);
                        if(progressBar.getProgress() == 7) {
                            btnAdd.setColorFilter(Color.parseColor("#97C58D"));
                        } else if(progressBar.getProgress() < 7) {
                            btnAdd.setColorFilter(Color.parseColor("#67828f"));
                        }
                        check = false;
                    }
                }
                else {
                    progressBar.setProgress(progressBar.getProgress() - 1);
                    check = true;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });




        //if(!txtCountry.getText().toString().isEmpty() && !txtRegion.getText().toString().isEmpty()) {
        //    progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#159700"), android.graphics.PorterDuff.Mode.SRC_IN);
        //}
        //txtCountry.getText().toString().isEmpty();
        //txtRegion.getText().toString().isEmpty();
        //txtDomain.getText().toString().isEmpty();
        //txtAppellation.getText().toString().isEmpty();
        //nbYear.getText().toString().isEmpty();
        //nbNumber.getText().toString().isEmpty();
        //nbEstimate.getText().toString().isEmpty();
        //if(!txtCountry.getText().toString().isEmpty() && !txtRegion.getText().toString().isEmpty()) {
        //    progressBar.getProgressDrawable().setColorFilter(Color.parseColor("#159700"), android.graphics.PorterDuff.Mode.SRC_IN);
        //}

    }

    private CurvedBottomNavigationView.OnNavigationItemSelectedListener customBottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.user:
                            startActivity(new Intent(getApplicationContext(), UserActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.cellar:
                            startActivity(new Intent(getApplicationContext(), CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.scan:
                            //startActivity(new Intent(getApplicationContext(), ScanActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.like:
                            startActivity(new Intent(getApplicationContext(), LikeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.search:
                            startActivity(new Intent(getApplicationContext(), SearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            //overridePendingTransition(0, 0);
                            return true;
                    }
                    return false;
                }
            };




}

