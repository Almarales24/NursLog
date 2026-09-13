package com.nurslog.app.ui.medicacion

import com.nurslog.app.data.entity.Horario

data class MedicacionUiItem(
    val horario: Horario,
    val medicamentoNombre: String,
    val dosis: String,
    val via: String,
    val hora: String,
    val estado: String, // "Pendiente" o "Administrado"
    val recomendacion: String? = null
) {
    val horarioId: Int get() = horario.id
}