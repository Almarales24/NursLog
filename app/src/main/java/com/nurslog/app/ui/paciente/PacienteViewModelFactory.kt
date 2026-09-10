package com.nurslog.app.ui.paciente

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.nurslog.app.data.repository.PacienteRepository

class PacienteViewModelFactory(
    private val repository: PacienteRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PacienteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PacienteViewModel(repository) as T
        }
        throw IllegalArgumentException("ViewModel desconocido: ${modelClass.name}")
    }
}