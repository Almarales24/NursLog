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

// ViewModel que gestiona medicamentos, horarios y registros de administración de un paciente
@OptIn(ExperimentalCoroutinesApi::class)
class MedicacionViewModel(
    private val repository: MedicacionRepository,
    private val pacienteId: Int
) : ViewModel() {

    // Lista de medicaciones con estado actual (pendiente o administrado)
    val medicacionItems: StateFlow<List<MedicacionUiItem>> =
        combine(
            // Obtiene horarios del paciente y todos los medicamentos
            repository.getHorariosByPaciente(pacienteId),
            repository.getMedicamentos()
        ) { horarios, medicamentos -> horarios to medicamentos }
            // Cambia a un nuevo flujo con registros de administración
            .flatMapLatest { (horarios, medicamentos) ->
                val horarioIds = horarios.map { it.id }
                if (horarioIds.isEmpty()) {
                    // Si no hay horarios, devuelve lista vacía
                    flowOf(emptyList())
                } else {
                    // Combina horarios, medicamentos y registros
                    combine(
                        flowOf(horarios),
                        flowOf(medicamentos),
                        repository.getRegistrosByHorarios(horarioIds)
                    ) { h, m, registros ->
                        // Mapea cada horario a un elemento de UI con información completa
                        h.map { horario ->
                            val medicamento = m.find { it.id == horario.medicamentoId }
                            // Verifica si ya fue administrado hoy
                            val administrado = registros.any {
                                it.horarioId == horario.id && it.estado == "Administrado"
                            }
                            MedicacionUiItem(
                                horario = horario,
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
            // Convierte a StateFlow manteniendo el último valor
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Recomendación/advertencia buscada en OpenFDA durante la creación del medicamento
    private val _recomendacionBuscada = MutableStateFlow<String?>(null)
    val recomendacionBuscada: StateFlow<String?> = _recomendacionBuscada.asStateFlow()

    // Busca recomendaciones en OpenFDA para un medicamento específico
    fun buscarRecomendacion(nombreMedicamento: String) {
        viewModelScope.launch {
            _recomendacionBuscada.value = repository.buscarRecomendacionOpenFda(nombreMedicamento)
        }
    }

    // Limpia la recomendación buscada
    fun limpiarRecomendacionBuscada() {
        _recomendacionBuscada.value = null
    }

    // Resultados de normalización de nombre de medicamento vía RxNorm
    private val _rxNormResultados = MutableStateFlow<List<String>>(emptyList())
    val rxNormResultados: StateFlow<List<String>> = _rxNormResultados.asStateFlow()

    // Busca medicamentos normalizados en RxNorm para completar automáticamente
    fun buscarRxNorm(nombre: String) {
        viewModelScope.launch {
            _rxNormResultados.value = repository.buscarRxNorm(nombre)
        }
    }

    // Limpia los resultados de búsqueda RxNorm
    fun limpiarRxNormResultados() {
        _rxNormResultados.value = emptyList()
    }

    // Crea un nuevo medicamento con su horario de administración
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

    // Registra la administración de un medicamento tras validar el checklist
    fun confirmarAdministracion(horarioId: Int, enfermero: String, nota: String?) {
        viewModelScope.launch {
            repository.registrarAdministracion(horarioId, enfermero, nota)
        }
    }

    // Elimina un horario de medicamento (deja de administrarlo)
    fun eliminarHorario(item: MedicacionUiItem) {
        viewModelScope.launch {
            repository.deleteHorario(item.horario)
        }
    }
}

// Factory que crea instancias de MedicacionViewModel con inyección de dependencias
class MedicacionViewModelFactory(
    private val repository: MedicacionRepository,
    private val pacienteId: Int
) : ViewModelProvider.Factory {
    // Crea la instancia del ViewModel con el repositorio y paciente ID
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MedicacionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MedicacionViewModel(repository, pacienteId) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}