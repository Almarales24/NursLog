package com.nurslog.app.data.remote.translate

import retrofit2.http.GET
import retrofit2.http.Query

// Interfaz para acceder a la API de Traducción automática (MyMemory)
interface TranslateApi {
    // Búsqueda: https://api.mymemory.translated.net/get?q={texto}&langpair=en|es
    // Traduce texto desde inglés al español utilizando la API de MyMemory
    @GET("get")
    suspend fun traducir(
        // Texto a traducir
        @Query("q") texto: String,
        // Par de idiomas: inglés (en) a español (es)
        @Query("langpair") parIdiomas: String = "en|es"
    ): TranslateResponse
}