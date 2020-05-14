package com.romain.cellarv1.vue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.romain.cellarv1.R;
import com.romain.cellarv1.outils.CurvedBottomNavigationView;
import com.romain.cellarv1.outils.ShowCamera;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class ScanActivity extends AppCompatActivity {

    //private int REQUEST_CODE_PERMISSIONS = 101;
    //private String[] REQUIRERD_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private FrameLayout scanView;
    private Camera camera;
    private ShowCamera showCamera;
    private FloatingActionButton btnScan;
    private FloatingActionButton btnTorch;
    private FloatingActionButton btnGallery;

    // Appareil photo
    private String photoPath = null;
    private static final int CAMERA_PERM_CODE = 101;
    private static final int CAMERA_REQUEST_CODE = 102;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);
        final FrameLayout scanView = (FrameLayout) findViewById(R.id.frameScanView);
        init();

        camera = Camera.open();
        showCamera = new ShowCamera(this, camera);
        scanView.addView(showCamera);



    }



    private void init() {

        CurvedBottomNavigationView curvedBottomNavigationView = findViewById(R.id.curvedBottomNavigationView);
        curvedBottomNavigationView.setOnNavigationItemSelectedListener(customBottomNavListener);

        FloatingActionButton btnScan = (FloatingActionButton) findViewById(R.id.scan);
        FloatingActionButton btnGallery = (FloatingActionButton) findViewById(R.id.btnGallery);
        FloatingActionButton btnTorch = (FloatingActionButton) findViewById(R.id.btnTorch);

    }

    public void takePicture(View view) {
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

                Uri photoUri = FileProvider.getUriForFile(ScanActivity.this, ScanActivity.this.getPackageName() + ".provider", photoFile);
                //Uri photoUri = FileProvider.getUriForFile(getApplicationContext(), getApplicationContext().getPackageName() + ".provider", photoFile);

                // Transfert uri vers l'intent pour enregistrement photo dans fichier temporaire
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                // Ouvrir l'activity par rapport à l'intent
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ImageView scanImageView = (ImageView) findViewById(R.id.scanImageView); // DOIT ETRE DECLAREE ICI !!!!!!!!!!!!!!!!!!!!!!
        Log.i("BUG", "1");
        // Vérification du bon code de retour et l'état du retour ok
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK) {
            // Récupération de l'image
            Bitmap image = BitmapFactory.decodeFile(photoPath);
            Log.i("BUG", "2");
            // Afficher l'image
            scanImageView.setImageBitmap(image);
            Log.i("BUG", "3");
        }
    }






    /*
    Camera.PictureCallback mPictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File picture_file = getOutputMediaFile();
            if(picture_file == null) {
                return;
            } else {
                try {
                    FileOutputStream fos = new FileOutputStream(picture_file);
                    fos.write(data);
                    Toast.makeText(getApplicationContext(), "photo enegistrée", Toast.LENGTH_LONG).show();
                    fos.close();
                    camera.startPreview();
                } catch(FileNotFoundException e) {
                    e.printStackTrace();
                } catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private File getOutputMediaFile() {
        String state = Environment.getExternalStorageState();
        if(!state.equals(Environment.MEDIA_MOUNTED)) {
            return null;
        } else {
            File folder_gui = new File(Environment.getExternalStorageDirectory() + File.separator + "GUI");
            if (folder_gui.exists()) {
                folder_gui.mkdirs();
            }
            File outputFile = new File(folder_gui, "temp.jpg");
            return outputFile;
        }
    }
    */









    private CurvedBottomNavigationView.OnNavigationItemSelectedListener customBottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch(item.getItemId()){
                        case R.id.user:
                            //Toast.makeText(getApplicationContext(), "USER", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ScanActivity.this, UserActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.cellar:
                            //Toast.makeText(getApplicationContext(), "CELLAR", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ScanActivity.this, CellarActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.like:
                            //Toast.makeText(getApplicationContext(), "LIKE", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(), LikeActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            //overridePendingTransition(0, 0);
                            return true;
                        case R.id.search:
                            //Toast.makeText(getApplicationContext(), "SEARCH", Toast.LENGTH_SHORT).show();
                            //startActivity(new Intent(getApplicationContext(), SearchActivity.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            //overridePendingTransition(0, 0);
                            return true;
                        default:
                            //Toast.makeText(getApplicationContext(), "BUG", Toast.LENGTH_SHORT).show();
                    }
                    return false;
                }
            };

}
