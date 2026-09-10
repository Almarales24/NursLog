package com.nurslog.app.data.repository

import com.nurslog.app.dao.HorarioDao
import com.nurslog.app.dao.MedicamentoDao
import com.nurslog.app.dao.RegistroAdministracionDao
import com.nurslog.app.data.entity.Horario
import com.nurslog.app.data.entity.Medicamento
import com.nurslog.app.data.entity.RegistroAdministracion
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