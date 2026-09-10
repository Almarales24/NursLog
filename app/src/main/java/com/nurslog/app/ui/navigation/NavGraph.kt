package com.nurslog.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.nurslog.app.ui.paciente.PacienteViewModel
import com.nurslog.app.ui.paciente.SeleccionPacienteScreen

// HU-09: navegación consistente en toda la app - Juan Felipe Giraldo
object NursLogRoutes {
    const val SELECCION_PACIENTE = "seleccion_paciente"
    const val HISTORIAL = "historial/{pacienteId}"
    const val MEDICACION = "medicacion/{pacienteId}"

    fun historial(pacienteId: Int) = "historial/$pacienteId"
    fun medicacion(pacienteId: Int) = "medicacion/$pacienteId"
}

@Composable
fun NavGraph(
    pacienteViewModel: PacienteViewModel,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = NursLogRoutes.SELECCION_PACIENTE
    ) {
        composable(NursLogRoutes.SELECCION_PACIENTE) {
            SeleccionPacienteScreen(
                viewModel = pacienteViewModel,
                onPacienteSelected = { paciente ->
                    navController.navigate(NursLogRoutes.historial(paciente.id))
                }
            )
        }

        // Se agregan cuando construyamos HistorialScreen y MedicacionScreen
        // composable(NursLogRoutes.HISTORIAL) { backStackEntry -> ... }
        // composable(NursLogRoutes.MEDICACION) { backStackEntry -> ... }
    }
}