package com.nurslog.app.ui.paciente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nurslog.app.data.entity.Paciente
import com.nurslog.app.data.repository.PacienteRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.FlowPreview

@OptIn(FlowPreview::class)
class PacienteViewModel(
    private val repository: PacienteRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    val pacientes: StateFlow<List<Paciente>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isBlank()) repository.getAllPacientes()
            else repository.searchPacientes(query)
        }
        .stateIn(
            scope = viewModelScope,
            started = kotlinx.coroutines.flow.SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    fun agregarPaciente(nombre: String, cama: String, sala: String, edad: Int) {
        viewModelScope.launch {
            repository.insert(Paciente(nombre = nombre, cama = cama, sala = sala, edad = edad))
        }
    }
}