package com.nurslog.app.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.nurslog.app.data.entity.Paciente
import kotlinx.coroutines.flow.Flow

// Interfaz de acceso a datos para operaciones CRUD con la tabla paciente
@Dao
interface PacienteDao {

    // Inserta un nuevo paciente y devuelve su ID
    @Insert
    suspend fun insert(paciente: Paciente): Long

    // Actualiza los datos de un paciente existente
    @Update
    suspend fun update(paciente: Paciente)

    // Elimina un paciente de la base de datos
    @Delete
    suspend fun delete(paciente: Paciente)

    // Obtiene todos los pacientes ordenados alfabéticamente por nombre
    @Query("SELECT * FROM paciente ORDER BY nombre ASC")
    fun getAllPacientes(): Flow<List<Paciente>>

    // Obtiene un paciente específico por su ID como flujo observable
    @Query("SELECT * FROM paciente WHERE id = :pacienteId")
    fun getPacienteById(pacienteId: Int): Flow<Paciente?>

    // Obtiene un paciente específico por su ID (ejecución inmediata, no observable)
    @Query("SELECT * FROM paciente WHERE id = :pacienteId")
    suspend fun getPacienteByIdOnce(pacienteId: Int): Paciente?

    // Busca pacientes cuyo nombre coincida parcialmente con la consulta
    @Query("SELECT * FROM paciente WHERE nombre LIKE '%' || :query || '%'")
    fun searchPacientes(query: String): Flow<List<Paciente>>
}