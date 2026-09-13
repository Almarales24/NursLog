package com.nurslog.app.data.repository

import com.nurslog.app.dao.PacienteDao
import com.nurslog.app.data.entity.Paciente
import kotlinx.coroutines.flow.Flow

// Capa de repositorio que abstrae las operaciones de pacientes desde la BD
class PacienteRepository(private val pacienteDao: PacienteDao) {

    // Obtiene todos los pacientes disponibles como flujo observable
    fun getAllPacientes(): Flow<List<Paciente>> = pacienteDao.getAllPacientes()

    // Obtiene un paciente específico por ID como flujo observable
    fun getPacienteById(id: Int): Flow<Paciente?> = pacienteDao.getPacienteById(id)

    // Busca pacientes que coincidan con una consulta de texto
    fun searchPacientes(query: String): Flow<List<Paciente>> = pacienteDao.searchPacientes(query)

    // Inserta un nuevo paciente y devuelve su ID generado
    suspend fun insert(paciente: Paciente): Long = pacienteDao.insert(paciente)

    // Actualiza los datos de un paciente existente
    suspend fun update(paciente: Paciente) = pacienteDao.update(paciente)

    // Elimina un paciente de la base de datos
    suspend fun delete(paciente: Paciente) = pacienteDao.delete(paciente)
}