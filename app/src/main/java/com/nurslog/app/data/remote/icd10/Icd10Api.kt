package com.nurslog.app.data.remote.icd10

import retrofit2.http.GET
import retrofit2.http.Query

interface Icd10Api {
    // https://clinicaltables.nlm.nih.gov/api/icd10cm/v3/search?sf=code,name&terms={texto}
    @GET("api/icd10cm/v3/search")
    suspend fun buscarDiagnosticos(
        @Query("sf") camposBusqueda: String = "code,name",
        @Query("terms") terminos: String
    ): List<Any>
}