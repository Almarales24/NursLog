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

@Composable
fun MedicamentoItem(
    item: MedicacionUiItem,
    onClick: (MedicacionUiItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val pendiente = item.estado == "Pendiente"

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = pendiente) { onClick(item) },
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(end = 8.dp)) {
                Text(
                    text = item.medicamentoNombre,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "${item.dosis} · ${item.via}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
            Text(
                text = item.hora,
                style = MaterialTheme.typography.bodyMedium,
                color = if (pendiente) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.tertiary
            )
        }
    }
}