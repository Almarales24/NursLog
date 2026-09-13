package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.nurslog.app.data.entity.Medicamento
import kotlinx.coroutines.flow.Flow

// Interfaz de acceso a datos para operaciones CRUD con la tabla medicamento
@Dao
interface MedicamentoDao {

    // Inserta un nuevo medicamento y devuelve su ID
    @Insert
    suspend fun insert(medicamento: Medicamento): Long

    // Actualiza los datos de un medicamento existente
    @Update
    suspend fun update(medicamento: Medicamento)

    // Elimina un medicamento de la base de datos
    @Delete
    suspend fun delete(medicamento: Medicamento)

    // Obtiene todos los medicamentos ordenados alfabéticamente por nombre
    @Query("SELECT * FROM medicamento ORDER BY nombre ASC")
    fun getAllMedicamentos(): Flow<List<Medicamento>>

    // Obtiene un medicamento específico por su ID como flujo observable
    @Query("SELECT * FROM medicamento WHERE id = :id")
    fun getMedicamentoById(id: Int): Flow<Medicamento?>

    // Obtiene un medicamento específico por su ID (ejecución inmediata, no observable)
    @Query("SELECT * FROM medicamento WHERE id = :id")
    suspend fun getMedicamentoByIdOnce(id: Int): Medicamento?
}