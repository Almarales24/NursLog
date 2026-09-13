package com.nurslog.app.data.remote.rxnorm

// Estructura jerárquica de respuesta de la API RxNorm
data class RxNormDrugsResponse(
    // Grupo de drogas encontradas
    val drugGroup: RxNormDrugGroup?
)

// Contenedor del grupo de drogas con conceptos asociados
data class RxNormDrugGroup(
    // Lista de grupos de conceptos (diferentes formas del medicamento)
    val conceptGroup: List<RxNormConceptGroup>?
)

// Grupo de concepto que contiene propiedades del medicamento
data class RxNormConceptGroup(
    // Propiedades individuales de cada concepto (formulaciones, dosis, etc.)
    val conceptProperties: List<RxNormConceptProperty>?
)

// Propiedad específica de un concepto del medicamento
data class RxNormConceptProperty(
    // Identificador único del concepto RxCUI (RxNorm Concept Unique Identifier)
    val rxcui: String,
    // Nombre de la formulación o presentación del medicamento
    val name: String
)