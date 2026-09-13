package com.nurslog.app.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nurslog.app.ui.medicacion.MedicacionUiItem

// Componente que muestra una medicación con su información y estado
@Composable
fun MedicamentoItem(
    // Datos de la medicación a mostrar
    item: MedicacionUiItem,
    // Callback al hacer clic (solo si está pendiente)
    onClick: (MedicacionUiItem) -> Unit,
    modifier: Modifier = Modifier
) {
    // Verifica si la medicación aún está pendiente de administrar
    val pendiente = item.estado == "Pendiente"

    // Tarjeta con información de la medicación
    Card(
        modifier = modifier
            .fillMaxWidth()
            // Solo es clickeable si está pendiente
            .clickable(enabled = pendiente) { onClick(item) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
            // Fila con información del medicamento e hora
            Row(modifier = Modifier.fillMaxWidth()) {
                // Información del medicamento (nombre, dosis, vía)
                Column(modifier = Modifier.padding(end = 8.dp)) {
                    // Nombre del medicamento
                    Text(
                        text = item.medicamentoNombre,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    // Dosis y vía de administración
                    Text(
                        text = "${item.dosis} · ${item.via}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                // Hora programada con color según estado
                Text(
                    text = item.hora,
                    style = MaterialTheme.typography.bodyMedium,
                    // Rojo si está pendiente, verde si ya se administró
                    color = if (pendiente) MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.tertiary
                )
            }

            // Muestra la recomendación de uso si existe
            item.recomendacion?.let { recomendacion ->
                Text(
                    text = recomendacion,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}