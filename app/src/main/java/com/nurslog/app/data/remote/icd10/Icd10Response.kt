package com.nurslog.app.data.remote.icd10

/**
 * La API de Clinical Tables (NLM) devuelve un array así:
 * [total, [codes], null, [[code, name], [code, name], ...]]
 * Se parsea manualmente en el repositorio, no con Gson directo.
 */
data class Icd10Resultado(
    val codigo: String,
    val nombre: String
)