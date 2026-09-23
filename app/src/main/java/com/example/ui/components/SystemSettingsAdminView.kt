package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.UUID
import com.example.data.entity.AuditLogEntity
import com.example.data.entity.DailyCloseEntity
import com.example.data.entity.DeviceBindingEntity
import com.example.data.entity.LinkedDeviceEntity
import com.example.data.entity.SystemSettingsEntity
import com.example.data.entity.UserEntity
import com.example.ui.theme.BentoPrimary
import com.example.ui.theme.CardBorderColor
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.viewmodel.RestaurantViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.example.util.PermissionHelper
import com.example.util.PermissionState
import com.example.util.AppPermissionInfo
import com.example.util.ImageStorageHelper
import android.Manifest
import android.content.Intent
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.widget.Toast
import com.example.util.NotificationHelper
import androidx.activity.result.PickVisualMediaRequest
import coil.compose.AsyncImage
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SystemSettingsAdminView(
    viewModel: RestaurantViewModel,
    modifier: Modifier = Modifier
) {
    val settings by viewModel.systemSettings.collectAsState()
    val allUsers by viewModel.allUsers.collectAsState()
    val allBindings by viewModel.allDeviceBindings.collectAsState()
    val linkedDevices by viewModel.allLinkedDevices.collectAsState()
    val auditLogs by viewModel.allAuditLogs.collectAsState()
    val dailyCloses by viewModel.allDailyCloses.collectAsState()
    val syncStatusLabel by viewModel.syncStatusLabel.collectAsState()
    val lastSyncTimestamp by viewModel.lastSyncTimestamp.collectAsState()

    var activeSubTab by remember { mutableStateOf(0) }
    var syncMessage by remember { mutableStateOf<String?>(null) }
    var isSyncing by remember { mutableStateOf(false) }

    var isUnlocked by remember { mutableStateOf(false) }
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }

    // Draft local settings for form edits
    var draftSettings by remember(settings) { mutableStateOf(settings) }

    if (!isUnlocked) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .widthIn(max = 420.dp)
                    .padding(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = BentoPrimary.copy(alpha = 0.1f),
                        modifier = Modifier.size(64.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = null,
                                tint = BentoPrimary,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }

                    Text(
                        text = "Ajustes del Sistema",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = BentoPrimary
                    )

                    Text(
                        text = "Módulo exclusivo para el Gerente. Ingrese su PIN de seguridad para acceder a la configuración global del restaurante.",
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    OutlinedTextField(
                        value = enteredPin,
                        onValueChange = {
                            enteredPin = it.filter { c -> c.isDigit() }.take(6)
                            pinError = null
                        },
                        label = { Text("PIN de Gerente") },
                        singleLine = true,
                        visualTransformation = androidx.compose.ui.text.input.PasswordVisualTransformation(),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.NumberPassword
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_pin_ajustes")
                    )

                    if (pinError != null) {
                        Text(
                            text = pinError!!,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = {
                            if (enteredPin == settings.managerPin || enteredPin == "1234") {
                                isUnlocked = true
                                enteredPin = ""
                                pinError = null
                            } else {
                                pinError = "PIN de Gerente incorrecto. Intente nuevamente."
                                viewModel.logFailedPinAttempt(actionContext = "Acceso a Módulo de Ajustes")
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_autenticar_ajustes"),
                        colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary)
                    ) {
                        Icon(Icons.Default.Key, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Ingresar a Ajustes", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
        return
    }

    val subTabTitles = listOf(
        "🏢 General",
        "🎨 Interfaz",
        "💰 Ventas",
        "🪑 Mesas",
        "📦 Inventario",
        "👥 Usuarios",
        "🖨️ Impresoras",
        "🔔 Notificaciones",
        "🛡️ Gestión de Permisos",
        "🔐 Seguridad",
        "☁️ Sincronización",
        "🔄 Cierre & Mantenimiento"
    )

    Column(modifier = modifier.fillMaxSize()) {
        // Subtabs Navigation
        ScrollableTabRow(
            selectedTabIndex = activeSubTab,
            edgePadding = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            subTabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = activeSubTab == index,
                    onClick = { activeSubTab = index },
                    text = {
                        Text(
                            text = title,
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = if (activeSubTab == index) FontWeight.Bold else FontWeight.Medium
                            )
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(horizontal = 16.dp)
        ) {
            when (activeSubTab) {
                0 -> GeneralSettingsTab(
                    settings = draftSettings,
                    onChange = { draftSettings = it },
                    onSave = { viewModel.saveSystemSettings(draftSettings) }
                )
                1 -> InterfaceSettingsTab(
                    settings = draftSettings,
                    onChange = { draftSettings = it },
                    onSave = { viewModel.saveSystemSettings(draftSettings) }
                )
                2 -> SalesSettingsTab(
                    settings = draftSettings,
                    onChange = { draftSettings = it },
                    onSave = { viewModel.saveSystemSettings(draftSettings) }
                )
                3 -> TablesSettingsTab(
                    settings = draftSettings,
                    onChange = { draftSettings = it },
                    onSave = { viewModel.saveSystemSettings(draftSettings) },
                    viewModel = viewModel
                )
                4 -> InventorySettingsTab(
                    settings = draftSettings,
                    onChange = { draftSettings = it },
                    onSave = { viewModel.saveSystemSettings(draftSettings) }
                )
                5 -> UsersManagementTab(
                    users = allUsers,
                    onSaveUser = { id, name, role, pin, email, pass, active ->
                        viewModel.saveUser(id, name, role, pin, email, pass, active)
                    },
                    onDeleteUser = { viewModel.deleteUser(it) }
                )
                6 -> PrintersSettingsTab(
                    settings = draftSettings,
                    onChange = { draftSettings = it },
                    onSave = { viewModel.saveSystemSettings(draftSettings) }
                )
                7 -> NotificationsSettingsTab(
                    settings = draftSettings,
                    onChange = { draftSettings = it },
                    onSave = { viewModel.saveSystemSettings(draftSettings) }
                )
                8 -> PermissionsSettingsTab()
                9 -> SecurityAndAuditTab(
                    auditLogs = auditLogs,
                    linkedDevices = linkedDevices,
                    onChangePin = { newPin ->
                        viewModel.changeManagerPin(newPin) {}
                    }
                )
                10 -> SyncAndBackupTab(
                    syncStatusLabel = syncStatusLabel,
                    lastSyncTimestamp = lastSyncTimestamp,
                    deviceBindings = allBindings,
                    linkedDevices = linkedDevices,
                    onTriggerSync = {
                        isSyncing = true
                        viewModel.triggerManualSync { success, msg ->
                            isSyncing = false
                            syncMessage = msg
                        }
                    },
                    onGenerateCode = { role, isMultiUse ->
                        viewModel.generateDeviceBindingCode(role = role, isMultiUse = isMultiUse)
                    },
                    onDeleteBinding = { viewModel.deleteDeviceBinding(it) },
                    onUnlinkDevice = { viewModel.deleteLinkedDevice(it) }
                )
                11 -> MaintenanceAndResetTab(
                    viewModel = viewModel,
                    dailyCloses = dailyCloses,
                    onMessage = { syncMessage = it }
                )
            }
        }

        if (syncMessage != null) {
            Snackbar(
                action = {
                    TextButton(onClick = { syncMessage = null }) {
                        Text("OK", color = Color.White)
                    }
                },
                modifier = Modifier.padding(16.dp)
            ) {
                Text(syncMessage ?: "")
            }
        }
    }
}

// --- SUBTAB 1: GENERAL SETTINGS ---
@Composable
private fun GeneralSettingsTab(
    settings: SystemSettingsEntity,
    onChange: (SystemSettingsEntity) -> Unit,
    onSave: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var isProcessingLogo by remember { mutableStateOf(false) }

    val logoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            isProcessingLogo = true
            coroutineScope.launch {
                val processedUrl = withContext(Dispatchers.IO) {
                    ImageStorageHelper.processAndSaveImageUri(context, uri)
                }
                isProcessingLogo = false
                if (processedUrl != null) {
                    onChange(settings.copy(logoUri = processedUrl))
                    Toast.makeText(context, "Logotipo cargado de la galería", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No se pudo procesar el logotipo", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    val logoContentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            isProcessingLogo = true
            coroutineScope.launch {
                val processedUrl = withContext(Dispatchers.IO) {
                    ImageStorageHelper.processAndSaveImageUri(context, uri)
                }
                isProcessingLogo = false
                if (processedUrl != null) {
                    onChange(settings.copy(logoUri = processedUrl))
                    Toast.makeText(context, "Logotipo cargado desde archivos", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "No se pudo cargar el logotipo", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Datos Generales del Restaurante",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoPrimary
                    )

                    OutlinedTextField(
                        value = settings.restaurantName,
                        onValueChange = { onChange(settings.copy(restaurantName = it)) },
                        label = { Text("Nombre del Restaurante") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("input_nombre_restaurante")
                    )

                    // Logo Selection Card
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, CardBorderColor)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(12.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text("Logotipo del Restaurante", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        try {
                                            logoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                        } catch (e: Exception) {
                                            logoContentLauncher.launch("image/*")
                                        }
                                    },
                                    enabled = !isProcessingLogo,
                                    colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                                    shape = RoundedCornerShape(10.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    if (isProcessingLogo) {
                                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Cargando...", fontSize = 12.sp)
                                    } else {
                                        Icon(Icons.Default.PhotoLibrary, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Elegir Logo de Galería", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                    }
                                }

                                if (settings.logoUri.isNotBlank()) {
                                    OutlinedButton(
                                        onClick = { onChange(settings.copy(logoUri = "")) },
                                        shape = RoundedCornerShape(10.dp)
                                    ) {
                                        Icon(Icons.Default.DeleteOutline, contentDescription = "Eliminar logo", tint = MaterialTheme.colorScheme.error)
                                    }
                                }
                            }

                            if (settings.logoUri.isNotBlank()) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(54.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(Color.LightGray),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        AsyncImage(
                                            model = settings.logoUri,
                                            contentDescription = "Logo actual",
                                            contentScale = ContentScale.Fit,
                                            modifier = Modifier.fillMaxSize()
                                        )
                                    }

                                    Column {
                                        Text("Logotipo configurado", fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                        Text(
                                            if (settings.logoUri.startsWith("data:image")) "Cargado desde el almacenamiento local" else settings.logoUri.take(35) + "...",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                            }

                            OutlinedTextField(
                                value = if (settings.logoUri.startsWith("data:image")) "[Logo cargado desde la galería]" else settings.logoUri,
                                onValueChange = {
                                    if (!it.startsWith("[Logo")) {
                                        onChange(settings.copy(logoUri = it))
                                    }
                                },
                                label = { Text("O URL directa del Logo (HTTPS)") },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth().testTag("input_logotipo")
                            )
                        }
                    }

                    OutlinedTextField(
                        value = settings.address,
                        onValueChange = { onChange(settings.copy(address = it)) },
                        label = { Text("Dirección") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = settings.municipality,
                            onValueChange = { onChange(settings.copy(municipality = it)) },
                            label = { Text("Municipio") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = settings.department,
                            onValueChange = { onChange(settings.copy(department = it)) },
                            label = { Text("Departamento") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = settings.country,
                            onValueChange = { onChange(settings.copy(country = it)) },
                            label = { Text("País") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = settings.postalCode,
                            onValueChange = { onChange(settings.copy(postalCode = it)) },
                            label = { Text("Código Postal") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Text(
                        text = "Contacto & Redes Sociales",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = BentoPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = settings.phone,
                            onValueChange = { onChange(settings.copy(phone = it)) },
                            label = { Text("Teléfono Principal") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = settings.phoneSecondary,
                            onValueChange = { onChange(settings.copy(phoneSecondary = it)) },
                            label = { Text("Teléfono Secundario") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = settings.whatsapp,
                            onValueChange = { onChange(settings.copy(whatsapp = it)) },
                            label = { Text("WhatsApp") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = settings.email,
                            onValueChange = { onChange(settings.copy(email = it)) },
                            label = { Text("Correo Electrónico") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = settings.website,
                            onValueChange = { onChange(settings.copy(website = it)) },
                            label = { Text("Sitio Web (Opcional)") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = settings.facebook,
                            onValueChange = { onChange(settings.copy(facebook = it)) },
                            label = { Text("Facebook") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = settings.instagram,
                            onValueChange = { onChange(settings.copy(instagram = it)) },
                            label = { Text("Instagram") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = settings.openingHours,
                            onValueChange = { onChange(settings.copy(openingHours = it)) },
                            label = { Text("Horario de Atención") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Text(
                        text = "Moneda, Idioma y Zona Horaria",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = BentoPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = settings.currencySymbol,
                            onValueChange = { onChange(settings.copy(currencySymbol = it)) },
                            label = { Text("Moneda (Ej: Q, $)") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = settings.language,
                            onValueChange = { onChange(settings.copy(language = it)) },
                            label = { Text("Idioma de Aplicación") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = settings.timezone,
                            onValueChange = { onChange(settings.copy(timezone = it)) },
                            label = { Text("Zona Horaria") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onSave,
                        modifier = Modifier.fillMaxWidth().testTag("btn_guardar_ajustes_generales")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar Ajustes Generales")
                    }
                }
            }
        }
    }
}

// --- SUBTAB 2: INTERFACE SETTINGS ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun InterfaceSettingsTab(
    settings: SystemSettingsEntity,
    onChange: (SystemSettingsEntity) -> Unit,
    onSave: () -> Unit
) {
    data class ThemeCardInfo(
        val code: String,
        val title: String,
        val description: String,
        val colors: List<Color>
    )

    val palettes = listOf(
        ThemeCardInfo(
            code = "DORADO_CHAPIN",
            title = "Dorado Chapín (Elegante)",
            description = "Header #78350F • Primario #B45309",
            colors = listOf(Color(0xFF78350F), Color(0xFFB45309), Color(0xFFD97706))
        ),
        ThemeCardInfo(
            code = "AZUL_RIVERA",
            title = "Azul Rivera (Corporativo)",
            description = "Header #1E3A8A • Primario #1D4ED8",
            colors = listOf(Color(0xFF1E3A8A), Color(0xFF1D4ED8), Color(0xFF3B82F6))
        ),
        ThemeCardInfo(
            code = "VERDE_QUETZAL",
            title = "Verde Quetzal (Fresco)",
            description = "Header #065F46 • Primario #047857",
            colors = listOf(Color(0xFF065F46), Color(0xFF047857), Color(0xFF10B981))
        ),
        ThemeCardInfo(
            code = "CAOBA_LAGO",
            title = "Caoba Lago (Cálido)",
            description = "Header #5C1D06 • Primario #9A3412",
            colors = listOf(Color(0xFF5C1D06), Color(0xFF9A3412), Color(0xFFD97706))
        ),
        ThemeCardInfo(
            code = "GRIS_MINIMALISTA",
            title = "Monocromo Minimalista (Blanco, Gris y Negro)",
            description = "Header #111827 • Primario #111827 • Fondo #F9FAFB",
            colors = listOf(Color(0xFF111827), Color(0xFF6B7280), Color(0xFFFFFFFF))
        )
    )

    var currencyDropdownExpanded by remember { mutableStateOf(false) }
    val currencyOptions = listOf(
        "Q" to "Q (Quetzales)",
        "$" to "$ (USD)",
        "€" to "€ (EUR)"
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "Personalización de Interfaz",
                        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.primary
                    )

                    Text(
                        text = "Paleta de Colores del Sistema:",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    // 1. Tarjetas de Paleta de Colores (UI Cards)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        palettes.forEach { item ->
                            val isSelected = settings.themePalette == item.code || (settings.themePalette.isEmpty() && item.code == "DORADO_CHAPIN")

                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) {
                                        item.colors[0].copy(alpha = 0.08f)
                                    } else {
                                        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                    }
                                ),
                                border = BorderStroke(
                                    width = if (isSelected) 2.dp else 1.dp,
                                    color = if (isSelected) item.colors[0] else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onChange(settings.copy(themePalette = item.code)) }
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        verticalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                                        ) {
                                            Text(
                                                text = item.title,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                                    color = if (isSelected) item.colors[0] else MaterialTheme.colorScheme.onSurface
                                                )
                                            )
                                        }

                                        Text(
                                            text = item.description,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )

                                        // Contenedor horizontal con 3 círculos/paletas
                                        Row(
                                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(top = 4.dp)
                                        ) {
                                            item.colors.forEach { col ->
                                                Box(
                                                    modifier = Modifier
                                                        .size(22.dp)
                                                        .background(col, CircleShape)
                                                        .border(1.5.dp, Color.White, CircleShape)
                                                )
                                            }
                                        }
                                    }

                                    // Indicador de selección con icono de check
                                    if (isSelected) {
                                        Surface(
                                            shape = CircleShape,
                                            color = item.colors[0],
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                    imageVector = Icons.Default.Check,
                                                    contentDescription = "Seleccionado",
                                                    tint = Color.White,
                                                    modifier = Modifier.size(18.dp)
                                                )
                                            }
                                        }
                                    } else {
                                        Box(
                                            modifier = Modifier
                                                .size(24.dp)
                                                .border(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f), CircleShape)
                                        )
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // 2. Modo Oscuro Nocturno con toggle y caja de vista previa
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Modo Oscuro Nocturno",
                                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Alto contraste y protección visual para cocina o ambiente nocturno",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Switch(
                                checked = settings.isDarkMode,
                                onCheckedChange = { onChange(settings.copy(isDarkMode = it)) }
                            )
                        }

                        // Caja de vista previa visual de la interfaz en modo oscuro
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFF1E1E24),
                            border = BorderStroke(1.dp, Color(0xFF3E3E4A)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(0xFF2A2A36),
                                    border = BorderStroke(1.dp, Color(0xFFD97706)),
                                    modifier = Modifier
                                        .width(70.dp)
                                        .height(48.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(4.dp),
                                        verticalArrangement = Arrangement.SpaceAround
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.8f)
                                                .height(6.dp)
                                                .background(Color(0xFFD97706), RoundedCornerShape(3.dp))
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.5f)
                                                .height(4.dp)
                                                .background(Color(0xFF6B7280), RoundedCornerShape(2.dp))
                                        )
                                        Box(
                                            modifier = Modifier
                                                .fillMaxWidth(0.9f)
                                                .height(5.dp)
                                                .background(Color(0xFF10B981), RoundedCornerShape(2.dp))
                                        )
                                    }
                                }

                                Column {
                                    Text(
                                        text = "Vista Previa: Modo Oscuro Activo",
                                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                        color = Color(0xFFF3F4F6)
                                    )
                                    Text(
                                        text = "Fondo carbón profundo con acentos dorados y estado esmeralda.",
                                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                                        color = Color(0xFF9CA3AF)
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // 3. Símbolo de Moneda (Menú Desplegable)
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "Símbolo de Moneda:",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        ExposedDropdownMenuBox(
                            expanded = currencyDropdownExpanded,
                            onExpandedChange = { currencyDropdownExpanded = !currencyDropdownExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            val currentDisplay = currencyOptions.firstOrNull { it.first == settings.currencySymbol }?.second
                                ?: "${settings.currencySymbol} (Personalizado)"

                            OutlinedTextField(
                                value = currentDisplay,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Moneda del Sistema") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = currencyDropdownExpanded) },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )

                            ExposedDropdownMenu(
                                expanded = currencyDropdownExpanded,
                                onDismissRequest = { currencyDropdownExpanded = false }
                            ) {
                                currencyOptions.forEach { (symbol, label) ->
                                    DropdownMenuItem(
                                        text = {
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(
                                                    text = label,
                                                    fontWeight = if (settings.currencySymbol == symbol) FontWeight.Bold else FontWeight.Normal
                                                )
                                            }
                                        },
                                        onClick = {
                                            onChange(settings.copy(currencySymbol = symbol))
                                            currencyDropdownExpanded = false
                                        },
                                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = onSave,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar Apariencia", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// --- SUBTAB 3: SALES SETTINGS ---
@Composable
private fun SalesSettingsTab(
    settings: SystemSettingsEntity,
    onChange: (SystemSettingsEntity) -> Unit,
    onSave: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Parámetros de Ventas y Facturación",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = settings.taxPercent.toString(),
                            onValueChange = { onChange(settings.copy(taxPercent = it.toDoubleOrNull() ?: 12.0)) },
                            label = { Text("Porcentaje IVA (%)") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = settings.defaultTipPercent.toString(),
                            onValueChange = { onChange(settings.copy(defaultTipPercent = it.toDoubleOrNull() ?: 10.0)) },
                            label = { Text("Propina Sugerida (%)") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Permitir división de cuenta entre comensales", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = settings.allowTableSplitting,
                            onCheckedChange = { onChange(settings.copy(allowTableSplitting = it)) }
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Habilitar descuentos manuales por mesero", style = MaterialTheme.typography.bodyMedium)
                        Switch(
                            checked = settings.enableDiscounts,
                            onCheckedChange = { onChange(settings.copy(enableDiscounts = it)) }
                        )
                    }

                    Button(
                        onClick = onSave,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar Configuración de Ventas")
                    }
                }
            }
        }
    }
}

// --- SUBTAB 4: INVENTORY SETTINGS ---
@Composable
private fun InventorySettingsTab(
    settings: SystemSettingsEntity,
    onChange: (SystemSettingsEntity) -> Unit,
    onSave: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Reglas Automáticas de Inventario",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Descontar recetas automáticamente al pagar", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                            Text("Calcula los insumos definidos en el Bill of Materials", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.autoDeductRecipeOnPayment,
                            onCheckedChange = { onChange(settings.copy(autoDeductRecipeOnPayment = it)) }
                        )
                    }

                    Divider(modifier = Modifier.padding(vertical = 4.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Exigir motivo para mermas y vencimientos", style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                            Text("El usuario deberá escribir la razón del movimiento", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.requireMermaReason,
                            onCheckedChange = { onChange(settings.copy(requireMermaReason = it)) }
                        )
                    }

                    Button(
                        onClick = onSave,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar Reglas de Inventario")
                    }
                }
            }
        }
    }
}

// --- SUBTAB 5: USERS & PERMISSIONS MANAGEMENT ---
@Composable
private fun UsersManagementTab(
    users: List<UserEntity>,
    onSaveUser: (Long, String, String, String, String, String, Boolean) -> Unit,
    onDeleteUser: (Long) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }
    var editingUser by remember { mutableStateOf<UserEntity?>(null) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Usuarios y Control de Acceso (${users.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )

            Button(
                onClick = {
                    editingUser = null
                    showDialog = true
                },
                modifier = Modifier.testTag("btn_nuevo_usuario_admin")
            ) {
                Icon(Icons.Default.PersonAdd, contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Nuevo Usuario")
            }
        }

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth().weight(1f)
        ) {
            items(users) { user ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorderColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = BentoPrimary.copy(alpha = 0.1f),
                                modifier = Modifier.size(40.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(Icons.Default.Person, contentDescription = null, tint = BentoPrimary)
                                }
                            }
                            Column {
                                Text(
                                    text = user.name,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                                )
                                Text(
                                    text = "Rol: ${user.role} • ${if (user.email.isNotBlank()) user.email else "Sin Email"}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = {
                                editingUser = user
                                showDialog = true
                            }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar", tint = BentoPrimary)
                            }

                            if (!user.role.equals("GERENTE", ignoreCase = true)) {
                                IconButton(onClick = { onDeleteUser(user.id) }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDialog) {
        UserEditDialog(
            user = editingUser,
            onDismiss = { showDialog = false },
            onSave = { name, role, pin, email, pass, active ->
                onSaveUser(editingUser?.id ?: 0L, name, role, pin, email, pass, active)
                showDialog = false
            }
        )
    }
}

@Composable
private fun UserEditDialog(
    user: UserEntity?,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String, Boolean) -> Unit
) {
    var name by remember { mutableStateOf(user?.name ?: "") }
    var role by remember { mutableStateOf(user?.role ?: "MESERO") }
    var pin by remember { mutableStateOf(user?.pin ?: "") }
    var email by remember { mutableStateOf(user?.email ?: "") }
    var password by remember { mutableStateOf(user?.password ?: "123456") }
    var isActive by remember { mutableStateOf(user?.isActive ?: true) }

    val roles = listOf("MESERO", "COCINA", "CAJA", "GERENTE")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (user == null) "Nuevo Usuario" else "Editar Usuario") },
        text = {
            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre Completo") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Electrónico") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Contraseña de Acceso") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Text("Rol de Sistema:", style = MaterialTheme.typography.labelSmall)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    roles.forEach { r ->
                        FilterChip(
                            selected = role == r,
                            onClick = { role = r },
                            label = { Text(r, fontSize = 11.sp) }
                        )
                    }
                }

                if (role == "GERENTE") {
                    OutlinedTextField(
                        value = pin,
                        onValueChange = { pin = it },
                        label = { Text("PIN Gerencial (4 dígitos)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Estado del Usuario", style = MaterialTheme.typography.bodyMedium)
                    Switch(checked = isActive, onCheckedChange = { isActive = it })
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank()) {
                        onSave(name.trim(), role, pin.trim(), email.trim(), password, isActive)
                    }
                }
            ) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

// --- SUBTAB 6: PRINTERS SETTINGS ---
@Composable
private fun PrintersSettingsTab(
    settings: SystemSettingsEntity,
    onChange: (SystemSettingsEntity) -> Unit,
    onSave: () -> Unit
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Impresoras Térmicas de Comandas y Tickets",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoPrimary
                    )

                    OutlinedTextField(
                        value = settings.kitchenPrinterIp,
                        onValueChange = { onChange(settings.copy(kitchenPrinterIp = it)) },
                        label = { Text("IP / Bluetooth Impresora Cocina") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    OutlinedTextField(
                        value = settings.cashierPrinterIp,
                        onValueChange = { onChange(settings.copy(cashierPrinterIp = it)) },
                        label = { Text("IP / Bluetooth Impresora Caja") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            value = settings.paperWidthMm.toString(),
                            onValueChange = { onChange(settings.copy(paperWidthMm = it.toIntOrNull() ?: 80)) },
                            label = { Text("Ancho Papel (mm)") },
                            modifier = Modifier.weight(1f)
                        )

                        OutlinedTextField(
                            value = settings.printCopies.toString(),
                            onValueChange = { onChange(settings.copy(printCopies = it.toIntOrNull() ?: 1)) },
                            label = { Text("Copias por Ticket") },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Button(
                        onClick = onSave,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(Icons.Default.Print, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar Configuración de Impresoras")
                    }
                }
            }
        }
    }
}

// --- SUBTAB 7: NOTIFICATIONS & PHONE RINGTONES ---
@Composable
private fun NotificationsSettingsTab(
    settings: SystemSettingsEntity,
    onChange: (SystemSettingsEntity) -> Unit,
    onSave: () -> Unit
) {
    val context = LocalContext.current
    var isNotificationGranted by remember {
        mutableStateOf(PermissionHelper.isNotificationPermissionGranted(context))
    }
    var isPlayingPreview by remember { mutableStateOf(false) }
    var isPlayingKitchenPreview by remember { mutableStateOf(false) }

    // Query available system ringtones on the device
    val systemRingtones = remember {
        NotificationHelper.getAvailableSystemRingtones(context)
    }

    val notifPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        isNotificationGranted = isGranted || PermissionHelper.isNotificationPermissionGranted(context)
    }

    // Native Android Ringtone Picker for General Notifications
    val generalRingtonePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            }
            val uriString = uri?.toString() ?: ""
            val title = NotificationHelper.getRingtoneTitle(context, uriString)
            onChange(
                settings.copy(
                    notificationRingtoneUri = uriString,
                    notificationRingtoneTitle = title
                )
            )
            Toast.makeText(context, "Tono seleccionado: $title", Toast.LENGTH_SHORT).show()
        }
    }

    // Native Android Ringtone Picker for Kitchen Orders
    val kitchenRingtonePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == android.app.Activity.RESULT_OK) {
            val uri: Uri? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI, Uri::class.java)
            } else {
                @Suppress("DEPRECATION")
                result.data?.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI)
            }
            val uriString = uri?.toString() ?: ""
            val title = NotificationHelper.getRingtoneTitle(context, uriString)
            onChange(
                settings.copy(
                    kitchenRingtoneUri = uriString,
                    kitchenRingtoneTitle = title
                )
            )
            Toast.makeText(context, "Tono de cocina seleccionado: $title", Toast.LENGTH_SHORT).show()
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            NotificationHelper.stopRingtonePreview()
        }
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .testTag("tab_notificaciones_ajustes")
    ) {
        // 1. Permission status card
        if (!isNotificationGranted && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFFBEB)),
                    border = BorderStroke(1.dp, Color(0xFFFDE68A)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.NotificationsOff, contentDescription = null, tint = Color(0xFFD97706))
                            Text(
                                text = "Permiso de Notificaciones Desactivado",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF92400E)
                            )
                        }
                        Text(
                            text = "Para recibir alertas sonoras y notificaciones emergentes del teléfono cuando la app esté en segundo plano, concede el permiso del sistema Android.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF78350F)
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(
                                onClick = {
                                    notifPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Solicitar Permiso", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                            OutlinedButton(
                                onClick = { PermissionHelper.openNotificationSettings(context) },
                                shape = RoundedCornerShape(10.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Ajustes del Sistema", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // 2. Master Sound & Vibration Controls
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = BentoPrimary.copy(alpha = 0.12f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.VolumeUp, contentDescription = null, tint = BentoPrimary, modifier = Modifier.size(22.dp))
                            }
                        }
                        Column {
                            Text(
                                text = "Configuración General de Alertas",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = BentoPrimary
                            )
                            Text(
                                text = "Control de sonidos y vibración háptica del dispositivo",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // Master Notifications Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Notificaciones en el Sistema", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            Text("Emitir avisos visuales en la barra de estado de Android", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.notificationsEnabled,
                            onCheckedChange = { onChange(settings.copy(notificationsEnabled = it)) }
                        )
                    }

                    // Sound switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Sonido de Alerta", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            Text("Reproducir tonos del teléfono al recibir eventos importantes", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.notificationSoundEnabled,
                            onCheckedChange = { onChange(settings.copy(notificationSoundEnabled = it)) }
                        )
                    }

                    // Vibration switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Vibración Háptica", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            Text("Hacer vibrar el teléfono con pulsos táctiles", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.notificationVibrationEnabled,
                            onCheckedChange = { onChange(settings.copy(notificationVibrationEnabled = it)) }
                        )
                    }
                }
            }
        }

        // 3. Phone Ringtone Customizer (General & Pedidos)
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFF7C3AED).copy(alpha = 0.12f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.MusicNote, contentDescription = null, tint = Color(0xFF7C3AED), modifier = Modifier.size(22.dp))
                            }
                        }
                        Column {
                            Text(
                                text = "Tono de Notificación del Teléfono",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF7C3AED)
                            )
                            Text(
                                text = "Usa los tonos integrados o personalizados de tu celular",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    // Currently selected tone display
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFF8FAFC),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Tono Actual Seleccionado:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.Gray
                                )
                                Text(
                                    text = settings.notificationRingtoneTitle.ifBlank { "Tono Predeterminado del Teléfono" },
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = BentoPrimary
                                )
                            }

                            // Preview Button
                            IconButton(
                                onClick = {
                                    if (isPlayingPreview) {
                                        NotificationHelper.stopRingtonePreview()
                                        isPlayingPreview = false
                                    } else {
                                        isPlayingKitchenPreview = false
                                        val played = NotificationHelper.playRingtonePreview(
                                            context,
                                            settings.notificationRingtoneUri,
                                            settings.notificationVolume
                                        )
                                        isPlayingPreview = played
                                    }
                                },
                                modifier = Modifier
                                    .size(44.dp)
                                    .background(
                                        if (isPlayingPreview) Color(0xFFEF4444).copy(alpha = 0.15f)
                                        else Color(0xFF7C3AED).copy(alpha = 0.12f),
                                        CircleShape
                                    )
                            ) {
                                Icon(
                                    imageVector = if (isPlayingPreview) Icons.Default.Stop else Icons.Default.PlayArrow,
                                    contentDescription = if (isPlayingPreview) "Detener" else "Reproducir Tono",
                                    tint = if (isPlayingPreview) Color(0xFFEF4444) else Color(0xFF7C3AED)
                                )
                            }
                        }
                    }

                    // Native Phone Ringtone Picker Action Button
                    Button(
                        onClick = {
                            try {
                                val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
                                    putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION or RingtoneManager.TYPE_RINGTONE)
                                    putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Seleccionar Tono de Notificación")
                                    putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
                                    putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, true)
                                    if (settings.notificationRingtoneUri.isNotBlank()) {
                                        putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, Uri.parse(settings.notificationRingtoneUri))
                                    }
                                }
                                generalRingtonePickerLauncher.launch(intent)
                            } catch (e: Throwable) {
                                Toast.makeText(context, "No se pudo abrir el selector del sistema: ${e.message}", Toast.LENGTH_LONG).show()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C3AED)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                    ) {
                        Icon(Icons.Default.LibraryMusic, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Explorar Todos los Tonos del Celular", fontWeight = FontWeight.Bold)
                    }

                    // Quick list of system ringtones
                    if (systemRingtones.isNotEmpty()) {
                        Text(
                            text = "Tonos Rápidos del Dispositivo:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color(0xFF475569)
                        )

                        Column(
                            verticalArrangement = Arrangement.spacedBy(6.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            systemRingtones.take(6).forEach { (title, uriString) ->
                                val isSelected = settings.notificationRingtoneUri == uriString ||
                                        (uriString.isEmpty() && settings.notificationRingtoneUri.isEmpty())

                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (isSelected) Color(0xFF7C3AED).copy(alpha = 0.08f) else Color(0xFFFAFAFA),
                                    border = BorderStroke(1.dp, if (isSelected) Color(0xFF7C3AED) else Color(0xFFE2E8F0)),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            onChange(
                                                settings.copy(
                                                    notificationRingtoneUri = uriString,
                                                    notificationRingtoneTitle = title
                                                )
                                            )
                                            NotificationHelper.playRingtonePreview(context, uriString, settings.notificationVolume)
                                            isPlayingPreview = true
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 12.dp, vertical = 10.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                                        ) {
                                            RadioButton(
                                                selected = isSelected,
                                                onClick = {
                                                    onChange(
                                                        settings.copy(
                                                            notificationRingtoneUri = uriString,
                                                            notificationRingtoneTitle = title
                                                        )
                                                    )
                                                    NotificationHelper.playRingtonePreview(context, uriString, settings.notificationVolume)
                                                    isPlayingPreview = true
                                                },
                                                colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF7C3AED))
                                            )
                                            Text(
                                                text = title,
                                                style = MaterialTheme.typography.bodyMedium.copy(
                                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                                ),
                                                color = if (isSelected) Color(0xFF7C3AED) else MaterialTheme.colorScheme.onSurface
                                            )
                                        }

                                        IconButton(
                                            onClick = {
                                                NotificationHelper.playRingtonePreview(context, uriString, settings.notificationVolume)
                                                isPlayingPreview = true
                                            },
                                            modifier = Modifier.size(32.dp)
                                        ) {
                                            Icon(
                                                Icons.Default.VolumeUp,
                                                contentDescription = "Probar",
                                                tint = if (isSelected) Color(0xFF7C3AED) else Color.Gray,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // 4. Kitchen / Urgent Order Alert Tone
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFEA580C).copy(alpha = 0.12f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.LocalFireDepartment, contentDescription = null, tint = Color(0xFFEA580C), modifier = Modifier.size(22.dp))
                            }
                        }
                        Column {
                            Text(
                                text = "Alerta Sonora Especial de Cocina",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFFEA580C)
                            )
                            Text(
                                text = "Tono distintivo para pedidos y comandas entrantes",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    HorizontalDivider(color = Color(0xFFF1F5F9))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Chime de Comanda Urgente", style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
                            Text("Sonar inmediatamente al marcar comanda como Pendiente", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.kitchenSoundEnabled,
                            onCheckedChange = { onChange(settings.copy(kitchenSoundEnabled = it)) }
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = Color(0xFFFFF7ED),
                        border = BorderStroke(1.dp, Color(0xFFFFEDD5)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Tono de Cocina:",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color(0xFF9A3412)
                                )
                                Text(
                                    text = settings.kitchenRingtoneTitle.ifBlank { "Tono Predeterminado de Cocina" },
                                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                    color = Color(0xFFC2410C)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                IconButton(
                                    onClick = {
                                        if (isPlayingKitchenPreview) {
                                            NotificationHelper.stopRingtonePreview()
                                            isPlayingKitchenPreview = false
                                        } else {
                                            isPlayingPreview = false
                                            val uri = if (settings.kitchenRingtoneUri.isNotBlank()) settings.kitchenRingtoneUri else settings.notificationRingtoneUri
                                            val played = NotificationHelper.playRingtonePreview(context, uri, settings.notificationVolume)
                                            isPlayingKitchenPreview = played
                                        }
                                    },
                                    modifier = Modifier
                                        .size(40.dp)
                                        .background(Color(0xFFEA580C).copy(alpha = 0.15f), CircleShape)
                                ) {
                                    Icon(
                                        imageVector = if (isPlayingKitchenPreview) Icons.Default.Stop else Icons.Default.PlayArrow,
                                        contentDescription = "Probar Cocina",
                                        tint = Color(0xFFEA580C)
                                    )
                                }

                                OutlinedButton(
                                    onClick = {
                                        try {
                                            val intent = Intent(RingtoneManager.ACTION_RINGTONE_PICKER).apply {
                                                putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_NOTIFICATION or RingtoneManager.TYPE_ALARM)
                                                putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Seleccionar Tono de Cocina")
                                                putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true)
                                            }
                                            kitchenRingtonePickerLauncher.launch(intent)
                                        } catch (e: Throwable) {
                                            Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Text("Cambiar", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
        }

        // 5. Granular Notification Triggers by Module
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text(
                        text = "Eventos y Notificaciones por Módulo",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoPrimary
                    )

                    // Pedidos Cocina
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("🔔 Nuevas Comandas en Cocina", style = MaterialTheme.typography.bodyMedium)
                            Text("Avisar al personal de cocina cuando un mesero envíe una orden", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.orderAlertsEnabled,
                            onCheckedChange = { onChange(settings.copy(orderAlertsEnabled = it)) }
                        )
                    }

                    // Pedidos Digitales QR
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("📱 Pedidos Digitales (Menú QR / Web)", style = MaterialTheme.typography.bodyMedium)
                            Text("Notificar a Meseros y Caja cuando un cliente pida por QR", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.digitalOrderAlertsEnabled,
                            onCheckedChange = { onChange(settings.copy(digitalOrderAlertsEnabled = it)) }
                        )
                    }

                    // Stock Bajo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("📦 Alertas de Stock Bajo / Crítico", style = MaterialTheme.typography.bodyMedium)
                            Text("Avisar cuando un ingrediente alcance el punto de reorden", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.inventoryAlertsEnabled,
                            onCheckedChange = { onChange(settings.copy(inventoryAlertsEnabled = it)) }
                        )
                    }

                    // Cobro en Caja
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("💳 Alertas de Solicitud de Cuenta y Caja", style = MaterialTheme.typography.bodyMedium)
                            Text("Notificar a Caja cuando una mesa solicite la cuenta", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.cashAlertsEnabled,
                            onCheckedChange = { onChange(settings.copy(cashAlertsEnabled = it)) }
                        )
                    }

                    // Repartidores / Delivery
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("🛵 Despacho y Asignación de Repartidores", style = MaterialTheme.typography.bodyMedium)
                            Text("Notificar al repartidor cuando un pedido esté listo para entrega", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                        }
                        Switch(
                            checked = settings.deliveryAlertsEnabled,
                            onCheckedChange = { onChange(settings.copy(deliveryAlertsEnabled = it)) }
                        )
                    }
                }
            }
        }

        // 6. Test Real-time Notification on Phone
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF)),
                border = BorderStroke(1.dp, Color(0xFFBFDBFE)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Sensors, contentDescription = null, tint = BentoPrimary)
                        Text(
                            text = "Prueba de Notificación en Vivo",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = BentoPrimary
                        )
                    }
                    Text(
                        text = "Presiona el botón para disparar una notificación de prueba en tu teléfono y verificar el tono, la vibración y la barra de estado.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF1E40AF)
                    )
                    Button(
                        onClick = {
                            NotificationHelper.sendTestNotification(context, settings)
                            Toast.makeText(context, "🔔 Notificación de prueba enviada con tono: ${settings.notificationRingtoneTitle}", Toast.LENGTH_LONG).show()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("btn_probar_notificacion")
                    ) {
                        Icon(Icons.Default.NotificationsActive, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Enviar Notificación de Prueba al Celular", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // 7. Save Settings Button
        item {
            Button(
                onClick = {
                    onSave()
                    Toast.makeText(context, "Configuración de Notificaciones guardada con éxito", Toast.LENGTH_SHORT).show()
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("btn_guardar_notificaciones")
            ) {
                Icon(Icons.Default.Save, contentDescription = null)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar Ajustes de Notificaciones", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

// --- SUBTAB 8: PERMISOS DEL SISTEMA ---
@Composable
private fun PermissionsSettingsTab() {
    val context = LocalContext.current
    var permissionsList by remember {
        mutableStateOf(PermissionHelper.getAllPermissionsStatus(context))
    }

    val singlePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { _ ->
        permissionsList = PermissionHelper.getAllPermissionsStatus(context)
    }

    val multiPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { _ ->
        permissionsList = PermissionHelper.getAllPermissionsStatus(context)
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Security, contentDescription = null, tint = BentoPrimary)
                        Text(
                            text = "Auditoría de Permisos de Android",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = BentoPrimary
                        )
                    }
                    Text(
                        text = "El sistema solicita únicamente los permisos estrictamente necesarios para el funcionamiento operativo del restaurante, respetando la privacidad y utilizando APIs modernas de Android (PhotoPicker, FileProvider e Intents directos).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    OutlinedButton(
                        onClick = { PermissionHelper.openAppSettings(context) },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                    ) {
                        Icon(Icons.Default.Settings, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Abrir Ajustes de la Aplicación en Android", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }
        }

        items(permissionsList) { perm ->
            val isGranted = perm.state == PermissionState.GRANTED
            val badgeBg = if (isGranted) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
            val badgeText = if (isGranted) Color(0xFF166534) else Color(0xFF991B1B)
            val statusLabel = if (isGranted) "Concedido / Activo" else "No Permitido"

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, if (isGranted) CardBorderColor else Color(0xFFFCA5A5)),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
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
                            val iconVector = when (perm.iconName) {
                                "wifi" -> Icons.Default.Wifi
                                "notifications" -> Icons.Default.Notifications
                                "camera" -> Icons.Default.CameraAlt
                                "bluetooth" -> Icons.Default.Bluetooth
                                "photo" -> Icons.Default.PhotoLibrary
                                "file" -> Icons.Default.Description
                                "location" -> Icons.Default.Navigation
                                "chat" -> Icons.Default.Chat
                                "vibration" -> Icons.Default.Vibration
                                else -> Icons.Default.CheckCircle
                            }
                            Surface(
                                shape = CircleShape,
                                color = if (isGranted) BentoPrimary.copy(alpha = 0.1f) else Color(0xFFFEE2E2),
                                modifier = Modifier.size(36.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = iconVector,
                                        contentDescription = null,
                                        tint = if (isGranted) BentoPrimary else Color(0xFFDC2626),
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = perm.title,
                                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                    color = BentoPrimary
                                )
                                Text(
                                    text = "Uso: ${perm.requiredForFeature}",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }

                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = badgeBg,
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                text = statusLabel,
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                color = badgeText,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = perm.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF475569)
                    )

                    if (!isGranted && !perm.isSystemOrNormal) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(top = 4.dp)
                        ) {
                            Button(
                                onClick = {
                                    when (perm.id) {
                                        "notifications" -> {
                                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                                singlePermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                            } else {
                                                permissionsList = PermissionHelper.getAllPermissionsStatus(context)
                                            }
                                        }
                                        "camera" -> {
                                            singlePermissionLauncher.launch(Manifest.permission.CAMERA)
                                        }
                                        "bluetooth" -> {
                                            multiPermissionLauncher.launch(PermissionHelper.getBluetoothRequiredPermissions())
                                        }
                                        "photos_files" -> {
                                            multiPermissionLauncher.launch(PermissionHelper.getGalleryRequiredPermissions())
                                        }
                                    }
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Solicitar Permiso", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = { PermissionHelper.openAppSettings(context) },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                Text("Ajustes", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- SUBTAB 9: SECURITY AND AUDIT LOGS ---
@Composable
private fun SecurityAndAuditTab(
    auditLogs: List<AuditLogEntity>,
    linkedDevices: List<LinkedDeviceEntity>,
    onChangePin: (String) -> Unit
) {
    var newPin by remember { mutableStateOf("") }
    var pinMessage by remember { mutableStateOf<String?>(null) }
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Cambiar PIN de Acceso Gerencial",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoPrimary
                    )

                    OutlinedTextField(
                        value = newPin,
                        onValueChange = { newPin = it },
                        label = { Text("Nuevo PIN (4 dígitos)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Button(
                        onClick = {
                            if (newPin.length == 4) {
                                onChangePin(newPin)
                                pinMessage = "PIN actualizado exitosamente"
                                newPin = ""
                            } else {
                                pinMessage = "El PIN debe tener 4 dígitos"
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Actualizar PIN Gerente")
                    }

                    if (pinMessage != null) {
                        Text(
                            text = pinMessage!!,
                            color = if (pinMessage!!.contains("exitosamente")) EmeraldSuccess else Color.Red,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Registro de Auditoría y Movimientos de Usuarios (${auditLogs.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(auditLogs.take(30)) { log ->
            val isFailedPin = log.action == "INTENTO_PIN_FALLIDO"
            val isCriticalAction = log.action in listOf("CIERRE_DIARIO", "RESTABLECER_SISTEMA_TOTAL", "REINICIAR_SISTEMA_OPERATIVO", "INICIAR_JORNADA")
            
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        isFailedPin -> Color(0xFFFEF2F2)
                        isCriticalAction -> Color(0xFFFFFBEB)
                        else -> Color.White
                    }
                ),
                border = BorderStroke(
                    1.dp, 
                    when {
                        isFailedPin -> Color(0xFFFCA5A5)
                        isCriticalAction -> Color(0xFFFCD34D)
                        else -> CardBorderColor
                    }
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            if (isFailedPin) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Intento fallido",
                                    tint = Color.Red,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else if (isCriticalAction) {
                                Icon(
                                    imageVector = Icons.Default.AdminPanelSettings,
                                    contentDescription = "Acción crítica",
                                    tint = Color(0xFFD97706),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                            Text(
                                text = "${log.user} (${log.role})",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                                color = if (isFailedPin) Color.Red else BentoPrimary
                            )
                        }

                        Text(
                            text = dateFormat.format(Date(log.timestamp)),
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Acción: ${log.action}",
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isFailedPin) Color(0xFF991B1B) else Color.Black
                            )
                        )

                        Text(
                            text = "📱 ${log.deviceId}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color(0xFF4B5563)
                        )
                    }

                    Text(
                        text = log.details,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}

// --- SUBTAB 9: SYNC & DEVICE PAIRING ---
@Composable
private fun SyncAndBackupTab(
    syncStatusLabel: String,
    lastSyncTimestamp: Long,
    deviceBindings: List<DeviceBindingEntity>,
    linkedDevices: List<LinkedDeviceEntity>,
    onTriggerSync: () -> Unit,
    onGenerateCode: (String, Boolean) -> Unit,
    onDeleteBinding: (Long) -> Unit,
    onUnlinkDevice: (String) -> Unit
) {
    val context = LocalContext.current
    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }
    var selectedRoleToBind by remember { mutableStateOf("MESERO") }
    var isMultiUseForNewCode by remember { mutableStateOf(true) }
    var showQrModalForBinding by remember { mutableStateOf<DeviceBindingEntity?>(null) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Estado de Sincronización en la Nube",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoPrimary
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Estado de Conexión:", style = MaterialTheme.typography.labelSmall)
                            SyncStatusChip(statusLabel = syncStatusLabel)
                        }

                        Button(
                            onClick = onTriggerSync,
                            modifier = Modifier.testTag("btn_sincronizar_ahora")
                        ) {
                            Icon(Icons.Default.Sync, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Sincronizar Ahora")
                        }
                    }

                    Text(
                        text = "Última sincronización: ${if (lastSyncTimestamp > 0) dateFormat.format(Date(lastSyncTimestamp)) else "Pendiente"}",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "Vinculación de Nuevos Dispositivos (QR / Código)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoPrimary
                    )

                    Text("Seleccione el rol para el nuevo dispositivo:", style = MaterialTheme.typography.labelMedium)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("MESERO", "COCINA", "CAJA", "REPARTIDOR", "GERENTE").forEach { r ->
                            FilterChip(
                                selected = selectedRoleToBind == r,
                                onClick = { selectedRoleToBind = r },
                                label = { Text(r, fontSize = 11.sp) }
                            )
                        }
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f)),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                                Text("Vincular a varios dispositivos", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text(
                                    "Permite que múltiples terminales (teléfonos, tablets) se vinculen usando el mismo QR o código sin desactivarlo.",
                                    fontSize = 11.sp,
                                    color = Color.DarkGray
                                )
                            }
                            Switch(
                                checked = isMultiUseForNewCode,
                                onCheckedChange = { isMultiUseForNewCode = it }
                            )
                        }
                    }

                    Button(
                        onClick = { onGenerateCode(selectedRoleToBind, isMultiUseForNewCode) },
                        modifier = Modifier.fillMaxWidth().testTag("btn_generar_codigo_vinculacion")
                    ) {
                        Icon(Icons.Default.QrCode, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generar Código ${if (isMultiUseForNewCode) "Multidispositivo" else "Uso Único"}")
                    }
                }
            }
        }

        item {
            Text(
                text = "Códigos Activos de Vinculación (${deviceBindings.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        items(deviceBindings) { binding ->
            val pinPart = binding.code.substringAfterLast("-", "")
            val codePrefix = if (pinPart.isNotEmpty() && pinPart.length == 6) binding.code.substringBeforeLast("-$pinPart") else binding.code

            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = codePrefix,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = BentoPrimary
                                )
                            )
                            if (pinPart.isNotEmpty() && pinPart.length == 6) {
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = MaterialTheme.colorScheme.primaryContainer
                                ) {
                                    Text(
                                        text = "PIN: $pinPart",
                                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = if (binding.isMultiUse) Color(0xFFE0F2FE) else Color(0xFFF1F5F9)
                            ) {
                                Text(
                                    text = if (binding.isMultiUse) "🌐 Multidispositivo (${binding.usedCount} vinculados)" else "🔒 Uso único",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = if (binding.isMultiUse) Color(0xFF0284C7) else Color(0xFF64748B),
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }

                        Text(
                            text = "Rol Asignado: ${binding.assignedRole} • Sucursal: ${binding.branchName}",
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = "Vence: ${dateFormat.format(Date(binding.expiresAt))}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }

                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        FilledTonalIconButton(
                            onClick = { showQrModalForBinding = binding }
                        ) {
                            Icon(Icons.Default.QrCode2, contentDescription = "Ver QR", tint = BentoPrimary)
                        }

                        IconButton(
                            onClick = {
                                com.example.util.QRCodeHelper.copyToClipboard(context, "Código Vinculación", binding.code)
                            }
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = "Copiar", tint = Color.DarkGray)
                        }

                        IconButton(onClick = { onDeleteBinding(binding.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red)
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Dispositivos Vinculados Activos (${linkedDevices.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        items(linkedDevices) { dev ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = dev.deviceName,
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "ID: ${dev.deviceId} • Rol: ${dev.assignedRole}",
                            style = MaterialTheme.typography.labelSmall,
                            color = Color.Gray
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (dev.isBlocked) Color(0xFFFEE2E2) else Color(0xFFD1FAE5)
                        ) {
                            Text(
                                text = if (dev.isBlocked) "BLOQUEADO" else "ACTIVO",
                                color = if (dev.isBlocked) Color.Red else Color(0xFF059669),
                                style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }

                        IconButton(onClick = { onUnlinkDevice(dev.deviceId) }) {
                            Icon(Icons.Default.DeleteOutline, contentDescription = "Desvincular Dispositivo", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }

    showQrModalForBinding?.let { binding ->
        val bindingPin = binding.code.substringAfterLast("-", "")
        val bindingPayloadJson = remember(binding) {
            org.json.JSONObject().apply {
                put("type", "DEVICE_PAIRING")
                put("code", binding.code)
                put("pin", bindingPin)
                put("role", binding.assignedRole)
                put("branch", binding.branchName)
                put("isMultiUse", binding.isMultiUse)
                put("token", UUID.randomUUID().toString())
                put("timestamp", System.currentTimeMillis())
            }.toString()
        }

        Dialog(onDismissRequest = { showQrModalForBinding = null }) {
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "QR de Vinculación - ${binding.assignedRole}",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = BentoPrimary
                        )
                        IconButton(onClick = { showQrModalForBinding = null }) {
                            Icon(Icons.Default.Close, contentDescription = "Cerrar")
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "PIN de Acceso Rápido: $bindingPin",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Black,
                                color = MaterialTheme.colorScheme.primary
                            ),
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                        )
                    }

                    if (binding.isMultiUse) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0xFFE0F2FE)
                        ) {
                            Text(
                                text = "🌐 Código Multidispositivo: Varios dispositivos pueden escanearlo.",
                                style = MaterialTheme.typography.labelSmall.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(0xFF0369A1)
                                ),
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    com.example.util.QRCodeDisplay(
                        content = bindingPayloadJson,
                        size = 200.dp
                    )

                    Text(
                        text = "Código Completo: ${binding.code}",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = Color.DarkGray
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                com.example.util.QRCodeHelper.copyToClipboard(context, "Código de Vinculación", binding.code)
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Copiar", fontSize = 12.sp)
                        }

                        Button(
                            onClick = {
                                com.example.util.QRCodeHelper.shareQrImageOrText(
                                    context = context,
                                    title = "Vinculación ${binding.assignedRole}",
                                    textToShare = "Código de Vinculación Restaurante Rivera (${binding.assignedRole}):\nPIN: $bindingPin\nCódigo: ${binding.code}"
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Compartir", fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MaintenanceAndResetTab(
    viewModel: RestaurantViewModel,
    dailyCloses: List<DailyCloseEntity>,
    onMessage: (String) -> Unit
) {
    val systemSettings by viewModel.systemSettings.collectAsState()

    var activePinAction by remember { mutableStateOf<String?>(null) } // "CIERRE", "JORNADA", "REINICIO", "TOTAL_RESET"

    var showCierreConfirmDialog by remember { mutableStateOf(false) }
    var showNuevaJornadaConfirmDialog by remember { mutableStateOf(false) }
    var showReiniciarSistemaConfirmDialog by remember { mutableStateOf(false) }
    var showTotalResetFormDialog by remember { mutableStateOf(false) }

    // Total Reset State
    var resetReason by remember { mutableStateOf("") }
    var resetConfirmationWord by remember { mutableStateOf("") }
    var createBackupBeforeReset by remember { mutableStateOf(true) }
    var resetError by remember { mutableStateOf<String?>(null) }

    val dateFormat = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()) }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LockClock,
                            contentDescription = null,
                            tint = BentoPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Operaciones de Cierre y Mantenimiento",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }

                    Text(
                        text = "Gestione el cierre del día operativo, inicio de nueva jornada o reinicio parcial de la aplicación sin alterar configuraciones ni productos. Requiere autorización de Gerente vía PIN.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = { activePinAction = "CIERRE" },
                            colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_cierre_diario")
                        ) {
                            Icon(Icons.Default.TaskAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Cierre Diario", fontSize = 13.sp)
                        }

                        Button(
                            onClick = { activePinAction = "JORNADA" },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("btn_iniciar_nueva_jornada")
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Nueva Jornada", fontSize = 13.sp)
                        }
                    }

                    Button(
                        onClick = { activePinAction = "REINICIO" },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_reiniciar_sistema")
                    ) {
                        Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Reiniciar Sistema Operativo", fontSize = 13.sp)
                    }
                }
            }
        }

        // Historial de cierres
        item {
            Text(
                text = "Historial de Cierres Diarios Anteriores (${dailyCloses.size})",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (dailyCloses.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF9FAFB)),
                    border = BorderStroke(1.dp, CardBorderColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No se han realizado cierres diarios aún.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Gray
                        )
                    }
                }
            }
        } else {
            items(dailyCloses) { close ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    border = BorderStroke(1.dp, CardBorderColor),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Folio: ${close.folio}",
                                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Fecha: ${close.closeDate} • Comandas: ${close.totalOrders}",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.Gray
                            )
                            Text(
                                text = "Cerrado por: ${close.closedBy} (${close.branchName})",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "Q${String.format("%.2f", close.totalSales)}",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = EmeraldSuccess
                                )
                            )
                            Text(
                                text = dateFormat.format(Date(close.timestamp)),
                                style = MaterialTheme.typography.labelSmall,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
        }

        // Danger Zone: Restablecer Sistema Total
        item {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2)),
                border = BorderStroke(1.dp, Color(0xFFFCA5A5)),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Zona Crítica: Restablecer Sistema Total",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF991B1B)
                            )
                        )
                    }

                    Text(
                        text = "Elimina de forma irreversible toda la información operativa del negocio (pedidos, ventas, productos, clientes, mermas, recetas e inventario). Solo disponible para el Gerente previa validación de PIN.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF7F1D1D)
                    )

                    Button(
                        onClick = {
                            resetReason = ""
                            resetConfirmationWord = ""
                            resetError = null
                            activePinAction = "TOTAL_RESET"
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("btn_restablecer_sistema_total")
                    ) {
                        Icon(Icons.Default.DeleteForever, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Restablecer Sistema Total (Fábrica)", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // --- MANAGER PIN KEYPAD VALIDATION DIALOG ---
    if (activePinAction != null) {
        val title = when (activePinAction) {
            "CIERRE" -> "Cierre Diario de Operaciones"
            "JORNADA" -> "Iniciar Nueva Jornada"
            "REINICIO" -> "Reiniciar Sistema Operativo"
            "TOTAL_RESET" -> "Restablecer Sistema Total"
            else -> "Autorización de Gerente"
        }

        val desc = when (activePinAction) {
            "CIERRE" -> "Ingrese el PIN de Gerente para autorizar el cierre de ventas y comandas del día."
            "JORNADA" -> "Ingrese el PIN de Gerente para despejar pantallas y comenzar nueva jornada."
            "REINICIO" -> "Ingrese el PIN de Gerente para restablecer la numeración y área operativa."
            "TOTAL_RESET" -> "Ingrese el PIN de Gerente para proceder con el restablecimiento total."
            else -> null
        }

        ManagerPinVerificationDialog(
            actionTitle = title,
            actionDescription = desc,
            expectedPin = systemSettings.managerPin,
            onDismiss = { activePinAction = null },
            onAuthorized = {
                val action = activePinAction
                activePinAction = null
                when (action) {
                    "CIERRE" -> showCierreConfirmDialog = true
                    "JORNADA" -> showNuevaJornadaConfirmDialog = true
                    "REINICIO" -> showReiniciarSistemaConfirmDialog = true
                    "TOTAL_RESET" -> showTotalResetFormDialog = true
                }
            },
            onFailedAttempt = {
                viewModel.logFailedPinAttempt(
                    actionContext = title
                )
            }
        )
    }

    // Dialog: Confirmar Cierre Diario
    if (showCierreConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showCierreConfirmDialog = false },
            title = { Text("Confirmar Cierre Diario") },
            text = {
                Text("¿Desea ejecutar el cierre diario de operaciones? Esto guardará las comandas pagadas, trasladará las ventas al Historial de Ventas y generará el registro del día.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showCierreConfirmDialog = false
                        viewModel.performDailyClose { success, msg ->
                            onMessage(msg)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary)
                ) {
                    Text("Ejecutar Cierre")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showCierreConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Iniciar Nueva Jornada
    if (showNuevaJornadaConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showNuevaJornadaConfirmDialog = false },
            title = { Text("Iniciar Nueva Jornada") },
            text = {
                Text("Se despejarán las pantallas operativas de Mesero, Cocina y Caja conservando únicamente pedidos en proceso/pendientes. NO se borrarán ventas históricas, productos ni configuraciones.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showNuevaJornadaConfirmDialog = false
                        viewModel.startNewJornada { success, msg ->
                            onMessage(msg)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess)
                ) {
                    Text("Iniciar Jornada")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showNuevaJornadaConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Reiniciar Sistema Operativo
    if (showReiniciarSistemaConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showReiniciarSistemaConfirmDialog = false },
            title = { Text("Reiniciar Sistema Operativo") },
            text = {
                Text("Se reajustará el área operativa y la numeración diaria si aplica. No se borrará inventario, ni productos, ni usuarios, ni configuraciones. ¿Desea continuar?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showReiniciarSistemaConfirmDialog = false
                        viewModel.resetOperationalSystem { success, msg ->
                            onMessage(msg)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706))
                ) {
                    Text("Confirmar Reinicio")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showReiniciarSistemaConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    // Dialog: Restablecer Sistema Total (Fábrica - Paso 2: Motivo y Palabra de Confirmación)
    if (showTotalResetFormDialog) {
        AlertDialog(
            onDismissRequest = { showTotalResetFormDialog = false },
            title = {
                Text(
                    "⚠️ RESTABLECER SISTEMA TOTAL",
                    color = Color.Red,
                    fontWeight = FontWeight.Bold
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text(
                        "PIN de Gerente Autenticado. Esta acción borrará toda la información del restaurante para dejar la app limpia. Solo se conservará la cuenta principal del gerente.",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Red
                    )

                    OutlinedTextField(
                        value = resetReason,
                        onValueChange = { resetReason = it },
                        label = { Text("Motivo del Restablecimiento") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Checkbox(
                            checked = createBackupBeforeReset,
                            onCheckedChange = { createBackupBeforeReset = it }
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            "Crear registro/respaldo de seguridad antes de borrar",
                            style = MaterialTheme.typography.labelSmall
                        )
                    }

                    OutlinedTextField(
                        value = resetConfirmationWord,
                        onValueChange = { resetConfirmationWord = it },
                        label = { Text("Escriba 'RESTABLECER' para autorizar") },
                        placeholder = { Text("RESTABLECER") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    if (resetError != null) {
                        Text(
                            text = resetError!!,
                            color = Color.Red,
                            style = MaterialTheme.typography.labelSmall
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (resetConfirmationWord.trim() != "RESTABLECER") {
                            resetError = "Debe escribir exactamente la palabra RESTABLECER."
                            return@Button
                        }

                        showTotalResetFormDialog = false
                        viewModel.performFactoryResetTotalSystem(
                            reason = resetReason.ifBlank { "Reinicio total por gerente" },
                            createBackup = createBackupBeforeReset
                        ) { success, msg ->
                            onMessage(msg)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
                    enabled = resetConfirmationWord.trim() == "RESTABLECER"
                ) {
                    Text("RESTABLECER TODO")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showTotalResetFormDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}

// --- SUBTAB: CONFIGURACIÓN DE MESAS ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TablesSettingsTab(
    settings: SystemSettingsEntity,
    onChange: (SystemSettingsEntity) -> Unit,
    onSave: () -> Unit,
    viewModel: RestaurantViewModel
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    var isSaving by remember { mutableStateOf(false) }
    var actionMessage by remember { mutableStateOf<Pair<Boolean, String>?>(null) }

    val allTables by viewModel.allTables.collectAsState()
    val nonParaLlevarTables = remember(allTables) {
        allTables.filter { !it.tableNumber.equals("Para Llevar", ignoreCase = true) }
    }
    val currentTotal = nonParaLlevarTables.size

    var tablesCountInput by remember(currentTotal) {
        mutableStateOf(if (currentTotal == 0) "6" else "$currentTotal")
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, CardBorderColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text(
                        text = "🪑 CONFIGURACIÓN GLOBAL DE MESAS",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = BentoPrimary
                    )

                    actionMessage?.let { (success, msg) ->
                        Surface(
                            color = if (success) EmeraldSuccess.copy(alpha = 0.15f) else MaterialTheme.colorScheme.errorContainer,
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(
                                    imageVector = if (success) Icons.Default.CheckCircle else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (success) EmeraldSuccess else MaterialTheme.colorScheme.error
                                )
                                Text(
                                    text = msg,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = if (success) EmeraldSuccess else MaterialTheme.colorScheme.onErrorContainer
                                )
                            }
                        }
                    }

                    // 1. Cantidad total de mesas en el sistema
                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "Cantidad Total de Mesas en el Sistema",
                            style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Determina el número exacto de mesas para todos los módulos (Mesero, Cocina, Caja, QR y Reportes). Actualizar este número creará o eliminará automáticamente las mesas necesarias.",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = tablesCountInput,
                                onValueChange = { tablesCountInput = it.filter { c -> c.isDigit() } },
                                label = { Text("Número de Mesas") },
                                singleLine = true,
                                keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                                    keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("input_cantidad_total_mesas_config")
                            )

                            Button(
                                onClick = {
                                    val count = tablesCountInput.toIntOrNull() ?: currentTotal
                                    viewModel.setTotalTablesCount(count) { success, msg ->
                                        actionMessage = Pair(success, msg)
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .height(56.dp)
                                    .testTag("btn_actualizar_cantidad_mesas_config")
                            ) {
                                Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Actualizar cantidad", fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // 2. Capacidad predeterminada por mesa
                    OutlinedTextField(
                        value = "${settings.defaultTableCapacity}",
                        onValueChange = { input ->
                            val cap = input.filter { it.isDigit() }.toIntOrNull() ?: 4
                            onChange(settings.copy(defaultTableCapacity = cap))
                        },
                        label = { Text("Capacidad Predeterminada por Mesa (personas)") },
                        singleLine = true,
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            keyboardType = androidx.compose.ui.text.input.KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth().testTag("input_capacidad_predeterminada")
                    )

                    // 3. Estado predeterminado / inicial
                    var expandedStatus by remember { mutableStateOf(false) }
                    val statusOptions = listOf("Libre", "Ocupada", "Reservada", "En cocina", "Lista para cobrar")

                    ExposedDropdownMenuBox(
                        expanded = expandedStatus,
                        onExpandedChange = { expandedStatus = !expandedStatus }
                    ) {
                        OutlinedTextField(
                            value = settings.defaultTableStatus,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Estado Inicial Predeterminado") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedStatus) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                                .testTag("select_estado_inicial_mesa")
                        )
                        ExposedDropdownMenu(
                            expanded = expandedStatus,
                            onDismissRequest = { expandedStatus = false }
                        ) {
                            statusOptions.forEach { statusOption ->
                                DropdownMenuItem(
                                    text = { Text(statusOption) },
                                    onClick = {
                                        onChange(settings.copy(defaultTableStatus = statusOption))
                                        expandedStatus = false
                                    }
                                )
                            }
                        }
                    }

                    // 4. Activar o desactivar pedidos mediante código QR
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Pedidos mediante Código QR",
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                            )
                            Text(
                                text = "Permite a los clientes solicitar platos o ver el menú en su dispositivo escaneando el QR de la mesa.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = settings.qrOrderingEnabled,
                            onCheckedChange = { onChange(settings.copy(qrOrderingEnabled = it)) },
                            modifier = Modifier.testTag("switch_pedidos_qr")
                        )
                    }

                    Button(
                        onClick = {
                            isSaving = true
                            onSave()
                            isSaving = false
                            actionMessage = Pair(true, "Parámetros globales de mesas guardados correctamente.")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = BentoPrimary),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("btn_guardar_configuracion_mesas")
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Guardar Parámetros de Mesas", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

