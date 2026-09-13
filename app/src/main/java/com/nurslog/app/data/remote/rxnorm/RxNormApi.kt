package com.nurslog.app.data.remote.rxnorm

import retrofit2.http.GET
import retrofit2.http.Query

// Interfaz para acceder a la API de medicamentos normalizados RxNorm (NIH)
interface RxNormApi {
    // Búsqueda: https://rxnav.nlm.nih.gov/REST/drugs.json?name={nombre}
    // Obtiene medicamentos que coincidan con el nombre para normalizar nomenclatura
    @GET("REST/drugs.json")
    suspend fun buscarMedicamentos(
        // Nombre del medicamento a buscar
        @Query("name") nombre: String
    ): RxNormDrugsResponse
}