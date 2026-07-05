package com.example.agroconecta.model;

import com.google.gson.annotations.SerializedName;

public class Producto {

    @SerializedName("id_producto")
    private int idProducto;

    @SerializedName("nombre")
    private String nombre;

    @SerializedName("categoria")
    private String categoria;

    @SerializedName("descripcion")
    private String descripcion;

    public int getIdProducto() {
        return idProducto;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public String getDescripcion() {
        return descripcion;
    }
}