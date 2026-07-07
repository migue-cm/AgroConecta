package com.example.agroconecta.model;

import com.google.gson.annotations.SerializedName;

public class Pedido {

    @SerializedName("id_pedido")
    private int idPedido;

    private String producto;

    @SerializedName("cantidad_solicitada")
    private String cantidadSolicitada;

    @SerializedName("precio_unitario")
    private String precioUnitario;

    private String total;
    private String estado;

    @SerializedName("fecha_pedido")
    private String fechaPedido;

    public int getIdPedido() { return idPedido; }
    public String getProducto() { return producto; }
    public String getCantidadSolicitada() { return cantidadSolicitada; }
    public String getPrecioUnitario() { return precioUnitario; }
    public String getTotal() { return total; }
    public String getEstado() { return estado; }
    public String getFechaPedido() { return fechaPedido; }
}
