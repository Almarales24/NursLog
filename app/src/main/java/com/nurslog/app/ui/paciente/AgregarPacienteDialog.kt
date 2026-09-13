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
import com.nurslog.app.data.entity.Paciente

// Diálogo para agregar o editar un paciente
@Composable
fun AgregarPacienteDialog(
    // Paciente a editar (null si se está creando uno nuevo)
    pacienteExistente: Paciente? = null,
    // Callback al confirmar con datos validados
    onConfirm: (nombre: String, cama: String, sala: String, edad: Int) -> Unit,
    // Callback al cancelar o cerrar el diálogo
    onDismiss: () -> Unit
) {
    // Estados de los campos del formulario (pre-llenan con datos existentes si es edición)
    var nombre by remember { mutableStateOf(pacienteExistente?.nombre ?: "") }
    var cama by remember { mutableStateOf(pacienteExistente?.cama ?: "") }
    var sala by remember { mutableStateOf(pacienteExistente?.sala ?: "") }
    var edadTexto by remember { mutableStateOf(pacienteExistente?.edad?.toString() ?: "") }

    // Intenta parsear la edad como entero
    val edad = edadTexto.toIntOrNull()
    // Valida que todos los campos obligatorios estén completos y sean válidos
    val esValido = nombre.isNotBlank() && cama.isNotBlank() && sala.isNotBlank() && edad != null

    // Diálogo con título, campos de entrada y botones
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (pacienteExistente == null) "Nuevo paciente" else "Editar paciente") },
        text = {
            Column {
                // Campo de nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                // Campo de cama
                OutlinedTextField(
                    value = cama,
                    onValueChange = { cama = it },
                    label = { Text("Cama") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                // Campo de sala
                OutlinedTextField(
                    value = sala,
                    onValueChange = { sala = it },
                    label = { Text("Sala") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
                )
                // Campo de edad
                OutlinedTextField(
                    value = edadTexto,
                    onValueChange = { edadTexto = it },
                    label = { Text("Edad") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        // Botón de confirmación (solo habilitado si datos son válidos)
        confirmButton = {
            TextButton(
                onClick = { if (esValido) onConfirm(nombre, cama, sala, edad!!) },
                enabled = esValido
            ) {
                Text("Guardar")
            }
        },
        // Botón de cancelación
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}