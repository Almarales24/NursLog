package com.nurslog.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Define la tabla "paciente" en la base de datos
@Entity(tableName = "paciente")
data class Paciente(
    // Identificador único generado automáticamente
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Nombre completo del paciente
    val nombre: String,
    // Número de cama donde está ubicado
    val cama: String,
    // Sala o unidad del hospital
    val sala: String,
    // Edad del paciente en años
    val edad: Int
)