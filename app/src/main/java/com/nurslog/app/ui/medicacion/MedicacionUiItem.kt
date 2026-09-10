package com.nurslog.app.ui.medicacion

data class MedicacionUiItem(
    val horarioId: Int,
    val medicamentoNombre: String,
    val dosis: String,
    val via: String,
    val hora: String,
    val estado: String // "Pendiente" o "Administrado"
)