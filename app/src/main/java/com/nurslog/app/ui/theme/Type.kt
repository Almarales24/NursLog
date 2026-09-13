package com.nurslog.app.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Define la tipografía personalizada para la aplicación
// Usa las fuentes por defecto del sistema (sans-serif para texto legible y monospace para datos numéricos)
val NursLogTypography = Typography(
    // Título grande: encabezados principales (ej: nombre de sección)
    titleLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    // Título mediano: subtítulos y encabezados secundarios
    titleMedium = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.SemiBold,
        fontSize = 16.sp
    ),
    // Cuerpo grande: textos descriptivos principales
    bodyLarge = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    // Cuerpo mediano: datos numéricos, dosis, horas (tipografía monoespaciada para alineación)
    bodyMedium = TextStyle(
        fontFamily = FontFamily.Monospace,
        fontWeight = FontWeight.Bold,
        fontSize = 18.sp
    ),
    // Etiqueta pequeña: textos auxiliares y notas de pie
    labelSmall = TextStyle(
        fontFamily = FontFamily.SansSerif,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
)