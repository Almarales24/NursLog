package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Delete
import androidx.room.Query
import com.nurslog.app.data.entity.RegistroAdministracion
import kotlinx.coroutines.flow.Flow

// Interfaz de acceso a datos para operaciones CRUD con la tabla registro_administracion
@Dao
interface RegistroAdministracionDao {

    // Inserta un nuevo registro y devuelve su ID
    @Insert
    suspend fun insert(registro: RegistroAdministracion): Long

    // Actualiza los datos de un registro existente
    @Update
    suspend fun update(registro: RegistroAdministracion)

    // Elimina un registro de la base de datos
    @Delete
    suspend fun delete(registro: RegistroAdministracion)

    // Obtiene todos los registros de un horario ordenados por hora real descendente
    @Query("SELECT * FROM registro_administracion WHERE horarioId = :horarioId ORDER BY horaReal DESC")
    fun getRegistrosByHorario(horarioId: Int): Flow<List<RegistroAdministracion>>

    // Obtiene registros para múltiples horarios a la vez
    @Query("SELECT * FROM registro_administracion WHERE horarioId IN (:horarioIds)")
    fun getRegistrosByHorarios(horarioIds: List<Int>): Flow<List<RegistroAdministracion>>

    // Obtiene el registro más reciente de un horario (ejecución inmediata)
    @Query("SELECT * FROM registro_administracion WHERE horarioId = :horarioId ORDER BY horaReal DESC LIMIT 1")
    suspend fun getUltimoRegistroOnce(horarioId: Int): RegistroAdministracion?
}