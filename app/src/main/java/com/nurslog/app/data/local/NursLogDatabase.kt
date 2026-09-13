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

// Clase base que define la estructura de la base de datos SQLite usando Room
@Database(
    // Todas las entidades que serán tablas en la BD
    entities = [
        Paciente::class,
        Diagnostico::class,
        Alergia::class,
        NotaEnfermeria::class,
        Medicamento::class,
        Horario::class,
        RegistroAdministracion::class
    ],
    // Versión de esquema de la base de datos
    version = 1,
    // No exporta el esquema a archivos JSON
    exportSchema = false
)
abstract class NursLogDatabase : RoomDatabase() {

    // Proporciona acceso al DAO de pacientes
    abstract fun pacienteDao(): PacienteDao
    // Proporciona acceso al DAO de diagnósticos
    abstract fun diagnosticoDao(): DiagnosticoDao
    // Proporciona acceso al DAO de alergias
    abstract fun alergiaDao(): AlergiaDao
    // Proporciona acceso al DAO de notas de enfermería
    abstract fun notaEnfermeriaDao(): NotaEnfermeriaDao
    // Proporciona acceso al DAO de medicamentos
    abstract fun medicamentoDao(): MedicamentoDao
    // Proporciona acceso al DAO de horarios
    abstract fun horarioDao(): HorarioDao
    // Proporciona acceso al DAO de registros de administración
    abstract fun registroAdministracionDao(): RegistroAdministracionDao

    // Companion object para manejar la instancia única de la BD (Singleton)
    companion object {
        // Variable volátil para acceso thread-safe
        @Volatile
        private var INSTANCE: NursLogDatabase? = null

        // Obtiene o crea la instancia única de la base de datos
        fun getDatabase(context: Context): NursLogDatabase {
            return INSTANCE ?: synchronized(this) {
                // Construye la instancia si no existe
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NursLogDatabase::class.java,
                    "nurslog_database"
                )
                    // Ejecuta datos de demostración al crear la BD por primera vez
                    .addCallback(seedCallback)
                    // Recrea la BD si hay cambios de esquema (desarrollo)
                    .fallbackToDestructiveMigration(dropAllTables = true)
                    .build()
                INSTANCE = instance
                instance
            }
        }

        // Callback para insertar datos de ejemplo en la creación de la BD
        private val seedCallback = object : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                // Accede a la instancia y ejecuta el seed en background
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        seedDatabase(database)
                    }
                }
            }
        }

        // Inserta datos de demostración: dos pacientes con medicamentos, diagnósticos y alergias
        private suspend fun seedDatabase(database: NursLogDatabase) {
            val pacienteDao = database.pacienteDao()
            val diagnosticoDao = database.diagnosticoDao()
            val alergiaDao = database.alergiaDao()
            val notaDao = database.notaEnfermeriaDao()
            val medicamentoDao = database.medicamentoDao()
            val horarioDao = database.horarioDao()

            // Inserta el primer paciente de demostración
            val paciente1Id = pacienteDao.insert(
                Paciente(nombre = "Carlos Ramírez", cama = "12A", sala = "3", edad = 58)
            ).toInt()

            // Inserta el segundo paciente de demostración
            val paciente2Id = pacienteDao.insert(
                Paciente(nombre = "María Peña", cama = "07B", sala = "2", edad = 34)
            ).toInt()

            // Agrega diagnósticos al primer paciente
            diagnosticoDao.insert(
                Diagnostico(pacienteId = paciente1Id, descripcion = "Neumonía adquirida en comunidad", estado = "Activo")
            )
            diagnosticoDao.insert(
                Diagnostico(pacienteId = paciente1Id, descripcion = "Hipertensión controlada", estado = "Estable")
            )
            // Agrega una alergia severa al primer paciente
            alergiaDao.insert(
                Alergia(pacienteId = paciente1Id, sustancia = "Penicilina", severidad = "Severa")
            )
            // Agrega una nota de enfermería al primer paciente
            notaDao.insert(
                NotaEnfermeria(pacienteId = paciente1Id, texto = "Paciente afebril, tolera vía oral, se mantiene en observación.", autor = "Enf. Ríos")
            )

            // Agrega un diagnóstico al segundo paciente
            diagnosticoDao.insert(
                Diagnostico(pacienteId = paciente2Id, descripcion = "Post-operatorio apendicectomía", estado = "Estable")
            )
            // Agrega una alergia leve al segundo paciente
            alergiaDao.insert(
                Alergia(pacienteId = paciente2Id, sustancia = "Aspirina", severidad = "Leve")
            )

            // Inserta medicamentos de demostración
            val medicamento1Id = medicamentoDao.insert(
                Medicamento(nombre = "Paracetamol", dosis = "500mg", via = "VO")
            ).toInt()
            val medicamento2Id = medicamentoDao.insert(
                Medicamento(nombre = "Omeprazol", dosis = "20mg", via = "VO")
            ).toInt()

            // Asocia medicamentos con pacientes en horarios específicos
            horarioDao.insert(Horario(medicamentoId = medicamento1Id, pacienteId = paciente1Id, hora = "08:00"))
            horarioDao.insert(Horario(medicamentoId = medicamento2Id, pacienteId = paciente1Id, hora = "08:00"))
            horarioDao.insert(Horario(medicamentoId = medicamento1Id, pacienteId = paciente2Id, hora = "12:00"))
        }
    }
}