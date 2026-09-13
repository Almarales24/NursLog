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
import com.nurslog.app.data.repository.MedicacionRepository
import com.nurslog.app.ui.historial.HistorialScreen
import com.nurslog.app.ui.historial.HistorialViewModel
import com.nurslog.app.ui.historial.HistorialViewModelFactory
import com.nurslog.app.ui.medicacion.MedicacionScreen
import com.nurslog.app.ui.medicacion.MedicacionViewModel
import com.nurslog.app.ui.medicacion.MedicacionViewModelFactory
import com.nurslog.app.ui.paciente.PacienteViewModel
import com.nurslog.app.ui.paciente.SeleccionPacienteScreen

// Define las rutas de navegación disponibles en la aplicación
object NursLogRoutes {
    // Ruta inicial: pantalla de selección de pacientes
    const val SELECCION_PACIENTE = "seleccion_paciente"
    // Ruta: pantalla de historial clínico con parámetro de ID del paciente
    const val HISTORIAL = "historial/{pacienteId}"
    // Ruta: pantalla de medicación con parámetro de ID del paciente
    const val MEDICACION = "medicacion/{pacienteId}"

    // Funciones auxiliares para generar rutas con parámetros
    fun historial(pacienteId: Int) = "historial/$pacienteId"
    fun medicacion(pacienteId: Int) = "medicacion/$pacienteId"
}

// Composable que define la navegación entre pantallas
@Composable
fun NavGraph(
    pacienteViewModel: PacienteViewModel,
    historialRepository: HistorialRepository,
    medicacionRepository: MedicacionRepository,
    navController: NavHostController = rememberNavController()
) {
    // Crea el host de navegación con la pantalla inicial
    NavHost(
        navController = navController,
        startDestination = NursLogRoutes.SELECCION_PACIENTE
    ) {
        // Pantalla de selección de pacientes
        composable(NursLogRoutes.SELECCION_PACIENTE) {
            SeleccionPacienteScreen(
                viewModel = pacienteViewModel,
                // Al seleccionar un paciente, navega al historial
                onPacienteSelected = { paciente ->
                    navController.navigate(NursLogRoutes.historial(paciente.id))
                }
            )
        }

        // Pantalla de historial clínico de un paciente
        composable(
            route = NursLogRoutes.HISTORIAL,
            arguments = listOf(navArgument("pacienteId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Extrae el ID del paciente de los argumentos
            val pacienteId = backStackEntry.arguments?.getInt("pacienteId") ?: 0
            // Crea el ViewModel del historial con su factory
            val historialViewModel: HistorialViewModel = viewModel(
                factory = HistorialViewModelFactory(historialRepository, pacienteId)
            )
            HistorialScreen(
                viewModel = historialViewModel,
                // Al hacer clic en medicación, navega a esa pantalla
                onIrAMedicacion = { navController.navigate(NursLogRoutes.medicacion(pacienteId)) }
            )
        }

        // Pantalla de medicación de un paciente
        composable(
            route = NursLogRoutes.MEDICACION,
            arguments = listOf(navArgument("pacienteId") { type = NavType.IntType })
        ) { backStackEntry ->
            // Extrae el ID del paciente de los argumentos
            val pacienteId = backStackEntry.arguments?.getInt("pacienteId") ?: 0
            // Crea el ViewModel de medicación con su factory
            val medicacionViewModel: MedicacionViewModel = viewModel(
                factory = MedicacionViewModelFactory(medicacionRepository, pacienteId)
            )
            MedicacionScreen(viewModel = medicacionViewModel)
        }
    }
}