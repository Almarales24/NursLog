package com.nurslog.app.data.remote.medlineplus

import retrofit2.http.GET
import retrofit2.http.Query

interface MedlinePlusApi {
    // https://wsearch.nlm.nih.gov/ws/query?db=healthTopics&term={sustancia} allergy
    // Responde en XML; se consume como texto plano y se parsea manualmente.
    @GET("ws/query")
    suspend fun buscarInformacion(
        @Query("db") baseDatos: String = "healthTopics",
        @Query("term") termino: String
    ): String
}