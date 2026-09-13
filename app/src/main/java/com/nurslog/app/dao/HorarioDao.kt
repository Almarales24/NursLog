package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.nurslog.app.data.entity.Horario
import kotlinx.coroutines.flow.Flow

// Interfaz de acceso a datos para operaciones CRUD con la tabla horario
@Dao
interface HorarioDao {

    // Inserta un nuevo horario y devuelve su ID
    @Insert
    suspend fun insert(horario: Horario): Long

    // Actualiza los datos de un horario existente
    @Update
    suspend fun update(horario: Horario)

    // Elimina un horario de la base de datos
    @Delete
    suspend fun delete(horario: Horario)

    // Obtiene todos los horarios de un paciente ordenados por hora ascendente
    @Query("SELECT * FROM horario WHERE pacienteId = :pacienteId ORDER BY hora ASC")
    fun getHorariosByPaciente(pacienteId: Int): Flow<List<Horario>>

    // Obtiene todos los horarios sin filtros (ejecución inmediata)
    @Query("SELECT * FROM horario")
    suspend fun getAllHorariosOnce(): List<Horario>
}