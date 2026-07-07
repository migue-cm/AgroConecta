package com.example.agroconecta.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agroconecta.R;
import com.example.agroconecta.api.ApiClient;
import com.example.agroconecta.api.ApiService;
import com.example.agroconecta.model.RegisterRequest;
import com.example.agroconecta.response.ApiResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegistroActivity extends AppCompatActivity {

    private TextInputEditText etNombres, etApellidos, etCorreo, etTelefono, etPassword;
    private Spinner spDepartamento, spProvincia, spDistrito;
    private MaterialButton btnAgricultor, btnComprador, btnRegistrarme;
    private ImageButton btnBackRegistro;

    private ApiService api;
    private String tipoUsuario = "AGRICULTOR";
    private ProgressBar progressRegistro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        api = ApiClient.getClient().create(ApiService.class);

        inicializarVistas();
        cargarUbicacion();

        btnBackRegistro.setOnClickListener(v -> finish());

        btnAgricultor.setOnClickListener(v -> {
            tipoUsuario = "AGRICULTOR";
            seleccionarTipo(true);
        });

        btnComprador.setOnClickListener(v -> {
            tipoUsuario = "COMPRADOR";
            seleccionarTipo(false);
        });

        btnRegistrarme.setOnClickListener(v -> registrar());
    }

    private void inicializarVistas() {
        etNombres = findViewById(R.id.etNombres);
        etApellidos = findViewById(R.id.etApellidos);
        etCorreo = findViewById(R.id.etCorreoRegistro);
        etTelefono = findViewById(R.id.etTelefonoRegistro);
        etPassword = findViewById(R.id.etPasswordRegistro);

        spDepartamento = findViewById(R.id.spDepartamento);
        spProvincia = findViewById(R.id.spProvincia);
        spDistrito = findViewById(R.id.spDistrito);

        btnAgricultor = findViewById(R.id.btnAgricultor);
        btnComprador = findViewById(R.id.btnComprador);
        btnRegistrarme = findViewById(R.id.btnRegistrarme);
        btnBackRegistro = findViewById(R.id.btnBackRegistro);
        progressRegistro = findViewById(R.id.progressRegistro);
    }

    private void cargarUbicacion() {
        String[] departamentos = {"Arequipa", "Cusco", "Puno", "Tacna", "Moquegua"};
        String[] provincias = {"Arequipa", "Caylloma", "Urubamba", "San Román", "Tacna"};
        String[] distritos = {"Cercado", "Chivay", "Yanque", "Majes", "Ollantaytambo"};

        spDepartamento.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                departamentos
        ));

        spProvincia.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                provincias
        ));

        spDistrito.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                distritos
        ));
    }

    private void seleccionarTipo(boolean agricultor) {
        if (agricultor) {
            btnAgricultor.setBackgroundTintList(getColorStateList(R.color.primary_green));
            btnAgricultor.setTextColor(getColor(R.color.button_text));

            btnComprador.setBackgroundTintList(getColorStateList(R.color.background));
            btnComprador.setTextColor(getColor(R.color.primary_green));
        } else {
            btnComprador.setBackgroundTintList(getColorStateList(R.color.primary_green));
            btnComprador.setTextColor(getColor(R.color.button_text));

            btnAgricultor.setBackgroundTintList(getColorStateList(R.color.background));
            btnAgricultor.setTextColor(getColor(R.color.primary_green));
        }
    }

    private void registrar() {
        String nombres = etNombres.getText().toString().trim();
        String apellidos = etApellidos.getText().toString().trim();
        String correo = etCorreo.getText().toString().trim();
        String telefono = etTelefono.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        String departamento = spDepartamento.getSelectedItem().toString();
        String provincia = spProvincia.getSelectedItem().toString();
        String distrito = spDistrito.getSelectedItem().toString();

        if (nombres.isEmpty() || apellidos.isEmpty() || correo.isEmpty()
                || telefono.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 8) {
            Toast.makeText(this, "La contraseña debe tener al menos 8 caracteres", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request = new RegisterRequest(
                nombres,
                apellidos,
                correo,
                telefono,
                password,
                tipoUsuario,
                departamento,
                provincia,
                distrito
        );

        btnRegistrarme.setEnabled(false);
        btnRegistrarme.setText("Registrando...");
        progressRegistro.setVisibility(android.view.View.VISIBLE);

        api.register(request).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call,
                                   Response<ApiResponse<Object>> response) {

                btnRegistrarme.setEnabled(true);
                btnRegistrarme.setText("Registrarme");
                progressRegistro.setVisibility(android.view.View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(RegistroActivity.this,
                            "Cuenta creada correctamente",
                            Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(RegistroActivity.this,
                            "No se pudo registrar el usuario",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                btnRegistrarme.setEnabled(true);
                btnRegistrarme.setText("Registrarme");
                progressRegistro.setVisibility(android.view.View.GONE);

                Toast.makeText(RegistroActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}