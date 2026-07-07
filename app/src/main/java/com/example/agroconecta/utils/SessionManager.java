package com.example.agroconecta.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.agroconecta.model.Usuario;

public class SessionManager {

    private static final String PREF_NAME = "agro_session";
    private static final String ID_USUARIO = "id_usuario";
    private static final String NOMBRES = "nombres";
    private static final String TIPO_USUARIO = "tipo_usuario";

    private SharedPreferences prefs;

    public SessionManager(Context context) {
        prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public void guardarUsuario(Usuario usuario) {
        prefs.edit()
                .putInt(ID_USUARIO, usuario.getIdUsuario())
                .putString(NOMBRES, usuario.getNombres())
                .putString(TIPO_USUARIO, usuario.getTipoUsuario())
                .putString(CORREO, usuario.getCorreo())
                .putString(TELEFONO, usuario.getTelefono())
                .putString(DEPARTAMENTO, usuario.getDepartamento())
                .putString(PROVINCIA, usuario.getProvincia())
                .putString(DISTRITO, usuario.getDistrito())
                .apply();
    }

    public int getIdUsuario() {
        return prefs.getInt(ID_USUARIO, -1);
    }

    public String getNombres() {
        return prefs.getString(NOMBRES, "Usuario");
    }

    public String getTipoUsuario() {
        return prefs.getString(TIPO_USUARIO, "");
    }
    public String getCorreo() { return prefs.getString(CORREO, ""); }
    public String getTelefono() { return prefs.getString(TELEFONO, ""); }
    public String getDepartamento() { return prefs.getString(DEPARTAMENTO, ""); }
    public String getProvincia() { return prefs.getString(PROVINCIA, ""); }
    public String getDistrito() { return prefs.getString(DISTRITO, ""); }
    public void cerrarSesion() {
        prefs.edit().clear().apply();
    }

    private static final String CORREO = "correo";
    private static final String TELEFONO = "telefono";
    private static final String DEPARTAMENTO = "departamento";
    private static final String PROVINCIA = "provincia";
    private static final String DISTRITO = "distrito";
}