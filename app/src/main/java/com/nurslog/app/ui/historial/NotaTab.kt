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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nurslog.app.data.entity.NotaEnfermeria
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun NotaTab(
    notas: List<NotaEnfermeria>,
    onAgregarNota: (texto: String, autor: String) -> Unit
) {
    var texto by remember { mutableStateOf("") }
    var autor by remember { mutableStateOf("") }
    val formato = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
        // HU-03: objetivo del dato visible para el usuario - Laura Chaparro
        Text(
            text = "Registra observaciones de enfermería. Cada nota queda firmada con tu nombre y la hora automáticamente.",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        OutlinedTextField(
            value = autor,
            onValueChange = { autor = it },
            label = { Text("Tu nombre") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = texto,
            onValueChange = { texto = it },
            label = { Text("Nota de enfermería") },
            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)
        )

        Button(
            onClick = {
                if (texto.isNotBlank() && autor.isNotBlank()) {
                    onAgregarNota(texto, autor)
                    texto = ""
                }
            },
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text("Guardar nota")
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(notas, key = { it.id }) { nota ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = nota.texto,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "— ${nota.autor}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                            Text(
                                text = formato.format(Date(nota.fechaHora)),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    }
                }
            }
        }
    }
}