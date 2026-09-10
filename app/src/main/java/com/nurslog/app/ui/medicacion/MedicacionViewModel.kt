package com.nurslog.app.ui.medicacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nurslog.app.data.repository.MedicacionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class MedicacionViewModel(
    private val repository: MedicacionRepository,
    private val pacienteId: Int
) : ViewModel() {

    val medicacionItems: StateFlow<List<MedicacionUiItem>> =
        combine(
            repository.getHorariosByPaciente(pacienteId),
            repository.getMedicamentos()
        ) { horarios, medicamentos -> horarios to medicamentos }
            .flatMapLatest { (horarios, medicamentos) ->
                val horarioIds = horarios.map { it.id }
                if (horarioIds.isEmpty()) {
                    kotlinx.coroutines.flow.flowOf(emptyList())
                } else {
                    repository.getRegistrosByHorarios(horarioIds).let { registrosFlow ->
                        kotlinx.coroutines.flow.combine(
                            kotlinx.coroutines.flow.flowOf(horarios),
                            kotlinx.coroutines.flow.flowOf(medicamentos),
                            registrosFlow
                        ) { h, m, registros ->
                            h.map { horario ->
                                val medicamento = m.find { it.id == horario.medicamentoId }
                                val administrado = registros.any {
                                    it.horarioId == horario.id && it.estado == "Administrado"
                                }
                                MedicacionUiItem(
                                    horarioId = horario.id,
                                    medicamentoNombre = medicamento?.nombre ?: "Desconocido",
                                    dosis = medicamento?.dosis ?: "",
                                    via = medicamento?.via ?: "",
                                    hora = horario.hora,
                                    estado = if (administrado) "Administrado" else "Pendiente"
                                )
                            }
                        }
                    }
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // HU-10: registro simple tras confirmar checklist de 5 correctos, sin pasos extra
    fun confirmarAdministracion(horarioId: Int, enfermero: String, nota: String?) {
        viewModelScope.launch {
            repository.registrarAdministracion(horarioId, enfermero, nota)
        }
    }
}

class MedicacionViewModelFactory(
    private val repository: MedicacionRepository,
    private val pacienteId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicacionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicacionViewModel(repository, pacienteId) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}