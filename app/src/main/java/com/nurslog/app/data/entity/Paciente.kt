package com.nurslog.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entidad base del sistema. Todas las demás entidades (Medicamento, Diagnostico,
 * Alergia, NotaEnfermeria, etc.) se relacionan con Paciente mediante pacienteId.
 */
@Entity(tableName = "paciente")
data class Paciente(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val cama: String,
    val sala: String,
    val edad: Int
)