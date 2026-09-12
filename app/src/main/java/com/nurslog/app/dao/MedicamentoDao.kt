package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.nurslog.app.data.entity.Medicamento
import kotlinx.coroutines.flow.Flow

@Dao
interface MedicamentoDao {

    @Insert
    suspend fun insert(medicamento: Medicamento): Long

    @Update
    suspend fun update(medicamento: Medicamento)

    @Delete
    suspend fun delete(medicamento: Medicamento)

    @Query("SELECT * FROM medicamento ORDER BY nombre ASC")
    fun getAllMedicamentos(): Flow<List<Medicamento>>

    @Query("SELECT * FROM medicamento WHERE id = :id")
    fun getMedicamentoById(id: Int): Flow<Medicamento?>

    @Query("SELECT * FROM medicamento WHERE id = :id")
    suspend fun getMedicamentoByIdOnce(id: Int): Medicamento?
}