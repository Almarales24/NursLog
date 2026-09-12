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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        NotificationHelper.crearCanal(applicationContext)
        programarRecordatorioMedicacion()

        setContent {
            NursLogTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val solicitarPermiso = rememberLauncherForActivityResult(
                        contract = ActivityResultContracts.RequestPermission()
                    ) { /* resultado ignorado: si se niega, simplemente no habrá notificaciones */ }

                    LaunchedEffect(Unit) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            solicitarPermiso.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }

                    val database = remember { NursLogDatabase.getDatabase(applicationContext) }

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

                    val pacienteViewModel: PacienteViewModel = viewModel(
                        factory = PacienteViewModelFactory(pacienteRepository)
                    )

                    NavGraph(
                        pacienteViewModel = pacienteViewModel,
                        historialRepository = historialRepository,
                        medicacionRepository = medicacionRepository
                    )
                }
            }
        }
    }

    // Recordatorio de medicación: revisa cada 15 minutos (mínimo permitido por WorkManager)
    private fun programarRecordatorioMedicacion() {
        val solicitud = PeriodicWorkRequestBuilder<MedicacionReminderWorker>(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(applicationContext).enqueueUniquePeriodicWork(
            "recordatorio_medicacion",
            ExistingPeriodicWorkPolicy.KEEP,
            solicitud
        )
    }
}