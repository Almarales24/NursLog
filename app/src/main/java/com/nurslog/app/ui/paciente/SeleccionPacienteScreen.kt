package com.nurslog.app.ui.paciente

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.nurslog.app.data.entity.Paciente
import com.nurslog.app.ui.components.PacienteCard
import com.nurslog.app.ui.components.SwipeToDeleteItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeleccionPacienteScreen(
    viewModel: PacienteViewModel,
    onPacienteSelected: (Paciente) -> Unit,
    modifier: Modifier = Modifier
) {
    val pacientes by viewModel.pacientes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    var mostrarDialogo by remember { mutableStateOf(false) }
    var pacienteEditando by remember { mutableStateOf<Paciente?>(null) }
    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
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

            Text(
                text = "Desliza a la izquierda para eliminar · Mantén presionado para editar",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn {
                items(pacientes, key = { it.id }) { paciente ->
                    SwipeToDeleteItem(
                        onDelete = { viewModel.eliminarPaciente(paciente) },
                        modifier = Modifier.padding(vertical = 4.dp)
                    ) {
                        PacienteCard(
                            paciente = paciente,
                            onClick = onPacienteSelected,
                            onLongClick = { pacienteEditando = it }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = { mostrarDialogo = true },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp)
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Agregar paciente")
        }

        // Botón temporal de prueba: fuerza la revisión de recordatorios sin esperar los 15 min
        TextButton(
            onClick = {
                val solicitud = androidx.work.OneTimeWorkRequestBuilder<com.nurslog.app.notifications.MedicacionReminderWorker>().build()
                androidx.work.WorkManager.getInstance(context).enqueue(solicitud)
            },
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp)
        ) {
            Text("Probar recordatorios ahora")
        }
    }

    if (mostrarDialogo) {
        AgregarPacienteDialog(
            onConfirm = { nombre, cama, sala, edad ->
                viewModel.agregarPaciente(nombre, cama, sala, edad)
                mostrarDialogo = false
            },
            onDismiss = { mostrarDialogo = false }
        )
    }

    pacienteEditando?.let { paciente ->
        AgregarPacienteDialog(
            pacienteExistente = paciente,
            onConfirm = { nombre, cama, sala, edad ->
                viewModel.editarPaciente(paciente, nombre, cama, sala, edad)
                pacienteEditando = null
            },
            onDismiss = { pacienteEditando = null }
        )
    }
}