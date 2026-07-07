package com.example.agroconecta.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.agroconecta.R;
import com.example.agroconecta.adapter.PublicacionAdapter;
import com.example.agroconecta.api.ApiClient;
import com.example.agroconecta.api.ApiService;
import com.example.agroconecta.model.Publicacion;
import com.example.agroconecta.response.ApiResponse;
import com.example.agroconecta.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView rvPublicaciones;
    private PublicacionAdapter adapter;
    private final List<Publicacion> lista = new ArrayList<>();
    private ApiService api;
    private CircularProgressIndicator progressHome;
    private TextView txtEmptyHome;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        rvPublicaciones = findViewById(R.id.rvPublicaciones);

        progressHome = findViewById(R.id.progressHome);
        txtEmptyHome = findViewById(R.id.txtEmptyHome);

        rvPublicaciones.setLayoutManager(
                new LinearLayoutManager(this));

        adapter = new PublicacionAdapter(this, lista);

        rvPublicaciones.setAdapter(adapter);

        api = ApiClient
                .getClient()
                .create(ApiService.class);

        cargarPublicaciones();

        bottomNavigation = findViewById(R.id.bottomNavigation);
        bottomNavigation.setSelectedItemId(R.id.nav_inicio);

        bottomNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                return true;
            }

            if (id == R.id.nav_publicar) {
                startActivity(new Intent(HomeActivity.this, PublicarActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;

            }

            if (id == R.id.nav_pedidos) {
                startActivity(new Intent(HomeActivity.this, PedidosActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }

            if (id == R.id.nav_perfil) {
                startActivity(new Intent(HomeActivity.this, PerfilActivity.class));
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                return true;
            }

            return false;
        });

        TextView txtNombreUsuario;
        SessionManager sessionManager;

        /*MaterialButton btnIrPublicar = findViewById(R.id.btnIrPublicar);

        btnIrPublicar.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, PublicarActivity.class));
        });

        MaterialButton btnEditarPerfil = findViewById(R.id.btnEditarPerfil);

        btnEditarPerfil.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, PerfilActivity.class));
        });*/

        txtNombreUsuario = findViewById(R.id.txtNombreUsuario);
        sessionManager = new SessionManager(this);

        txtNombreUsuario.setText(sessionManager.getNombres());
    }


    private void cargarPublicaciones() {

        progressHome.setVisibility(View.VISIBLE);
        rvPublicaciones.setVisibility(View.GONE);
        txtEmptyHome.setVisibility(View.GONE);

        api.obtenerPublicaciones().enqueue(new Callback<ApiResponse<List<Publicacion>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<Publicacion>>> call,
                                   Response<ApiResponse<List<Publicacion>>> response) {
                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isSuccess()) {

                    lista.clear();
                    lista.addAll(response.body().getData());
                    progressHome.setVisibility(View.GONE);

                    if (lista.isEmpty()) {
                        rvPublicaciones.setVisibility(View.GONE);
                        txtEmptyHome.setVisibility(View.VISIBLE);
                    } else {
                        rvPublicaciones.setVisibility(View.VISIBLE);
                        txtEmptyHome.setVisibility(View.GONE);
                    }

                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Publicacion>>> call,
                                  Throwable t) {
                progressHome.setVisibility(View.GONE);
                rvPublicaciones.setVisibility(View.GONE);
                txtEmptyHome.setVisibility(View.VISIBLE);
                txtEmptyHome.setText("No se pudo cargar la información.\nVerifica tu conexión.");

                Toast.makeText(HomeActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG).show();

            }

        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        if (bottomNavigation != null) {
            bottomNavigation.setSelectedItemId(R.id.nav_inicio);
        }

        if (adapter != null) {
            cargarPublicaciones();
        }
    }



}