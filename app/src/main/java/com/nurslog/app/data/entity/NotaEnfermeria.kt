package com.nurslog.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

// Define la tabla "nota_enfermeria" para registrar observaciones con trazabilidad completa
@Entity(
    tableName = "nota_enfermeria",
    // Si se elimina el paciente, se eliminan automáticamente sus notas
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
data class NotaEnfermeria(
    // Identificador único generado automáticamente
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Referencia al paciente de la nota
    val pacienteId: Int,
    // Contenido de la nota o recomendación
    val texto: String,
    // Nombre del enfermero que realizó la nota
    val autor: String,
    // Tipo de registro: "Nota" o "Recomendacion"
    val tipo: String = "Nota",
    // Marca de tiempo automática en milisegundos para trazabilidad
    val fechaHora: Long = System.currentTimeMillis()
)