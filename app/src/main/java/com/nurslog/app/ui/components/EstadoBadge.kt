package com.nurslog.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Componente que muestra un estado o etiqueta de severidad con código de color
@Composable
fun EstadoBadge(
    // Texto a mostrar en la insignia
    texto: String,
    modifier: Modifier = Modifier
) {
    // Selecciona el color según el estado (rojo crítico, verde OK, etc.)
    val color = when (texto.lowercase()) {
        // Estados críticos/activos en rojo
        "activo", "severa" -> MaterialTheme.colorScheme.error
        // Estados moderados/estables en verde
        "estable", "moderada" -> MaterialTheme.colorScheme.tertiary
        // Estados leves/resueltos en gris
        "leve", "resuelto" -> MaterialTheme.colorScheme.secondary
        // Color por defecto si no coincide
        else -> MaterialTheme.colorScheme.secondary
    }

    // Texto centrado con fondo redondeado y color determinado
    Text(
        text = texto,
        color = Color.White,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
            // Fondo redondeado con el color seleccionado
            .background(color, RoundedCornerShape(6.dp))
            // Espaciado interno
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}