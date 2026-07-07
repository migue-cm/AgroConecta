package com.example.agroconecta.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Publicacion implements Serializable {

    @SerializedName("id_publicacion")
    private int idPublicacion;

    @SerializedName("producto")
    private String producto;

    @SerializedName("categoria")
    private String categoria;

    @SerializedName("cantidad_disponible")
    private String cantidadDisponible;

    @SerializedName("unidad_medida")
    private String unidadMedida;

    @SerializedName("precio_unitario")
    private String precioUnitario;

    @SerializedName("descripcion")
    private String descripcion;

    @SerializedName("ruta_imagen")
    private String rutaImagen;

    @SerializedName("fecha_cosecha")
    private String fechaCosecha;

    @SerializedName("fecha_publicacion")
    private String fechaPublicacion;

    @SerializedName("id_usuario")
    private int idUsuario;

    @SerializedName("agricultor")
    private String agricultor;

    @SerializedName("departamento")
    private String departamento;

    @SerializedName("provincia")
    private String provincia;

    @SerializedName("distrito")
    private String distrito;

    public int getIdPublicacion() { return idPublicacion; }
    public String getProducto() { return producto; }
    public String getCategoria() { return categoria; }
    public String getCantidadDisponible() { return cantidadDisponible; }
    public String getUnidadMedida() { return unidadMedida; }
    public String getPrecioUnitario() { return precioUnitario; }
    public String getDescripcion() { return descripcion; }
    public String getRutaImagen() { return rutaImagen; }
    public String getFechaCosecha() { return fechaCosecha; }
    public String getFechaPublicacion() { return fechaPublicacion; }
    public int getIdUsuario() { return idUsuario; }
    public String getAgricultor() { return agricultor; }
    public String getDepartamento() { return departamento; }
    public String getProvincia() { return provincia; }
    public String getDistrito() { return distrito; }
}