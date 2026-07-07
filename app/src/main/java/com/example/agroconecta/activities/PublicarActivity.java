package com.example.agroconecta.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
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
import com.example.agroconecta.model.Producto;
import com.example.agroconecta.model.PublicacionRequest;
import com.example.agroconecta.response.ApiResponse;
import com.example.agroconecta.utils.SessionManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.Calendar;

public class PublicarActivity extends AppCompatActivity {

    private Spinner spProductos, spUnidad;
    private TextInputEditText etCantidad, etPrecio, etFechaCosecha, etDescripcion;
    private MaterialButton btnPublicar;
    private ImageButton btnBackPublicar;

    private ApiService api;
    private SessionManager sessionManager;

    private final List<Producto> productos = new ArrayList<>();
    private final List<String> nombresProductos = new ArrayList<>();

    private int idUsuario;

    private ProgressBar progressPublicar;
    private BottomNavigationView bottomNavigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publicar);

        api = ApiClient.getClient().create(ApiService.class);
        sessionManager = new SessionManager(this);
        idUsuario = sessionManager.getIdUsuario();

        inicializarVistas();

        bottomNavigation = findViewById(R.id.bottomNavigation);

        bottomNavigation.setSelectedItemId(R.id.nav_publicar);

        bottomNavigation.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_inicio) {
                startActivity(new Intent(PublicarActivity.this, HomeActivity.class));
                finish();
                return true;
            }

            if (id == R.id.nav_publicar) {
                return true;
            }

            if (id == R.id.nav_pedidos) {
                startActivity(new Intent(PublicarActivity.this, PedidosActivity.class));
                finish();
                return true;
            }

            if (id == R.id.nav_perfil) {
                startActivity(new Intent(PublicarActivity.this, PerfilActivity.class));
                finish();
                return true;
            }

            return false;
        });

        cargarUnidades();
        cargarProductos();

        btnBackPublicar.setOnClickListener(v -> finish());
        btnPublicar.setOnClickListener(v -> publicar());

        etFechaCosecha.setFocusable(false);
        etFechaCosecha.setOnClickListener(v -> mostrarDatePicker());
    }

    private void inicializarVistas() {
        spProductos = findViewById(R.id.spProductos);
        spUnidad = findViewById(R.id.spUnidad);
        etCantidad = findViewById(R.id.etCantidad);
        etPrecio = findViewById(R.id.etPrecio);
        etFechaCosecha = findViewById(R.id.etFechaCosecha);
        etDescripcion = findViewById(R.id.etDescripcion);
        btnPublicar = findViewById(R.id.btnPublicar);
        btnBackPublicar = findViewById(R.id.btnBackPublicar);
        progressPublicar = findViewById(R.id.progressPublicar);

    }

    private void cargarUnidades() {
        String[] unidades = {"kg", "tonelada", "caja", "saco"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_dropdown_item,
                unidades
        );

        spUnidad.setAdapter(adapter);
    }

    private void cargarProductos() {
        api.obtenerProductos().enqueue(new Callback<ApiResponse<List<Producto>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Producto>>> call,
                                   Response<ApiResponse<List<Producto>>> response) {

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    productos.clear();
                    nombresProductos.clear();

                    productos.addAll(response.body().getData());

                    for (Producto p : productos) {
                        nombresProductos.add(p.getNombre());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            PublicarActivity.this,
                            android.R.layout.simple_spinner_dropdown_item,
                            nombresProductos
                    );

                    spProductos.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Producto>>> call, Throwable t) {
                Toast.makeText(PublicarActivity.this,
                        "Error al cargar productos",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void publicar() {
        if (productos.isEmpty()) {
            Toast.makeText(this, "No hay productos cargados", Toast.LENGTH_SHORT).show();
            return;
        }

        String cantidadTxt = etCantidad.getText().toString().trim();
        String precioTxt = etPrecio.getText().toString().trim();
        String fecha = etFechaCosecha.getText().toString().trim();
        String descripcion = etDescripcion.getText().toString().trim();

        if (cantidadTxt.isEmpty() || precioTxt.isEmpty() || fecha.isEmpty()) {
            Toast.makeText(this, "Completa cantidad, precio y fecha", Toast.LENGTH_SHORT).show();
            return;
        }

        Producto producto = productos.get(spProductos.getSelectedItemPosition());

        double cantidad = Double.parseDouble(cantidadTxt);
        double precio = Double.parseDouble(precioTxt);
        String unidad = spUnidad.getSelectedItem().toString();

        PublicacionRequest request = new PublicacionRequest(
                idUsuario,
                producto.getIdProducto(),
                cantidad,
                unidad,
                precio,
                descripcion,
                obtenerImagenPorProducto(producto.getNombre()),
                fecha
        );

        btnPublicar.setEnabled(false);
        btnPublicar.setText("Publicando...");
        progressPublicar.setVisibility(android.view.View.VISIBLE);

        api.crearPublicacion(request).enqueue(new Callback<ApiResponse<Object>>() {
            @Override
            public void onResponse(Call<ApiResponse<Object>> call,
                                   Response<ApiResponse<Object>> response) {

                btnPublicar.setEnabled(true);
                btnPublicar.setText("Publicar");
                progressPublicar.setVisibility(android.view.View.GONE);

                if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                    Toast.makeText(PublicarActivity.this,
                            "Publicación creada correctamente",
                            Toast.LENGTH_SHORT).show();

                    finish();
                } else {
                    Toast.makeText(PublicarActivity.this,
                            "No se pudo crear la publicación",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Object>> call, Throwable t) {
                btnPublicar.setEnabled(true);
                btnPublicar.setText("Publicar");
                progressPublicar.setVisibility(android.view.View.GONE);

                Toast.makeText(PublicarActivity.this,
                        "Error: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void mostrarDatePicker() {
        Calendar calendar = Calendar.getInstance();

        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String fecha = String.format(
                            Locale.US,
                            "%04d-%02d-%02d",
                            selectedYear,
                            selectedMonth + 1,
                            selectedDay
                    );

                    etFechaCosecha.setText(fecha);
                },
                year,
                month,
                day
        );

        datePickerDialog.show();
    }

    private String obtenerImagenPorProducto(String nombreProducto) {
        if (nombreProducto == null) return "placeholder.jpg";

        // Limpiamos el texto por completo
        nombreProducto = nombreProducto.toLowerCase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ñ", "n")
                .trim();

        // En lugar de un switch estricto, usamos .contains() para buscar la palabra clave
        if (nombreProducto.contains("aji")) return "aji.jpg";
        if (nombreProducto.contains("arroz")) return "arroz.jpg";
        if (nombreProducto.contains("brocoli")) return "brocoli.jpg";
        if (nombreProducto.contains("limon")) return "limon.jpg";
        if (nombreProducto.contains("naranja")) return "naranja.jpg";
        if (nombreProducto.contains("platano")) return "platano.jpg";
        if (nombreProducto.contains("papa")) return "papa.jpg";
        if (nombreProducto.contains("tomate")) return "tomate.jpg";
        if (nombreProducto.contains("palta")) return "palta.jpg";
        if (nombreProducto.contains("cebolla")) return "cebolla.jpg";
        if (nombreProducto.contains("zanahoria")) return "zanahoria.jpg";
        if (nombreProducto.contains("maiz")) return "maiz.jpg";
        if (nombreProducto.contains("quinua")) return "quinua.jpg";
        if (nombreProducto.contains("cafe")) return "cafe.jpg";
        if (nombreProducto.contains("cacao")) return "cacao.jpg";
        if (nombreProducto.contains("camote")) return "camote.jpg";
        if (nombreProducto.contains("fresa")) return "fresa.jpg";
        if (nombreProducto.contains("lechuga")) return "lechuga.jpg";
        if (nombreProducto.contains("manzana")) return "manzana.jpg";
        if (nombreProducto.contains("naranja")) return "naranja.jpg";
        if (nombreProducto.contains("yuca")) return "yuca.jpg";
        return "placeholder.jpg"; // Si no encuentra nada, usa el respaldo
    }
}