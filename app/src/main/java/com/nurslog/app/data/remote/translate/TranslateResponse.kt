package com.nurslog.app.data.remote.translate

// Estructura de respuesta de la API de MyMemory Translation
data class TranslateResponse(
    // Objeto con los datos de la traducción
    val responseData: TranslateResponseData?
)

// Datos específicos de la respuesta de traducción
data class TranslateResponseData(
    // Texto traducido resultante
    val translatedText: String?
)