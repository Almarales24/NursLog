package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.nurslog.app.data.entity.Alergia
import kotlinx.coroutines.flow.Flow

@Dao
interface AlergiaDao {

    @Insert
    suspend fun insert(alergia: Alergia): Long

    @Update
    suspend fun update(alergia: Alergia)

    @Delete
    suspend fun delete(alergia: Alergia)

    @Query("SELECT * FROM alergia WHERE pacienteId = :pacienteId")
    fun getAlergiasByPaciente(pacienteId: Int): Flow<List<Alergia>>

    @Query("SELECT * FROM alergia WHERE pacienteId = :pacienteId AND severidad = 'Severa'")
    fun getAlergiasSeveras(pacienteId: Int): Flow<List<Alergia>>
}