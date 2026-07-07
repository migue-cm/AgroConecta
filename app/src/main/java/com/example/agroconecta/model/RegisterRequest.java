package com.example.agroconecta.model;

import com.google.gson.annotations.SerializedName;

public class RegisterRequest {

    private String nombres;
    private String apellidos;
    private String correo;
    private String telefono;
    private String password;

    @SerializedName("tipo_usuario")
    private String tipoUsuario;

    private String departamento;
    private String provincia;
    private String distrito;

    public RegisterRequest(String nombres, String apellidos, String correo, String telefono,
                           String password, String tipoUsuario, String departamento,
                           String provincia, String distrito) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.password = password;
        this.tipoUsuario = tipoUsuario;
        this.departamento = departamento;
        this.provincia = provincia;
        this.distrito = distrito;
    }
}
