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
import com.nurslog.app.data.entity.Alergia
import com.nurslog.app.ui.components.EstadoBadge
import com.nurslog.app.ui.components.SwipeToDeleteItem

/**
 * Pestaña de visualización y gestión de alergias dentro del Historial Clínico.
 * Muestra una lista de alergias y permite agregar nuevas o eliminar las existentes.
 *
 * @param alergias Lista de alergias del paciente.
 * @param viewModel ViewModel encargado de la lógica del historial.
 */
@Composable
fun AlergiaTab(
    alergias: List<Alergia>,
    viewModel: HistorialViewModel
) {
    var mostrarDialogo by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // HU-03: objetivo del dato visible para el usuario - Laura Chaparro
        Text(
            text = "Alergias conocidas del paciente. Revisa esta lista antes de administrar cualquier medicamento. Desliza a la izquierda para eliminar.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Button(
            onClick = { mostrarDialogo = true },
            modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp)
        ) {
            Text("Agregar alergia")
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(alergias, key = { it.id }) { alergia ->
                SwipeToDeleteItem(onDelete = { viewModel.eliminarAlergia(alergia) }) {
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = alergia.sustancia,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                EstadoBadge(texto = alergia.severidad)
                            }
                            alergia.informacion?.let { info ->
                                Text(
                                    text = info,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.secondary,
                                    modifier = Modifier.padding(top = 6.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarDialogo) {
        AlergiaDialog(
            viewModel = viewModel,
            onConfirm = { sustancia, severidad, informacion ->
                viewModel.agregarAlergia(sustancia, severidad, informacion)
                mostrarDialogo = false
            },
            onDismiss = { mostrarDialogo = false }
        )
    }
}