package com.nurslog.app.data.remote.openfda

import retrofit2.http.GET
import retrofit2.http.Query

// Interfaz para acceder a la API de drogas y medicamentos de la FDA
interface OpenFdaApi {
    // Búsqueda: https://api.fda.gov/drug/label.json?search=openfda.brand_name:"{nombre}"&limit=1
    // Obtiene etiqueta de información del medicamento (dosificación, advertencias, etc.)
    @GET("drug/label.json")
    suspend fun buscarEtiqueta(
        // Consulta SQL-like que especifica el campo y valor a buscar
        @Query("search") busqueda: String,
        // Limita el resultado a 1 registro
        @Query("limit") limite: Int = 1
    ): OpenFdaResponse
}