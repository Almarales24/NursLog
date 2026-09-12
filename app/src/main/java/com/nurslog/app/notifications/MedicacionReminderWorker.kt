package com.nurslog.app.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nurslog.app.data.local.NursLogDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Recordatorio de administración de medicamentos: revisa periódicamente si algún
// horario ya llegó a su hora y no ha sido administrado hoy, y notifica al enfermero.
class MedicacionReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val db = NursLogDatabase.getDatabase(applicationContext)
        val prefs = applicationContext.getSharedPreferences("nurslog_notificaciones", Context.MODE_PRIVATE)
        val formatoFecha = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val hoy = formatoFecha.format(Date())

        val ahora = Calendar.getInstance()
        val minutosAhora = ahora.get(Calendar.HOUR_OF_DAY) * 60 + ahora.get(Calendar.MINUTE)

        val horarios = db.horarioDao().getAllHorariosOnce()

        horarios.forEach { horario ->
            val partes = horario.hora.split(":")
            if (partes.size != 2) return@forEach
            val horaProgramada = partes[0].toIntOrNull() ?: return@forEach
            val minutoProgramado = partes[1].toIntOrNull() ?: return@forEach
            val minutosProgramados = horaProgramada * 60 + minutoProgramado

            if (minutosAhora < minutosProgramados) return@forEach // aún no es la hora

            val ultimoRegistro = db.registroAdministracionDao().getUltimoRegistroOnce(horario.id)
            val yaAdministradoHoy = ultimoRegistro?.estado == "Administrado" &&
                    ultimoRegistro.horaReal != null &&
                    formatoFecha.format(Date(ultimoRegistro.horaReal)) == hoy

            if (yaAdministradoHoy) return@forEach

            val claveNotificado = "notificado_${horario.id}_$hoy"
            if (prefs.getBoolean(claveNotificado, false)) return@forEach

            val medicamento = db.medicamentoDao().getMedicamentoByIdOnce(horario.medicamentoId)
            val paciente = db.pacienteDao().getPacienteByIdOnce(horario.pacienteId)

            NotificationHelper.mostrarNotificacion(
                context = applicationContext,
                id = horario.id,
                titulo = "Hora de administrar medicamento",
                mensaje = "${paciente?.nombre ?: "Paciente"} — ${medicamento?.nombre ?: "Medicamento"} (${horario.hora})"
            )

            prefs.edit().putBoolean(claveNotificado, true).apply()
        }

        return Result.success()
    }
}