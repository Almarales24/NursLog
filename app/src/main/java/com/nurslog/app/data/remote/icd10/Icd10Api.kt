package com.nurslog.app.data.remote.icd10

import retrofit2.http.GET
import retrofit2.http.Query

// Interfaz para acceder a la API de Clasificación de Diagnósticos ICD-10 (NIH)
interface Icd10Api {
    // Búsqueda: https://clinicaltables.nlm.nih.gov/api/icd10cm/v3/search?sf=code,name&terms={texto}
    // Obtiene códigos y descripciones de diagnósticos que coincidan con el término
    @GET("api/icd10cm/v3/search")
    suspend fun buscarDiagnosticos(
        // Especifica qué campos retornar: código e nombre
        @Query("sf") camposBusqueda: String = "code,name",
        // Términos a buscar en la clasificación
        @Query("terms") terminos: String
    ): List<Any>
}