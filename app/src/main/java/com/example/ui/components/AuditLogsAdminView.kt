package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
import com.example.data.entity.AuditLogEntity
import com.example.ui.theme.*
import com.example.ui.viewmodel.RestaurantViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class EventTypeFilter(val label: String, val icon: @Composable () -> Unit) {
    TODOS("Todos los eventos", { Icon(Icons.Default.List, contentDescription = null, modifier = Modifier.size(16.dp)) }),
    FALLO_PIN("Fallo de PIN (🚨)", { Icon(Icons.Default.Warning, contentDescription = null, tint = Color.Red, modifier = Modifier.size(16.dp)) }),
    CIERRES("Cierres (🔒)", { Icon(Icons.Default.Lock, contentDescription = null, tint = Color(0xFFD97706), modifier = Modifier.size(16.dp)) }),
    RESTABLECIMIENTO("Restablecimientos (⚡)", { Icon(Icons.Default.RestartAlt, contentDescription = null, tint = Color(0xFF7C3AED), modifier = Modifier.size(16.dp)) }),
    USUARIOS_PIN("Usuarios & PINs (👤)", { Icon(Icons.Default.People, contentDescription = null, tint = Color(0xFF2563EB), modifier = Modifier.size(16.dp)) }),
    SISTEMA("Ajustes & Sistema (⚙️)", { Icon(Icons.Default.Settings, contentDescription = null, tint = Color(0xFF4B5563), modifier = Modifier.size(16.dp)) })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditLogsAdminView(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val auditLogs by viewModel.allAuditLogs.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val isFirestoreSyncActive by viewModel.isFirestoreSyncActive.collectAsState()
    val lastSyncTimestamp by viewModel.lastSyncTimestamp.collectAsState()

    var selectedEventType by remember { mutableStateOf(EventTypeFilter.TODOS) }
    var selectedUserFilter by remember { mutableStateOf("TODOS") }
    var searchQuery by remember { mutableStateOf("") }
    var showUserDropdown by remember { mutableStateOf(false) }

    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()) }
    val lastSyncFormatted = remember(lastSyncTimestamp) {
        if (lastSyncTimestamp > 0) dateFormat.format(Date(lastSyncTimestamp)) else "No disponible"
    }

    // Dynamic list of user names for filtering
    val availableUsers = remember(auditLogs, allUsers) {
        val userNamesFromLogs = auditLogs.map { it.user }.filter { it.isNotBlank() }
        val userNamesFromEntities = allUsers.map { it.name }.filter { it.isNotBlank() }
        (listOf("TODOS") + (userNamesFromLogs + userNamesFromEntities).distinct().sorted())
    }

    // Filtered audit logs list
    val filteredLogs = remember(auditLogs, selectedEventType, selectedUserFilter, searchQuery) {
        auditLogs.filter { log ->
            // Event type filter
            val matchesType = when (selectedEventType) {
                EventTypeFilter.TODOS -> true
                EventTypeFilter.FALLO_PIN -> log.action == "INTENTO_PIN_FALLIDO" || log.details.contains("fallido", ignoreCase = true)
                EventTypeFilter.CIERRES -> log.action.contains("CIERRE", ignoreCase = true) || log.details.contains("cierre", ignoreCase = true)
                EventTypeFilter.RESTABLECIMIENTO -> log.action.contains("RESET", ignoreCase = true) || log.action.contains("REINICIA", ignoreCase = true) || log.action.contains("RESTABLECER", ignoreCase = true)
                EventTypeFilter.USUARIOS_PIN -> log.action.contains("USUARIO", ignoreCase = true) || log.action.contains("PIN", ignoreCase = true)
                EventTypeFilter.SISTEMA -> log.action in listOf("AJUSTES_SISTEMA", "GENERAR_CODIGO", "VINCULAR_DISPOSITIVO") || !log.action.contains("PIN") && !log.action.contains("CIERRE")
            }

            // User filter
            val matchesUser = selectedUserFilter == "TODOS" || log.user.equals(selectedUserFilter, ignoreCase = true)

            // Search query filter
            val matchesSearch = searchQuery.isBlank() ||
                    log.details.contains(searchQuery, ignoreCase = true) ||
                    log.action.contains(searchQuery, ignoreCase = true) ||
                    log.deviceId.contains(searchQuery, ignoreCase = true) ||
                    log.user.contains(searchQuery, ignoreCase = true)

            matchesType && matchesUser && matchesSearch
        }.sortedByDescending { it.timestamp }
    }

    // Summary counts
    val failedPinCount = remember(auditLogs) {
        auditLogs.count { it.action == "INTENTO_PIN_FALLIDO" || it.details.contains("fallido", ignoreCase = true) }
    }
    val closesCount = remember(auditLogs) {
        auditLogs.count { it.action.contains("CIERRE", ignoreCase = true) }
    }
    val resetsCount = remember(auditLogs) {
        auditLogs.count { it.action.contains("RESET", ignoreCase = true) || it.action.contains("REINICIA", ignoreCase = true) || it.action.contains("RESTABLECER", ignoreCase = true) }
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // --- LIVE FIRESTORE STATUS BANNER ---
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorderColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = if (isFirestoreSyncActive) EmeraldSuccess.copy(alpha = 0.15f) else Color(0xFFFEF3C7),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.CloudSync,
                                    contentDescription = null,
                                    tint = if (isFirestoreSyncActive) EmeraldSuccess else Color(0xFFD97706),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "Audit Trail en Tiempo Real",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BentoPrimary
                                )
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isFirestoreSyncActive) EmeraldSuccess else Color(0xFFD97706)
                                ) {
                                    Text(
                                        text = if (isFirestoreSyncActive) "FIRESTORE LIVE" else "MODO LOCAL",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = Color.White),
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(
                                text = "Última sincronización: $lastSyncFormatted",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    IconButton(
                        onClick = { viewModel.triggerManualSync() },
                        modifier = Modifier.testTag("refresh_audit_logs_btn")
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refrescar desde Firestore", tint = BentoPrimary)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // --- METRIC STATS ROW ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AuditMetricChip(
                        modifier = Modifier.weight(1f),
                        title = "Total Logs",
                        count = "${auditLogs.size}",
                        icon = Icons.Default.ReceiptLong,
                        color = BentoPrimary,
                        bgColor = Color(0xFFF1F5F9)
                    )

                    AuditMetricChip(
                        modifier = Modifier.weight(1f),
                        title = "Fallos PIN",
                        count = "$failedPinCount",
                        icon = Icons.Default.Warning,
                        color = Color(0xFFDC2626),
                        bgColor = Color(0xFFFEF2F2)
                    )

                    AuditMetricChip(
                        modifier = Modifier.weight(1f),
                        title = "Cierres",
                        count = "$closesCount",
                        icon = Icons.Default.Lock,
                        color = Color(0xFFD97706),
                        bgColor = Color(0xFFFFFBEB)
                    )

                    AuditMetricChip(
                        modifier = Modifier.weight(1f),
                        title = "Resets",
                        count = "$resetsCount",
                        icon = Icons.Default.RestartAlt,
                        color = Color(0xFF7C3AED),
                        bgColor = Color(0xFFF3E8FF)
                    )
                }
            }
        }

        // --- FILTERING CONTROLS CARD ---
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            border = BorderStroke(1.dp, CardBorderColor),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = "Filtros de Auditoría",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = BentoPrimary
                )

                // Event Type Category Filter Chips Row
                ScrollableTabRow(
                    selectedTabIndex = selectedEventType.ordinal,
                    edgePadding = 0.dp,
                    divider = {},
                    containerColor = Color.Transparent,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    EventTypeFilter.values().forEach { filter ->
                        Tab(
                            selected = selectedEventType == filter,
                            onClick = { selectedEventType = filter },
                            text = {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    filter.icon()
                                    Text(
                                        text = filter.label,
                                        fontSize = 12.sp,
                                        fontWeight = if (selectedEventType == filter) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Search Text Field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = { Text("Buscar detalles o dispositivo...", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                        trailingIcon = if (searchQuery.isNotEmpty()) {
                            {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "Limpiar búsqueda", modifier = Modifier.size(16.dp))
                                }
                            }
                        } else null,
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp)
                            .testTag("audit_search_input"),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // User Filter Dropdown Button
                    Box {
                        OutlinedButton(
                            onClick = { showUserDropdown = true },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(52.dp)
                                .testTag("audit_user_filter_btn"),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (selectedUserFilter != "TODOS") BentoPrimary.copy(alpha = 0.1f) else Color.White
                            )
                        ) {
                            Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (selectedUserFilter == "TODOS") "Usuario: Todos" else selectedUserFilter,
                                fontSize = 12.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Icon(Icons.Default.ArrowDropDown, contentDescription = null)
                        }

                        DropdownMenu(
                            expanded = showUserDropdown,
                            onDismissRequest = { showUserDropdown = false }
                        ) {
                            availableUsers.forEach { user ->
                                DropdownMenuItem(
                                    text = {
                                        Text(
                                            text = if (user == "TODOS") "Todos los Usuarios" else user,
                                            fontWeight = if (selectedUserFilter == user) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        selectedUserFilter = user
                                        showUserDropdown = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Active Filters Indicator & Clear Button
                if (selectedEventType != EventTypeFilter.TODOS || selectedUserFilter != "TODOS" || searchQuery.isNotBlank()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mostrando ${filteredLogs.size} de ${auditLogs.size} registros",
                            style = MaterialTheme.typography.labelMedium,
                            color = Color(0xFF64748B)
                        )

                        TextButton(
                            onClick = {
                                selectedEventType = EventTypeFilter.TODOS
                                selectedUserFilter = "TODOS"
                                searchQuery = ""
                            }
                        ) {
                            Icon(Icons.Default.FilterAltOff, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Limpiar Filtros", fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // --- AUDIT LOGS LAZY COLUMN ---
        if (filteredLogs.isEmpty()) {
            EmptyStateCard(
                icon = Icons.Default.SearchOff,
                title = "Sin registros para este filtro",
                message = "Intente cambiando los filtros de evento o usuario para visualizar la actividad del sistema."
            )
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("audit_logs_list"),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredLogs, key = { log -> "${log.id}_${log.timestamp}_${log.action}" }) { log ->
                    AuditLogItemCard(log = log, dateFormat = dateFormat)
                }
            }
        }
    }
}

@Composable
fun AuditMetricChip(
    modifier: Modifier = Modifier,
    title: String,
    count: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    bgColor: Color
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bgColor,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
                Text(text = title, fontSize = 10.sp, color = color, fontWeight = FontWeight.Bold)
            }
            Text(
                text = count,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold,
                color = color
            )
        }
    }
}

@Composable
fun AuditLogItemCard(
    log: AuditLogEntity,
    dateFormat: SimpleDateFormat
) {
    val isFailedPin = log.action == "INTENTO_PIN_FALLIDO" || log.details.contains("fallido", ignoreCase = true)
    val isCloseAction = log.action.contains("CIERRE", ignoreCase = true)
    val isResetAction = log.action.contains("RESET", ignoreCase = true) || log.action.contains("REINICIA", ignoreCase = true) || log.action.contains("RESTABLECER", ignoreCase = true)
    val isUserAction = log.action.contains("USUARIO", ignoreCase = true) || log.action.contains("PIN", ignoreCase = true)

    val cardBg = when {
        isFailedPin -> Color(0xFFFEF2F2)
        isResetAction -> Color(0xFFF3E8FF)
        isCloseAction -> Color(0xFFFFFBEB)
        isUserAction -> Color(0xFFEFF6FF)
        else -> Color.White
    }

    val cardBorder = when {
        isFailedPin -> Color(0xFFFCA5A5)
        isResetAction -> Color(0xFFC084FC)
        isCloseAction -> Color(0xFFFCD34D)
        isUserAction -> Color(0xFF93C5FD)
        else -> CardBorderColor
    }

    val iconVector = when {
        isFailedPin -> Icons.Default.Warning
        isResetAction -> Icons.Default.RestartAlt
        isCloseAction -> Icons.Default.Lock
        isUserAction -> Icons.Default.Person
        else -> Icons.Default.ReceiptLong
    }

    val iconTint = when {
        isFailedPin -> Color(0xFFDC2626)
        isResetAction -> Color(0xFF7C3AED)
        isCloseAction -> Color(0xFFD97706)
        isUserAction -> Color(0xFF2563EB)
        else -> BentoPrimary
    }

    Card(
        colors = CardDefaults.cardColors(containerColor = cardBg),
        border = BorderStroke(1.dp, cardBorder),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("audit_log_item_${log.id}")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Header Row: User, Role, Action Icon & Timestamp
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
                        color = iconTint.copy(alpha = 0.15f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(imageVector = iconVector, contentDescription = null, tint = iconTint, modifier = Modifier.size(18.dp))
                        }
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = log.user,
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isFailedPin) Color(0xFF991B1B) else BentoPrimary
                            )

                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = iconTint.copy(alpha = 0.12f)
                            ) {
                                Text(
                                    text = log.role,
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold, color = iconTint),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }

                Text(
                    text = dateFormat.format(Date(log.timestamp)),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            }

            Divider(color = cardBorder.copy(alpha = 0.5f), thickness = 0.8.dp)

            // Action Badge & Device ID
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Acción: ${log.action}",
                    style = MaterialTheme.typography.labelMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        color = if (isFailedPin) Color(0xFFB91C1C) else Color(0xFF1E293B)
                    )
                )

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = Color(0xFFF1F5F9)
                ) {
                    Text(
                        text = "📱 ${log.deviceId}",
                        style = MaterialTheme.typography.labelSmall.copy(color = Color(0xFF475569)),
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            // Detailed Description Text
            Text(
                text = log.details,
                style = MaterialTheme.typography.bodyMedium,
                color = if (isFailedPin) Color(0xFF7F1D1D) else Color(0xFF334155)
            )
        }
    }
}
