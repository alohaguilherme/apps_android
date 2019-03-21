package com.example.requisicoeshttp.api;

import com.example.requisicoeshttp.model.Foto;
import com.example.requisicoeshttp.model.Postagens;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DataService {

    @GET("/photos")
    Call<List<Foto>> recuperarFotos();

    @GET("/posts")
    Call<List<Postagens>> recuperarPosts();

    @POST("/posts")
    Call<Postagens> savePostagem(@Body Postagens postagem);

    @FormUrlEncoded
    @POST("/posts")
    Call<Postagens> savePostagem(
            @Field("userId") String userId,
            @Field("title") String title,
            @Field("body") String body
    );

    @PUT("/posts/{id}")
    Call<Postagens> updatePost(@Path("id") int id, @Body Postagens postagem);

    @PATCH("/posts/{id}")
    Call<Postagens> updatePostPath(@Path("id") int id, @Body Postagens postagem);

    @DELETE("/posts/{id}")
    Call<Void> deletePostagem(@Path("id") int id);
}
