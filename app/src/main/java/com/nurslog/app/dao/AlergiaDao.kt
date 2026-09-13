package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.nurslog.app.data.entity.Alergia
import kotlinx.coroutines.flow.Flow

// Interfaz de acceso a datos para operaciones CRUD con la tabla alergia
@Dao
interface AlergiaDao {

    // Inserta una nueva alergia y devuelve su ID
    @Insert
    suspend fun insert(alergia: Alergia): Long

    // Actualiza los datos de una alergia existente
    @Update
    suspend fun update(alergia: Alergia)

    // Elimina una alergia de la base de datos
    @Delete
    suspend fun delete(alergia: Alergia)

    // Obtiene todas las alergias asociadas a un paciente específico
    @Query("SELECT * FROM alergia WHERE pacienteId = :pacienteId")
    fun getAlergiasByPaciente(pacienteId: Int): Flow<List<Alergia>>

    // Obtiene solo las alergias severas de un paciente (críticas para revisar)
    @Query("SELECT * FROM alergia WHERE pacienteId = :pacienteId AND severidad = 'Severa'")
    fun getAlergiasSeveras(pacienteId: Int): Flow<List<Alergia>>
}