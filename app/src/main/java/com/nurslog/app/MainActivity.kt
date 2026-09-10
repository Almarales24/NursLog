package com.nurslog.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.nurslog.app.data.local.NursLogDatabase
import com.nurslog.app.data.repository.HistorialRepository
import com.nurslog.app.data.repository.PacienteRepository
import com.nurslog.app.ui.navigation.NavGraph
import com.nurslog.app.ui.paciente.PacienteViewModel
import com.nurslog.app.ui.paciente.PacienteViewModelFactory
import com.nurslog.app.ui.theme.NursLogTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NursLogTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val database = remember { NursLogDatabase.getDatabase(applicationContext) }

                    val pacienteRepository = remember { PacienteRepository(database.pacienteDao()) }
                    val historialRepository = remember {
                        HistorialRepository(
                            diagnosticoDao = database.diagnosticoDao(),
                            alergiaDao = database.alergiaDao(),
                            notaEnfermeriaDao = database.notaEnfermeriaDao()
                        )
                    }

                    val pacienteViewModel: PacienteViewModel = viewModel(
                        factory = PacienteViewModelFactory(pacienteRepository)
                    )

                    NavGraph(
                        pacienteViewModel = pacienteViewModel,
                        historialRepository = historialRepository
                    )
                }
            }
        }
    }
}