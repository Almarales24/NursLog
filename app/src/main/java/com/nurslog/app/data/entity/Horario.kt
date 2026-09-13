package com.nurslog.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

// Define la tabla "horario" para vincular medicamentos con pacientes y sus horarios de administración
@Entity(
    tableName = "horario",
    // Relaciones con Medicamento y Paciente para eliminar en cascada
    foreignKeys = [
        ForeignKey(
            entity = Medicamento::class,
            parentColumns = ["id"],
            childColumns = ["medicamentoId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Paciente::class,
            parentColumns = ["id"],
            childColumns = ["pacienteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    // Índices para optimizar búsquedas frecuentes
    indices = [Index("medicamentoId"), Index("pacienteId")]
)
data class Horario(
    // Identificador único generado automáticamente
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Referencia al medicamento que se debe administrar
    val medicamentoId: Int,
    // Referencia al paciente que lo recibe
    val pacienteId: Int,
    // Hora programada en formato "HH:mm" (ej: "08:00")
    val hora: String
)