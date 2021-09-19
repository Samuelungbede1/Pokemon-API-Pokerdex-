package com.example.goheme.service

import com.example.goheme.model.PokemonDataClass.Details
import com.example.goheme.model.PokemonRequest
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PokeApiService {

//    @GET("pokemon?limit=100&offset=0")
//    fun getProperties(): Call<PokemonRequest>

    @GET("pokemon")
    fun getProperties( @Query("limit")
                       limit: Int,
                       @Query("offset")
                       offset: Int): Call<PokemonRequest>


    @GET ("{name}")
    fun getPokemonDetails(@Path ("name")
                          pokemonId: String): Call<Details>

    //@GET ("{name}")
    fun getPokemonDetails(@Query("limit")limit: Int, @Query("offset")offset: Int): Call<Details>

//    fun getProperty( @Query("limit")limit: Int,@Query("offset")offset: Int):Call<PokemonDataClass>
}

// getPokemonDetails(1)     BASEURL/pokemon/1