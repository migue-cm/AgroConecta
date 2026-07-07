package com.example.agroconecta.model;

import com.google.gson.annotations.SerializedName;

public class CalificacionRequest {

    @SerializedName("id_pedido")
    private int idPedido;

    private int puntuacion;

    private String comentario;

    public CalificacionRequest(int idPedido, int puntuacion, String comentario) {
        this.idPedido = idPedido;
        this.puntuacion = puntuacion;
        this.comentario = comentario;
    }
}
