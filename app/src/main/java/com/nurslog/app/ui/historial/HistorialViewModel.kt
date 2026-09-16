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

/**
 * ViewModel que gestiona la lógica de negocio para la pantalla de Historial Clínico.
 * Mantiene el estado de diagnósticos, alergias y notas, y facilita la interacción con el repositorio.
 *
 * @property repository Repositorio de datos del historial.
 * @property pacienteId Identificador del paciente actual.
 */
class HistorialViewModel(
    private val repository: HistorialRepository,
    private val pacienteId: Int
) : ViewModel() {

    /** Lista observable de diagnósticos del paciente. */
    val diagnosticos: StateFlow<List<Diagnostico>> = repository.getDiagnosticos(pacienteId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Lista observable de alergias del paciente. */
    val alergias: StateFlow<List<Alergia>> = repository.getAlergias(pacienteId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Lista observable de notas de enfermería del paciente. */
    val notas: StateFlow<List<NotaEnfermeria>> = repository.getNotas(pacienteId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    /** Resultados de búsqueda de códigos ICD-10. HU-08: Karol */
    private val _icd10Resultados = MutableStateFlow<List<Icd10Resultado>>(emptyList())
    val icd10Resultados: StateFlow<List<Icd10Resultado>> = _icd10Resultados.asStateFlow()

    /** Inicia la búsqueda de un diagnóstico en el servicio ICD-10. */
    fun buscarIcd10(termino: String) {
        viewModelScope.launch {
            _icd10Resultados.value = repository.buscarIcd10(termino)
        }
    }

    /** Limpia los resultados de la búsqueda ICD-10 actual. */
    fun limpiarIcd10Resultados() {
        _icd10Resultados.value = emptyList()
    }

    /** Información sugerida (causa/recomendación) obtenida para una alergia. */
    private val _infoAlergiaBuscada = MutableStateFlow<String?>(null)
    val infoAlergiaBuscada: StateFlow<String?> = _infoAlergiaBuscada.asStateFlow()

    /** Busca información sobre una alergia en MedlinePlus. */
    fun buscarInfoAlergia(sustancia: String) {
        viewModelScope.launch {
            _infoAlergiaBuscada.value = repository.buscarInfoAlergia(sustancia)
        }
    }

    /** Limpia la información sugerida de alergia. */
    fun limpiarInfoAlergiaBuscada() {
        _infoAlergiaBuscada.value = null
    }

    /** Registra un nuevo diagnóstico en la base de datos local. */
    fun agregarDiagnostico(descripcion: String, estado: String) {
        viewModelScope.launch {
            repository.insertDiagnostico(
                Diagnostico(pacienteId = pacienteId, descripcion = descripcion, estado = estado)
            )
        }
    }

    /** Elimina un diagnóstico registrado. */
    fun eliminarDiagnostico(diagnostico: Diagnostico) {
        viewModelScope.launch { repository.deleteDiagnostico(diagnostico) }
    }

    /** Registra una nueva alergia en la base de datos local. */
    fun agregarAlergia(sustancia: String, severidad: String, informacion: String?) {
        viewModelScope.launch {
            repository.insertAlergia(
                Alergia(pacienteId = pacienteId, sustancia = sustancia, severidad = severidad, informacion = informacion)
            )
        }
    }

    /** Elimina una alergia registrada. */
    fun eliminarAlergia(alergia: Alergia) {
        viewModelScope.launch { repository.deleteAlergia(alergia) }
    }

    /**
     * Agrega una nota de enfermería con trazabilidad.
     * HU-01: Paula Mendoza | HU-04: Karen Nava
     */
    fun agregarNota(texto: String, autor: String, tipo: String = "Nota") {
        viewModelScope.launch {
            repository.insertNota(
                NotaEnfermeria(pacienteId = pacienteId, texto = texto, autor = autor, tipo = tipo)
            )
        }
    }

    /** Elimina una nota de enfermería. */
    fun eliminarNota(nota: NotaEnfermeria) {
        viewModelScope.launch { repository.deleteNota(nota) }
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