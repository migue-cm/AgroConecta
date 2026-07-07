package com.example.agroconecta.model;

import com.google.gson.annotations.SerializedName;

public class PublicacionRequest {

    @SerializedName("id_usuario")
    private int idUsuario;

    @SerializedName("id_producto")
    private int idProducto;

    @SerializedName("cantidad_disponible")
    private double cantidadDisponible;

    @SerializedName("unidad_medida")
    private String unidadMedida;

    @SerializedName("precio_unitario")
    private double precioUnitario;

    private String descripcion;

    @SerializedName("ruta_imagen")
    private String rutaImagen;

    @SerializedName("fecha_cosecha")
    private String fechaCosecha;

    public PublicacionRequest(int idUsuario, int idProducto, double cantidadDisponible,
                              String unidadMedida, double precioUnitario,
                              String descripcion, String rutaImagen, String fechaCosecha) {
        this.idUsuario = idUsuario;
        this.idProducto = idProducto;
        this.cantidadDisponible = cantidadDisponible;
        this.unidadMedida = unidadMedida;
        this.precioUnitario = precioUnitario;
        this.descripcion = descripcion;
        this.rutaImagen = rutaImagen;
        this.fechaCosecha = fechaCosecha;
    }
}
