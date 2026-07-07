package com.example.agroconecta.api;

import com.example.agroconecta.model.CalificacionRequest;
import com.example.agroconecta.model.Pedido;
import com.example.agroconecta.model.PedidoRequest;
import com.example.agroconecta.model.Producto;
import com.example.agroconecta.model.Publicacion;
import com.example.agroconecta.model.PublicacionRequest;
import com.example.agroconecta.model.RegisterRequest;
import com.example.agroconecta.response.ApiResponse;
import com.example.agroconecta.model.LoginRequest;
import com.example.agroconecta.model.Usuario;

import retrofit2.http.Body;
import retrofit2.http.POST;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {

    @GET("productos")
    Call<ApiResponse<List<Producto>>> obtenerProductos();

    @GET("publicaciones")
    Call<ApiResponse<List<Publicacion>>> obtenerPublicaciones();

    @POST("auth/login")
    Call<ApiResponse<Usuario>> login(@Body LoginRequest request);
    @POST("pedidos")
    Call<ApiResponse<Object>> crearPedido(@Body PedidoRequest request);
    @GET("pedidos")
    Call<ApiResponse<List<Pedido>>> obtenerPedidos(@Query("id") int idComprador);
    @POST("publicaciones")
    Call<ApiResponse<Object>> crearPublicacion(@Body PublicacionRequest request);
    @POST("auth/register")
    Call<ApiResponse<Object>> register(@Body RegisterRequest request);
    @POST("calificaciones")
    Call<ApiResponse<Object>> crearCalificacion(@Body CalificacionRequest request);

}
