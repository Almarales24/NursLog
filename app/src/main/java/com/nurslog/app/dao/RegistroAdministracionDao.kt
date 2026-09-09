package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.nurslog.app.data.entity.RegistroAdministracion
import kotlinx.coroutines.flow.Flow

@Dao
interface RegistroAdministracionDao {

    @Insert
    suspend fun insert(registro: RegistroAdministracion): Long

    @Update
    suspend fun update(registro: RegistroAdministracion)

    @Delete
    suspend fun delete(registro: RegistroAdministracion)

    @Query("SELECT * FROM registro_administracion WHERE horarioId = :horarioId ORDER BY horaReal DESC")
    fun getRegistrosByHorario(horarioId: Int): Flow<List<RegistroAdministracion>>
}