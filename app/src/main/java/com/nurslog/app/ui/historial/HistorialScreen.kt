package com.nurslog.app.ui.historial

import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// HU-09: mismo patrón de navegación por tabs ya validado - Juan Felipe Giraldo
@Composable
fun HistorialScreen(
    viewModel: HistorialViewModel,
    onIrAMedicacion: () -> Unit,
    modifier: Modifier = Modifier
) {
    var tabSeleccionada by remember { mutableIntStateOf(0) }
    val titulos = listOf("Diagnóstico", "Alergias", "Notas")

    val diagnosticos by viewModel.diagnosticos.collectAsState()
    val alergias by viewModel.alergias.collectAsState()
    val notas by viewModel.notas.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = "Historial clínico",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(16.dp)
        )

        TabRow(selectedTabIndex = tabSeleccionada) {
            titulos.forEachIndexed { index, titulo ->
                Tab(
                    selected = tabSeleccionada == index,
                    onClick = { tabSeleccionada = index },
                    text = { Text(titulo) }
                )
            }
        }

        when (tabSeleccionada) {
            0 -> DiagnosticoTab(diagnosticos = diagnosticos, viewModel = viewModel)
            1 -> AlergiaTab(alergias = alergias, onAgregarAlergia = viewModel::agregarAlergia)
            2 -> NotaTab(notas = notas, onAgregarNota = viewModel::agregarNota)
        }

        Button(
            onClick = onIrAMedicacion,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Text("Ir a Medicación")
        }
    }
}