package com.example.agroconecta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agroconecta.R;
import com.example.agroconecta.adapter.PedidoAdapter;
import com.example.agroconecta.api.ApiClient;
import com.example.agroconecta.api.ApiService;
import com.example.agroconecta.model.Pedido;
import com.example.agroconecta.response.ApiResponse;
import com.example.agroconecta.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidosActivity extends AppCompatActivity {

    private RecyclerView rvPedidos;
    private TextView txtCantidadPedidos;
    private ImageButton btnBackPedidos;

    private PedidoAdapter adapter;
    private final List<Pedido> lista = new ArrayList<>();

    private ApiService api;
    private CircularProgressIndicator progressPedidos;
    private TextView txtEmptyPedidos;

    // Temporal: luego lo cambiamos por sesión real
    private int idComprador;
    private BottomNavigationView bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);

        progressPedidos = findViewById(R.id.progressPedidos);
        txtEmptyPedidos = findViewById(R.id.txtEmptyPedidos);

        rvPedidos = findViewById(R.id.rvPedidos);
        txtCantidadPedidos = findViewById(R.id.txtCantidadPedidos);
        btnBackPedidos = findViewById(R.id.btnBackPedidos);

        btnBackPedidos.setOnClickListener(v -> finish());

        rvPedidos.setLayoutManager(new LinearLayoutManager(this));
        adapter = new PedidoAdapter(this, lista);
        rvPedidos.setAdapter(adapter);

        api = ApiClient.getClient().create(ApiService.class);
        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.setSelectedItemId(R.id.nav_pedidos);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                startActivity(new Intent(PedidosActivity.this, HomeActivity.class));
                finish();
                return true;
            }

            if (id == R.id.nav_publicar) {
                startActivity(new Intent(PedidosActivity.this, PublicarActivity.class));
                finish();
                return true;
            }

            if (id == R.id.nav_pedidos) {
                return true;
            }

            if (id == R.id.nav_perfil) {
                startActivity(new Intent(PedidosActivity.this, PerfilActivity.class));
                finish();
                return true;
            }

            return false;
        });
        
        SessionManager sessionManager = new SessionManager(this);
        idComprador = sessionManager.getIdUsuario();

        cargarPedidos();
    }

    private void cargarPedidos() {

        progressPedidos.setVisibility(View.VISIBLE);
        rvPedidos.setVisibility(View.GONE);
        txtEmptyPedidos.setVisibility(View.GONE);


        api.obtenerPedidos(idComprador).enqueue(new Callback<ApiResponse<List<Pedido>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Pedido>>> call,
                                   Response<ApiResponse<List<Pedido>>> response) {

                progressPedidos.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    lista.clear();
                    lista.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();

                    txtCantidadPedidos.setText(lista.size() + " pedidos en total");
                }

                if (lista.isEmpty()) {
                    rvPedidos.setVisibility(View.GONE);
                    txtEmptyPedidos.setVisibility(View.VISIBLE);
                } else {
                    rvPedidos.setVisibility(View.VISIBLE);
                    txtEmptyPedidos.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Pedido>>> call, Throwable t) {

                progressPedidos.setVisibility(View.GONE);
                rvPedidos.setVisibility(View.GONE);
                txtEmptyPedidos.setVisibility(View.VISIBLE);
                txtEmptyPedidos.setText("No se pudieron cargar tus pedidos.\nVerifica tu conexión.");

                Toast.makeText(PedidosActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}