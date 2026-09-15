package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.entity.SystemSettingsEntity
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun WebScheduleStatusCustomizerView(
    systemSettings: SystemSettingsEntity,
    onSaveScheduleSettings: (SystemSettingsEntity) -> Unit,
    modifier: Modifier = Modifier
) {
    // Current in-memory editable states initialized from systemSettings
    var masterStatus by remember(systemSettings) { mutableStateOf(systemSettings.webMasterStatus) }
    var is24Hours by remember(systemSettings) { mutableStateOf(systemSettings.webIs24Hours) }
    var openHour by remember(systemSettings) { mutableStateOf(systemSettings.webOpenHour.ifBlank { "08:00" }) }
    var closeHour by remember(systemSettings) { mutableStateOf(systemSettings.webCloseHour.ifBlank { "22:30" }) }
    var activeDaysStr by remember(systemSettings) { mutableStateOf(systemSettings.webActiveDays.ifBlank { "LUN,MAR,MIE,JUE,VIE,SAB,DOM" }) }
    var closedMessage by remember(systemSettings) {
        mutableStateOf(
            systemSettings.webClosedMessage.ifBlank {
                "Nuestro horario de atención es de 08:00 a 22:30. Los pedidos se reanudarán al abrir."
            }
        )
    }

    // Feedback notification state
    var showSavedSnackbar by remember { mutableStateOf<String?>(null) }

    // Dialog state for hour pickers
    var showOpenHourDialog by remember { mutableStateOf(false) }
    var showCloseHourDialog by remember { mutableStateOf(false) }

    // Real-Time Clock with 1-second dynamic update
    var currentDeviceTime by remember { mutableStateOf(Date()) }
    LaunchedEffect(Unit) {
        while (true) {
            currentDeviceTime = Date()
            delay(1000L)
        }
    }

    val timeFormatter = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }
    val dateFormatter = remember { SimpleDateFormat("EEEE, d 'de' MMMM 'de' yyyy", Locale("es", "GT")) }
    val dayOfWeekFormatter = remember { SimpleDateFormat("EEE", Locale.US) }

    val formattedTime = timeFormatter.format(currentDeviceTime)
    val formattedDate = dateFormatter.format(currentDeviceTime).replaceFirstChar { it.uppercase() }
    val rawCurrentDay = dayOfWeekFormatter.format(currentDeviceTime).uppercase()

    // Map device day code to app standard: LUN, MAR, MIE, JUE, VIE, SAB, DOM
    val currentDayCode = when {
        rawCurrentDay.startsWith("MON") -> "LUN"
        rawCurrentDay.startsWith("TUE") -> "MAR"
        rawCurrentDay.startsWith("WED") -> "MIE"
        rawCurrentDay.startsWith("THU") -> "JUE"
        rawCurrentDay.startsWith("FRI") -> "VIE"
        rawCurrentDay.startsWith("SAT") -> "SAB"
        rawCurrentDay.startsWith("SUN") -> "DOM"
        else -> "LUN"
    }

    val activeDaysList = remember(activeDaysStr) {
        activeDaysStr.split(",").map { it.trim().uppercase() }.filter { it.isNotBlank() }.toSet()
    }

    // Dynamic Operating State Calculation in Real-Time
    val calculatedOperatingState = remember(
        masterStatus, is24Hours, openHour, closeHour, activeDaysList, currentDeviceTime, currentDayCode
    ) {
        evaluateOperatingState(
            masterStatus = masterStatus,
            is24Hours = is24Hours,
            openHour = openHour,
            closeHour = closeHour,
            activeDays = activeDaysList,
            currentDate = currentDeviceTime,
            currentDayCode = currentDayCode
        )
    }

    // Pulsing animation for active status beacon
    val infiniteTransition = rememberInfiniteTransition(label = "beacon")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        // Confirmation banner when saved
        AnimatedVisibility(visible = showSavedSnackbar != null) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF15803D),
                shadowElevation = 4.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = showSavedSnackbar ?: "",
                        color = Color.White,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { showSavedSnackbar = null }) {
                        Icon(Icons.Default.Close, contentDescription = "Cerrar", tint = Color.White)
                    }
                }
            }
        }

        // Section Title & Description
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Control Operativo & Horarios Web",
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = "Gestiona la recepción de pedidos en la carta digital y sincroniza con Firebase",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // ==========================================
        // 1. MONITOR DE ESTADO EN TIEMPO REAL
        // ==========================================
        val isOpen = calculatedOperatingState.isOpen
        val statusBgColor = if (isOpen) Color(0xFFDCFCE7) else Color(0xFFFEE2E2)
        val statusBorderColor = if (isOpen) Color(0xFF22C55E) else Color(0xFFEF4444)
        val statusTextColor = if (isOpen) Color(0xFF15803D) else Color(0xFFB91C1C)

        Card(
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = statusBgColor),
            border = BorderStroke(2.dp, statusBorderColor),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth().testTag("monitor_estado_tiempo_real")
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                // Top Row: Dynamic Badge + Clock
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Dynamic Status Beacon Badge
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(30.dp))
                            .background(Color.White.copy(alpha = 0.85f))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = statusBorderColor,
                            modifier = Modifier
                                .size(14.dp)
                                .scale(if (isOpen) pulseScale else 1f)
                        ) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isOpen) "🟢 ABIERTO A PEDIDOS" else "🔴 CERRADO TEMPORALMENTE",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Black,
                                letterSpacing = 0.5.sp
                            ),
                            color = statusTextColor
                        )
                    }

                    // Live Digital Clock
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Icon(
                                Icons.Default.AccessTime,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = formattedTime,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Current Device Date
                Text(
                    text = "📅 $formattedDate",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.75f)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Operational Status Explanation
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White.copy(alpha = 0.95f),
                    border = BorderStroke(1.dp, statusBorderColor.copy(alpha = 0.4f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isOpen) Icons.Default.Storefront else Icons.Default.LockClock,
                            contentDescription = null,
                            tint = statusTextColor,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = calculatedOperatingState.title,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = statusTextColor
                            )
                            Text(
                                text = calculatedOperatingState.description,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.DarkGray
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // 2. CONTROL MAESTRO INMEDIATO
        // ==========================================
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Tune,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Control Maestro Inmediato",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Text(
                    text = "Establece el comportamiento de apertura y cierre para la carta web y pedidos",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 3 Options: ABIERTO, AUTOMATICO, CERRADO
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MasterStatusOptionRow(
                        title = "🟢 Abierto Incondicional",
                        subtitle = "Fuerza la apertura inmediata sin importar la hora o el día",
                        isSelected = masterStatus == "ABIERTO",
                        accentColor = Color(0xFF16A34A),
                        onClick = { masterStatus = "ABIERTO" }
                    )

                    MasterStatusOptionRow(
                        title = "🔄 Modo Automático (Recomendado)",
                        subtitle = "Abre y cierra la recepción de pedidos según el horario y días configurados",
                        isSelected = masterStatus == "AUTOMATICO",
                        accentColor = MaterialTheme.colorScheme.primary,
                        onClick = { masterStatus = "AUTOMATICO" }
                    )

                    MasterStatusOptionRow(
                        title = "🔴 Cerrado Inmediato",
                        subtitle = "Fuerza el cierre inmediato y activa los avisos de restricción en la carta y carrito",
                        isSelected = masterStatus == "CERRADO",
                        accentColor = Color(0xFFDC2626),
                        onClick = { masterStatus = "CERRADO" }
                    )
                }
            }
        }

        // ==========================================
        // 3. MODALIDAD 24 HORAS ININTERRUMPIDA
        // ==========================================
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer,
                        modifier = Modifier.size(42.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                Icons.Default.AllInclusive,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Modalidad 24 Horas Ininterrumpida",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Servicio continuo de cocina y pedidos sin límite de horario",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Switch(
                    checked = is24Hours,
                    onCheckedChange = { is24Hours = it },
                    modifier = Modifier.testTag("switch_24_horas")
                )
            }
        }

        // ==========================================
        // 4. HORARIO HABITUAL (APERTURA Y CIERRE)
        // ==========================================
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Horario Habitual (Apertura y Cierre)",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Text(
                    text = "Soporta turnos diurnos estándar y turnos nocturnos que cruzan la medianoche",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Selector Hora de Apertura
                    TimePickerCard(
                        title = "Hora de Apertura",
                        timeString = openHour,
                        icon = Icons.Default.LightMode,
                        iconColor = Color(0xFFD97706),
                        enabled = !is24Hours,
                        onClick = { showOpenHourDialog = true },
                        modifier = Modifier.weight(1f)
                    )

                    // Selector Hora de Cierre
                    TimePickerCard(
                        title = "Hora de Cierre",
                        timeString = closeHour,
                        icon = Icons.Default.DarkMode,
                        iconColor = Color(0xFF4F46E5),
                        enabled = !is24Hours,
                        onClick = { showCloseHourDialog = true },
                        modifier = Modifier.weight(1f)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Shift analysis badge (Overnight vs Daytime)
                val isOvernight = remember(openHour, closeHour) {
                    checkIsOvernight(openHour, closeHour)
                }
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (is24Hours) {
                        MaterialTheme.colorScheme.surfaceVariant
                    } else if (isOvernight) {
                        Color(0xFFEDE9FE)
                    } else {
                        Color(0xFFFEF3C7)
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (is24Hours) Icons.Default.AllInclusive else if (isOvernight) Icons.Default.Nightlight else Icons.Default.WbSunny,
                            contentDescription = null,
                            tint = if (is24Hours) MaterialTheme.colorScheme.onSurfaceVariant else if (isOvernight) Color(0xFF6D28D9) else Color(0xFFB45309),
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = if (is24Hours) {
                                "Modo 24 horas habilitado: Los horarios específicos se omiten."
                            } else if (isOvernight) {
                                "Turno Nocturno detectado: La hora de cierre ($closeHour) cruza la medianoche del día siguiente."
                            } else {
                                "Turno Diurno estándar: Abierto desde las $openHour hasta las $closeHour del mismo día."
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = if (is24Hours) MaterialTheme.colorScheme.onSurfaceVariant else if (isOvernight) Color(0xFF6D28D9) else Color(0xFFB45309)
                        )
                    }
                }
            }
        }

        // ==========================================
        // 5. DÍAS DE ATENCIÓN DE LA SEMANA
        // ==========================================
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Días de Atención de la Semana",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                    }
                }
                Text(
                    text = "Selecciona qué días se admiten pedidos (sincronizado con el calendario del dispositivo)",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Fast Action Buttons: "Todos" / "Lun - Vie"
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            activeDaysStr = "LUN,MAR,MIE,JUE,VIE,SAB,DOM"
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.DoneAll, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Todos (7 días)", style = MaterialTheme.typography.labelMedium)
                    }

                    OutlinedButton(
                        onClick = {
                            activeDaysStr = "LUN,MAR,MIE,JUE,VIE"
                        },
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Lun - Vie", style = MaterialTheme.typography.labelMedium)
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 7 Interactive Day Buttons
                val daysDefinition = listOf(
                    Triple("LUN", "Lunes", "L"),
                    Triple("MAR", "Martes", "M"),
                    Triple("MIE", "Miércoles", "M"),
                    Triple("JUE", "Jueves", "J"),
                    Triple("VIE", "Viernes", "V"),
                    Triple("SAB", "Sábado", "S"),
                    Triple("DOM", "Domingo", "D")
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    daysDefinition.forEach { (code, fullName, initial) ->
                        val isDayActive = activeDaysList.contains(code)
                        val isToday = currentDayCode == code

                        DayButtonChip(
                            code = code,
                            initial = initial,
                            fullName = fullName,
                            isActive = isDayActive,
                            isToday = isToday,
                            onToggle = {
                                val currentSet = activeDaysList.toMutableSet()
                                if (isDayActive) {
                                    if (currentSet.size > 1) { // keep at least 1 day
                                        currentSet.remove(code)
                                    }
                                } else {
                                    currentSet.add(code)
                                }
                                activeDaysStr = currentSet.joinToString(",")
                            }
                        )
                    }
                }
            }
        }

        // ==========================================
        // 6. MENSAJE PERSONALIZADO DE CIERRE
        // ==========================================
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.EditNote,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Mensaje Personalizado de Cierre",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
                Text(
                    text = "Texto visible para los clientes en la carta y en el carrito cuando el restaurante se encuentra cerrado",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = closedMessage,
                    onValueChange = { closedMessage = it },
                    label = { Text("Mensaje de cierre para comensales") },
                    placeholder = { Text("Ej: Nuestro horario de atención es de 08:00 a 22:30...") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth().testTag("input_mensaje_cierre")
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TextButton(
                        onClick = {
                            closedMessage = "Nuestro horario de atención es de $openHour a $closeHour. Los pedidos se reanudarán al abrir."
                        }
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sugerir texto según horario", style = MaterialTheme.typography.labelSmall)
                    }

                    Text(
                        text = "${closedMessage.length} caracteres",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Live Preview of the closed alert box
                Text(
                    text = "Vista previa en carta digital:",
                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFFFF1F2),
                    border = BorderStroke(1.dp, Color(0xFFFECDD3)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Text(text = "🕒", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Restaurante Cerrado Actualmente",
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color(0xFF9F1239)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = closedMessage,
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFF881337)
                            )
                        }
                    }
                }
            }
        }

        // ==========================================
        // 7. GUARDADO Y APLICACIÓN INMEDIATA
        // ==========================================
        Button(
            onClick = {
                val updatedOpeningHoursStr = if (is24Hours) {
                    "Lunes a Domingo: 24 Horas"
                } else {
                    "${activeDaysList.joinToString("-")}: $openHour - $closeHour"
                }

                val updatedSettings = systemSettings.copy(
                    webMasterStatus = masterStatus,
                    webIs24Hours = is24Hours,
                    webOpenHour = openHour,
                    webCloseHour = closeHour,
                    webActiveDays = activeDaysStr,
                    webClosedMessage = closedMessage,
                    openingHours = updatedOpeningHoursStr
                )

                onSaveScheduleSettings(updatedSettings)
                showSavedSnackbar = "¡Horarios y estado web guardados y sincronizados con éxito!"
            },
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(54.dp)
                .testTag("btn_guardar_horarios_estado")
        ) {
            Icon(Icons.Default.Save, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "💾 Guardar Horarios y Estado",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Dialog for Open Hour Selector
    if (showOpenHourDialog) {
        QuickTimePickerDialog(
            title = "Seleccionar Hora de Apertura",
            initialTime = openHour,
            onDismiss = { showOpenHourDialog = false },
            onConfirm = { selected ->
                openHour = selected
                showOpenHourDialog = false
            }
        )
    }

    // Dialog for Close Hour Selector
    if (showCloseHourDialog) {
        QuickTimePickerDialog(
            title = "Seleccionar Hora de Cierre",
            initialTime = closeHour,
            onDismiss = { showCloseHourDialog = false },
            onConfirm = { selected ->
                closeHour = selected
                showCloseHourDialog = false
            }
        )
    }
}

// -------------------------------------------------------------
// HELPER COMPOSABLES & LOGIC
// -------------------------------------------------------------

@Composable
private fun MasterStatusOptionRow(
    title: String,
    subtitle: String,
    isSelected: Boolean,
    accentColor: Color,
    onClick: () -> Unit
) {
    val borderColor = if (isSelected) accentColor else MaterialTheme.colorScheme.outlineVariant
    val containerColor = if (isSelected) accentColor.copy(alpha = 0.08f) else Color.Transparent

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = containerColor,
        border = BorderStroke(if (isSelected) 2.dp else 1.dp, borderColor),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RadioButton(
                selected = isSelected,
                onClick = onClick,
                colors = RadioButtonDefaults.colors(selectedColor = accentColor)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isSelected) accentColor else MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun TimePickerCard(
    title: String,
    timeString: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = if (enabled) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f) else Color(0xFFF3F4F6),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        modifier = modifier.clickable(enabled = enabled) { onClick() }
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, tint = if (enabled) iconColor else Color.Gray, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (enabled) MaterialTheme.colorScheme.onSurfaceVariant else Color.Gray
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (enabled) MaterialTheme.colorScheme.surface else Color(0xFFE5E7EB),
                border = BorderStroke(1.dp, if (enabled) MaterialTheme.colorScheme.primary.copy(alpha = 0.5f) else Color.Transparent)
            ) {
                Text(
                    text = timeString,
                    style = MaterialTheme.typography.titleLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace
                    ),
                    color = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = if (enabled) "Toca para cambiar" else "Deshabilitado (24h)",
                style = MaterialTheme.typography.labelSmall,
                color = if (enabled) MaterialTheme.colorScheme.primary else Color.Gray
            )
        }
    }
}

@Composable
private fun DayButtonChip(
    code: String,
    initial: String,
    fullName: String,
    isActive: Boolean,
    isToday: Boolean,
    onToggle: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onToggle() }
    ) {
        Surface(
            shape = CircleShape,
            color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
            border = if (isToday) BorderStroke(2.5.dp, Color(0xFFF59E0B)) else null,
            modifier = Modifier.size(42.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = initial,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = if (isActive) Color.White else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = code,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = if (isActive) MaterialTheme.colorScheme.primary else Color.Gray
        )

        if (isToday) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                color = Color(0xFFF59E0B)
            ) {
                Text(
                    text = "Hoy",
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp, fontWeight = FontWeight.Black),
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                )
            }
        } else {
            Spacer(modifier = Modifier.height(14.dp))
        }
    }
}

@Composable
private fun QuickTimePickerDialog(
    title: String,
    initialTime: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit
) {
    val parts = initialTime.split(":")
    var hour by remember { mutableStateOf(parts.getOrNull(0)?.toIntOrNull() ?: 8) }
    var minute by remember { mutableStateOf(parts.getOrNull(1)?.toIntOrNull() ?: 0) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(text = title, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
        },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Large Time Display
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.primaryContainer,
                    modifier = Modifier.padding(vertical = 12.dp)
                ) {
                    Text(
                        text = String.format("%02d:%02d", hour, minute),
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Monospace
                        ),
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 10.dp)
                    )
                }

                // Increment / Decrement controls
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    // Hours column
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Hora (0-23)", style = MaterialTheme.typography.labelSmall)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { hour = (hour - 1 + 24) % 24 }) {
                                Icon(Icons.Default.Remove, contentDescription = "-1 hora")
                            }
                            Text(
                                text = String.format("%02d", hour),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(onClick = { hour = (hour + 1) % 24 }) {
                                Icon(Icons.Default.Add, contentDescription = "+1 hora")
                            }
                        }
                    }

                    // Minutes column
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("Minutos (0-59)", style = MaterialTheme.typography.labelSmall)
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(onClick = { minute = (minute - 15 + 60) % 60 }) {
                                Icon(Icons.Default.Remove, contentDescription = "-15 min")
                            }
                            Text(
                                text = String.format("%02d", minute),
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                modifier = Modifier.padding(horizontal = 8.dp)
                            )
                            IconButton(onClick = { minute = (minute + 15) % 60 }) {
                                Icon(Icons.Default.Add, contentDescription = "+15 min")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Presets
                Text("Preajustes Rápidos:", style = MaterialTheme.typography.labelSmall, modifier = Modifier.align(Alignment.Start))
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val presets = listOf(Pair(7, 0), Pair(8, 0), Pair(12, 0), Pair(22, 0), Pair(22, 30), Pair(0, 0))
                    presets.forEach { (h, m) ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .weight(1f)
                                .clickable {
                                    hour = h
                                    minute = m
                                }
                        ) {
                            Text(
                                text = String.format("%02d:%02d", h, m),
                                style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(vertical = 6.dp)
                            )
                        }
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(String.format("%02d:%02d", hour, minute)) }) {
                Text("Aceptar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

private fun checkIsOvernight(openHourStr: String, closeHourStr: String): Boolean {
    val openParts = openHourStr.split(":").mapNotNull { it.toIntOrNull() }
    val closeParts = closeHourStr.split(":").mapNotNull { it.toIntOrNull() }
    val openMins = (openParts.getOrNull(0) ?: 8) * 60 + (openParts.getOrNull(1) ?: 0)
    val closeMins = (closeParts.getOrNull(0) ?: 22) * 60 + (closeParts.getOrNull(1) ?: 30)
    return closeMins < openMins
}

private data class OperatingStateEvaluation(
    val isOpen: Boolean,
    val title: String,
    val description: String
)

private fun evaluateOperatingState(
    masterStatus: String,
    is24Hours: Boolean,
    openHour: String,
    closeHour: String,
    activeDays: Set<String>,
    currentDate: Date,
    currentDayCode: String
): OperatingStateEvaluation {
    if (masterStatus == "ABIERTO") {
        return OperatingStateEvaluation(
            isOpen = true,
            title = "Forzado Manual: Abierto por Gerencia",
            description = "El restaurante está forzado a recibir pedidos las 24 horas sin importar el reloj."
        )
    }

    if (masterStatus == "CERRADO") {
        return OperatingStateEvaluation(
            isOpen = false,
            title = "Forzado Manual: Cerrado por Gerencia",
            description = "El restaurante ha sido cerrado manualmente. La carta y el carrito no aceptan pedidos."
        )
    }

    // Modo Automático
    if (is24Hours) {
        return OperatingStateEvaluation(
            isOpen = true,
            title = "Modo Automático: Servicio Continuo 24 Horas",
            description = "El restaurante permanece abierto día y noche sin límite de horario."
        )
    }

    // Check Day of Week
    if (!activeDays.contains(currentDayCode)) {
        return OperatingStateEvaluation(
            isOpen = false,
            title = "Modo Automático: Cerrado Hoy",
            description = "Hoy ($currentDayCode) el restaurante no presta servicio por día de descanso programado."
        )
    }

    // Check Open / Close hours
    val openParts = openHour.split(":").mapNotNull { it.toIntOrNull() }
    val closeParts = closeHour.split(":").mapNotNull { it.toIntOrNull() }
    val openMins = (openParts.getOrNull(0) ?: 8) * 60 + (openParts.getOrNull(1) ?: 0)
    val closeMins = (closeParts.getOrNull(0) ?: 22) * 60 + (closeParts.getOrNull(1) ?: 30)

    val calendar = Calendar.getInstance()
    calendar.time = currentDate
    val currentMins = calendar.get(Calendar.HOUR_OF_DAY) * 60 + calendar.get(Calendar.MINUTE)

    val isWithinTime = if (closeMins > openMins) {
        // Daytime shift
        currentMins in openMins until closeMins
    } else {
        // Overnight shift (e.g. 18:00 to 02:00)
        currentMins >= openMins || currentMins < closeMins
    }

    return if (isWithinTime) {
        OperatingStateEvaluation(
            isOpen = true,
            title = "Modo Automático: Abierto Hoy",
            description = "Horario de atención en curso: de $openHour a $closeHour."
        )
    } else {
        OperatingStateEvaluation(
            isOpen = false,
            title = "Modo Automático: Cerrado Actualmente",
            description = "Fuera del horario de servicio. Próxima apertura a las $openHour."
        )
    }
}
