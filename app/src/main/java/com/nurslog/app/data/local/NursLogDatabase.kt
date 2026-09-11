package com.nurslog.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.nurslog.app.dao.PacienteDao
import com.nurslog.app.dao.DiagnosticoDao
import com.nurslog.app.dao.AlergiaDao
import com.nurslog.app.dao.NotaEnfermeriaDao
import com.nurslog.app.dao.MedicamentoDao
import com.nurslog.app.dao.HorarioDao
import com.nurslog.app.dao.RegistroAdministracionDao
import com.nurslog.app.data.entity.Paciente
import com.nurslog.app.data.entity.Diagnostico
import com.nurslog.app.data.entity.Alergia
import com.nurslog.app.data.entity.NotaEnfermeria
import com.nurslog.app.data.entity.Medicamento
import com.nurslog.app.data.entity.Horario
import com.nurslog.app.data.entity.RegistroAdministracion
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        Paciente::class,
        Diagnostico::class,
        Alergia::class,
        NotaEnfermeria::class,
        Medicamento::class,
        Horario::class,
        RegistroAdministracion::class
    ],
    version = 1,
    exportSchema = false
)
abstract class NursLogDatabase : RoomDatabase() {

    abstract fun pacienteDao(): PacienteDao
    abstract fun diagnosticoDao(): DiagnosticoDao
    abstract fun alergiaDao(): AlergiaDao
    abstract fun notaEnfermeriaDao(): NotaEnfermeriaDao
    abstract fun medicamentoDao(): MedicamentoDao
    abstract fun horarioDao(): HorarioDao
    abstract fun registroAdministracionDao(): RegistroAdministracionDao

    companion object {
        @Volatile
        private var INSTANCE: NursLogDatabase? = null

        fun getDatabase(context: Context): NursLogDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NursLogDatabase::class.java,
                    "nurslog_database"
                )
                    .addCallback(seedCallback)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Datos de ejemplo: se insertan solo la primera vez que se crea la BD.
        // Son datos de demostracion, se pueden borrar manualmente desde la app.
        private val seedCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedDatabase(database)
                    }
                }
            }
        }

        private suspend fun seedDatabase(database: NursLogDatabase) {
            val pacienteDao = database.pacienteDao()
            val diagnosticoDao = database.diagnosticoDao()
            val alergiaDao = database.alergiaDao()
            val notaDao = database.notaEnfermeriaDao()
            val medicamentoDao = database.medicamentoDao()
            val horarioDao = database.horarioDao()

            val paciente1Id = pacienteDao.insert(
                Paciente(nombre = "Carlos Ramírez", cama = "12A", sala = "3", edad = 58)
            ).toInt()

            val paciente2Id = pacienteDao.insert(
                Paciente(nombre = "María Peña", cama = "07B", sala = "2", edad = 34)
            ).toInt()

            diagnosticoDao.insert(
                Diagnostico(pacienteId = paciente1Id, descripcion = "Neumonía adquirida en comunidad", estado = "Activo")
            )
            diagnosticoDao.insert(
                Diagnostico(pacienteId = paciente1Id, descripcion = "Hipertensión controlada", estado = "Estable")
            )
            alergiaDao.insert(
                Alergia(pacienteId = paciente1Id, sustancia = "Penicilina", severidad = "Severa")
            )
            notaDao.insert(
                NotaEnfermeria(pacienteId = paciente1Id, texto = "Paciente afebril, tolera vía oral, se mantiene en observación.", autor = "Enf. Ríos")
            )

            diagnosticoDao.insert(
                Diagnostico(pacienteId = paciente2Id, descripcion = "Post-operatorio apendicectomía", estado = "Estable")
            )
            alergiaDao.insert(
                Alergia(pacienteId = paciente2Id, sustancia = "Aspirina", severidad = "Leve")
            )

            val medicamento1Id = medicamentoDao.insert(
                Medicamento(nombre = "Paracetamol", dosis = "500mg", via = "VO")
            ).toInt()
            val medicamento2Id = medicamentoDao.insert(
                Medicamento(nombre = "Omeprazol", dosis = "20mg", via = "VO")
            ).toInt()

            horarioDao.insert(Horario(medicamentoId = medicamento1Id, pacienteId = paciente1Id, hora = "08:00"))
            horarioDao.insert(Horario(medicamentoId = medicamento2Id, pacienteId = paciente1Id, hora = "08:00"))
            horarioDao.insert(Horario(medicamentoId = medicamento1Id, pacienteId = paciente2Id, hora = "12:00"))
        }
    }
}