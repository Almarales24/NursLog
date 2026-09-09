package com.nurslog.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

// HU-06: historial clínico vinculado para evitar administración innecesaria - Michelle Díaz
@Entity(
    tableName = "diagnostico",
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
data class Diagnostico(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pacienteId: Int,
    val descripcion: String,
    val estado: String // "Activo", "Estable", "Resuelto"
)