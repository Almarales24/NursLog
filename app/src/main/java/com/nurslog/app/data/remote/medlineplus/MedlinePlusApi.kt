package com.nurslog.app.data.remote.medlineplus

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Interfaz para interactuar con el servicio de búsqueda de MedlinePlus.
 * Permite obtener información sobre temas de salud, como alergias.
 */
interface MedlinePlusApi {
    /**
     * Busca información médica sobre un término específico.
     * La respuesta es en formato XML, por lo que se recibe como String para su procesamiento manual.
     *
     * @param baseDatos Base de datos a consultar (por defecto "healthTopics").
     * @param termino El término de búsqueda (ej: "{sustancia} allergy").
     * @return El XML completo de la respuesta como una cadena de texto.
     */
    @GET("ws/query")
    suspend fun buscarInformacion(
        @Query("db") baseDatos: String = "healthTopics",
        @Query("term") termino: String
    ): String
}