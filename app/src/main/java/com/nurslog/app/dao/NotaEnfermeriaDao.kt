package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.nurslog.app.data.entity.NotaEnfermeria
import kotlinx.coroutines.flow.Flow

@Dao
interface NotaEnfermeriaDao {

    @Insert
    suspend fun insert(nota: NotaEnfermeria): Long

    @Update
    suspend fun update(nota: NotaEnfermeria)

    @Delete
    suspend fun delete(nota: NotaEnfermeria)

    // Orden cronológico descendente: nota más reciente primero (trazabilidad)
    @Query("SELECT * FROM nota_enfermeria WHERE pacienteId = :pacienteId ORDER BY fechaHora DESC")
    fun getNotasByPaciente(pacienteId: Int): Flow<List<NotaEnfermeria>>
}