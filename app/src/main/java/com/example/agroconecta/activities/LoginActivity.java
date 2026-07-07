package com.example.agroconecta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agroconecta.R;
import com.example.agroconecta.api.ApiClient;
import com.example.agroconecta.api.ApiService;
import com.example.agroconecta.model.LoginRequest;
import com.example.agroconecta.model.Usuario;
import com.example.agroconecta.response.ApiResponse;
import com.example.agroconecta.utils.SessionManager;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private TextInputEditText etCorreo, etPassword;
    private MaterialButton btnLogin, btnRegister;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // usa login_activity si así nombraste tu XML

        etCorreo = findViewById(R.id.etCorreo);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        progressBar = findViewById(R.id.progressBar);

        btnLogin.setOnClickListener(v -> iniciarSesion());

        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistroActivity.class);
            startActivity(intent);
        });
    }

    private void iniciarSesion() {
        String correo = etCorreo.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (correo.isEmpty()) {
            etCorreo.setError("Ingrese su correo");
            return;
        }

        if (password.isEmpty()) {
            etPassword.setError("Ingrese su contraseña");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        btnLogin.setEnabled(false);
        btnLogin.setText("Iniciando sesión...");

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        LoginRequest request = new LoginRequest(correo, password);

        apiService.login(request).enqueue(new Callback<ApiResponse<Usuario>>() {
            @Override
            public void onResponse(Call<ApiResponse<Usuario>> call, Response<ApiResponse<Usuario>> response) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                btnLogin.setText("Iniciar Sesión");

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Usuario usuario = response.body().getData();

                    SessionManager sessionManager = new SessionManager(LoginActivity.this);
                    sessionManager.guardarUsuario(usuario);

                    Toast.makeText(
                            LoginActivity.this,
                            "Bienvenido " + usuario.getNombres(),
                            Toast.LENGTH_SHORT
                    ).show();

                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this,
                            "Correo o contraseña incorrectos",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Usuario>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                btnLogin.setEnabled(true);
                btnLogin.setText("Iniciar Sesión");

                Toast.makeText(LoginActivity.this,
                        "Error de conexión: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}