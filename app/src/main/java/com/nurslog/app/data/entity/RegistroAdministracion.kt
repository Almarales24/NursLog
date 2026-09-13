package com.nurslog.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ForeignKey
import androidx.room.Index

// Define la tabla "registro_administracion" para registrar cada administración de medicamento
@Entity(
    tableName = "registro_administracion",
    // Si se elimina el horario, se eliminan automáticamente sus registros
    foreignKeys = [
        ForeignKey(
            entity = Horario::class,
            parentColumns = ["id"],
            childColumns = ["horarioId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    // Índice para optimizar búsquedas por horarioId
    indices = [Index("horarioId")]
)
data class RegistroAdministracion(
    // Identificador único generado automáticamente
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    // Referencia al horario del medicamento
    val horarioId: Int,
    // Estado: "Pendiente", "Administrado" u "Omitido"
    val estado: String,
    // Hora real de administración en milisegundos (se completa al confirmar)
    val horaReal: Long? = null,
    // Nombre del enfermero que administró el medicamento
    val enfermero: String,
    // Observaciones o notas al momento de administrar
    val nota: String? = null
)