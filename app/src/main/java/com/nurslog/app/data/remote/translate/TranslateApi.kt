package com.nurslog.app.data.remote.translate

import retrofit2.http.GET
import retrofit2.http.Query

interface TranslateApi {
    // https://api.mymemory.translated.net/get?q={texto}&langpair=en|es
    @GET("get")
    suspend fun traducir(
        @Query("q") texto: String,
        @Query("langpair") parIdiomas: String = "en|es"
    ): TranslateResponse
}