package com.example.recom;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

@SuppressLint("CustomSplashScreen")
public class BasicSplashScreen extends AppCompatActivity {

    private AppCompatImageView splash_logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        findSplashViews();
        startAnimation(splash_logo);
    }

    private void startAnimation(AppCompatImageView splashLogo) {
        splashLogo.setImageResource(R.drawable.envelope_closed);
        splashLogo.postDelayed(() -> {
            // Fade out the closed envelope
            splashLogo.animate().alpha(0f).setDuration(500).withEndAction(() -> {
                // Change to open envelope
                splashLogo.setImageResource(R.drawable.envelope_opened);
                // Fade in the open envelope
                splashLogo.animate().alpha(1f).setDuration(500).withEndAction(() -> {
                    // Transition to the login activity after the animation
                    Intent intent = new Intent(BasicSplashScreen.this, LogInActivity.class);
                    startActivity(intent);
                    finish();
                }).start();
            }).start();
        }, 500); // Wait 0.5 second before starting the animation
    }

    private void findSplashViews() {
        splash_logo = findViewById(R.id.splash_logo);
    }
}
