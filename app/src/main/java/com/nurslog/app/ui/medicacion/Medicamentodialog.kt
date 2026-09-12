package com.nurslog.app.ui.medicacion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// Recomendación por medicamento vía OpenFDA (dosificación/advertencia)
// Normalización de nombre vía RxNorm (nombre comercial vs. genérico)
@Composable
fun MedicamentoDialog(
    viewModel: MedicacionViewModel,
    onConfirm: (nombre: String, dosis: String, via: String, recomendacion: String?, hora: String) -> Unit,
    onDismiss: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var dosis by remember { mutableStateOf("") }
    var via by remember { mutableStateOf("") }
    var hora by remember { mutableStateOf("") }
    var recomendacion by remember { mutableStateOf("") }
    val recomendacionSugerida by viewModel.recomendacionBuscada.collectAsState()
    val rxNormSugerencias by viewModel.rxNormResultados.collectAsState()

    val esValido = nombre.isNotBlank() && dosis.isNotBlank() && via.isNotBlank() && hora.isNotBlank()

    LaunchedEffect(nombre) {
        if (nombre.length >= 3) {
            viewModel.buscarRxNorm(nombre)
        } else {
            viewModel.limpiarRxNormResultados()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo medicamento") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                if (rxNormSugerencias.isNotEmpty()) {
                    Text(
                        text = "Sugerencias RxNorm:",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                    LazyColumn(modifier = Modifier.heightIn(max = 120.dp)) {
                        items(rxNormSugerencias) { sugerencia ->
                            Text(
                                text = sugerencia,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = dosis,
                    onValueChange = { dosis = it },
                    label = { Text("Dosis (ej. 500mg)") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp, bottom = 8.dp)
                )
                OutlinedTextField(
                    value = via,
                    onValueChange = { via = it },
                    label = { Text("Vía (VO, IV, IM, SC)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = hora,
                    onValueChange = { hora = it },
                    label = { Text("Hora (HH:mm)") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )

                Row {
                    TextButton(
                        onClick = { viewModel.buscarRecomendacion(nombre) },
                        enabled = nombre.isNotBlank()
                    ) {
                        Text("Buscar recomendación (OpenFDA)")
                    }
                }

                if (recomendacionSugerida != null && recomendacion.isBlank()) {
                    Text(
                        text = "Sugerido: $recomendacionSugerida",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    TextButton(onClick = { recomendacion = recomendacionSugerida ?: "" }) {
                        Text("Usar esta recomendación")
                    }
                }

                OutlinedTextField(
                    value = recomendacion,
                    onValueChange = { recomendacion = it },
                    label = { Text("Recomendación de uso (opcional)") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirm(nombre, dosis, via, recomendacion.ifBlank { null }, hora)
                    viewModel.limpiarRecomendacionBuscada()
                    viewModel.limpiarRxNormResultados()
                },
                enabled = esValido
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                viewModel.limpiarRecomendacionBuscada()
                viewModel.limpiarRxNormResultados()
                onDismiss()
            }) {
                Text("Cancelar")
            }
        }
    )
}