package com.nurslog.app.ui.historial

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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

// HU-09: mismo patrón de navegación por tabs ya validado - Juan Felipe Giraldo
@Composable
fun HistorialScreen(
    viewModel: HistorialViewModel,
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
            modifier = Modifier
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
            0 -> DiagnosticoTab(diagnosticos = diagnosticos)
            1 -> AlergiaTab(alergias = alergias)
            2 -> NotaTab(notas = notas, onAgregarNota = viewModel::agregarNota)
        }
    }
}