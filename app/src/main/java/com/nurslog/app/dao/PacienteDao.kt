package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.nurslog.app.data.entity.Paciente
import kotlinx.coroutines.flow.Flow

@Dao
interface PacienteDao {

    @Insert
    suspend fun insert(paciente: Paciente): Long

    @Update
    suspend fun update(paciente: Paciente)

    @Delete
    suspend fun delete(paciente: Paciente)

    @Query("SELECT * FROM paciente ORDER BY nombre ASC")
    fun getAllPacientes(): Flow<List<Paciente>>

    @Query("SELECT * FROM paciente WHERE id = :pacienteId")
    fun getPacienteById(pacienteId: Int): Flow<Paciente?>

    @Query("SELECT * FROM paciente WHERE nombre LIKE '%' || :query || '%'")
    fun searchPacientes(query: String): Flow<List<Paciente>>
}