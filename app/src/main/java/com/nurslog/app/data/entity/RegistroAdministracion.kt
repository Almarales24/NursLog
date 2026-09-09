package com.nurslog.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

// HU-05: nota de enfermería asociada a cada registro de medicación - Karol Leon
@Entity(
    tableName = "registro_administracion",
    foreignKeys = [
        ForeignKey(
            entity = Horario::class,
            parentColumns = ["id"],
            childColumns = ["horarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("horarioId")]
)
data class RegistroAdministracion(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val horarioId: Int,
    val estado: String, // "Pendiente", "Administrado", "Omitido"
    val horaReal: Long? = null, // se completa al confirmar administración
    val enfermero: String,
    val nota: String? = null // observaciones al momento de administrar - HU-05
)