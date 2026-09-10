package com.nurslog.app.ui.paciente

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
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

@Composable
fun AgregarPacienteDialog(
    onConfirm: (nombre: String, cama: String, sala: String, edad: Int) -> Unit,
    onDismiss: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var cama by remember { mutableStateOf("") }
    var sala by remember { mutableStateOf("") }
    var edadTexto by remember { mutableStateOf("") }

    val edad = edadTexto.toIntOrNull()
    val esValido = nombre.isNotBlank() && cama.isNotBlank() && sala.isNotBlank() && edad != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nuevo paciente") },
        text = {
            Column {
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = cama,
                    onValueChange = { cama = it },
                    label = { Text("Cama") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = sala,
                    onValueChange = { sala = it },
                    label = { Text("Sala") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                OutlinedTextField(
                    value = edadTexto,
                    onValueChange = { edadTexto = it },
                    label = { Text("Edad") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (esValido) onConfirm(nombre, cama, sala, edad!!) },
                enabled = esValido
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
