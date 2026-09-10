package com.nurslog.app.data.repository

import com.nurslog.app.dao.PacienteDao
import com.nurslog.app.data.entity.Paciente
import kotlinx.coroutines.flow.Flow

class PacienteRepository(private val pacienteDao: PacienteDao) {

    fun getAllPacientes(): Flow<List<Paciente>> = pacienteDao.getAllPacientes()

    fun getPacienteById(id: Int): Flow<Paciente?> = pacienteDao.getPacienteById(id)

    fun searchPacientes(query: String): Flow<List<Paciente>> = pacienteDao.searchPacientes(query)

    suspend fun insert(paciente: Paciente): Long = pacienteDao.insert(paciente)

    suspend fun update(paciente: Paciente) = pacienteDao.update(paciente)

    suspend fun delete(paciente: Paciente) = pacienteDao.delete(paciente)
}