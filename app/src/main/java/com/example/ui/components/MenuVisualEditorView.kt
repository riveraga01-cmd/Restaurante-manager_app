package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.entity.MenuItemEntity
import com.example.ui.theme.*

@Composable
fun MenuVisualEditorView(
    menuItems: List<MenuItemEntity>,
    onSaveMenuItem: (id: Long, name: String, category: String, price: Double, description: String, imageUrl: String, isAvailable: Boolean, isVisibleWeb: Boolean) -> Unit,
    onDeleteMenuItem: (id: Long) -> Unit,
    onToggleAvailability: (MenuItemEntity) -> Unit,
    onToggleWebVisibility: (MenuItemEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Todos") }
    var showWebOnly by remember { mutableStateOf(false) }

    var itemToEdit by remember { mutableStateOf<MenuItemEntity?>(null) }
    var isNewItem by remember { mutableStateOf(false) }
    var itemToDelete by remember { mutableStateOf<MenuItemEntity?>(null) }

    val categories = remember(menuItems) {
        listOf("Todos") + (menuItems.map { it.category }.distinct().ifEmpty { listOf("Platillos", "Bebidas", "Postres", "Entradas") })
    }

    val filteredItems = remember(menuItems, searchQuery, selectedCategory, showWebOnly) {
        menuItems.filter { item ->
            val matchesCategory = (selectedCategory == "Todos" || item.category.equals(selectedCategory, ignoreCase = true))
            val matchesSearch = item.name.contains(searchQuery, ignoreCase = true) || item.description.contains(searchQuery, ignoreCase = true)
            val matchesWebFilter = !showWebOnly || item.isVisibleWeb
            matchesCategory && matchesSearch && matchesWebFilter
        }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Top Header and Add Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Gestor de Menú & Precios",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "${filteredItems.size} de ${menuItems.size} productos | Control Web y POS",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = {
                    isNewItem = true
                    itemToEdit = MenuItemEntity(
                        name = "",
                        category = if (selectedCategory != "Todos") selectedCategory else "Platillos",
                        price = 0.0,
                        description = "",
                        imageUrl = "",
                        isAvailable = true,
                        isVisibleWeb = true
                    )
                },
                modifier = Modifier.testTag("btn_agregar_producto_menu")
            ) {
                Icon(Icons.Default.AddPhotoAlternate, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Nuevo Platillo")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Search Bar & Web Filter Switch
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Buscar en menú...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(Icons.Default.Close, contentDescription = "Limpiar")
                        }
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_buscar_menu")
            )

            FilterChip(
                selected = showWebOnly,
                onClick = { showWebOnly = !showWebOnly },
                label = { Text("Solo Web (${menuItems.count { it.isVisibleWeb }})", fontSize = 12.sp) },
                leadingIcon = {
                    Icon(
                        imageVector = if (showWebOnly) Icons.Default.Language else Icons.Outlined.Language,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Category Pills
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(categories) { cat ->
                val count = if (cat == "Todos") menuItems.size else menuItems.count { it.category.equals(cat, ignoreCase = true) }
                FilterChip(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    label = { Text("$cat ($count)") }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Menu Items List
        if (filteredItems.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Outlined.RestaurantMenu,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "No se encontraron platillos",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Intenta con otra búsqueda o agrega un nuevo producto.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredItems, key = { it.id }) { item ->
                    VisualMenuItemCard(
                        item = item,
                        onEditClick = {
                            isNewItem = false
                            itemToEdit = item
                        },
                        onDeleteClick = {
                            itemToDelete = item
                        },
                        onToggleAvailability = { onToggleAvailability(item) },
                        onToggleWebVisibility = { onToggleWebVisibility(item) }
                    )
                }
            }
        }
    }

    // Add / Edit Visual Menu Item Dialog
    itemToEdit?.let { currentItem ->
        AddEditVisualMenuItemDialog(
            item = currentItem,
            isNew = isNewItem,
            onDismiss = { itemToEdit = null },
            onSave = { id, name, category, price, description, imageUrl, isAvailable, isVisibleWeb ->
                onSaveMenuItem(id, name, category, price, description, imageUrl, isAvailable, isVisibleWeb)
                itemToEdit = null
            }
        )
    }

    // Delete Confirmation Dialog
    itemToDelete?.let { item ->
        AlertDialog(
            onDismissRequest = { itemToDelete = null },
            icon = { Icon(Icons.Default.DeleteForever, contentDescription = null, tint = MaterialTheme.colorScheme.error) },
            title = { Text("¿Eliminar Platillo?") },
            text = {
                Text("Se eliminará '${item.name}' permanentemente tanto del POS local como del Menú Web en Firestore.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteMenuItem(item.id)
                        itemToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { itemToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

@Composable
fun VisualMenuItemCard(
    item: MenuItemEntity,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onToggleAvailability: () -> Unit,
    onToggleWebVisibility: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (item.isAvailable) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(
            1.dp,
            if (item.isVisibleWeb) MaterialTheme.colorScheme.primary.copy(alpha = 0.3f) else MaterialTheme.colorScheme.outlineVariant
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_menu_item_${item.id}")
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Image Thumbnail with Fallback
                Box(
                    modifier = Modifier
                        .size(76.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer),
                    contentAlignment = Alignment.Center
                ) {
                    if (item.imageUrl.isNotBlank()) {
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = item.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Icon(
                            imageVector = when (item.category.lowercase()) {
                                "bebidas" -> Icons.Default.LocalBar
                                "postres" -> Icons.Default.Cake
                                "entradas" -> Icons.Default.Tapas
                                else -> Icons.Default.Restaurant
                            },
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Info Column
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = item.name,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f, fill = false)
                        )

                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.secondaryContainer
                        ) {
                            Text(
                                text = item.category,
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                    }

                    if (item.description.isNotBlank()) {
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = item.description,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Q${String.format(java.util.Locale.US, "%.2f", item.price)}",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                // Action Buttons
                Column(horizontalAlignment = Alignment.End) {
                    Row {
                        IconButton(
                            onClick = onEditClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Default.Edit,
                                contentDescription = "Editar",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(
                            onClick = onDeleteClick,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                Icons.Default.DeleteOutline,
                                contentDescription = "Eliminar",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                thickness = 0.5.dp,
                color = MaterialTheme.colorScheme.outlineVariant
            )

            // Bottom Quick Toggles Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Availability Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onToggleAvailability() }
                ) {
                    Switch(
                        checked = item.isAvailable,
                        onCheckedChange = { onToggleAvailability() },
                        modifier = Modifier.size(38.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (item.isAvailable) "Disponible" else "Agotado",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (item.isAvailable) Color(0xFF15803D) else Color(0xFFDC2626)
                        )
                    )
                }

                // Web Visibility Toggle
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { onToggleWebVisibility() }
                ) {
                    Icon(
                        imageVector = if (item.isVisibleWeb) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = null,
                        tint = if (item.isVisibleWeb) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = if (item.isVisibleWeb) "Público en Web" else "Oculto en Web",
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            color = if (item.isVisibleWeb) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
                        )
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Switch(
                        checked = item.isVisibleWeb,
                        onCheckedChange = { onToggleWebVisibility() },
                        modifier = Modifier.size(38.dp)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditVisualMenuItemDialog(
    item: MenuItemEntity,
    isNew: Boolean,
    onDismiss: () -> Unit,
    onSave: (id: Long, name: String, category: String, price: Double, description: String, imageUrl: String, isAvailable: Boolean, isVisibleWeb: Boolean) -> Unit
) {
    var name by remember { mutableStateOf(item.name) }
    var category by remember { mutableStateOf(item.category.ifBlank { "Platillos" }) }
    var priceText by remember { mutableStateOf(if (item.price > 0) item.price.toString() else "") }
    var description by remember { mutableStateOf(item.description) }
    var imageUrl by remember { mutableStateOf(item.imageUrl) }
    var isAvailable by remember { mutableStateOf(item.isAvailable) }
    var isVisibleWeb by remember { mutableStateOf(item.isVisibleWeb) }

    val categories = listOf("Platillos", "Bebidas", "Postres", "Entradas", "Guarniciones")

    val photoPresets = listOf(
        Pair("Pepián de Pollo", "https://images.unsplash.com/photo-1546069901-ba9599a7e63c?w=600&q=80"),
        Pair("Churrasco Típico", "https://images.unsplash.com/photo-1555939594-58d7cb561ad1?w=600&q=80"),
        Pair("Hamburguesa Gourmet", "https://images.unsplash.com/photo-1568901346375-23c9450c58cd?w=600&q=80"),
        Pair("Ensalada Fresca", "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=600&q=80"),
        Pair("Cerveza / Bebida", "https://images.unsplash.com/photo-1608270199042-3e2840c83a1b?w=600&q=80"),
        Pair("Café Artesanal", "https://images.unsplash.com/photo-1509042239860-f550ce710b93?w=600&q=80"),
        Pair("Postre Casero", "https://images.unsplash.com/photo-1551024709-8f23befc6f87?w=600&q=80")
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (isNew) Icons.Default.AddCircleOutline else Icons.Default.EditNote,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(if (isNew) "Agregar Producto al Menú" else "Editar Platillo / Bebida")
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del Producto *") },
                    placeholder = { Text("Ej. Pepián Tradicional") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Category Selector
                Text("Categoría:", style = MaterialTheme.typography.labelMedium)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = category == cat,
                            onClick = { category = cat },
                            label = { Text(cat) }
                        )
                    }
                }

                // Price
                OutlinedTextField(
                    value = priceText,
                    onValueChange = { priceText = it },
                    label = { Text("Precio en Quetzales (Q) *") },
                    placeholder = { Text("Ej. 65.00") },
                    prefix = { Text("Q ", fontWeight = FontWeight.Bold) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Description
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción / Ingredientes") },
                    placeholder = { Text("Receta tradicional con arroz, tamalito y ensalada...") },
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth()
                )

                // Photo / Image URL Section
                Text("Fotografía del Platillo (Para el Menú Web):", style = MaterialTheme.typography.labelMedium)
                OutlinedTextField(
                    value = imageUrl,
                    onValueChange = { imageUrl = it },
                    label = { Text("URL de la imagen (HTTPS)") },
                    placeholder = { Text("https://...") },
                    leadingIcon = { Icon(Icons.Default.Image, contentDescription = null) },
                    trailingIcon = {
                        if (imageUrl.isNotEmpty()) {
                            IconButton(onClick = { imageUrl = "" }) {
                                Icon(Icons.Default.Close, contentDescription = "Limpiar")
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                // Quick photo presets
                Text("O elige una foto recomendada:", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(photoPresets) { (presetName, url) ->
                        AssistChip(
                            onClick = { imageUrl = url },
                            label = { Text(presetName, fontSize = 11.sp) },
                            leadingIcon = { Icon(Icons.Default.Photo, contentDescription = null, modifier = Modifier.size(14.dp)) }
                        )
                    }
                }

                // Image Preview if provided
                if (imageUrl.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(130.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color.DarkGray),
                        contentAlignment = Alignment.Center
                    ) {
                        AsyncImage(
                            model = imageUrl,
                            contentDescription = "Vista previa",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }

                HorizontalDivider()

                // Web & POS Toggles
                Card(
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Mostrar en Menú Web", fontWeight = FontWeight.SemiBold)
                                Text("Los clientes podrán verlo y pedirlo desde la web / QR", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                            }
                            Switch(checked = isVisibleWeb, onCheckedChange = { isVisibleWeb = it })
                        }

                        HorizontalDivider(thickness = 0.5.dp)

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Producto Disponible", fontWeight = FontWeight.SemiBold)
                                Text("Si está agotado, se marcará 'No disponible'", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.outline)
                            }
                            Switch(checked = isAvailable, onCheckedChange = { isAvailable = it })
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val p = priceText.toDoubleOrNull() ?: 0.0
                    if (name.isNotBlank() && p > 0) {
                        onSave(item.id, name.trim(), category.trim(), p, description.trim(), imageUrl.trim(), isAvailable, isVisibleWeb)
                    }
                },
                enabled = name.isNotBlank() && (priceText.toDoubleOrNull() ?: 0.0) > 0
            ) {
                Text(if (isNew) "Crear Platillo" else "Guardar Cambios")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
