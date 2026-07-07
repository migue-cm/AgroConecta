package com.example.agroconecta.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.agroconecta.R;
import com.example.agroconecta.activities.DetalleActivity;
import com.example.agroconecta.model.Publicacion;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import java.util.List;

public class PublicacionAdapter extends RecyclerView.Adapter<PublicacionAdapter.ViewHolder> {

    private final Context context;
    private final List<Publicacion> lista;

    public PublicacionAdapter(Context context, List<Publicacion> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.item_publicacion, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Publicacion p = lista.get(position);

        holder.txtProducto.setText(p.getProducto());

        holder.txtCantidad.setText(
                formatearCantidad(p.getCantidadDisponible(), p.getUnidadMedida())
                        + " " + p.getUnidadMedida());

        holder.txtUbicacion.setText(
                p.getDepartamento());

        holder.txtAgricultor.setText(
                p.getAgricultor());

        holder.chipCategoria.setText(
                p.getCategoria());

        holder.chipPrecio.setText(
                "S/. " +
                        p.getPrecioUnitario() +
                        "/" +
                        p.getUnidadMedida());

        holder.txtDescripcionCorta.setText(p.getDescripcion());
        holder.txtInicialesAgricultor.setText(obtenerIniciales(p.getAgricultor()));

        Glide.with(context)
                .load("http://172.16.10.31:31255/uploads/" + p.getRutaImagen())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imgProducto);

        holder.btnComprar.setOnClickListener(v -> {
            Intent intent = new Intent(context, DetalleActivity.class);
            intent.putExtra("publicacion", p);
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProducto;

        TextView txtProducto,
                txtCantidad,
                txtUbicacion,
                txtAgricultor,
                txtDescripcionCorta,
                txtInicialesAgricultor;

        Chip chipCategoria,
                chipPrecio;

        MaterialButton btnComprar;

        ViewHolder(View itemView) {

            super(itemView);

            imgProducto = itemView.findViewById(R.id.imgProducto);

            txtProducto = itemView.findViewById(R.id.txtProducto);

            txtCantidad = itemView.findViewById(R.id.txtCantidad);

            txtUbicacion = itemView.findViewById(R.id.txtUbicacion);

            txtAgricultor = itemView.findViewById(R.id.txtAgricultor);

            txtDescripcionCorta = itemView.findViewById(R.id.txtDescripcionCorta);

            txtInicialesAgricultor = itemView.findViewById(R.id.txtInicialesAgricultor);

            chipCategoria = itemView.findViewById(R.id.chipCategoria);

            chipPrecio = itemView.findViewById(R.id.chipPrecio);

            btnComprar = itemView.findViewById(R.id.btnComprar);

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
}
