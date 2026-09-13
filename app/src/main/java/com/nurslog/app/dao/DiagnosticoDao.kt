package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.nurslog.app.data.entity.Diagnostico
import kotlinx.coroutines.flow.Flow

// Interfaz de acceso a datos para operaciones CRUD con la tabla diagnostico
@Dao
interface DiagnosticoDao {

    // Inserta un nuevo diagnóstico y devuelve su ID
    @Insert
    suspend fun insert(diagnostico: Diagnostico): Long

    // Actualiza los datos de un diagnóstico existente
    @Update
    suspend fun update(diagnostico: Diagnostico)

    // Elimina un diagnóstico de la base de datos
    @Delete
    suspend fun delete(diagnostico: Diagnostico)

    // Obtiene todos los diagnósticos asociados a un paciente específico
    @Query("SELECT * FROM diagnostico WHERE pacienteId = :pacienteId")
    fun getDiagnosticosByPaciente(pacienteId: Int): Flow<List<Diagnostico>>

    // Obtiene solo los diagnósticos activos de un paciente (en tratamiento)
    @Query("SELECT * FROM diagnostico WHERE pacienteId = :pacienteId AND estado = 'Activo'")
    fun getDiagnosticosActivos(pacienteId: Int): Flow<List<Diagnostico>>
}