package com.nurslog.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// Paleta de colores para modo claro basada en los colores definidos
private val LightColors = lightColorScheme(
    primary = PrimarioClaro,
    onPrimary = SuperficieClaro,
    background = FondoClaro,
    surface = SuperficieClaro,
    onBackground = TextoPrincipalClaro,
    onSurface = TextoPrincipalClaro,
    secondary = TextoSecundarioClaro,
    error = AlertaClaro,
    tertiary = ExitoClaro
)

// Paleta de colores para modo oscuro basada en los colores definidos
private val DarkColors = darkColorScheme(
    primary = PrimarioOscuro,
    onPrimary = FondoOscuro,
    background = FondoOscuro,
    surface = SuperficieOscuro,
    onBackground = TextoPrincipalOscuro,
    onSurface = TextoPrincipalOscuro,
    secondary = TextoSecundarioOscuro,
    error = AlertaOscuro,
    tertiary = ExitoOscuro
)

// Composable que aplica el tema a la aplicación según preferencia del sistema
@Composable
fun NursLogTheme(
    // Detecta automáticamente el modo oscuro del sistema si no se especifica
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // Selecciona la paleta según el tema
    val colorScheme = if (darkTheme) DarkColors else LightColors

    // Aplica el MaterialTheme con los colores y tipografía personalizados
    MaterialTheme(
        colorScheme = colorScheme,
        typography = NursLogTypography,
        content = content
    )
}