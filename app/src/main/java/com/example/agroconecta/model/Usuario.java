package com.example.agroconecta.model;

import com.google.gson.annotations.SerializedName;

public class Usuario {

    @SerializedName("id_usuario")
    private int idUsuario;

    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;

    @SerializedName("tipo_usuario")
    private String tipoUsuario;

    private String departamento;
    private String provincia;
    private String distrito;
    private String estado;

    public int getIdUsuario() { return idUsuario; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public String getTipoUsuario() { return tipoUsuario; }
    public String getDepartamento() { return departamento; }
    public String getProvincia() { return provincia; }
    public String getDistrito() { return distrito; }
    public String getEstado() { return estado; }
}
