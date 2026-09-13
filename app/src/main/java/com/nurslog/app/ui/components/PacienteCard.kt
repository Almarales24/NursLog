package com.nurslog.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.dp
import com.nurslog.app.data.entity.Paciente

// Componente de tarjeta que muestra información del paciente con interacción animada
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PacienteCard(
    // Paciente a mostrar
    paciente: Paciente,
    // Callback al hacer clic normal
    onClick: (Paciente) -> Unit,
    // Callback al hacer clic prolongado
    onLongClick: (Paciente) -> Unit = {},
    modifier: Modifier = Modifier
) {
    // Tracking del estado de presión del usuario
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    // Animación de escala al presionar (efecto visual de presión)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.97f else 1f,
        label = "cardScale"
    )

    // Tarjeta que contiene la información del paciente
    Card(
        modifier = modifier
            .fillMaxWidth()
            // Aplica la animación de escala
            .graphicsLayer { scaleX = scale; scaleY = scale }
            // Detecta clics normales y prolongados sin efecto visual por defecto
            .combinedClickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = { onClick(paciente) },
                onLongClick = { onLongClick(paciente) }
            ),
        // Color de fondo de la tarjeta
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        // Contenido de la tarjeta
        Column(modifier = Modifier.padding(16.dp)) {
            // Nombre del paciente como título
            Text(
                text = paciente.nombre,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            // Información de ubicación (cama y sala)
            Text(
                text = "Cama ${paciente.cama} · Sala ${paciente.sala}",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary
            )
        }
    }
}