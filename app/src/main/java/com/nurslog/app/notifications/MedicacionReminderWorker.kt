package com.nurslog.app.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.nurslog.app.data.local.NursLogDatabase
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Worker que revisa periódicamente si hay medicamentos pendientes de administrar y notifica
class MedicacionReminderWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params) {

    // Ejecuta la verificación de medicamentos pendientes
    override suspend fun doWork(): Result {
        // Obtiene la base de datos
        val db = NursLogDatabase.getDatabase(applicationContext)
        // Obtiene preferencias compartidas para rastrear notificaciones enviadas hoy
        val prefs = applicationContext.getSharedPreferences("nurslog_notificaciones", Context.MODE_PRIVATE)
        // Formato de fecha para comparación (sin hora)
        val formatoFecha = SimpleDateFormat("yyyyMMdd", Locale.getDefault())
        val hoy = formatoFecha.format(Date())

        // Obtiene la hora actual en minutos desde medianoche
        val ahora = Calendar.getInstance()
        val minutosAhora = ahora.get(Calendar.HOUR_OF_DAY) * 60 + ahora.get(Calendar.MINUTE)

        // Obtiene todos los horarios programados
        val horarios = db.horarioDao().getAllHorariosOnce()

        // Itera sobre cada horario para verificar si debe notificar
        horarios.forEach { horario ->
            // Parsea la hora programada (formato "HH:mm")
            val partes = horario.hora.split(":")
            if (partes.size != 2) return@forEach
            val horaProgramada = partes[0].toIntOrNull() ?: return@forEach
            val minutoProgramado = partes[1].toIntOrNull() ?: return@forEach
            val minutosProgramados = horaProgramada * 60 + minutoProgramado

            // Si aún no es la hora, salta este horario
            if (minutosAhora < minutosProgramados) return@forEach

            // Verifica si ya fue administrado hoy
            val ultimoRegistro = db.registroAdministracionDao().getUltimoRegistroOnce(horario.id)
            val yaAdministradoHoy = ultimoRegistro?.estado == "Administrado" &&
                    ultimoRegistro.horaReal != null &&
                    formatoFecha.format(Date(ultimoRegistro.horaReal)) == hoy

            // Si ya fue administrado, salta la notificación
            if (yaAdministradoHoy) return@forEach

            // Verifica si ya se envió notificación hoy para este horario
            val claveNotificado = "notificado_${horario.id}_$hoy"
            if (prefs.getBoolean(claveNotificado, false)) return@forEach

            // Obtiene datos del medicamento y paciente
            val medicamento = db.medicamentoDao().getMedicamentoByIdOnce(horario.medicamentoId)
            val paciente = db.pacienteDao().getPacienteByIdOnce(horario.pacienteId)

            // Muestra la notificación con los datos del paciente y medicamento
            NotificationHelper.mostrarNotificacion(
                context = applicationContext,
                id = horario.id,
                titulo = "Hora de administrar medicamento",
                mensaje = "${paciente?.nombre ?: "Paciente"} — ${medicamento?.nombre ?: "Medicamento"} (${horario.hora})"
            )

            // Marca la notificación como enviada hoy
            prefs.edit().putBoolean(claveNotificado, true).apply()
        }

        // Retorna éxito (WorkManager lo reintentará si falla)
        return Result.success()
    }
}