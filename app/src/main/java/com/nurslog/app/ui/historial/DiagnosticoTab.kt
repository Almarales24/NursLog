package com.nurslog.app.ui.historial

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nurslog.app.data.entity.Diagnostico
import com.nurslog.app.ui.components.EstadoBadge

@Composable
fun DiagnosticoTab(
    diagnosticos: List<Diagnostico>,
    viewModel: HistorialViewModel
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // HU-03: objetivo del dato visible para el usuario - Laura Chaparro
        Text(
            text = "Diagnósticos registrados del paciente. Se usan para evitar administraciones o procedimientos contraindicados.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Button(
            onClick = { mostrarDialogo = true },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            Text("Agregar diagnóstico")
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(diagnosticos, key = { it.id }) { diagnostico ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = diagnostico.descripcion,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        EstadoBadge(texto = diagnostico.estado)
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        DiagnosticoDialog(
            viewModel = viewModel,
            onConfirm = { descripcion, estado ->
                viewModel.agregarDiagnostico(descripcion, estado)
                mostrarDialogo = false
            },
            onDismiss = { mostrarDialogo = false }
        )
    }
}