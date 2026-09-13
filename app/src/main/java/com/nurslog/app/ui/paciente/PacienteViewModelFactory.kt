package com.nurslog.app.ui.paciente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nurslog.app.data.repository.PacienteRepository

// Factory que crea instancias de PacienteViewModel con inyección de dependencias
class PacienteViewModelFactory(
    private val repository: PacienteRepository
) : ViewModelProvider.Factory {
    // Crea la instancia del ViewModel con el repositorio
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PacienteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PacienteViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}