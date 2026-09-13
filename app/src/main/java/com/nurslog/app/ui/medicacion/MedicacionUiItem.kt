package com.nurslog.app.ui.medicacion

import com.nurslog.app.data.entity.Horario

// Clase de modelo para la UI que agrupa la información del medicamento con su estado de administración
data class MedicacionUiItem(
    // Horario de administración vinculado
    val horario: Horario,
    // Nombre del medicamento
    val medicamentoNombre: String,
    // Dosis del medicamento
    val dosis: String,
    // Vía de administración (VO, IV, IM, SC)
    val via: String,
    // Hora programada en formato "HH:mm"
    val hora: String,
    // Estado actual: "Pendiente" o "Administrado"
    val estado: String,
    // Recomendación o advertencia de uso (opcional)
    val recomendacion: String? = null
) {
    // Propiedad computed que devuelve el ID del horario
    val horarioId: Int get() = horario.id
}