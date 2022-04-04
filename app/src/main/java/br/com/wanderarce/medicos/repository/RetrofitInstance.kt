package br.com.wanderarce.medicos.repository

import br.com.wanderarce.medicos.interfaces.RetrofitService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

// Inicializando o Moshi- A nossa Converter Factory
private val moshi= Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

// MÉTODO 1class
class RetrofitInstance{
    companion object{
         val retrofit= Retrofit.Builder().baseUrl("https://app-vacinas.herokuapp.com/") // URL base
        .addConverterFactory(MoshiConverterFactory.create(moshi)) // Conversor de Jsonem objetos Kotlin
        .build()
        .create(RetrofitService::class.java)
    }
}