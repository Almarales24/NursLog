package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.nurslog.app.data.entity.NotaEnfermeria
import kotlinx.coroutines.flow.Flow

// Interfaz de acceso a datos para operaciones CRUD con la tabla nota_enfermeria
@Dao
interface NotaEnfermeriaDao {

    // Inserta una nueva nota y devuelve su ID
    @Insert
    suspend fun insert(nota: NotaEnfermeria): Long

    // Actualiza los datos de una nota existente
    @Update
    suspend fun update(nota: NotaEnfermeria)

    // Elimina una nota de la base de datos
    @Delete
    suspend fun delete(nota: NotaEnfermeria)

    // Obtiene todas las notas de un paciente ordenadas por fecha descendente (más recientes primero)
    @Query("SELECT * FROM nota_enfermeria WHERE pacienteId = :pacienteId ORDER BY fechaHora DESC")
    fun getNotasByPaciente(pacienteId: Int): Flow<List<NotaEnfermeria>>
}