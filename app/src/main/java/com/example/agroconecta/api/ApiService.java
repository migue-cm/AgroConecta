package com.example.agroconecta.api;

import com.example.agroconecta.model.Producto;
import com.example.agroconecta.model.Publicacion;
import com.example.agroconecta.response.ApiResponse;
import com.example.agroconecta.model.LoginRequest;
import com.example.agroconecta.model.Usuario;

import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {

    @GET("productos")
    Call<ApiResponse<List<Producto>>> obtenerProductos();

    @GET("publicaciones")
    Call<ApiResponse<List<Publicacion>>> obtenerPublicaciones();

    @POST("auth/login")
    Call<ApiResponse<Usuario>> login(@Body LoginRequest request);


}
