package com.nurslog.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.nurslog.app.data.repository.HistorialRepository
import com.nurslog.app.ui.historial.HistorialScreen
import com.nurslog.app.ui.historial.HistorialViewModel
import com.nurslog.app.ui.historial.HistorialViewModelFactory
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
    historialRepository: HistorialRepository,
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

        composable(
            route = NursLogRoutes.HISTORIAL,
            arguments = listOf(navArgument("pacienteId") { type = NavType.IntType })
        ) { backStackEntry ->
            val pacienteId = backStackEntry.arguments?.getInt("pacienteId") ?: 0
            val historialViewModel: HistorialViewModel = viewModel(
                factory = HistorialViewModelFactory(historialRepository, pacienteId)
            )
            HistorialScreen(viewModel = historialViewModel)
        }

        // Se agrega cuando construyamos MedicacionScreen
        // composable(NursLogRoutes.MEDICACION) { backStackEntry -> ... }
    }
}