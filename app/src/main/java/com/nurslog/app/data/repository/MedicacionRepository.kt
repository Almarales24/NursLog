package com.nurslog.app.data.repository

import com.nurslog.app.dao.HorarioDao
import com.nurslog.app.dao.MedicamentoDao
import com.nurslog.app.dao.RegistroAdministracionDao
import com.nurslog.app.data.entity.Horario
import com.nurslog.app.data.entity.Medicamento
import com.nurslog.app.data.entity.RegistroAdministracion
import com.nurslog.app.data.remote.RetrofitInstance
import kotlinx.coroutines.flow.Flow

class MedicacionRepository(
    private val medicamentoDao: MedicamentoDao,
    private val horarioDao: HorarioDao,
    private val registroAdministracionDao: RegistroAdministracionDao
) {
    fun getMedicamentos(): Flow<List<Medicamento>> = medicamentoDao.getAllMedicamentos()

    fun getHorariosByPaciente(pacienteId: Int): Flow<List<Horario>> =
        horarioDao.getHorariosByPaciente(pacienteId)

    fun getRegistrosByHorario(horarioId: Int): Flow<List<RegistroAdministracion>> =
        registroAdministracionDao.getRegistrosByHorario(horarioId)

    fun getRegistrosByHorarios(horarioIds: List<Int>): Flow<List<RegistroAdministracion>> =
        registroAdministracionDao.getRegistrosByHorarios(horarioIds)

    suspend fun insertMedicamento(medicamento: Medicamento): Long =
        medicamentoDao.insert(medicamento)

    suspend fun insertHorario(horario: Horario): Long =
        horarioDao.insert(horario)

    // Crea el medicamento y su horario en un solo paso desde el formulario
    suspend fun crearMedicamentoConHorario(
        nombre: String,
        dosis: String,
        via: String,
        recomendacion: String?,
        hora: String,
        pacienteId: Int
    ) {
        val medicamentoId = medicamentoDao.insert(
            Medicamento(nombre = nombre, dosis = dosis, via = via, recomendacion = recomendacion)
        ).toInt()
        horarioDao.insert(
            Horario(medicamentoId = medicamentoId, pacienteId = pacienteId, hora = hora)
        )
    }

    // HU: normalización de nombres de medicamentos vía RxNorm
    suspend fun buscarRxNorm(nombre: String): List<String> {
        if (nombre.isBlank()) return emptyList()
        return try {
            val respuesta = RetrofitInstance.rxNormApi.buscarMedicamentos(nombre)
            respuesta.drugGroup?.conceptGroup
                ?.flatMap { it.conceptProperties ?: emptyList() }
                ?.map { it.name }
                ?.distinct()
                ?.take(8)
                ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }

    // Recomendación/advertencia de uso según hora y frecuencia consumida, vía OpenFDA
    suspend fun buscarRecomendacionOpenFda(nombreMedicamento: String): String? {
        if (nombreMedicamento.isBlank()) return null

        val camposBusqueda = listOf(
            "openfda.brand_name:\"$nombreMedicamento\"",
            "openfda.generic_name:\"$nombreMedicamento\"",
            "openfda.substance_name:\"$nombreMedicamento\""
        )

        for (busqueda in camposBusqueda) {
            try {
                val respuesta = RetrofitInstance.openFdaApi.buscarEtiqueta(busqueda = busqueda)
                val resultado = respuesta.results?.firstOrNull()
                if (resultado != null) {
                    val dosificacion = resultado.dosage_and_administration?.firstOrNull()
                    val texto = dosificacion?.take(200)
                    if (!texto.isNullOrBlank()) return traducirAlEspanol(texto)
                }
            } catch (e: Exception) {
                // intenta el siguiente campo de búsqueda
            }
        }
        return null
    }

    // Traduce texto en inglés (OpenFDA) al español vía MyMemory Translation API
    private suspend fun traducirAlEspanol(texto: String): String {
        return try {
            val respuesta = RetrofitInstance.translateApi.traducir(texto = texto)
            respuesta.responseData?.translatedText ?: texto
        } catch (e: Exception) {
            texto // si falla la traducción, se muestra el texto original en inglés
        }
    }

    // HU-10: registro simple tras validar checklist de 5 correctos
    suspend fun registrarAdministracion(
        horarioId: Int,
        enfermero: String,
        nota: String?
    ) {
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