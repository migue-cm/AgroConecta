package com.example.agroconecta.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agroconecta.R;
import com.example.agroconecta.utils.SessionManager;

public class SplashActivity extends AppCompatActivity {

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(android.graphics.Color.TRANSPARENT);
            // Si el fondo es blanco, mantén los íconos oscuros:
            window.getDecorView().setSystemUiVisibility(android.view.View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | android.view.View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_splash);

        sessionManager = new SessionManager(this);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {

            Intent intent;

            if (sessionManager.getIdUsuario() != -1) {
                intent = new Intent(SplashActivity.this, HomeActivity.class);
            } else {
                intent = new Intent(SplashActivity.this, LoginActivity.class);
            }

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }, 2000);
    }
}