package com.nurslog.app.ui.paciente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nurslog.app.data.entity.Paciente
import com.nurslog.app.data.repository.PacienteRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

// ViewModel que gestiona la lógica de la pantalla de selección de pacientes
@OptIn(FlowPreview::class)
class PacienteViewModel(
    private val repository: PacienteRepository
) : ViewModel() {

    // Flujo mutable del texto de búsqueda (cambios de usuario)
    private val _searchQuery = MutableStateFlow("")
    // Flujo observable del texto de búsqueda (expuesto a UI)
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Lista de pacientes con búsqueda en tiempo real (con debounce para no sobrecargar)
    val pacientes: StateFlow<List<Paciente>> = _searchQuery
        // Espera 300ms sin cambios antes de ejecutar la búsqueda
        .debounce(300)
        // Cambia a un nuevo flujo de búsqueda según la consulta
        .flatMapLatest { query ->
            if (query.isBlank()) repository.getAllPacientes()
            else repository.searchPacientes(query)
        }
        // Convierte el flujo a StateFlow que mantiene el último valor
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    // Actualiza el texto de búsqueda cuando el usuario escribe
    fun onSearchQueryChange(query: String) {
        _searchQuery.value = query
    }

    // Crea un nuevo paciente en la base de datos
    fun agregarPaciente(nombre: String, cama: String, sala: String, edad: Int) {
        viewModelScope.launch {
            repository.insert(Paciente(nombre = nombre, cama = cama, sala = sala, edad = edad))
        }
    }

    // Actualiza los datos de un paciente existente
    fun editarPaciente(paciente: Paciente, nombre: String, cama: String, sala: String, edad: Int) {
        viewModelScope.launch {
            repository.update(paciente.copy(nombre = nombre, cama = cama, sala = sala, edad = edad))
        }
    }

    // Elimina un paciente de la base de datos
    fun eliminarPaciente(paciente: Paciente) {
        viewModelScope.launch {
            repository.delete(paciente)
        }
    }
}