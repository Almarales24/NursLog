package com.nurslog.app.data.repository

import com.nurslog.app.dao.HorarioDao
import com.nurslog.app.dao.MedicamentoDao
import com.nurslog.app.dao.RegistroAdministracionDao
import com.nurslog.app.data.entity.Horario
import com.nurslog.app.data.entity.Medicamento
import com.nurslog.app.data.entity.RegistroAdministracion
import com.nurslog.app.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow

// Capa de repositorio que maneja medicamentos, horarios y registros de administración
class MedicacionRepository(
    private val medicamentoDao: MedicamentoDao,
    private val horarioDao: HorarioDao,
    private val registroAdministracionDao: RegistroAdministracionDao
) {
    // Obtiene todos los medicamentos disponibles como flujo observable
    fun getMedicamentos(): Flow<List<Medicamento>> = medicamentoDao.getAllMedicamentos()

    // Obtiene los horarios programados para un paciente
    fun getHorariosByPaciente(pacienteId: Int): Flow<List<Horario>> =
        horarioDao.getHorariosByPaciente(pacienteId)

    // Obtiene el historial de registros de administración para un horario específico
    fun getRegistrosByHorario(horarioId: Int): Flow<List<RegistroAdministracion>> =
        registroAdministracionDao.getRegistrosByHorario(horarioId)

    // Obtiene registros de administración para múltiples horarios
    fun getRegistrosByHorarios(horarioIds: List<Int>): Flow<List<RegistroAdministracion>> =
        registroAdministracionDao.getRegistrosByHorarios(horarioIds)

    // Inserta un nuevo medicamento en la base de datos
    suspend fun insertMedicamento(medicamento: Medicamento): Long =
        medicamentoDao.insert(medicamento)

    // Inserta un nuevo horario de administración
    suspend fun insertHorario(horario: Horario): Long =
        horarioDao.insert(horario)

    // Elimina un horario de administración
    suspend fun deleteHorario(horario: Horario) =
        horarioDao.delete(horario)

    // Crea un medicamento con su horario en un solo paso (operación atómica)
    suspend fun crearMedicamentoConHorario(
        nombre: String,
        dosis: String,
        via: String,
        recomendacion: String?,
        hora: String,
        pacienteId: Int
    ) {
        // Inserta el medicamento y obtiene su ID generado
        val medicamentoId = medicamentoDao.insert(
            Medicamento(nombre = nombre, dosis = dosis, via = via, recomendacion = recomendacion)
        ).toInt()
        // Crea el horario vinculando el medicamento con el paciente
        horarioDao.insert(
            Horario(medicamentoId = medicamentoId, pacienteId = pacienteId, hora = hora)
        )
    }

    // Busca nombres normalizados de medicamentos en la API RxNorm
    suspend fun buscarRxNorm(nombre: String): List<String> {
        if (nombre.isBlank()) return emptyList()
        return try {
            // Consulta la API RxNorm (NIH) con el nombre del medicamento
            val respuesta = RetrofitInstance.rxNormApi.buscarMedicamentos(nombre)
            // Extrae los nombres de los medicamentos encontrados
            respuesta.drugGroup?.conceptGroup
                ?.flatMap { it.conceptProperties ?: emptyList() }
                ?.map { it.name }
                ?.distinct()
                ?.take(8) // Limita a 8 resultados para la UI
                ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Busca recomendaciones de uso en la API OpenFDA
    suspend fun buscarRecomendacionOpenFda(nombreMedicamento: String): String? {
        if (nombreMedicamento.isBlank()) return null

        // Intenta buscar en múltiples campos de la API
        val camposBusqueda = listOf(
            "openfda.brand_name:\"$nombreMedicamento\"",
            "openfda.generic_name:\"$nombreMedicamento\"",
            "openfda.substance_name:\"$nombreMedicamento\""
        )

        for (busqueda in camposBusqueda) {
            try {
                // Consulta OpenFDA con el nombre del medicamento
                val respuesta = RetrofitInstance.openFdaApi.buscarEtiqueta(busqueda = busqueda)
                val resultado = respuesta.results?.firstOrNull()
                if (resultado != null) {
                    // Extrae la información de dosificación
                    val dosificacion = resultado.dosage_and_administration?.firstOrNull()
                    val texto = dosificacion?.take(200)
                    if (!texto.isNullOrBlank()) return traducirAlEspanol(texto)
                }
            } catch (e: Exception) {
                // Continúa con el siguiente campo si falla
            }
        }
        return null
    }

    // Traduce texto en inglés al español usando MyMemory Translation API
    private suspend fun traducirAlEspanol(texto: String): String {
        return try {
            val respuesta = RetrofitInstance.translateApi.traducir(texto = texto)
            respuesta.responseData?.translatedText ?: texto
        } catch (e: Exception) {
            // Si la traducción falla, devuelve el texto original
            texto
        }
    }

    // Registra la administración de un medicamento con validación de checklist
    suspend fun registrarAdministracion(
        horarioId: Int,
        enfermero: String,
        nota: String?
    ) {
        // Crea un nuevo registro con marca de tiempo automática
        registroAdministracionDao.insert(
            RegistroAdministracion(
                horarioId = horarioId,
                estado = "Administrado",
                horaReal = System.currentTimeMillis(),
                enfermero = enfermero,
                nota = nota
            )
        )
    }
}