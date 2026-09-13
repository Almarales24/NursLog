package com.nurslog.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Define la tabla "medicamento" en la base de datos
@Entity(tableName = "medicamento")
data class Medicamento(
    // Identificador único generado automáticamente
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Nombre comercial o genérico del medicamento
    val nombre: String,
    // Dosis del medicamento (ej: "500mg", "10ml")
    val dosis: String,
    // Vía de administración: VO (oral), IV (intravenosa), IM (intramuscular), SC (subcutánea)
    val via: String,
    // Advertencia o recomendación de uso obtenida de OpenFDA o manual
    val recomendacion: String? = null
)