package com.example.agroconecta.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.agroconecta.R;
import com.example.agroconecta.model.Publicacion;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

public class DetalleActivity extends AppCompatActivity {

    private Publicacion publicacion;

    private ImageView imgDetalle;
    private Chip chipCategoriaDetalle;
    private TextView txtProductoDetalle, txtPrecioDetalle, txtUnidadDetalle;
    private TextView txtDisponibleDetalle, txtCosechaDetalle, txtUbicacionDetalle;
    private TextView txtDescripcionDetalle, txtAgricultorDetalle, txtIniciales;
    private MaterialButton btnComprarAhora;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);

        publicacion = (Publicacion) getIntent().getSerializableExtra("publicacion");

        if (publicacion == null) {
            Toast.makeText(this, "No se pudo cargar el detalle", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        inicializarVistas();
        mostrarDatos();

        btnBack.setOnClickListener(v -> finish());

        btnComprarAhora.setOnClickListener(v -> {
            Intent intent = new Intent(DetalleActivity.this, PedidoActivity.class);
            intent.putExtra("publicacion", publicacion);
            startActivity(intent);
        });
    }

    private void inicializarVistas() {
        imgDetalle = findViewById(R.id.imgDetalle);
        chipCategoriaDetalle = findViewById(R.id.chipCategoriaDetalle);
        txtProductoDetalle = findViewById(R.id.txtProductoDetalle);
        txtPrecioDetalle = findViewById(R.id.txtPrecioDetalle);
        txtUnidadDetalle = findViewById(R.id.txtUnidadDetalle);
        txtDisponibleDetalle = findViewById(R.id.txtDisponibleDetalle);
        txtCosechaDetalle = findViewById(R.id.txtCosechaDetalle);
        txtUbicacionDetalle = findViewById(R.id.txtUbicacionDetalle);
        txtDescripcionDetalle = findViewById(R.id.txtDescripcionDetalle);
        txtAgricultorDetalle = findViewById(R.id.txtAgricultorDetalle);
        txtIniciales = findViewById(R.id.txtIniciales);
        btnComprarAhora = findViewById(R.id.btnComprarAhora);
        btnBack = findViewById(R.id.btnBack);
    }

    private void mostrarDatos() {
        txtProductoDetalle.setText(publicacion.getProducto());
        chipCategoriaDetalle.setText(publicacion.getCategoria());

        txtPrecioDetalle.setText("S/ " + publicacion.getPrecioUnitario());
        txtUnidadDetalle.setText("por " + publicacion.getUnidadMedida());

        txtDisponibleDetalle.setText(
                "Disponible\n" +
                        formatearCantidad(publicacion.getCantidadDisponible(), publicacion.getUnidadMedida())
                        + " " + publicacion.getUnidadMedida()
        );

        txtCosechaDetalle.setText(
                "Cosecha\n" + publicacion.getFechaCosecha()
        );

        txtUbicacionDetalle.setText(
                "Ubicación\n" + publicacion.getDepartamento()
        );

        txtDescripcionDetalle.setText(publicacion.getDescripcion());
        txtAgricultorDetalle.setText(publicacion.getAgricultor());

        txtIniciales.setText(obtenerIniciales(publicacion.getAgricultor()));

        Glide.with(this)
                .load("http://172.16.10.31:31255/uploads/" + publicacion.getRutaImagen())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(imgDetalle);
    }

    private String formatearCantidad(String cantidad, String unidad) {
        try {
            double valor = Double.parseDouble(cantidad);

            if (unidad.equalsIgnoreCase("saco") ||
                    unidad.equalsIgnoreCase("caja")) {
                return String.valueOf((int) valor);
            }

            return cantidad;

        } catch (Exception e) {
            return cantidad;
        }
    }

    private String obtenerIniciales(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) return "AG";

        String[] partes = nombre.trim().split(" ");

        if (partes.length >= 2) {
            return (partes[0].substring(0, 1) + partes[1].substring(0, 1)).toUpperCase();
        }

        return partes[0].substring(0, 1).toUpperCase();
    }
}