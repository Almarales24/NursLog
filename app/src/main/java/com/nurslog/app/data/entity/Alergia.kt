package com.nurslog.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

/**
 * Representa una alergia registrada para un paciente.
 * HU-06: historial clínico vinculado para evitar administración innecesaria - Michelle Díaz
 *
 * @property id Identificador único autogenerado.
 * @property pacienteId ID del paciente al que pertenece la alergia.
 * @property sustancia Nombre de la sustancia o medicamento alérgeno.
 * @property severidad Nivel de gravedad: "Leve", "Moderada" o "Severa".
 * @property informacion Detalles adicionales como causa, síntomas o recomendaciones (provenientes de MedlinePlus).
 */
@Entity(
    tableName = "alergia",
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
data class Alergia(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val pacienteId: Int,
    val sustancia: String,
    val severidad: String,
    val informacion: String? = null
)