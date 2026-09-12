package com.nurslog.app.data.remote.openfda

import retrofit2.http.GET
import retrofit2.http.Query

interface OpenFdaApi {
    // https://api.fda.gov/drug/label.json?search=openfda.brand_name:"{nombre}"&limit=1
    @GET("drug/label.json")
    suspend fun buscarEtiqueta(
        @Query("search") busqueda: String,
        @Query("limit") limite: Int = 1
    ): OpenFdaResponse
}