package com.nurslog.app.ui.medicacion

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nurslog.app.data.repository.MedicacionRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
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
                    flowOf(emptyList())
                } else {
                    combine(
                        flowOf(horarios),
                        flowOf(medicamentos),
                        repository.getRegistrosByHorarios(horarioIds)
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
                                estado = if (administrado) "Administrado" else "Pendiente",
                                recomendacion = medicamento?.recomendacion
                            )
                        }
                    }
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Recomendación buscada en OpenFDA durante la creación del medicamento
    private val _recomendacionBuscada = MutableStateFlow<String?>(null)
    val recomendacionBuscada: StateFlow<String?> = _recomendacionBuscada.asStateFlow()

    fun buscarRecomendacion(nombreMedicamento: String) {
        viewModelScope.launch {
            _recomendacionBuscada.value = repository.buscarRecomendacionOpenFda(nombreMedicamento)
        }
    }

    fun limpiarRecomendacionBuscada() {
        _recomendacionBuscada.value = null
    }

    // Normalización de nombre de medicamento vía RxNorm
    private val _rxNormResultados = MutableStateFlow<List<String>>(emptyList())
    val rxNormResultados: StateFlow<List<String>> = _rxNormResultados.asStateFlow()

    fun buscarRxNorm(nombre: String) {
        viewModelScope.launch {
            _rxNormResultados.value = repository.buscarRxNorm(nombre)
        }
    }

    fun limpiarRxNormResultados() {
        _rxNormResultados.value = emptyList()
    }

    fun crearMedicamento(nombre: String, dosis: String, via: String, recomendacion: String?, hora: String) {
        viewModelScope.launch {
            repository.crearMedicamentoConHorario(
                nombre = nombre,
                dosis = dosis,
                via = via,
                recomendacion = recomendacion,
                hora = hora,
                pacienteId = pacienteId
            )
        }
    }

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