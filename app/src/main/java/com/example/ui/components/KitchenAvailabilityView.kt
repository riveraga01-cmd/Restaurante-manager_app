package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.entity.MenuItemEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.RestaurantViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitchenAvailabilityView(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val allMenuItems by viewModel.allMenuItems.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var statusFilter by remember { mutableStateOf("TODOS") } // "TODOS", "DISPONIBLE", "AGOTADO"
    var showWebSimulator by remember { mutableStateOf(false) }
    var lastToggledItemMessage by remember { mutableStateOf<String?>(null) }

    val categories = remember(allMenuItems) {
        listOf("Todos") + allMenuItems.map { it.category }.distinct().sorted()
    }

    val availableCount = remember(allMenuItems) { allMenuItems.count { it.isAvailable } }
    val outOfStockCount = remember(allMenuItems) { allMenuItems.count { !it.isAvailable } }

    val filteredItems = remember(allMenuItems, searchQuery, selectedCategory, statusFilter) {
        allMenuItems.filter { item ->
            val matchSearch = searchQuery.isBlank() ||
                    item.name.contains(searchQuery, ignoreCase = true) ||
                    item.description.contains(searchQuery, ignoreCase = true) ||
                    item.category.contains(searchQuery, ignoreCase = true)

            val matchCategory = selectedCategory == "Todos" || item.category.equals(selectedCategory, ignoreCase = true)

            val matchStatus = when (statusFilter) {
                "DISPONIBLE" -> item.isAvailable
                "AGOTADO" -> !item.isAvailable
                else -> true
            }

            matchSearch && matchCategory && matchStatus
        }.sortedWith(
            compareBy<MenuItemEntity> { it.isAvailable } // Show Agotados on top or ordered cleanly
                .thenBy { it.category }
                .thenBy { it.name }
        )
    }

    // Auto-clear notification message after delay
    LaunchedEffect(lastToggledItemMessage) {
        if (lastToggledItemMessage != null) {
            kotlinx.coroutines.delay(3500)
            lastToggledItemMessage = null
        }
    }

    if (showWebSimulator) {
        WebMenuSimulatorDialog(
            initialTable = "Mesa 1",
            onDismiss = { showWebSimulator = false }
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = 4.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // --- 1. BANNER DE CONTROL RÁPIDO & MÉTRICAS ---
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, CardBorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (outOfStockCount > 0) Color(0xFFFEE2E2) else Color(0xFFDCFCE7),
                            modifier = Modifier.size(42.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = if (outOfStockCount > 0) Icons.Default.Block else Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = if (outOfStockCount > 0) Color(0xFFDC2626) else Color(0xFF16A34A),
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                        Column {
                            Text(
                                text = "Control de Disponibilidad en Cocina",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = "Actualización en tiempo real con Menú Web y POS",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Button to test/simulate Web Menu
                    FilledTonalButton(
                        onClick = { showWebSimulator = true },
                        shape = RoundedCornerShape(10.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                        modifier = Modifier.testTag("btn_simular_web_desde_cocina")
                    ) {
                        Icon(Icons.Default.Language, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Ver Menú Web", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Summary Chips Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Total Chip
                    FilterChip(
                        selected = statusFilter == "TODOS",
                        onClick = { statusFilter = "TODOS" },
                        label = { Text("Total: ${allMenuItems.size}", fontWeight = FontWeight.SemiBold, fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.RestaurantMenu, contentDescription = null, modifier = Modifier.size(14.dp)) },
                        modifier = Modifier.testTag("filter_todos_status")
                    )

                    // Disponibles Chip
                    FilterChip(
                        selected = statusFilter == "DISPONIBLE",
                        onClick = { statusFilter = "DISPONIBLE" },
                        label = {
                            Text(
                                "$availableCount Disponibles",
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF16A34A),
                                fontSize = 12.sp
                            )
                        },
                        leadingIcon = {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFF16A34A),
                                modifier = Modifier.size(8.dp)
                            ) {}
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFDCFCE7),
                            selectedLabelColor = Color(0xFF166534)
                        ),
                        modifier = Modifier.testTag("filter_disponibles_status")
                    )

                    // Agotados Chip
                    FilterChip(
                        selected = statusFilter == "AGOTADO",
                        onClick = { statusFilter = "AGOTADO" },
                        label = {
                            Text(
                                "$outOfStockCount Agotados",
                                fontWeight = FontWeight.Bold,
                                color = if (outOfStockCount > 0) Color(0xFFDC2626) else MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp
                            )
                        },
                        leadingIcon = {
                            Surface(
                                shape = CircleShape,
                                color = Color(0xFFDC2626),
                                modifier = Modifier.size(8.dp)
                            ) {}
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFFFEE2E2),
                            selectedLabelColor = Color(0xFF991B1B)
                        ),
                        modifier = Modifier.testTag("filter_agotados_status")
                    )
                }
            }
        }

        // --- 2. INSTANT FEEDBACK ALERT ---
        AnimatedVisibility(
            visible = lastToggledItemMessage != null,
            enter = fadeIn(tween(200)),
            exit = fadeOut(tween(200))
        ) {
            Surface(
                color = Color(0xFF1E293B),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CloudSync,
                        contentDescription = null,
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = lastToggledItemMessage ?: "",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                        color = Color.White,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }

        // --- 3. SEARCH & CATEGORY FILTERS ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_buscar_disponibilidad"),
                placeholder = { Text("Buscar platillo por nombre o categoría...", fontSize = 13.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(20.dp)) },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = "Limpiar", modifier = Modifier.size(18.dp))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
        }

        // Category Horizontal Scroll
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { category ->
                val isSelected = selectedCategory.equals(category, ignoreCase = true)
                FilterChip(
                    selected = isSelected,
                    onClick = { selectedCategory = category },
                    label = { Text(category, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                    modifier = Modifier.testTag("cat_chip_$category")
                )
            }
        }

        // --- 4. LIST OF PLATILLOS WITH INSTANT AVAILABILITY TOGGLES ---
        if (filteredItems.isEmpty()) {
            EmptyStateCard(
                icon = Icons.Default.SearchOff,
                title = "No se encontraron platillos",
                message = "Intenta cambiar el término de búsqueda o la categoría seleccionada.",
                modifier = Modifier.weight(1f)
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    KitchenMenuItemAvailabilityCard(
                        item = item,
                        onToggle = {
                            val newStatus = !item.isAvailable
                            viewModel.toggleMenuItemAvailability(item)
                            val statusText = if (newStatus) "DISPONIBLE (Habilitado)" else "AGOTADO (Bloqueado en Web)"
                            lastToggledItemMessage = "⚡ '${item.name}' marcado como $statusText. Base de datos y Web sincronizadas."
                        }
                    )
                }
            }
        }
    }
}

/**
 * Componente Lateral para Control de Disponibilidad Rápida en Cocina.
 * Se integra como panel lateral (docked o slide-over sheet) permitiendo a los cocineros
 * alternar el estado 'isAvailable' sin salir de la visualización de comandas activas.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitchenAvailabilitySidePanel(
    viewModel: RestaurantViewModel,
    onClose: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val allMenuItems by viewModel.allMenuItems.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var statusFilter by remember { mutableStateOf("TODOS") }
    var lastToggledItemMessage by remember { mutableStateOf<String?>(null) }

    val categories = remember(allMenuItems) {
        listOf("Todos") + allMenuItems.map { it.category }.distinct().sorted()
    }

    val availableCount = remember(allMenuItems) { allMenuItems.count { it.isAvailable } }
    val outOfStockCount = remember(allMenuItems) { allMenuItems.count { !it.isAvailable } }

    val filteredItems = remember(allMenuItems, searchQuery, selectedCategory, statusFilter) {
        allMenuItems.filter { item ->
            val matchSearch = searchQuery.isBlank() ||
                    item.name.contains(searchQuery, ignoreCase = true) ||
                    item.category.contains(searchQuery, ignoreCase = true)
            val matchCategory = selectedCategory == "Todos" || item.category.equals(selectedCategory, ignoreCase = true)
            val matchStatus = when (statusFilter) {
                "DISPONIBLE" -> item.isAvailable
                "AGOTADO" -> !item.isAvailable
                else -> true
            }
            matchSearch && matchCategory && matchStatus
        }.sortedWith(
            compareBy<MenuItemEntity> { it.isAvailable }
                .thenBy { it.category }
                .thenBy { it.name }
        )
    }

    LaunchedEffect(lastToggledItemMessage) {
        if (lastToggledItemMessage != null) {
            kotlinx.coroutines.delay(2800)
            lastToggledItemMessage = null
        }
    }

    Surface(
        color = MaterialTheme.colorScheme.surface,
        modifier = modifier.fillMaxHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = if (outOfStockCount > 0) Color(0xFFFEE2E2) else Color(0xFFDCFCE7),
                        modifier = Modifier.size(34.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = Icons.Default.FlashOn,
                                contentDescription = null,
                                tint = if (outOfStockCount > 0) Color(0xFFDC2626) else Color(0xFF16A34A),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Column {
                        Text(
                            text = "Disponibilidad Rápida",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "$outOfStockCount agotados de ${allMenuItems.size} platillos",
                            style = MaterialTheme.typography.bodySmall,
                            color = if (outOfStockCount > 0) Color(0xFFDC2626) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (onClose != null) {
                    IconButton(onClick = onClose) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar Panel Lateral")
                    }
                }
            }

            // Quick Status Filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                FilterChip(
                    selected = statusFilter == "TODOS",
                    onClick = { statusFilter = "TODOS" },
                    label = { Text("Todos (${allMenuItems.size})", fontSize = 11.sp) },
                    modifier = Modifier.weight(1f)
                )
                FilterChip(
                    selected = statusFilter == "AGOTADO",
                    onClick = { statusFilter = "AGOTADO" },
                    label = {
                        Text(
                            "Agotados ($outOfStockCount)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (outOfStockCount > 0) Color(0xFFDC2626) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = Color(0xFFFEE2E2),
                        selectedLabelColor = Color(0xFF991B1B)
                    ),
                    modifier = Modifier.weight(1f)
                )
            }

            // Search Bar
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Buscar en menú...", fontSize = 12.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp)) },
                trailingIcon = {
                    if (searchQuery.isNotBlank()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Clear, contentDescription = null, modifier = Modifier.size(16.dp))
                        }
                    }
                },
                singleLine = true,
                shape = RoundedCornerShape(10.dp)
            )

            // Category Chips Row
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(categories) { cat ->
                    val isSelected = selectedCategory.equals(cat, ignoreCase = true)
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = cat },
                        label = { Text(cat, fontSize = 11.sp) }
                    )
                }
            }

            // Sync message
            AnimatedVisibility(visible = lastToggledItemMessage != null) {
                Surface(
                    color = Color(0xFF1E293B),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = lastToggledItemMessage ?: "",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(8.dp)
                    )
                }
            }

            // Items List
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    KitchenMenuItemAvailabilityCard(
                        item = item,
                        onToggle = {
                            val newStatus = !item.isAvailable
                            viewModel.toggleMenuItemAvailability(item)
                            val statusText = if (newStatus) "DISPONIBLE" else "AGOTADO"
                            lastToggledItemMessage = "⚡ '${item.name}' -> $statusText"
                        }
                    )
                }
            }
        }
    }
}

/**
 * Modal BottomSheet / SideSheet dialog for instant access
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KitchenAvailabilitySideSheetModal(
    viewModel: RestaurantViewModel,
    onDismiss: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        containerColor = MaterialTheme.colorScheme.surface,
        dragHandle = { BottomSheetDefaults.DragHandle() }
    ) {
        KitchenAvailabilitySidePanel(
            viewModel = viewModel,
            onClose = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.85f)
        )
    }
}

@Composable
fun KitchenMenuItemAvailabilityCard(
    item: MenuItemEntity,
    onToggle: () -> Unit
) {
    val isAvailable = item.isAvailable

    val cardBorderColor by animateColorAsState(
        targetValue = if (!isAvailable) Color(0xFFF87171) else CardBorderColor,
        label = "border_anim"
    )

    val containerBgColor by animateColorAsState(
        targetValue = if (!isAvailable) Color(0xFFFEF2F2) else MaterialTheme.colorScheme.surface,
        label = "bg_anim"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = containerBgColor),
        border = BorderStroke(if (!isAvailable) 1.5.dp else 1.dp, cardBorderColor),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("item_disponibilidad_${item.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Product Image or Category Icon Badge
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (!isAvailable) Color(0xFFFEE2E2) else MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                if (item.imageUrl.isNotBlank()) {
                    AsyncImage(
                        model = item.imageUrl,
                        contentDescription = item.name,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = androidx.compose.ui.layout.ContentScale.Crop
                    )
                } else {
                    Icon(
                        imageVector = when (item.category.lowercase()) {
                            "bebidas" -> Icons.Default.LocalBar
                            "postres" -> Icons.Default.Icecream
                            "entradas" -> Icons.Default.Tapas
                            else -> Icons.Default.Restaurant
                        },
                        contentDescription = null,
                        tint = if (!isAvailable) Color(0xFFDC2626) else MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                }

                // Overlay Agotado Badge if unavailable
                if (!isAvailable) {
                    Surface(
                        color = Color(0xCCDC2626),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "AGOTADO",
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.Black,
                                    fontSize = 9.sp
                                )
                            )
                        }
                    }
                }
            }

            // Info Column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (!isAvailable) Color(0xFF991B1B) else MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant
                    ) {
                        Text(
                            text = item.category,
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    Text(
                        text = "Q ${String.format(java.util.Locale.US, "%.2f", item.price)}",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    if (item.isVisibleWeb) {
                        Text(
                            text = "• 🌐 Web",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                if (item.description.isNotBlank()) {
                    Text(
                        text = item.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            // --- REAL-TIME TOGGLE SWITCH (DISPONIBLE / AGOTADO) ---
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                // Interactive Toggle Switch
                Switch(
                    checked = isAvailable,
                    onCheckedChange = { onToggle() },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color.White,
                        checkedTrackColor = Color(0xFF16A34A),
                        uncheckedThumbColor = Color.White,
                        uncheckedTrackColor = Color(0xFFDC2626)
                    ),
                    modifier = Modifier.testTag("switch_disponible_${item.id}")
                )

                // High-visibility status label
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = if (isAvailable) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                    border = BorderStroke(1.dp, if (isAvailable) Color(0xFF86EFAC) else Color(0xFFFCA5A5))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                    ) {
                        Icon(
                            imageVector = if (isAvailable) Icons.Default.Check else Icons.Default.Block,
                            contentDescription = null,
                            tint = if (isAvailable) Color(0xFF15803D) else Color(0xFFB91C1C),
                            modifier = Modifier.size(12.dp)
                        )
                        Text(
                            text = if (isAvailable) "Disponible" else "Agotado",
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = if (isAvailable) Color(0xFF15803D) else Color(0xFFB91C1C)
                        )
                    }
                }
            }
        }
    }
}
