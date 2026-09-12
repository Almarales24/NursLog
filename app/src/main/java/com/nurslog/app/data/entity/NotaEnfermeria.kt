package com.nurslog.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

// HU-01: notas de enfermería con trazabilidad completa - Paula Mendoza
// HU-04: recomendaciones del personal de salud dirigidas al paciente - Karen Nava
@Entity(
    tableName = "nota_enfermeria",
    foreignKeys = [
        ForeignKey(
            entity = Paciente::class,
            parentColumns = ["id"],
            childColumns = ["pacienteId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("pacienteId")]
)
data class NotaEnfermeria(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pacienteId: Int,
    val texto: String,
    val autor: String,
    val tipo: String = "Nota", // "Nota" o "Recomendacion"
    val fechaHora: Long = System.currentTimeMillis() // trazabilidad automática
)