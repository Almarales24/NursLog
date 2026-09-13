package com.nurslog.app

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.nurslog.app.data.local.NursLogDatabase
import com.nurslog.app.data.repository.HistorialRepository
import com.nurslog.app.data.repository.MedicacionRepository
import com.nurslog.app.data.repository.PacienteRepository
import com.nurslog.app.notifications.MedicacionReminderWorker
import com.nurslog.app.notifications.NotificationHelper
import com.nurslog.app.ui.navigation.NavGraph
import com.nurslog.app.ui.paciente.PacienteViewModel
import com.nurslog.app.ui.paciente.PacienteViewModelFactory
import com.nurslog.app.ui.theme.NursLogTheme
import java.util.concurrent.TimeUnit

// Actividad principal que configura la UI y la navegación de la aplicación
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Habilita el diseño desde borde a borde (sistema de navegación transparente)
        enableEdgeToEdge()

        // Crea el canal de notificaciones para los recordatorios de medicación
        NotificationHelper.crearCanal(applicationContext)
        // Programa la tarea periódica de recordatorio de medicamentos
        programarRecordatorioMedicacion()

        // Establece el contenido Compose
        setContent {
            NursLogTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Solicita permiso para enviar notificaciones (Android 13+)
                    val solicitarPermiso = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission()
                    ) { /* resultado ignorado: si se niega, simplemente no habrá notificaciones */ }

                    // Lanza el permiso en la composición inicial
                    LaunchedEffect(Unit) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            solicitarPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }

                    // Inicializa la base de datos (singleton)
                    val database = remember { NursLogDatabase.getDatabase(applicationContext) }

                    // Crea los repositorios necesarios
                    val pacienteRepository = remember { PacienteRepository(database.pacienteDao()) }
                    val historialRepository = remember {
                        HistorialRepository(
                            diagnosticoDao = database.diagnosticoDao(),
                            alergiaDao = database.alergiaDao(),
                            notaEnfermeriaDao = database.notaEnfermeriaDao()
                        )
                    }
                    val medicacionRepository = remember {
                        MedicacionRepository(
                            medicamentoDao = database.medicamentoDao(),
                            horarioDao = database.horarioDao(),
                            registroAdministracionDao = database.registroAdministracionDao()
                        )
                    }

                    // Crea el ViewModel de pacientes con su factory
                    val pacienteViewModel: PacienteViewModel = viewModel(
                        factory = PacienteViewModelFactory(pacienteRepository)
                    )

                    // Configura la navegación entre pantallas
                    NavGraph(
                        pacienteViewModel = pacienteViewModel,
                        historialRepository = historialRepository,
                        medicacionRepository = medicacionRepository
                    )
                }
            }
        }
    }

    // Programa un trabajo periódico que revisa cada 15 minutos si hay medicaciones pendientes
    private fun programarRecordatorioMedicacion() {
        // Crea una solicitud de trabajo periódico cada 15 minutos (mínimo permitido)
        val solicitud = PeriodicWorkRequestBuilder<MedicacionReminderWorker>(15, TimeUnit.MINUTES)
            .build()

        // Enqueue el trabajo con política KEEP (mantiene el trabajo existente)
        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "recordatorio_medicacion",
            ExistingPeriodicWorkPolicy.KEEP,
            solicitud
        )
    }
}