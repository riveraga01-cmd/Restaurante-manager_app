package com.example.ui.components

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.InventoryItemEntity
import com.example.data.entity.InventoryMovementEntity
import com.example.data.entity.MenuItemEntity
import com.example.data.entity.RecipeItemEntity
import com.example.data.entity.UserEntity
import com.example.ui.theme.EmeraldSuccess
import com.example.util.ShoppingListExportHelper
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryManagerView(
    inventory: List<InventoryItemEntity>,
    menuItems: List<MenuItemEntity>,
    recipeItems: List<RecipeItemEntity>,
    movements: List<InventoryMovementEntity>,
    currentUser: UserEntity?,
    isManagerRole: Boolean,
    onSaveIngredient: (id: Long, name: String, stock: Double, minStock: Double, idealStock: Double, cost: Double, supplier: String, unit: String, expDate: Long?) -> Unit,
    onDeleteIngredient: (id: Long) -> Unit,
    onRegisterMovement: (ingredientId: Long, type: String, qty: Double, reason: String, userName: String) -> Unit,
    onSaveRecipeItem: (id: Long, menuItemId: Long, ingredientId: Long, ingredientName: String, qty: Double, unit: String) -> Unit,
    onDeleteRecipeItem: (id: Long) -> Unit
) {
    var selectedSubTab by remember { mutableStateOf(0) } // 0: Ingredientes, 1: Recetas (BOM), 2: Lista de Compras, 3: Historial
    var searchQuery by remember { mutableStateOf("") }
    var alertFilter by remember { mutableStateOf("TODOS") } // TODOS, BAJO, AGOTADO, VENCER

    val context = LocalContext.current
    val now = System.currentTimeMillis()

    // Alert calculations
    val lowStockCount = inventory.count { it.currentStock in 0.001..it.minStock }
    val outOfStockCount = inventory.count { it.currentStock <= 0.0 }
    val expiringCount = inventory.count { item ->
        item.expirationDate != null && (item.expirationDate - now) <= 3 * 86400000L
    }

    // Shopping List (Items under minimum stock)
    val shoppingListItems = remember(inventory) {
        inventory.filter { it.currentStock < it.minStock }
    }

    // Dialog States
    var showAddIngredientDialog by remember { mutableStateOf<InventoryItemEntity?>(null) }
    var showMovementDialog by remember { mutableStateOf<InventoryItemEntity?>(null) }
    var showAddRecipeDialog by remember { mutableStateOf<MenuItemEntity?>(null) }
    var showPrintShoppingListModal by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Section Header with Permission Badge
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Inventory2,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "Gestión de Inventarios y Recetas (BOM)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        text = if (isManagerRole) "Modo Gerente • Permiso Total de Modificación" else "Modo Cocina • Consulta de Stock y Recetas (Solo Lectura)",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(8.dp),
                color = if (isManagerRole) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    text = if (isManagerRole) "GERENTE" else "SOLO LECTURA",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    color = if (isManagerRole) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // Live Alert Summary Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Out of Stock Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (outOfStockCount > 0) Color(0xFFFEF2F2) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, if (outOfStockCount > 0) Color(0xFFEF4444) else Color.Transparent),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        selectedSubTab = 0
                        alertFilter = "AGOTADO"
                    }
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        tint = Color(0xFFDC2626),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("Agotados", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
                        Text("$outOfStockCount insumos", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFFDC2626)))
                    }
                }
            }

            // Low Stock Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (lowStockCount > 0) Color(0xFFFFFBEB) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, if (lowStockCount > 0) Color(0xFFF59E0B) else Color.Transparent),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        selectedSubTab = 0
                        alertFilter = "BAJO"
                    }
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = null,
                        tint = Color(0xFFD97706),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("Stock Bajo", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
                        Text("$lowStockCount insumos", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFFD97706)))
                    }
                }
            }

            // Expiring Soon Badge
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = if (expiringCount > 0) Color(0xFFFFF7ED) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                border = BorderStroke(1.dp, if (expiringCount > 0) Color(0xFFEA580C) else Color.Transparent),
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        selectedSubTab = 0
                        alertFilter = "VENCER"
                    }
            ) {
                Row(
                    modifier = Modifier.padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFFEA580C),
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text("Por Vencer", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp)
                        Text("$expiringCount insumos", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFFEA580C)))
                    }
                }
            }
        }

        // Sub-Navigation Tabs
        TabRow(
            selectedTabIndex = selectedSubTab,
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ) {
            Tab(
                selected = selectedSubTab == 0,
                onClick = { selectedSubTab = 0 },
                text = { Text("Ingredientes", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.ListAlt, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
            Tab(
                selected = selectedSubTab == 1,
                onClick = { selectedSubTab = 1 },
                text = { Text("Recetas (BOM)", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
            Tab(
                selected = selectedSubTab == 2,
                onClick = { selectedSubTab = 2 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Lista Compras", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        if (shoppingListItems.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(4.dp))
                            Badge(containerColor = MaterialTheme.colorScheme.error) {
                                Text("${shoppingListItems.size}", fontSize = 10.sp)
                            }
                        }
                    }
                },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
            Tab(
                selected = selectedSubTab == 3,
                onClick = { selectedSubTab = 3 },
                text = { Text("Bitácora", fontSize = 12.sp, fontWeight = FontWeight.Bold) },
                icon = { Icon(Icons.Default.History, contentDescription = null, modifier = Modifier.size(16.dp)) }
            )
        }

        // TAB 0: INGREDIENTES & STOCK
        if (selectedSubTab == 0) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Search & Filter Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier.weight(1f),
                        placeholder = { Text("Buscar ingrediente...", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(16.dp)) },
                        trailingIcon = if (searchQuery.isNotEmpty()) {
                            {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Limpiar", modifier = Modifier.size(16.dp))
                                }
                            }
                        } else null,
                        singleLine = true,
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isManagerRole) {
                        Button(
                            onClick = {
                                showAddIngredientDialog = InventoryItemEntity(
                                    productName = "",
                                    currentStock = 0.0,
                                    minStock = 5.0,
                                    idealStock = 20.0,
                                    unitCost = 0.0,
                                    supplier = "Distribuidora Rivera",
                                    unit = "kg"
                                )
                            },
                            modifier = Modifier.testTag("btn_nuevo_ingrediente"),
                            shape = RoundedCornerShape(10.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 8.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Ingrediente", fontSize = 11.sp)
                        }
                    }
                }

                // Filter Chips
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    val filterOptions = listOf("TODOS" to "Todos", "BAJO" to "Stock Bajo", "AGOTADO" to "Agotados", "VENCER" to "Por Vencer")
                    items(filterOptions) { (key, label) ->
                        FilterChip(
                            selected = alertFilter == key,
                            onClick = { alertFilter = key },
                            label = { Text(label, fontSize = 11.sp) }
                        )
                    }
                }

                // Filtered Ingredients List
                val filteredIngredients = inventory.filter { item ->
                    val matchesSearch = searchQuery.isBlank() || item.productName.contains(searchQuery, ignoreCase = true)
                    val matchesAlert = when (alertFilter) {
                        "BAJO" -> item.currentStock in 0.001..item.minStock
                        "AGOTADO" -> item.currentStock <= 0.0
                        "VENCER" -> item.expirationDate != null && (item.expirationDate - now) <= 3 * 86400000L
                        else -> true
                    }
                    matchesSearch && matchesAlert
                }

                if (filteredIngredients.isEmpty()) {
                    EmptyStateCard(
                        icon = Icons.Default.SearchOff,
                        title = "No se encontraron ingredientes",
                        message = "Intenta cambiar el término de búsqueda o el filtro de alerta seleccionado."
                    )
                } else {
                    filteredIngredients.forEach { ingredient ->
                        IngredientStockRowCard(
                            ingredient = ingredient,
                            isManagerRole = isManagerRole,
                            onEdit = { showAddIngredientDialog = ingredient },
                            onRegisterMovement = { showMovementDialog = ingredient },
                            onDelete = { onDeleteIngredient(ingredient.id) }
                        )
                    }
                }
            }
        }

        // TAB 1: RECETAS (BILL OF MATERIALS)
        if (selectedSubTab == 1) {
            RecipeBomTabContent(
                menuItems = menuItems,
                inventory = inventory,
                recipeItems = recipeItems,
                isManagerRole = isManagerRole,
                onSaveRecipeItem = onSaveRecipeItem,
                onDeleteRecipeItem = onDeleteRecipeItem
            )
        }

        // TAB 2: LISTA DE COMPRAS
        if (selectedSubTab == 2) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                // Header & Action Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "🛒 Lista Sugerida de Reabastecimiento",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        OutlinedButton(
                            onClick = { ShoppingListExportHelper.printOrExportPdf(context, shoppingListItems) },
                            enabled = shoppingListItems.isNotEmpty(),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.PictureAsPdf, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFFDC2626))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("PDF", fontSize = 11.sp)
                        }

                        OutlinedButton(
                            onClick = { ShoppingListExportHelper.exportToCsvAndShare(context, shoppingListItems) },
                            enabled = shoppingListItems.isNotEmpty(),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.TableChart, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color(0xFF16A34A))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Excel", fontSize = 11.sp)
                        }

                        Button(
                            onClick = { ShoppingListExportHelper.shareViaWhatsApp(context, shoppingListItems) },
                            enabled = shoppingListItems.isNotEmpty(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366)),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(14.dp), tint = Color.White)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("WhatsApp", fontSize = 11.sp, color = Color.White)
                        }
                    }
                }

                if (shoppingListItems.isEmpty()) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = EmeraldSuccess.copy(alpha = 0.1f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(28.dp))
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("¡Inventario Completo y Saludable!", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold, color = EmeraldSuccess))
                                Text("Todos los ingredientes están por encima del stock mínimo establecido.", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                    }
                } else {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            // Table Header
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(6.dp))
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Ingrediente", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1.8f))
                                Text("Actual", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1f))
                                Text("Mínimo", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1f))
                                Text("Sugerido", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1.2f))
                                Text("Proveedor", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1.5f))
                            }

                            Divider(modifier = Modifier.padding(vertical = 4.dp))

                            shoppingListItems.forEach { item ->
                                val suggestedQty = maxOf(0.0, item.idealStock - item.currentStock)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 4.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(modifier = Modifier.weight(1.8f)) {
                                        Text(item.productName, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                        Text("Costo Est: Q${String.format(Locale.US, "%.2f", suggestedQty * item.unitCost)}", style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = MaterialTheme.colorScheme.outline)
                                    }
                                    Text("${item.currentStock} ${item.unit}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                                    Text("${item.minStock} ${item.unit}", style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
                                    Surface(
                                        shape = RoundedCornerShape(6.dp),
                                        color = Color(0xFFFEF2F2),
                                        modifier = Modifier.weight(1.2f)
                                    ) {
                                        Text(
                                            text = "${String.format(Locale.US, "%.1f", suggestedQty)} ${item.unit}",
                                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color(0xFFDC2626)),
                                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                    Text(item.supplier, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1.5f), maxLines = 1)
                                }
                                Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }

        // TAB 3: HISTORIAL / BITÁCORA DE MOVIMIENTOS
        if (selectedSubTab == 3) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "📜 Bitácora de Movimientos de Inventario",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )

                if (movements.isEmpty()) {
                    EmptyStateCard(
                        icon = Icons.Default.History,
                        title = "Sin movimientos registrados",
                        message = "Aquí aparecerán las compras, mermas, vencimientos, consumos internos y ajustes realizados."
                    )
                } else {
                    movements.take(20).forEach { mov ->
                        val sdf = remember { SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault()) }
                        val dateStr = sdf.format(Date(mov.timestamp))
                        val isPositive = mov.quantity > 0

                        Card(
                            shape = RoundedCornerShape(10.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                                    Surface(
                                        shape = CircleShape,
                                        color = if (isPositive) Color(0xFFD1FAE5) else Color(0xFFFEE2E2)
                                    ) {
                                        Icon(
                                            imageVector = if (isPositive) Icons.Default.AddCircle else Icons.Default.RemoveCircle,
                                            contentDescription = null,
                                            tint = if (isPositive) Color(0xFF059669) else Color(0xFFDC2626),
                                            modifier = Modifier.padding(6.dp).size(16.dp)
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(mov.ingredientName, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold))
                                        Text("Motivo: ${mov.reason} • Por: ${mov.user}", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        Text(dateStr, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, color = MaterialTheme.colorScheme.outline)
                                    }
                                }

                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                ) {
                                    Text(
                                        text = "${if (mov.quantity > 0) "+" else ""}${String.format(Locale.US, "%.2f", mov.quantity)} (${mov.type})",
                                        style = MaterialTheme.typography.labelSmall.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = if (isPositive) Color(0xFF059669) else Color(0xFFDC2626)
                                        ),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // MODAL DIALOGS FOR GERENTE
    showAddIngredientDialog?.let { currentItem ->
        AddEditIngredientDialog(
            item = currentItem,
            onDismiss = { showAddIngredientDialog = null },
            onSave = { id, name, stock, minStock, idealStock, cost, supplier, unit, expDate ->
                onSaveIngredient(id, name, stock, minStock, idealStock, cost, supplier, unit, expDate)
                showAddIngredientDialog = null
            }
        )
    }

    showMovementDialog?.let { ingredient ->
        RegisterMovementDialog(
            ingredient = ingredient,
            currentUser = currentUser,
            onDismiss = { showMovementDialog = null },
            onRegister = { type, qty, reason, user ->
                onRegisterMovement(ingredient.id, type, qty, reason, user)
                showMovementDialog = null
                Toast.makeText(context, "Movimiento de $type registrado con éxito", Toast.LENGTH_SHORT).show()
            }
        )
    }
}

@Composable
fun IngredientStockRowCard(
    ingredient: InventoryItemEntity,
    isManagerRole: Boolean,
    onEdit: () -> Unit,
    onRegisterMovement: () -> Unit,
    onDelete: () -> Unit
) {
    val now = System.currentTimeMillis()
    val isBelowMin = ingredient.currentStock <= ingredient.minStock
    val isOutOfStock = ingredient.currentStock <= 0.0
    val isExpiringSoon = ingredient.expirationDate != null && (ingredient.expirationDate - now) <= 3 * 86400000L

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isOutOfStock -> Color(0xFFFEE2E2)
                isBelowMin -> Color(0xFFFEF2F2)
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isBelowMin) BorderStroke(1.5.dp, Color(0xFFDC2626)) else null,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = ingredient.productName,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isBelowMin) Color(0xFF991B1B) else MaterialTheme.colorScheme.onSurface
                            )
                        )
                        Spacer(modifier = Modifier.width(6.dp))

                        // Status Alert Badge
                        when {
                            isOutOfStock -> {
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFDC2626)) {
                                    Text("⛔ SIN STOCK", color = Color.White, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontWeight = FontWeight.Bold)
                                }
                            }
                            isBelowMin -> {
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFDC2626)) {
                                    Text("⚠️ STOCK BAJO MÍNIMO", color = Color.White, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontWeight = FontWeight.Bold)
                                }
                            }
                            isExpiringSoon -> {
                                Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFEA580C)) {
                                    Text("POR VENCER", color = Color.White, style = MaterialTheme.typography.labelSmall, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Proveedor: ${ingredient.supplier} • Costo: Q${ingredient.unitCost}/${ingredient.unit}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                if (isManagerRole) {
                    Row {
                        IconButton(onClick = onRegisterMovement) {
                            Icon(Icons.Default.SwapVert, contentDescription = "Ajustar", tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = onEdit) {
                            Icon(Icons.Default.Edit, contentDescription = "Editar", tint = MaterialTheme.colorScheme.primary)
                        }
                        IconButton(onClick = onDelete) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = MaterialTheme.colorScheme.error)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Stock Metrics Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isBelowMin) Color(0xFFFCA5A5).copy(alpha = 0.25f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        "Stock Actual",
                        style = MaterialTheme.typography.labelSmall,
                        fontSize = 10.sp,
                        color = if (isBelowMin) Color(0xFFDC2626) else MaterialTheme.colorScheme.outline,
                        fontWeight = if (isBelowMin) FontWeight.Bold else FontWeight.Normal
                    )
                    Text(
                        "${ingredient.currentStock} ${ingredient.unit}",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.ExtraBold,
                            color = if (isBelowMin) Color(0xFFDC2626) else MaterialTheme.colorScheme.onSurface
                        )
                    )
                }
                Column {
                    Text("Stock Mínimo", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                    Text("${ingredient.minStock} ${ingredient.unit}", style = MaterialTheme.typography.titleSmall)
                }
                Column {
                    Text("Stock Ideal", style = MaterialTheme.typography.labelSmall, fontSize = 10.sp, color = MaterialTheme.colorScheme.outline)
                    Text("${ingredient.idealStock} ${ingredient.unit}", style = MaterialTheme.typography.titleSmall)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeBomTabContent(
    menuItems: List<MenuItemEntity>,
    inventory: List<InventoryItemEntity>,
    recipeItems: List<RecipeItemEntity>,
    isManagerRole: Boolean,
    onSaveRecipeItem: (id: Long, menuItemId: Long, ingredientId: Long, ingredientName: String, qty: Double, unit: String) -> Unit,
    onDeleteRecipeItem: (id: Long) -> Unit
) {
    var selectedMenuItem by remember { mutableStateOf<MenuItemEntity?>(menuItems.firstOrNull()) }
    var showAddRecipeIngredientModal by remember { mutableStateOf(false) }

    LaunchedEffect(menuItems) {
        if (selectedMenuItem == null && menuItems.isNotEmpty()) {
            selectedMenuItem = menuItems.first()
        }
    }

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        // Menu Item Dropdown / Selector
        Text(
            text = "Selecciona un Platillo/Bebida del Menú para gestionar su Receta:",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            items(menuItems) { item ->
                FilterChip(
                    selected = selectedMenuItem?.id == item.id,
                    onClick = { selectedMenuItem = item },
                    label = { Text("${item.name} (Q${item.price})", fontSize = 11.sp) }
                )
            }
        }

        selectedMenuItem?.let { activeMenuItem ->
            val ingredientsInRecipe = recipeItems.filter { it.menuItemId == activeMenuItem.id }

            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Receta (BOM) de: ${activeMenuItem.name}",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "Categoría: ${activeMenuItem.category} • Precio: Q${activeMenuItem.price}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        if (isManagerRole) {
                            Button(
                                onClick = { showAddRecipeIngredientModal = true },
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Agregar Insumo", fontSize = 11.sp)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (ingredientsInRecipe.isEmpty()) {
                        Text(
                            text = "Este plato no tiene ingredientes asignados aún en su receta. Agrega insumos para que se descuenten automáticamente al vender.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )
                    } else {
                        // Table
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant, shape = RoundedCornerShape(6.dp))
                                .padding(8.dp)
                        ) {
                            Text("Ingrediente / Insumo Requerido", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(2f))
                            Text("Cantidad Consumida", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(1.2f))
                            if (isManagerRole) {
                                Text("Acción", style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.width(50.dp))
                            }
                        }

                        Divider(modifier = Modifier.padding(vertical = 4.dp))

                        ingredientsInRecipe.forEach { recItem ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 6.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(recItem.ingredientName, style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold), modifier = Modifier.weight(2f))
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer,
                                    modifier = Modifier.weight(1.2f)
                                ) {
                                    Text(
                                        text = "${recItem.quantityRequired} ${recItem.unit}",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary),
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                                if (isManagerRole) {
                                    IconButton(
                                        onClick = { onDeleteRecipeItem(recItem.id) },
                                        modifier = Modifier.width(50.dp).height(30.dp)
                                    ) {
                                        Icon(Icons.Default.Delete, contentDescription = "Eliminar Insumo Receta", tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                                    }
                                }
                            }
                            Divider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                        }
                    }
                }
            }

            if (showAddRecipeIngredientModal) {
                AddRecipeIngredientDialog(
                    menuItem = activeMenuItem,
                    availableIngredients = inventory,
                    onDismiss = { showAddRecipeIngredientModal = false },
                    onAdd = { ingredient, qty ->
                        onSaveRecipeItem(
                            0L,
                            activeMenuItem.id,
                            ingredient.id,
                            ingredient.productName,
                            qty,
                            ingredient.unit
                        )
                        showAddRecipeIngredientModal = false
                    }
                )
            }
        }
    }
}

@Composable
fun AddEditIngredientDialog(
    item: InventoryItemEntity,
    onDismiss: () -> Unit,
    onSave: (id: Long, name: String, stock: Double, minStock: Double, idealStock: Double, cost: Double, supplier: String, unit: String, expDate: Long?) -> Unit
) {
    var name by remember { mutableStateOf(item.productName) }
    var currentStockStr by remember { mutableStateOf(if (item.currentStock == 0.0) "" else item.currentStock.toString()) }
    var minStockStr by remember { mutableStateOf(if (item.minStock == 0.0) "" else item.minStock.toString()) }
    var idealStockStr by remember { mutableStateOf(if (item.idealStock == 0.0) "" else item.idealStock.toString()) }
    var unitCostStr by remember { mutableStateOf(if (item.unitCost == 0.0) "" else item.unitCost.toString()) }
    var supplier by remember { mutableStateOf(item.supplier) }
    var unit by remember { mutableStateOf(item.unit) }

    val units = listOf("g", "kg", "lb", "ml", "L", "unidad")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (item.id == 0L) "Nuevo Ingrediente" else "Editar Ingrediente", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre del Ingrediente") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = currentStockStr,
                        onValueChange = { currentStockStr = it },
                        label = { Text("Stock Actual") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = minStockStr,
                        onValueChange = { minStockStr = it },
                        label = { Text("Stock Mínimo") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = idealStockStr,
                        onValueChange = { idealStockStr = it },
                        label = { Text("Stock Ideal") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = unitCostStr,
                        onValueChange = { unitCostStr = it },
                        label = { Text("Costo Unit. (Q)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }

                OutlinedTextField(
                    value = supplier,
                    onValueChange = { supplier = it },
                    label = { Text("Proveedor") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                Column {
                    Text("Unidad de Medida:", style = MaterialTheme.typography.labelSmall)
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        items(units) { u ->
                            FilterChip(
                                selected = unit == u,
                                onClick = { unit = u },
                                label = { Text(u) }
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        val stock = currentStockStr.toDoubleOrNull() ?: 0.0
                        val minStock = minStockStr.toDoubleOrNull() ?: 5.0
                        val idealStock = idealStockStr.toDoubleOrNull() ?: (minStock * 2.5)
                        val cost = unitCostStr.toDoubleOrNull() ?: 0.0
                        onSave(item.id, name.trim(), stock, minStock, idealStock, cost, supplier.ifBlank { "Distribuidora Rivera" }, unit, item.expirationDate)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun RegisterMovementDialog(
    ingredient: InventoryItemEntity,
    currentUser: UserEntity?,
    onDismiss: () -> Unit,
    onRegister: (type: String, qty: Double, reason: String, user: String) -> Unit
) {
    var type by remember { mutableStateOf("COMPRA") }
    var quantityStr by remember { mutableStateOf("") }
    var reason by remember { mutableStateOf("") }

    val movementTypes = listOf("COMPRA", "MERMA", "VENCIMIENTO", "CONSUMO_INTERNO", "AJUSTE")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Movimiento Manual: ${ingredient.productName}", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Selecciona Tipo de Movimiento:", style = MaterialTheme.typography.labelSmall)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(movementTypes) { t ->
                        FilterChip(
                            selected = type == t,
                            onClick = { type = t },
                            label = { Text(t, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = quantityStr,
                    onValueChange = { quantityStr = it },
                    label = { Text("Cantidad (${ingredient.unit})") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )

                OutlinedTextField(
                    value = reason,
                    onValueChange = { reason = it },
                    label = { Text("Motivo / Explicación") },
                    placeholder = { Text("Ej: Factura #102, Vencido por calor, etc.") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val qty = quantityStr.toDoubleOrNull() ?: 0.0
                    if (qty > 0 && reason.isNotBlank()) {
                        onRegister(type, qty, reason.trim(), currentUser?.name ?: "Gerente")
                    }
                }
            ) {
                Text("Registrar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}

@Composable
fun AddRecipeIngredientDialog(
    menuItem: MenuItemEntity,
    availableIngredients: List<InventoryItemEntity>,
    onDismiss: () -> Unit,
    onAdd: (ingredient: InventoryItemEntity, qtyRequired: Double) -> Unit
) {
    var selectedIngredient by remember { mutableStateOf<InventoryItemEntity?>(availableIngredients.firstOrNull()) }
    var qtyStr by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Agregar Insumo a la Receta", fontWeight = FontWeight.Bold) },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("Selecciona Ingrediente:", style = MaterialTheme.typography.labelSmall)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(availableIngredients) { ing ->
                        FilterChip(
                            selected = selectedIngredient?.id == ing.id,
                            onClick = { selectedIngredient = ing },
                            label = { Text("${ing.productName} (${ing.unit})", fontSize = 11.sp) }
                        )
                    }
                }

                selectedIngredient?.let { ing ->
                    OutlinedTextField(
                        value = qtyStr,
                        onValueChange = { qtyStr = it },
                        label = { Text("Cantidad Requerida por porción (${ing.unit})") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val ing = selectedIngredient
                    val qty = qtyStr.toDoubleOrNull() ?: 0.0
                    if (ing != null && qty > 0) {
                        onAdd(ing, qty)
                    }
                }
            ) {
                Text("Guardar en Receta")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}
