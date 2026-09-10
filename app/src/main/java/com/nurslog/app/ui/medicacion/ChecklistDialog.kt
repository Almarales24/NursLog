package com.nurslog.app.ui.medicacion

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
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

private val ITEMS_CHECKLIST = listOf(
    "Paciente correcto",
    "Medicamento correcto",
    "Dosis correcta",
    "Vía correcta",
    "Hora correcta"
)

// HU-10: checklist simple sin botones de redireccion adicionales
@Composable
fun ChecklistDialog(
    medicamentoNombre: String,
    onConfirm: (enfermero: String, nota: String?) -> Unit,
    onDismiss: () -> Unit
) {
    val marcados = remember { mutableStateOf(List(ITEMS_CHECKLIST.size) { false }) }
    var enfermero by remember { mutableStateOf("") }
    var nota by remember { mutableStateOf("") }

    val todosMarcados = marcados.value.all { it }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Verificación: $medicamentoNombre") },
        text = {
            Column {
                ITEMS_CHECKLIST.forEachIndexed { index, item ->
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
                    ) {
                        Checkbox(
                            checked = marcados.value[index],
                            onCheckedChange = { checked ->
                                marcados.value = marcados.value.toMutableList().also {
                                    it[index] = checked
                                }
                            }
                        )
                        Text(
                            text = item,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = enfermero,
                    onValueChange = { enfermero = it },
                    label = { Text("Tu nombre") },
                    modifier = Modifier.fillMaxWidth().padding(top = 12.dp)
                )

                // HU-05: nota asociada al registro de medicación - Karol Leon
                OutlinedTextField(
                    value = nota,
                    onValueChange = { nota = it },
                    label = { Text("Observaciones (opcional)") },
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(enfermero, nota.ifBlank { null }) },
                enabled = todosMarcados && enfermero.isNotBlank()
            ) {
                Text("Confirmar administración")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}