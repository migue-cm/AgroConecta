package com.example.agroconecta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agroconecta.R;
import com.example.agroconecta.utils.SessionManager;

public class PerfilActivity extends AppCompatActivity {

    private TextView txtNombrePerfil, txtCorreoPerfil, txtTelefonoPerfil;
    private TextView txtUbicacionPerfil, txtTipoPerfil;
    private TextView btnEditarPerfil, btnCerrarSesion;

    private SessionManager sessionManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        sessionManager = new SessionManager(this);

        txtNombrePerfil = findViewById(R.id.txtNombrePerfil);
        txtCorreoPerfil = findViewById(R.id.txtCorreoPerfil);
        txtTelefonoPerfil = findViewById(R.id.txtTelefonoPerfil);
        txtUbicacionPerfil = findViewById(R.id.txtUbicacionPerfil);
        txtTipoPerfil = findViewById(R.id.txtTipoPerfil);

        btnEditarPerfil = findViewById(R.id.btnEditarPerfil);
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion);

        cargarDatos();

        btnEditarPerfil.setOnClickListener(v ->
                Toast.makeText(this, "Editar perfil pendiente", Toast.LENGTH_SHORT).show()
        );

        btnCerrarSesion.setOnClickListener(v -> {
            sessionManager.cerrarSesion();

            Intent intent = new Intent(PerfilActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
    }

    private void cargarDatos() {
        txtNombrePerfil.setText(sessionManager.getNombres());

        txtCorreoPerfil.setText("Correo\n" + sessionManager.getCorreo());
        txtTelefonoPerfil.setText("Teléfono\n" + sessionManager.getTelefono());

        txtUbicacionPerfil.setText(
                "Ubicación\n" +
                        sessionManager.getDepartamento() + ", " +
                        sessionManager.getProvincia() + ", " +
                        sessionManager.getDistrito()
        );

        txtTipoPerfil.setText("Tipo de usuario\n" + sessionManager.getTipoUsuario());
    }
}