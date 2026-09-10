package com.nurslog.app.ui.medicacion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nurslog.app.ui.components.MedicamentoItem

@Composable
fun MedicacionScreen(
    viewModel: MedicacionViewModel,
    modifier: Modifier = Modifier
) {
    val items by viewModel.medicacionItems.collectAsState()
    var itemSeleccionado by remember { mutableStateOf<MedicacionUiItem?>(null) }

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = "Medicación",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 12.dp)
        )

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(items, key = { it.horarioId }) { item ->
                MedicamentoItem(
                    item = item,
                    onClick = { itemSeleccionado = it }
                )
            }
        }
    }

    itemSeleccionado?.let { item ->
        ChecklistDialog(
            medicamentoNombre = item.medicamentoNombre,
            onConfirm = { enfermero, nota ->
                viewModel.confirmarAdministracion(item.horarioId, enfermero, nota)
                itemSeleccionado = null
            },
            onDismiss = { itemSeleccionado = null }
        )
    }
}