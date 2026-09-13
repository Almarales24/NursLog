package com.nurslog.app.data.remote.openfda

// Estructura de respuesta de la API OpenFDA
data class OpenFdaResponse(
    // Lista de resultados encontrados en la búsqueda
    val results: List<OpenFdaResult>?
)

// Estructura de cada resultado individual de OpenFDA
data class OpenFdaResult(
    // Advertencias o contraindicaciones asociadas al medicamento
    val warnings: List<String>?,
    // Información de dosificación y administración recomendada
    val dosage_and_administration: List<String>?
)