package com.nurslog.app.data.remote.icd10

// Clase auxiliar que representa un resultado de búsqueda ICD-10
// La API de Clinical Tables devuelve un array: [total, [codes], null, [[code, name], [code, name], ...]]
// Se parsea manualmente en el repositorio, no con Gson directo
data class Icd10Resultado(
    // Código ICD-10 del diagnóstico (ej: "A00")
    val codigo: String,
    // Descripción del diagnóstico (traducida al español)
    val nombre: String
)