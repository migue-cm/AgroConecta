package com.example.agroconecta;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.util.Log;

import com.example.agroconecta.api.ApiClient;
import com.example.agroconecta.api.ApiService;
import com.example.agroconecta.model.Producto;
import com.example.agroconecta.response.ApiResponse;

import java.util.List;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ApiService api = ApiClient
                .getClient()
                .create(ApiService.class);

        api.obtenerProductos().enqueue(new Callback<ApiResponse<List<Producto>>>() {

            @Override
            public void onResponse(Call<ApiResponse<List<Producto>>> call,
                                   Response<ApiResponse<List<Producto>>> response) {

                if(response.isSuccessful()){

                    ApiResponse<List<Producto>> respuesta = response.body();

                    Log.d("API", respuesta.getMessage());

                    for(Producto p : respuesta.getData()){

                        Log.d("PRODUCTO", p.getNombre());

                    }

                }

            }

            @Override
            public void onFailure(Call<ApiResponse<List<Producto>>> call,
                                  Throwable t) {

                Log.e("API", t.getMessage());

            }

        });

    }
}