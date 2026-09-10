package com.nurslog.app.ui.paciente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nurslog.app.data.entity.Paciente
import com.nurslog.app.ui.components.PacienteCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionPacienteScreen(
    viewModel: PacienteViewModel,
    onPacienteSelected: (Paciente) -> Unit,
    modifier: Modifier = Modifier
) {
    val pacientes by viewModel.pacientes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Pacientes",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )

        OutlinedTextField(
            value = searchQuery,
            onValueChange = viewModel::onSearchQueryChange,
            label = { Text("Buscar paciente") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp, bottom = 12.dp)
        )

        LazyColumn {
            items(pacientes, key = { it.id }) { paciente ->
                PacienteCard(
                    paciente = paciente,
                    onClick = onPacienteSelected,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }
        }
    }
}