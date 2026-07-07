package com.example.agroconecta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.agroconecta.R;
import com.example.agroconecta.api.ApiClient;
import com.example.agroconecta.api.ApiService;
import com.example.agroconecta.model.PedidoRequest;
import com.example.agroconecta.model.Publicacion;
import com.example.agroconecta.response.ApiResponse;
import com.example.agroconecta.utils.SessionManager;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PedidoActivity extends AppCompatActivity {

    private Publicacion publicacion;
    private int cantidad = 1;

    private ImageView imgPedido;
    private TextView txtProductoPedido, txtPrecioPedido, txtCantidadPedido, txtResumenPedido;
    private MaterialButton btnMas, btnMenos, btnConfirmarPedido;
    private ImageButton btnBackPedido;

    private ApiService api;

    // Temporal: luego lo sacaremos de SharedPreferences
    private int idComprador;
    private ProgressBar progressPedido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedido);

        publicacion = (Publicacion) getIntent().getSerializableExtra("publicacion");

        if (publicacion == null) {
            Toast.makeText(this, "No se pudo cargar el pedido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        api = ApiClient.getClient().create(ApiService.class);

        inicializarVistas();
        mostrarDatos();
        actualizarResumen();

        SessionManager sessionManager = new SessionManager(this);
        idComprador = sessionManager.getIdUsuario();

        btnBackPedido.setOnClickListener(v -> finish());

        btnMas.setOnClickListener(v -> {
            cantidad++;
            actualizarResumen();
        });

        btnMenos.setOnClickListener(v -> {
            if (cantidad > 1) {
                cantidad--;
                actualizarResumen();
            }
        });

        btnConfirmarPedido.setOnClickListener(v -> confirmarPedido());
    }

    private void inicializarVistas() {
        imgPedido = findViewById(R.id.imgPedido);
        txtProductoPedido = findViewById(R.id.txtProductoPedido);
        txtPrecioPedido = findViewById(R.id.txtPrecioPedido);
        txtCantidadPedido = findViewById(R.id.txtCantidadPedido);
        txtResumenPedido = findViewById(R.id.txtResumenPedido);
        btnMas = findViewById(R.id.btnMas);
        btnMenos = findViewById(R.id.btnMenos);
        btnConfirmarPedido = findViewById(R.id.btnConfirmarPedido);
        btnBackPedido = findViewById(R.id.btnBackPedido);
        progressPedido = findViewById(R.id.progressPedido);
        progressPedido = findViewById(R.id.progressPedido);
    }

    private void mostrarDatos() {
        txtProductoPedido.setText(publicacion.getProducto());
        txtPrecioPedido.setText("S/ " + publicacion.getPrecioUnitario());

        Glide.with(this)
                .load("http://172.16.10.31:31255/uploads/" + publicacion.getRutaImagen())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imgPedido);
    }

    private void actualizarResumen() {
        double precio = Double.parseDouble(publicacion.getPrecioUnitario());
        double total = precio * cantidad;

        txtCantidadPedido.setText(String.valueOf(cantidad));

        txtResumenPedido.setText(
                "Precio unitario: S/ " + String.format("%.2f", precio) +
                        "\nCantidad: " + cantidad + " " + publicacion.getUnidadMedida() +
                        "\nTotal: S/ " + String.format("%.2f", total)
        );
    }

    private void confirmarPedido() {
        double precio = Double.parseDouble(publicacion.getPrecioUnitario());
        double total = precio * cantidad;

        PedidoRequest request = new PedidoRequest(
                publicacion.getIdPublicacion(),
                idComprador,
                cantidad,
                precio,
                total
        );

        btnConfirmarPedido.setEnabled(false);
        btnConfirmarPedido.setText("Confirmando...");
        progressPedido.setVisibility(android.view.View.VISIBLE);

        api.crearPedido(request).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call,
                                   Response<ApiResponse<Object>> response) {

                btnConfirmarPedido.setText("Confirmar Pedido");
                progressPedido.setVisibility(android.view.View.GONE);
                btnConfirmarPedido.setEnabled(true);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(PedidoActivity.this,
                            "Pedido registrado correctamente",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(PedidoActivity.this, PedidosActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(PedidoActivity.this,
                            "No se pudo registrar el pedido",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                btnConfirmarPedido.setText("Confirmar Pedido");
                progressPedido.setVisibility(android.view.View.GONE);
                btnConfirmarPedido.setEnabled(true);

                Toast.makeText(PedidoActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}