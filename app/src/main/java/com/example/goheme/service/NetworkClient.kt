package com.example.goheme.service

import com.example.goheme.Util.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NetworkClient {

  fun retrofitBuilder(): PokeApiService {
val interceptor = HttpLoggingInterceptor()
    interceptor.level= HttpLoggingInterceptor.Level.BODY

    return Retrofit.Builder()
      .client(OkHttpClient.Builder().addInterceptor(interceptor).build())
      .baseUrl(BASE_URL)
      .addConverterFactory(GsonConverterFactory.create())
      .build()
      .create(PokeApiService::class.java)
  }
}