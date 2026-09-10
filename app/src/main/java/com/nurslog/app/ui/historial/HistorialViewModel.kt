package com.nurslog.app.ui.historial

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.nurslog.app.data.entity.Alergia
import com.nurslog.app.data.entity.Diagnostico
import com.nurslog.app.data.entity.NotaEnfermeria
import com.nurslog.app.data.repository.HistorialRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    fun agregarNota(texto: String, autor: String) {
        viewModelScope.launch {
            repository.insertNota(
                NotaEnfermeria(pacienteId = pacienteId, texto = texto, autor = autor)
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