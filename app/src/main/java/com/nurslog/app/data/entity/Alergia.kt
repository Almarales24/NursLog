package com.nurslog.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

// Define la tabla "alergia" con relación a Paciente para evitar administración innecesaria
@Entity(
    tableName = "alergia",
    // Si se elimina el paciente, se eliminan automáticamente sus alergias
    foreignKeys = [
        ForeignKey(
            entity = Paciente::class,
            parentColumns = ["id"],
            childColumns = ["pacienteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    // Índice para optimizar búsquedas por pacienteId
    indices = [Index("pacienteId")]
)
data class Alergia(
    // Identificador único generado automáticamente
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Referencia al paciente que tiene la alergia
    val pacienteId: Int,
    // Sustancia o medicamento alergénico
    val sustancia: String,
    // Nivel de reacción: "Leve", "Moderada" o "Severa"
    val severidad: String
)