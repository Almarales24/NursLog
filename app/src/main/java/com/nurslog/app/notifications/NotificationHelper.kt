package com.nurslog.app.notifications

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

// Utilidad para gestionar notificaciones de recordatorios de medicación
object NotificationHelper {
    // Identificador único del canal de notificaciones
    private const val CHANNEL_ID = "nurslog_medicacion"

    // Crea el canal de notificaciones (requerido en Android 8+)
    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Define las propiedades del canal
            val canal = NotificationChannel(
                CHANNEL_ID,
                "Recordatorios de medicación",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Avisa cuando es hora de administrar un medicamento"
            }
            // Registra el canal en el sistema
            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(canal)
        }
    }

    // Muestra una notificación al usuario
    fun mostrarNotificacion(context: Context, id: Int, titulo: String, mensaje: String) {
        // Verifica permisos en Android 13+ antes de mostrar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            val permisoConcedido = ActivityCompat.checkSelfPermission(
                context, Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
            if (!permisoConcedido) return
        }

        // Construye la notificación con icono, título y contenido
        val notificacion = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_popup_reminder)
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            // Se cierra automáticamente cuando el usuario la selecciona
            .setAutoCancel(true)
            .build()

        // Muestra la notificación al usuario
        NotificationManagerCompat.from(context).notify(id, notificacion)
    }
}