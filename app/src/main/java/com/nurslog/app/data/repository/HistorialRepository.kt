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

// Capa de repositorio que maneja el historial clínico de pacientes
class HistorialRepository(
    private val diagnosticoDao: DiagnosticoDao,
    private val alergiaDao: AlergiaDao,
    private val notaEnfermeriaDao: NotaEnfermeriaDao
) {
    // Obtiene todos los diagnósticos de un paciente como flujo observable
    fun getDiagnosticos(pacienteId: Int): Flow<List<Diagnostico>> =
        diagnosticoDao.getDiagnosticosByPaciente(pacienteId)

    // Obtiene todas las alergias de un paciente como flujo observable
    fun getAlergias(pacienteId: Int): Flow<List<Alergia>> =
        alergiaDao.getAlergiasByPaciente(pacienteId)

    // Obtiene todas las notas de un paciente como flujo observable
    fun getNotas(pacienteId: Int): Flow<List<NotaEnfermeria>> =
        notaEnfermeriaDao.getNotasByPaciente(pacienteId)

    // Inserta un nuevo diagnóstico
    suspend fun insertDiagnostico(diagnostico: Diagnostico) =
        diagnosticoDao.insert(diagnostico)

    // Elimina un diagnóstico existente
    suspend fun deleteDiagnostico(diagnostico: Diagnostico) =
        diagnosticoDao.delete(diagnostico)

    // Inserta una nueva alergia
    suspend fun insertAlergia(alergia: Alergia) =
        alergiaDao.insert(alergia)

    // Elimina una alergia existente
    suspend fun deleteAlergia(alergia: Alergia) =
        alergiaDao.delete(alergia)

    // Inserta una nueva nota de enfermería
    suspend fun insertNota(nota: NotaEnfermeria) =
        notaEnfermeriaDao.insert(nota)

    // Elimina una nota de enfermería existente
    suspend fun deleteNota(nota: NotaEnfermeria) =
        notaEnfermeriaDao.delete(nota)

    // Busca diagnósticos en la clasificación ICD-10 de la API externa
    @Suppress("UNCHECKED_CAST")
    suspend fun buscarIcd10(termino: String): List<Icd10Resultado> {
        if (termino.isBlank()) return emptyList()

        // Consulta la API de Clinical Tables de NIH
        val respuesta = RetrofitInstance.icd10Api.buscarDiagnosticos(terminos = termino)
        // La respuesta tiene estructura: [total, [codes], null, [[code, name], ...]]
        val listaPares = respuesta.getOrNull(3) as? List<List<String>> ?: return emptyList()

        // Mapea los resultados: traduce los nombres del inglés al español
        return listaPares.map { par ->
            val nombreTraducido = traducirAlEspanol(par.getOrElse(1) { "" })
            Icd10Resultado(codigo = par[0], nombre = nombreTraducido)
        }
    }

    // Traduce texto en inglés al español usando la API de MyMemory Translation
    private suspend fun traducirAlEspanol(texto: String): String {
        if (texto.isBlank()) return texto
        return try {
            val respuesta = RetrofitInstance.translateApi.traducir(texto = texto)
            respuesta.responseData?.translatedText ?: texto
        } catch (e: Exception) {
            // Si la traducción falla, devuelve el texto original
            texto
        }
    }
}