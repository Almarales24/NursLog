package com.nurslog.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}