package com.example.agroconecta.activities;

import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        rvPublicaciones = findViewById(R.id.rvPublicaciones);

        rvPublicaciones.setLayoutManager(
                new LinearLayoutManager(this));

        adapter = new PublicacionAdapter(this, lista);

        rvPublicaciones.setAdapter(adapter);

        api = ApiClient
                .getClient()
                .create(ApiService.class);

        cargarPublicaciones();
    }

    private void cargarPublicaciones() {

        api.obtenerPublicaciones().enqueue(new Callback<ApiResponse<List<Publicacion>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<Publicacion>>> call,
                                   Response<ApiResponse<List<Publicacion>>> response) {

                if (response.isSuccessful()
                        && response.body() != null
                        && response.body().isSuccess()) {

                    lista.clear();

                    lista.addAll(response.body().getData());

                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onFailure(Call<ApiResponse<List<Publicacion>>> call,
                                  Throwable t) {

                Toast.makeText(HomeActivity.this,
                        t.getMessage(),
                        Toast.LENGTH_LONG).show();

            }

        });

    }

}