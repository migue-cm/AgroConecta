package com.example.agroconecta.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agroconecta.R;
import com.example.agroconecta.activities.CalificarActivity;
import com.example.agroconecta.model.Pedido;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PedidoAdapter extends RecyclerView.Adapter<PedidoAdapter.ViewHolder> {

    private final Context context;
    private final List<Pedido> lista;

    public PedidoAdapter(Context context, List<Pedido> lista) {
        this.context = context;
        this.lista = lista;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pedido, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Pedido p = lista.get(position);

        holder.txtProducto.setText(p.getProducto());
        holder.txtEstado.setText(p.getEstado());
        holder.txtFecha.setText("Pedido #" + p.getIdPedido() + " · " + p.getFechaPedido());
        holder.txtCantidad.setText("Cantidad\n" + p.getCantidadSolicitada() + " kg");
        holder.txtTotal.setText("Total\nS/ " + p.getTotal());

        if ("ENTREGADO".equalsIgnoreCase(p.getEstado())) {
            holder.btnCalificar.setVisibility(View.VISIBLE);
        } else {
            holder.btnCalificar.setVisibility(View.GONE);
        }

        holder.btnCalificar.setOnClickListener(v -> {
            Intent intent = new Intent(context, CalificarActivity.class);
            intent.putExtra("id_pedido", p.getIdPedido());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtProducto, txtEstado, txtFecha, txtCantidad, txtTotal;
        MaterialButton btnCalificar;

        ViewHolder(View itemView) {
            super(itemView);

            txtProducto = itemView.findViewById(R.id.txtProductoPedidoItem);
            txtEstado = itemView.findViewById(R.id.txtEstadoPedido);
            txtFecha = itemView.findViewById(R.id.txtFechaPedido);
            txtCantidad = itemView.findViewById(R.id.txtCantidadPedidoItem);
            txtTotal = itemView.findViewById(R.id.txtTotalPedidoItem);
            btnCalificar = itemView.findViewById(R.id.btnCalificarPedido);
        }
    }
}
