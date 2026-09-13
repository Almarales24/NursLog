package com.nurslog.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

// Define la tabla "diagnostico" con relación a Paciente para mantener historial clínico
@Entity(
    tableName = "diagnostico",
    // Si se elimina el paciente, se eliminan automáticamente sus diagnósticos
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
data class Diagnostico(
    // Identificador único generado automáticamente
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Referencia al paciente con este diagnóstico
    val pacienteId: Int,
    // Descripción o código del diagnóstico clínico
    val descripcion: String,
    // Estado actual: "Activo", "Estable" o "Resuelto"
    val estado: String
)