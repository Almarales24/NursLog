package com.nurslog.app.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

// Botón personalizado con animación de escala al presionar
@Composable
fun AnimatedButton(
    // Acción al hacer clic
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    // Si está habilitado o deshabilitado
    enabled: Boolean = true,
    // Colores personalizados del botón
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    // Contenido del botón (etiqueta, icono, etc.)
    content: @Composable () -> Unit
) {
    // Tracking del estado de presión
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    // Animación de escala (se encoge al presionar)
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = "buttonScale"
    )

    // Botón con animación de escala
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = colors,
        interactionSource = interactionSource,
        modifier = modifier.graphicsLayer {
            // Aplica la transformación de escala
            scaleX = scale
            scaleY = scale
        }
    ) {
        content()
    }
}