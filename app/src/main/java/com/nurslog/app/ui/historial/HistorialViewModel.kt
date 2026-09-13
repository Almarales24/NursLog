package com.nurslog.app.ui.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nurslog.app.data.entity.Alergia
import com.nurslog.app.data.entity.Diagnostico
import com.nurslog.app.data.entity.NotaEnfermeria
import com.nurslog.app.data.remote.icd10.Icd10Resultado
import com.nurslog.app.data.repository.HistorialRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ViewModel que gestiona el historial clínico de un paciente específico
class HistorialViewModel(
    private val repository: HistorialRepository,
    private val pacienteId: Int
) : ViewModel() {

    // Lista de diagnósticos del paciente como flujo observable
    val diagnosticos: StateFlow<List<Diagnostico>> = repository.getDiagnosticos(pacienteId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Lista de alergias del paciente como flujo observable
    val alergias: StateFlow<List<Alergia>> = repository.getAlergias(pacienteId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Lista de notas de enfermería del paciente como flujo observable
    val notas: StateFlow<List<NotaEnfermeria>> = repository.getNotas(pacienteId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Resultados de búsqueda en ICD-10 (diagnósticos estandarizados)
    private val _icd10Resultados = MutableStateFlow<List<Icd10Resultado>>(emptyList())
    val icd10Resultados: StateFlow<List<Icd10Resultado>> = _icd10Resultados.asStateFlow()

    // Busca diagnósticos en la clasificación ICD-10 mediante la API
    fun buscarIcd10(termino: String) {
        viewModelScope.launch {
            _icd10Resultados.value = repository.buscarIcd10(termino)
        }
    }

    // Limpia los resultados de búsqueda ICD-10
    fun limpiarIcd10Resultados() {
        _icd10Resultados.value = emptyList()
    }

    // Agrega un nuevo diagnóstico al paciente
    fun agregarDiagnostico(descripcion: String, estado: String) {
        viewModelScope.launch {
            repository.insertDiagnostico(
                Diagnostico(pacienteId = pacienteId, descripcion = descripcion, estado = estado)
            )
        }
    }

    // Elimina un diagnóstico existente del paciente
    fun eliminarDiagnostico(diagnostico: Diagnostico) {
        viewModelScope.launch { repository.deleteDiagnostico(diagnostico) }
    }

    // Agrega una nueva alergia al paciente
    fun agregarAlergia(sustancia: String, severidad: String) {
        viewModelScope.launch {
            repository.insertAlergia(
                Alergia(pacienteId = pacienteId, sustancia = sustancia, severidad = severidad)
            )
        }
    }

    // Elimina una alergia existente del paciente
    fun eliminarAlergia(alergia: Alergia) {
        viewModelScope.launch { repository.deleteAlergia(alergia) }
    }

    // Agrega una nueva nota con autor y tipo (nota o recomendación)
    fun agregarNota(texto: String, autor: String, tipo: String = "Nota") {
        viewModelScope.launch {
            repository.insertNota(
                NotaEnfermeria(pacienteId = pacienteId, texto = texto, autor = autor, tipo = tipo)
            )
        }
    }

    // Elimina una nota existente del paciente
    fun eliminarNota(nota: NotaEnfermeria) {
        viewModelScope.launch { repository.deleteNota(nota) }
    }
}

// Factory que crea instancias de HistorialViewModel con inyección de dependencias
class HistorialViewModelFactory(
    private val repository: HistorialRepository,
    private val pacienteId: Int
) : ViewModelProvider.Factory {
    // Crea la instancia del ViewModel con el repositorio y paciente ID
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistorialViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistorialViewModel(repository, pacienteId) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}