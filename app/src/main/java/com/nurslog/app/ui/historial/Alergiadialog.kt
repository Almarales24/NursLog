package com.nurslog.app.ui.historial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val SEVERIDADES = listOf("Leve", "Moderada", "Severa")

/**
 * Diálogo interactivo para registrar una nueva alergia.
 * Incluye la funcionalidad de búsqueda automática de información médica vía MedlinePlus.
 *
 * @param viewModel ViewModel para gestionar la búsqueda de información.
 * @param onConfirm Callback invocado al guardar la alergia.
 * @param onDismiss Callback invocado al cancelar el diálogo.
 */
@Composable
fun AlergiaDialog(
    viewModel: HistorialViewModel,
    onConfirm: (sustancia: String, severidad: String, informacion: String?) -> Unit,
    onDismiss: () -> Unit
) {
    var sustancia by remember { mutableStateOf("") }
    var severidad by remember { mutableStateOf(SEVERIDADES[0]) }
    var informacion by remember { mutableStateOf("") }
    val informacionSugerida by viewModel.infoAlergiaBuscada.collectAsState()

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva alergia") },
        text = {
            Column {
                OutlinedTextField(
                    value = sustancia,
                    onValueChange = { sustancia = it },
                    label = { Text("Sustancia") },
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "Severidad",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
                Row {
                    SEVERIDADES.forEach { opcion ->
                        FilterChip(
                            selected = severidad == opcion,
                            onClick = { severidad = opcion },
                            label = { Text(opcion) },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }

                TextButton(
                    onClick = { viewModel.buscarInfoAlergia(sustancia) },
                    enabled = sustancia.isNotBlank(),
                    modifier = Modifier.padding(top = 8.dp)
                ) {
                    Text("Buscar causa y recomendación (MedlinePlus)")
                }

                if (informacionSugerida != null && informacion.isBlank()) {
                    Text(
                        text = "Sugerido: $informacionSugerida",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextButton(onClick = { informacion = informacionSugerida ?: "" }) {
                        Text("Usar esta información")
                    }
                }

                OutlinedTextField(
                    value = informacion,
                    onValueChange = { informacion = it },
                    label = { Text("Causa, síntomas y recomendación (opcional)") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(sustancia, severidad, informacion.ifBlank { null })
                    viewModel.limpiarInfoAlergiaBuscada()
                },
                enabled = sustancia.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.limpiarInfoAlergiaBuscada()
                onDismiss()
            }) {
                Text("Cancelar")
            }
        }
    )
}