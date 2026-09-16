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

    suspend fun deleteDiagnostico(diagnostico: Diagnostico) =
        diagnosticoDao.delete(diagnostico)

    suspend fun insertAlergia(alergia: Alergia) =
        alergiaDao.insert(alergia)

    suspend fun deleteAlergia(alergia: Alergia) =
        alergiaDao.delete(alergia)

    suspend fun insertNota(nota: NotaEnfermeria) =
        notaEnfermeriaDao.insert(nota)

    suspend fun deleteNota(nota: NotaEnfermeria) =
        notaEnfermeriaDao.delete(nota)

    // HU-08: acceso a clasificación ICD-10 para estandarizar diagnósticos - Karol
    @Suppress("UNCHECKED_CAST")
    suspend fun buscarIcd10(termino: String): List<Icd10Resultado> {
        if (termino.isBlank()) return emptyList()

        val respuesta = RetrofitInstance.icd10Api.buscarDiagnosticos(terminos = termino)
        // Estructura: [total, [codes], null, [[code, name], ...]]
        val listaPares = respuesta.getOrNull(3) as? List<List<String>> ?: return emptyList()

        return listaPares.map { par ->
            val nombreTraducido = traducirAlEspanol(par.getOrElse(1) { "" })
            Icd10Resultado(codigo = par[0], nombre = nombreTraducido)
        }
    }

    // Causa, síntomas y manejo general de una alergia, vía MedlinePlus (NIH), traducido al español
    suspend fun buscarInfoAlergia(sustancia: String): String? {
        if (sustancia.isBlank()) return null
        return try {
            val xml = RetrofitInstance.medlinePlusApi.buscarInformacion(termino = "$sustancia allergy")
            val snippet = extraerSnippet(xml) ?: return null
            traducirAlEspanol(snippet)
        } catch (e: Exception) {
            null
        }
    }

    // Extrae el primer <content name="snippet">...</content> y limpia etiquetas HTML/resaltado
    private fun extraerSnippet(xml: String): String? {
        val regex = Regex("<content name=\"snippet\">(.*?)</content>", RegexOption.DOT_MATCHES_ALL)
        val coincidencia = regex.find(xml)?.groupValues?.get(1) ?: return null
        return coincidencia
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&hellip;", "…")
            .replace(Regex("<[^>]*>"), "") // quita etiquetas HTML ya decodificadas (ej. <span class="qt0">)
            .trim()
            .ifBlank { null }
    }

    // Traduce texto en inglés al español vía MyMemory Translation API
    private suspend fun traducirAlEspanol(texto: String): String {
        if (texto.isBlank()) return texto
        return try {
            val respuesta = RetrofitInstance.translateApi.traducir(texto = texto)
            respuesta.responseData?.translatedText ?: texto
        } catch (e: Exception) {
            texto
        }
    }
}