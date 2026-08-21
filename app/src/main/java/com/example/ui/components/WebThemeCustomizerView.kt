package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.entity.ThemeConfigEntity

// Helper to safely parse hex colors
fun parseHexColor(hex: String, defaultColor: Color = Color(0xFF1E3A8A)): Color {
    return try {
        val clean = hex.trim().removePrefix("#")
        when (clean.length) {
            6 -> Color(android.graphics.Color.parseColor("#$clean"))
            8 -> Color(android.graphics.Color.parseColor("#$clean"))
            else -> defaultColor
        }
    } catch (e: Exception) {
        defaultColor
    }
}

data class ThemePreset(
    val name: String,
    val primaryHex: String,
    val secondaryHex: String,
    val bannerUrl: String,
    val welcomeMessage: String
)

@Composable
fun WebThemeCustomizerView(
    themes: List<ThemeConfigEntity>,
    activeTheme: ThemeConfigEntity?,
    onSaveTheme: (id: Long, themeName: String, primaryColorHex: String, secondaryColorHex: String, bannerImageUrl: String, welcomeMessage: String, isActive: Boolean) -> Unit,
    onActivateTheme: (themeId: Long) -> Unit,
    onDeleteTheme: (themeId: Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var themeToEdit by remember { mutableStateOf<ThemeConfigEntity?>(null) }
    var isNewTheme by remember { mutableStateOf(false) }
    var themeToDelete by remember { mutableStateOf<ThemeConfigEntity?>(null) }
    var showSuccessSnackbar by remember { mutableStateOf<String?>(null) }

    val presetThemes = listOf(
        ThemePreset(
            name = "Día de la Madre",
            primaryHex = "#BE185D", // Pink 700
            secondaryHex = "#F59E0B", // Amber
            bannerUrl = "https://images.unsplash.com/photo-1513151233558-d860c5398176?w=800&q=80",
            welcomeMessage = "¡Feliz Día Mamá! Celebramos con postre de cortesía y platillos especiales para toda la familia."
        ),
        ThemePreset(
            name = "Noche Disco & Bar",
            primaryHex = "#7C3AED", // Violet 600
            secondaryHex = "#06B6D4", // Cyan
            bannerUrl = "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800&q=80",
            welcomeMessage = "¡Viernes y Sábado de Noche Disco! Cocteles 2x1, DJ en vivo y la mejor comida de la ciudad."
        ),
        ThemePreset(
            name = "Promoción del Día",
            primaryHex = "#B91C1C", // Red 700
            secondaryHex = "#FBBF24", // Yellow
            bannerUrl = "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&q=80",
            welcomeMessage = "¡Súper Promoción! 25% de descuento en todos los platillos principales ordenando por la Web."
        ),
        ThemePreset(
            name = "Azul Rivera Clásico",
            primaryHex = "#1E3A8A", // Blue 900
            secondaryHex = "#D97706", // Amber 600
            bannerUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&q=80",
            welcomeMessage = "¡Bienvenidos a Restaurante Rivera! Auténtica gastronomía guatemalteca con ingredientes de alta calidad."
        ),
        ThemePreset(
            name = "Dorado Chapín Gourmet",
            primaryHex = "#92400E", // Amber 800
            secondaryHex = "#10B981", // Emerald
            bannerUrl = "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=800&q=80",
            welcomeMessage = "Tradición, sabor y excelencia culinaria en cada receta ancestral de nuestra tierra."
        )
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Personalización Web & Temas",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Controla la imagen de portada, colores y promociones de la web en tiempo real",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = {
                    isNewTheme = true
                    themeToEdit = ThemeConfigEntity(
                        themeName = "Nuevo Tema Especial",
                        primaryColorHex = "#1E3A8A",
                        secondaryColorHex = "#D97706",
                        bannerImageUrl = "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&q=80",
                        welcomeMessage = "¡Bienvenidos a Restaurante Rivera! Descubre nuestras deliciosas especialidades.",
                        isActive = false
                    )
                },
                modifier = Modifier.testTag("btn_crear_tema_web")
            ) {
                Icon(Icons.Default.Palette, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Crear Tema")
            }
        }

        // Active Theme Showcase & Real-Time Status Card
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)
            ),
            border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF16A34A),
                            modifier = Modifier.size(12.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "TEMA ACTIVO EN LA WEB",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.ExtraBold,
                                letterSpacing = 1.sp
                            ),
                            color = Color(0xFF15803D)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Text(
                            text = "Sincronizado con Firestore",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.White,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                val current = activeTheme ?: themes.firstOrNull { it.isActive } ?: themes.firstOrNull()
                if (current != null) {
                    val pColor = parseHexColor(current.primaryColorHex)
                    val sColor = parseHexColor(current.secondaryColorHex)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Thumbnail of active theme banner
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.DarkGray)
                        ) {
                            AsyncImage(
                                model = current.bannerImageUrl,
                                contentDescription = current.themeName,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = current.themeName,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = current.welcomeMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Colores Web: ", style = MaterialTheme.typography.labelSmall)
                                Surface(
                                    shape = CircleShape,
                                    color = pColor,
                                    border = BorderStroke(1.dp, Color.White),
                                    modifier = Modifier.size(18.dp)
                                ) {}
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(current.primaryColorHex, style = MaterialTheme.typography.labelSmall)
                                Spacer(modifier = Modifier.width(8.dp))
                                Surface(
                                    shape = CircleShape,
                                    color = sColor,
                                    border = BorderStroke(1.dp, Color.White),
                                    modifier = Modifier.size(18.dp)
                                ) {}
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(current.secondaryColorHex, style = MaterialTheme.typography.labelSmall)
                            }
                        }

                        IconButton(onClick = {
                            isNewTheme = false
                            themeToEdit = current
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar Tema Activo", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                } else {
                    Text(
                        text = "No hay temas configurados aún. ¡Crea uno abajo!",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Section: Plantillas Rápidas (Presets)
        Text(
            text = "✨ Plantillas Temáticas Recomendadas",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            items(presetThemes) { preset ->
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
                    modifier = Modifier
                        .width(220.dp)
                        .clickable {
                            isNewTheme = true
                            themeToEdit = ThemeConfigEntity(
                                themeName = preset.name,
                                primaryColorHex = preset.primaryHex,
                                secondaryColorHex = preset.secondaryHex,
                                bannerImageUrl = preset.bannerUrl,
                                welcomeMessage = preset.welcomeMessage,
                                isActive = false
                            )
                        }
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.DarkGray)
                        ) {
                            AsyncImage(
                                model = preset.bannerUrl,
                                contentDescription = preset.name,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = preset.name,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = parseHexColor(preset.primaryHex),
                                modifier = Modifier.size(14.dp)
                            ) {}
                            Spacer(modifier = Modifier.width(4.dp))
                            Surface(
                                shape = CircleShape,
                                color = parseHexColor(preset.secondaryHex),
                                modifier = Modifier.size(14.dp)
                            ) {}
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Usar plantilla",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        // Section: Lista de Temas Creados
        Text(
            text = "📚 Temas Guardados (${themes.size})",
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
        )

        themes.forEach { theme ->
            ThemeItemCard(
                theme = theme,
                isActive = theme.isActive,
                onActivateClick = {
                    onActivateTheme(theme.id)
                    showSuccessSnackbar = "¡Tema '${theme.themeName}' activado para la Web!"
                },
                onEditClick = {
                    isNewTheme = false
                    themeToEdit = theme
                },
                onDeleteClick = {
                    themeToDelete = theme
                }
            )
        }
    }

    // Modal Editor Dialog
    themeToEdit?.let { currentTheme ->
        AddEditThemeDialog(
            theme = currentTheme,
            isNew = isNewTheme,
            onDismiss = { themeToEdit = null },
            onSave = { id, name, pColor, sColor, banner, welcome, active ->
                onSaveTheme(id, name, pColor, sColor, banner, welcome, active)
                themeToEdit = null
            }
        )
    }

    // Delete Theme Dialog
    themeToDelete?.let { theme ->
        AlertDialog(
            onDismissRequest = { themeToDelete = null },
            icon = { Icon(Icons.Default.DeleteForever, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("¿Eliminar Tema '${theme.themeName}'?") },
            text = {
                Text("Esta acción eliminará el tema permanentemente del POS y de Firestore.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteTheme(theme.id)
                        themeToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { themeToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun ThemeItemCard(
    theme: ThemeConfigEntity,
    isActive: Boolean,
    onActivateClick: () -> Unit,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val pColor = parseHexColor(theme.primaryColorHex)
    val sColor = parseHexColor(theme.secondaryColorHex)

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isActive) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isActive) 3.dp else 1.dp),
        border = BorderStroke(
            if (isActive) 2.dp else 1.dp,
            if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Banner Preview
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.DarkGray)
            ) {
                AsyncImage(
                    model = theme.bannerImageUrl,
                    contentDescription = theme.themeName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            // Details
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = theme.themeName,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (isActive) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = Color(0xFF15803D)
                        ) {
                            Text(
                                text = "ACTIVO",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = theme.welcomeMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = pColor, modifier = Modifier.size(16.dp)) {}
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(theme.primaryColorHex, style = MaterialTheme.typography.labelSmall)
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(shape = CircleShape, color = sColor, modifier = Modifier.size(16.dp)) {}
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(theme.secondaryColorHex, style = MaterialTheme.typography.labelSmall)
                }
            }

            // Actions
            Column(horizontalAlignment = Alignment.End, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                if (!isActive) {
                    Button(
                        onClick = onActivateClick,
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Activar", fontSize = 12.sp)
                    }
                }

                Row {
                    IconButton(onClick = onEditClick, modifier = Modifier.size(34.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    }
                    if (!isActive) {
                        IconButton(onClick = onDeleteClick, modifier = Modifier.size(34.dp)) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditThemeDialog(
    theme: ThemeConfigEntity,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (id: Long, themeName: String, primaryColorHex: String, secondaryColorHex: String, bannerImageUrl: String, welcomeMessage: String, isActive: Boolean) -> Unit
) {
    var themeName by remember { mutableStateOf(theme.themeName) }
    var primaryHex by remember { mutableStateOf(theme.primaryColorHex) }
    var secondaryHex by remember { mutableStateOf(theme.secondaryColorHex) }
    var bannerUrl by remember { mutableStateOf(theme.bannerImageUrl) }
    var welcomeMessage by remember { mutableStateOf(theme.welcomeMessage) }
    var isActive by remember { mutableStateOf(theme.isActive) }

    val colorPresets = listOf(
        Pair("Azul Rivera", "#1E3A8A"),
        Pair("Rosa Mamá", "#BE185D"),
        Pair("Púrpura Disco", "#7C3AED"),
        Pair("Rojo Promoción", "#B91C1C"),
        Pair("Ámbar Gourmet", "#92400E"),
        Pair("Verde Esmeralda", "#047857"),
        Pair("Negro Elegante", "#18181B"),
        Pair("Dorado Chapín", "#D97706")
    )

    val bannerPresets = listOf(
        Pair("Restaurante Principal", "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800&q=80"),
        Pair("Día de la Madre / Flores", "https://images.unsplash.com/photo-1513151233558-d860c5398176?w=800&q=80"),
        Pair("Noche Disco / Cócteles", "https://images.unsplash.com/photo-1514525253161-7a46d19cd819?w=800&q=80"),
        Pair("Churrascos y Parrilla", "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=800&q=80"),
        Pair("Gastronomía Gourmet", "https://images.unsplash.com/photo-1555396273-367ea4eb4db5?w=800&q=80"),
        Pair("Bebidas y Fiesta", "https://images.unsplash.com/photo-1572116469696-31de0f17cc34?w=800&q=80")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Palette, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isNew) "Crear Nuevo Tema Web" else "Editar Tema Web")
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Theme Name
                OutlinedTextField(
                    value = themeName,
                    onValueChange = { themeName = it },
                    label = { Text("Nombre del Tema *") },
                    placeholder = { Text("Ej. Día de la Madre, Noche Disco, Promoción...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Welcome Message
                OutlinedTextField(
                    value = welcomeMessage,
                    onValueChange = { welcomeMessage = it },
                    label = { Text("Texto de Bienvenida / Promoción *") },
                    placeholder = { Text("¡Celebra con nosotros! Disfruta 2x1 en cócteles...") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                // Primary Color
                Text("Color Primario (Encabezados, Botones Principales):", style = MaterialTheme.typography.labelMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = parseHexColor(primaryHex),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.size(48.dp)
                    ) {}

                    OutlinedTextField(
                        value = primaryHex,
                        onValueChange = { primaryHex = it },
                        label = { Text("Código Hexadecimal") },
                        placeholder = { Text("#1E3A8A") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Palette quick pick for Primary
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(colorPresets) { (name, hex) ->
                        AssistChip(
                            onClick = { primaryHex = hex },
                            label = { Text(name, fontSize = 11.sp) },
                            leadingIcon = {
                                Surface(shape = CircleShape, color = parseHexColor(hex), modifier = Modifier.size(12.dp)) {}
                            }
                        )
                    }
                }

                // Secondary Color
                Text("Color Secundario / Acento (Precios, Badges):", style = MaterialTheme.typography.labelMedium)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = parseHexColor(secondaryHex),
                        border = BorderStroke(1.5.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier.size(48.dp)
                    ) {}

                    OutlinedTextField(
                        value = secondaryHex,
                        onValueChange = { secondaryHex = it },
                        label = { Text("Código Hexadecimal Secundario") },
                        placeholder = { Text("#D97706") },
                        singleLine = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Banner URL
                Text("Imagen Principal / Banner del Menú Web:", style = MaterialTheme.typography.labelMedium)
                OutlinedTextField(
                    value = bannerUrl,
                    onValueChange = { bannerUrl = it },
                    label = { Text("URL de la Imagen Banner (HTTPS)") },
                    placeholder = { Text("https://images.unsplash.com/...") },
                    leadingIcon = { Icon(Icons.Default.Image, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Banner Presets
                Text("Banners temáticos disponibles:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(bannerPresets) { (presetTitle, url) ->
                        AssistChip(
                            onClick = { bannerUrl = url },
                            label = { Text(presetTitle, fontSize = 11.sp) },
                            leadingIcon = { Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(14.dp)) }
                        )
                    }
                }

                // LIVE SMARTPHONE WEB MOCKUP PREVIEW
                Text("📱 Vista Previa en Vivo (Menú Web del Cliente):", style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold))
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                    border = BorderStroke(2.dp, parseHexColor(primaryHex)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        // Phone Status Bar Simulation
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(22.dp)
                                .background(parseHexColor(primaryHex)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("🌐 https://restaurante-rivera.web.app", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp)
                        }

                        // Banner Hero
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(120.dp)
                                .background(Color.DarkGray)
                        ) {
                            if (bannerUrl.isNotBlank()) {
                                AsyncImage(
                                    model = bannerUrl,
                                    contentDescription = "Banner Preview",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.fillMaxSize()
                                )
                            }
                            // Gradient Overlay
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.verticalGradient(
                                            listOf(Color.Transparent, parseHexColor(primaryHex).copy(alpha = 0.85f))
                                        )
                                    )
                            )
                            Column(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .padding(10.dp)
                            ) {
                                Text(
                                    text = themeName.ifBlank { "Restaurante Rivera" },
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        color = Color.White
                                    )
                                )
                                Text(
                                    text = welcomeMessage.ifBlank { "¡Bienvenidos a nuestro menú digital!" },
                                    style = MaterialTheme.typography.bodySmall.copy(color = Color.White.copy(alpha = 0.9f)),
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }

                        // Sample Category Chips inside Mockup
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = parseHexColor(primaryHex)
                            ) {
                                Text("Platillos", color = Color.White, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, Color.LightGray)
                            ) {
                                Text("Bebidas", color = Color.Black, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = Color.White,
                                border = BorderStroke(1.dp, Color.LightGray)
                            ) {
                                Text("Postres", color = Color.Black, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                            }
                        }

                        // Sample Mock Product Card
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Pepián Especial", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("Q65.00", color = parseHexColor(secondaryHex), fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                                }
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = parseHexColor(primaryHex)
                                ) {
                                    Text("+ Agregar", color = Color.White, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                HorizontalDivider()

                // Mark as Active Switch
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Activar este tema ahora", fontWeight = FontWeight.SemiBold)
                        Text("Se aplicará inmediatamente a los clientes en el Menú Web / QR", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                    }
                    Switch(checked = isActive, onCheckedChange = { isActive = it })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (themeName.isNotBlank()) {
                        onSave(theme.id, themeName.trim(), primaryHex.trim(), secondaryHex.trim(), bannerUrl.trim(), welcomeMessage.trim(), isActive)
                    }
                },
                enabled = themeName.isNotBlank()
            ) {
                Text(if (isNew) "Guardar y Publicar Tema" else "Guardar Cambios")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
