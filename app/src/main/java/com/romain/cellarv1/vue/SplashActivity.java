package com.romain.cellarv1.vue;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.romain.cellarv1.R;

public class SplashActivity extends AppCompatActivity {

    private Thread objectThread;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startSplash();
    }

    private void startSplash() {
        try {

            Animation objectAnimation = AnimationUtils.loadAnimation(this, R.anim.splash_animation);
            objectAnimation.reset();
            ImageView splashImage = findViewById(R.id.splashImage);
            splashImage.clearAnimation();
            splashImage.startAnimation(objectAnimation);

            objectThread = new Thread() {
                @Override
                public void run() {
                    int pauseIt = 0;
                    while (pauseIt < 2000) {
                        try {
                            sleep(100);
                        } catch(InterruptedException e) {
                            e.printStackTrace();
                        }
                        pauseIt += 100;
                    }
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                    SplashActivity.this.finish();

                }
            };
            objectThread.start();
        } catch(Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
