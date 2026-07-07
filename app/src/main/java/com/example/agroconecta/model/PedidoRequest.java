package com.example.agroconecta.model;

import com.google.gson.annotations.SerializedName;

public class PedidoRequest {

    @SerializedName("id_publicacion")
    private int idPublicacion;

    @SerializedName("id_comprador")
    private int idComprador;

    @SerializedName("cantidad_solicitada")
    private double cantidadSolicitada;

    @SerializedName("precio_unitario")
    private double precioUnitario;

    private double total;

    public PedidoRequest(int idPublicacion, int idComprador, double cantidadSolicitada, double precioUnitario, double total) {
        this.idPublicacion = idPublicacion;
        this.idComprador = idComprador;
        this.cantidadSolicitada = cantidadSolicitada;
        this.precioUnitario = precioUnitario;
        this.total = total;
    }
}