package com.nurslog.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

// Contenedor que permite deslizar un elemento hacia la izquierda para eliminarlo
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteItem(
    // Callback cuando el usuario desliza y confirma la eliminación
    onDelete: () -> Unit,
    modifier: Modifier = Modifier,
    // Contenido del elemento que se puede deslizar
    content: @Composable () -> Unit
) {
    // Estado que rastrea la posición del deslizamiento
    val estadoDeslizar = rememberSwipeToDismissBoxState(
        confirmValueChange = { valor ->
            // Si se desliza de derecha a izquierda, ejecuta eliminación
            if (valor == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else {
                false
            }
        }
    )

    // Contenedor con capacidad de deslizamiento
    SwipeToDismissBox(
        state = estadoDeslizar,
        modifier = modifier,
        // Solo permite deslizar de derecha a izquierda (no de izquierda a derecha)
        enableDismissFromStartToEnd = false,
        // Contenido de fondo que se muestra al deslizar
        backgroundContent = {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    // Fondo rojo para indicar peligro/eliminación
                    .background(MaterialTheme.colorScheme.error)
                    .padding(horizontal = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                // Icono de basura blanco
                Icon(
                    imageVector = Icons.Filled.Delete,
                    contentDescription = "Eliminar",
                    tint = Color.White
                )
            }
        }
    ) {
        content()
    }
}