package com.nurslog.app.ui.historial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
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
import com.nurslog.app.data.remote.icd10.Icd10Resultado

private val ESTADOS = listOf("Activo", "Estable", "Resuelto")

// HU-08: acceso a clasificación ICD-10 para estandarizar diagnósticos - Karol
@Composable
fun DiagnosticoDialog(
    viewModel: HistorialViewModel,
    onConfirm: (descripcion: String, estado: String) -> Unit,
    onDismiss: () -> Unit
) {
    var descripcion by remember { mutableStateOf("") }
    var estado by remember { mutableStateOf(ESTADOS[0]) }
    val resultadosIcd10 by viewModel.icd10Resultados.collectAsState()

    LaunchedEffect(descripcion) {
        if (descripcion.length >= 3) {
            viewModel.buscarIcd10(descripcion)
        } else {
            viewModel.limpiarIcd10Resultados()
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo diagnóstico") },
        text = {
            Column {
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Buscar en ICD-10") },
                    modifier = Modifier.fillMaxWidth()
                )

                if (resultadosIcd10.isNotEmpty()) {
                    LazyColumn(modifier = Modifier.heightIn(max = 150.dp)) {
                        items(resultadosIcd10) { resultado: Icd10Resultado ->
                            Text(
                                text = "${resultado.codigo} — ${resultado.nombre}",
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp)
                            )
                        }
                    }
                }

                Text(
                    text = "Estado",
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier.padding(top = 12.dp, bottom = 4.dp)
                )
                Row {
                    ESTADOS.forEach { opcion ->
                        FilterChip(
                            selected = estado == opcion,
                            onClick = { estado = opcion },
                            label = { Text(opcion) },
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (descripcion.isNotBlank()) onConfirm(descripcion, estado) },
                enabled = descripcion.isNotBlank()
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}