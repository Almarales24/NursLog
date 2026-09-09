package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.nurslog.app.data.entity.Diagnostico
import kotlinx.coroutines.flow.Flow

@Dao
interface DiagnosticoDao {

    @Insert
    suspend fun insert(diagnostico: Diagnostico): Long

    @Update
    suspend fun update(diagnostico: Diagnostico)

    @Delete
    suspend fun delete(diagnostico: Diagnostico)

    @Query("SELECT * FROM diagnostico WHERE pacienteId = :pacienteId")
    fun getDiagnosticosByPaciente(pacienteId: Int): Flow<List<Diagnostico>>

    @Query("SELECT * FROM diagnostico WHERE pacienteId = :pacienteId AND estado = 'Activo'")
    fun getDiagnosticosActivos(pacienteId: Int): Flow<List<Diagnostico>>
}