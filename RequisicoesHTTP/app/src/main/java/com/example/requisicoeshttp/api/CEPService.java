package com.example.requisicoeshttp.api;

import com.example.requisicoeshttp.model.CEP;
import com.google.gson.Gson;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CEPService {

    @GET("{cep}/json/")
    Call<CEP> recuperarCEP( @Path("cep") String cep);





}
