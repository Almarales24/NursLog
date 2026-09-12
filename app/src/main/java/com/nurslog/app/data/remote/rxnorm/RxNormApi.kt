package com.nurslog.app.data.remote.rxnorm

import retrofit2.http.GET
import retrofit2.http.Query

interface RxNormApi {
    // https://rxnav.nlm.nih.gov/REST/drugs.json?name={nombre}
    @GET("REST/drugs.json")
    suspend fun buscarMedicamentos(@Query("name") nombre: String): RxNormDrugsResponse
}