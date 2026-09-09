package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.nurslog.app.data.entity.Horario
import kotlinx.coroutines.flow.Flow

@Dao
interface HorarioDao {

    @Insert
    suspend fun insert(horario: Horario): Long

    @Update
    suspend fun update(horario: Horario)

    @Delete
    suspend fun delete(horario: Horario)

    @Query("SELECT * FROM horario WHERE pacienteId = :pacienteId ORDER BY hora ASC")
    fun getHorariosByPaciente(pacienteId: Int): Flow<List<Horario>>
}