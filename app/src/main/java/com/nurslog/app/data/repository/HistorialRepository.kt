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

/**
 * Repositorio encargado de gestionar la lógica de datos del historial clínico del paciente.
 * Combina el acceso a la base de datos local (Room) y servicios externos (Retrofit).
 */
class HistorialRepository(
    private val diagnosticoDao: DiagnosticoDao,
    private val alergiaDao: AlergiaDao,
    private val notaEnfermeriaDao: NotaEnfermeriaDao
) {
    /** Obtiene el flujo de diagnósticos registrados para un paciente. */
    fun getDiagnosticos(pacienteId: Int): Flow<List<Diagnostico>> =
        diagnosticoDao.getDiagnosticosByPaciente(pacienteId)

    /** Obtiene el flujo de alergias registradas para un paciente. */
    fun getAlergias(pacienteId: Int): Flow<List<Alergia>> =
        alergiaDao.getAlergiasByPaciente(pacienteId)

    /** Obtiene el flujo de notas de enfermería registradas para un paciente. */
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

    /**
     * Busca diagnósticos en la clasificación ICD-10 mediante el servicio clínico de la NLM.
     * HU-08: acceso a clasificación ICD-10 para estandarizar diagnósticos - Karol
     *
     * @param termino Palabra o código a buscar.
     * @return Lista de resultados mapeados a [Icd10Resultado] con nombres traducidos.
     */
    @Suppress("UNCHECKED_CAST")
    suspend fun buscarIcd10(termino: String): List<Icd10Resultado> {
        if (termino.isBlank()) return emptyList()

        val respuesta = RetrofitInstance.icd10Api.buscarDiagnosticos(terminos = termino)
        // Estructura de la API: [total, [codes], null, [[code, name], ...]]
        val listaPares = respuesta.getOrNull(3) as? List<List<String>> ?: return emptyList()

        return listaPares.map { par ->
            val nombreTraducido = traducirAlEspanol(par.getOrElse(1) { "" })
            Icd10Resultado(codigo = par[0], nombre = nombreTraducido)
        }
    }

    /**
     * Obtiene información detallada (causa, síntomas, manejo) de una alergia desde MedlinePlus.
     *
     * @param sustancia Nombre de la sustancia alérgena.
     * @return Texto descriptivo traducido al español o null si ocurre un error o no hay resultados.
     */
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

    /**
     * Parsea el XML de MedlinePlus para extraer el resumen ("snippet") de la información.
     * Realiza la limpieza de entidades XML y remoción de etiquetas HTML.
     */
    private fun extraerSnippet(xml: String): String? {
        val regex = Regex("<content name=\"snippet\">(.*?)</content>", RegexOption.DOT_MATCHES_ALL)
        val coincidencia = regex.find(xml)?.groupValues?.get(1) ?: return null
        return coincidencia
            .replace("&lt;", "<")
            .replace("&gt;", ">")
            .replace("&amp;", "&")
            .replace("&quot;", "\"")
            .replace("&hellip;", "…")
            .replace(Regex("<[^>]*>"), "") // quita etiquetas HTML (ej. <span class="qt0">)
            .trim()
            .ifBlank { null }
    }

    /**
     * Traduce un texto al español utilizando la API de MyMemory.
     * Si falla, retorna el texto original.
     */
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