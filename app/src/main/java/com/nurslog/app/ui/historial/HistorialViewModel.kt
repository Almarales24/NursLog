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

class HistorialViewModel(
    private val repository: HistorialRepository,
    private val pacienteId: Int
) : ViewModel() {

    val diagnosticos: StateFlow<List<Diagnostico>> = repository.getDiagnosticos(pacienteId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val alergias: StateFlow<List<Alergia>> = repository.getAlergias(pacienteId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notas: StateFlow<List<NotaEnfermeria>> = repository.getNotas(pacienteId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // HU-08: resultados de busqueda ICD-10 - Karol
    private val _icd10Resultados = MutableStateFlow<List<Icd10Resultado>>(emptyList())
    val icd10Resultados: StateFlow<List<Icd10Resultado>> = _icd10Resultados.asStateFlow()

    fun buscarIcd10(termino: String) {
        viewModelScope.launch {
            _icd10Resultados.value = repository.buscarIcd10(termino)
        }
    }

    fun limpiarIcd10Resultados() {
        _icd10Resultados.value = emptyList()
    }

    fun agregarDiagnostico(descripcion: String, estado: String) {
        viewModelScope.launch {
            repository.insertDiagnostico(
                Diagnostico(pacienteId = pacienteId, descripcion = descripcion, estado = estado)
            )
        }
    }

    fun agregarAlergia(sustancia: String, severidad: String) {
        viewModelScope.launch {
            repository.insertAlergia(
                Alergia(pacienteId = pacienteId, sustancia = sustancia, severidad = severidad)
            )
        }
    }

    // HU-01: nota con trazabilidad (autor + fechaHora automática) - Paula Mendoza
    // HU-04: distingue nota general de recomendación del personal - Karen Nava
    fun agregarNota(texto: String, autor: String, tipo: String = "Nota") {
        viewModelScope.launch {
            repository.insertNota(
                NotaEnfermeria(pacienteId = pacienteId, texto = texto, autor = autor, tipo = tipo)
            )
        }
    }
}

class HistorialViewModelFactory(
    private val repository: HistorialRepository,
    private val pacienteId: Int
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HistorialViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HistorialViewModel(repository, pacienteId) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}