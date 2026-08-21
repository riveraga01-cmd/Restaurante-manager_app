package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private fun getLightPalette(paletteCode: String) = when (paletteCode) {
    "AZUL_RIVERA" -> lightColorScheme(
        primary = AzulRiveraPrimary,
        secondary = AzulRiveraSecondary,
        tertiary = AzulRiveraTertiary,
        primaryContainer = AzulRiveraPrimaryContainer,
        secondaryContainer = Color(0xFFBAE6FD),
        background = AzulRiveraBackground,
        surface = AzulRiveraSurface,
        surfaceVariant = AzulRiveraSurfaceVariant,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onPrimaryContainer = AzulRiveraOnPrimaryContainer,
        onSecondaryContainer = Color(0xFF075985),
        onBackground = Color(0xFF0F172A),
        onSurface = Color(0xFF0F172A),
        onSurfaceVariant = Color(0xFF475569),
        outline = AzulRiveraOutline
    )
    "VERDE_QUETZAL" -> lightColorScheme(
        primary = VerdeQuetzalPrimary,
        secondary = VerdeQuetzalSecondary,
        tertiary = VerdeQuetzalTertiary,
        primaryContainer = VerdeQuetzalPrimaryContainer,
        secondaryContainer = Color(0xFFA7F3D0),
        background = VerdeQuetzalBackground,
        surface = VerdeQuetzalSurface,
        surfaceVariant = VerdeQuetzalSurfaceVariant,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onPrimaryContainer = VerdeQuetzalOnPrimaryContainer,
        onSecondaryContainer = Color(0xFF065F46),
        onBackground = Color(0xFF132219),
        onSurface = Color(0xFF132219),
        onSurfaceVariant = Color(0xFF435A4D),
        outline = VerdeQuetzalOutline
    )
    "CAOBA_LAGO" -> lightColorScheme(
        primary = CaobaLagoPrimary,
        secondary = CaobaLagoSecondary,
        tertiary = CaobaLagoTertiary,
        primaryContainer = CaobaLagoPrimaryContainer,
        secondaryContainer = Color(0xFFFDE68A),
        background = CaobaLagoBackground,
        surface = CaobaLagoSurface,
        surfaceVariant = CaobaLagoSurfaceVariant,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onPrimaryContainer = CaobaLagoOnPrimaryContainer,
        onSecondaryContainer = Color(0xFF78350F),
        onBackground = Color(0xFF271A12),
        onSurface = Color(0xFF271A12),
        onSurfaceVariant = Color(0xFF6B584C),
        outline = CaobaLagoOutline
    )
    "GRIS_MINIMALISTA" -> lightColorScheme(
        primary = GrisMinimalistaPrimary, // Header: #1F2937 (Gris Oscuro)
        secondary = GrisMinimalistaSecondary, // Primario: #111827 (Negro)
        tertiary = GrisMinimalistaTertiary, // #6B7280 (Gris Medio)
        primaryContainer = GrisMinimalistaPrimaryContainer,
        secondaryContainer = Color(0xFFE5E7EB),
        background = GrisMinimalistaBackground, // #F9FAFB (Blanco roto)
        surface = GrisMinimalistaSurface, // #FFFFFF (Blanco)
        surfaceVariant = GrisMinimalistaSurfaceVariant,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onPrimaryContainer = Color(0xFF111827),
        onSecondaryContainer = Color(0xFF374151),
        onBackground = Color(0xFF111827), // #111827 (Texto)
        onSurface = Color(0xFF111827),
        onSurfaceVariant = Color(0xFF4B5563),
        outline = GrisMinimalistaOutline // #E5E7EB (Borde)
    )
    else -> lightColorScheme( // "DORADO_CHAPIN" (Elegante Tradicional)
        primary = DoradoChapinPrimary,
        secondary = DoradoChapinSecondary,
        tertiary = DoradoChapinTertiary,
        primaryContainer = DoradoChapinPrimaryContainer,
        secondaryContainer = RestaurantSecondaryContainer,
        background = DoradoChapinBackground,
        surface = DoradoChapinSurface,
        surfaceVariant = DoradoChapinSurfaceVariant,
        onPrimary = Color.White,
        onSecondary = Color.White,
        onPrimaryContainer = DoradoChapinOnPrimaryContainer,
        onSecondaryContainer = Color(0xFF452200),
        onBackground = TextPrimary,
        onSurface = TextPrimary,
        onSurfaceVariant = TextSecondary,
        outline = DoradoChapinOutline
    )
}

private fun getDarkPalette(paletteCode: String) = when (paletteCode) {
    "AZUL_RIVERA" -> darkColorScheme(
        primary = Color(0xFF60A5FA),
        secondary = Color(0xFF93C5FD),
        tertiary = Color(0xFF3B82F6),
        background = Color(0xFF0B1120),
        surface = Color(0xFF131C31),
        surfaceVariant = Color(0xFF1E293B),
        onPrimary = Color(0xFF002244),
        onSecondary = Color(0xFF002A45),
        onBackground = Color(0xFFF1F5F9),
        onSurface = Color(0xFFF1F5F9),
        onSurfaceVariant = Color(0xFF94A3B8),
        primaryContainer = Color(0xFF1E3A8A),
        onPrimaryContainer = Color(0xFFDBEAFE),
        secondaryContainer = Color(0xFF1D4ED8),
        onSecondaryContainer = Color(0xFFBFDBFE),
        outline = Color(0xFF334155)
    )
    "VERDE_QUETZAL" -> darkColorScheme(
        primary = Color(0xFF34D399),
        secondary = Color(0xFF6EE7B7),
        tertiary = Color(0xFF10B981),
        background = Color(0xFF061A12),
        surface = Color(0xFF0B291D),
        surfaceVariant = Color(0xFF133E2D),
        onPrimary = Color(0xFF003822),
        onSecondary = Color(0xFF00301D),
        onBackground = Color(0xFFECFDF5),
        onSurface = Color(0xFFECFDF5),
        onSurfaceVariant = Color(0xFFA7F3D0),
        primaryContainer = Color(0xFF065F46),
        onPrimaryContainer = Color(0xFFD1FAE5),
        secondaryContainer = Color(0xFF047857),
        onSecondaryContainer = Color(0xFFA7F3D0),
        outline = Color(0xFF1E5B42)
    )
    "CAOBA_LAGO" -> darkColorScheme(
        primary = Color(0xFFFB923C),
        secondary = Color(0xFFFDBA74),
        tertiary = Color(0xFFEA580C),
        background = Color(0xFF180E09),
        surface = Color(0xFF27150E),
        surfaceVariant = Color(0xFF3A2016),
        onPrimary = Color(0xFF431407),
        onSecondary = Color(0xFF3D1500),
        onBackground = Color(0xFFFEF2E8),
        onSurface = Color(0xFFFEF2E8),
        onSurfaceVariant = Color(0xFFD6C3B4),
        primaryContainer = Color(0xFF5C1D06),
        onPrimaryContainer = Color(0xFFFEF2E8),
        secondaryContainer = Color(0xFF7C2D12),
        onSecondaryContainer = Color(0xFFFED7AA),
        outline = Color(0xFF5A392A)
    )
    "GRIS_MINIMALISTA" -> darkColorScheme(
        primary = Color(0xFFF3F4F6),
        secondary = Color(0xFF9CA3AF),
        tertiary = Color(0xFFD1D5DB),
        background = Color(0xFF111827),
        surface = Color(0xFF1F2937),
        surfaceVariant = Color(0xFF374151),
        onPrimary = Color(0xFF111827),
        onSecondary = Color(0xFF111827),
        onBackground = Color(0xFFF9FAFB),
        onSurface = Color(0xFFF9FAFB),
        onSurfaceVariant = Color(0xFFE5E7EB),
        primaryContainer = Color(0xFF374151),
        onPrimaryContainer = Color(0xFFF9FAFB),
        secondaryContainer = Color(0xFF4B5563),
        onSecondaryContainer = Color(0xFFE5E7EB),
        outline = Color(0xFF4B5563)
    )
    else -> darkColorScheme( // "DORADO_CHAPIN" & default
        primary = Color(0xFFE5C07B),
        secondary = Color(0xFFD4AF37),
        tertiary = Color(0xFFF59E0B),
        background = DarkRestaurantBackground,
        surface = DarkRestaurantSurface,
        surfaceVariant = DarkRestaurantSurfaceVariant,
        onPrimary = Color(0xFF422006),
        onSecondary = Color(0xFF472A00),
        onBackground = DarkRestaurantOnSurface,
        onSurface = DarkRestaurantOnSurface,
        onSurfaceVariant = DarkRestaurantOnSurfaceVariant,
        primaryContainer = Color(0xFF5C2F0E),
        onPrimaryContainer = Color(0xFFFDF6E2),
        secondaryContainer = Color(0xFF683D00),
        onSecondaryContainer = Color(0xFFFFDEAC),
        outline = DarkRestaurantOutline
    )
}

@Composable
fun RestauranteTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    paletteCode: String = "AZUL_RIVERA",
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> getDarkPalette(paletteCode)
        else -> getLightPalette(paletteCode)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

