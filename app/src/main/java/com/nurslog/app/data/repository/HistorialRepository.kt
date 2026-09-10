package com.nurslog.app.data.repository

import com.nurslog.app.data.remote.RetrofitInstance
import com.nurslog.app.data.remote.icd10.Icd10Resultado
import com.nurslog.app.dao.DiagnosticoDao
import com.nurslog.app.dao.AlergiaDao
import com.nurslog.app.dao.NotaEnfermeriaDao
import com.nurslog.app.data.entity.Diagnostico
import com.nurslog.app.data.entity.Alergia
import com.nurslog.app.data.entity.NotaEnfermeria
import kotlinx.coroutines.flow.Flow

class HistorialRepository(
    private val diagnosticoDao: DiagnosticoDao,
    private val alergiaDao: AlergiaDao,
    private val notaEnfermeriaDao: NotaEnfermeriaDao
) {
    fun getDiagnosticos(pacienteId: Int): Flow<List<Diagnostico>> =
        diagnosticoDao.getDiagnosticosByPaciente(pacienteId)

    fun getAlergias(pacienteId: Int): Flow<List<Alergia>> =
        alergiaDao.getAlergiasByPaciente(pacienteId)

    fun getNotas(pacienteId: Int): Flow<List<NotaEnfermeria>> =
        notaEnfermeriaDao.getNotasByPaciente(pacienteId)

    suspend fun insertDiagnostico(diagnostico: Diagnostico) =
        diagnosticoDao.insert(diagnostico)

    suspend fun insertAlergia(alergia: Alergia) =
        alergiaDao.insert(alergia)

    suspend fun insertNota(nota: NotaEnfermeria) =
        notaEnfermeriaDao.insert(nota)

    // HU-08: acceso a clasificación ICD-10 para estandarizar diagnósticos - Karol
    @Suppress("UNCHECKED_CAST")
    suspend fun buscarIcd10(termino: String): List<Icd10Resultado> {
        if (termino.isBlank()) return emptyList()

        val respuesta = RetrofitInstance.icd10Api.buscarDiagnosticos(terminos = termino)
        // Estructura: [total, [codes], null, [[code, name], ...]]
        val listaPares = respuesta.getOrNull(3) as? List<List<String>> ?: return emptyList()

        return listaPares.map { par ->
            Icd10Resultado(codigo = par[0], nombre = par.getOrElse(1) { "" })
        }
    }
}