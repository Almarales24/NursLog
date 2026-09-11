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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

private val SEVERIDADES = listOf("Leve", "Moderada", "Severa")

@Composable
fun AlergiaDialog(
    onConfirm: (sustancia: String, severidad: String) -> Unit,
    onDismiss: () -> Unit
) {
    var sustancia by remember { mutableStateOf("") }
    var severidad by remember { mutableStateOf(SEVERIDADES[0]) }

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
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (sustancia.isNotBlank()) onConfirm(sustancia, severidad) },
                enabled = sustancia.isNotBlank()
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