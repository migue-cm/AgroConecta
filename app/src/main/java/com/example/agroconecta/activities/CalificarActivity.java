package com.example.agroconecta.activities;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.agroconecta.R;
import com.example.agroconecta.api.ApiClient;
import com.example.agroconecta.api.ApiService;
import com.example.agroconecta.model.CalificacionRequest;
import com.example.agroconecta.response.ApiResponse;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalificarActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView txtTextoRating;
    private TextInputEditText etComentario;
    private MaterialButton btnEnviarCalificacion;
    private ImageButton btnBackCalificar;

    private ApiService api;

    private int puntuacionSeleccionada = 0;

    // Temporal: luego se puede pasar desde PedidosActivity
    private int idPedido;

    private ProgressBar progressCalificar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calificar);

        api = ApiClient.getClient().create(ApiService.class);

        ratingBar = findViewById(R.id.ratingBar);
        txtTextoRating = findViewById(R.id.txtTextoRating);
        etComentario = findViewById(R.id.etComentario);
        btnEnviarCalificacion = findViewById(R.id.btnEnviarCalificacion);
        btnBackCalificar = findViewById(R.id.btnBackCalificar);
        progressCalificar = findViewById(R.id.progressCalificar);

        btnBackCalificar.setOnClickListener(v -> finish());

        configurarRating();

        btnEnviarCalificacion.setOnClickListener(v -> enviarCalificacion());

        idPedido = getIntent().getIntExtra("id_pedido", -1);

        if (idPedido == -1) {
            Toast.makeText(this, "No se recibió el pedido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
    }

    private void configurarRating() {
        ratingBar.setRating(0);
        txtTextoRating.setText("");
        txtTextoRating.setVisibility(TextView.GONE);

        btnEnviarCalificacion.setEnabled(false);
        btnEnviarCalificacion.setBackgroundTintList(
                ColorStateList.valueOf(ContextCompat.getColor(this, R.color.accent_green))
        );

        ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            puntuacionSeleccionada = (int) rating;

            if (puntuacionSeleccionada > 0) {
                txtTextoRating.setVisibility(TextView.VISIBLE);
                txtTextoRating.setText(obtenerTextoRating(puntuacionSeleccionada));

                btnEnviarCalificacion.setEnabled(true);
                btnEnviarCalificacion.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.primary_green))
                );
            } else {
                txtTextoRating.setVisibility(TextView.GONE);
                txtTextoRating.setText("");

                btnEnviarCalificacion.setEnabled(false);
                btnEnviarCalificacion.setBackgroundTintList(
                        ColorStateList.valueOf(ContextCompat.getColor(this, R.color.accent_green))
                );
            }
        });
    }

    private String obtenerTextoRating(int rating) {
        switch (rating) {
            case 1:
                return "Muy malo";
            case 2:
                return "Malo";
            case 3:
                return "Regular";
            case 4:
                return "Bueno";
            case 5:
                return "Excelente";
            default:
                return "";
        }
    }

    private void enviarCalificacion() {
        String comentario = etComentario.getText() != null
                ? etComentario.getText().toString().trim()
                : "";

        CalificacionRequest request = new CalificacionRequest(
                idPedido,
                puntuacionSeleccionada,
                comentario
        );

        btnEnviarCalificacion.setEnabled(false);
        btnEnviarCalificacion.setText("Enviando...");
        progressCalificar.setVisibility(android.view.View.VISIBLE);

        api.crearCalificacion(request).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call,
                                   Response<ApiResponse<Object>> response) {

                btnEnviarCalificacion.setText("Enviar Calificación");
                progressCalificar.setVisibility(android.view.View.GONE);
                btnEnviarCalificacion.setEnabled(true);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(CalificarActivity.this,
                            "Calificación enviada correctamente",
                            Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(CalificarActivity.this,
                            "No se pudo enviar la calificación",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                btnEnviarCalificacion.setText("Enviar Calificación");
                progressCalificar.setVisibility(android.view.View.GONE);
                btnEnviarCalificacion.setEnabled(true);

                Toast.makeText(CalificarActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }
}