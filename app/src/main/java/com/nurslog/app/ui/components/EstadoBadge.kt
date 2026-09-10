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

@Composable
fun EstadoBadge(
    texto: String,
    modifier: Modifier = Modifier
) {
    val color = when (texto.lowercase()) {
        "activo", "severa" -> MaterialTheme.colorScheme.error
        "estable", "moderada" -> MaterialTheme.colorScheme.tertiary
        "leve", "resuelto" -> MaterialTheme.colorScheme.secondary
        else -> MaterialTheme.colorScheme.secondary
    }

    Text(
        text = texto,
        color = Color.White,
        style = MaterialTheme.typography.labelSmall,
        modifier = modifier
            .background(color, RoundedCornerShape(6.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    )
}