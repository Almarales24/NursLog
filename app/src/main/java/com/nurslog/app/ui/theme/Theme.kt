package com.nurslog.app.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

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

@Composable
fun NursLogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        typography = NursLogTypography,
        content = content
    )
}